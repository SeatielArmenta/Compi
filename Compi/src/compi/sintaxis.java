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

    nodo p = null;

    public sintaxis(nodo n) {
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
                            if (p.token==120) {
                             System.out.println("Analisis sintactico completado correctamente");    
                            }else{
                                System.out.println("Se espera }");
                            }
                        } else {
                            System.out.println("Se espera {");

                        }
                    } else {
                        System.out.println("Se espera )");

                    }
                } else {
                    System.out.println("Se espera (");

                }
            } else {
                System.out.println("Se espera la palabra main");

            }
            break;
        }

    }
    private void ciclostatements(){
        if (p.token!=120) {
            statements();
            ciclostatements();
        }
    }

    private void variables() {
        tipos();
        if (p.token == 100) {
            p = p.sig;
            ciclovariables();

        } else {
            System.out.println("Se espera identificador");

        }
    }

    private void tipos() {

        if (p.token == 209 || p.token == 208 || p.token == 212 || p.token == 213) {
            p = p.sig;
        } else {
            System.out.println("Se espera tipo de dato");

        }

    }

    private void ciclovariables() {
        if (p.token == 124) {
            p = p.sig;
            if (p.token == 100) {
                p = p.sig;
                ciclovariables();

            } else {
                System.out.println("Se espera un identificador");

            }

        } else if (p.token == 125) {
            p = p.sig;

        } else {
            System.out.println("Se espera un ;");
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
                                        System.out.println("Se espera }");
                                    }
                                } else {
                                    System.out.println("Se espera {");
                                }
                            }
                        } else {
                            System.out.println("Se espera }");
                        }
                    } else {
                        System.out.println("Se espera {");
                    }
                } else {
                    System.out.println("Se espera )");
                }
            } else {
                System.out.println("Se espera un (");
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
                            System.out.println("Se espera }");
                        }
                    } else {
                        System.out.println("Se espera {");
                    }
                } else {

                    System.out.println("Se espera )");
                }
            } else {

                System.out.println("Se espera (");
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
                        p=p.sig;
                        if (p.token == 125) {
                            p = p.sig;
                        } else {
                            System.out.println("Se espera ;");
                        }
                    } else {
                       
                        System.out.println("Se espera )");
                    }
                } else {
                    System.out.println("Se espera identificador");
                }

            } else {
                System.out.println("Se espera (");
            }

        } //Evaluacion de variable
        else if (p.token == 100) {

            p = p.sig;
            if (p.token == 123) {
                p = p.sig;
                exp_sim();

                if (p.token == 125) {
                    p = p.sig;

                } else {

                    System.out.println("Se espera ;");
                }
            } else {
                System.out.println("Se espera =");
            }
        }else if (p.token==120) {
            
        }  else {
            System.out.println("Se espera un statement");
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
                System.out.println("Se espera identificador");
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

            System.out.println("Se espera un operador relacional");
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
            System.out.println("Se espera un operador aditivo");
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
                System.out.println("Se espera )");
            }
        } else if (p.token == 103 || p.token == 104) {
            signo();
            termino();
        } else {
            System.out.println("Se espera factor");
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
            System.out.println("Se espera un operador multiplicativo");
        }

    }

}
