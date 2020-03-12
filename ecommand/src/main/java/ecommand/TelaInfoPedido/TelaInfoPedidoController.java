package ecommand.TelaInfoPedido;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ecommand.Prato;
import ecommand.TabelaLista;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaInfoPedidoController implements Initializable {
    private TabelaLista tabelaPrato = new TabelaLista();
    public static int codPedido;
    @FXML
    private TableView<Prato> tabelaPratos;
    @FXML
    private TableColumn colunaPrato, colunaDescPrato, colunaCodPrato, colunaPrecoPrato;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String sql = "select p.nome, p.codprato, p.descricao, p.preco, p.imagem, pe.codpedido "+
                "from prato p, pedido pe, pedidoprato pp "+
                "where p.codprato = pp.codprato and pe.codpedido = "+codPedido+" and pe.codpedido = pp.codpedido;";
        tabelaPrato.mostraTabelaPratos(tabelaPratos,colunaPrato,colunaDescPrato,colunaCodPrato,colunaPrecoPrato,sql);
    }
}
