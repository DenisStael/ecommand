package ecommand.TelaCozinheiro;

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
import ecommand.TelaAtendimento.TelaAtendimentoController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaCozinheiroController extends Logout implements Initializable {
    private static Usuario usuario;
    private TabelaLista pedido = new TabelaLista();
    private Stage stage = new Stage();
    private String sql = "select distinct p.codpedido,c.id_mesa,p.observacao from pedido p, prato pr, pedidoprato pp, comanda c " +
            "where p.cozinheiro_usuario_codusuario is null and pp.codpedido = p.codpedido and pp.codprato = pr.codprato " +
            "and pr.tipo = 'Comida' and p.codcomanda = c.codcomanda order by p.codpedido;";
    @FXML
    private TableView<Pedido> tabelaCozinheiro;
    @FXML
    private TableColumn colunaCodPedido,colunaNumMesa,colunaObservacao;

    public void acaoVoltar() throws IOException {
        acaoSair();
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        TelaCozinheiroController.usuario = usuario;
    }

    public void acaoInformacaoCozinheiro() throws IOException {
        if(tabelaCozinheiro.getSelectionModel().getSelectedItem() != null){
            TelaInformacaoCozinheiroController.codPedido = tabelaCozinheiro.getSelectionModel().getSelectedItem().getCodpedido();
            stage.setTitle("Informações do Pedido");
            Parent root = FXMLLoader.load(getClass().getResource("telaInformacaoCozinheiro.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void acaoAtendimento() throws IOException {
        TelaAtendimentoController.setUsuario(usuario);
        Main.trocaTela("TelaAtendimento/telaAtendimento.fxml");
    }

    public void acaoAtualizar() {
        pedido.mostraTabelaPedidos(tabelaCozinheiro,colunaCodPedido,colunaNumMesa,colunaObservacao,sql);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acaoAtualizar();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                stage.close();//Fecha a aplicação
                acaoAtualizar();
            }
        });
    }
}
