

package br.com.r2p.Dao;

import android.os.StrictMode;
import android.util.Log;

import br.com.r2p.Modal.Pessoa;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class PessoaDao {

    // private String[] params;

    public String ConectarWS(String url) {

        // HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_16);
        HttpConnectionParams.setConnectionTimeout(params, 1000);
        HttpClient httpclient = new DefaultHttpClient(params);

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
          /*
          comentando para de pois eu estudar com ofunciona o trabalhar com o json

            if (!responseBody.equals("null") && retorno != "") {
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
            }
            */

        } catch (UnknownHostException errro) {
            Log.e("erro", errro.toString());
            // trata o erro de conexão.
            retorno = "2";
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("erro", e.toString());
            retorno = "2";
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("erro", e.toString());
            retorno = "2";
        } catch (Throwable t) {
            Log.e("erro", t.toString());
            retorno = t.getMessage();
            retorno = "2";
        }

        if (retorno.equals("null") || retorno == "") {
            //retorno = "CPF não cadastrado em nossa base de dados!";
            retorno = "1";
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
            Log.e("Error: ", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("erro", e.toString());
        } catch (Throwable t) {
            Log.i("erro", t.toString());
            Log.e("Error: ", t.getMessage());
        }
        return lstPessoas;

    }


    public String incluir(String url, Pessoa pessoa) {

        HttpClient httpclient = new DefaultHttpClient();
        String retorno = "";

        // Instantiate a GET HTTP method
        try {

            JSONObject pessoaObj = new JSONObject();
            //  pessoaObj.put("Pessoa", pessoa );
            // pessoaObj.put("codigo", pessoa.getCodigo());
            pessoaObj.put("nome", pessoa.getNome());
            pessoaObj.put("cpf", pessoa.getCpf());
            pessoaObj.put("celular", pessoa.getCelular());
            pessoaObj.put("fixo", pessoa.getFixo());
            pessoaObj.put("email", pessoa.getEmail());
            pessoaObj.put("dataNascimento", pessoa.getDataNascimento());
            pessoaObj.put("sexo", pessoa.getSexo());

            HttpPost post = new HttpPost(url);

            StringEntity se = new StringEntity(pessoaObj.toString());
            post.setEntity(se);
            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            //httpclient.execute(post);

            HttpResponse httpresponse = httpclient.execute(post);

            StatusLine statusLine = httpresponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) { // Ok
                retorno = "200";
            } else {
                retorno = "2";
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "2";
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("erro", e.toString());
            return "2";
        } catch (Throwable t) {
            Log.i("erro", t.toString());
            return "2";
        }
        return retorno;

    }


    public String alterar(String url, Pessoa pessoa) {

        //HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_16);
        HttpConnectionParams.setConnectionTimeout(params, 1000);
        HttpClient httpclient = new DefaultHttpClient(params);

        String retorno = "";


        // Instantiate a GET HTTP method
        try {

            JSONObject pessoaObj = new JSONObject();
            //  pessoaObj.put("Pessoa", pessoa );
            pessoaObj.put("codigo", pessoa.getCodigo());
            pessoaObj.put("nome", pessoa.getNome());
            pessoaObj.put("cpf", pessoa.getCpf());
            pessoaObj.put("celular", pessoa.getCelular());
            pessoaObj.put("fixo", pessoa.getFixo());
            pessoaObj.put("email", pessoa.getEmail());
            pessoaObj.put("dataNascimento", pessoa.getDataNascimento());
            pessoaObj.put("sexo", pessoa.getSexo());

            HttpPost post = new HttpPost(url);

            StringEntity se = new StringEntity(pessoaObj.toString());
            post.setEntity(se);
            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            //httpclient.execute(post);

            HttpResponse httpresponse = httpclient.execute(post);

            StatusLine statusLine = httpresponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Ok
                retorno = "200";
            } else {
                retorno = "2";
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.i("erro", e.toString());
            return "2";
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("erro", e.toString());
            return "2";
        } catch (Throwable t) {
            Log.i("erro", t.toString());
            return "2";
        }
        return retorno;

    }


}
