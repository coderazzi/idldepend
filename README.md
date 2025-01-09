idldepend - an ANT CORBA task
=============================

*   [Description](#Description)
*   [License and download](#LicenseAndDownload)
*   [Version and CORBA compliance](#Version):1.30 version delivered the 10th June 2009
*   [Attributes](#Attributes)
*   [Nested elements](#NestedElements)
*   [Example](#Example)
*   [Behaviour](#Behaviour)
    *   [Using the preprocessor](#UsingThePreprocessor)
    *   [Using package and translate](#UsingPackageAndTranslate)
    *   [Invoking the idl compiler](#IDLCompiler)
    *   [Translation of compiler arguments](#Translation)
        *   [JDK / IBM](#JDK)
        *   [Orbacus](#Orbacus)
        *   [Jacorb](#Jacorb)
        *   [OpenORB](#OpenORB)
        *   [Orbix](#Orbix)
    *   [Additional notes](#notes)
*   [Troubleshooting](#Troubleshooting)
*   [History changes](#History)
*   [Bugs and limitations](#Bugs)

Description
-----------

idldepend is an [ant](http://jakarta.apache.org/ant/index.html) task verifying the dependencies of a CORBA/IDL file.

It parses the file, verifying the Java files that must be generated, taking in account the modifications that can happen due to command line parameters. If any of the Java files is missing or is older than the source IDL specification, it launches the specified compiler, being correctly supported those coming with Orbacus, Orbix, Jacorb, OpenORB, IBM and Sun'JDK distributions.

To speed up the process and avoid parsing unnecesarily the same files continuously, it keeps the dependencies into intermediate files.

This task does not launch the Java compiler, that is, its output are Java files and not directly the final bytecode.

License and download
--------------------

idldepend is delivered as it, without any responsabilities on the author. It is open source, it can be used or modified without any limitations.

This task has been on use since September 2001. As idldepend is a wrapper around several IDL compilers, it needs to continuosly evolve, following the changes that those compilers implement.

If some bug is found or the behaviour is not exactly the shown by the compiler, please send me a mail (dr.lu@coderazzi.net) and I will promptly introduce the modifications. In any case, the task is distributed with source, and therefore you can make by your own any changes.

Following are the downloadable files. Please note that, as from release 1.30, idldepend is packaged as _net.coderazzi.idldepend_, what can require minor changes in your ant build files.

*   Version 1.30. Latest version, (10/06/09).
    *   Source code, to be built using ant: [idldepend-1-3-0.zip](idldepend-1-3-0.zip)
    *   Binary code: [idldepend-1-3-0.jar](idldepend-1-3-0.jar)
*   Version 1.22. (com.byteslooser package), (04/05/08).
    *   Source code (.zip format, 146.289 bytes), to be built using ant: [idldepend-1-2-2.zip](idldepend-1-2-2.zip)
    *   Binary code (.jar file, 238.634 bytes): [idldepend-1-2-2.jar](idldepend-1-2-2.jar)

Previous available versions:

*   Version 1.21 (19/10/07). [idldepend-1-2-1.zip](idldepend-1-2-1.zip) and [idldepend-1-2-1.jar](idldepend-1-2-1.jar)
*   Version 1.10 (12/12/06). [idldepend-1-1-0.zip](idldepend-1-1-0.zip) and [idldepend-1-1-0.jar](idldepend-1-1-0.jar)
*   Version 1.00 (09/11/06). [idldepend-1-0-0.zip](idldepend-1-0-0.zip) and [idldepend-1-0-0.jar](idldepend-1-0-0.jar)
*   Version 0.81 (28/09/05). [idldepend-0-8-1.zip](idldepend-0-8-1.zip) and [idldepend-0-8-1.jar](idldepend-0-8-1.jar)

In case of using the source code, you need to have [Ant](http://jakarta.apache.org/ant/index.html) (to build the task) and [JavaCC](https://javacc.dev.java.net/) (only if modifying the grammars, to parse them). Please modify the ant script to define your own JavaCC home directory, and compile it just entering 'ant'.

Version and CORBA compliance
----------------------------

Initially built using Ant 1.4, the current version has been tested against Ant 1.7.0; please note that I do not regularly check if it works under other Ant versions (but I would be surprised if not).

idldepend is compliant with the IDL to Java mapping defined for CORBA 2.4. Support for the mapping 1.2 -CORBA 3.0 compliant- is not immediately foreseen: it will be introduced when a CORBA distribution with Java mapping finally supports it.

In particular, the following keywords are not supported:

*   component
*   consumes
*   finder
*   home
*   emits
*   eventtype
*   getraises
*   import
*   multiple
*   primarykey
*   provides
*   publishes
*   setraises
*   typeid
*   typeprefix
*   uses

Note that all these keywords have been added with CORBA 3.0, and, by now, I have not knowledge of any ORB with Java mapping supporting them.

idldepend supports the grammar defined by the OMG; it raises no warnings or errors even if the used compiler does not support some used features. For example, a valid IDL file could contain proper local interface definitions, which are not supported by the JDK compiler. idldepend will only warn about incorrect IDL constructs.

Task's Attributes
-----------------

| Attribute | Description | Required | Default value |
| --- | --- | --- | --- |
| compiler | jdk / orbacus / jacorb / jacorb2 / openorb / orbix / ibm  <br/>Specifies the compiler to use, which must be available on the classpath (jdk/jacorb/jacorb2/openorb/ibm) or in the path (orbacus/orbix2k):<br><br>*   jacorb2 (Jacorb2.x) is equivalent to jacorb, but it is required to handle different behaviours in the way they handle packages. See below the section on [package and translate](#UsingPackageAndTranslate) for further information<br>*   Note that the compilers for jacorb and openorb must be available from the classpath. | no  | jdk |
| callCompiler | foreach / once / onceWithAll  <br>Using foreach, the compiler is called each time that a file is verified to have been modified, while with once, it is called only once, with all the files. Please look at [invoking the idl compiler](#IDLCompiler) for a deeper explanation. | no  | foreach |
| compilerPath | Specifies the location of the idl compiler.  <br>This is specially useful for orbacus / orbix, to avoid specifying explicitely the PATH to the compiler.  <br>Note that if it is specified on the jdk / openORB ... compilers, the idl compiler is not invoked anymore through the classpath, but spawning a different process. | no  | \-  |
| file | File: Specifies the file to be verified. This or the nested element fileset must be specified. | no  | \-  |
| targetdir | File: target directory used on compiler's generation. | no  | as base dir |
| dependsdir | File: directory used to store the dependency's files. | no  | as targetdir |
| side | client / server / all / serverTIE / allTIE  <br>It specifies the kind of generation to be performed: for client or server purposes, and generating TIE files or not | no  | allTIE |
| ami | no / callback  <br>It specifies whether to support AMI (asynchronous messaging interface). It support currently only the callback model (i.e., not the polling) | no  | no  |
| checkall | boolean: it checks all the files that must be generated, including those coming out from types defined in included files<br><br>Note: if a file is included inside some scope (for example, the include directive appears inside a module definition), some compilers will anyway generate the included types. Although this is a reasonable behaviour, other compilers, as Orbacus, will ignore those included types unless checkAll is set to true.  <br>Idldepend mimics this behaviour, so, using Orbacus, it will dismiss the included types if checkAll is false. | no  | false |
| force | boolean: if set, it does not perform any check, launching the compiler directly.  <br>That is, no dependencies are verified, the idl files are not parsed, and this task is only used to invoke the compiler. | no  | false |
| failOnError | boolean: if set, the idl compiler is not launched if the parser's task finds an error. | no  | false |
| preprocess | dismiss / store / storeFull / use / useFull  <br>Specifies the action on the preprocessed file, as described in the [section below](#UsingThePreprocessor). | no  | dismiss |
| verbose | quiet / basic / debug  <br>Specifies the task's verbosity level | no  | basic |

Nested Elements
---------------
| Element | Description |
| --- | --- |
| fileset | Fileset: files to be compiled. This element is optional, but if the attribute file is not specified, there must be at least a fileset element. |
| include | Path: specifies the paths to be used when looking for included files on the IDL files. |
| define | Macro definition, defined with the following attributes:<br><br>*   **name**, required<br>*   **value**: not required |
| undefine | Macro undefinition, defined with the following attribute:<br><br>*   **name**, required |
| package | Modifier used to change the final package for the generated Java files, prepending a given prefix. Additional information is shown [below](#UsingPackageAndTranslate).  <br>Following are the attributes of this element:<br><br>*   **module**: String:The module on which the package prefix will be applied. Every type belonging to this module is included inside the package given. When specified, the attribute prefix must be specified as well.<br>*   **prefix**:String:The package prefix to apply.<br>*   **auto**:boolean: if set, it includes every file into a package whose name is taken the prefix defined in the idl dile. It is only valid with orbacus and openorb. |
| translate | Modifier used to change the final package for the generated Java files, modifying the standard package per module generation. Additional information is shown [below](#UsingPackageAndTranslate).  <br>Following are the attributes of this element (both are mandatory):<br><br>*   : String: The module to translate.<br>*   : String: The package on which the previous module is translated. |
| classpath | Sets the classpath to use to launch the IDL compiler. A whole description of this element is given in the ant manual, on the [Path-like Structures](http://ant.apache.org/manual/using.html#path) |
| path | Sets the path to use when the IDL compiler is executed as an external program. This applies only to orbacus and orbix compilers; if the compilerPath is specified, it affects to all the compilers.  <br>If it is used with jdk or jacorb and the compilerPath is not specified, a warning is raised.  <br>A whole description of this element is given in the ant manual, on the [Path-like Structures](http://ant.apache.org/manual/using.html#path) |
| arg | Argument: additional argument to supply to the compiler. Note that if this argument modifies the name of the files being generated, force should be set to true, as this task will not be able to calculate correctly the dependencies  <br>Example: <arg value="-Gdsi"> |

Example
-------

    <project name="Sensei" default="main" basedir=".">

      <taskdef name="idlcheck"
               classname="net.coderazzi.idldepend.IDLCheckerTask"/>

      <target name="idl">
        <idlcheck compiler="jdk" 
                  force="false"  
                  checkAll="false"
                  verbose="basic"
                  targetDir="output"
                  dependsDir="dependencies">
          <fileset dir=".">
            <include name="*.idl"/>
          </fileset>
          <include>
            <pathelement path=".."/>
          </include>
          <package module="example" prefix="UNO"/>
          <translate module="CORBA_HP" package="CORBA_HP.v2"/>
          <translate module="IfacePackage" package="Interf"/>
        </idlcheck>
      </target>
    
      <target name="main" depends="idl">
        <javac srcdir="output" includes="**/*.java"/>
      </target>
    
    </project>

Behaviour
---------

### Using the preprocessor

idldepend can store the preprocessed file (the IDL file after the C preprocessor parses it), and even use it to generate the Java files. This feature is very valuable for those IDL compilers that lack a full-featured preprocessor (Jacorb has problems with basic macros, and the jdk compiler has as well problems with basic preprocessing functionality, like macros defined across several lines).

This functionality is specified with the attribute preprocess, which can have the following values:

*   dismiss:
    *   The preprocessed file is not stored.
*   store:
    *   The preprocessed file is stored, in the same directory as the dependencies (specified with the attribute dependsdir).
    *   This file has the same name as the original, plus a 4-digits hexadecimal number, plus '.idl'. The number is the same as used for the dependencies, and it is unique for each file.
    *   This file is not fully expanded: the #include directives are kept (but with the files to be included written with its absolute path).
    *   If the attribute force is set to true, no preprocessed file is generated.
*   storeFull:
    *   The preprocessed file is stored, like it is done in case of store
    *   This file is fully expanded: the #include directives are handled, including therefore the related file into the preprocessed file, like any C preprocessor would do.
    *   If the attribute force is set to true, no preprocessed file is generated.
*   use:
    *   The preprocessed file is stored, like it is done in case of store
    *   This file is not fully expanded, like it is explained for store
    *   The preprocessed file is used as input for the IDL compiler; hereby the need to avoid a full expansion, as the IDL compiler would generate otherwise the Java files for all the encountered types.
    *   If the attribute force is set to true, the preprocessed file is still generated.
*   useFull:
    *   The preprocessed file is stored, like it is done in case of store
    *   This file is fully expanded, like it is explained for storeFull
    *   The preprocessed file is used as input for the IDL compiler; note that in this case, the compiler will generate Java code for all the types found in the included files. Therefore, the attribute checkAll looses his meaning if this value is used.
    *   If the attribute force is set to true, the preprocessed file is still generated.

### Using package and translate

These modifiers affect to the packages under which the final Java files are generated. For example: package moduleA prefixA.prefixB converts the file moduleA/moduleB/file.java into prefixA/prefixB/moduleB/file.java The result of these modifications is not fully specified. idldepend just performs the translations made by the specific compiler, and each compiler behaves quite different.

For example, when translate is used on the jdk compiler, it only affects to top-level modules, while under jacorb it modifies any given module; and this modifier is not applicable for orbacus.

Following is a brief description of its behaviour. In case of doubt, check directly the compiler:

*   Package
    *   jdk
        *   equivalent to modifier -pkgPrefix.
        *   module and prefix are mandatory, and auto is not supported.
        *   module affects only to the top level module
        *   prefix can be compound. I.e, it's possible to specify packageA.packageB
        *   It is applied before any translate modification.
    *   orbacus
        *   equivalent to modifiers --package, --prefix-package and auto-package.
        *   if auto is specified, no other attribute can be present. It is equivalent to --auto-package
        *   if only prefix is specified, it is equivalent to --package, with priority over any auto modifier
        *   if module is specified, prefix must be specified as well, being in this case equivalent to --prefix-package: it has priority over --package
    *   jacorb
        *   In version 1.x, it is equivalent to modifier -p
        *   In version 2.x, it is equivalent to modifier -i2jpackage :prefix. To handle the difference with version 1.x, the compiler attribute must be set as jacorb2 instead of jacorb
        *   it only supports the prefix modifier.
        *   it's applied after any translate modification
    *   openorb
        *   equivalent to modifier -package
        *   Important:if package is used when compiling more than one idl files, openORB will behave differently if these files are passed once by once than when they are passed all at once! For this reason, when the combination openORB + package modifier is used, it is convenient to pass the parameter callCompiler=onceWithAll
    *   orbix
        *   The prefix is always required, while module is optional and the auto modifier not supported.
        *   if no module is specified, it is equivalent to the Orbix parameter -P prefix
        *   if the module is specified, it is equivalent to the Orbix parameter -Pmodule=prefix
        *   If several matching packages are specified, it is only used the last one.
*   Translate
    *   jdk
        *   equivalent to modifier -pkgTranslate.
        *   module and package are mandatory
        *   module affects only to the top level modules
        *   prefix can be compound. I.e, it's possible to specify packageA.packageB
        *   It is applied after any package modification.
        *   If two translates are specified affecting the same module:
            *   If both translates have the same module name, only the second one remains.
            *   Otherwise, the translate with a more specific module name will be applied first.
    *   orbacus
        *   Not supported
    *   jacorb
        *   equivalent to modifier -i2jpackage.
        *   module and package are mandatory
        *   module affects to any module in the type. It must be simple, i.e, moduleA.moduleB is not valid
        *   prefix can be compound. I.e, it's possible to specify packageA.packageB
        *   It is applied before any package modification.
    *   openorb
        *   Not supported
    *   orbix
        *   Not supported

### Invoking the IDL compiler

A number of attributes and nested elements control how the IDL compiler will be invoked from idldepend. Of course, every single attribute or element modifies the way the IDL compiler is called, adding or modifying the parameters passed to it; this section refers to which compiler is invoked and how the idl files are passed.

Which compiler is invoked is specified via the attributes compiler and compilerPath, and nested elements classpath and path. How the idl files are passed to the compiler is handled via the callCompiler attribute. From these, only this last attribute requires a deeper explanation:

*   If callCompiler is set to foreach, each idl file is compiled separately. This is the default behaviour for two reasons:
    *   It was the only mode existing before version 0.53
    *   Some compilers only supports this mode (like JDK or IBM)
*   If callCompiler is set to once, the compiler is only invoked once, passing all the idl files that require compilation.
    *   It should increase the performance, on those compilers that support it.
    *   It is mandatory in some cases. In special, for the OpenORB compiler, it behaves differently compiling two files sequentially or at once, when the package elements are used.
*   If callCompiler is set to onceWithAll, the compiler is only invoked once. The main difference with once is that, if idldepend identifies that at least once idl file must be compiled, the compiler will be invoked with ALL the idl files, not only those requiring compilation.
    *   Obviously the performance will be worse.
    *   It is mandatory in some cases. In special, for the OpenORB compiler, as it behaves differently compiling files sequentially or at once, it can be required to pass all the files on every compilation.

The callCompiler functionality has been introduced to deal with the OpenORB compiler, though it could be useful with other compilers as well. The OpenORB compiler contains currently a bug: if file a.idl includes the file b.idl and both are passed at once to the idl compiler, OpenORB will not generate the code for the types found in b.idl if it is passed in the command line after a.idl.  
To work around this bug, idldepend will reorder the idl files, passing first those without dependencies to the others (currently, there are no mechanisms to disable this reordering)

### Translation of compiler arguments

Below is listed the translation of compiler arguments into the task parameters for the supported compilers.

The lists are given following the compiler order. When a parameter is not supported, is explicitely writen. Note that the args parameter in the task allows to specify any other parameter, but in that case the task can become useless. When a parameter is not directly supported but it can still be specified using the args parameter without any problem, it is as well explicitely writen.

#### JDK (as to 1.4.0) and IBM JDK (3.0)

| compiler argument | task parameter |
| --- | --- |
| \-d <symbol> | define name=<symbol> |
| \-emitAll | checkAll=true |
| \-f<side> | side=<side>  <br>Note that the side's names match what is expected by JDK (client, server...) |
| \-i <include path> | include |
| \-keep | Not supported |
| \-noWarn | verbose="quiet" or "basic" |
| \-oldImpBase | Not supported |
| \-pkgPrefix <t> <prefix> | package module=<t> prefix=<prefix> |
| \-pkgTranslate <t> <pkg> | translate module=<t> package=<pkg> |
| \-skeletonName | Not supported |
| \-td <dir> | targetDir=<dir> |
| \-tieName | Not supported |
| \-v, -verbose | verbose="debug" |
| \-version | Not supported |

#### Orbacus (as to 4.1.0)

| compiler argument | task parameter |
| --- | --- |
| \-h, --help | Not supported |
| \-v, --version | Not supported |
| \-e, --cpp | Not supported (use args instead) |
| \-d, --debug | verbose="debug" |
| \-DNAME | define name=NAME |
| \-DNAME=DEF | define name=NAME value=DEF |
| \-UNAME | undefine name=NAME |
| \-IDIR | include |
| \-E | Not supported |
| \--no-skeletons | side=client |
| \--no-comments | Not supported (use args instead) |
| \--tie | side=serverTIE or side=allTIE |
| \--clone | Not supported (use args instead) |
| \--all | checkAll=true |
| \--impl | Not supported |
| \--impl-tie | Not supported |
| \--package PKG | package prefix=PKG |
| \--prefix-package PRE PKG | package module=PRE prefix=PKG |
| \--auto-package | package auto=true |
| \--output-dir DIR | targetDIR=DIR |
| \--file-list FILE | Not supported (use args instead) |
| \--with-interceptor-args | Not supported (use args instead) |
| \--no-local-copy | Not supported (use args instead) |
| \--case-sensitive | Not supported |

#### Jacorb (as to 1.4.1 and 2.3)

| compiler argument | task parameter |
| --- | --- |
| \-forceOverwrite | Included by default |
| \-syntax | Not supported |
| \-noskel | side=client |
| \-nostub | side=server or side=serverTIE |
| \-Idir | include |
| \-sloppy\_forward | Not supported (use args instead) |
| \-sloppy\_names | Not supported |
| \-Dx | define name=x |
| \-Dx=y | define name=x value=y |
| \-Ux | undefine name=x |
| \-p pack | package prefix=pack (version 1.x) |
| \-i2jpackage :package | package prefix=package (version 2.x, compiler set to jacorb2) |
| \-i2jpackage x:a.b.c | translate module=x package=a.b.c |
| \-i2jpackagefile | Not supported (use args instead) |
| \-i | Not supported (use args instead) |
| \-ir | Not supported (use args instead) |
| \-global\_import | Not supported (use args instead) |
| \-d dir | targetDIR=dir |
| \-W 4 | \-verbose=debug |
| \-all | checkAll=true |
| \-v\|version | Not supported |
| \-h\|help | Not supported |
| \-backend | Not supported (use args instead) |
| \-jdk14 | Not supported (use args instead) |
| \-nofinal | Not supported (use args instead) |
| \-ami\_callback | ami=callback |
| \-ami\_polling | Not supported (use args instead) |

#### OpenORB (as to 1.3 and 1.4)

| compiler argument | task parameter |
| --- | --- |
| \-release | Not supported |
| \-d directory name | targetDIR=directory name |
| \-package package\_name | package prefix=package\_name |
| \-I | include |
| \-D | define |
| \-nostub | side=server or side=serverTIE |
| \-nolocalstub | Not supported (use args instead) |
| \-noskeleton | side=client |
| \-notie | side=client or side=server or side=all |
| \-boa | Not supported |
| \-dynamic | Not supported |
| \-portablehelper | Not supported (use args instead) |
| \-all | checkAll=true |
| \-noprefix | Should not be any prefix auto=yes |
| \-noreverseprefix | Not supported |
| \-native | Not supported |
| \-quiet | verbose=quiet |
| \-silence | verbose=quiet |
| \-verbose | verbose=debug |
| \-jdk1.4 | Not supported (use args instead) |
| \-invokeMethod | Not supported (use args instead) |
| \-minTableSize | Not supported (use args instead) |
| \-XgenerateValueFactory | Not supported (use args instead) |
| \-XgenerateValueImpl | Not supported (use args instead) |

#### Orbix (2000)

Please note that for this compiler, only the directly supported arguments are specified.  
If no present in the table but still required, the args task attribute can be used.

In addition, if an argument is added via arg and this argument must be embedded in jbase or jpoa, like is the case for arguments starting by -P, -O, -G, -M, -J, -V, -F, the addition is handled automatically by idldepend, i.e., it is not required to add explicitely jbase or jpoa.

| compiler argument | task parameter |
| --- | --- |
| \-jbase | side=client |
| \-jpoa | side=server |
| \-Idir | include |
| \-Dx | define name=x |
| \-Dx=y | define name=x value=y |
| \-Ux | undefine name=x |
| \-P pack | package prefix=pack |
| \-Px=y | package module=x package=y |
| \-Odir | targetDIR=dir |
| \-w | \-verbose=quiet |
| \-v | \-verbose=debug |

### Additional notes

*   The idldepend grammar supports empty module definitions. The OMG specification declares that an empty module -that is, containing no nested definitions- is invalid; however, compilers like JDK allow it, and there is no reason for IdlDepend to stop the compilation.
*   idldepend supports empty files (IDL files not defining types). In this case, IDLdepend will not fail, and it depends on the IDL compiler to handle them or fail, as the OMG IDL grammar does not allow those files.
*   idldepend supports the non-standard pseudo keyword.  
    This keyword is used to define a pseudo object in the JacORB compiler; it is not standard OMG/IDL at all, and it is not supported in the other compilers (JDK / IBM / OpenORB).  
    In the JacORB compiler, it generates a simple abstract class for the given interface (pseudo interface Example {} will generate the class Example.java at current scope, without added helpers or holders).
*   idldepend defines in the same way the constructs: "#ifdef \_\_FOO\_\_", "#if defined(\_\_FOO\_\_)", and "#if defined \_\_FOO\_\_ "  
    Note that the IDL compiler can still handle them in different form; in special, the JDK compiler treats the second one as a macro to be expanded.
*   Handling circular dependencies, idldepend fixes a limit on the recursion: if a file is included 400 times (without being completed its inclusion), it reports an error. This is usually due to a wrong circular dependency.

Troubleshooting
---------------

In case of errors, the fastest way to verify what is happening is to make idldepend verbose, using the attribute verbose=debug.

*   idldepend does not report obvious IDL errors.  
    idldepend performs exclusively a grammar check, and some errors cannot be verified. For example, an operation void foo(in short long) is an obvious error, but under a syntax check it just means the use of a variable called long
*   idldepend reports as erroneous an IDL file that is perfectly parsed by the ORB.  
    Please send me the file to check the error.
*   idldepend launches always the IDL compiler, even when no new files must be generated  
    Please verify that dependency files are generated (attribute force=yes not defined). These files are generated in the directory given by the dependsdir attribute.  
    In the directory with the dependencies, idldepend stores a file for each parsed IDL file; this file has the same name as the IDL file, adding a 4-digits hexadecimal number, and the extension .depends In this file, each line but the last one is prependend with:

    *   <: the rest of the line contains the name of a file referenced by the current file.  
        If the referenced file changes (its timestamp changes), this IDL file must be re-parsed.
    *   \>: the rest of the line contains the name of a file generated from the current IDL file.  
        If this file is older that the current IDL file or does not exist, it is needed to parse again the IDL file to re-generate any depending files.

    By checking the content of this file, you can verify why idldepend is re-launching the idl compiler; in most of the cases, because some file-to-be-generated is missing. If this is not the case, please send me the idl file to verify its behaviour.
*   If you get warnings like **"Error reading Messages File."** please note that the warning is issued by the JDK idl compiler, not by idldepend. This error can be solved in MacOsX by providing the location to the classes.jar file: `export CLASSPATH=/System/Library/Frameworks/JavaVM.framework/Classes/classes.jar`

History changes
---------------

| Version 1.30 : 10th June 2009                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Changed package for idldepend to net.coderazzi**, getting it consistent with my others programs, after a problem with my previous domain.                                                                                                                                                                                                                                                                                                                                                                                                             |

| Version 1.22: 4th May 2008                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| --- |
| **Included specific behaviour for Jacorb**  <br>Jacorb does not adhere to the standard OMG Java mapping: when defining IDL exceptions, Jacorb does not produce the Holder classes. From this version, _idldepend_ will not expect the associated Holder files when reading IDL exceptions.  <br>In addition, Jacorb implements its own logic to decide whether to generate Java files or not. _idldepend_ will trigger the idl compilation using the argument _\-forceOverwrite_, which disables such functionality -already provided by _idldepend_\-. |

| Version 1.21: 19th October 2007                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| --- |
| **Corrected handling of nested structs and unions**  <br>Nested structs were not included on its right scope.  <br>The same bug happened with definitions inside unions.  <br>Thanks to Jean Lepropre, who provided the patches to both errors.                                                                                                                                                                                                                                                                                                         |
| **Added support for constructed recursive types**  <br>Thanks again to Jean Lepropre, who wrote the grammar changes.                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| **Changed package for idldepend to com.byteslooser**  <br>Since the beginning, idldepend has been included in a package _idldepend_; this is the first release that I include it as _com.byteslooser.idldepend_, getting it consistent with my others programs.                                                                                                                                                                                                                                                                                         |

| Version 1.10: 12th December 2006                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| --- |
| Changed the behaviour of checkAll.  <br>If a file is included inside some scope (for example, the include directive appears inside a module definition), some compilers will generate the included types, whenever checkAll is set or not.  <br>Although this is a reasonable behaviour, other compilers, as Orbacus, will ignore those included types unless checkAll is set to true.  <br>idldepend mimics this behaviour, so, using Orbacus, it will dismiss the included types if checkAll is false.                                                |

| Version 1.0: 9th November 2006                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| --- |
| Supported more than one fileset as input. Thanks to Esmond Pitt for providing the solution.                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| Specialized output depending on the used compiler. In special, this solves an issue with JDK when the option serverTIE is used.  <br>As summary, idldepend tries now to mimic the behaviour of the compiler, even if the compiler does not comply with the standard Java Mapping.                                                                                                                                                                                                                                                                       |
| JavaCC 4.0 used                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| Revamped the documentation                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |

| Previous versions                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| --- |
| The updates done on previous versions are not part anymore of the documentation. Please visit the [old documentation](http://www.coderazzi.net/idldepend/olddoc.htm) if you have any interest on it                                                                                                                                                                                                                                                                                                                                                     |
| Thanks to everybody who submitted bugs. In special, thanks to those who also submitted code corrections: Brian Wallis, Jeff Downs, Guillaume Codina, Andreas Ebbert and Duane Griffin.                                                                                                                                                                                                                                                                                                                                                                  |
Bugs and Limitations
--------------------

*   CORBA compliance support  
    Please look to the section [above](#Version), to check the current compliance.
*   JDK  
    If the JDK IDL compiler fails, idldepend will not fail, and the ant task using idldepend will show: BUILD SUCCESSFUL. Unfortunately, this is a limitation on the JDK IDL compiler: when it fails, does not return a error code.
*   Performance  
    The main limitation of idldepend is currently its performance: it must duplicate most of the work that the compiler does to generate the files and therefore it can be expected it to require double the time that the compiler needs.  
    In fact, using the JDK compiler, it requires for long files with deep and recursive includes up to six times more. This time is only required for the first time, as the following times that idldepend is executed, it uses the created dependency files and then it is faster than the compiler (it doubles it speed).  
    That is, on its first execution, idldepend is much slower than using directly the IDL compiler, but following executions are faster than using the compiler. (and if there are no changes, it avoids of course the compilation of the generated .java files!).  
    This performance is due to the little experience I have with parsers. If you feel more confortable with it, feel free to improve it!
*   ORBs support  
    I included support initially for JDK, Jacorb, OpenORB and Orbacus ORBs. The support for Orbix was included afterwards by Jeff Downs.  
    Since then, most of the changes in idldepend have been the result of added functionality -like the preprocessor-, or to solve reported bugs. In special, I have not tracked the IDL compilers to verify new command line options. As a result, it is possible that the ORB version you are using it is not perfectly supported by idldepend. If this is the case, please report me the change(s) to enhance this program.