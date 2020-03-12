package ecommand.TelaMontarCardapio;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ecommand.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.lang.Boolean.TRUE;

public class TelaMontarCardapioController implements Initializable {

    private TabelaLista tabelaPrato = new TabelaLista();
    private String sql = "select nome,codprato,descricao,preco,imagem from prato where cardapio = FALSE order by codprato;";//String sql
    private String sql_2 = "select nome,codprato,descricao,preco,imagem from prato where cardapio = TRUE and tipo = 'Comida' order by codprato;";
    private String sql_3 = "select nome,codprato,descricao,preco,imagem from prato where cardapio = TRUE and tipo = 'Bebida' order by codprato;";
    @FXML
    private TableView<Prato> tabelaPratos, tabelaPratos2, tabelaBebida;
    @FXML
    private TableColumn colunaPrato, colunaDescPrato, colunaCodPrato, colunaPreco, colunaNomePrato2, colunaDescPrato2,
            colunaCodPrato2, colunaPreco2, colunaNomeBebida, colunaCodBebida, colunaDescBebida, colunaPrecoBebida;
    @FXML
    private TextField txtNomePrato, txtCodPrato;
    @FXML
    private ImageView imgCardapio;
    @FXML
    private Button botaoSalvarImg;
    private String caminhoFoto = null;
    private ResultSet codFoto;

    @FXML //volta para a tela anterior
    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaGerente/telaGerente.fxml");
    }

    @FXML //pesquisa por nome e mostra na tabela apenas pratos correspondentes à pesquisa
    private void acaoPesquisarPrato() {
        String sqlPesquisa = "select * from prato where nome ilike '%" + txtNomePrato.getText() + "%';";
        tabelaPrato.mostraTabelaPratos(tabelaPratos, colunaPrato, colunaDescPrato, colunaCodPrato, colunaPreco, sqlPesquisa);
    }

    @FXML //preenche o campo de texto com o código do prato selecionado ao clicar numa linha da tabela
    private void clicarTabelaPrato() {
        txtCodPrato.setText(tabelaPratos.getSelectionModel().getSelectedItem().getCodprato());
    }

    @FXML
    private void acaoSelecaoPrato() {
        if (tabelaBebida.getSelectionModel().getSelectedItem() != null)
            tabelaBebida.getSelectionModel().clearSelection();
    }

    @FXML
    private void acaoSelecaoBebida() {
        if (tabelaPratos2.getSelectionModel().getSelectedItem() != null)
            tabelaPratos2.getSelectionModel().clearSelection();
    }

    @FXML
    private void acaoRemoverDoCardapio() {
        int cod = 0;
        boolean selecao = false;
        if (tabelaPratos2.getSelectionModel().getSelectedItem() != null) {
            cod = Integer.parseInt(tabelaPratos2.getSelectionModel().getSelectedItem().getCodprato());
            selecao = true;
        } else if (tabelaBebida.getSelectionModel().getSelectedItem() != null) {
            cod = Integer.parseInt(tabelaBebida.getSelectionModel().getSelectedItem().getCodprato());
            selecao = true;
        }
        if (selecao) {
            try {
                PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement
                        ("update prato set cardapio = FALSE where codprato = ?;");
                ps.setInt(1, cod);
                ps.executeUpdate();

                mostrarTabelas();

                JOptionPane.showMessageDialog(null, "Removido do Cardápio!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao remover do Cardápio!");
            }
        }
    }

    public void acaoImagem() {
        FileChooser f = new FileChooser();
        f.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.jpg", "*.png", "*.jpeg"));
        File file = f.showOpenDialog(new Stage());
        if (file != null) {
            imgCardapio.setImage(new Image("file:///" + file.getAbsolutePath()));
            caminhoFoto = file.getAbsolutePath();
        }
    }

    private void insereImgCardapio() {
        imgCardapio.setImage(new Image(getClass().getResourceAsStream("file:///" + caminhoFoto)));

    }

    @FXML
    private void acaoSalvarFoto() throws SQLException {

            PreparedStatement psteste = ConexaoBanco.getConnection().prepareStatement("SELECT CodImagem FROM ImgCardapio;");
            ResultSet rsteste = psteste.executeQuery();

        if (!rsteste.next() && caminhoFoto != null) {
            try {
                PreparedStatement ps2 = ConexaoBanco.getConnection().prepareStatement("  INSERT INTO ImgCardapio (CodImagem,ImagemCardapio) VALUES (1,?)");
                ps2.setString(1, caminhoFoto);
                ps2.executeUpdate();
                JOptionPane.showMessageDialog(null, "Imagem salva com sucesso");
                desabilitaBotao();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao salvar imagem:" + e);
            }

        }else{
            try {
                PreparedStatement ps3 = ConexaoBanco.getConnection().prepareStatement("UPDATE ImgCardapio SET ImagemCardapio = ? WHERE CodImagem = 1;");
                ps3.setString(1,caminhoFoto);
                ps3.executeUpdate();
                JOptionPane.showMessageDialog(null,"Imagem atualizada com sucesso!");
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Erro ao atualizar imagem!"+e);
            }

        }
    }

    private void desabilitaBotao() {
        botaoSalvarImg.setDisable(TRUE);
    }

    public void acaoAdicionarPrato() {
        if (!txtCodPrato.getText().isEmpty()) {
            try {
                //Declaração SQL pra inserção no banco
                PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement
                        ("UPDATE Prato SET cardapio = TRUE WHERE codprato = ?;");

                //Atribui os parâmetros e os valores à declaração SQL criada anteriormente
                ps.setInt(1, Integer.parseInt(txtCodPrato.getText()));

                ps.executeUpdate();//Executa a declaração SQL

                limpar(); //limpa os campos de texto

                //chama as informações da tabela
                mostrarTabelas();

                //Mensagem de Sucesso
                JOptionPane.showMessageDialog(null, "Prato Cadastrado!");

            } catch (Exception e) {
                //Mensagem de erro
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar prato!\nErro: " + e);
            }
        } else JOptionPane.showMessageDialog(null, "Digite o código do prato que deseja adicionar ao Cardápio!");
    }

    public void limpar() {
        txtCodPrato.clear();
    }

    private void mostrarTabelas() {
        tabelaPrato.mostraTabelaPratos(tabelaPratos, colunaPrato, colunaDescPrato, colunaCodPrato, colunaPreco, sql);
        tabelaPrato.mostraTabelaPratos(tabelaPratos2, colunaNomePrato2, colunaDescPrato2, colunaCodPrato2, colunaPreco2, sql_2);
        tabelaPrato.mostraTabelaPratos(tabelaBebida, colunaNomeBebida, colunaDescBebida, colunaCodBebida, colunaPrecoBebida, sql_3);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarTabelas();
        /*try {
            PreparedStatement ps3 = ConexaoBanco.getConnection().prepareStatement("SELECT CodImagem FROM ImgCardapio;");
            codFoto = ps3.executeQuery();
            System.out.println(codFoto);
            if(codFoto != null) {
                insereImgCardapio();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Erro");
        }
    }*/


    }
}
