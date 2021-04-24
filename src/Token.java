import java.util.ArrayList;
import java.util.List;

public class Token {
    enum Toktype {
        NUMBER, OP, PAREN
    }

    // Pensa a implementar els "getters" d'aquests atributs
    private Toktype ttype;
    private int value;
    private char tk;

    // Constructor privat. Evita que es puguin construir objectes Token externament
    private Token() {
    }

    public Toktype getTokType() {
        return ttype;
    }

    public double getValue() {
        return value;
    }

    public char getTkOp() {
        return tk;
    }


    // Torna un token de tipus "NUMBER"
    static Token tokNumber(int value) {

        Token tok = new Token(); // Declaram un nou token
        tok.ttype = Toktype.NUMBER;
        tok.value = value;

        return tok;

    }

    // Torna un token de tipus "OP"
    static Token tokOp(char c) {

        Token tok = new Token();
        tok.ttype = Toktype.OP; // Assignam el valor del token com a operador
        tok.tk = c;

        return tok; // Retornam l'operador
    }

    // Torna un token de tipus "PAREN"
    static Token tokParen(char c) {

        Token tok = new Token();
        tok.ttype = Toktype.PAREN;
        tok.tk = c;

        return tok;
    }

    // Mostra un token (conversió a String)
    public String toString() {
        return " " + ttype + " ";
    }

    // Mètode equals. Comprova si dos objectes Token són iguals
    public boolean equals(Object o) {
        return (this.tk == ((Token) o).tk) && (this.value == ((Token) o).value) && (this.ttype == ((Token) o).ttype);
    }

    // A partir d'un String, torna una llista de tokens
    public static Token[] getTokens(String expr) {
        List<Token> devolver = new ArrayList<Token>();
        String[] partes = addSpace(expr).split("\\s+");

        boolean encontradoOperador = true;

        for (int i = 0; i < partes.length; i++) {
            String part = partes[i];
            if (part.length() != 0) {
                if (part.contains("-") && encontradoOperador) {
                    devolver.add(tokOp('_'));
                    encontradoOperador = false;
                } else if (isOperator(part.charAt(0))) {
                    encontradoOperador = true;
                    devolver.add(tokOp(part.charAt(0)));
                } else if (leftParent(part.charAt(0))) {
                    devolver.add(tokParen(part.charAt(0)));
                    encontradoOperador = true;
                } else if (rightParent(part.charAt(0))) {
                    devolver.add(tokParen(part.charAt(0)));
                    encontradoOperador = false;
                } else if (Character.isDigit(part.charAt(0))) {
                    if (i + 1 >= partes.length) {
                        devolver.add(tokNumber(Integer.parseInt(part)));
                    } else {
                        if (partes[i + 1].charAt(0) == '.') {
                            int index = 1;
                            String ayuda = "";
                            for (int j = i + 2; j < partes.length; j++) {

                                if (Character.isDigit(partes[j].charAt(0))) {
                                    ayuda += partes[j];
                                    index++;
                                } else {
                                    break;
                                }
                            }
                            i += index;
                            devolver.add(tokNumber(Integer.parseInt(part + "." + ayuda)));
                        } else {
                            devolver.add(tokNumber(Integer.parseInt(part)));
                        }
                    }
                    encontradoOperador = false;
                }
            }
        }

        return devolver.toArray(new Token[0]);
    }

    static private boolean isOperator(char c) {
        switch (c) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
                return true;
            default:
                return false;
        }
    }

    static private boolean rightParent(char c) {
        return c == ')';
    }

    static private boolean leftParent(char c) {
        return c == '(';
    }


    static private String addSpace(String expresion) {
        StringBuilder devolver = new StringBuilder();
        for (int i = 0; i < expresion.length(); i++) {
            if (Character.isDigit(expresion.charAt(i))) {
                devolver.append(expresion.charAt(i));
            } else {
                devolver.append(" " + expresion.charAt(i) + " ");
            }
        }
        return devolver.toString();
    }

}
