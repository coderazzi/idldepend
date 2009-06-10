/**
 * File: IDLToJavaMapping.java
 * Author: LuisM Pena
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package net.coderazzi.idldepend.idl;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class provides a mapping from IDL structures into file names that must
 * be created out of those structures. This class is compliant with the "IDL to
 * Java Language Mapping Specification", Version 1.1, June 2001.
 */
public class IDLToJavaMapping {

   /**
    * The constructor allows to specify how further mappings will be performed.
    * It admits three boolean flags, which affects to the interface's mapping:
    * -clientSide: if set, _stubs, helper and holders are needed -serverSide: if
    * set, _POA files is needed -generateTIE: if serverSide is true, this flag
    * specifies wether delegation-based interfaces are created (xxPOATie)
    * -generateAMI: true if AMI must be supported on the interfaces
    */
   public IDLToJavaMapping(IDLMapperUser user, boolean clientSide,
         boolean serverSide, boolean generateTIE, boolean generateAMI) {
      this.clientSide = clientSide;
      this.serverSide = serverSide;
      this.generateTIE = generateTIE;
      this.generateAMI = generateAMI;
      this.user = user;
   }

   /**
    * This method must be called when a new prefix rules
    */
   public void prefixPragma(String prefix) {
      currentPrefix = prefix;
   }

   /**
    * This method must be called when a new scope (module, interface, valuetype,
    * struct) is entered.
    */
   public void enteredScope(String name, boolean module) {
      name = reservedWordsCare(name);
      scope.add(new Scope(name, module));
   }

   /**
    * This method must be called when a scope (module, interface, valuetype,
    * struct) is existed.
    */
   public void exitedScope() {
      scope.remove(scope.size() - 1);
   }

   /**
    * Maps the files that must be created when a const is found.
    */
   public void mapConst(String constName) {
      // mapping 1.6.1: consts inside interface does not produce new files
      // mapping 1.6.2: consts outside interfaces produce an interface with
      // the name of the const
      constName = reservedWordsCare(constName);
      if (isModuleScope()) {
         fileNeeded(createFileName(getScopePath(), constName, null), constName);
      }
   }

   /**
    * Maps the files that must be created when an enum is found.
    */
   public void mapEnum(String enumName) {
      // mapping 1.7: three files are required, whose names are the name
      // of the enum, the same+"Helper" and the same+"Holder"
      enumName = reservedWordsCare(enumName);
      reportElementWithHolderAndHelper(enumName);
   }

   /**
    * Maps the files that must be created when a struct is found.
    */
   public void mapStruct(String structName) {
      // mapping 1.8: three files are required, whose names are the name
      // of the struct, the same+"Helper" and the same+"Holder"
      structName = reservedWordsCare(structName);
      reportElementWithHolderAndHelper(structName);
   }

   /**
    * Maps the files that must be created when a union is found.
    */
   public void mapUnion(String unionName) {
      // mapping 1.9: three files are required, whose names are the name
      // of the union, the same+"Helper" and the same+"Holder"
      unionName = reservedWordsCare(unionName);
      reportElementWithHolderAndHelper(unionName);
   }

   /**
    * Maps the files that must be created when an interface is found. Because
    * the mapping differs on weather the interface is abstract or not, a second
    * parameters is needed to specify it. It is needed as well an additional
    * parameter to support local interfaces
    */
   public void mapInterface(String interfaceName, boolean abstractInterface,
         boolean localInterface) {
      // mapping 1.10: three files are required, whose names are the name
      // of the interface, the same+"Helper" and the same+"Holder"
      // Additionally, it's generated NameOperations and _NameStub.
      // NamePOA and NamePOATie are optional.
      // In case of and abstract interface, nor the Operations neither
      // the POA or POATie are generated
      // For local interfaces (mappint 1.20), the same three basic files
      // (basic, Helper, Holder) and the Operations file are required. For
      // servers, it is needed as well a class _LocalBase.java, and, if TIE
      // is generated, as well LocalTie.java
      // If AMI callback is supported, every interface seems to have another
      // interface (with all the generated classes except the holder), with
      // name AMI_nameHandler
      // However, these class are only required on the client side.

      mapInterface(interfaceName, abstractInterface, localInterface, false,
            clientSide, serverSide, generateTIE);

      if (generateAMI) {
         // generate client, server and TIE, with a modified interface name. The
         // Holder is not required
         // This interface is not abstract, not local
         mapInterface("AMI_" + interfaceName + "Handler", false, false, true,
               true, true, true);
      }
   }

   /**
    * Maps the files that must be created when an pseudo interface is found.
    */
   public void mapPseudoInterface(String interfaceName) {
      // I am not completely sure whether this mapping is standard; jdk and
      // openorb do not support
      // 'pseudo'; jacorb generates a simple class with the name of the pseudo
      // interface, in the
      // current context.
      // However, the standard does not mention this mapping.
      // http://www.cs.sunysb.edu/documentation/jdk/guide/idl/mapping/pseudo.fm.html
      // has a proposal to generate just an abstract class in the package
      // org.omg.CORBA
      // In this release, the JacORB way is implemented to
      interfaceName = reservedWordsCare(interfaceName);
      fileScoperNeeded(createFileName(getScopePath(), interfaceName, null),
            interfaceName);

      // this would be the 'standard':
      // user.fileNeeded(null,createFileName(getCORBAScopePath(), interfaceName,
      // null), interfaceName, false);
   }

   /**
    * Maps the files that must be created when an interface is found. Because
    * the mapping differs on weather the interface is abstract or not, a second
    * parameters is needed to specify it. It is needed as well an additional
    * parameter to support local interfaces To facilitate the generation of AMI
    * interfaces, a 4th parameter is used. Note that AMI generated interfaces do
    * not require a Holder class
    */
   protected void mapInterface(String interfaceName, boolean abstractInterface,
         boolean localInterface, boolean excludeHolder, boolean clientSide,
         boolean serverSide, boolean generateTIE) {
      String scope = getScopePath();
      interfaceName = reservedWordsCare(interfaceName);
      fileScoperNeeded(createFileName(scope, interfaceName, null),
            interfaceName);
      fileScoperNeeded(createFileName(scope, interfaceName, HELPER_EXT),
            interfaceName);
      if (!excludeHolder) {
         fileScoperNeeded(createFileName(scope, interfaceName, HOLDER_EXT),
               interfaceName);
      }
      if (clientSide && !localInterface) {
         fileScoperNeeded(createFileName(scope, "_" + interfaceName, STUB_EXT),
               interfaceName);
      }
      if (!abstractInterface) {
         fileScoperNeeded(createFileName(scope, interfaceName, OPERATIONS_EXT),
               interfaceName);
         if (serverSide) {
            if (localInterface) {
               fileScoperNeeded(createFileName(scope, "_" + interfaceName,
                     LOCALBASE_EXT), interfaceName);
               if (generateTIE) {
                  fileScoperNeeded(createFileName(scope, interfaceName,
                        LOCALTIE_EXT), interfaceName);
               }
            } else {
               fileScoperNeeded(createFileName(scope, interfaceName, POA_EXT),
                     interfaceName);
               if (generateTIE) {
                  fileScoperNeeded(
                        createFileName(scope, interfaceName, TIE_EXT),
                        interfaceName);
               }
            }
         }
      }
   }

   /**
    * Maps the files that must be created when an exception is found.
    */
   public void mapException(String exceptionName) {
      // mapping 1.15: three files are required, whose names are the name
      // of the exception, the same+"Helper" and the same+"Holder"
      exceptionName = reservedWordsCare(exceptionName);
      reportElementWithHolderAndHelper(exceptionName);
   }

   /**
    * Maps the files that must be created when a valuetype is found. The result
    * depends on wether the valuetype defines a factory or not, and therefore,
    * an additional parameter is offered to specify it
    */
   public void mapValuetype(String valuetypeName, boolean withFactory) {
      // mapping 1.13: three files are required, whose names are the name
      // of the valuetype, the same+"Helper" and the same+"Holder"
      // If the valuetype includes a factory, another file is required
      // with extension ValueFactory
      String scope = getScopePath();
      valuetypeName = reservedWordsCare(valuetypeName);
      fileScoperNeeded(createFileName(scope, valuetypeName, null),
            valuetypeName);
      fileScoperNeeded(createFileName(scope, valuetypeName, HELPER_EXT),
            valuetypeName);
      fileScoperNeeded(createFileName(scope, valuetypeName, HOLDER_EXT),
            valuetypeName);
      if (withFactory) {
         fileScoperNeeded(
               createFileName(scope, valuetypeName, VALUEFACTORY_EXT),
               valuetypeName);
      }
   }

   /**
    * Maps the files that must be created when a valuebox is found. The result
    * depends on wether the valuebox is defined over a primitive type or not,
    * and therefore, an additional parameter is offered to specify it
    */
   public void mapValuebox(String valueboxName, boolean primitive) {
      // mapping 1.14: three files are required, whose names are the name
      // of the valuebox, the same+"Helper" and the same+"Holder"
      // If the valuebox is not build over a primitive, the first class,
      // (just the name) is not needed
      String scope = getScopePath();
      valueboxName = reservedWordsCare(valueboxName);
      fileNeeded(createFileName(scope, valueboxName, HELPER_EXT), valueboxName);
      fileNeeded(createFileName(scope, valueboxName, HOLDER_EXT), valueboxName);
      if (primitive) {
         fileNeeded(createFileName(scope, valueboxName, null), valueboxName);
      }
   }

   /**
    * Maps the files that must be created when a typedef is found. The result
    * depends on wether the typedef is defined over a sequence or array or not,
    * and therefore, an additional parameter is offered to specify it
    */
   public void mapTypedef(String typedefName, boolean sequenceOrArray) {
      // mapping 1.18: A helper file is always created. A holder is only
      // created for sequence or arrays
      String scope = getScopePath();
      typedefName = reservedWordsCare(typedefName);
      fileNeeded(createFileName(scope, typedefName, HELPER_EXT), typedefName);
      if (sequenceOrArray) {
         fileNeeded(createFileName(scope, typedefName, HOLDER_EXT), typedefName);
      }
   }

   /**
    * Creates a String[3], containing a holder, a helper, and the name of the
    * item itself
    */
   private void reportElementWithHolderAndHelper(String name) {
      String scope = getScopePath();
      fileNeeded(createFileName(scope, name, null), name);
      fileNeeded(createFileName(scope, name, HELPER_EXT), name);
      fileNeeded(createFileName(scope, name, HOLDER_EXT), name);
   }

   /**
    * Creates a file name taking in account the scope. To the fileName is added
    * the 'added' string, if it's not null
    */
   protected String createFileName(String scope, String name, String added) {
      StringBuffer creator = new StringBuffer(scope).append(name);
      if (added != null) {
         creator.append(added);
      }
      return creator.append(FILE_EXT).toString();
   }

   /**
    * Returns an StringBuffer where the scope has been built in.
    */
   protected String getScopePath() {
      StringBuffer ret = new StringBuffer();
      Iterator it = scope.iterator();
      while (it.hasNext()) {
        Scope s = (Scope) it.next();
        ret.append(s);
        if(!s.isModule())
        {
           ret.append("Package"); // mapping 1.17
        }
        ret.append(File.separatorChar);
      }
      return ret.toString();
   }

   /**
    * Takes care that the name is not reserved. In that case, prepends '_' It
    * does the same if the name ends with Package, Helper, Holder
    */
   protected String reservedWordsCare(String name) {
      if (javaReservedList.contains(name)
            || (name.endsWith(PACKAGE_EXT) && !name.equals(PACKAGE_EXT))
            || (name.endsWith(HELPER_EXT) && !name.equals(HELPER_EXT))
            || (name.endsWith(HOLDER_EXT) && !name.equals(HOLDER_EXT))) {
         return "_" + name;
      }
      return name;
   }

   /**
    * Called to isue a user.fileNeeded request
    */
   protected void fileNeeded(String name, String originatingType) {
      if (isModuleScope()) {
         user.fileNeeded(currentPrefix, name, originatingType, false);
      } else {
         user.fileNeeded(currentPrefix, name, scope.get(scope.size() - 1)
               .toString(), true);
      }
   }

   /**
    * Same as fileNeeded, associated to a given scope
    */
   protected void fileScoperNeeded(String name, String itsScope) {
      user.fileNeeded(currentPrefix, name, itsScope, false);
   }

   /**
    * Checks whether the current scope is a module.
    */
   private boolean isModuleScope() {
      if (scope.isEmpty()) {
         return true;
      }
      Scope outermost = (Scope) scope.get(scope.size() - 1);
      return outermost.isModule();
   }

   /**
    * Contains information on a scope component.
    */
   private static class Scope {
      public Scope(String name, boolean module) {
         this.name = name;
         this.module = module;
      }

      public String getName() {
         return name;
      }

      public boolean isModule() {
         return module;
      }

      public String toString() {
         return name;
      }

      private String name;

      private boolean module;
   }

   private String currentPrefix;

   private List scope = new ArrayList(); // Scope

   private boolean clientSide, serverSide, generateTIE, generateAMI;

   private IDLMapperUser user;

   protected static final String PACKAGE_EXT = "Package";

   protected static final String HOLDER_EXT = "Holder";

   protected static final String HELPER_EXT = "Helper";

   protected static final String OPERATIONS_EXT = "Operations";

   protected static final String STUB_EXT = "Stub";

   protected static final String POA_EXT = "POA";

   protected static final String TIE_EXT = "POATie";

   protected static final String LOCALTIE_EXT = "LocalTie";

   protected static final String LOCALBASE_EXT = "LocalBase";

   protected static final String VALUEFACTORY_EXT = "ValueFactory";

   private static final String FILE_EXT = ".java";

   private static Set javaReservedList;
   static {
      javaReservedList = new HashSet(); // those reserved in Java but also in
      // IDL are not included
      javaReservedList.add("break");
      javaReservedList.add("byte");
      javaReservedList.add("catch");
      javaReservedList.add("class");
      javaReservedList.add("continue");
      javaReservedList.add("do");
      javaReservedList.add("else");
      javaReservedList.add("extends");
      javaReservedList.add("false");
      javaReservedList.add("final");
      javaReservedList.add("finally");
      javaReservedList.add("for");
      javaReservedList.add("goto");
      javaReservedList.add("if");
      javaReservedList.add("implements");
      javaReservedList.add("import");
      javaReservedList.add("instanceof");
      javaReservedList.add("int");
      javaReservedList.add("new");
      javaReservedList.add("null");
      javaReservedList.add("package");
      javaReservedList.add("protected");
      javaReservedList.add("return");
      javaReservedList.add("static");
      javaReservedList.add("super");
      javaReservedList.add("synchronized");
      javaReservedList.add("this");
      javaReservedList.add("throw");
      javaReservedList.add("throws");
      javaReservedList.add("transient");
      javaReservedList.add("true");
      javaReservedList.add("try");
      javaReservedList.add("volatile");
      javaReservedList.add("while");
      javaReservedList.add("strictfp");
   }
}
