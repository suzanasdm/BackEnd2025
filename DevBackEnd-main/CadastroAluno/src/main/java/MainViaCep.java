
import model.Endereco;
import jakarta.xml.bind.JAXBException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainViaCep {

    public static void main(String[] args) throws IOException, JAXBException {

        String cep = "85900122";
        String url = "https://viacep.com.br/ws/" + cep + "/xml/";

        // estabelecendo a conexão
        URL urlViaCep = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlViaCep.openConnection();
        connection.setRequestMethod("GET"); //metodo que pega o conteudo da api

        //leitura do conteudo
        BufferedReader leitor = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String linha;
        StringBuilder resposta = new StringBuilder();

        while ((linha = leitor.readLine()) != null){
            resposta.append(linha); //incrementa na resposta a linha lida
        }
        leitor.close();

        System.out.println(resposta);

        Endereco endereco = Endereco.unmarshalFromString(resposta.toString());

        System.out.println("CEP: " + endereco.getCep() +
                "\nLogradouro: " + endereco.getLogradouro() +
                "\nComplemento: " + endereco.getComplemento() +
                "\nBairro: " + endereco.getBairro() +
                "\nLocalidade: " + endereco.getLocalidade() +
                "\nUF: " + endereco.getUf());

    }

}