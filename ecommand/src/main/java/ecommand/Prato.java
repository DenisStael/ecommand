package ecommand;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Prato {

    //Atributos da classe que serão utilizados na tabela de pratos
    private SimpleStringProperty nome;
    private SimpleStringProperty descricao;
    private SimpleFloatProperty preco;
    private SimpleIntegerProperty codprato;
    private SimpleStringProperty imagem;
    private SimpleIntegerProperty codcozinheiro;
    private SimpleIntegerProperty codgarcom;

    //construtor com 4 parâmetros
    public Prato(String nome, int codprato, String descricao, float preco) {
        this.nome = new SimpleStringProperty(nome);
        this.descricao = new SimpleStringProperty(descricao);
        this.preco = new SimpleFloatProperty(preco);
        this.codprato = new SimpleIntegerProperty(codprato);
    }

    public Prato(String nome, int codprato, String descricao, float preco, String imagem) {
        this.nome = new SimpleStringProperty(nome);
        this.descricao = new SimpleStringProperty(descricao);
        this.preco = new SimpleFloatProperty(preco);
        this.codprato = new SimpleIntegerProperty(codprato);
        this.imagem = new SimpleStringProperty(imagem);
    }

    public Prato(String nome, int codprato, String descricao, float preco, String imagem, int codcozinheiro, int codgarcom) {
        this.nome = new SimpleStringProperty(nome);
        this.descricao = new SimpleStringProperty(descricao);
        this.preco = new SimpleFloatProperty(preco);
        this.codprato = new SimpleIntegerProperty(codprato);
        this.imagem = new SimpleStringProperty(imagem);
        this.codcozinheiro = new SimpleIntegerProperty(codcozinheiro);
        this.codgarcom = new SimpleIntegerProperty(codgarcom);
    }

    public Prato(){
    }

    //Getters e Setters
    public String getNome() {
        return nome.get();
    }

    public SimpleStringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getDescricao() {
        return descricao.get();
    }

    public SimpleStringProperty descricaoProperty() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    public String getPreco() {
        return String.valueOf(preco.get());
    }

    public SimpleFloatProperty precoProperty() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco.set(preco);
    }

    public String getCodprato() {
        return String.valueOf(codprato.get());
    }

    public SimpleIntegerProperty codpratoProperty() {
        return codprato;
    }

    public void setCodprato(int codprato) {
        this.codprato.set(codprato);
    }

    public String getImagem() {
        return imagem.get();
    }

    public SimpleStringProperty imagemProperty() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem.set(imagem);
    }

    public int getCodcozinheiro() {
        return codcozinheiro.get();
    }

    public SimpleIntegerProperty codcozinheiroProperty() {
        return codcozinheiro;
    }

    public void setCodcozinheiro(int codcozinheiro) {
        this.codcozinheiro.set(codcozinheiro);
    }

    public int getCodgarcom() {
        return codgarcom.get();
    }

    public SimpleIntegerProperty codgarcomProperty() {
        return codgarcom;
    }

    public void setCodgarcom(int codgarcom) {
        this.codgarcom.set(codgarcom);
    }
}
