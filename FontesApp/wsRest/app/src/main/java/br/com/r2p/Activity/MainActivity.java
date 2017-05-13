package br.com.r2p.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import br.com.r2p.Dao.PessoaDao;
import br.com.r2p.Modal.Pessoa;
import br.com.r2p.R;
import br.com.r2p.Util.CPF;
import br.com.r2p.Util.Conexao;
import br.com.r2p.Util.Mask;

import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements GridView.OnClickListener, Runnable {


    private Button btnListar;
    private ProgressDialog proDialog;
    private Handler handler = new Handler();
    private EditText editTextCpf;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alerta;
    private long backPressedTime = 0;
    private String cpf = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCpf = (EditText) findViewById(R.id.editTextCpf);
        editTextCpf.addTextChangedListener(Mask.insert("###.###.###-##", editTextCpf));

        btnListar = (Button) findViewById(R.id.btnListar);
        btnListar.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //PEGO OS DADOS DA INTENT ANTERIOR
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.getString("CPF") != null && !bundle.getString("CPF").isEmpty()) {
                    cpf = bundle.getString("CPF");
                    editTextCpf.setText(cpf);
                }
            }
        }
    }


    @Override
    public void onClick(View view) {

        if (!Conexao.isOnline(getApplicationContext())) {
            Toast.makeText(MainActivity.this, "Falha de Conexão com a sua Internet.", Toast.LENGTH_LONG).show();
            return;
        }

        proDialog = new ProgressDialog(this);
        proDialog.setTitle("Informação");
        proDialog.setMessage("Processando ...");
        proDialog.show();

        editTextCpf = (EditText) findViewById(R.id.editTextCpf);
        String cpf = Mask.unmask(editTextCpf.getText().toString());
        if (cpf == null || cpf == "") {
            //validarCpfVazio();
            Toast.makeText(MainActivity.this, "Informe um CPF.", Toast.LENGTH_LONG).show();
            proDialog.dismiss();
            return;
        } else {
            if(cpf.length()< 11){
                Toast.makeText(MainActivity.this, "CPF incompleto.", Toast.LENGTH_LONG).show();
                proDialog.dismiss();
                return;
            }
            if (CPF.isCPFvalido(cpf)) {
                Thread tread = new Thread(this);
                tread.start();
            } else {
                //alertaCpfInvalido();
                Toast.makeText(MainActivity.this, "Informe um CPF válido.", Toast.LENGTH_LONG).show();
                proDialog.dismiss();
                return;
            }
        }
    }

    @Override
    public void run() {
        try {

            editTextCpf = (EditText) findViewById(R.id.editTextCpf);
            String cpf = Mask.unmask(editTextCpf.getText().toString());

            PessoaDao pessoaDao = new PessoaDao();
            String url = Conexao.IP_ATUAL + Conexao.CONSULTAR_CPF + cpf;
            final String resultado = pessoaDao.ConectarWS(url);

            if (resultado.equals("1")) {
                MainActivity.super.finish();
                Bundle bundle = new Bundle();
                bundle.putString("CPF", cpf);
                Intent intent = new Intent(getApplicationContext(), CadastrarPessoa.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (resultado.equals("2")) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Sistema fora do ar. Entrar em contato com o departamento T.I.", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (!resultado.isEmpty()) {
                MainActivity.super.finish();
                //CHAMO A TELA DE EDIÇÃO
                Bundle bundle = new Bundle();
                bundle.putString("resultado", resultado);
                Intent intent = new Intent(getApplicationContext(), Activity_inicial.class);
                intent.putExtras(bundle);
                startActivity(intent);

            } else {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Sistema fora do ar. Entrar em contato com o departamento T.I.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception erro) {
            Log.e("ERRO", erro.getMessage());
        } finally {
            proDialog.dismiss();
        }
    }


    private void validarCpfVazio() {
        alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setTitle("Aviso");
        alerta.setIcon(R.mipmap.ic_atencao).setMessage("Informe o CPF!")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getApplicationContext(), "Cancelar escolhido", Toast.LENGTH_SHORT).show();
                        proDialog.dismiss();
                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getApplicationContext(), "OK escolhido", Toast.LENGTH_SHORT).show();
                proDialog.dismiss();
            }
        });
        alertDialog = alerta.create();
        alertDialog.show();
    }

    private void alertaCpfInvalido() {
        alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setTitle("Aviso");
        alerta.setIcon(R.mipmap.ic_atencao).setMessage("Informe um CPF válido.")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        proDialog.dismiss();
                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                proDialog.dismiss();
            }
        });
        alertDialog = alerta.create();
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.
                Builder(this)
                .setIcon(R.mipmap.ic_atencao)
                .setTitle("Fechar Aplicação")
                .setMessage("Deseja fechar a aplicação?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent intente = new Intent();
                        //setResult(1, intente);
                        //finish();
                        MainActivity.super.finish();
                    }

                })
                .setNegativeButton("Não", null)
                .show();
    }


}