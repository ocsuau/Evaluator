import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Evaluator {


    public static int calculate(String expr) {
        // Convertim l'string d'entrada en una llista de tokens
        Token[] tokens = Token.getTokens(expr);
        Queue<Token> resultList = new LinkedList<>();
        LinkedList<Token> operationList = new LinkedList<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].getTtype() == Token.Toktype.NUMBER) {
                resultList.offer(tokens[i]);
            } else{
                if(tokens[i].getOp() == ')'){
                    while(operationList.peek().getOp() != '('){
                        resultList.offer(operationList.poll());
                    }
                    operationList.poll();
                    continue;
                }
                else if (operationList.size() != 0 && operationList.peek().getOp() != '('){
                    while (operationList.size() > 0 && getPriority(tokens[i].getOp()) <= getPriority(operationList.peek().getOp()) && operationList.peek().getOp() != '('){
                        resultList.offer(operationList.poll());
                    }
                }
                operationList.push(tokens[i]);
            }
        }
        if (operationList.size() > 0) {
            Iterator<Token> it = operationList.iterator();
            while (it.hasNext()) {
                resultList.offer(it.next());
            }
        }
        return calcRPN(resultList.toArray(new Token[resultList.size()]));
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
            case '(':
            case ')':
                return 3;
            case '^':
            case '_':
                return 4;
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
            case '^':
                return (int) Math.pow(value1, value2);
            case '_':
                return (int) Math.pow(value1, (1/value2));
            default:
                throw new RuntimeException("Operación no permitida. Carácter equivocado");
        }
    }

}
