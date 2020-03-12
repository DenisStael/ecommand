package ecommand.TelaMontarPrato;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ecommand.ConexaoBanco;
import ecommand.Main;
import ecommand.TabelaLista;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;

public class TelaMontarPratoController implements Initializable {

    private TabelaLista tabelaPrato = new TabelaLista();
    private String sql = "select nome,codprato,descricao,preco,imagem from prato;";
    private String caminhoFoto = null;

    //Atributos da tela
    @FXML
    TextField txtNomePrato, txtPreco;
    @FXML
    TextArea txtDescricao;
    @FXML
    TableColumn colunaPratos, colunaCodigo, colunaDescricao, colunaPreco;
    @FXML
    TableView tabelaPratos;
    @FXML
    private RadioButton tipoComida, tipoBebida;
    @FXML
    private ImageView imgProduto;

    //método voltat para tela anterior
    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaGerente/telaGerente.fxml");
    }
    //limpa os campos da tela
    public void acaoLimpar(){
        txtNomePrato.clear(); txtPreco.clear();
        txtDescricao.clear(); tipoBebida.setSelected(false);
        tipoComida.setSelected(false);
        insereImagem();
    }

    public void selecaoComida(){
        if(tipoBebida.isSelected()){
            tipoBebida.setSelected(false);
        }
    }

    public void selecaoBebida(){
        if(tipoComida.isSelected()){
            tipoComida.setSelected(false);
        }
    }
    public void acaoSelecionaImagem() {
        FileChooser f = new FileChooser();
        f.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.jpg", "*.png", "*.jpeg"));
        File file = f.showOpenDialog(new Stage());
        if (file != null) {
            imgProduto.setImage(new Image("file:///"+file.getAbsolutePath()));
            caminhoFoto = file.getAbsolutePath();
        }
    }
    //Método de salvar prato no banco de dados
    public void acaoSalvar(){
        //Verifica se todos os campos estão preenchidos
        if(!txtNomePrato.getText().isEmpty() && !txtPreco.getText().isEmpty() && !txtDescricao.getText().isEmpty() && caminhoFoto != null && (tipoBebida.isSelected() || tipoComida.isSelected())){
            try {
                //Cria declaração sql para inserção no banco
                PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("insert into prato(nome,preco,descricao,tipo,imagem)values(?,?,?,?,?);");

                //insere os valores aos parâmetros da declaração sql
                ps.setString(1, txtNomePrato.getText());
                ps.setFloat(2, Float.parseFloat(txtPreco.getText())); //converte para float
                ps.setString(3, txtDescricao.getText());
                ps.setString(5,caminhoFoto);
                if(tipoComida.isSelected()){
                    ps.setString(4, "Comida");
                } else if(tipoBebida.isSelected()){
                    ps.setString(4, "Bebida");
                }

                ps.executeUpdate(); //Executa declaração sql

                acaoLimpar();//Limpa os campos da tela

                insereImagem();

                tabelaPrato.mostraTabelaPratos(tabelaPratos,colunaPratos,colunaDescricao,colunaCodigo,colunaPreco,sql);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Prato cadastrado!");
                alert.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Erro ao cadastrar prato!\nErro: "+e);
                alert.show();
                 //mensagem de erro
            }
            //mensagem de erro
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Preencha todos os campos para adiconar!");
            alert.show();
        }
    }

    private void insereImagem(){
        imgProduto.setImage(new Image(getClass().getResourceAsStream("../img/sem_imagem.png")));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabelaPrato.mostraTabelaPratos(tabelaPratos,colunaPratos,colunaDescricao,colunaCodigo,colunaPreco,sql);
        tipoComida.setSelected(true);
    }
}
