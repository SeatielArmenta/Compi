/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi;

/**
 *
 * @author alberto
 */
public class Compi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("equisde");
        System.out.println("joto");
        System.out.println("ily");
        
        String palabrasReservadas[][] = {

            {"break","200"},
            {"if","201"},
            {"else","202"},
            {"main","203"},
            {"while","204"},
            {"goto","205"},
            {"print","206"},
            {"new","207"},
            {"float","208"},
            {"int","209"},
            {"false","210"},
            {"true","211"},
            {"string","212"}

        };

        String errores[][]={

            {"se espera digito","500"},
            {"se espera cierre de comentario","501"},
            {"se espera &","502"},
            {"se espera un |","503"},
            {"se espera cierre de cadena","504"},
            {"caracter no valido","505"}

        };
    }
    
}
