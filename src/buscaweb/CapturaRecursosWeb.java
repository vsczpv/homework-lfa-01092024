/*
 * Importado de https://github.com/alexrese/BuscaPadraoWeb/
 */

package buscaweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 *
 * @author Santiago
 */
public class CapturaRecursosWeb {
    private final ArrayList<String> listaRecursos = new ArrayList<>();


    public ArrayList<String> carregarRecursos() {
        ArrayList<String> resultado = new ArrayList<>();
        for (var stringURL : listaRecursos) {
            String resposta = "";

            try {
                URL url = new URL(stringURL);
                URLConnection connection = url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));

                String inputLine;

                var sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) sb.append(inputLine).append("\n");
                resposta = sb.toString();
                resultado.add(resposta);
                in.close();
            } catch (IOException ex) {
                //noinspection CallToPrintStackTrace
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    /**
     * @return the listaRecursos
     */
    public ArrayList<String> getListaRecursos() {
        return listaRecursos;
    }
}