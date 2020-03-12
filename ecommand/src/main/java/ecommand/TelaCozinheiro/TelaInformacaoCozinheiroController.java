package ecommand.TelaCozinheiro;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ecommand.ConexaoBanco;
import ecommand.Prato;
import ecommand.TabelaLista;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class TelaInformacaoCozinheiroController implements Initializable {
    private TabelaLista tabelaPrato = new TabelaLista();
    public static int codPedido;
    @FXML
    private Button botaoAtenderPedido;
    @FXML
    private TableView<Prato> tabelaPratos;
    @FXML
    private TableColumn colunaPrato, colunaDescPrato, colunaCodPrato, colunaPrecoPrato;

    public void acaoAtenderPedido(){
        /*Aqui tem que inserir o codigo do garçom no pedido para demonstrar que o pedido foi atendido
          e para o pedido sair da tabela de pedidos para serem atendidos*/

        try {
            PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("update pedido set cozinheiro_usuario_codusuario = ? where codpedido = ?;");
            ps.setInt(1, TelaCozinheiroController.getUsuario().getCodusuario());
            ps.setInt(2,codPedido);
            ps.executeUpdate();
            botaoAtenderPedido.setDisable(true);
            botaoAtenderPedido.setText("Pedido Atendido");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao atender pedido!\n"+e);
            alert.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String sql = "select p.nome, p.codprato, p.descricao, p.preco, p.imagem, pe.codpedido "+
                "from prato p, pedido pe, pedidoprato pp "+
                "where p.codprato = pp.codprato and p.tipo = 'Comida' and pe.codpedido = "+codPedido+" and pe.codpedido = pp.codpedido;";
        tabelaPrato.mostraTabelaPratos(tabelaPratos,colunaPrato,colunaDescPrato,colunaCodPrato,colunaPrecoPrato,sql);
    }
}
