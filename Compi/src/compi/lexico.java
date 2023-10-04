
package compi;
import java.io.RandomAccessFile;
import javax.swing.JOptionPane;

public class lexico {
    String Impresion="";
    nodo cabeza=null,p;
    int estado=0,columna,valorMT,numRenglon=1,caracter=0;
    String lexema="";
    boolean errorEncontrado=false;
    String archivo="";
    
      int[][] matriz = {
                        /*     L,       @,      _,     d,      +,       -,      *,      /,      ^,      <,        >,      =,       !,       &,      |,       (,       ),      {,     },     , ,      ;,    ",    eb,    tab,    nl,      . ,    eof,   oc,   rt*/
                       /*      0,       1,      2,     3,      4,       5,      6,      7,      8,      9,       10,     11,      12,      13,     14,      15,      16,     17,    18,     19,     20,   21,    22,     23,    24,      25,     26,   27,    28*/
                   /* 0 */{    1,       1,      1,     2,    103,     104,    105,      5,    107,      8,        9,     10,      11,      12,     13,     117,     118,    119,   120,    124,    125,   14,     0,      0,     0,     505,      0,  505,     0},
                   /* 1 */{    1,       1,      1,     1,    100,     100,    100,    100,    100,    100,      100,    100,     100,     100,    100,     100,     100,    100,   100,    100,    100,  100,   100,    100,   100,     100,    100,  100,   100}, 
                   /* 2 */{  101,     101,    101,     2,    101,     101,    101,    101,    101,    101,      101,    101,     101,     101,    101,     101,     101,    101,   101,    101,    101,  101,   101,    101,   101,       3,    101,  101,   101},
                   /* 3 */{  500,     500,    500,     4,    500,     500,    500,    500,    500,    500,      500,    500,     500,     500,    500,     500,     500,    500,   500,    500,    500,  500,   500,    500,   500,     500,    500,  500,   500},
                   /* 4 */{  102,     102,    102,     4,    102,     102,    102,    102,    102,    102,      102,    102,     102,     102,    102,     102,     102,    102,   102,    102,    102,  102,   102,    102,   102,     102,    102,  102,   102}, 
                   /* 5 */{  106,     106,    106,   106,    106,     106,      6,    106,    106,    106,      106,    106,     106,     106,    106,     106,     106,    106,   106,    106,    106,  106,   106,    106,   106,     106,    106,  106,   106},
                   /* 6 */{    6,       6,      6,     6,      6,       6,      7,      6,      6,      6,        6,      6,       6,       6,      6,       6,       6,      6,     6,      6,      6,    6,     6,      6,     6,       6,    501,    6,     6}, 
                   /* 7 */{    6,       6,      6,     6,      6,       6,      6,      0,      6,      6,        6,      6,       6,       6,      6,       6,       6,      6,     6,      6,      6,    6,     6,      6,     6,       6,    501,    6,     6},
                   /* 8 */{  108,     108,    108,   108,    108,     108,    108,    108,    108,    108,      108,    110,     108,     108,    108,     108,     108,    108,   108,    108,    108,  108,   108,    108,   108,     108,    108,  108,   108},
                   /* 9 */{  109,     109,    109,   109,    109,     109,    109,    109,    109,    109,      109,    111,     109,     109,    109,     109,     109,    109,   109,    109,    109,  109,   109,    109,   109,     109,    109,  109,   109},
                   /* 10 */{ 123,     123,    123,   123,    123,     123,    123,    123,    123,    123,      123,    112,     123,     123,    123,     123,     123,    123,   123,    123,    123,  123,   123,    123,   123,     123,    123,  123,   123},
                   /* 11 */{ 116,     116,    116,   116,    116,     116,    116,    116,    116,    116,      116,    113,     116,     116,    116,     116,     116,    116,   116,    116,    116,  116,   116,    116,   116,     116,    116,  116,   116},
                   /* 12 */{ 502,     502,    502,   502,    502,     502,    502,    502,    502,    502,      502,    502,     502,     114,    502,     502,     502,    502,   502,    502,    502,  502,   502,    502,   502,     502,    502,  502,   502},
                   /* 13 */{ 503,     503,    503,   503,    503,     503,    503,    503,    503,    503,      503,    503,     503,     503,    115,     503,     503,    503,   503,    503,    503,  503,   503,    503,   503,     503,    503,  503,   503},
                   /* 14 */{  14,      14,     14,    14,     14,      14,     14,     14,     14,     14,       14,     14,      14,      14,     14,      14,      14,     14,    14,     14,     14,  122,    14,     14,   504,      14,    504,   14,   504}
          
      };
    public String[][] palabrasReservadas = {
        {"break", "200"},
        {"if", "201"},
        {"else", "202"},
        {"main", "203"},
        {"while", "204"},
        {"goto", "205"},
        {"print", "206"},
        {"new", "207"},
        {"float", "208"},
        {"int", "209"},
        {"false", "210"},
        {"true", "211"},
        {"string", "212"},
        {"boolean", "213"},
        {"getValue", "214"}

    };

    String errores[][] = {
        {"se espera digito", "500"},
        {"se espera cierre de comentario", "501"},
        {"se espera &", "502"},
        {"se espera un |", "503"},
        {"se espera cierre de cadena", "504"},
        {"caracter no valido", "505"}

    };

    RandomAccessFile file = null;

    public lexico(String archivoPath) {
        archivo = archivoPath;
        try {
            file = new RandomAccessFile(archivo, "r");
            while (caracter != -1) {
                caracter = file.read();

                if (Character.isLetter(((char) caracter))) { //letra
                    columna = 0;
                } else if (Character.isDigit(((char) caracter))) {
                    columna = 3;
                } else {
                    switch ((char) caracter) {
                        case '@':
                            columna = 1;
                            break;
                        case '_':
                            columna = 2;
                            break;
                        case '+':
                            columna = 4;
                            break;
                        case '-':
                            columna = 5;
                            break;
                        case '*':
                            columna = 6;
                            break;
                        case '/':
                            columna = 7;
                            break;
                        case '^':
                            columna = 8;
                            break;
                        case '<':
                            columna = 9;
                            break;
                        case '>':
                            columna = 10;
                            break;
                        case '=':
                            columna = 11;
                            break;
                        case '!':
                            columna = 12;
                            break;
                        case '&':
                            columna = 13;
                            break;
                        case '|':
                            columna = 14;
                            break;
                        case '(':
                            columna = 15;
                            break;
                        case ')':
                            columna = 16;
                            break;
                        case '{':
                            columna = 17;
                            break;
                        case '}':
                            columna = 18;
                            break;
                        case ',':
                            columna = 19;
                            break;
                        case ';':
                            columna = 20;
                            break;
                        case '"':
                            columna = 21;
                            break;
                        case ' ': //espacio en blanco
                            columna = 22;
                            break;
                        case 9: //tabulador
                            columna = 23;
                            break;
                        case 10: //nueva linea
                        {
                            columna = 24;
                            numRenglon = numRenglon + 1;
                        }
                        break;
                        case 13://retorno de carro
                            columna = 28;
                            break;
                        case '.':
                            columna = 25;
                            break;

                        default://otro caracter
                            if (caracter == -1) {
                                columna = 26;

                            } else {
                                columna = 27;
                            }

                            break;
                    }
                }
                valorMT = matriz[estado][columna];
                if (valorMT < 100) { //cambiar estado
                    estado = valorMT;

                    if (estado == 0) {
                        lexema = "";
                    } else {
                        lexema = lexema + (char) caracter;
                    }
                } else if (valorMT >= 100 && valorMT < 500) { //estado final
                    if (valorMT == 100) {
                        validarPalabraReservada();
                    }
                    // Valores finales adelantados: 100,101,102,106,123,108,109,116 
                    if (valorMT == 100 || valorMT == 101 || valorMT == 102 || valorMT == 106 || valorMT == 123 || valorMT == 108 || valorMT == 109 || valorMT == 116 || valorMT >= 200) {
                        file.seek(file.getFilePointer() - 1);
                    } else {
                        lexema = lexema + (char) caracter;
                    }
                    insertarNodo();
                    estado = 0;
                    lexema = "";
                } else { //estado de error
                    if (valorMT == 501 || valorMT==504) {
                        imprimirError();
                        break;
                    }else{
                    imprimirError();
                    }
                    
                }

            }
            if (errorEncontrado) {

                //   imprimirNodos();
                Impresion += "Analisis lexico completado con errores ❌❌❌";

            } else {

                //  imprimirNodos();
                Impresion += "Analisis Lexico completado sin errores 😊😊😊";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private void insertarNodo() {
        nodo nodo = new nodo(lexema, valorMT, numRenglon);
        if (cabeza == null) {
            cabeza = nodo;
            p = cabeza;
        } else {
            p.sig = nodo;
            p = nodo;
        }
    }

    private void imprimirNodos() {
        p = cabeza;
        while (p != null) {
            Impresion += (p.lexema + " |  " + p.token + " |  " + p.renglon);
            Impresion += System.lineSeparator();
            p = p.sig;
        }
    }

    private void validarPalabraReservada() {
        for (String[] palreservada : palabrasReservadas) {
            if (lexema.equals(palreservada[0])) {
                valorMT = Integer.valueOf(palreservada[1]);
            }
        }
    }

    private void imprimirError() {
        if ((caracter != -1 && valorMT >= 500)) {
            for (String[] error : errores) {
                if (valorMT == Integer.valueOf(error[1])) {
                    Impresion += "El error encontrado es: " + error[0] + ", error " + valorMT + " caracter " + caracter + " en el renglon " + numRenglon;
                    Impresion += System.lineSeparator();
                }
            }
            errorEncontrado = true;
        } else if ((caracter == -1) && (valorMT == 501 || valorMT==504)) {
            for (String[] error : errores) {
                if (valorMT == Integer.valueOf(error[1])) {
                    
                    try {
                        file.seek(file.getFilePointer() - 1);
                        caracter = file.read();
                    } catch (Exception e) {
                    }
                    Impresion += "El error encontrado es: " + error[0] + ", error " + valorMT + " caracter " + caracter + " en el renglon " + numRenglon;
                    Impresion += System.lineSeparator();
                }
            }
            errorEncontrado = true;
        }
    }
    
    public String[][] enviarArray(){
        String[][] arreglo=palabrasReservadas;
        System.out.println(arreglo[0][0]);
        return arreglo;
    }
}