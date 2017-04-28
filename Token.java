import java.util.LinkedList;
import java.util.Queue;

public class Token {
    enum Toktype {
        NUMBER, OP, PAREN
    }

    private Toktype ttype;
    private int value;
    private char tk;

    private Token(Toktype t, int v, char c) {
        this.ttype = t;
        this.value = v;
        this.tk = c;
    }

    static Token tokNumber(int value) {
        return new Token(Toktype.NUMBER, value, ' ');
    }

    static Token tokOp(char c) {
        return new Token(Toktype.OP, 0, c);
    }

    static Token tokParen(char c) {
        return new Token(Toktype.PAREN, 0, c);
    }

    public Toktype getTtype(){
        return this.ttype;
    }

    public char getOp(){
        return this.tk;
    }

    public int getValue(){
        return this.value;
    }

    public String toString() {
        return "";
    }

    public boolean equals(Object o) {
        if(o instanceof Token){
            Token t = (Token) o;
            return this.ttype == t.ttype && this.value == t.value && this.tk == t.tk;
        }
        return false;
    }

    public static Token[] getTokens(String expr) {
        Queue<Token> charToken = new LinkedList<>();
        insertExpr(charToken, expr);
        return charToken.toArray(new Token[charToken.size()]);
    }

    private static void insertExpr(Queue<Token> charToken, String expr){
        int count = 0;
        for(int i = 0; i < expr.length(); i++){
            if(expr.charAt(i) >= 48 && expr.charAt(i) <= 57){
                count *= 10;
                count += ((int) expr.charAt(i)) - 48;
            }
            else{
                if(count != 0){
                    charToken.offer(tokNumber(count));
                    count = 0;
                }

                if(expr.charAt(i) == '(' || expr.charAt(i) == ')'){
                    charToken.offer(tokParen(expr.charAt(i)));
                }

                else if(expr.charAt(i) != ' '){
                    charToken.offer(tokOp(expr.charAt(i)));
                }
            }
        }
        if(count != 0){
            charToken.offer(tokNumber(count));
        }
    }
}
