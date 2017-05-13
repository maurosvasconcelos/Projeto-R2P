package br.com.r2p.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import br.com.r2p.Modal.Pessoa;
import br.com.r2p.R;
import br.com.r2p.Util.Mask;

public class Activity_inicial extends AppCompatActivity {
    private EditText editTextCpf;
    private String resultado = "";
    Pessoa pessoa = new Pessoa();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///PEGO OS DADOS DA INTENT ANTERIOR
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        resultado = bundle.getString("resultado");
        pessoa = pessoa.toPessoa(resultado);
        Toast.makeText(Activity_inicial.this, "Seja bem vindo ao app R2P, " + pessoa.getNome(), Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_estudo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_Voltar) {
            return true;
        } else {
            Activity_inicial.super.finish();
            Bundle bundle = new Bundle();
            //bundle.putString("CPF", cpf);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void visualizarEstudos(View view) {
        Activity_inicial.super.finish();
        Pessoa pessoa = new Pessoa();
        Bundle bundle = new Bundle();
        pessoa.toPessoa(resultado);
        bundle.putString("resultado", resultado);
        Intent intent = new Intent(getApplicationContext(), VisualizarEstudo.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void editarDados(View view) {
        Activity_inicial.super.finish();
        Bundle bundle = new Bundle();
        bundle.putString("resultado", resultado);
        Intent intent = new Intent(getApplicationContext(), EditarPessoa.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
                        Activity_inicial.super.finish();
                    }

                })
                .setNegativeButton("Não", null)
                .show();
    }
}
