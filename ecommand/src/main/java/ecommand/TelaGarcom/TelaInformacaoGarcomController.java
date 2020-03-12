package ecommand.TelaGarcom;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ecommand.ConexaoBanco;
import ecommand.Prato;
import ecommand.TabelaLista;
import javax.swing.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class TelaInformacaoGarcomController implements Initializable {
    private TabelaLista tabelaPrato = new TabelaLista();
    public static int codPedido;
    @FXML
    private Button botaoAtenderPedido;
    @FXML
    private TableView<Prato> tabelaPratos;
    @FXML
    private TableColumn colunaPrato, colunaDescPrato, colunaCodPrato, colunaPrecoPrato;

    public void acaoAtenderPedido(){
        try {
            PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("update pedido set garcom_usuario_codusuario = ? where codpedido = ?;");
            ps.setInt(1, TelaGarcomController.getUsuario().getCodusuario());
            ps.setInt(2,codPedido);
            ps.executeUpdate();
            botaoAtenderPedido.setDisable(true);
            botaoAtenderPedido.setText("Pedido Atendido");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Erro ao atender pedido!\n"+e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String sql = "select p.nome, p.codprato, p.descricao, p.preco, p.imagem, pe.codpedido "+
                "from prato p, pedido pe, pedidoprato pp "+
                "where p.codprato = pp.codprato and pe.codpedido = "+codPedido+" and pe.codpedido = pp.codpedido;";
        tabelaPrato.mostraTabelaPratos(tabelaPratos,colunaPrato,colunaDescPrato,colunaCodPrato,colunaPrecoPrato,sql);
    }
}
