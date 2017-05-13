package br.com.r2p.Modal;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Date;

/**
 * Created by mauro on 01/09/2016.
 */
public class Pessoa {

    private Long codigo;
    private String nome;
    private String cpf;
    private String horaCadastro;
    private Date dataCadastro;

    private String dataNascimento;
    private String email;
    private String celular;
    private String fixo;
    private String sexo;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getHoraCadastro() {
        return horaCadastro;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public void setHoraCadastro(String horaCadastro) {
        this.horaCadastro = horaCadastro;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFixo() {
        return fixo;
    }

    public void setFixo(String fixo) {
        this.fixo = fixo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String validarPessoa(Pessoa pessoa) {
        String msg = "";

        if (pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
            msg += "Informe o Nome" + "\n";
        }
        if (pessoa.getCpf() == null || pessoa.getCpf().isEmpty()) {
            msg += "Informe o Cpf" + "\n";
            ;
        }
        if (pessoa.getDataNascimento() == null || pessoa.getDataNascimento().isEmpty()) {
            msg += "Informe a Data Nascimento" + "\n";
            ;
        }
        if (pessoa.getSexo() == null || pessoa.getSexo().isEmpty()) {
            msg += "Informe o Sexo" + "\n";
            ;
        }
        if (pessoa.getCelular() == null || pessoa.getCelular().isEmpty()) {
            msg += "Informe o Celular";
        }
        return msg;
    }

    public Pessoa toPessoa(String resultado) {
        Pessoa obj = new Pessoa();
        //ESTUDAR AQUI
        //http://www.guj.com.br/t/retornando-apenas-metodos-no-meu-json/334517/2
        Gson gson = new Gson();
        //converte o retorno do web service em um JsonElement
        JsonElement jsonElementRoot = gson.fromJson(resultado, JsonElement.class);
        //converte o JsonElement em JsonObject
        JsonObject root = jsonElementRoot.getAsJsonObject();
        //joga o conteudo de "cliente" em um JsonObject
        JsonObject pessoaJson = root.getAsJsonObject();
        if (pessoaJson.get("codigo") != null) {
            obj.setCodigo(pessoaJson.get("codigo").getAsLong());
        }
        if (pessoaJson.get("nome") != null) {
            obj.setNome(pessoaJson.get("nome").getAsString());
        }
        if (pessoaJson.get("cpf") != null) {
            obj.setCpf(pessoaJson.get("cpf").getAsString());
        }
        if (pessoaJson.get("email") != null) {
            obj.setEmail(pessoaJson.get("email").getAsString());
        }
        if (pessoaJson.get("sexo") != null) {
            obj.setSexo(pessoaJson.get("sexo").getAsString());
        }
        if (pessoaJson.get("celular") != null) {
            obj.setCelular(pessoaJson.get("celular").getAsString());
        }
        if (pessoaJson.get("fixo") != null) {
            obj.setFixo(pessoaJson.get("fixo").getAsString());
        }
        if (pessoaJson.get("dataNascimento") != null) {
            obj.setDataNascimento(pessoaJson.get("dataNascimento").getAsString());
        }


        return obj;
    }


}
