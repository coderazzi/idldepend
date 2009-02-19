/**
 * File: InternalMacroLine.java
 * Author: LuisM Pena
 * Last update: 1.00, 9th November 2006
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package com.byteslooser.idldepend.preprocessor;


import java.util.ArrayList;
import java.util.List;

import com.byteslooser.idldepend.javacc.generated.ParseException;
import com.byteslooser.idldepend.javacc.generated.PreprocessorConstants;
import com.byteslooser.idldepend.javacc.generated.Token;

/**
 * Definition of the internal macro '__LINE__'.
 * It needs no parameters
 */
class InternalMacroLine extends Macro {

   /**
    * The constructor requires the PreprocessorController, to be able to retrieve the
    * file being processed
    */
   public InternalMacroLine(PreprocessorController controller) {
      complex = false;
      numberOfParameters = 0;
      name = "__LINE__";
      this.controller = controller;
   }

   public List expand(List parameters) throws ParseException {
      Token line = new Token();
      line.kind = PreprocessorConstants.OTHER; // no really...
      line.image = String.valueOf(controller.getLine());
      List ret = new ArrayList();
      ret.add(line);
      return ret;
   }

   private PreprocessorController controller;
}
