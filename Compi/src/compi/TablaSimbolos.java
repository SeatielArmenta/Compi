/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi;

/**
 *
 * @author Usuario
 */
public class TablaSimbolos {
    int numLinea;
    String tipo;
    String nombre;
    TablaSimbolos siguiente=null;

    public TablaSimbolos(int numLinea, String tipo, String nombre) {
        this.numLinea = numLinea;
        this.tipo = tipo;
        this.nombre = nombre;
    }
    
    
    
}
