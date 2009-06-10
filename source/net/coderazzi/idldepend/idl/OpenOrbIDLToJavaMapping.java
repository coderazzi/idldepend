/**
 * File: OpenOrbIDLToJavaMapping.java
 * Author: LuisM Pena
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package net.coderazzi.idldepend.idl;

/**
 * Specialization of IDLToJavaMapping to handle the deviations in the openorb
 * The deviations affect to the local interfaces, not generating all the
 * required files. More specifically, it does not produce the _NAMELocalBase
 * neither NAMELocalTie For the arguments, see the IDLToJavaMapping constructor
 */
public class OpenOrbIDLToJavaMapping extends IDLToJavaMapping {

   public OpenOrbIDLToJavaMapping(IDLMapperUser user, boolean clientSide,
         boolean serverSide, boolean generateTIE, boolean generateAMI) {
      super(user, clientSide, serverSide, generateTIE, generateAMI);
   }

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
         if (serverSide && !localInterface) {
            fileScoperNeeded(createFileName(scope, interfaceName, POA_EXT),
                  interfaceName);
            if (generateTIE) {
               fileScoperNeeded(createFileName(scope, interfaceName, TIE_EXT),
                     interfaceName);
            }
         }
      }
   }
}

   
