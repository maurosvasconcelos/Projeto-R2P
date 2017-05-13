package br.com.r2p.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import br.com.r2p.Dao.PessoaDao;
import br.com.r2p.Modal.Pessoa;
import br.com.r2p.R;
import br.com.r2p.Util.Conexao;
import br.com.r2p.Util.Mask;

public class CadastrarPessoa extends AppCompatActivity implements GridView.OnClickListener, Runnable {

    private EditText editTextId;
    private EditText editTextCpf;
    private EditText editTextNome;
    private Button btnSalvar;
    private ProgressDialog proDialog;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alerta;
    private Handler handler = new Handler();
    private Pessoa pessoa;

    private EditText editTextDtNascimento;
    private EditText editTextEmail;
    private EditText editTextCelular;
    private EditText editTextFixo;
    private CheckBox checkBoxMasc;
    private CheckBox checkBoxFem;
    private String msg;
    private String cpf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_pessoa01);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //PEGO OS DADOS DA INTENT ANTERIOR
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        cpf = bundle.getString("CPF");

        editTextCpf = (EditText) findViewById(R.id.editTextCpf);
        editTextCpf.addTextChangedListener(Mask.insert("###.###.###-##", editTextCpf));
        editTextCpf.setText(cpf);

        editTextDtNascimento = (EditText) findViewById(R.id.dtNacimento);
        editTextDtNascimento.addTextChangedListener(Mask.insert("##/##/####", editTextDtNascimento));

        editTextCelular = (EditText) findViewById(R.id.editTextCelular);
        editTextCelular.addTextChangedListener(Mask.insert("(##)#####-####", editTextCelular));

        editTextFixo = (EditText) findViewById(R.id.editTextFixo);
        editTextFixo.addTextChangedListener(Mask.insert("(##)####-####", editTextFixo));

        checkBoxMasc = (CheckBox) findViewById(R.id.checkBoxM);
        checkBoxFem = (CheckBox) findViewById(R.id.checkBoxF);

        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(this);

        editTextNome = (EditText) findViewById(R.id.editTextNome);
        editTextNome.setFocusableInTouchMode(true);
        editTextNome.requestFocus();
        editTextNome.setSelection(0);
        msg = "";
    }


    @Override
    public void onClick(View view) {

        if (!Conexao.isOnline(getApplicationContext())) {
            Toast.makeText(CadastrarPessoa.this, "Falha de Conexão com a sua Internet.", Toast.LENGTH_LONG).show();
            return;
        }

        proDialog = new ProgressDialog(this);
        proDialog.setTitle("Informação");
        proDialog.setMessage("Salvando ...");
        proDialog.show();

        editTextCpf = (EditText) findViewById(R.id.editTextCpf);
        String cpf = Mask.unmask(editTextCpf.getText().toString());

        editTextNome = (EditText) findViewById(R.id.editTextNome);
        editTextDtNascimento = (EditText) findViewById(R.id.dtNacimento);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextCelular = (EditText) findViewById(R.id.editTextCelular);
        String celular = Mask.unmask(editTextCelular.getText().toString());
        editTextFixo = (EditText) findViewById(R.id.editTextFixo);
        String fixo = Mask.unmask(editTextFixo.getText().toString());
        checkBoxFem = (CheckBox) findViewById(R.id.checkBoxF);
        checkBoxMasc = (CheckBox) findViewById(R.id.checkBoxM);

        pessoa = new Pessoa();
        pessoa.setCpf(cpf);
        pessoa.setNome(editTextNome.getText().toString());
        // pessoa.setCodigo(Long.parseLong(editTextId.getText().toString()));
        pessoa.setDataNascimento(editTextDtNascimento.getText().toString());
        pessoa.setCelular(celular);
        pessoa.setFixo(fixo);
        pessoa.setEmail(editTextEmail.getText().toString());

        if (checkBoxFem.isChecked()) {
            pessoa.setSexo("F");
        }
        if (checkBoxMasc.isChecked()) {
            pessoa.setSexo("M");
        }

        String retorno = pessoa.validarPessoa(pessoa);

        if (!retorno.isEmpty()) {
            exibirAlertaPessoa(retorno);
        } else {
            Thread tread = new Thread(this);
            tread.start();
        }
        //Toast.makeText(getApplicationContext(), "Dados Cadastrados!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void run() {
        try {

            final PessoaDao pessoaDao = new PessoaDao();
            String url = Conexao.IP_ATUAL + Conexao.INCLUIR;
            String retorno = pessoaDao.incluir(url, pessoa);

            if (retorno.equals("200")) {
                url = Conexao.IP_ATUAL + Conexao.CONSULTAR_CPF + cpf;
                String resultado = pessoaDao.ConectarWS(url);

                if (resultado.equals("1")) {
                    CadastrarPessoa.super.finish();
                    Bundle bundle = new Bundle();
                    bundle.putString("CPF", cpf);
                    Intent intent = new Intent(getApplicationContext(), CadastrarPessoa.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (resultado.equals("2")) {
                    CadastrarPessoa.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CadastrarPessoa.this, "Sistema fora do ar. Entrar em contato com o departamento T.I.", Toast.LENGTH_LONG).show();
                        }
                    });
                } else if (!resultado.isEmpty()) {
                    CadastrarPessoa.super.finish();
                    //CHAMO A TELA DE EDIÇÃO
                    Bundle bundle = new Bundle();
                    bundle.putString("resultado", resultado);
                    Intent intent = new Intent(getApplicationContext(), Activity_inicial.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else {
                    CadastrarPessoa.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CadastrarPessoa.this, "Sistema fora do ar. Entrar em contato com o departamento T.I.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else if (retorno.equals("2")) {
                CadastrarPessoa.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CadastrarPessoa.this, "Sistema fora do ar. Entrar em contato com o departamento T.I.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                CadastrarPessoa.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CadastrarPessoa.this, "Dados não processados. \nFavor tente novamente!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception erro) {
            Log.e("ERRO", erro.getMessage());
        } finally {
            proDialog.dismiss();
        }
    }

    private void exibirAlertaPessoa(String msg) {
        alerta = new AlertDialog.Builder(CadastrarPessoa.this);
        alerta.setTitle("Aviso");

        alerta.setIcon(R.mipmap.ic_aviso).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Cancelar escolhido", Toast.LENGTH_SHORT).show();
                        proDialog.dismiss();
                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "OK escolhido", Toast.LENGTH_SHORT).show();
                proDialog.dismiss();
            }
        });
        alertDialog = alerta.create();
        alertDialog.show();
    }

    public void onCheckboxClickedM(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        checkBoxMasc = (CheckBox) findViewById(R.id.checkBoxM);
        checkBoxMasc.setChecked(true);
        checkBoxFem = (CheckBox) findViewById(R.id.checkBoxF);
        checkBoxFem.setChecked(false);
    }

    public void onCheckboxClickedF(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        checkBoxFem = (CheckBox) findViewById(R.id.checkBoxF);
        checkBoxFem.setChecked(true);
        checkBoxMasc = (CheckBox) findViewById(R.id.checkBoxM);
        checkBoxMasc.setChecked(false);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.
                Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Fechar Aplicação")
                .setMessage("Deseja sair da aplicação?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        // startActivity(intent);
                        //finish();

                        Intent intente = new Intent();
                        setResult(1, intente);
                        finish();
                    }

                })
                .setNegativeButton("Não", null)
                .show();
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
            CadastrarPessoa.super.finish();
            Intent intentPrincipal = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentPrincipal);
        }
        return super.onOptionsItemSelected(item);
    }


}
