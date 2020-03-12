package ecommand.TelaCadastroMesa;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ecommand.ConexaoBanco;
import ecommand.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TelaCadastroMesaController implements Initializable {

    @FXML
    private TextField txtNumeroMesas;
    @FXML
    private Label labelNumeroMesas;

    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaGerente/telaGerente.fxml");
    }

    public void acaoConfirmar(){
        try {
            Statement stmt = ConexaoBanco.getConnection().createStatement();

            for(int i = 0; i < Integer.parseInt(txtNumeroMesas.getText()); i++)
                stmt.execute("insert into mesa(idmesa)values(default);");

            labelNumeroMesas.setText(Integer.toString(numeroMesas()));
            txtNumeroMesas.clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Mesas cadastradas!");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int numeroMesas(){
        int qtd = 0;
        try {
            Statement stmt = ConexaoBanco.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select count(idmesa) as qtd from mesa");
            if(rs.next())
                qtd = rs.getInt("qtd");
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return qtd;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelNumeroMesas.setText(Integer.toString(numeroMesas()));

        txtNumeroMesas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    acaoConfirmar();
                }
            }
        });
    }
}
