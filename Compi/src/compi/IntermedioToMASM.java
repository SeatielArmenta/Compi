
package compi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.nio.file.Path;

public class IntermedioToMASM {
 static List<String> asmResultado = new ArrayList<>();
 static List<String> declaraciones = new ArrayList<>();
 static List<String> operaciones = new ArrayList<>();
    public static List<String> convertirASM(List<String> codigoIntermedio) {
        
         List<List<String>> palabrasPorLinea = new ArrayList<>();

        for (String linea : codigoIntermedio) {
            
            String[] palabras = linea.split("\\s+"); 
             List<String> palabrasList = new ArrayList<>(Arrays.asList(palabras));
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
         asmResultado.addAll(operaciones);
         asmResultado.add("mov ax,4c00h");
         asmResultado.add("int 21h");
         asmResultado.add(".exit");
         asmResultado.add("end");
         
         System.out.println(asmResultado);
        
        return asmResultado;
    }

    public IntermedioToMASM(String dir,int temps) {
        
        for (int i = 1; i < temps+1; i++) {
            declaraciones.add("temp"+i+" dword ?");
        }
        try {
           List<String> lst = Files.readAllLines(Paths.get("C:\\compi\\intermedio.txt"));
        List<String> assm=convertirASM(lst);
         escribirArchivoASM("C:\\compi\\intermedio.asm", assm);
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
                    declaraciones.add(palabras.get(2)+" dword ?");
                    System.out.println(declaraciones);
                    break;
                case "ASIGNAR":
                    if (palabras.get(1).contains("TEMP")) {
                        operaciones.add("mov "+palabras.get(1)+", eax");
                    }else{
                    operaciones.add("mov eax, "+palabras.get(2));
                    operaciones.add("mov "+palabras.get(1)+", eax");
                    }
                    break;

                case "SUB":
                    operaciones.add("mov eax, "+palabras.get(1));
                    operaciones.add("sub eax, "+palabras.get(2));
                    break;

                case "DIV":
                    operaciones.add("mov eax, "+palabras.get(1));
                    operaciones.add("mov ebx, "+palabras.get(2));
                    operaciones.add("div ebx");
                    break;

                case "MUL":
                    operaciones.add("mov eax, "+palabras.get(2));
                    operaciones.add("mov ebx, "+palabras.get(1));
                    operaciones.add("imul eax, ebx");
                    break;

                case "ADD":
                    operaciones.add("mov eax, "+palabras.get(2));
                    operaciones.add("add eax, "+palabras.get(1));
                    break;

                default:
                    break;
            }
        }
    }

       

    
}

