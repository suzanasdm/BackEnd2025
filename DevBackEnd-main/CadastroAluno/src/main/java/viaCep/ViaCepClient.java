package viaCep;

import model.Endereco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViaCepClient {

    public static Endereco buscarEnderecoXML(String cep) throws Exception {
        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) {
            throw new IllegalArgumentException("CEP inválido: " + cep);
        }

        String urlStr = "https://viacep.com.br/ws/" + cepLimpo + "/xml/";
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        int status = con.getResponseCode();
        if (status != 200) {
            throw new IOException("Erro ao consultar ViaCEP. HTTP " + status);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            // Mapeia o XML diretamente para o objeto Endereco
            return Endereco.unmarshalFromString(content.toString());
        } finally {
            con.disconnect();
        }
    }
}