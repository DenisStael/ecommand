package ecommand;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Produto {

    //Lista de objetos do tipo produto
    private ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    //atributos da classe que serão utilizados nas colunas da tabela
    private SimpleStringProperty nome;
    private SimpleStringProperty descricao;
    private SimpleFloatProperty quantidade;
    private SimpleIntegerProperty codproduto;
    private SimpleStringProperty medida;

    //construtor com 5 parâmetros
    public Produto(String nome, int codproduto, String descricao, float quantidade, String medida) {
        this.nome = new SimpleStringProperty(nome);
        this.descricao = new SimpleStringProperty(descricao);
        this.quantidade = new SimpleFloatProperty(quantidade);
        this.codproduto = new SimpleIntegerProperty(codproduto);
        this.medida = new SimpleStringProperty(medida);
    }
    public Produto(String nome,String descricao){
        this.nome = new SimpleStringProperty(nome);
        this.descricao = new SimpleStringProperty(descricao);
    }
    public Produto(String nome,String descricao,String medida,float quantidade){
        this.nome = new SimpleStringProperty(nome);
        this.descricao = new SimpleStringProperty(descricao);
        this.medida = new SimpleStringProperty(medida);
        this.quantidade = new SimpleFloatProperty(quantidade);


    }

    public Produto() {
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

    public String getQuantidade() {
        return String.valueOf(quantidade.get());
    }

    public SimpleFloatProperty quantidadeProperty() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade.set(quantidade);
    }

    public String getCodproduto() {
        return String.valueOf(codproduto.get());
    }

    public SimpleIntegerProperty codprodutoProperty() {
        return codproduto;
    }

    public void setCodproduto(int codproduto) {
        this.codproduto.set(codproduto);
    }

    public String getMedida() {
        return medida.get();
    }

    public SimpleStringProperty medidaProperty() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida.set(medida);
    }

    /*método que mostra a tabela com todos os produtos em estoque e
    recebe como parâmetros a tabela que será apresentada, suas colunas e a string sql*/
    public void mostraTabela(TableView tabelaProdutos, TableColumn colunaProd, TableColumn colunaDescricao, TableColumn colunaCod, TableColumn colunaQuantidade, TableColumn colunaMedida, String sql){
        try {
            this.listaProdutos.clear();//limpa a lista
            Statement stmt = ConexaoBanco.getConnection().createStatement();//cria declaração sql
            ResultSet rs = stmt.executeQuery(sql); //executa a declaração e armazena o resultado

            //enquanto há resultados na consulta, registra os produtos na lista
            while (rs.next()){
                this.listaProdutos.add(new Produto(rs.getString("nome"), rs.getInt("codproduto"),
                        rs.getString("descricao"), rs.getInt("quantidade"), rs.getString("medida")));
            }
            rs.close(); //fecha o resultset

            //atribui às colunas da tabela os valores
            colunaProd.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colunaCod.setCellValueFactory(new PropertyValueFactory<>("codproduto"));
            colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
            colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
            colunaMedida.setCellValueFactory(new PropertyValueFactory<>("medida"));

            //insere os itens na tabela
            tabelaProdutos.setItems(this.listaProdutos);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao apresentar produtos!\n"+e);
        }
    }
}
