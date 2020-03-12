package ecommand.TelaMesa;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ecommand.ConexaoBanco;
import ecommand.Main;
import ecommand.TelaPedido.TelaPedidoController;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TelaMesaController {

    @FXML
    private TextField txtNumeroMesa;
    @FXML
    private Label labelMesa;

    public void acaoVisualizarCardapio() throws IOException, SQLException {

        if(!txtNumeroMesa.getText().isEmpty()){
            TelaPedidoController.numeroMesa = Integer.parseInt(txtNumeroMesa.getText());
            PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("SELECT idmesa FROM mesa WHERE idmesa = ? ;");
            ps.setInt(1, Integer.parseInt(txtNumeroMesa.getText()));
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Main.trocaTela("TelaVisualizarCardapio/telaVisualizarCardapio.fxml");
            }else{
                labelMesa.setText("Esta mesa não existe");
            }
        }else{
            labelMesa.setText("Informe o número da mesa");
        }
    }

    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaInicial/telaInicial.fxml");
    }
    public void initialize(URL location, ResourceBundle resources) {
        txtNumeroMesa.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    try {
                        acaoVisualizarCardapio();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
