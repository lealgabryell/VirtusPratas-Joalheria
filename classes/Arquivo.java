package classes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Arquivo {
    public static void escreverArquivo(String caminhoArquivo, String texto) {
        try (
                FileWriter criadorDeArquivos = new FileWriter(caminhoArquivo, true);
                BufferedWriter buffer = new BufferedWriter(criadorDeArquivos);
                PrintWriter escritorDeArquivos = new PrintWriter(buffer);) {
            escritorDeArquivos.append(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
