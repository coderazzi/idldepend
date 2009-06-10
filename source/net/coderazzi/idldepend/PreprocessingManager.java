/**
 * File: PreprocessingManager.java
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package net.coderazzi.idldepend;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.coderazzi.idldepend.javacc.generated.ParseException;
import net.coderazzi.idldepend.preprocessor.PreprocessorController;
import net.coderazzi.idldepend.preprocessor.PreprocessorUser;

/**
 * This class manages the preprocessing, storing the tokens in a file
 * ocassionally. It filters the tokens that must be handled by the IDL parser,
 * passing only those belonging to the file being parsed, and not those
 * belonging to included files -unless explicitely said.
 */
class PreprocessingManager extends Thread implements PreprocessorUser {

   /**
    * @param idlFile
    *           the file to preprocess
    * @param uniqueId
    *           an identity that can identify this generation uniquely, to track
    *           dependencies (two generations of the same file with different
    *           parameters will produce different dependencies)
    * @param dependsDir
    *           the directory where the dependency files are stored
    * @param includePath
    *           an string containing the include path
    * @param defines
    *           an string containing all the defines. They must be written in as
    *           the preprocessor expects them: #define symbol [value] In fact,
    *           this string can contain any information, as it is passed to the
    *           preprocessor
    * @param verbose
    *           set to true to get some information during the checking
    * @param debug
    *           set to true to get all the information during the checking
    * @param logger
    *           is the object to use to log any information
    * @param checkScopedIncludes
    *           set to true if includes provided at scope level must be
    *           tracked.
    * @param storePreprocessedFile
    *           set to true if the preprocessing output must be stored in one
    *           file
    * @param storeFull
    *           set to true if the preprocessing output must be complete
    *           (including the content of the included files)
    * @param outputFull
    *           set to true if the stream output must be complete (including the
    *           content of the included files)
    */
   public PreprocessingManager(File idlFile, UniqueDependencyId uniqueId,
         File dependsDir, String includePath, String addedToPrep,
         boolean verbose, boolean debug, Logger logger,
         boolean checkScopedIncludes, 
         boolean storePreprocessedFile, boolean storeFull, boolean outputFull)
         throws IOException {
      this.idlFile = idlFile;
      this.includePath = includePath;
      this.addedToPrep = addedToPrep;
      this.verbose = verbose;
      this.debug = debug;
      this.checkScopedIncludes=checkScopedIncludes;
      this.logger = logger;
      this.storeFull = storeFull;
      this.outputFull = outputFull;

      if (!idlFile.canRead()) {
         throw new FileNotFoundException(idlFile.toString());
      }

      if (storePreprocessedFile) {
         preprocessFile = new File(dependsDir, idlFile.getName() + "."
               + uniqueId.generateUniqueId(idlFile) + ".idl");
      }
   }

   /**
    * Performs the preprocessing
    * 
    * @param output:
    *           the stream to output the read tokens. If it is null, no tokens
    *           are output
    * @param checker:
    *           the DependenciesChecker object handling the dependencies; it can
    *           be null if no dependencies must be checked
    * @param forceStorage
    *           is set to true if the preprocessed file must be stored, even if
    *           it is already updated. If the class was built using
    *           storePreprocessedFile=false on the constructor, this parameter
    *           does nothing
    * @return null if everything were alright. If not, a string showing the
    *         error
    */
   public Exception process(PrintWriter output, DependenciesChecker checker,
         boolean forceStorage) {
      this.output = output;
      this.checker = checker;
      if (preprocessFile != null) {
         boolean store = true;
         if (!forceStorage) {
            long a = idlFile.lastModified();
            long b = preprocessFile.lastModified();
            if ((a != 0L) && (b != 0L) && (a <= b)) {
               store = false;
            }
         }
         if (store) {
            try {
               preprocessFileWriter = new FileWriter(preprocessFile);
            } catch (IOException ex) {
               return ex;
            }
            preprocessPrintWriter = new PrintWriter(preprocessFileWriter);
         }
      }

      if (preprocessPrintWriter != null || output != null) {
         start();
         synchronized (this) {
            while (!resultReady) {
               try {
                  wait();
               } catch (Exception ex) {
               }
            }
         }
      }

      if (preprocessPrintWriter != null) {
         preprocessPrintWriter.close();
         preprocessPrintWriter = null;
         try {
            preprocessFileWriter.close();
         } catch (IOException iex) {
         }
         preprocessFileWriter = null;

         if (buildException != null) {
            preprocessFile.delete();
         }
      }

      return buildException;
   }

   /**
    * Stops the current preprocessing, if any
    */
   public synchronized void stopPreprocessing() {
      if (preprocessorController != null) {
         boolean removeIdl = !resultReady;
         preprocessorController.stopParsing();
         if (removeIdl && (preprocessFile != null)) {
            while (!resultReady) {
               try {
                  wait();
               } catch (InterruptedException iex) {
               }
            }
            preprocessFile.delete();
         }
      }
   }

   /**
    * Returns the preprocessed file, it is null if generatePreprocessFile was
    * null
    */
   public File getPreprocessedFile() {
      return preprocessFile;
   }

   /**
    * Thread public method.
    */
   public void run() {
      if (this == currentThread()) {
         try {
            preprocessorController = new PreprocessorController(this);
            preprocessorController.addPath(includePath);
            preprocessorController.parseInput(idlFile, addedToPrep);
            synchronized (this) {
               resultReady = true;
               notifyAll();
            }
         } catch (Exception ex) {
            error(ex);
         } finally {
            synchronized (this) {
               preprocessorController = null;
            }
         }
      }
   }

   /**
    * Error found by the preprocessor while reading the IDL file
    */
   public void asynchronousException(Exception exception) {
      error(exception);
   }

   /**
    * Method called by the preprocessor/parser when an include statement is
    * read. This allows for excluding types not belonging to the original file
    */
   public void includingFile(String file) throws ParseException {
      if ((preprocessPrintWriter != null) && !storeFull
            && ((inclusion == 0) || (openBlocks > 0))) {
         preprocessPrintWriter.println("#include \"" + file + "\"");
      }
      ++inclusion;
      if (!resultReady && (checker != null)) {
         checker.addSource(new File(file));
      }
      // store information on the file: in special, how many times is included
      // (and is still being included, to trace recursion)
      includedFilesStack.add(file); // to know the last included file
      int included = 1;
      Integer inMap = (Integer) includedFilesInfo.get(file);
      if (inMap != null) {
         included += inMap.intValue();
      }
      if (included >= MAX_RECURSION_DEPTH) {
         throw new ParseException("include recursion too deep");
      }
      includedFilesInfo.put(file, new Integer(included));
   }

   /**
    * Method called by the preprocessor/parser when an include statement has
    * been completed This allows for excluding types not belonging to the
    * original file
    */
   public void fileIncluded() throws ParseException {
      --inclusion;
      if (includedFilesStack.size() == 0) {
         throw new ParseException("Internal IDLDEPEND error");
      }
      String file = (String) includedFilesStack.remove(0);
      Integer inMap = (Integer) includedFilesInfo.remove(file);
      if (inMap == null) {
         throw new ParseException("Internal IDLDEPEND error");
      }
      int included = inMap.intValue() - 1;
      if (included > 0) {
         includedFilesInfo.put(file, new Integer(included));
      }
   }

   /**
    * Preprocessor warning (like redefining a macro)
    */
   public void warning(String message) {
      if (!resultReady && verbose) {
         logger.log("Warning: " + message);
      }
   }

   /**
    * Specific preprocessor warning
    */
   public void unknownDirective(String content) {
      if (!resultReady && debug) {
         logger.log("Unknown directive: " + content);
      }
   }

   /**
    * Token read by the preprocessor, to be passed to the IDL parser
    */
   public void token(String t) {
      if (preprocessPrintWriter != null) {
         // if the preprocess is not stored full,
         // we remove the #line files and all the tokens belonging to included
         // files
         if (storeFull || openBlocks > 0) {
            preprocessPrintWriter.print(t);
         } else if (inclusion == 0) {
            // if storeFull is false, lines #line... are not printed. In
            // addition,
            // the next coming token (end of line token) must be removed as
            // well, to
            // keep the line numbers
            if (t.startsWith("#line")) {
               removeNext = true;
            } else if (removeNext) {
               removeNext = false;
            } else {
               preprocessPrintWriter.print(t);
            }
         }
      }
      if (checkScopedIncludes && ( inclusion == 0)) {
         // next check cannot be included in the preprocessor (or its inclusion
         // is not simple)
         // the main reason is that a symbol like 'BEGIN' could be transformed
         // into '{'
         // In addition, we only track blocks on the top module
         if (t.equals("{")) {
            openBlocks += 1;
         } else if (openBlocks > 0 && t.equals("}")) {
            openBlocks -= 1;
         }
      }
      if ((output != null) && (!resultReady)
            && (outputFull || (openBlocks > 0) || (inclusion == 0))) {
         output.print(t);
      }
   }

   /**
    * Reports an error
    */
   private synchronized void error(Exception ex) {
      if (!resultReady) {
         if (verbose) {
            logger.log(ex.toString());
         }
         if (checker != null) {
            checker.stopDependenciesGeneration(false);
         }
         buildException = ex;
         resultReady = true;
         notifyAll();
      }
   }

   private File idlFile;

   private String includePath, addedToPrep;

   private FileWriter preprocessFileWriter;

   private PrintWriter preprocessPrintWriter, output;

   private PreprocessorController preprocessorController;

   private boolean verbose, debug;
   
   // set to true if it is needed to check includes directives provided 
   // at some scope (i.e., not at top level).
   private boolean checkScopedIncludes;

   private int inclusion = 0;

   // number of blocks open on the top module. It is only increased if
   // the variable checkScopedIncludes is set to true
   private int openBlocks = 0; 

   private Logger logger;

   private List includedFilesStack = new ArrayList();

   private Map includedFilesInfo = new HashMap();

   private DependenciesChecker checker;

   private File preprocessFile;

   private Exception buildException;

   private boolean resultReady, storeFull, outputFull, removeNext;

   private static int MAX_RECURSION_DEPTH = 50;

}
