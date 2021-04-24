
import java.util.*;

public class Evaluator {


    public static int calculate(String expr) {
        // Convertim l'string d'entrada en una llista de tokens
        Token[] tokens = Token.getTokens(expr);
        Deque<Token> stack = new LinkedList<Token>();
        List<Token> out = new ArrayList<Token>();
        // Efectua el procediment per convertir la llista de tokens en notació RPN
        int contador = 0;
        for (int i = 0; i < tokens.length; i++) {

            if(tokens[i].getTokType() == Token.Toktype.NUMBER) {
                out.add(tokens[i]); // Si es un número l'afegim a la cua
            } else if (tokens[i].getTkOp() == ')') {
                Iterator<Token> itStack = stack.iterator(); // Cream una llista nova

                while (itStack.hasNext()) {
                    Token tok = itStack.next();

                    if(tok.getTkOp() == '(') {
                        break; // Si ens trobem el parentesi contrari aturam l'iteració
                    } else {
                        out.add(tok); // Afegim el token dintre de la llista
                        contador++; // Incrementam l'operador
                    }

                }


                for (int j = 0; j <= contador; j++) {
                    stack.pop();
                }

                contador = 0; // retornam el comptador a 0
            } else {

                Iterator<Token> stackIt = stack.iterator();

                if(tokens[i].getTkOp() == '+' | tokens[i].getTkOp() == '-') {
                    Token temporal;
                    while(stackIt.hasNext()) {
                        temporal = stackIt.next();

                        if(temporal.getTokType() == Token.Toktype.PAREN) {
                            break; // Si trobam un parentesis aturam el bucle
                        }

                        out.add(temporal); // Si no trobam un parentesi afegim el token dintre de la llista
                        contador++; // Incrementam el comptador
                    }
                } else if(tokens[i].getTkOp() == '*' | tokens[i].getTkOp() == '/') {
                    Token temporal;
                    while(stackIt.hasNext()) {
                        temporal = stackIt.next();

                        if(temporal.getTkOp() == '+' | temporal.getTkOp() == '-' | temporal.getTkOp() == '(') {
                            break; // Si trobam un signe de suma, resta o parentesis aturam el bucle
                        }

                        out.add(temporal); // Si no trobam un parentesi afegim el token dintre de la llista
                        contador++; // Incrementam el comptador
                    }
                } else if(tokens[i].getTkOp() == '^') {
                    Token temporal;
                    while(stackIt.hasNext()) {
                        temporal = stackIt.next();

                        if(temporal.getTkOp() == '+' | temporal.getTkOp() == '-' | temporal.getTkOp() == '(' | temporal.getTkOp() == '*' | temporal.getTkOp() == '/') {
                            break; // Si trobam un signe de suma, resta, parentesis, multipliació o divisió aturam el bucle
                        }

                        out.add(temporal); // Si no trobam un parentesi afegim el token dintre de la llista
                        contador++; // Incrementam el comptador
                    }
                } else if(tokens[i].getTkOp() == '_') {
                    Token temporal;
                    while (stackIt.hasNext()) {
                        temporal = stackIt.next();

                        if (temporal.getTkOp() != '_') break;

                        out.add(temporal);

                        contador++;
                    }
                }

                for (int j = 0; j < contador; j++) {
                    stack.pop();
                }
                contador = 0;
                stack.push(tokens[i]);

            }

        }


        // Finalment, crida a calcRPN amb la nova llista de tokens i torna el resultat
        Iterator<Token> pilaIterator = stack.iterator();
        while (pilaIterator.hasNext()) {
            out.add(pilaIterator.next());
        }
        Token[] tokns = out.toArray(new Token[0]);
        return calcRPN(tokns);
    }

    public static int calcRPN(Token[] list) {

       Deque<Token> stack = new LinkedList<Token>();

        for (int i = 0; i < list.length; i++) {
            if(list[i].getTokType() == Token.Toktype.NUMBER) {
                stack.push(list[i]);
            } else if(list[i].getTokType() == Token.Toktype.OP) {

                if(list[i].getTkOp() == '_') {
                    double res;
                    double n2 = stack.pop().getValue();
                    res = n2 * (-1);
                    stack.push(Token.tokNumber((int)res));
                    continue;
                }

                double res;
                double n2 = stack.pop().getValue();
                double n1 = stack.pop().getValue();
                char operator = list[i].getTkOp();
                res = operation(n1, n2, operator);
                stack.push(Token.tokNumber((int)res));
            }
        }

        // Calcula el valor resultant d'avaluar la llista de tokens
        return (int)stack.pop().getValue();
    }

    // Mètode per a realitzar les operacions
    static private double operation(double n1, double n2, char operator) {
        double res = switch (operator) {
            case '+' -> n1 + n2;
            case '-' -> n1 - n2;
            case '*' -> n1 * n2;
            case '/' -> n1 / n2;
            case '^' -> Math.pow(n1, n2);
            default -> 0;
        };

        return res;
    }


}
