package ecommand.TelaGerenciarEstoque;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ecommand.ConexaoBanco;
import ecommand.Main;
import ecommand.Produto;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class TelaGerenciarEstoqueController implements Initializable {

    private Produto tabelaProduto = new Produto(); //Tabela dos produtos cadastrados no banco
    private String sql = "select * from produto order by codproduto;"; //String sql
    //Atributos da tela
    @FXML
    private TextField txtNomeProduto,txtQtdProduto, txtCodProduto, txtPesquisar; //Caixas de texto
    @FXML
    private TableColumn colunaProd, colunaDescricao, colunaCod, colunaQuantidade, colunaMedida; //Colunas da tabela
    @FXML
    private TableView<Produto> tabelaProdutos; //Tabela
    @FXML
    private TextArea txtDescricao; //Caixa de texto
    @FXML
    private RadioButton radioKilo, radioUnidade, radioLitro;

    //Método de voltar para tela anterior
    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaGerente/telaGerente.fxml"); //Chama a scene
    }

    //Método de limpar os campos da tela
    public void acaoLimpar() {
        txtCodProduto.clear(); txtDescricao.clear();
        txtNomeProduto.clear(); txtQtdProduto.clear();
        txtPesquisar.clear(); radioKilo.setSelected(false);
        radioLitro.setSelected(false); radioUnidade.setSelected(false);
    }

    //Método de remover os produtos do banco
    public void acaoRemoverProduto() {
        if(!txtCodProduto.getText().isEmpty()){
            try {
                int cod = Integer.parseInt(txtCodProduto.getText()); //Recebe o código inserido pelo usuário convertido para inteiro

                //Cria declaração sql
                PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("DELETE FROM Produto WHERE codproduto = ? ;");
                ps.setInt(1, cod); //Insere valor no parâmetro da declaração sql
                ps.executeUpdate(); //Eexecuta declaração sql

                //Chama método mostraTabela com passagem de parâmetros da tabela, colunas e String sql que será executada
                tabelaProduto.mostraTabela(tabelaProdutos,colunaProd,colunaDescricao,colunaCod,colunaQuantidade,colunaMedida,sql);
                acaoLimpar(); //limpa os campos de texto
                JOptionPane.showMessageDialog(null, "Produto Deletado!"); //Mensagem de sucesso
            }
            catch (SQLException e){
                //Mensagem de erro
                JOptionPane.showMessageDialog(null, "Erro ao Deletar produto!\nErro: " + e);
            }
        } else JOptionPane.showMessageDialog(null,"Insira o código do produto que deseja remover!");
    }

    //Método de atualizar produtos
    public void acaoAttProduto() {
            if(!txtCodProduto.getText().isEmpty()){
                if(!txtNomeProduto.getText().isEmpty() && !txtQtdProduto.getText().isEmpty() && !txtDescricao.getText().isEmpty() &&
                        (radioLitro.isSelected() || radioKilo.isSelected() || radioUnidade.isSelected())){
                    try {
                        //Cria declaração sql
                        PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("UPDATE Produto set descricao = ? ,nome = ? ,quantidade = ?, medida = ? WHERE codproduto = ? ;");

                        //Insere valores nos parâmetros da declaração sql
                        ps.setString(1, txtDescricao.getText());
                        ps.setString(2, txtNomeProduto.getText());
                        ps.setFloat(3, Float.parseFloat(txtQtdProduto.getText()));
                        if(radioUnidade.isSelected()){
                            ps.setString(4, "Un");
                        } else if(radioKilo.isSelected()){
                            ps.setString(4, "Kg");
                        } else if(radioLitro.isSelected()){
                            ps.setString(4, "Lt");
                        }
                        ps.setInt(5, Integer.parseInt(txtCodProduto.getText()));
                        ps.executeUpdate(); // Executa a declaração sql

                        //Chama método mostraTabela com passagem de parâmetros da tabela, colunas e String sql que será executada
                        tabelaProduto.mostraTabela(tabelaProdutos,colunaProd,colunaDescricao,colunaCod,colunaQuantidade,colunaMedida,sql);
                        JOptionPane.showMessageDialog(null, "Produto Atualizado!");//Mensagem de Sucesso
                    }
                    catch(SQLException e) {
                        //Mensagem de erro
                        JOptionPane.showMessageDialog(null, "Erro ao Atualizar produto!\nErro: " + e);
                    }
                } else JOptionPane.showMessageDialog(null,"Preencha todos os campos para atualizar!");
            } else JOptionPane.showMessageDialog(null,"Insira o código do produto que deseja atualizar!");
            acaoLimpar();
    }

    //Método de pesquisar pelos produtos por nome
    public void acaoPesquisar() {
        String sqlPesquisa = "select * from produto where nome ilike '%"+txtPesquisar.getText()+"%';";
        tabelaProduto.mostraTabela(tabelaProdutos,colunaProd,colunaDescricao,colunaCod,colunaQuantidade,colunaMedida,sqlPesquisa);
    }

    //ao clicar numa linha da tabela pega os valores e preenche os campos de texto
    public void acaoClicarTabela(){
        txtNomeProduto.setText(tabelaProdutos.getSelectionModel().getSelectedItem().getNome());
        txtDescricao.setText(tabelaProdutos.getSelectionModel().getSelectedItem().getDescricao());
        txtQtdProduto.setText(tabelaProdutos.getSelectionModel().getSelectedItem().getQuantidade());
        if(tabelaProdutos.getSelectionModel().getSelectedItem().getMedida().equals("Kg")){
            radioKilo.setSelected(true);
        } else if(tabelaProdutos.getSelectionModel().getSelectedItem().getMedida().equals("Lt")){
            radioLitro.setSelected(true);
        }else if(tabelaProdutos.getSelectionModel().getSelectedItem().getMedida().equals("Un")){
            radioUnidade.setSelected(true);
        }
        txtCodProduto.setText(tabelaProdutos.getSelectionModel().getSelectedItem().getCodproduto());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Chama método mostraTabela com passagem de parâmetros da tabela, colunas e String sql que será executada
        tabelaProduto.mostraTabela(tabelaProdutos,colunaProd,colunaDescricao,colunaCod,colunaQuantidade,colunaMedida,sql);
    }
}
