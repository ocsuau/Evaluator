import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Evaluator {


    public static int calculate(String expr) {
        // Convertim l'string d'entrada en una llista de tokens
        Token[] tokens = Token.getTokens(expr);
        Queue<Token> resultList = new LinkedList<>();
        LinkedList<Token> operationList = new LinkedList<>();
        boolean par = false;

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].getTtype() == Token.Toktype.NUMBER) {
                resultList.offer(tokens[i]);
            } else if (tokens[i].getTtype() == Token.Toktype.OP) {
                if (operationList.size() != 0) {
                    while (operationList.size() > 0 && getPriority(tokens[i].getOp()) <= getPriority(operationList.peek().getOp())) {
                        resultList.offer(operationList.poll());
                    }
                }
                operationList.push(tokens[i]);
            }
                    /*while(operationList.size() > 0 && (operationList.peek().getOp() == '*' || operationList.peek().getOp() == '/')){
                        resultList.offer(operationList.poll());
                    }*/


        //else if(tokens[i].getTtype() == Token.Toktype.PAREN){

        //}
        if (i == tokens.length - 1) {
            if (operationList.size() > 0) {
                Iterator<Token> it = operationList.iterator();
                while (it.hasNext()) {
                    resultList.offer(it.next());
                }
            }
            //resultList.offer(operationList.poll());
        }
    }
    // Efectua el procediment per convertir la llista de tokens en notació RPN
    // Finalment, crida a calcRPN amb la nova llista de tokens i torna el resultat
        return

    calcRPN(resultList.toArray(new Token[resultList.size()]));
}

    public static int calcRPN(Token[] list) {
        // Calcula el valor resultant d'avaluar la llista de tokens
        int result = 0;
        LinkedList<Integer> tokenList = new LinkedList<>();

        for (int i = 0; i < list.length; i++) {
            if (list[i].getTtype() == Token.Toktype.NUMBER) {
                tokenList.push(list[i].getValue());
            } else {
                result += doOp(tokenList.poll(), tokenList.poll(), list[i].getOp());
                tokenList.push(result);
                result = 0;
            }
        }
        return tokenList.poll();

    }

    private static int getPriority(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                throw new RuntimeException();
        }
    }

    private static float doOp(int value2, int value1, char op) {
        switch (op) {
            case '+':
                return value1 + value2;
            case '-':
                return value1 - value2;
            case '*':
                return value1 * value2;
            case '/':
                return value1 / value2;
            default:
                throw new RuntimeException("Operación no permitida. Carácter equivocado");
        }
    }

}
