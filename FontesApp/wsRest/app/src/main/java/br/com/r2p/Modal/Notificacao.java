package br.com.r2p.Modal;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by mauro on 03/05/2017.
 */

public class Notificacao {

    private Long codigo;
    private String mensagem;


    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Notificacao toNotificacao(String resultado) {
        String messages = "";
        Notificacao notificacao = new Notificacao();
        Gson gson = new Gson();
        JsonElement jsonElementRoot = gson.fromJson(resultado, JsonElement.class);
        JsonObject root = jsonElementRoot.getAsJsonObject();
        JsonObject notidicacaoJson = root.getAsJsonObject();
        if (!notidicacaoJson.get("mensagem").isJsonNull()) {
            try {
                messages = new String(notidicacaoJson.get("mensagem").getAsString().getBytes("ISO-8859-1"), "UTF-8");
                notificacao.setMensagem(messages);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (!notidicacaoJson.get("codigo").isJsonNull()) {
            notificacao.setCodigo(notidicacaoJson.get("codigo").getAsLong());
        }
        return notificacao;
    }


    @Override
    public String toString() {
        return "Estudo: " + mensagem;
    }

}
