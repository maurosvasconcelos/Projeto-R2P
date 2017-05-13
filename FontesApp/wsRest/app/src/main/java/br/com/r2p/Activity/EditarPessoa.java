package br.com.r2p.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

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

public class EditarPessoa extends AppCompatActivity implements GridView.OnClickListener, Runnable {

    private EditText editTextCpf;
    private EditText editTextNome;
    private Button btnEditar;
    private EditText editTextDtNascimento;
    private EditText editTextEmail;
    private EditText editTextCelular;
    private EditText editTextFixo;
    private CheckBox checkBoxMasc;
    private CheckBox checkBoxFem;
    private Pessoa pessoa = new Pessoa();
    private ProgressDialog proDialog;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alerta;
    private Handler handler = new Handler();

    private EditText editTextCodigo;

    private String msg = "";
    private String resultado = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pessoa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextCpf = (EditText) findViewById(R.id.editTextCpf);
        editTextCpf.addTextChangedListener(Mask.insert("###.###.###-##", editTextCpf));

        editTextDtNascimento = (EditText) findViewById(R.id.dtNacimento);
        editTextDtNascimento.addTextChangedListener(Mask.insert("##/##/####", editTextDtNascimento));

        editTextCelular = (EditText) findViewById(R.id.editTextCelular);
        editTextCelular.addTextChangedListener(Mask.insert("(##)#####-####", editTextCelular));

        editTextFixo = (EditText) findViewById(R.id.editTextFixo);
        editTextFixo.addTextChangedListener(Mask.insert("(##)####-####", editTextFixo));


        //PEGO OS DADOS DA INTENT ANTERIOR
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        resultado = bundle.getString("resultado");
        pessoa = pessoa.toPessoa(resultado);


        editTextNome = (EditText) findViewById(R.id.editTextNome);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);


        editTextNome.setText(pessoa.getNome());
        editTextCpf.setText(pessoa.getCpf());
        if (pessoa.getCelular() != null) {
            editTextCelular.setText(pessoa.getCelular());
        }

        if (pessoa.getFixo() != null) {
            editTextFixo.setText(pessoa.getFixo());
        }

        editTextEmail.setText(pessoa.getEmail());
        editTextDtNascimento.setText(pessoa.getDataNascimento());

        if (pessoa.getSexo().equals("M")) {
            checkBoxMasc = (CheckBox) findViewById(R.id.checkBoxM);
            checkBoxMasc.setChecked(true);
            checkBoxFem = (CheckBox) findViewById(R.id.checkBoxF);
            checkBoxFem.setChecked(false);
        } else {
            checkBoxFem = (CheckBox) findViewById(R.id.checkBoxF);
            checkBoxFem.setChecked(true);
            checkBoxMasc = (CheckBox) findViewById(R.id.checkBoxM);
            checkBoxMasc.setChecked(false);
        }

        editTextCodigo = (EditText) findViewById(R.id.editTextCodigo);
        editTextCodigo.setText(pessoa.getCodigo().toString());


        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(this);

        msg = "";
    }

    @Override
    public void run() {
        try {

            final PessoaDao pessoaDao = new PessoaDao();
            String url = Conexao.IP_ATUAL + Conexao.ALTERAR;
            String retorno = pessoaDao.alterar(url, pessoa);

            if (retorno.equals("200")) {

                url = Conexao.IP_ATUAL + Conexao.CONSULTAR_CPF + pessoa.getCpf();
                resultado = "";
                resultado = pessoaDao.ConectarWS(url);

                if (resultado.equals("1")) {
                    EditarPessoa.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(EditarPessoa.this, "Sistema fora do ar. Entrar em contato com o departamento T.I.", Toast.LENGTH_LONG).show();
                        }
                    });
                } else if (resultado.equals("2")) {
                    EditarPessoa.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(EditarPessoa.this, "Sistema fora do ar. Entrar em contato com o departamento T.I.", Toast.LENGTH_LONG).show();
                        }
                    });
                } else if (!resultado.isEmpty()) {
                    EditarPessoa.super.finish();
                    //CHAMO A TELA DE EDIÇÃO
                    Bundle bundle = new Bundle();
                    bundle.putString("resultado", resultado);
                    Intent intent = new Intent(getApplicationContext(), EditarPessoa.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else {
                    EditarPessoa.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(EditarPessoa.this, "Sistema fora do ar. Entrar em contato com o departamento T.I.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else if (retorno.equals("2")) {
                EditarPessoa.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(EditarPessoa.this, "Sistema fora do ar. Entrar em contato com o departamento T.I.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                EditarPessoa.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(EditarPessoa.this, "Dados não processados. \nFavor tente novamente!", Toast.LENGTH_LONG).show();
                    }
                });
            }

        } catch (Exception erro) {
            Log.e("ERRO", erro.getMessage());
        } finally {
            proDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {

        if (!Conexao.isOnline(getApplicationContext())) {
            Toast.makeText(EditarPessoa.this, "Falha de Conexão com a sua Internet.", Toast.LENGTH_LONG).show();
            return;
        }

        proDialog = new ProgressDialog(this);
        proDialog.setTitle("Informação");
        proDialog.setMessage("Salvando ...");
        proDialog.show();

        editTextCpf = (EditText) findViewById(R.id.editTextCpf);
        String cpf = Mask.unmask(editTextCpf.getText().toString());

        editTextCodigo = (EditText) findViewById(R.id.editTextCodigo);
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
        pessoa.setCodigo(Long.valueOf(editTextCodigo.getText().toString()));
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
        Toast.makeText(getApplicationContext(), "Dados Alterados!", Toast.LENGTH_SHORT).show();
        //exibirAlertaPessoa(msg);
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
                        EditarPessoa.super.finish();
                    }

                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void exibirAlertaPessoa(String msg) {
        alerta = new AlertDialog.Builder(EditarPessoa.this);
        alerta.setTitle("Aviso");

        alerta.setIcon(R.mipmap.ic_atencao).setMessage(msg)
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


    public void voltarTelaPrincipal(View view) {
        EditarPessoa.super.finish();
        Intent intentPrincipal = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentPrincipal);
    }

    public void visualizarEstudos(View view) {
        EditarPessoa.super.finish();
        Bundle bundle = new Bundle();
        String cpf = Mask.unmask(editTextCpf.getText().toString());
        bundle.putString("CPF", cpf);
        Intent intent = new Intent(getApplicationContext(), VisualizarEstudo.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
            EditarPessoa.super.finish();
            Bundle bundle = new Bundle();
            bundle.putString("resultado", resultado);
            Intent intent = new Intent(getApplicationContext(), Activity_inicial.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
