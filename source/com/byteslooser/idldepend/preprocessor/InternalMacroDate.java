/**
 * File: InternalMacroDate.java
 * Author: LuisM Pena
 * Last update: 1.00, 9th November 2006
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package com.byteslooser.idldepend.preprocessor;


import java.io.File;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.byteslooser.idldepend.javacc.generated.ParseException;
import com.byteslooser.idldepend.javacc.generated.PreprocessorConstants;
import com.byteslooser.idldepend.javacc.generated.Token;

/**
 * Definition of the internal macro '__DATE__', '__TIME__', '__TIMESTAMP__'
 * It needs no parameters
 */
class InternalMacroDate extends Macro {

   /**
    * The constructor requires the PreprocessorController, to be able to retrieve the
    * file being processed
    */
   public InternalMacroDate(PreprocessorController controller, String name,
         String format) {
      complex = false;
      numberOfParameters = 0;
      this.name = name;
      this.controller = controller;
      this.format = new SimpleDateFormat(format);
   }

   public List expand(List parameters) throws ParseException {
      Date date = new Date(new File(controller.getParsingFile()).lastModified());
      Token formatedDate = new Token();
      formatedDate.kind = PreprocessorConstants.STRING;
      formatedDate.image = "\"" + format.format(date) + "\"";
      List ret = new ArrayList();
      ret.add(formatedDate);
      return ret;
   }

   private PreprocessorController controller;

   private DateFormat format;
}
