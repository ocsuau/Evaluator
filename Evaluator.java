import java.util.LinkedList;
import java.util.Queue;

//Clase "Evaluator", donde calcularemos tanto las operaciones con notación polaca como la notación no fija.
public class Evaluator {

    /*Método donde generamos la notación polaca inversa para, posteriormente, realizar las operaciones.*/
    public static int calculate(String expr) {

        /*Generamos un array de tokens a parti de la expresión que nos pasan a través del método "getTokens" de la clase "Token".
        Además, creamos una cola y una pila para aplicar correctamente el algoritmo "Shunting Yard"*/
        Token[] tokens = Token.getTokens(expr);

        //"resultList" donde iremos guardando el resultado de la notación
        Queue<Token> resultList = new LinkedList<>();
        //"operationList" donde iremos guardando y sacando los operadores/paréntesis siguiendo el algorimo "Shunting Yard"
        LinkedList<Token> operationList = new LinkedList<>();

        /*Encapsularemos el código en un "try" para poder capturar las posibles excepciones que se pudieran generar.*/
        try {
            for (Token tok : tokens) {

                //Si el token que estamos tratando es de tipo "NUMBER", lo insertamos directamente en "resultList".
                //if (tokens[i].getTtype() == Token.Toktype.NUMBER) {
                if (tok.getTtype() == Token.Toktype.NUMBER) {
                    resultList.offer(tok);
                } else {

                    /*Si el token que estamos tratando es un paréntesis de cierre, meteremos todos los operadores de "operationList"
                    en resultList hasta que encontremos un token que represente un paréntesis de apertura. Seguidamente
                    realizaremos un continue*/
                    if (tok.getOp() == ')') {
                        for (Token t = operationList.poll(); t.getOp() != '('; t = operationList.poll()) {
                            resultList.offer(t);
                        }
                        continue;
                    }

                    /*En este punto sabemos que el caráter que estamos tratando es, teóricamente, un operador. Por lo tanto, lo
                    introduciremos en "operationList". Pero antes, debemos comprobar si sacamos o no operadores de "operationList"
                    para introducirlos en "resultList". Los casos son los siguientes.*/

                    /*Mientras "operationList" contenga, al menos, un elemento Y el carácter que queramos sacar de "operationList"
                    (stack) no sea un paréntesis de apertura Y la prioridad del operador que estamos tratando sea inferior o igual
                    a la prioridad del operador que queremos sacar de "operationList"

                            sacaremos el primer elemento de la pila "operationList" y lo insertaremos en "resultList"*/

                    /*Las prioridades las comprobamos a través del método "getPriority"*/
                    while (operationList.size() != 0 && operationList.peek().getOp() != '(' && getPriority(tok.getOp()) <= getPriority(operationList.peek().getOp())) {
                        resultList.offer(operationList.poll());
                    }

                    /*Finalmente, independientemente de si hemos sacado o no algún token de "operationList" para insertarlo en "resultList",
                    insertamos el token que estamos tratando en "operationList"*/
                    operationList.push(tok);
                }
            }

            /*Una vez finalizado el bucle, debemos comprobar si en "operationList" quedan tokens por introducir en "resultList".
            Lo comprobamos directamente con un bucle ya que, si en "operationList" no quedan tokens, no entrará en el bucle.*/
            for (Token t : operationList) {
                resultList.offer(t);
            }

            /*Retornamos el retorno de llamar al método "calcRPN", pasándole como parámetro la conversión de "resultList" en un
            array de tokens.*/
            return calcRPN(resultList.toArray(new Token[resultList.size()]));

            /*Capturamos las posibles excepciones que se hayan podido generar en el bloque "try" o en funciones a las que llamemos
            desde el ya mencionado bloque.*/
        } catch (RuntimeException e) {

            //Si es una excepción Runtime, simplemente imprimimos su mensaje para saber de qué método viene.
            System.out.println(e.getMessage());
        } catch (Exception e) {

            /*Si no es una excepción Runtime, capturamos la excepción en variable tipo "Exception" y provocamos un "RuntimeException"*/
            throw new RuntimeException();
        }
        return 0;
    }

    //Método donde calcularemos el resultado de una operación con notación polaca inversa
    public static int calcRPN(Token[] list) {

        //En "tokenList" iremos almacenando los números además de los resultados de las operaciones entre esos números.
        LinkedList<Integer> tokenList = new LinkedList<>();

        /*Encapsularemos el código en un "try" para poder capturar las posibles excepciones que se pudieran generar.*/
        try {
            //Recorremos "list" (array de tokens que nos pasan como parámetro)
            for (Token t : list) {

                //Si el token que estamos tratando es de tipo "NUMBER", introducimos su valor en "tokenList".
                if (t.getTtype() == Token.Toktype.NUMBER) {
                    tokenList.push(t.getValue());

            /*Si el token que estamos tratando es de tipo "OP", sacaremos los dos primeros elementos de la pila y los pasaremos,
            junto con el token que estamos tratando, al método "doOp". Su retorno lo añadiremos en la misma "tokenList".*/
                } else {
                    tokenList.push(doOp(tokenList.poll(), tokenList.poll(), t.getOp()));
                }
            }

        /*Al finalizar el bucle, en "tokenList" sólo quedará un elemento, que será el resultado final de las operaciones que hemos
        ido realizando en el bucle anterior. Sacamos dicho elemento y lo retornamos.*/
            return tokenList.poll();
        }
        /*Capturamos las posibles excepciones que se hayan podido generar en el bloque "try" o en funciones a las que llamemos
        desde el ya mencionado bloque.*/ catch (RuntimeException e) {

            //Si es una excepción Runtime, simplemente imprimimos su mensaje para saber de qué método viene.
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        } catch (Exception e) {

            /*Si no es una excepción Runtime, capturamos la excepción en variable tipo "Exception" y provocamos un "RuntimeException"*/
            throw new RuntimeException();
        }
        return 0;
    }

    /*Método donde retornamos la prioridad del operador en función del carácter que nos mandan. Además, indicamos que este
    método puede generar exceptiones de tipo Runtime.*/
    private static int getPriority(char op) throws RuntimeException {

        //Evaluamos el carácter con un "switch"
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
            case '¬':
                return 3;
                /*Los paréntesis son los que mayor prioridad tienen ya que no permitirían sacar un operador de la pila de operadores
                en caso de que quisiéramos introducir un paréntesis*/
            case '(':

                //Teóricamente éste caso nunca se podrá dar, por lo pongo para que sea visualmente correcto
            case ')':
                return 4;
            default:
                /*En caso de que no se hayan cumplido ninguno de los casos anteriores, provocamos un "RuntimeException" indicando el
                mensaje de error*/
                throw new RuntimeException("Carácter equivocado. Función getPriority");
        }
    }

    /*Método donde retornamos el valor resultante de una operación (que nos mandan como parámetro) entre dos valores (que también
    nos pasan como parámetros). Además, indicamos que este método puede generar exceptiones de tipo Runtime.*/
    private static int doOp(int value2, int value1, char op) throws RuntimeException {
        switch (op) {
            case '+':
                return value1 + value2;
            case '-':
                return value1 - value2;
            case '*':
                return value1 * value2;
            case '/':
                return value1 / value2;
            //Potencias
            case '^':
                return (int) (float) Math.pow(value1, value2);
            //Raíces 'n'arias
            case '¬':
                return (int) (float) Math.pow(value1, (1 / (float) value2));
            default:

                /*En caso de que no se hayan cumplido ninguno de los casos anteriores, provocamos un "RuntimeException" indicando el
                mensaje de error*/
                throw new RuntimeException("Operación no permitida. Carácter/valores equivocados");
        }
    }
}