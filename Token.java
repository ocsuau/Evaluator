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
        return new Token(Toktype.OP, -1, c);
    }

    /*Método donde retornamos la instancia que retorna el constructor. Acceder a este método significa que el token que quieren
    crear es uno de los dos carácteres que representan el paréntesis*/
    static Token tokParen(char c) {
        return new Token(Toktype.PAREN, -1, c);
    }

    //Getter del tipo de token.
    public Toktype getTtype(){
        return this.ttype;
    }

    //Getter del carácter que representa al operador del token
    public char getOp() {

        /*Comprobamos que a dicho método no lo está llamando una instancia de "Token" que sea de tipo "NUMBER". En dicho caso,
        provocamos una excepción*/
        if (this.tk != ' ') {
            return this.tk;
        }
        throw new RuntimeException("Token de tipo NUMBER intentando consultar su carácter");
    }

    //Getter del valor que representa el valor del token
    public int getValue() {

        /*Comprobamos que este método no está siendo consultado por una instancia de "Token" de un tipo distinto a "NUMBER". Si
        fuera así, provocamos una excepción.*/
        if (this.tk == ' ') {
            return this.value;
        }
        throw new RuntimeException("Token de tipo distinto a NUMBER intentando consultar su número");
    }

    /*Método "toString", donde retornamos el carácter de la instancia siempre que sea de tipo distinto a "NUMBER". En caso contrario,
    provocamos una excepción*/
    public String toString() {
        if (this.tk != ' ') {
            return "" + this.tk;
        }
        throw new RuntimeException("Token de tipo distinto a NUMBER intentando consultar su número");
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
        LinkedList<Token> charToken = new LinkedList<>();

        /*Definimos un bloque "try" para poder manejar las posibles excepcionesque se pudieran generar*/
        try {
            insertExpr(charToken, expr);
            return charToken.toArray(new Token[charToken.size()]);

            //Capturamos la posible excepción Runtime.
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

            /*En caso de tener una excepción diferente a Runtime, capturamos la excepción en variable "Exception" y provocamos una
            "RuntimeException"*/
        } catch (Exception e) {
            throw new RuntimeException("Excepción que no es subclase de 'RuntimeException'");
        }
        return null;
    }

    //Método donde generaremos la lista de tokens a partir del String que nos pasan como parámetro.
    private static void insertExpr(LinkedList<Token> charToken, String expr) {

        /*La variable "number" nos permitirá generar números enteros de más de una cifra a partir de los carácteres que forman el
        número (Si en el bucle estamos tratando un carácter que representa un número, no lo podemos añadir directamente a la cola
        porque puede que el carácter siguiente sea otro número y, por lo tanto, estemos tratando con un número de más de una cifra)*/
        StringBuilder number = new StringBuilder();

        char c;

        //Recorremos la expresión
        for(int i = 0; i < expr.length(); i++){

            //Si el carácter es representa un número
            if (expr.charAt(i) >= '0' && expr.charAt(i) <= '9') {

                /*Añadimos el número "number"*/
                number.append(expr.charAt(i));
            }
            else{
                //Llegar a este punto significa que el carácter que estamos tratando no representa un número.

                /*Antes de comprobar qué carácter estamos tratando, comprobaremos el contenido de "number" (Llegar a este punto
                significa que el número que hayamos podido almacenar en "number" no está formado por más cifras, por eso comprobamos
                si su longitud es distinta a 0 (que valga 0 significa que no hemos almacenado ningún número). En caso afirmativo,
                introducimos el objeto de tipo "NUMBER" llamando a "tokNumber" y le pasamos el resultado de llamar al método
                "parseInt" de la clase "Integer". Su retorno lo añadimos a la cola)*/
                if (number.length() != 0) {
                    charToken.offer(tokNumber(Integer.parseInt(number.toString())));

                    /*Seguidamente vaciamos "number" para indicar que el próximo número que nos encontremos en la
                    expresión será el primer dígito de un nuevo número y, por lo tanto, un nuevo objeto "Token" de tipo "NUMBER".*/
                    number.delete(0, number.length());
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
                    c = expr.charAt(i);
                    if (c == '-' && (i == 0 || (charToken.getLast().getTtype() == Toktype.OP || (charToken.getLast().getTtype() == Toktype.PAREN && charToken.getLast().getOp() == '(')))) {
                        charToken.offer(tokNumber(-1));
                        charToken.offer(tokOp('*'));
                    } else {
                        charToken.offer(tokOp(c));
                    }
                }
            }
        }
        /*Una vez finalizado el bucle, puede darse el caso que en "count" tengamos algún valor que no hayamos añadido en la cola.
        Si es así, lo insertamos."*/
        if (number.length() != 0) {
            charToken.offer(tokNumber(Integer.parseInt(number.toString())));
        }
    }
}
