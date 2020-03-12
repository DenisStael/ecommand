package ecommand.TelaComanda;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import ecommand.ConexaoBanco;
import ecommand.Main;
import ecommand.TelaGarcom.TelaAvisosController;
import ecommand.TelaPedido.TelaPedidoController;
import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TelaPagamentoController {

    private TelaAvisosController telaAviso = new TelaAvisosController();
    public static String precoTotal;
    @FXML
    private Label labelPagamento,labelFinalizado,labelInfo,labelValorTotal,labelPrecoTotal,labelCartao,labelDinheiro;
    @FXML
    private Button botaoSair;
    @FXML
    private ImageView imgCartao,imgDinheiro;

    public void acaoPagarCartao() {
        telaAviso.insereLista(TelaPedidoController.numeroMesa,"Pagamento em cartão");
        try {
            economizaCodigo();
            fecharPedido();
            labelPagamento.setText("Favor aguarde na mesa enquanto o garçom traz a máquina de cartão!");
            labelValorTotal.setText("Valor total: R$");
            labelPrecoTotal.setText(precoTotal);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
        }
    }

    private void fecharPedido(){
        try {
            PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement
                    ("UPDATE comanda SET statuscomanda = 'Fechado' WHERE id_mesa = ? AND statuscomanda = 'Aberto';");
            ps.setInt(1, TelaPedidoController.numeroMesa);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void acaoPagarDinheiro() {
        try {
            economizaCodigo();
            labelPagamento.setText("Por favor dirija-se ao caixa para realizar o pagamento!");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }

    public void economizaCodigo(){
        try {
            PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement
                    ("UPDATE pedido SET statuspedido = 'Fechado' WHERE codcomanda = ? AND statuspedido = 'Aberto';");
            ps.setInt(1,TelaPedidoController.numeroComanda);
            ps.executeUpdate();
            TelaPedidoController.numeroComanda = null;
            TelaComandaController.numeroComanda = 0;
            imgCartao.setDisable(true);
            imgCartao.setVisible(false);
            imgDinheiro.setDisable(true);
            imgDinheiro.setVisible(false);
            botaoSair.setVisible(true);
            botaoSair.setDisable(false);
            labelInfo.setVisible(false);
            labelCartao.setDisable(true);
            labelDinheiro.setVisible(false);
            labelDinheiro.setDisable(true);
            labelCartao.setVisible(false);
            labelFinalizado.setText("FINALIZADO");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    public void acaoSair() throws IOException {
        Main.trocaTela("TelaVisualizarCardapio/telaVisualizarCardapio.fxml");
        telaAviso.removeLista(TelaPedidoController.numeroMesa);
    }
}
