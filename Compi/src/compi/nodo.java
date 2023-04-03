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
public class nodo {
    String lexema;
    int token;
    int renglon;
    nodo sig= null;

    nodo (String lexema,int token,int renglon){
        this.lexema=lexema;
        this.token=token;
        this.renglon=renglon;
    }
}
