/**
 * File: OpenOrbIDLToJavaMapping.java
 * Author: LuisM Pena
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package com.byteslooser.idldepend.idl;

/**
 * Specialization of IDLToJavaMapping to handle the deviations in the jacorb
 * The deviations affect to the exceptions, not generating all the
 * required files. More specifically, it does not produce the Holder file
 */
public class JacorbIDLToJavaMapping extends IDLToJavaMapping {

   public JacorbIDLToJavaMapping(IDLMapperUser user, boolean clientSide,
         boolean serverSide, boolean generateTIE, boolean generateAMI) {
      super(user, clientSide, serverSide, generateTIE, generateAMI);
   }
   
   public void mapException(String exceptionName) {
       exceptionName = reservedWordsCare(exceptionName);
       String scope = getScopePath();
       fileNeeded(createFileName(scope, exceptionName, null), exceptionName);
       fileNeeded(createFileName(scope, exceptionName, HELPER_EXT), exceptionName);
   }

}

   
