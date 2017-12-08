package symbols;

import lexer.Tag;
import lexer.Word;

public class Type extends Word {
  public int width = 0; //記憶域割り当てに使う

  public Type(String s, int tag, int wd){
    super(s, tag);
    width = wd;
  }

  public static final Type
          Int = new Type("int", Tag.BASIC, 4),
          Float = new Type("float", Tag.BASIC, 8),
          Char = new Type("char", Tag.BASIC, 1),
          Bool = new Type("bool", Tag.BASIC, 1);

  //数値型 char, int floatの強制型変換を許す
  public static boolean numeric(Type p) {
    return p == Type.Char || p == Type.Int || p == Type.Float;
  }

  //異なる数値型に算術演算子が適用されているとき
  //演算結果は両者の最大の型とする
  public static Type max(Type p1, Type p2) {
    if (!numeric(p1) || !numeric(p2)) {
      return null;
    } else if (p1 == Type.Float || p2 == Type.Float) {
      return Type.Float;
    } else if (p1 == Type.Int || p2 == Type.Int) {
      return Type.Int;
    } else {
      return Type.Char;
    }
  }
}
