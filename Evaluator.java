import java.util.LinkedList;
import java.util.Queue;

public class Evaluator {
    public static int calculate(String expr) {
        Token[] tokens = Token.getTokens(expr);
        Queue<Token> resultList = new LinkedList<>();
        LinkedList<Token> operationList = new LinkedList<>();
        try {
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].getTtype() == Token.Toktype.NUMBER) {
                    resultList.offer(tokens[i]);
                } else {
                    if (tokens[i].getOp() == ')') {
                        for (Token t = operationList.poll(); t.getOp() != '('; t = operationList.poll()) {
                            resultList.offer(t);
                        }
                        continue;
                    }
                    while (operationList.size() != 0 && operationList.peek().getOp() != '(' && getPriority(tokens[i].getOp()) <= getPriority(operationList.peek().getOp())) {
                        resultList.offer(operationList.poll());
                    }
                    operationList.push(tokens[i]);
                }
            }

            for (Token t : operationList) {
                resultList.offer(t);
            }
            return calcRPN(resultList.toArray(new Token[resultList.size()]));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return 0;
    }

    public static int calcRPN(Token[] list) {
        LinkedList<Integer> tokenList = new LinkedList<>();

        for (Token t : list) {
            if (t.getTtype() == Token.Toktype.NUMBER) {
                tokenList.push(t.getValue());
            } else {
                tokenList.push((int) doOp(tokenList.poll(), tokenList.poll(), t.getOp()));
            }
        }
        return tokenList.poll();
    }

    private static int getPriority(char op) throws RuntimeException {
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

    private static float doOp(int value2, int value1, char op) throws RuntimeException {
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
                return (float) Math.pow(value1, (1 / (float) value2));
            default:
                throw new RuntimeException("Operación no permitida. Carácter/valores equivocados");
        }
    }
}