package ecommand.TelaVisualizarCardapio;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ecommand.*;
import ecommand.TelaGarcom.TelaAvisosController;
import ecommand.TelaPedido.TelaPedidoController;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TelaVisualizarCardapioController extends Logout implements Initializable {

    public static boolean ativado;
    public static Stage stage;
    private TelaPedidoAtualController pedidoAtual = new TelaPedidoAtualController();
    private TelaAvisosController telaAviso = new TelaAvisosController();
    private TabelaLista tabelaLista = new TabelaLista();
    private String sqlPrato = "select nome,codprato,descricao,preco,imagem from prato where cardapio = TRUE and tipo = 'Comida' order by codprato;"; //String sql
    private String sqlBebida = "select nome,codprato,descricao,preco,imagem from prato where cardapio = TRUE and tipo = 'Bebida' order by codprato;"; //String sql
    private String caminhoFoto;
    @FXML
    private ListView<GridPane> listaPratos, listaBebidas;
    @FXML
    private ImageView imgRestaurante;
    @FXML
    private Button botaoGarcom;

    private void insereImgCardapio() {
        imgRestaurante.setImage(new Image("file:///" + caminhoFoto));
    }

    private void selecionaImagem() {
        try {
            PreparedStatement ps2 = ConexaoBanco.getConnection().prepareStatement
                    ("SELECT ImagemCardapio FROM ImgCardapio WHERE CodImagem = 1;");
            ResultSet rs = ps2.executeQuery();
            if (rs.next()) {
                caminhoFoto = rs.getString("imagemcardapio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void acaoVoltar() throws IOException {
        Image icone = new Image("ecommand/img/icone.png");
        stage = new Stage();
        Parent root = Main.novaTela("TelaVisualizarCardapio/telaSairMesa.fxml");
        Scene scene = new Scene(root);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(Main.stage);
        stage.resizableProperty().setValue(false);
        stage.sizeToScene();
        stage.getIcons().add(icone);
        stage.setTitle("Deseja mesmo sair do cardápio?");
        stage.setScene(scene);
        stage.show();
    }


    public void acaoComanda() throws IOException {
        Main.trocaTela("TelaComanda/telaComanda.fxml");
    }

    @FXML
    private void acaoSelecaoPrato() {
        if (listaBebidas.getSelectionModel().getSelectedItem() != null) {
            listaBebidas.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void acaoSelecaoBebida() {
        if (listaPratos.getSelectionModel().getSelectedItem() != null)
            listaPratos.getSelectionModel().clearSelection();
    }

    public void acaoAddPedido() {
        String cod = null, nome = null, preco = null, desc = null, imagem = null;

        if (listaPratos.getSelectionModel().getSelectedItem() != null || listaBebidas.getSelectionModel().getSelectedItem() != null) {
            if (listaPratos.getSelectionModel().getSelectedItem() != null) {
                cod = (((Label) listaPratos.getSelectionModel().getSelectedItem().getChildren().get(0)).getText());
                nome = (((Label) listaPratos.getSelectionModel().getSelectedItem().getChildren().get(2)).getText());
                preco = (((Label) listaPratos.getSelectionModel().getSelectedItem().getChildren().get(4)).getText());
                desc = (((Label) listaPratos.getSelectionModel().getSelectedItem().getChildren().get(5)).getText());
            } else if (listaBebidas.getSelectionModel().getSelectedItem() != null) {
                cod = (((Label) listaBebidas.getSelectionModel().getSelectedItem().getChildren().get(0)).getText());
                nome = (((Label) listaBebidas.getSelectionModel().getSelectedItem().getChildren().get(2)).getText());
                preco = (((Label) listaBebidas.getSelectionModel().getSelectedItem().getChildren().get(4)).getText());
                desc = (((Label) listaBebidas.getSelectionModel().getSelectedItem().getChildren().get(5)).getText());
            }
            try {
                PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("select imagem from prato where codprato = ?;");
                ps.setInt(1, Integer.parseInt(cod));
                ResultSet rs = ps.executeQuery();

                if (rs.next())
                    imagem = rs.getString("imagem");

                pedidoAtual.pegaInformacoes(nome, preco, desc, Integer.parseInt(cod), imagem);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        acaoSelecaoBebida();
        acaoSelecaoPrato();
    }

    public void acaoProximo() throws IOException {
        TelaPedidoController.setListaPedido(pedidoAtual.getListaPedido());
        if(ativado){
            telaAviso.removeLista(TelaPedidoController.numeroMesa);
        }
        Main.trocaTela("TelaPedido/telaPedido.fxml");
    }

    public void acaoPedidoAtual() throws IOException {
        Image icone = new Image("ecommand/img/icone.png");
        Stage stage2 = new Stage();
        Parent root = Main.novaTela("TelaVisualizarCardapio/telaPedido.fxml");
        Scene scene = new Scene(root);
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.initOwner(Main.stage);
        stage2.resizableProperty().setValue(false);
        stage2.sizeToScene();
        stage2.getIcons().add(icone);
        stage2.setTitle("Pedido Atual");
        stage2.setScene(scene);
        stage2.show();
    }
    public void acaoGarcom () {
        if (!ativado){
            ativado = true;
            botaoGarcom.setStyle("-fx-background-color: #da1313; -fx-text-fill:#ffffff ;-fx-font-weight: bold");
            botaoGarcom.setText("Cancelar Chamado");
            telaAviso.insereLista(TelaPedidoController.numeroMesa,"Chamando garçom");
        }else{
            telaAviso.removeLista(TelaPedidoController.numeroMesa);
            ativado = false;
            botaoGarcom.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;-fx-font-weight: bold");
            botaoGarcom.setText("Chamar Garçom");
        }
    }

    public void acaoSelecionaItem(){
        if(listaPratos.getSelectionModel().getSelectedItem() != null){
            ((Button)listaPratos.getSelectionModel().getSelectedItem().getChildren().get(6)).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    acaoAddPedido();
                }
            });
        }else if(listaBebidas.getSelectionModel().getSelectedItem() != null){
            ((Button)listaBebidas.getSelectionModel().getSelectedItem().getChildren().get(6)).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    acaoAddPedido();
                }
            });
        }
    }

    public void initialize(URL url, ResourceBundle rb) {
        tabelaLista.listaPrato(listaPratos, sqlPrato);
        tabelaLista.listaPrato(listaBebidas, sqlBebida);
        ativado = false;
        selecionaImagem();
        insereImgCardapio();
        botaoGarcom.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;-fx-font-weight: bold");
        botaoGarcom.setText("Chamar Garçom");
    }
}
