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

        int tipoEntrada = 0; // 0: no es operador, 1: es parentesis (, 2: es parentesis ), 3: es operando
        int parentesis = 0;

        int i = 0;
        while (i < expresionInfijo.length() && !error) {
            String caracter = expresionInfijo.substring(i, i + 1);
            if (caracter.equals("(")) {
                tipoEntrada = 1;
                pila.push(caracter);
                parentesis++;
            } else if (caracter.equals(")")) {
                tipoEntrada = 2;
                if (parentesis == 0) {
                    error = true;
                    mensajeError = "Falta paréntesis izquierdo";
                } else {
                    parentesis--;
                    caracter = (String) pila.peek();
                    while (!pila.empty() && !caracter.equals("(")) {
                        expresionPostfijo = expresionPostfijo + " " + (String) pila.pop();
                        caracter = (String) pila.peek();
                    }
                    pila.pop();
                }
            } else if (esOperador(caracter)) {
                if (tipoEntrada < 2) {
                    error = true;
                    mensajeError = "Hace falta un operando antes de [" + caracter + "]";
                } else {
                    tipoEntrada = 0;
                    expresionPostfijo = expresionPostfijo + " ";
                    while (!pila.empty() && esPredecesor((String) pila.peek(), caracter)) {
                        expresionPostfijo = expresionPostfijo + " " + (String) pila.pop();
                    }
                    pila.push(caracter);
                }
            } else if (esLetra(caracter) || esDigito(caracter)) {
                expresionPostfijo = expresionPostfijo + caracter;
                tipoEntrada = 3;
            } else {
                error = true;
                mensajeError = "El caracter [" + caracter + "] no es válido";
            }
            i++;
        }
        // verificar errores despues del recorrido
        if (parentesis > 0) {
            mensajeError = "Hace falta un paréntesis derecho";
            expresionPostfijo = "";
        } else if (error) {
            expresionPostfijo = "";
        } else if (i == 0 || tipoEntrada == 0) {
            mensajeError = "No hay expresión o falta operando";
        } else {
            // terminar de construir la expresion postfijo
            expresionPostfijo = expresionPostfijo + " ";
            while (!pila.empty()) {
                expresionPostfijo = expresionPostfijo + " " + (String) pila.pop();
            }
        }
        return expresionPostfijo;
    }

}
