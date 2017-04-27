import java.util.LinkedList;
import java.util.Queue;

public class Token {
    enum Toktype {
        NUMBER, OP, PAREN
    }

    // Pensa a implementar els "getters" d'aquests atributs
    private Toktype ttype;
    private int value;
    private char tk;



    // Constructor privat. Evita que es puguin construir objectes Token externament
    private Token(Toktype t, int v, char c) {
        this.ttype = t;
        this.value = v;
        this.tk = c;
    }

    // Torna un token de tipus "NUMBER"
    static Token tokNumber(int value) {
        return new Token(Toktype.NUMBER, value, ' ');
    }

    // Torna un token de tipus "OP"
    static Token tokOp(char c) {
        return new Token(Toktype.OP, 0, c);
    }

    // Torna un token de tipus "PAREN"
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

    // Mostra un token (conversió a String)
    public String toString() {
        return "";
    }

    // Mètode equals. Comprova si dos objectes Token són iguals
    public boolean equals(Object o) {
        if(o instanceof Token){
            Token t = (Token) o;
            return this.ttype == t.ttype && this.value == t.value && this.tk == t.tk;
        }
        return false;
    }

    // A partir d'un String, torna una llista de tokens
    public static Token[] getTokens(String expr) {
        Queue<Token> charToken = new LinkedList<>();
        insertExpr(charToken, expr);
        return charToken.toArray(new Token[charToken.size()]);
    }

    private static void insertExpr(Queue<Token> charToken, String expr){
        for(int i = 0, count = 0; i < expr.length(); i++){
            if(expr.charAt(i) >= 48 && expr.charAt(i) <= 57){
                count *= 10;
                count += ((int) expr.charAt(i)) - 48;

                if(i == expr.length() - 1 && count != 0){
                    charToken.offer(tokNumber(count));
                }
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
    }
}
