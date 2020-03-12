package ecommand.TelaEditarPrato;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ecommand.ConexaoBanco;
import ecommand.Main;
import ecommand.Prato;
import ecommand.TabelaLista;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TelaEditarPratoController implements Initializable {

    private TabelaLista tabelaPrato = new TabelaLista(); //objeto do tipo TabelaPrato
    private String sql = "select * from prato order by codprato;"; //String sql
    private String caminhoFoto;

    @FXML
    private TableView<Prato> tabelaPratos;
    @FXML
    private TableColumn colunaPratos, colunaDescricao, colunaCodigo, colunaPreco;
    @FXML
    private TextField txtNome, txtPreco, txtCodigo, txtPesquisa;
    @FXML
    private TextArea txtDescricao;
    @FXML
    private ImageView imgProduto;

    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaGerente/telaGerente.fxml");
    }

    public void acaoPesquisar() {
        String sqlPesquisa = "select * from prato where nome ilike '%" + txtPesquisa.getText() + "%';";
        tabelaPrato.mostraTabelaPratos(tabelaPratos, colunaPratos, colunaDescricao, colunaCodigo, colunaPreco, sqlPesquisa);
    }

    //método que preenche os campos de texto com as informaçoes ao selecionar o prato na tabela
    public void clicarTabela() {
        if (tabelaPratos.getSelectionModel().getSelectedItem() != null) {
            txtNome.setText(tabelaPratos.getSelectionModel().getSelectedItem().getNome());
            txtCodigo.setText(tabelaPratos.getSelectionModel().getSelectedItem().getCodprato());
            txtDescricao.setText(tabelaPratos.getSelectionModel().getSelectedItem().getDescricao());
            txtPreco.setText(tabelaPratos.getSelectionModel().getSelectedItem().getPreco());
            caminhoFoto = tabelaPratos.getSelectionModel().getSelectedItem().getImagem();
            insereImagem();
        }
    }

    private void insereImagem() {
        imgProduto.setImage(new Image("file:///" + caminhoFoto));
    }

    private void limpaImagem() {
        imgProduto.setImage(new Image(getClass().getResourceAsStream("../img/sem_imagem.png")));
    }

    //Método para atualizar os pratos
    public void acaoAtualizar() {
        if (!txtCodigo.getText().isEmpty()) { //verifica se o campo do código não está vazio
            //verifica se todos os campos de texto estão preenchidos
            if (!txtNome.getText().isEmpty() && !txtDescricao.getText().isEmpty() && !txtDescricao.getText().isEmpty() && !txtPreco.getText().isEmpty() && caminhoFoto != null) {
                try {
                    //Cria declaração sql
                    PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("UPDATE Prato set descricao = ? ,nome = ? ,preco = ? , imagem = ? WHERE codprato = ?;");

                    //Insere valores nos parâmetros da declaração sql
                    ps.setString(1, txtDescricao.getText());
                    ps.setString(2, txtNome.getText());
                    ps.setFloat(3, Float.parseFloat(txtPreco.getText()));
                    ps.setString(4, caminhoFoto);
                    ps.setInt(5, Integer.parseInt(txtCodigo.getText()));
                    ps.executeUpdate(); // Executa a declaração sql

                    //Chama método mostraTabela com passagem de parâmetros da tabela, colunas e String sql que será executada
                    tabelaPrato.mostraTabelaPratos(tabelaPratos, colunaPratos, colunaDescricao, colunaCodigo, colunaPreco, sql);
                    acaoLimpar();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Produto atualizado!");
                    alert.show();//Mensagem de Sucesso

                } catch (SQLException e) {
                    //Mensagem de erro
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Erro ao atualizar produto!\nErro: " + e);
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Preencha todos os campos para atualizar!");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Insira o código do prato que deseja atualizar!");
            alert.show();
        }
    }


    public void acaoRemover(){
        if(!txtCodigo.getText().isEmpty()){
            try {
                //Cria declaração sql
                PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("DELETE FROM Prato WHERE codprato = ? ;");
                ps.setInt(1, Integer.parseInt(txtCodigo.getText())); //Insere valor no parâmetro da declaração sql
                ps.executeUpdate(); //Eexecuta declaração sql

                //Chama método mostraTabela com passagem de parâmetros da tabela, colunas e String sql que será executada
                tabelaPrato.mostraTabelaPratos(tabelaPratos,colunaPratos,colunaDescricao,colunaCodigo,colunaPreco,sql);
                acaoLimpar(); //limpa os campos de texto
                JOptionPane.showMessageDialog(null, "Prato Deletado!"); //Mensagem de sucesso
            }
            catch (SQLException e){
                //Mensagem de erro
                JOptionPane.showMessageDialog(null, "Erro ao Deletar prato!\nErro: " + e);
            }
        }
    }

    public void acaoSelecionaImagem(){
        FileChooser f = new FileChooser();
        f.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.jpg", "*.png", "*.jpeg"));
        File file = f.showOpenDialog(new Stage());
        if (file != null) {
            imgProduto.setImage(new Image("file:///"+file.getAbsolutePath()));
            caminhoFoto = file.getAbsolutePath();
        }
    }

    public void acaoLimpar(){
        txtNome.clear(); txtCodigo.clear(); txtDescricao.clear();
        txtPreco.clear(); txtPesquisa.clear();
        limpaImagem();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabelaPrato.mostraTabelaPratos(tabelaPratos,colunaPratos,colunaDescricao,colunaCodigo,colunaPreco,sql);
    }
}

