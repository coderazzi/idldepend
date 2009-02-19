/**
 * File: IDLCheckerTask.java
 * Author: LuisM Pena
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package com.byteslooser.idldepend;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import com.byteslooser.idldepend.idl.VersionsSupporter;

/**
 * Task to execute the compiler transforming IDL specifications into java files.
 * Check the site documentation to verify the supported parameters/attributes.
 */
public class IDLCheckerTask extends Task implements Logger {

   public IDLCheckerTask() {
   }

   /****************************************************************************
    * * METHODS TO SET THE TASK'S ARGUMENTS *
    ***************************************************************************/

   public void setForce(boolean set) {
      force = set;
   }

   public void setFailOnError(boolean set) {
      failOnError = set;
   }

   public void setCheckall(boolean set) {
      checkAll = set;
   }

   public void setVerbose(TaskAttributesHandler.VerboseLevel level) {
      verboseLevel = level;
   }

   public void setSide(TaskAttributesHandler.SideAttribute attribute) {
      side = attribute;
   }

   public void setCompiler(TaskAttributesHandler.CompilerAttribute attribute) {
      compiler = attribute;
   }

   public void setPreprocess(TaskAttributesHandler.PreprocessAttribute attribute) {
      preprocess = attribute;
   }

   public void setAMI(TaskAttributesHandler.AMIAttribute attribute) {
      ami = attribute;
   }

   public void setCallCompiler(
         TaskAttributesHandler.CallCompilerAttribute attribute) {
      callCompiler = attribute;
   }

   public void setDependsdir(File dir) {
      dependsDir = dir;
   }

   public Task createDefine() {
      TaskAttributesHandler.Define newDefine = new TaskAttributesHandler.Define();
      defines.add(newDefine);
      return newDefine;
   }

   public Task createUndefine() {
      TaskAttributesHandler.Undefine newUndefine = new TaskAttributesHandler.Undefine();
      undefines.add(newUndefine);
      return newUndefine;
   }

   public Path createInclude() {
      if (include == null) {
         include = new Path(getProject());
      }
      return include;
   }

   public Path createClasspath() {
      if (classpath == null) {
         classpath = new Path(getProject());
      }
      return classpath;
   }

   public Path createPath() {
      if (path == null) {
         path = new Path(getProject());
      }
      return path;
   }

   public FileSet createFileset() {
      FileSet ret = new FileSet();
      idlFiles.add(ret);
      return ret;
   }

   public Task createPackage() {
      Task ret = new Translator.PackageTask();
      packages.add(ret);
      return ret;
   }

   public Task createTranslate() {
      Task ret = new Translator.TranslateTask();
      translates.add(ret);
      return ret;
   }

   public void setFile(File file) {
      idlFile = file;
   }

   public void setTargetdir(File dir) {
      targetDir = dir;
   }

   public void setCompilerPath(String compiler) {
      idlCompiler = compiler;
   }

   public Commandline.Argument createArg() {
      return commandLine.createArgument();
   }

   /**
    * Execute the task, once the arguments have been set
    */
   public void execute() {
      if (verboseLevel.isVerbose() || verboseLevel.isBasic()) {
         log(VERSION);
      }

      if (preprocess.useFull()) {
         checkAll = true;
      }

      // converts the introduced parameters, modifying the command line
      convertParameters();

      VersionsSupporter versionsSupporter = new VersionsSupporter(compiler,
            side);

      Translator translator = attributesHandler.createTranslator(packages,
            translates);
      translator.modifyCommandline(commandLine);

      UniqueDependencyId uniqueId = new UniqueDependencyId(commandLine,
            preprocess.getValue());

      // include now the arguments that should not affect the uniqueId
      attributesHandler.handleVerboseLevel(verboseLevel, commandLine);

      if (idlFile != null) {
         check(idlFile, uniqueId, translator, versionsSupporter);
      }
      Iterator it = idlFiles.iterator();
      while (it.hasNext()){
         FileSet fset=(FileSet)(it.next());
         DirectoryScanner scanner = fset.getDirectoryScanner(getProject());
         File baseDir = scanner.getBasedir();
         String which[] = scanner.getIncludedFiles();
         for (int i = 0; i < which.length; i++) {
            if (check(new File(baseDir, which[i]), uniqueId, translator,
                  versionsSupporter)
                  && callCompiler.isIncremental()) {
               callIDLCompiler(candidateFiles, (Commandline) commandLine
                     .clone());
            }
         }
      }
      // if some files have changed, it is needed to compile them now
      if (!candidateFiles.isEmpty()) {
         if (callCompiler.useAllFiles()) {
            callIDLCompiler(sortOnDependencies(allFiles), commandLine);
         } else {
            callIDLCompiler(sortOnDependencies(candidateFiles), commandLine);
         }
      }
   }

   /**
    * Verifies one independent file, once that the arguments have been checked
    * and preprocessed. The file is added to the list allFiles and, if it
    * requires being idl-compiled, to the candidateFiles list.
    * 
    * @return true if the file must be compiled
    */
   private boolean check(File idlFile, UniqueDependencyId uniqueId,
         Translator translator, VersionsSupporter versionsSupporter) {
      boolean requiresCompilation = true;
      boolean usePreprocessedFile = preprocess.usePreprocessorFile();
      if (!force || usePreprocessedFile) {
         if (!dependsDir.isDirectory()) {
            throw new BuildException("Directory " + dependsDir.toString()
                  + " does not exist");
         }
         try {
            //Orbacus compiler does not follow include directives even
            // if defined inside scope
            boolean checkScopedIncludes=!compiler.isOrbacus();
            IDLChecker checker = new IDLChecker(idlFile, uniqueId, dependsDir,
                  targetDir, checkerIncludes, checkerPreprocessor,
                  attributesHandler.toGenerateClient(), attributesHandler
                        .toGenerateServer(),
                  attributesHandler.toGenerateTIEs(), attributesHandler
                        .toGenerateAMI(), checkAll, translator,
                  attributesHandler.isVerbosityNormal(), attributesHandler
                        .isVerbosityDebug(), this, checkScopedIncludes,
                        !force, preprocess.generatePreprocessorFile(), 
                        preprocess.expandFull());

            if (checker.build(versionsSupporter, failOnError
                  || usePreprocessedFile)
                  || force) {
               if (usePreprocessedFile) {
                  idlFile = checker.getPreprocessedFile();
               }
            } else {
               requiresCompilation = false;
            }
            dependencies.put(idlFile, checker.getIDLSources());
         } catch (Exception ex) {
            throw new BuildException(ex.getMessage());
         }
      }
      allFiles.add(idlFile);
      if (requiresCompilation) {
         candidateFiles.add(idlFile);
      }
      if (verboseLevel.isVerbose() || verboseLevel.isBasic()) {
         log("Status: " + idlFile.getName()
               + (requiresCompilation ? " requires" : " does not require")
               + " idl compilation");
      }
      return requiresCompilation;
   }

   /**
    * Processes the files found in the list, with the options given in the
    * command line The list is then removed
    */
   private void callIDLCompiler(List files, Commandline commandLine) {
      attributesHandler.execute(commandLine, files, classpath, path, this);
      files.clear();
   }

   /**
    * Returns a list composed by the same elements in the original list, but
    * whose order depends on the dependencies between files. That is, if file
    * a.idl depends on b.idl, b.idl will appear first in the final list
    */
   private List sortOnDependencies(List files) {
      List ret;
      if (dependencies.size() == 0) { // no dependencies, cannot be sorted!
         ret = files;
      } else {
         ret = new ArrayList();
         boolean extracted;
         // we will iterate on the given list; for each element, we study its
         // dependencies
         // if a file depends on other file that is (still)in the same list,
         // means that
         // this file should go later in the final list.
         // to avoid infinite loops, in the (as principle) impossible case where
         // A depends
         // on B and B depends on A, if a loop does not extract any file, the
         // ordering
         // ends, and the files are dumped like they are to the final list
         do {
            extracted = false;
            Iterator it = files.iterator();
            while (it.hasNext()) {
               File file = (File) it.next();
               boolean independent = true;
               Iterator it2 = ((Set) dependencies.get(file)).iterator();
               while (independent && it2.hasNext()) {
                  if (files.contains(it2.next())) {
                     independent = false;
                  }
               }
               if (independent) {
                  ret.add(file);
                  it.remove();
                  extracted = true;
               }
            }
         } while (extracted);
         ret.addAll(files);
      }
      return ret;
   }

   /**
    * Converts the parameters given into a Commandline, with information that is
    * already dependant on the compiler. It also updates the global variables
    * checkerPreprocessor, checkerIncludes. The only element not included is the
    * debug information, which must be included later
    */
   private void convertParameters() {
      if (idlFile == null && idlFiles == null) {
         throw new BuildException("\"file\" or \"FileSet\" must be specified");
      }

      if (targetDir == null) {
         targetDir = getProject().getBaseDir();
      }
      if (dependsDir == null) {
         dependsDir = targetDir;
      }

      attributesHandler = new TaskAttributesHandler(compiler, commandLine,
            idlCompiler);
      attributesHandler.handleCheckAllFlag(checkAll, commandLine);
      attributesHandler.handleSideAttribute(side, commandLine);
      attributesHandler.handleTargetDir(targetDir, commandLine);
      attributesHandler.handleAMIFlag(ami, commandLine);
      String checkerUndefines = attributesHandler.handleUndefines(undefines,
            commandLine);
      String checkerDefines = attributesHandler.handleDefines(defines,
            commandLine);
      checkerIncludes = attributesHandler.handleIncludes(include, commandLine);
      checkerPreprocessor = checkerDefines + checkerUndefines;
   }

   public void log(String msg) {
      super.log(msg);
   }

   public final static void main(String args[]) {
      System.out.println(VERSION);
   }

   private boolean checkAll;

   private boolean force;

   private boolean failOnError;

   private File targetDir, dependsDir;

   private File idlFile;

   private Path include;

   private Path classpath, path;

   private String idlCompiler;

   private Commandline commandLine = new Commandline();

   private List idlFiles = new ArrayList(); //FileSet

   private List defines = new ArrayList();

   private List undefines = new ArrayList();

   private List packages = new ArrayList();

   private List translates = new ArrayList();

   private TaskAttributesHandler.VerboseLevel verboseLevel = new TaskAttributesHandler.VerboseLevel();

   private TaskAttributesHandler.SideAttribute side = new TaskAttributesHandler.SideAttribute();

   private TaskAttributesHandler.CompilerAttribute compiler = new TaskAttributesHandler.CompilerAttribute();

   private TaskAttributesHandler.PreprocessAttribute preprocess = new TaskAttributesHandler.PreprocessAttribute();

   private TaskAttributesHandler.AMIAttribute ami = new TaskAttributesHandler.AMIAttribute();

   private TaskAttributesHandler.CallCompilerAttribute callCompiler = new TaskAttributesHandler.CallCompilerAttribute();

   private List allFiles = new ArrayList(); // all the specified files,

   // probably converted into
   // preprocessed form

   private List candidateFiles = new ArrayList(); // from allFiles, those that

   // require being idl-compiled

   private Map dependencies = new HashMap(); // each candidate file, with a Set

   // on which it depends on

   private TaskAttributesHandler attributesHandler;

   private String checkerPreprocessor, checkerIncludes;

   private final static String VERSION = "idldepend v1.22 - http://www.byteslooser.com/idldepend";
}
