package br.com.r2p.Service;

import android.os.StrictMode;
import android.util.Log;

import br.com.r2p.Modal.Notificacao;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mauro on 03/05/2017.
 */

public class NotificacaoService {

    JSONArray notificacoesJson = null;
    ArrayList<Notificacao> lstNotificacao = new ArrayList<Notificacao>();
    Notificacao notificacaoOb = new Notificacao();

    public ArrayList<Notificacao> listarNotificoes(String url, String cpf) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet chamadaget = new HttpGet(url);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(chamadaget, responseHandler);
            JSONObject json = new JSONObject(responseBody);

            String notificacao = json.getString("notificacao");

            Object jsonRetorno = new JSONTokener(notificacao).nextValue();

            if (jsonRetorno instanceof JSONObject) {
                notificacaoOb = notificacaoOb.toNotificacao(notificacao);
                lstNotificacao.add(notificacaoOb);
            } else if (jsonRetorno instanceof JSONArray) {
                notificacoesJson = new JSONArray(notificacao);

                for (int i = 0; i < notificacoesJson.length(); i++) {
                    JSONObject obj = notificacoesJson.getJSONObject(i);
                    Notificacao notificacaoObj = new Notificacao();
                    notificacaoObj.setCodigo(obj.getLong("codigo"));
                    String messages  = new String(obj.getString("mensagem").getBytes("ISO-8859-1"), "UTF-8");
                    notificacaoObj.setMensagem(messages);
                    lstNotificacao.add(notificacaoObj);
                }
            } else {
                //RETORNO LISTA VAZIA
                return lstNotificacao;
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
        return lstNotificacao;

    }


}
