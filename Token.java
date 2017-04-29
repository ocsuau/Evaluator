import java.util.LinkedList;
import java.util.Queue;

//Clase "Token", donde almacenamos las características de los token que vayamos creando.
public class Token {

    //Generamos un ENUM del tipo de token para evitar que se puedan crear tokens distintos a los establecidos.
    enum Toktype {
        NUMBER, OP, PAREN
    }

    /*Atributos de los objetos, donde almacenamos el tipo de token (ttype), el valor en caso de ser un número (value) y el carácter
    en caso de ser un operador o los paréntesis (tk)*/
    private Toktype ttype;
    private int value;
    private char tk;

    /*Constructor privado, lo que nos obliga a realizar factoría*/
    private Token(Toktype t, int v, char c) {
        this.ttype = t;
        this.value = v;
        this.tk = c;
    }

    /*Método donde retornamos la instancia que retorna el constructor. Acceder a este método significa que el token que quieren
    crear es un número*/
    static Token tokNumber(int value) {
        return new Token(Toktype.NUMBER, value, ' ');
    }

    /*Método donde retornamos la instancia que retorna el constructor. Acceder a este método significa que el token que quieren
    crear es un operador*/
    static Token tokOp(char c) {
        return new Token(Toktype.OP, 0, c);
    }

    /*Método donde retornamos la instancia que retorna el constructor. Acceder a este método significa que el token que quieren
    crear es uno de los dos carácteres que representan el paréntesis*/
    static Token tokParen(char c) {
        return new Token(Toktype.PAREN, 0, c);
    }

    //Getter del tipo de token.
    public Toktype getTtype(){
        return this.ttype;
    }

    //Getter del carácter que representa al operador del token
    public char getOp(){
        return this.tk;
    }

    //Getter del valor que representa el valor del token
    public int getValue(){
        return this.value;
    }

    public String toString() {
        return "";
    }

    /*Método equals donde comparamos dos tokens*/
    public boolean equals(Object o) {
        if(o instanceof Token){
            Token t = (Token) o;
            return this.ttype == t.ttype && this.value == t.value && this.tk == t.tk;
        }
        return false;
    }

    /*Método donde generamos un array de tokens a partir del String que nos pasan como parámetro. (Facilita las operaciones
    posteriores)*/
    public static Token[] getTokens(String expr) {

        /*En la cola "charToken" almacenaremos los tokens que vayamos generando en el método "insertExpr" a partir de "expr". Posteriormente,
        convertiremos la lista en un array de tokens y lo retornaremos*/
        Queue<Token> charToken = new LinkedList<>();
        insertExpr(charToken, expr);
        return charToken.toArray(new Token[charToken.size()]);
    }

    //Método donde generaremos la lista de tokens a partir del String que nos pasan como parámetro.
    private static void insertExpr(Queue<Token> charToken, String expr){

        /*La variable "count" nos permitirá generar números enteros de más de una cifra a partir de los carácteres que forman el
        número (Si en el bucle estamos tratando un carácter que representa un número, no lo podemos añadir directamente a la cola
        porque puede que el carácter siguiente sea otro número y, por lo tanto, estemos tratando con un número de más de una cifra)*/
        int count = 0;

        //Recorremos la expresión
        for(int i = 0; i < expr.length(); i++){

            //Si el carácter es representa un número
            if (expr.charAt(i) >= '0' && expr.charAt(i) <= '9') {
                /*Multiplicamos por 10 el valor de "count" para que, al sumarle el dígito que estamos tratando, se forme el
                verdadero número de más de una cifra. (Funciona aunque no hayamos almacenado nada anteriormente en "count", porque
                por defecto valdrá 0 y al multiplicarse por 10 no se verá afectado)*/
                count *= 10;

                /*Pasamos el carácter a un número entero que corresponde al valor del carácter. (Podríamos utilizar el método
                "Integer.parseInt()", pero dicho método solo acepta, como parámetro, un String, y de esta otra forma me parece más
                sencillo)*/
                count += ((int) expr.charAt(i)) - 48;
            }
            else{
                //Llegar a este punto significa que el carácter que estamos tratando no representa un número.

                /*Antes de comprobar qué carácter estamos tratando, comprobaremos el contenido de "count" (Llegar a este punto
                significa que el número que hayamos podido almacenar en "count" no está formado por más cifras, por eso comprobamos
                si su valor es distinto a 0 (que valga 0 significa que no hemos almacenado ningún número). En caso afirmativo,
                introducimos el objeto de tipo "NUMBER" llamando a "tokNumber" y le pasamos el valor de "count" como parámetro
                en la cola)*/
                if(count != 0){
                    charToken.offer(tokNumber(count));
                    count = 0;
                }

                /*Si el carácter que estamos tratando es un paréntesis, insertamos en la cola el objeto de tipo "PAREN" llamando
                al método "tokParen"*/
                if(expr.charAt(i) == '(' || expr.charAt(i) == ')'){
                    charToken.offer(tokParen(expr.charAt(i)));
                }

                /*Si no estamos tratando un paréntesis, comprobamos que no estemos tratando un espacio. En caso afirmativo,
                significa que estamos tratando un operador y, por lo tanto, insertamos en la cola el objeto de tipo "OP" llamando
                al método "tokOp"*/
                else if(expr.charAt(i) != ' '){
                    charToken.offer(tokOp(expr.charAt(i)));
                }
            }
        }
        /*Una vez finalizado el bucle, puede darse el caso que en "count tengamos algún valor que no hayamos añadido en la cola.
        Si es así, lo insertamos."*/
        if(count != 0){
            charToken.offer(tokNumber(count));
        }
    }
}
