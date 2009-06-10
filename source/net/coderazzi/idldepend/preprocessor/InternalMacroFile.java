/**
 * File: InternalMacroFile.java
 * Author: LuisM Pena
 * Last update: 1.00, 9th November 2006
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package net.coderazzi.idldepend.preprocessor;


import java.util.ArrayList;
import java.util.List;

import net.coderazzi.idldepend.javacc.generated.ParseException;
import net.coderazzi.idldepend.javacc.generated.PreprocessorConstants;
import net.coderazzi.idldepend.javacc.generated.Token;

/**
 * Definition of the internal macro '__FILE__'.
 * It needs no parameters
 */
class InternalMacroFile extends Macro {

   /**
    * The constructor requires the PreprocessorController, to be able to retrieve the
    * file being processed
    */
   public InternalMacroFile(PreprocessorController controller) {
      complex = false;
      numberOfParameters = 0;
      name = "__FILE__";
      this.controller = controller;
   }

   public List expand(List parameters) throws ParseException {
      Token fileInfo = new Token();
      fileInfo.kind = PreprocessorConstants.STRING;
      fileInfo.image = "\"" + controller.getParsingFile() + "\"";
      List ret = new ArrayList();
      ret.add(fileInfo);
      return ret;
   }

   private PreprocessorController controller;
}
