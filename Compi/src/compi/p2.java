/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi;

import java.util.Stack;

/**
 *
 * @author Usuario
 */
class Nodo1 {

    Nodo1 izquierda;
    Nodo1 derecha;
    char dato;

    public Nodo1(char dato) {
        this.dato = dato;
    }
}

public class p2 {

    
    String posfija = "";
    static Nodo1 raiz;

    public int orden(char o) {
        switch (o) {
            case '*':
            case '/':
                return 1;
            case '+':
            case '-':
                return 2;
            default:
                return 10;
        }
    }

    public String traductor(String expresion) {
        Stack<Character> operadores = new Stack<>();
        Stack<Character> operando = new Stack<>();
        String temp1="";
        String temp2="";
        for (char i : expresion.toCharArray()) {
            if (i == '(') {
                
                operadores.push(i);
            } else if (i == ')') {
                while (operadores.peek() != '(') {
                    temp2 += operadores.pop();
                }
                operadores.pop();
            } else if (Character.isLetterOrDigit(i)) {
                temp1+=i;
                
            } else {
                while (!operadores.isEmpty() && orden(i) >= orden(operadores.peek())) {
                    posfija += operadores.pop();
                }
                
                operadores.push(i);
            }
        }
        while (!operadores.isEmpty()) {
            posfija += operadores.pop();
        }
        
        return posfija;
    }
    
    public String traductorGod(String expresion){
        String temp1 ="";
        boolean banderaOperando=false;
        Stack<Nodo1> s = new Stack<>();
        Stack<String> operando =new Stack<>();
        Stack<Character> operadores =new Stack<>();
        for (char i :expresion.toCharArray()) {
            if (isOperator(i)) {
                operadores.push(i);
                
            }else if (Character.isLetterOrDigit(i)) {
                while(!Character.isLetterOrDigit(i)){
                        temp1 +=i;
                        
                    }
                
            }
                
            
        }
        return expresion;
    }
    
    public static boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/' || c == '^');
    }

    public Nodo1 Arbol(String posfija) {
        Stack<Nodo1> nodos = new Stack<>();
        for (char i : posfija.toCharArray()) {
            if (Character.isLetterOrDigit(i)) {
                Nodo1 Q = new Nodo1(i);
                nodos.push(Q);
            } else {
                Nodo1 derecho = nodos.pop();
                Nodo1 izquierdo = nodos.pop();
                Nodo1 operador = new Nodo1(i);
                operador.izquierda = izquierdo;
                operador.derecha = derecho;
                nodos.push(operador);
            }
        }
        raiz = nodos.pop();
        return raiz;
    }

    public void Postorden(Nodo1 X) {
        if (X != null) {
            Postorden(X.izquierda);
            Postorden(X.derecha);
            if (X.dato == raiz.dato) {
                System.out.println("Información: " + X.dato);
            } else {
                System.out.println("Información: " + X.dato);
            }
        }
    }

}
