package ecommand.TelaComanda;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import org.controlsfx.control.Rating;
import ecommand.ConexaoBanco;
import ecommand.Main;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class TelaAvaliacao implements Initializable {

    private Rating ratingAtendimento = new Rating(), ratingAplicativo = new Rating(), ratingComida = new Rating();
    @FXML
    private Pane pane;
    @FXML
    private TextArea txtComentario;

    public void acaoSemAvaliar() throws IOException {
        Main.trocaTela("TelaComanda/telaPagamento.fxml");
    }

    public void acaoEnviarAvaliacao() {
        try {
            PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement
                    ("insert into avaliacao(notaatendimento,notacomida,notaaplicativo,comentario)values(?,?,?,?);");
            ps.setInt(1, (int) ratingAtendimento.getRating());
            ps.setInt(2, (int) ratingComida.getRating());
            ps.setInt(3, (int) ratingAplicativo.getRating());
            if (!txtComentario.getText().isEmpty()) {
                ps.setString(4, txtComentario.getText());
            } else {
                ps.setString(4, null);
            }
            ps.executeUpdate();
            Main.trocaTela("TelaComanda/telaPagamento.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaComanda/telaComanda.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ratingAtendimento.getStyleClass().add("rating");
        ratingAplicativo.getStyleClass().add("rating");
        ratingComida.getStyleClass().add("rating");
        ratingAtendimento.setLayoutX(260);
        ratingAtendimento.setLayoutY(113);
        ratingAplicativo.setLayoutX(260);
        ratingAplicativo.setLayoutY(175);
        ratingComida.setLayoutX(260);
        ratingComida.setLayoutY(238);
        pane.getChildren().addAll(ratingAtendimento, ratingAplicativo, ratingComida);
    }
}
