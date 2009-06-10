/**
 * File: JdkIDLToJavaMapping.java
 * Author: LuisM Pena
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package net.coderazzi.idldepend.idl;

/**
 * Specialization of IDLToJavaMapping to handle the deviations in the JDK The
 * deviations affect to the interfaces:<br>
 * 1- If side=server or serverTIE, the helper and holder should not be created.<br>
 * 2- If side=serverTIE, the POA file should not be created.<br>
 * For the arguments, see the IDLToJavaMapping constructor.<br>
 */
public class JdkIDLToJavaMapping extends IDLToJavaMapping {

   public JdkIDLToJavaMapping(IDLMapperUser user, boolean clientSide,
         boolean serverSide, boolean generateTIE, boolean generateAMI,
         boolean serverTIE) {
      super(user, clientSide, serverSide, generateTIE, generateAMI);
      this.serverTIE = serverTIE;
   }

   protected void mapInterface(String interfaceName, boolean abstractInterface,
         boolean localInterface, boolean excludeHolder, boolean clientSide,
         boolean serverSide, boolean generateTIE) {

      // note:local interfaces are not supported in the JDK

      String scope = getScopePath();
      interfaceName = reservedWordsCare(interfaceName);
      fileScoperNeeded(createFileName(scope, interfaceName, null),
            interfaceName);
      if (clientSide) {
         fileScoperNeeded(createFileName(scope, interfaceName, HELPER_EXT),
               interfaceName);
         fileScoperNeeded(createFileName(scope, "_" + interfaceName, STUB_EXT),
               interfaceName);
         if (!excludeHolder) {
            fileScoperNeeded(createFileName(scope, interfaceName, HOLDER_EXT),
                  interfaceName);
         }
      }
      if (!abstractInterface) {
         fileScoperNeeded(createFileName(scope, interfaceName, OPERATIONS_EXT),
               interfaceName);
         if (serverSide) {
            if (!serverTIE) {
               fileScoperNeeded(createFileName(scope, interfaceName, POA_EXT),
                     interfaceName);
            }
            if (generateTIE) {
               fileScoperNeeded(createFileName(scope, interfaceName, TIE_EXT),
                     interfaceName);
            }
         }
      }
   }

   /**
    * JDK creates as well a DefaultFactory file
    */
   public void mapValuetype(String valuetypeName, boolean withFactory) {
      super.mapValuetype(valuetypeName, withFactory);
      valuetypeName = reservedWordsCare(valuetypeName);
      fileScoperNeeded(createFileName(getScopePath(), valuetypeName,
            "DefaultFactory"), valuetypeName);
   }

   private boolean serverTIE;

}
