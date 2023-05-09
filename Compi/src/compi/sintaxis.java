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
                            //s
                            variables();
                            statements();
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

    }
}
