import java.util.LinkedList;

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
        return new Token(Toktype.NUMBER, value, (char) 0);
    }

    /*Método donde retornamos la instancia que retorna el constructor. Acceder a este método significa que el token que quieren
    crear es un operador*/
    static Token tokOp(char c) {
        /*Comprobamos si el parámetro que nos han pasado es uno de los carácteres con los que trabajamos para realizar los cálculos.
        En caso contrario, provocamos una excepción*/
        if (opExists(c)) {
            return new Token(Toktype.OP, -1, c);
        }
        throw new RuntimeException("Se ha introducido un carácter que no representa a uno de los carácteres que nuestro algoritmo comprende como operador");
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
        if (this.tk != (char) 0) {
            return this.tk;
        }
        throw new RuntimeException("Token de tipo NUMBER intentando consultar su carácter");
    }

    //Getter del valor que representa el valor del token
    public int getValue() {

        /*Comprobamos que este método no está siendo consultado por una instancia de "Token" de un tipo distinto a "NUMBER". Si
        fuera así, provocamos una excepción.*/
        if (this.tk == (char) 0) {
            return this.value;
        }
        throw new RuntimeException("Token de tipo DISTINTO a NUMBER intentando consultar su número");
    }

    /*Método "toString", donde, en caso de acceda una instancia tipo "NUMBER", retornamos su valor. En su defecto, retornamos el
    carácter que representa a la instancia*/
    public String toString() {
        return (this.ttype == Toktype.NUMBER) ? "" + this.value : "" + this.tk;
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

        /*En la lista "charToken" almacenaremos los tokens que vayamos generando en el método "insertExpr" a partir de "expr". Posteriormente,
        convertiremos la lista en un array de tokens y lo retornaremos*/
        LinkedList<Token> charToken = new LinkedList<>();
        insertExpr(charToken, expr);
        return charToken.toArray(new Token[charToken.size()]);
    }

    //Método donde generaremos la lista de tokens a partir del String que nos pasan como parámetro.
    private static void insertExpr(LinkedList<Token> charToken, String expr) {

        /*La variable "number" nos permitirá generar números enteros de más de una cifra a partir de los carácteres que forman el
        número (Si en el bucle estamos tratando un carácter que representa un número, no lo podemos añadir directamente a la cola
        porque puede que el carácter siguiente sea otro número y, por lo tanto, estemos tratando con un número de más de una cifra)*/
        StringBuilder number = new StringBuilder();

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
                creamomos una instancia de tipo "NUMBER" llamando a "tokNumber" y le pasamos el resultado de llamar al método
                "parseInt" de la clase "Integer". Su retorno lo añadimos a la lista)*/
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

                /*Si no estamos tratando un paréntesis, comprobamos que no estemos tratando un espacio.*/
                else if(expr.charAt(i) != ' '){

                    /*En este punto, comprobamos que el carácter en cuestión sea un '-' que actúa como operador unario. En dicho
                    caso, añadimos en "charToken" una nueva instancia tipo "NUMBER" con valor -1 y una nueva instancia tipo "OP"
                    con el carácter '*'. (Independientemente del carácter que tenga a la izquierda, tendrá mayor prioridad la
                    multiplicación (si a la izquierda tiene un operador con mayor prioridad, será independiente cambiar el signo al grupo
                    de la izquierda como el de la derecha)*/
                    if (expr.charAt(i) == '-' && (i == 0 || (charToken.getLast().getTtype() == Toktype.OP || (charToken.getLast().getTtype() == Toktype.PAREN && charToken.getLast().getOp() == '(')))) {
                        charToken.offer(tokNumber(-1));
                        charToken.offer(tokOp('*'));

                        /*En caso de no cumplirse la condición anterior, simplemente creamos una nueva instancia de "Token" de
                        tipo "OP" pasándole como parámetro el carácter que representa al operador*/
                    } else {
                        charToken.offer(tokOp(expr.charAt(i)));
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

    /*Método donde comprobamos si el carácter que nos pasan por parámetro es uno de los que permitimos en nuestro algorimo (En dicho
    caso, retornamos true)*/
    private static boolean opExists(char c) {
        switch (c) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
            case '¬':
            case '!':
                return true;
        }
        return false;
    }
}
