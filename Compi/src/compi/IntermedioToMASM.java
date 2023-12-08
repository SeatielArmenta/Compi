
package compi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntermedioToMASM {
 static List<String> asmResultado = new ArrayList<>();
 static List<String> declaraciones = new ArrayList<>();
 static List<String> operaciones = new ArrayList<>();
 static boolean multdiv=false;
 static int numciclo;
 static int numif;
 
 public static List<String> obtenerPalabras(String linea) {
        // Utilizamos una expresión regular para obtener las cadenas entre comillas
        Pattern patronCadenas = Pattern.compile("\"[^\"]*\"");
        Matcher matcherCadenas = patronCadenas.matcher(linea);

        // Reemplazamos las cadenas entre comillas por un marcador y las almacenamos en una lista
        List<String> cadenasEntreComillas = new ArrayList<>();
        while (matcherCadenas.find()) {
            String cadena = matcherCadenas.group();
            cadenasEntreComillas.add(cadena);
            linea = linea.replace(cadena, "");
        }

        // Dividimos la línea en palabras (excluyendo las cadenas entre comillas)
        String[] palabras = linea.split("\\s+");

        // Convertimos el array de palabras a una lista
        List<String> palabrasList = new ArrayList<>(Arrays.asList(palabras));

        // Agregamos las cadenas entre comillas en la lista final con las comillas
        for (String cadena : cadenasEntreComillas) {
            palabrasList.add("" + cadena + "");
        }
        System.out.println(palabrasList);
        return palabrasList;
    }
 
    public static List<String> convertirASM(List<String> codigoIntermedio) {
        numciclo=0;
        numif=0;
         List<List<String>> palabrasPorLinea = new ArrayList<>();

        for (String linea : codigoIntermedio) {
            List<String> palabrasList = obtenerPalabras(linea);
            palabrasPorLinea.add(palabrasList);
        }
        asmResultado.add(".model small");
        asmResultado.add(".386");
        asmResultado.add(".stack 64");
        asmResultado.add(".data");
        
         for (List<String> palabras : palabrasPorLinea) {
                procesarLinea(palabras);
            }
         asmResultado.addAll(declaraciones);
         asmResultado.add(".code");
         asmResultado.add("main PROC");
         asmResultado.add("MOV ax, @data");
         asmResultado.add("MOV ds, ax");
         asmResultado.addAll(operaciones);
         
         asmResultado.add(".exit");
         asmResultado.add("main ENDP");
         asmResultado.add("print PROC");
         asmResultado.add("mov bx,10");
         asmResultado.add("xor cx,cx");
         asmResultado.add("aa: xor dx,dx");
         asmResultado.add("div bx");
         asmResultado.add("push dx");
         asmResultado.add("inc cx");
         asmResultado.add("test ax,ax");
         asmResultado.add("jnz aa");
         asmResultado.add("bb: pop dx");
         asmResultado.add("add dl,\"0\"");
         asmResultado.add("mov ah,02h");
         asmResultado.add("int 21h");
         asmResultado.add("loop bb");
         asmResultado.add("ret");
         asmResultado.add("print endp");
         asmResultado.add("end main");
         
         System.out.println(asmResultado);
        
        return asmResultado;
    }

    public IntermedioToMASM(String dir,int temps, String dir2) {
        
        for (int i = 1; i < temps+1; i++) {
            declaraciones.add("t"+i+" db ?");
        }
        try {
          List<String> lst = Files.readAllLines(Paths.get(dir));
        List<String> assm=convertirASM(lst);
         escribirArchivoASM(dir2+".asm", assm);
            System.out.println("Archivo .asm creado exitosamente.");
            asmResultado.clear();
            operaciones.clear();
            declaraciones.clear();
                   
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     public static void escribirArchivoASM(String rutaArchivo, List<String> contenido) throws IOException {
        Path archivoPath = Paths.get(rutaArchivo);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoPath.toFile()))) {
            for (String linea : contenido) {
                writer.write(linea);
                writer.newLine(); // Agregar un salto de línea después de cada línea
            }
            writer.close();
        }
    }
    
     public static void procesarLinea(List<String> palabras) {
        if (!palabras.isEmpty()) {
            switch (palabras.get(0)) {
                case "DECLARAR":
                    declaraciones.add(palabras.get(2)+" db ?");
                    System.out.println(declaraciones);
                    break;
                case "ASIGNAR":
                    if (palabras.get(1).contains("t")) {
                        if (multdiv==false) {
                            operaciones.add("mov "+palabras.get(1)+", al");
                        }else{
                        operaciones.add("mov "+palabras.get(1)+", al");
                        multdiv=false;
                        }
                        
                    }else{
                        if (palabras.contains("string")) {
                             String valorBuscado = palabras.get(1)+" db ?";
                             String nuevoValor = palabras.get(1)+" db "+palabras.get(3)+ ",10,13,'$'";
                             declaraciones.replaceAll(registro -> registro.equals(valorBuscado) ? nuevoValor : registro);

                        }else{
                            operaciones.add("xor ax,ax");
                        if (multdiv==false) {
                            if (Character.isLetter(palabras.get(1).charAt(0))&&Character.isLetter(palabras.get(2).charAt(0))) {
                                operaciones.add("mov bl, "+palabras.get(2));
                                operaciones.add("mov "+palabras.get(1)+",bl");
                            }else{
                            operaciones.add("mov "+palabras.get(1)+", "+palabras.get(2));
                            }
                            
                        }else{
                        operaciones.add("mov "+palabras.get(1)+", al");
                        multdiv=false;
                        }
                        }
                    
                    }
                    break;

                case "SUB":
                    operaciones.add("mov al, "+palabras.get(1));
                    operaciones.add("sub al, "+palabras.get(2));
                    break;

                case "DIV":
                    operaciones.add("mov al, "+palabras.get(1));
                    operaciones.add("mov bl, "+palabras.get(2));
                    operaciones.add("div bl");
                    multdiv=true;
                    break;

                case "MUL":
                    operaciones.add("mov al, "+palabras.get(2));
                    operaciones.add("mov bl, "+palabras.get(1));
                    operaciones.add("mul bl");
                    multdiv=true;
                    break;

                case "ADD":
                    operaciones.add("mov al, "+palabras.get(2));
                    operaciones.add("add al, "+palabras.get(1));
                    break;
                case "IMPRIMIR":
                    if (palabras.get(2).equals("string")) {
                    operaciones.add("mov dx, offset "+palabras.get(1));
                    operaciones.add("mov ah, 9");
                    operaciones.add("int 21h");
                    operaciones.add("xor ax, ax");
                    }else{
                    operaciones.add("mov al, "+palabras.get(1));
                    operaciones.add("call print");
                    
                    /*operaciones.add("mov ah, 0");
                    operaciones.add("add al, "+palabras.get(1));
                    operaciones.add("call numerostr");
                    operaciones.add("mov dx, offset buffer");
                    operaciones.add("mov ah, 09h");
                    operaciones.add("int 21h");*/
                    }
                    break;
                case "CICLO":
                    operaciones.add("C"+numciclo+":");
                    operaciones.add("mov al, "+palabras.get(1));
                    operaciones.add("cmp al, "+palabras.get(3));
                    if (palabras.get(2).equals("<")) {
                        operaciones.add("JG C"+(numciclo+1));
                    }else if (palabras.get(2).equals(">")) {
                        operaciones.add("JL C"+(numciclo+1));
                    }else if (palabras.get(2).equals("<=")) {
                        operaciones.add("JGE C"+(numciclo+1));
                    }else if (palabras.get(2).equals(">=")) {
                        operaciones.add("JLE C"+(numciclo+1));
                    }else if (palabras.get(2).equals("==")) {
                        operaciones.add("JNE C"+(numciclo+1));
                    }else if (palabras.get(2).equals("!=")) {
                        operaciones.add("JE C"+(numciclo+1));
                    }
                    numciclo++;
                    break;
                case "IF":
                    
                    operaciones.add("mov al, "+palabras.get(1));
                    operaciones.add("cmp al, "+palabras.get(3));
                    if (palabras.get(2).equals("<")) {
                        operaciones.add("JG I"+(numif));
                    }else if (palabras.get(2).equals(">")) {
                        operaciones.add("JL I"+(numif));
                    }else if (palabras.get(2).equals("<=")) {
                        operaciones.add("JGE I"+(numif));
                    }else if (palabras.get(2).equals(">=")) {
                        operaciones.add("JLE I"+(numif));
                    }else if (palabras.get(2).equals("==")) {
                        operaciones.add("JNE I"+(numif));
                    }else if (palabras.get(2).equals("!=")) {
                        operaciones.add("JE I"+(numif));
                    }
                    
                    break;
                case "FIN":
                    if (palabras.get(1).equals("CICLO")) {
                        operaciones.add("JMP C"+(numciclo-1));
                        operaciones.add("C"+numciclo+":");
                    }else if (palabras.get(1).equals("IF")) {
                        operaciones.add("JMP I"+(numif+1));
                        operaciones.add("I"+numif+":");
                    }else if (palabras.get(1).equals("ELSE")) {
                        operaciones.add("I"+(numif+1)+":");
                    }
                default:
                    break;
            }
        }
    }

       

    
}

