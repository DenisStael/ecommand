package ecommand.TelaAtendimento;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ecommand.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaAtendimentoController implements Initializable {

    private Stage stage = new Stage();
    private static Usuario usuario;
    private TabelaLista tabelaLista = new TabelaLista();
    private String sql, sql_2;
    @FXML
    private TableView<Pedido> tabelaPedido;
    @FXML
    private TableColumn<Pedido, String> colunaNumMesa,colunaObservacao;
    @FXML
    private TableColumn<Pedido, Integer> colunaCodPedido ,colunaStatus;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        TelaAtendimentoController.usuario = usuario;
    }

    public void acaoInformacao() throws IOException {
        if(tabelaPedido.getSelectionModel().getSelectedItem() != null){
            TelaInformacaoController.codPedido = tabelaPedido.getSelectionModel().getSelectedItem().getCodpedido();
            Parent root = FXMLLoader.load(getClass().getResource("telaInformacao.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Meus Atendimentos");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void acaoVoltar() throws IOException {
        if(usuario.getTipo().equals("Garçom"))
            Main.trocaTela("TelaGarcom/telaGarcom.fxml");
        else if(usuario.getTipo().equals("Cozinheiro"))
            Main.trocaTela("TelaCozinheiro/telaCozinheiro.fxml");
    }

    private void montaTabela(){
        tabelaLista.mostraPedidosAtendimento(tabelaPedido,colunaCodPedido,colunaNumMesa,colunaObservacao,colunaStatus,sql,sql_2);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(usuario.getTipo().equals("Garçom")) {
            sql = "select distinct pe.codpedido, c.id_mesa, pe.observacao from pedido pe, pedidoprato pp, comanda c " +
                    "where pe.garcom_usuario_codusuario = " + usuario.getCodusuario() + " and pp.codpedido = pe.codpedido " +
                    "and pe.codcomanda = c.codcomanda order by pe.codpedido;";
            sql_2 = "select pp.codcozinheiro, pp.codgarcom from pedidoprato pp, pedido pe where pe.codpedido = pp.codpedido" +
                    " and pe.garcom_usuario_codusuario = "+ usuario.getCodusuario() +"and pe.codpedido = ?;";
        }
        else if(usuario.getTipo().equals("Cozinheiro")) {
            sql = "select distinct pe.codpedido, c.id_mesa, pe.observacao from pedido pe, pedidoprato pp, comanda c " +
                    "where pe.cozinheiro_usuario_codusuario = " + usuario.getCodusuario() + " and pp.codpedido = pe.codpedido" +
                    " and pe.codcomanda = c.codcomanda order by pe.codpedido;";
            sql_2 = "select pp.codcozinheiro, pp.codgarcom from pedidoprato pp, pedido pe where pe.codpedido = pp.codpedido" +
                    " and pe.cozinheiro_usuario_codusuario = "+ usuario.getCodusuario() +"and pe.codpedido = ?;";
        }

        montaTabela();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                stage.close();//Fecha a aplicação
                montaTabela();
            }
        });
    }
}
