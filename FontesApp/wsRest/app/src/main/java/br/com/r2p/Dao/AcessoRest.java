

package br.com.r2p.Dao;

import android.os.StrictMode;
import android.util.Log;

import br.com.r2p.Modal.Pessoa;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class AcessoRest {


    private int TIMEOUT_MILLISEC = 3000;


    // private String[] params;

    public String ConectarWS(String url) {


        HttpClient httpclient = new DefaultHttpClient();

        HttpGet chamadaget = new HttpGet(url);
        String retorno = "";

        ArrayList<Pessoa> lstPessoas = new ArrayList<Pessoa>();

        // Instantiate a GET HTTP method
        try {
            //Aqui o ideal é colocar a requesição assíncrona
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String responseBody = httpclient.execute(chamadaget, responseHandler);

            retorno = responseBody;
            // se quiParse
            JSONObject json = new JSONObject(responseBody);
            //Pessoa pes = new Pessoa();
            // pes.setNome(json.getString("pessoa"));

            String teste = json.getString("pessoa");


            JSONArray pessoasJson = new JSONArray(teste);

            for (int i = 0; i < pessoasJson.length(); i++) {
                JSONObject obj = pessoasJson.getJSONObject(i);
                Pessoa pessoa = new Pessoa();
                pessoa.setCodigo(obj.getLong("codigo"));
                pessoa.setNome(obj.getString("nome"));
                pessoa.setCpf(obj.getString("cpf"));
                lstPessoas.add(pessoa);
            }


        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.i("erro", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("erro", e.toString());
        } catch (Throwable t) {
            Log.i("erro", t.toString());
        }

        if (retorno == "") {
            retorno = "Erro de conexão WS!";
        }
        lstPessoas.isEmpty();

        return retorno;

    }


    public ArrayList<Pessoa> ConectarWSListaPessoas(String url) {


        HttpClient httpclient = new DefaultHttpClient();

        HttpGet chamadaget = new HttpGet(url);

        ArrayList<Pessoa> lstPessoas = new ArrayList<Pessoa>();

        // Instantiate a GET HTTP method
        try {
            //Aqui o ideal é colocar a requesição assíncrona
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String responseBody = httpclient.execute(chamadaget, responseHandler);


            // se quiParse
            JSONObject json = new JSONObject(responseBody);
            //Pessoa pes = new Pessoa();
            // pes.setNome(json.getString("pessoa"));

            String teste = json.getString("pessoa");


            JSONArray pessoasJson = new JSONArray(teste);

            for (int i = 0; i < pessoasJson.length(); i++) {
                JSONObject obj = pessoasJson.getJSONObject(i);
                Pessoa pessoa = new Pessoa();
                pessoa.setCodigo(obj.getLong("codigo"));
                pessoa.setNome(obj.getString("nome"));
                pessoa.setCpf(obj.getString("cpf"));
                lstPessoas.add(pessoa);
            }


        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.i("erro", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("erro", e.toString());
        } catch (Throwable t) {
            Log.i("erro", t.toString());
        }
        return lstPessoas;

    }
}
