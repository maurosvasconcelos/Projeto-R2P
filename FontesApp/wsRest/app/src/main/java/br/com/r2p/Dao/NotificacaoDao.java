package br.com.r2p.Dao;


import android.util.Log;

import br.com.r2p.Service.NotificacaoService;
import br.com.r2p.Modal.Notificacao;

import java.util.ArrayList;

/**
 * Created by mauro on 03/05/2017.
 */

public class NotificacaoDao {
    NotificacaoService notificacaoService = new NotificacaoService();


    public ArrayList<Notificacao> listarNotificoes(String url, String cpf) {

        ArrayList<Notificacao> lstNotificacao = new ArrayList<Notificacao>();

        try {
            lstNotificacao = notificacaoService.listarNotificoes(url, cpf);
        } catch (Throwable t) {
            Log.i("erro", t.toString());
        }

        return lstNotificacao;
    }
}


