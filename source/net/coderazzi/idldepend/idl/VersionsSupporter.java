/**
 * File: VersionsSupporter.java
 * Author: LuisM Pena
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package net.coderazzi.idldepend.idl;

import net.coderazzi.idldepend.TaskAttributesHandler.CompilerAttribute;
import net.coderazzi.idldepend.TaskAttributesHandler.SideAttribute;

/**
 * Class used to handle deviations from the standard/default behaviour
 */
public class VersionsSupporter {

   /**
    * All the deviations can currently be decided by inspecting the used
    *   compiler and the type of generation to perform 
    * @param compiler
    * @param side
    */
   public VersionsSupporter(CompilerAttribute compiler, SideAttribute side) {
      this.compiler = compiler;
      this.side = side;
   }

   /**
    * Factory method: creates a IDLToJavaMapping instance, according to the
    * compiler used The arguments are those required by the IDLToJavaMapping
    * constructor
    */
   public IDLToJavaMapping createIdlToJavaMapping(IDLMapperUser user,
         boolean clientSide, boolean serverSide, boolean generateTIE,
         boolean generateAMI) {
      if (compiler.isOpenORB()) {
         return new OpenOrbIDLToJavaMapping(user, clientSide, serverSide,
               generateTIE, generateAMI);
      } else if (compiler.isJdkOrIbm()) {
         return new JdkIDLToJavaMapping(user, clientSide, serverSide,
               generateTIE, generateAMI, side.isServerTie());
      } else if (compiler.isJacorb()) {
          return new JacorbIDLToJavaMapping(user, clientSide, serverSide,
             generateTIE, generateAMI);
      }
      return new IDLToJavaMapping(user, clientSide, serverSide, generateTIE,
            generateAMI);
   }

   private CompilerAttribute compiler;

   private SideAttribute side;
}
