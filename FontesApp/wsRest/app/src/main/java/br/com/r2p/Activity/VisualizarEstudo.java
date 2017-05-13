package br.com.r2p.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import br.com.r2p.Dao.NotificacaoDao;
import br.com.r2p.Modal.Notificacao;
import br.com.r2p.Modal.Pessoa;
import br.com.r2p.R;
import br.com.r2p.Util.Conexao;

import java.util.ArrayList;

public class VisualizarEstudo extends AppCompatActivity {
    private ListView listViewNotificacao;
    NotificacaoDao notificacaoDao = new NotificacaoDao();
    private String cpf;
    private String resultado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Pessoa pessoa = new Pessoa();
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_visualizar_estudo);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //PEGO OS DADOS DA INTENT ANTERIOR
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            resultado = bundle.getString("resultado");
            pessoa = pessoa.toPessoa(resultado);

            //String url = Conexao.IP_ATUAL + Conexao.LISTAR_TODODAS_NOTIFICACAO;

            String url = Conexao.IP_ATUAL + Conexao.LISTAR_TODODAS_NOTIFICACAO_PESSOA + pessoa.getCpf();


            ArrayList<Notificacao> lstNotificacao = notificacaoDao.listarNotificoes(url, pessoa.getCpf());
            listViewNotificacao = (ListView) findViewById(R.id.listViewEstudos);

            //estudei aqui para criar http://blog.alura.com.br/personalizando-uma-listview-no-android/
            ArrayAdapter<Notificacao> adapter = new ArrayAdapter<Notificacao>(this, R.layout.listviewestudos, lstNotificacao);
            listViewNotificacao.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
            Toast.makeText(VisualizarEstudo.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            pessoa = null;
        }
    }


    public void voltarTelaEditar(View view) {
        VisualizarEstudo.super.finish();
        Intent intentPrincipal = new Intent(getApplicationContext(), EditarPessoa.class);
        startActivity(intentPrincipal);
    }


    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_estudo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Voltar) {
            VisualizarEstudo.super.finish();
            Bundle bundle = new Bundle();
            bundle.putString("CPF", cpf);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        } else {
            VisualizarEstudo.super.finish();
            Bundle bundle = new Bundle();
            bundle.putString("resultado", resultado);
            Intent intent = new Intent(getApplicationContext(), Activity_inicial.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.
                Builder(this)
                .setIcon(R.mipmap.ic_atencao)
                .setTitle("Fechar Aplicação")
                .setMessage("Deseja sair da aplicação?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //startActivity(intent);
                        VisualizarEstudo.super.finish();
                        //android.os.Process.killProcess(android.os.Process.myPid());
                        //Intent intente = new Intent();
                        //setResult(1, intente);
                        //finish();
                    }

                })
                .setNegativeButton("Não", null)
                .show();
    }

}
