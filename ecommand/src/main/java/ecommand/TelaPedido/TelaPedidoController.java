package ecommand.TelaPedido;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ecommand.ConexaoBanco;
import ecommand.FormataPreco;
import ecommand.Main;
import ecommand.TelaComanda.TelaComandaController;
import ecommand.TelaVisualizarCardapio.TelaPedidoAtualController;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TelaPedidoController implements Initializable {

    public static int numeroMesa;
    public static Integer numeroComanda;
    private static ObservableList<GridPane> listaPedido = FXCollections.observableArrayList();
    @FXML
    private TextArea txtObservacao;
    @FXML
    private ListView<GridPane> listaPratos;
    @FXML
    private Label labelPrecoTotal, labelNumeroMesa;
    @FXML
    private Button btConfirmarPedido;


    public static ObservableList<GridPane> getListaPedido() {
        return listaPedido;
    }

    public static void setListaPedido(ObservableList<GridPane> listaPedido) {
        TelaPedidoController.listaPedido = listaPedido;
    }

    public void acaoVoltar() throws IOException {
        TelaPedidoAtualController.setListaPedido(listaPedido);
        Main.trocaTela("TelaVisualizarCardapio/telaVisualizarCardapio.fxml");
    }

    public void acaoConfirmar(){
        try {
            if(numeroComanda == null){
                System.out.println(numeroComanda);
                PreparedStatement ps2 = ConexaoBanco.getConnection().prepareStatement
                        ("insert into comanda(statuscomanda,id_mesa) values('Aberto',?);");
                ps2.setInt(1,numeroMesa);
                ps2.executeUpdate();

                Statement stmt_2 = ConexaoBanco.getConnection().createStatement();
                ResultSet rs2 = stmt_2.executeQuery("select max(codcomanda) as codmax from comanda;");

                if(rs2.next()){
                    numeroComanda = rs2.getInt("codmax");
                }
            }

            PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement
                    ("insert into pedido(codcomanda,observacao,precototal,statuspedido)values(?,?,?,'Aberto');");
            ps.setInt(1,numeroComanda);
            ps.setFloat(3,calculaPreco());

            if(txtObservacao.getText().isEmpty())
                ps.setString(2,null);
            else
                ps.setString(2,txtObservacao.getText());

            ps.executeUpdate();

            Statement stmt = ConexaoBanco.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select max(codpedido) as qtd from pedido;");
            if(rs.next()){
                String sql = "";

                PreparedStatement ps3 = ConexaoBanco.getConnection().prepareStatement
                        ("update prato set quantidade = quantidade + 1 where codprato = ?;");
                for(int i = 0; i < listaPedido.size(); i++){
                    PreparedStatement ps4 = ConexaoBanco.getConnection().prepareStatement("select tipo from prato where codprato = ?;");
                    ps4.setInt(1, Integer.parseInt(((Label)listaPedido.get(i).getChildren().get(0)).getText()));

                    ResultSet rs2 = ps4.executeQuery();

                    if(rs2.next()){
                        if(rs2.getString("tipo").equals("Bebida")){
                            sql = "insert into pedidoprato(codprato,codpedido,codcozinheiro)values(?,?,1);";
                        } else {
                            sql = "insert into pedidoprato(codprato,codpedido,codcozinheiro)values(?,?,null);";
                        }
                    }
                    PreparedStatement ps2 = ConexaoBanco.getConnection().prepareStatement(sql);
                    ps2.setInt(1, Integer.parseInt(((Label)listaPedido.get(i).getChildren().get(0)).getText()));
                    ps2.setInt(2,rs.getInt("qtd"));
                    ps2.executeUpdate();
                    ps3.setInt(1, Integer.parseInt(((Label)listaPedido.get(i).getChildren().get(0)).getText()));
                    ps3.executeUpdate();
                }
            }
            rs.close();

            listaPratos.getItems().clear();
            TelaComandaController.numeroComanda = numeroComanda;
            Main.trocaTela("TelaComanda/telaComanda.fxml");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Erro ao realizar pedido!\n"+e);
        }
    }

    private float calculaPreco(){
        Float precoTotal = 0f;
        for(int i = 0; i < listaPedido.size(); i++){
            precoTotal += Float.parseFloat(((Label) listaPedido.get(i).getChildren().get(4)).getText());
        }
        return precoTotal;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaPratos.setItems(listaPedido);
        labelPrecoTotal.setText(FormataPreco.formatarFloat(calculaPreco()));
        labelNumeroMesa.setText(Integer.toString(numeroMesa));
        if(!listaPratos.getItems().isEmpty())
            btConfirmarPedido.setDisable(false);
        else
            btConfirmarPedido.setDisable(true);
    }
}
