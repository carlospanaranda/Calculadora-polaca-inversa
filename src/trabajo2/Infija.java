package trabajo2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Infija {

    private final String infijo;
    private String postfijo;
    private int resultado;
    private final Stack<Character> pila;
    private String error;

    public Infija(String infijo) {
        this.infijo = infijo;
        this.pila = new Stack<>();
        this.calcular();
    }

    private void calcular() {
        if (infijo.equals("")) {
            error = "NO PUEDE SER VACIA LA EXPRESIÓN INFIJA";
            return;
        }
        if (!validarParentesis(infijo)) {
            error = ("PARENTESIS INVALIDOS");
            return;
        }

        if (empiezaConOperador(infijo)) {
            error = ("NO PUEDE EMPEZAR CON UN OPERADOR");
            return;
        }
        if (terminaConOperador(infijo)) {
            error = ("NO PUEDE TERMINAR CON UN OPERADOR");
            return;
        }

        if (!evaluarAlternaciones(infijo)) {
            error = ("ORDEN DE EXPRESIÓN INCORRECTA");
            return;
        }

        postfijo = this.toPosfijo();
        if (postfijo == null) {
            error = ("NO SE PUEDE CONVERTIR LA EXPRESIÓN A POSTFIJA");
            return;
        }

        resultado = evaluarPosfijo();

    }

    private boolean validarParentesis(String operacion) {
        Stack<Character> pilaV = new Stack<>();

        char[] ecuacion = operacion.toCharArray();
        for (int c = 0; c < ecuacion.length; c++) {
            char caracter = ecuacion[c];
            if (caracter == '(') {
                pilaV.push(caracter);
            } else if (caracter == ')') {
                if (pilaV.empty()) {
                    // Numero de parentesis impares
                    return false;
                } else {
                    pilaV.pop();
                }
            }
        }
        return pilaV.empty();
    }

    private boolean empiezaConOperador(String infijo) {
        char[] cadena = infijo.toCharArray();
        if (cadena != null && cadena.length > 0) {
            char caracter = cadena[0];
            if (Character.isDigit(caracter)) {
                return false;
            }
            if (Character.isLetter(caracter)) {
                return false;
            } else if (caracter == '+' || caracter == '-' || caracter == '*' || caracter == '/' || caracter == '^') {
                return true;
            }
        }
        return false;
    }

    private boolean terminaConOperador(String infijo) {
        char[] cadena = infijo.toCharArray();

        if (cadena != null && cadena.length > 0) {
            char caracter = cadena[cadena.length - 1];
            if (Character.isDigit(caracter)) {
                return false;
            }
            if (Character.isLetter(caracter)) {
                return false;
            } else if (caracter == '+' || caracter == '-' || caracter == '*' || caracter == '/' || caracter == '^') {                
                return true;
            }
        }
        return false;
    }

    private boolean evaluarAlternaciones(String infijo) {
        char[] cadena = infijo.toCharArray();
        char ultimoElemento = cadena[0];

        //Se da por entendido que todas las expresiones son correctas hasta que se encuentre un error
        boolean validacion = true;

        for (int c = 1; c < cadena.length; c++) {
            char caracter = cadena[c];
            if (Character.isDigit(caracter)) {
                if (ultimoElemento == ')') {
                    System.out.println("No se puede poner un numero despues de un ')'");
                    validacion = false;
                } else {
                    ultimoElemento = caracter;
                }
            } else if (caracter == '(') {
                switch (ultimoElemento) {
                    case '(':
                        ultimoElemento = caracter;
                        break;
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '^':
                        ultimoElemento = caracter;
                        break;
                    default:
                        System.out.println("Despues de un '" + ultimoElemento + "' no puede venir un '('");
                        validacion = false;
                        break;
                }
            } else if (caracter == ')') {
                if (ultimoElemento == ')') {
                    ultimoElemento = caracter;
                } else if (Character.isDigit(ultimoElemento)) {
                    ultimoElemento = caracter;
                } else {
                    System.out.println("Despues de un '" + ultimoElemento + "' no puede venir un ')'");
                    validacion = false;
                }
            } else if (caracter == '+' || caracter == '-' || caracter == '*' || caracter == '/' || caracter == '^') {
                if (ultimoElemento == ')') {
                    ultimoElemento = caracter;
                } else if (Character.isDigit(ultimoElemento)) {
                    ultimoElemento = caracter;
                } else {
                    System.out.println("Despues de un '" + ultimoElemento + "' no puede venir un Operador '" + caracter + "'");
                    validacion = false;
                }
            }
        }

        return validacion;
    }

    private boolean esNegativo(String infijo) {
        char[] cadena = infijo.toCharArray();
        char ultimoElemento = cadena[0];
        for (int c = 1; c < cadena.length; c++) {
            char caracter = cadena[c];

        }

        return false;

    }

    private String toPosfijo() {
        String salida = "";
        char[] cadena = infijo.toCharArray();

        for (int c = 0; c < cadena.length; c++) {
            char caracter = cadena[c];
            if (caracter == '(') {
                pila.push(caracter);
            } else if (caracter == ')') {
                while (true) {
                    if (pila.empty()) {
                        System.out.println("Operacion invalida numero de parentesis impares");
                        return null;
                    }
                    char temp = pila.pop();
                    if (temp == '(') {
                        break;
                    } else {
                        salida += " " + temp;
                    }
                }//fin del wile
            } else if (Character.isDigit(caracter)) {
                salida += " " + caracter;
                c++;

                for (; c < cadena.length; c++) {
                    if (Character.isDigit(cadena[c])) {
                        salida += cadena[c];
                    } else {
                        c--;
                        break;
                    }
                }
            } else if (caracter == '+' || caracter == '-' || caracter == '/' || caracter == '*' || caracter == '^') {
                if (pila.empty()) {
                    pila.push(caracter);
                } else {
                    while (true) {
                        if (esDeMayorPrioridad(caracter)) {
                            pila.push(caracter);
                            break;
                        } else {
                            salida += " " + pila.pop();
                        }
                    }
                }
            } else {
                System.out.println("caracter no valido para la exprecion : '" + caracter + "'");
                return null;
            }
        }//fin del for
        if (!pila.empty()) {
            do {
                salida += " " + pila.pop();
            } while (!pila.empty());
        }

        return salida.trim();
    }

    private int evaluarPosfijo() {

        ArrayList<String> token = new ArrayList<>();

        String[] st = postfijo.split(" ");

        token.addAll(Arrays.asList(st));

        if (token.size() == 1) {
            return Integer.parseInt(token.get(0));
        }
        int c = 0;
        while (token.size() != 1) {

            String operador = token.get(c);
            if (operador.equals("+") || operador.equals("-") || operador.equals("*") || operador.equals("/") || operador.equals("^")) {

                String operando1 = token.get(c - 1);
                String operando2 = token.get(c - 2);

                token.remove(c);
                token.remove(c - 1);
                token.remove(c - 2);
                switch (operador) {
                    case "+":
                        try {
                            String suma = (Integer.parseInt(operando2) + Integer.parseInt(operando1)) + "";
                            token.add(c - 2, suma);
                            c = 0;
                        } catch (NumberFormatException e) {
                            System.out.println("Error al comvertir un operando\n" + e);
                            return 0;
                        }
                        break;
                    case "-":
                        try {
                            String resta = (Integer.parseInt(operando2) - Integer.parseInt(operando1)) + "";
                            token.add(c - 2, resta);
                            c = 0;
                        } catch (NumberFormatException e) {
                            System.out.println("Error al comvertir un operando\n" + e);
                            return 0;
                        }
                        break;
                    case "*":
                        try {
                            String multiplicacion = (Integer.parseInt(operando2) * Integer.parseInt(operando1)) + "";
                            token.add(c - 2, multiplicacion);
                            c = 0;
                        } catch (NumberFormatException e) {
                            System.out.println("Error al comvertir un operando\n" + e);
                            return 0;
                        }
                        break;
                    case "/":
                        try {
                            String division = (Integer.parseInt(operando2) / Integer.parseInt(operando1)) + "";
                            token.add(c - 2, division);
                            c = 0;
                        } catch (NumberFormatException e) {
                            System.out.println("Error al comvertir un operando\n" + e);
                            return 0;
                        }
                        break;
                    default:
                        try {
                            String potencia = (int) Math.pow(Integer.parseInt(operando2), Integer.parseInt(operando1)) + "";
                            token.add(c - 2, potencia);
                            c = 0;
                        } catch (NumberFormatException e) {
                            System.out.println("Error al comvertir un operando\n" + e);
                            return 0;
                        }
                        break;
                }
            } else {
                c++;
            }
        }

        try {
            return Integer.parseInt(token.get(0));
        } catch (NumberFormatException e) {
            System.out.println("Error al parsear el resultado\n" + e);
            return 0;
        }

    }

    private boolean esDeMayorPrioridad(char caracter) {
        if (pila.empty()) {
            return true;
        }
        if (caracter == pila.peek()) {
            return false;
        }
        if (caracter == '^') {
            return true;
        }
        if ((caracter == '*' && pila.peek() == '/') || (caracter == '/' && pila.peek() == '*')) {
            return false;
        }
        if ((caracter == '+' && pila.peek() == '-') || (caracter == '-' && pila.peek() == '+')) {
            return false;
        } else if (caracter == '-' || caracter == '+') {
            if (pila.peek() == '*' || pila.peek() == '/') {
                return false;
            }
        }
        return true;
    }

    public String getInfijo() {
        return infijo;
    }

    public String getPostfijo() {
        return postfijo;
    }

    public int getResultado() {
        return resultado;
    }

    public String getError() {
        return error;
    }

}
