import java.util.Stack;

public class Postfijo {

    private static String expresionInfijo;
    private static String expresionPostfijo;
    private static String mensajeError;

    public static String getMensajeError() {
        return mensajeError;
    }

    public static void setExpresionInfijo(String expresionInfijo) {
        Postfijo.expresionInfijo = expresionInfijo;
    }

    public static boolean esLetra(String caracter) {
        return (caracter.compareTo("A") >= 0 && caracter.compareTo("Z") <= 0)
                || (caracter.compareTo("a") >= 0 && caracter.compareTo("z") <= 0);
    }

    public static boolean esDigito(String caracter) {
        return caracter.compareTo("0") >= 0 && caracter.compareTo("9") <= 0;
    }

    public static boolean esOperador(String caracter) {
        String caracteres = "+-*/%^";
        return caracteres.contains(caracter);
    }

    public static boolean esPredecesor(String operador1, String operador2) {
        boolean p = false;
        if (operador1.equals("^")) {
            p = true;
        } else if (operador1.equals("%")) {
            if (!operador2.equals("^")) {
                p = true;
            }
        } else if (operador1.equals("/") || operador1.equals("*")) {
            if (!operador2.equals("^") && !operador1.equals("%")) {
                p = true;
            }
        } else if (operador1.equals("-") || operador1.equals("+")) {
            if (operador2.equals("-") || operador2.equals("+")) {
                p = true;
            }
        }
        return p;
    }

    public static String getExpresionPostfijo() {
        expresionInfijo = expresionInfijo.replace(" ", "");
        expresionPostfijo = "";
        mensajeError = "";
        Stack pila = new Stack();
        boolean error = false;

        int i = 0;
        while (i < expresionInfijo.length() && !error) {
            String caracter = expresionInfijo.substring(i, i + 1);
            if (esOperador(caracter)) {
                pila.push(caracter);
            } else if (esLetra(caracter) || esDigito(caracter)) {
                expresionPostfijo = expresionPostfijo + caracter;
            }

            System.out.println(caracter);
            System.out.println(expresionPostfijo);
            i++;
        }

        return expresionPostfijo;
    }

}
