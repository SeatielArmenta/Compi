/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi;

/**
 *
 * @author seati
 */
public class sintaxis {
    
    TablaSimbolos cabezaVariables=null,S;
    String nombreVariable;
    String tipo;
   
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
                                
                                statements();
                                ciclostatements();
                                if (p.token == 120) {
                                    System.out.println("Analisis sintactico completado correctamente");
                                    imprimirListaVariables();
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

    private void variables() {
        tipos();
        if (p.token == 100) {
            nombreVariable=p.lexema;
            insertarNodoVariables();
            p = p.sig;
            ciclovariables();
            

        } else {
            imprimirError(6);

        }
    }

    private void tipos() {
        if (p.token==207) {
            p=p.sig;
           if (p.token == 209 || p.token == 208 || p.token == 212 || p.token == 213) {
            tipo=p.lexema;
            p = p.sig;
        } else {
            imprimirError(7);

        } 
        }else{
            imprimirError(16);
        }
        

    }

    private void ciclovariables() {
        if (p.token == 124) {
            p = p.sig;
            if (p.token == 100) {
                nombreVariable=p.lexema;
                insertarNodoVariables();
                p = p.sig;
                ciclovariables();

            } else {
                imprimirError(6);

            }

        } else if (p.token == 125) {
            p = p.sig;
            if (p.token==207) {
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
                    imprimirError(6);
                }

            } else {
                imprimirError(2);
            }

        } //Evaluacion de variable
        else if (p.token == 100) {

            p = p.sig;
            if (p.token == 123) {
                p = p.sig;
                if (p.token==100 || p.token==101||p.token==102||p.token==117||p.token==116||p.token==104||p.token==103) {
                    exp_sim();
                } else if (p.token==214) {
                    p = p.sig;
                    if (p.token==117) {
                        p = p.sig;
                        if (p.token==118) {
                            p = p.sig;
                        }else{
                            imprimirError(3);
                        }
                    }else{
                        imprimirError(2);
                    }
                    
                }else if(p.token==122){
                p=p.sig;
                } 
                
                else{
                    imprimirError(15);
                }
                

                if (p.token == 125) {
                    p = p.sig;

                } else {
                    imprimirError(8);
                }
            } else {
                imprimirError(9);
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
                p = p.sig;
                if (p.token == 124) {
                    cicloimpresion();
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

            op_aditivo();

            exp_sim();
        }

    }

    private void op_aditivo() {
        if (p.token == 103) {
            p = p.sig;
        } else if (p.token == 104) {
            p = p.sig;
        } else if (p.token == 115) {
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
            p = p.sig;
        } else if (p.token == 116) {
            p = p.sig;
            factor();
        } else if (p.token == 101) {
            p = p.sig;
        } else if (p.token == 102) {
            p = p.sig;
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
            p = p.sig;
        } else if (p.token == 106) {
            p = p.sig;
        } else if (p.token == 114) {
            p = p.sig;
        } else {
            imprimirError(14);
        }

    }

    private void imprimirError(int nerror) {
        for (String[] error : errores) {
            if (nerror == Integer.valueOf(error[1])) {

                System.out.println(error[0]);
                
            }
        }
    }
    
    private void insertarNodoVariables() {
        TablaSimbolos tablasimbolos=new TablaSimbolos(p.renglon, tipo, nombreVariable);
        System.out.println("new variable "+p.renglon +tipo +nombreVariable);
        if (cabezaVariables==null) {
            cabezaVariables=tablasimbolos;
            S=cabezaVariables;
        }else{
            S.siguiente=tablasimbolos;
            S=tablasimbolos;
        }
        
    }
    
    private void imprimirListaVariables(){
        S=cabezaVariables;
        while(S !=null){
            System.out.println("num linea :"+S.numLinea +" tipo: "+S.tipo+" nombre variable: "+S.nombre);
            S=S.siguiente;
        }
    }

}
