package compi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class sintaxis {

    TablaSimbolos cabezaVariables = null, S;
    String nombreVariable;
    String tipo;
    String impresion = "";
    static String intermedio="";
    String evaluada = "";
    String valorTemp;
    lexico RefLexico;
    int renglonError;
    boolean evDirecta = true;
    boolean primerVariable = true;
    boolean errorsemantico = false;
    boolean errorSintactico =false;
    boolean evaluacionVariable = false;
    ArrayList<String> tipos = new ArrayList<String>();
    boolean negaBandera=false;
    private static int tempCount = 1;

    List<String> nombresVistos = new ArrayList<>();

    String[][] palabras;

    String longitud[][] = {
        {"int", "7"},
        {"string", "101"},
        {"float", "7"},
        {"boolean","5"}
    };

    String errores[][] = {
        {"Se espera main", "1"},
        {"Se espera (", "2"},
        {"Se espera )", "3"},
        {"Se espera {", "4"},
        {"Se espera }", "5"},
        {"Se espera identificador", "6"},
        {"Se espera tipo de dato", "7"},
        {"Se espera ;", "8"},
        {"Se espera =", "9"},
        {"Se espera statement", "10"},
        {"Se espera operador relacional", "11"},
        {"Se espera operador aditivo", "12"},
        {"Se espera factor", "13"},
        {"Se espera operador multiplicativo", "14"},
        {"Se espera valor o entrada de teclado", "15"},
        {"Se espera new", "16"}
    };

    String errorSemantico[][] = {
        {"Variable no creada", "1"},
        {"Variable repetida", "2"},
        {"Incompatibilidad de tipos", "3"},
        {"Valor excedente", "4"}
    };

    public String[] palabrasReservadas = {"int", "string", "boolean", "float"};
    nodo p = null;

    public sintaxis(nodo n) {

        try {
            p = n;
            while (p != null) {
                if (p.token == 203) {
                    p = p.sig;
                    if (p.token == 117) {
                        p = p.sig;
                        if (p.token == 118) {
                            p = p.sig;
                            if (p.token == 119) {
                                p = p.sig;
                                variables();
                                IntermedioData();
                                statements();
                                ciclostatements();
                                if (p.token == 120) {
                                    impresion += ("Analisis sintactico completado correctamente 🐕🐕🐶🐶");
                                    imprimirListaVariables();
                                    System.out.println(intermedio);
                                    break;
                                }
                            } else {
                                imprimirError(4);
                            }
                        } else {
                            imprimirError(3);
                        }
                    } else {
                        imprimirError(2);
                    }
                } else {
                    imprimirError(1);

                }

            }

        } catch (NullPointerException e) {
            imprimirError(5);
        }
    }

    private void ciclostatements() {

        if (p.token != 120) {

            statements();
            
            ciclostatements();
        }
    }

    private void limpiarComprobar() {
        if (evaluacionVariable) {
            comprobarCompatibilidad();
            evaluacionVariable = false;
        }
        tipos.clear();
    }

    private void variables() {
        tipos();
        if (p.token == 100) {
            nombreVariable = p.lexema;
            insertarNodoVariables();

            p = p.sig;
            ciclovariables();

        } else {
            imprimirError(6);

        }
    }

    private void tipos() {
        if (p.token == 207) {
            p = p.sig;
            if (p.token == 209 || p.token == 208 || p.token == 212 || p.token == 213) {
                tipo = p.lexema;
                p = p.sig;
            } else {
                imprimirError(7);

            }
        } else {
            imprimirError(16);
        }

    }

    private void ciclovariables() {
        if (p.token == 124) {
            p = p.sig;
            if (p.token == 100) {
                nombreVariable = p.lexema;
                insertarNodoVariables();
                
                p = p.sig;
                

                ciclovariables();

            } else {
                imprimirError(6);

            }

        } else if (p.token == 125) {
            p = p.sig;
            if (p.token == 207) {
                variables();
            }

        } else {

            imprimirError(8);
        }

    }

    private void statements() {
        if (p.token == 201) {
            p = p.sig;
            if (p.token == 117) {
                p = p.sig;
                exp_cond();
                comprobarCompatibilidad();
                tipos.clear();
                if (p.token == 118) {
                    p = p.sig;
                    if (p.token == 119) {
                        p = p.sig;

                        statements();
                        ciclostatements();

                        if (p.token == 120) {

                            p = p.sig;

                            if (p.token == 202) {
                                p = p.sig;
                                if (p.token == 119) {
                                    p = p.sig;
                                    statements();
                                    ciclostatements();

                                    if (p.token == 120) {

                                        p = p.sig;
                                    } else {
                                        imprimirError(5);
                                    }
                                } else {
                                    imprimirError(4);
                                }
                            }
                        } else {
                            imprimirError(5);
                        }
                    } else {
                        imprimirError(4);
                    }
                } else {
                    imprimirError(3);
                }
            } else {
                imprimirError(2);
            }
        } //Ciclo while
        else if (p.token == 204) {
            p = p.sig;
            if (p.token == 117) {
                p = p.sig;
                exp_cond();
                comprobarCompatibilidad();
                tipos.clear();
                if (p.token == 118) {
                    p = p.sig;
                    if (p.token == 119) {
                        p = p.sig;
                        statements();
                        ciclostatements();
                        if (p.token == 120) {
                            p = p.sig;
                        } else {
                            imprimirError(5);
                        }
                    } else {
                        imprimirError(4);
                    }
                } else {
                    imprimirError(3);
                }
            } else {
                imprimirError(2);
            }

        } //Funcion de imprimir
        else if (p.token == 206) {
            p = p.sig;
            if (p.token == 117) {
                p = p.sig;
                if (p.token == 100) {
                    if (existeVariable(p)) {
                        comprobarImpresion(p);
                        p = p.sig;
                        cicloimpresion();
                        if (p.token == 118) {
                            p = p.sig;
                            if (p.token == 125) {
                                p = p.sig;
                            } else {
                                imprimirError(8);
                            }
                        } else {

                            imprimirError(3);
                        }
                    } else {
                        //error de variable
                        impSemantico(1);
                        while (renglonError == p.renglon) {
                            p = p.sig;
                        }
                    }
                } else {
                    imprimirError(6);
                }
            } else {
                imprimirError(2);
            }

        } //Evaluacion de variable
        else if (p.token == 100) {
            evDirecta = true;
            evaluacionVariable = true;
            if (existeVariable(p)) {
                insertarTipo(p);
                evaluada = p.lexema;
                p = p.sig;
                if (p.token == 123) {
                    p = p.sig;
                    if (p.token == 100 || p.token == 101 || p.token == 102 || p.token == 117 || p.token == 116 || p.token == 104 || p.token == 103) {
                        if (p.token==104) {
                            negaBandera=true;
                        }
                        if (p.token == 100) {
                            if (existeVariable(p)) {
                                exp_sim();
                            } else {
                                impSemantico(1);
                                if (errorsemantico) {
                                    p = p.sig;
                                }
                            }
                        } else {
                            exp_sim();
                        }

                    } else if (p.token == 214) {
                        p = p.sig;
                        if (p.token == 117) {
                            p = p.sig;
                            if (p.token == 118) {
                                p = p.sig;
                            } else {
                                imprimirError(3);
                            }
                        } else {
                            imprimirError(2);
                        }

                    } else if (p.token == 122) {
                        agregarValor(p);
                        tipos.add("string");
                        p = p.sig;

                    } else if (p.token == 210 || p.token == 211) {
                        agregarValor(p);
                        tipos.add("boolean");
                        p = p.sig;

                    } else {
                        imprimirError(15);
                    }

                    if (p.token == 125) {
                        limpiarComprobar();
                        asignarValor();
                       
                        p = p.sig;

                    } else {
                        imprimirError(8);
                    }
                } else {
                    imprimirError(9);
                }
            } else {
                impSemantico(1);
                while (renglonError == p.renglon) {
                    p = p.sig;
                }
                evaluacionVariable = false;
                

            }

        } else if (p.token == 120) {

        } else {
            imprimirError(10);
        }

    }

    private void exp_cond() {
        exp_sim();

        exp_rel();
        exp_sim();

    }

    private void cicloimpresion() {
        if (p.token == 124) {
            p = p.sig;
            if (p.token == 100) {
                if (existeVariable(p)) {
                    p = p.sig;
                    if (p.token == 124) {
                        cicloimpresion();
                    }
                } else {
                    impSemantico(1);
                    p = p.sig;
                }

            } else {
                imprimirError(6);
            }
        }
    }

    private void exp_rel() {
        if (p.token == 108) {
            p = p.sig;
        } else if (p.token == 109) {
            p = p.sig;
        } else if (p.token == 110) {
            p = p.sig;
        } else if (p.token == 111) {
            p = p.sig;
        } else if (p.token == 112) {
            p = p.sig;
        } else if (p.token == 113) {
            p = p.sig;
        } else {

            imprimirError(11);
        }
    }

    private void exp_sim() {

        termino();

        if (p.token == 103 || p.token == 104 || p.token == 115) {
            evDirecta = false;
            if (negaBandera) {
                negaBandera=false;
                valorTemp="-"+valorTemp;
            }
            
            
            op_aditivo();

            exp_sim();
        }

    }

    private void op_aditivo() {
        
        if (p.token == 103) {
            valorTemp+=p.lexema;
            p = p.sig;
        } else if (p.token == 104) {
            valorTemp+=p.lexema;
            p = p.sig;
        } else if (p.token == 115) {
            valorTemp+=p.lexema;
            p = p.sig;
        } else {
            imprimirError(12);
        }
    }

    private void signo() {
        if (p.token == 103) {
            
            p = p.sig;
        } else if (p.token == 104) {
            p = p.sig;

        }
    }

    private void termino() {
        factor();
        if (p.token == 105 || p.token == 106 || p.token == 114) {
            op_mult();
            termino();
        }

    }

    private void factor() {
        if (p.token == 100) {
            if (evaluacionVariable) {
                agregarValor(p);
            }
            if (existeVariable(p)) {
                insertarTipo(p);
                p = p.sig;
            } else {
                impSemantico(1);
                p = p.sig;

            }

        } else if (p.token == 116) {
            p = p.sig;
            factor();
        } else if (p.token == 101) {
            agregarValor(p);
            p = p.sig;
            tipos.add("int");
        } else if (p.token == 102) {
            agregarValor(p);
            p = p.sig;
            tipos.add("float");
        } else if (p.token == 117) {
            p = p.sig;
            exp_sim();
            if (p.token == 118) {
                p = p.sig;
            } else {
                imprimirError(3);
            }
        } else if (p.token == 103 || p.token == 104) {
            signo();
            termino();
        } else {
            imprimirError(13);
        }
    }

    private void op_mult() {
        if (p.token == 105) {
            valorTemp+=p.lexema;
            p = p.sig;
        } else if (p.token == 106) {
            valorTemp+=p.lexema;
            p = p.sig;
        } else if (p.token == 114) {
            valorTemp+=p.lexema;
            p = p.sig;
        } else {
            imprimirError(14);
        }

    }

    private void imprimirError(int nerror) {
        for (String[] error : errores) {
            if (nerror == Integer.valueOf(error[1])) {
                impresion += (error[0]);
                impresion += "\n";
                errorSintactico=true;
                p = p.sig;
            }
        }
    }

    private void impSemantico(int nerror) {
        for (String[] error : errorSemantico) {
            if (nerror == Integer.valueOf(error[1])) {
                renglonError = p.renglon;
                impresion += (error[0]);
                impresion += "\n";
                errorsemantico = true;
            }
        }

    }

    private void insertarNodoVariables() {
        errorsemantico = false;
        if (primerVariable) {
            TablaSimbolos tablasimbolos = new TablaSimbolos(p.renglon, tipo, nombreVariable);
            cabezaVariables = tablasimbolos;
            S = tablasimbolos;
            nombresVistos.add(S.nombre);
            primerVariable = false;
        } else {
            if (existeVariable(p)) {
                impSemantico(2);
            } else {
                TablaSimbolos tablasimbolos = new TablaSimbolos(p.renglon, tipo, nombreVariable);

                S.siguiente = tablasimbolos;
                S = tablasimbolos;
                nombresVistos.add(S.nombre);
            }
        }
    }

    private void imprimirListaVariables() {
        S = cabezaVariables;
        while (S != null) {
          //  System.out.println("num linea :" + S.numLinea + " tipo: " + S.tipo + " nombre variable: " + S.nombre + " con valor " + S.valor);
            S = S.siguiente;
        }
    }

    private boolean existeVariable(nodo P) {
        return nombresVistos.contains(P.lexema);
    }

    private void insertarTipo(nodo variable) {
        TablaSimbolos s;
        s = cabezaVariables;
        do {
            if (s.nombre.equals(variable.lexema)) {
                tipos.add(s.tipo);
                break;
            } else {
                s = s.siguiente;
            }
        } while (s != null);
    }

    private void comprobarCompatibilidad() {
        boolean correcto = true;
        String tipovar = tipos.get(0);
        for (String elemento : tipos) {
            if (!elemento.equals(tipovar)) {
                
                correcto = false;
                break;
            }
        }



    }

    private void comprobarImpresion(nodo p) {
        TablaSimbolos s;
        s = cabezaVariables;
        do {
            if (s.nombre.equals(p.lexema)) {
                if (s.tipo.equals("string")) {
                  
                    break;
                } else {
                    impresion += "Error de impresion: ";
                    impSemantico(3);
                    break;
                }
            } else {
                s = s.siguiente;
            }
        } while (s != null);

    }

    private void agregarValor(nodo p) {
        if (evDirecta) {
            valorTemp = p.lexema;
        }else{
            valorTemp += p.lexema;
        }
        
    }

    private void asignarValor() {
        TablaSimbolos s;
            s = cabezaVariables;
        if (evDirecta) {
            do {
                if (s.nombre.equals(evaluada)) {
                    if (comprobarLongitud(s)) {
                        if (negaBandera) {
                            s.valor = "-"+valorTemp;
                            negaBandera=false;
                        }else{
                          s.valor = valorTemp;  
                        }
                        intermedio+="ASIGNAR "+s.nombre+","+valorTemp+"\n";
                        break;
                    } else {
                        impSemantico(4);
                        break;
                    }

                } else {
                    s = s.siguiente;
                }
            } while (s != null);
        }else{
            while(s!=null){
                if(s.nombre.equals(evaluada)){
                    s.valor=valorTemp;
                    generarCodigoIntermedio(valorTemp);
                    intermedio+="ASIGNAR "+s.nombre+",TEMP"+tempCount+"\n";
                }
                s=s.siguiente;
            }
        }

    }

    private boolean comprobarLongitud(TablaSimbolos s) {
        boolean estado = false;
        for (String[] tipoLongitud : longitud) {
            if (s.tipo.equals(tipoLongitud[0])) {
                int longitudMaxima = Integer.parseInt(tipoLongitud[1]);
                if (valorTemp.length() <= longitudMaxima) {
                    estado = true;
                    break;
                } else {
                    estado = false;
                }

            } else {
                estado = false;
            }
        }
        return estado;
    }
    
    
     
     public static void generarCodigoIntermedio(String expresion) {
        Stack<String> pilaOperadores = new Stack<>();
        Stack<String> pilaOperandos = new Stack<>();

        int i = 0;
        while (i < expresion.length()) {
            char caracter = expresion.charAt(i);

            if (Character.isDigit(caracter)) {
                // Si el caracter es un dígito, agrega el número a la pila de operandos
                int inicio = i;
                while (i < expresion.length() && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    i++;
                }
                pilaOperandos.push(expresion.substring(inicio, i));
                i--; // Ajusta el índice para el siguiente ciclo
            } else if (Character.isLetter(caracter)) {
                // Si el caracter es una letra, es una variable
                int inicio = i;
                while (i < expresion.length() && Character.isLetterOrDigit(expresion.charAt(i))) {
                    i++;
                }
                pilaOperandos.push(expresion.substring(inicio, i));
                i--; // Ajusta el índice para el siguiente ciclo
            } else if (esOperador(caracter)) {
                // Si el caracter es un operador, maneja la jerarquía de operaciones
                while (!pilaOperadores.isEmpty() && tienePrecedencia(pilaOperadores.peek(), String.valueOf(caracter))) {
                    // Genera el código intermedio
                    String operador = pilaOperadores.pop();
                    String operando2 = pilaOperandos.pop();
                    String operando1 = pilaOperandos.pop();
                    String temp = generarTemp();
                    generarCodigo(operador, operando1, operando2, temp);
                    pilaOperandos.push(temp);
                }
                // Agrega el operador actual a la pila de operadores
                pilaOperadores.push(convertirOperador(String.valueOf(caracter)));
            }

            i++;
        }

        // Procesa los operadores restantes en la pila de operadores
        while (!pilaOperadores.isEmpty()) {
            String operador = pilaOperadores.pop();
            String operando2 = pilaOperandos.pop();
            String operando1 = pilaOperandos.pop();
            String temp = generarTemp();
            generarCodigo(operador, operando1, operando2, temp);
            pilaOperandos.push(temp);
        }

        
    }

    public static boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static boolean tienePrecedencia(String op1, String op2) {
        // Define la precedencia de los operadores
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-")))
            return true;
        return false;
    }

    public static String generarTemp() {
        // Genera nombres de variables temporales únicas
        return "TEMP" + tempCount++;
    }

    public static void generarCodigo(String operador, String operando1, String operando2, String resultado) {
        // Convierte los operadores a palabras clave
        if (operador.equals("+")) {
            operador = "SUM";
        } else if (operador.equals("-")) {
            operador = "SUB";
        } else if (operador.equals("*")) {
            operador = "MUL";
        } else if (operador.equals("/")) {
            operador = "DIV";
        }

        // Imprime el código intermedio con la instrucción de asignación
        intermedio+=operador + " " + operando1 + ", " + operando2 + " : ASIGNAR " + resultado+"\n";
    }

    public static String convertirOperador(String operador) {
        // Convierte los operadores a palabras clave
        if (operador.equals("+")) {
            return "SUM";
        } else if (operador.equals("-")) {
            return "SUB";
        } else if (operador.equals("*")) {
            return "MUL";
        } else if (operador.equals("/")) {
            return "DIV";
        }
        return operador;
    }
    
    
    
    private void IntermedioData(){
        intermedio+="Segmento DATA\n";
        TablaSimbolos c;
        c=cabezaVariables;
        do {
            intermedio+="DECLARAR "+c.tipo+" "+c.nombre+"\n";
            c=c.siguiente;
        } while (c!=null);
        intermedio+="Segmento CODE\n";
    }
}
