/* Generated By:JavaCC: Do not edit this line. PreprocessorConstants.java */
package net.coderazzi.idldepend.javacc.generated;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface PreprocessorConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int PREPLINE = 2;
  /** RegularExpression Id. */
  int LINE = 3;
  /** RegularExpression Id. */
  int LINE2 = 4;
  /** RegularExpression Id. */
  int DEFINE = 5;
  /** RegularExpression Id. */
  int UNDEF = 6;
  /** RegularExpression Id. */
  int IFDEF = 7;
  /** RegularExpression Id. */
  int IFNDEF = 8;
  /** RegularExpression Id. */
  int IF = 9;
  /** RegularExpression Id. */
  int ENDIF = 10;
  /** RegularExpression Id. */
  int ELSE = 11;
  /** RegularExpression Id. */
  int ELIF = 12;
  /** RegularExpression Id. */
  int ERROR = 13;
  /** RegularExpression Id. */
  int PRAGMA = 14;
  /** RegularExpression Id. */
  int INCLUDEQ = 15;
  /** RegularExpression Id. */
  int LINE3 = 16;
  /** RegularExpression Id. */
  int LINE4 = 17;
  /** RegularExpression Id. */
  int UNKNOWN = 18;
  /** RegularExpression Id. */
  int EOL = 19;
  /** RegularExpression Id. */
  int LEFTPAR = 20;
  /** RegularExpression Id. */
  int RIGHTPAR = 21;
  /** RegularExpression Id. */
  int COMMA = 22;
  /** RegularExpression Id. */
  int SPECIAL = 23;
  /** RegularExpression Id. */
  int STRING = 24;
  /** RegularExpression Id. */
  int CHAR = 25;
  /** RegularExpression Id. */
  int SPACE = 26;
  /** RegularExpression Id. */
  int ID = 27;
  /** RegularExpression Id. */
  int OTHER = 28;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"\\r\"",
    "<PREPLINE>",
    "<LINE>",
    "<LINE2>",
    "\"//define\"",
    "\"//undef\"",
    "\"//ifdef\"",
    "\"//ifndef\"",
    "\"//if\"",
    "\"//endif\"",
    "\"//else\"",
    "\"//elif\"",
    "\"//error\"",
    "\"//pragma\"",
    "\"//canIncludeFile\"",
    "<LINE3>",
    "<LINE4>",
    "\"//\"",
    "\"\\n\"",
    "\"(\"",
    "\")\"",
    "\",\"",
    "<SPECIAL>",
    "<STRING>",
    "<CHAR>",
    "<SPACE>",
    "<ID>",
    "<OTHER>",
  };

}