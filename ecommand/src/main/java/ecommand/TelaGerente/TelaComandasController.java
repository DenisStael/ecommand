package ecommand.TelaGerente;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ecommand.*;
import ecommand.TelaInfoPedido.TelaInfoPedidoController;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TelaComandasController implements Initializable {

    private String sql = "select c.codcomanda, pe.codpedido, c.id_mesa, pe.precototal from pedido pe, comanda c where " +
            "pe.codcomanda = c.codcomanda and c.statusComanda = 'Aberto';";
    private TabelaLista tabelaPedido= new TabelaLista();
    private boolean statusBotao;
    @FXML
    private TableView<Pedido> tabelaPedidos;
    @FXML
    private TableColumn colunaCodPedido, colunaNumeroMesa,colunaPrecoPedido, colunaComanda;
    @FXML
    private Label labelValorTotal;
    @FXML
    private TextField txtNumeroMesa;
    @FXML
    private Button botaoMostrarTodos, botaoMostrarFechados;

    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaGerente/telaGerente.fxml");
    }

    public void acaoPesquisar(){
        String sqlPesquisa;
        if(!txtNumeroMesa.getText().isEmpty()){
            if(statusBotao){
                sqlPesquisa = "select c.codcomanda, pe.codpedido, c.id_mesa, pe.precototal from pedido pe, comanda c " +
                        "where pe.codcomanda = c.codcomanda and c.id_mesa = "+txtNumeroMesa.getText()+" and c.statuscomanda = 'Aberto';";
            }else {
                sqlPesquisa = "select c.codcomanda, pe.codpedido, c.id_mesa, pe.precototal from pedido pe, comanda c " +
                        "where pe.codcomanda = c.codcomanda and c.id_mesa = "+txtNumeroMesa.getText()+";";
            }
            tabelaPedido.mostraTabelaPedidosComanda(tabelaPedidos,colunaCodPedido,colunaNumeroMesa,colunaPrecoPedido,colunaComanda,sqlPesquisa);
            labelValorTotal.setText(FormataPreco.formatarFloat(calculaPreco()));
        }
    }

    public void acaoInformacaoPedido() throws IOException {
        if(tabelaPedidos.getSelectionModel().getSelectedItem() != null){
            Stage stage = new Stage();
            TelaInfoPedidoController.codPedido = tabelaPedidos.getSelectionModel().getSelectedItem().getCodpedido();
            stage.setTitle("Informações do Pedido");
            Parent root = Main.novaTela("TelaInfoPedido/telaInfoPedido.fxml");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void acaoMostrarTodos(){
        statusBotao = false;
        botaoMostrarTodos.setStyle("-fx-background-color: #47d147");
        botaoMostrarFechados.setStyle("-fx-background-color: #d3d2d2");
        String sqlTodos = "select c.codcomanda, pe.codpedido, c.id_mesa, pe.precototal from pedido pe, comanda c where " +
                "pe.codcomanda = c.codcomanda;";
        tabelaPedido.mostraTabelaPedidosComanda(tabelaPedidos,colunaCodPedido,colunaNumeroMesa,colunaPrecoPedido,colunaComanda,sqlTodos);
        txtNumeroMesa.clear();
    }

    public void acaoMostrarAbertos(){
        statusBotao = true;
        botaoMostrarFechados.setStyle("-fx-background-color: #47d147");
        botaoMostrarTodos.setStyle("-fx-background-color: #d3d2d2");
        tabelaPedido.mostraTabelaPedidosComanda(tabelaPedidos,colunaCodPedido,colunaNumeroMesa,colunaPrecoPedido,colunaComanda,sql);
        txtNumeroMesa.clear();
    }

    public void acaoFecharComanda(){
        if(tabelaPedidos.getSelectionModel().getSelectedItem() != null){
            try {
                PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement
                        ("update comanda set statuscomanda = 'Fechado' where id_mesa = ? and statuscomanda = 'Aberto';");
                ps.setInt(1, tabelaPedidos.getSelectionModel().getSelectedItem().getIdmesa());
                ps.executeUpdate();
                tabelaPedido.mostraTabelaPedidosComanda(tabelaPedidos,colunaCodPedido,colunaNumeroMesa,colunaPrecoPedido,colunaComanda,sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private float calculaPreco(){
        Float precoTotal = 0f;
        for(int i = 0; i < tabelaPedidos.getItems().size(); i++){
            precoTotal += tabelaPedidos.getItems().get(i).getPreco();
        }
        return precoTotal;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabelaPedido.mostraTabelaPedidosComanda(tabelaPedidos,colunaCodPedido,colunaNumeroMesa,colunaPrecoPedido,colunaComanda,sql);
        statusBotao = true;
        botaoMostrarFechados.setStyle("-fx-background-color: #47d147");
    }
}
