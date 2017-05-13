package br.com.r2p.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mauro on 15/01/2017.
 */

public class Conexao {
    public static String IP_CASA_URL = "http://192.168.1.5:8084/RestFulWS/";
    public static String IP_SOGRA_URL = "http://192.168.25.116:8084/RestFulWS/";
    public static String IP_TRB_URL = "http://192.168.25.230:8084/RestFulWS/";
    public static String IP_TRB_URL_INTERNO = "http://10.14.1.76:8084/RestFulWS/";
    public static String IP_WEB_URL = "http://189.112.226.70:85/RestFulWS/";
    public static String IP_SENAI_URL = "http://192.168.80.152:8084/RestFulWS/";
    public static String IP_ATUAL = IP_WEB_URL;

    //PESSOA
    public static String INCLUIR = "pessoa/incluir";
    public static String ALTERAR = "pessoa/alterar";
    public static String LISTAR_TODOS = "pessoa/listarTodos";
    public static String CONSULTAR_CPF = "pessoa/consultarCpf";
    //NOTIFICACAO
    public static String LISTAR_TODODAS_NOTIFICACAO = "notificacao/listarTodasNotificacao";
    public static String LISTAR_TODODAS_NOTIFICACAO_PESSOA = "notificacao/listarNotificacaoPessoa";



    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnectedOrConnecting();

    }

}
