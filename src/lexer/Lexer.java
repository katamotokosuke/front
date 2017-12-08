package lexer;

import symbols.Type;
import java.io.IOException;
import java.util.*;

public class Lexer {
  public static int line = 1;
  public char peek = ' '; //先読み記号
  private Hashtable<String, Object> words = new Hashtable();

  //予約語を登録する
  private void reserve(Word w) {
    words.put(w.lexeme, w);
  }

  public Lexer() {
    reserve(new Word("if", Tag.IF));
    reserve(new Word("else", Tag.ELSE));
    reserve(new Word("while", Tag.WHILE));
    reserve(new Word("do", Tag.DO));
    reserve(new Word("break", Tag.BREAK));
    reserve(Word.True);
    reserve(Word.False);
    //基本型
    reserve(Type.Int);
    reserve(Type.Char);
    reserve(Type.Bool);
    reserve(Type.Float);
  }

  //peekに次の入力記号を読み込む
  public void readch() throws IOException {
    peek = (char) System.in.read();
  }

  /*複合トークンの認識するために再利用
  example: 入力"<"を走査した時にreadch('=')を呼び出してpeekが"="かどうか調べる
  */
  public boolean readch(char c) throws IOException {
    readch();
    if (peek != c) return false;
    peek = ' ';
    return true;
  }

  public Token scan() throws IOException {
    //43~48までで空白, タブを取り除く. 改行の時は行数を+1
    for (; ; readch()) {
      if (peek == ' ' || peek == '\t') continue;
      else if (peek == '\n') line = line + 1;
      else break;
    }

    switch (peek) {
      case '&': if (readch('&')) return Word.and; else return new Token('&');
      case '|': if (readch('|')) return Word.or; else return new Token('|');
      case '=': if (readch('=')) return Word.eq; else return new Token('=');
      case '!': if (readch('=')) return Word.ne; else return new Token('!');
      case '<': if (readch('=')) return Word.le; else return new Token('<');
      case '>': if (readch('=')) return Word.ge; else return new Token('>');
    }

    if (Character.isDigit(peek)){
      int v = 0;
      do {
        v = 10 * v + Character.digit(peek, 10); readch();
      } while(Character.isDigit(peek));

      if (peek != '.') return new Num(v);
      float x = v;
      float d = 10;

      for (;;){
        readch();
        if (!Character.isDigit(peek)) break;
        x = x + Character.digit(peek, 10)/d; d = d * 10;
      }
      return new Real(x);
    }

    if (Character.isDigit(peek)) {
      StringBuffer b  = new StringBuffer();
      do {
        b.append(peek);
        readch();
      } while (Character.isLetterOrDigit(peek));
      String s = b.toString();
      Word w = (Word) words.get(s);
      if (w != null) return w;
      w = new Word(s, Tag.ID);
      words.put(s, w);
      return w;
    }

    Token tok = new Token(peek);
    peek = ' ';
    return tok;
  }
}
