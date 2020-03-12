package ecommand.TelaAtendimento;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ecommand.ConexaoBanco;
import ecommand.Prato;
import ecommand.TabelaLista;
import javax.swing.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class TelaInformacaoController implements Initializable {

    private String sql = "";
    public static int codPedido;
    private int codPrato;
    private TabelaLista tabelaLista = new TabelaLista();
    @FXML
    private Button botaoSituacao;
    @FXML
    private TableView<Prato> tabelaPratos;
    @FXML
    private TableColumn colunaPrato, colunaDescPrato, colunaCodPrato;
    @FXML
    private TableColumn<Prato, Integer> colunaStatus;
    @FXML
    private Label labelStatus;

    public void acaoFinalizar(){
        boolean finalizado = true;
        if(tabelaPratos.getSelectionModel().getSelectedItem() != null) {
            if(TelaAtendimentoController.getUsuario().getTipo().equals("Garçom")){
                if(tabelaPratos.getSelectionModel().getSelectedItem().getCodcozinheiro() == 0){
                    labelStatus.setText("Este prato ainda não foi finalizado!");
                    finalizado = false;
                }
            } else {
                finalizado = true;
            }
        }

        if(finalizado){
            String sql_2 = "";
            String sql_3 = "";
            if(TelaAtendimentoController.getUsuario().getTipo().equals("Garçom")){
                sql_2 = "update pedidoprato set codgarcom = ? where codprato = ? and codpedido = ? and codpedprato = ?;";
                sql_3 = "select min(codpedprato) as codigo from pedidoprato where codprato = ? and codpedido = ? and codgarcom is null;";
            } else if(TelaAtendimentoController.getUsuario().getTipo().equals("Cozinheiro")){
                sql_2 = "update pedidoprato set codcozinheiro = ? where codprato = ? and codpedido = ? and codpedprato = ?;";
                sql_3 = "select min(codpedprato) as codigo from pedidoprato where codprato = ? and codpedido = ? and codcozinheiro is null;";
            }
            try {
                PreparedStatement ps2 = ConexaoBanco.getConnection().prepareStatement(sql_3);
                ps2.setInt(1,codPrato);
                ps2.setInt(2,codPedido);
                ResultSet rs = ps2.executeQuery();

                if(rs.next()){
                    PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement(sql_2);
                    ps.setInt(1, TelaAtendimentoController.getUsuario().getCodusuario());
                    ps.setInt(2, codPrato);
                    ps.setInt(3, codPedido);
                    ps.setInt(4, rs.getInt("codigo"));
                    ps.executeUpdate();
                }

                labelStatus.setText("");
                montaTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"Erro "+e);
            }
        }
    }

    public void acaoPegaCodigoTabela(){
        if(tabelaPratos.getSelectionModel().getSelectedItem() != null){
            codPrato = Integer.parseInt(tabelaPratos.getSelectionModel().getSelectedItem().getCodprato());
        }
    }

    private void montaTabela(){
        tabelaLista.mostraPratosAtendimento(tabelaPratos,colunaPrato,colunaDescPrato,colunaCodPrato,colunaStatus,sql);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(TelaAtendimentoController.getUsuario().getTipo().equals("Garçom")){
            botaoSituacao.setText("Prato entregue");
            sql = "select p.nome, p.descricao, p.codprato ,p.preco, p.imagem, pp.codcozinheiro, pp.codgarcom from prato p, pedido pe, pedidoprato pp where " +
                    "p.codprato = pp.codprato and pe.codpedido = pp.codpedido and pe.codpedido = "+codPedido+" order by pp.codpedprato;";
        } else if(TelaAtendimentoController.getUsuario().getTipo().equals("Cozinheiro")){
            botaoSituacao.setText("Prato Finalizado");
            sql = "select p.nome, p.descricao, p.codprato ,p.preco, p.imagem, pp.codcozinheiro, pp.codgarcom from prato p, pedido pe, pedidoprato pp where " +
                    "p.codprato = pp.codprato and p.tipo = 'Comida' and pe.codpedido = pp.codpedido and pe.codpedido = "+codPedido+" order by pp.codpedprato;";
        }

        montaTabela();
    }
}
