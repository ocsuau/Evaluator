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
        LinkedList<Integer> tokenList = new LinkedList<>();

        for(Token t : list){
            if (t.getTtype() == Token.Toktype.NUMBER) {
                tokenList.push(t.getValue());
            } else {
                tokenList.push((int)doOp(tokenList.poll(), tokenList.poll(), t.getOp()));
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
            case '^':
            case '_':
                return 3;
            case '(':
            case ')':
                return 4;
            default:
                throw new RuntimeException("Carácter equivocado. Función getPriority");
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
                return (float) Math.pow(value1, value2);
            case '_':
                return (float) Math.pow(value1, (1/(float)value2));
            default:
                throw new RuntimeException("Operación no permitida. Carácter/valores equivocados");
        }
    }
}
