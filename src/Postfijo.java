import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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

    public static List<String> getVariables() {
        List<String> variables = new ArrayList<>();
        boolean error = false;
        int tipoOperando = 0; // 0: No es operando, 1: es variable, 2: es constante numérica

        String texto = "";
        int i = 0;
        while (i < expresionPostfijo.length() && !error) {
            String caracter = expresionPostfijo.substring(i, i + 1);

            if (esLetra(caracter) && tipoOperando == 2) {
                error = true;
                mensajeError = "La constante numérica solo puede contener digitos";
            } else if ((esLetra(caracter) && tipoOperando < 2)
                    || (esDigito(caracter) && tipoOperando == 1)) {
                tipoOperando = 1;
                texto += caracter;
            } else if (esDigito(caracter) && tipoOperando != 1) {
                tipoOperando = 2;
                texto += caracter;
            } else if (caracter.equals(" ") && tipoOperando == 1) {
                // no permitir variables repetidas
                if (!variables.contains(texto)) {
                    variables.add(texto);
                }
                tipoOperando = 0;
                texto = "";
            } else if (caracter.equals(" ") && tipoOperando == 2) {
                tipoOperando = 0;
                texto = "";
            }
            i++;
        }
        return variables;
    }

    public static String[] encabezados = new String[] { "Variable", "Valor" };

    public static void mostrarVariables(JTable tbl) {
        List<String> variables = getVariables();
        String[][] datos = null;
        if (variables.size() > 0) {
            datos = new String[variables.size()][2];
            for (int i = 0; i < variables.size(); i++) {
                datos[i][0] = variables.get(i);
            }
        }
        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);
        tbl.setModel(dtm);
    }

    public static ArbolBinario getArbolExpresion() {

        boolean error = false;
        TipoOperando tipoOperando = TipoOperando.NINGUNO;
        Stack pila = new Stack();

        int i = 0;
        String texto = "";
        while (i < expresionPostfijo.length() && !error) {
            String caracter = expresionPostfijo.substring(i, i + 1);
            if (esLetra(caracter) && tipoOperando == TipoOperando.CONSTANTE) {
                error = true;
                mensajeError = "La constante numérica solo puede contener digitos";
            } else if ((esLetra(caracter) && tipoOperando != TipoOperando.CONSTANTE) ||
                    (esDigito(caracter) && tipoOperando == TipoOperando.VARIABLE)) {
                tipoOperando = TipoOperando.VARIABLE;
                texto += caracter;
            } else if (esDigito(caracter) && tipoOperando != TipoOperando.VARIABLE) {
                tipoOperando = TipoOperando.CONSTANTE;
                texto += caracter;
            } else if (caracter.equals(" ") && tipoOperando != TipoOperando.NINGUNO) {
                Nodo nodoOperando = new Nodo(tipoOperando, texto);
                pila.push(nodoOperando);
                texto = "";
                tipoOperando = TipoOperando.NINGUNO;
            } else {
                caracter = expresionPostfijo.substring(i, i + 1);
                if (esOperador(caracter)) {
                    Nodo nodoOperador = new Nodo(TipoOperando.NINGUNO, caracter);
                    Nodo nodoDerecho = (Nodo) pila.pop();
                    Nodo nodoIzquierdo = (Nodo) pila.pop();
                    nodoOperador.izquierdo = nodoIzquierdo;
                    nodoOperador.derecho = nodoDerecho;
                    pila.push(nodoOperador);
                }
            }

            i++;
        }
        return !error ? new ArbolBinario((Nodo) pila.pop()) : null;
    }

}
