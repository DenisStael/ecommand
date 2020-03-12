package ecommand.TelaVisualizarCardapio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import ecommand.FormataPreco;
import ecommand.TabelaLista;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaPedidoAtualController implements Initializable {

    private TabelaLista tabelaLista = new TabelaLista();
    private static ObservableList<GridPane> listaPedido = FXCollections.observableArrayList();
    @FXML
    private ListView<GridPane> listaPedidoAtual;
    @FXML
    private Label precoTotal;

    public static ObservableList<GridPane> getListaPedido() {
        return listaPedido;
    }

    public static void setListaPedido(ObservableList<GridPane> listaPedido) {
        TelaPedidoAtualController.listaPedido = listaPedido;
    }

    public void pegaInformacoes(String nome, String preco, String descricao, int cod, String imagem){
        GridPane gridPane = tabelaLista.criaGridPane(nome, preco, descricao, cod, imagem, "Remover");
        listaPedido.add(gridPane);
    }

    private float calculaPreco(){
        Float precoTotal = 0f;
        for(int i = 0; i < listaPedido.size(); i++){
            precoTotal += Float.parseFloat(((Label)listaPedido.get(i).getChildren().get(4)).getText());
        }
        return precoTotal;
    }

    private void removerPratoPedido(){
        if(listaPedidoAtual.getSelectionModel().getSelectedItem() != null){
            listaPedido.remove(listaPedidoAtual.getSelectionModel().getSelectedItem());
            precoTotal.setText(FormataPreco.formatarFloat(calculaPreco()));
            listaPedidoAtual.getSelectionModel().clearSelection();
        }
    }

    public void acaoSelecionaItem(){
        if(listaPedidoAtual.getSelectionModel().getSelectedItem() != null){
            ((Button)listaPedidoAtual.getSelectionModel().getSelectedItem().getChildren().get(6)).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    removerPratoPedido();
                }
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaPedidoAtual.setItems(listaPedido);
        precoTotal.setText(FormataPreco.formatarFloat(calculaPreco()));
    }
}
