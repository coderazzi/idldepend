/* Generated By:JavaCC: Do not edit this line. Evaluator.java */
package net.coderazzi.idldepend.javacc.generated;

import java.io.StringReader;

public class Evaluator implements EvaluatorConstants {

  public Evaluator(String toEvaluate){
    this(new StringReader(toEvaluate));
  }

  public boolean evaluate() throws ParseException{
    return evaluateInput();
  }

  private boolean toBoolean(int n) {
    return n!=0;
  }

  private int toInt(boolean b) {
    return b? 1 : 0;
  }

  final public boolean evaluateInput() throws ParseException {
  int n;
    n = prec0();
    jj_consume_token(0);
                   {if (true) return n!=0;}
    throw new Error("Missing return statement in function");
  }

  final public int prec0() throws ParseException {
  int ret, tmp;
    ret = prec1();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ORLOGICAL:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      jj_consume_token(ORLOGICAL);
      tmp = prec1();
                                 ret=toInt(toBoolean(ret)||toBoolean(tmp));
    }
     {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public int prec1() throws ParseException {
  int ret, tmp;
    ret = prec2();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ANDLOGICAL:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
      jj_consume_token(ANDLOGICAL);
      tmp = prec2();
                                  ret=toInt(toBoolean(ret)&&toBoolean(tmp));
    }
     {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public int prec2() throws ParseException {
  int n1, n2;
    n1 = prec3();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ORBINARY:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_3;
      }
      jj_consume_token(ORBINARY);
      n2 = prec3();
                               n1|=n2;
    }
     {if (true) return n1;}
    throw new Error("Missing return statement in function");
  }

  final public int prec3() throws ParseException {
  int n1, n2;
    n1 = prec4();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NOTBINARY:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_4;
      }
      jj_consume_token(NOTBINARY);
      n2 = prec4();
                                n1^=n2;
    }
     {if (true) return n1;}
    throw new Error("Missing return statement in function");
  }

  final public int prec4() throws ParseException {
  int n1, n2;
    n1 = prec5();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ANDBINARY:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_5;
      }
      jj_consume_token(ANDBINARY);
      n2 = prec5();
                                n1&=n2;
    }
     {if (true) return n1;}
    throw new Error("Missing return statement in function");
  }

  final public int prec5() throws ParseException {
  int ret, tmp;
    ret = prec6();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EQUAL:
      label_6:
      while (true) {
        jj_consume_token(EQUAL);
        tmp = prec6();
                                ret=toInt(ret==tmp);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case EQUAL:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_6;
        }
      }
      break;
    default:
      jj_la1[7] = jj_gen;
      label_7:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NOTEQUAL:
          ;
          break;
        default:
          jj_la1[6] = jj_gen;
          break label_7;
        }
        jj_consume_token(NOTEQUAL);
        tmp = prec6();
                                ret=toInt(ret!=tmp);
      }
    }
     {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public int prec6() throws ParseException {
  int ret, tmp;
    ret = prec7();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOTGREATER:
      label_8:
      while (true) {
        jj_consume_token(NOTGREATER);
        tmp = prec7();
                                  ret=toInt(ret<=tmp);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NOTGREATER:
          ;
          break;
        default:
          jj_la1[8] = jj_gen;
          break label_8;
        }
      }
      break;
    case MINOR:
      label_9:
      while (true) {
        jj_consume_token(MINOR);
        tmp = prec7();
                                  ret=toInt(ret<tmp);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MINOR:
          ;
          break;
        default:
          jj_la1[9] = jj_gen;
          break label_9;
        }
      }
      break;
    case NOTMINOR:
      label_10:
      while (true) {
        jj_consume_token(NOTMINOR);
        tmp = prec7();
                                  ret=toInt(ret>=tmp);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NOTMINOR:
          ;
          break;
        default:
          jj_la1[10] = jj_gen;
          break label_10;
        }
      }
      break;
    default:
      jj_la1[12] = jj_gen;
      label_11:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case GREATER:
          ;
          break;
        default:
          jj_la1[11] = jj_gen;
          break label_11;
        }
        jj_consume_token(GREATER);
        tmp = prec7();
                                  ret=toInt(ret>tmp);
      }
    }
     {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public int prec7() throws ParseException {
  int ret, tmp;
    ret = prec8();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SHIFTLEFT:
      label_12:
      while (true) {
        jj_consume_token(SHIFTLEFT);
        tmp = prec8();
                                  ret<<=tmp;
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SHIFTLEFT:
          ;
          break;
        default:
          jj_la1[13] = jj_gen;
          break label_12;
        }
      }
      break;
    default:
      jj_la1[15] = jj_gen;
      label_13:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SHIFTRIGHT:
          ;
          break;
        default:
          jj_la1[14] = jj_gen;
          break label_13;
        }
        jj_consume_token(SHIFTRIGHT);
        tmp = prec8();
                                  ret>>=tmp;
      }
    }
     {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public int prec8() throws ParseException {
  int ret,tmp;
    ret = prec9();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ADD:
      label_14:
      while (true) {
        jj_consume_token(ADD);
        tmp = prec9();
                             ret+=tmp;
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ADD:
          ;
          break;
        default:
          jj_la1[16] = jj_gen;
          break label_14;
        }
      }
      break;
    default:
      jj_la1[18] = jj_gen;
      label_15:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SUBST:
          ;
          break;
        default:
          jj_la1[17] = jj_gen;
          break label_15;
        }
        jj_consume_token(SUBST);
        tmp = prec9();
                             ret-=tmp;
      }
    }
     {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public int prec9() throws ParseException {
  int ret,tmp;
    ret = prec10();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case MULT:
      label_16:
      while (true) {
        jj_consume_token(MULT);
        tmp = prec10();
                               ret*=tmp;
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MULT:
          ;
          break;
        default:
          jj_la1[19] = jj_gen;
          break label_16;
        }
      }
      break;
    case DIV:
      label_17:
      while (true) {
        jj_consume_token(DIV);
        tmp = prec10();
                               ret/=tmp;
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DIV:
          ;
          break;
        default:
          jj_la1[20] = jj_gen;
          break label_17;
        }
      }
      break;
    default:
      jj_la1[22] = jj_gen;
      label_18:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MODULE:
          ;
          break;
        default:
          jj_la1[21] = jj_gen;
          break label_18;
        }
        jj_consume_token(MODULE);
        tmp = prec10();
                               ret%=tmp;
      }
    }
     {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public int prec10() throws ParseException {
  int ret;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
    case DEFINED:
    case ID:
    case SUBST:
    case NOT:
    case 26:
      ret = prec11();
                   {if (true) return ret;}
      break;
    case NEG:
      jj_consume_token(NEG);
      ret = prec11();
                            {if (true) return ~ret;}
      break;
    default:
      jj_la1[23] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public int prec11() throws ParseException {
  int ret;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
    case DEFINED:
    case ID:
    case SUBST:
    case 26:
      ret = prec12();
                   {if (true) return ret;}
      break;
    case NOT:
      jj_consume_token(NOT);
      ret = prec12();
                             {if (true) return toInt(!toBoolean(ret));}
      break;
    default:
      jj_la1[24] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public int prec12() throws ParseException {
  int ret;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
    case DEFINED:
    case ID:
    case 26:
      ret = prec13();
                   {if (true) return ret;}
      break;
    case SUBST:
      jj_consume_token(SUBST);
      ret = prec13();
                               {if (true) return -ret;}
      break;
    default:
      jj_la1[25] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public int prec13() throws ParseException {
  int ret;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
    case ID:
      ret = number();
                   {if (true) return ret;}
      break;
    case DEFINED:
      jj_consume_token(DEFINED);
      jj_consume_token(26);
      ret = prec0();
      jj_consume_token(27);
                                        {if (true) return toInt(toBoolean(ret));}
      break;
    case 26:
      jj_consume_token(26);
      ret = prec0();
      jj_consume_token(27);
                              {if (true) return ret;}
      break;
    default:
      jj_la1[26] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public int number() throws ParseException {
  Token n;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
      n = jj_consume_token(NUMBER);
    String number = n.toString();
    if (number.startsWith("0x")) {
      {if (true) return Integer.parseInt(number.substring(2),16);}
    }
    if (number.endsWith("L") || number.endsWith("l")) {
      number=number.substring(0,number.length()-1);
    }
    if (number.endsWith("U") || number.endsWith("u")) {
      number=number.substring(0,number.length()-1);
    }
    {if (true) return Integer.parseInt(number);}
      break;
    case ID:
      jj_consume_token(ID);
          {if (true) return 0;}
      break;
    default:
      jj_la1[27] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public EvaluatorTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[28];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x40,0x80,0x100,0x200,0x400,0x800,0x1000,0x800,0x2000,0x4000,0x8000,0x10000,0xe000,0x20000,0x40000,0x20000,0x80000,0x100000,0x80000,0x200000,0x400000,0x800000,0x600000,0x7100038,0x6100038,0x4100038,0x4000038,0x28,};
   }

  /** Constructor with InputStream. */
  public Evaluator(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Evaluator(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new EvaluatorTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Evaluator(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new EvaluatorTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Evaluator(EvaluatorTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(EvaluatorTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List jj_expentries = new java.util.ArrayList();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[28];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 28; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 28; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
