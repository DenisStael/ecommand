package ecommand.TelaGerente;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.Rating;
import ecommand.Avaliacao;
import ecommand.ConexaoBanco;
import ecommand.Main;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TelaVisualizarAvaliacaoController implements Initializable{

    private ObservableList<Avaliacao> listaAvaliacao = FXCollections.observableArrayList();
    private Rating ratingMedia = new Rating(), ratingComida = new Rating(), ratingAtendimento = new Rating()
            , ratingAplicativo = new Rating();
    @FXML
    private Label labelMedia, labelAtendimento, labelComida, labelAplicativo;
    @FXML
    private ListView<GridPane> listaComentarios;
    @FXML
    private AnchorPane paneMedia, paneAvaliacoes;

    private void buscaAvaliacoes(){
        listaComentarios.getItems().clear();
        try {
            Statement stmt = ConexaoBanco.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from avaliacao;");

            while(rs.next()){
                listaAvaliacao.add(new Avaliacao(rs.getInt("codavaliacao"),rs.getInt("notacomida"),
                        rs.getInt("notaatendimento"),rs.getInt("notaaplicativo"),rs.getString("comentario")));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void criaGridPane(ObservableList<Avaliacao> lista){
        ObservableList<GridPane> listaGridpane = FXCollections.observableArrayList();
        for(int i = 0; i < lista.size(); i++) {
            if(lista.get(i).getComentario() != null){
                GridPane gridPane = new GridPane();
                gridPane.getColumnConstraints().add(new ColumnConstraints(75));
                gridPane.getColumnConstraints().add(new ColumnConstraints(155));
                gridPane.getColumnConstraints().add(new ColumnConstraints(60));
                gridPane.getColumnConstraints().add(new ColumnConstraints(145));
                gridPane.getColumnConstraints().add(new ColumnConstraints(95));
                gridPane.getColumnConstraints().add(new ColumnConstraints(140));
                gridPane.getStyleClass().add("gridPane");
                //gridPane.setGridLinesVisible(true);
                Rating notaAtendimento = new Rating();
                notaAtendimento.setRating(lista.get(i).getNota_atendimento());
                notaAtendimento.setDisable(true);
                Rating notaComida = new Rating();
                notaComida.setRating(lista.get(i).getNota_comida());
                notaComida.setDisable(true);
                Rating notaAplicativo = new Rating();
                notaAplicativo.setRating(lista.get(i).getNota_aplicativo());
                notaAplicativo.setDisable(true);
                Label comentario = new Label();
                comentario.setText(lista.get(i).getComentario());
                comentario.getStyleClass().add("text");
                comentario.setMaxWidth(700);
                comentario.setWrapText(true);
                Label labelAtend = new Label("Atendimento:");
                labelAtend.getStyleClass().add("title");
                Label labelCom = new Label("Comida:");
                labelCom.getStyleClass().add("title");
                Label labelAplic = new Label("Aplicativo:");
                labelAplic.getStyleClass().add("title");
                gridPane.add(comentario, 0,1,6,1);
                gridPane.add(labelAplic,0,0);
                gridPane.add(notaAplicativo,1,0);
                gridPane.add(labelCom,2,0);
                gridPane.add(notaComida,3,0);
                gridPane.add(labelAtend,4,0);
                gridPane.add(notaAtendimento,5,0);
                listaGridpane.add(gridPane);
            }
        }
        listaComentarios.setItems(listaGridpane);
    }

    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaGerente/telaGerente.fxml");
    }

    private float calculaMedia(){
        float media, mediaTotal = 0f, soma;

        for(int i = 0; i < listaAvaliacao.size(); i++){
            soma = 0;
            media = 0;
            soma += listaAvaliacao.get(i).getNota_aplicativo()+listaAvaliacao.get(i).getNota_atendimento()+listaAvaliacao.get(i).getNota_comida();
            media = soma / 3;
            mediaTotal += media;
        }

        if(mediaTotal != 0){
            return mediaTotal / listaAvaliacao.size();
        } else
            return mediaTotal;
    }

    private float calculaMediaAtendimento(){
        float soma = 0f;

        for(int i = 0; i < listaAvaliacao.size(); i++){
            soma += listaAvaliacao.get(i).getNota_atendimento();
        }

        if(soma != 0){
            return soma / listaAvaliacao.size();
        } else
            return soma;
    }

    private float calculaMediaAplicativo(){
        float soma = 0f;

        for(int i = 0; i < listaAvaliacao.size(); i++){
            soma += listaAvaliacao.get(i).getNota_aplicativo();
        }

        if(soma != 0){
            return soma / listaAvaliacao.size();
        } else
            return soma;
    }

    private float calculaMediaComida(){
        float soma = 0f;

        for(int i = 0; i < listaAvaliacao.size(); i++){
            soma += listaAvaliacao.get(i).getNota_comida();
        }

        if(soma != 0){
            return soma / listaAvaliacao.size();
        } else
            return soma;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buscaAvaliacoes();
        ratingMedia.getStylesheets().add("ecommand/css/telaAvaliacao.css");
        ratingMedia.getStyleClass().add("rating");
        ratingMedia.setLayoutX(380);
        ratingMedia.setLayoutY(20);
        ratingMedia.setPartialRating(true);
        ratingMedia.setDisable(true);
        paneMedia.getChildren().add(ratingMedia);

        ratingAtendimento.getStylesheets().add("ecommand/css/telaAvaliacao.css");
        ratingAtendimento.getStyleClass().add("rating");
        ratingAtendimento.setLayoutX(350);
        ratingAtendimento.setLayoutY(12);
        ratingAtendimento.setPartialRating(true);
        ratingAtendimento.setDisable(true);

        ratingComida.getStylesheets().add("ecommand/css/telaAvaliacao.css");
        ratingComida.getStyleClass().add("rating");
        ratingComida.setLayoutX(350);
        ratingComida.setLayoutY(126);
        ratingComida.setPartialRating(true);
        ratingComida.setDisable(true);

        ratingAplicativo.getStylesheets().add("ecommand/css/telaAvaliacao.css");
        ratingAplicativo.getStyleClass().add("rating");
        ratingAplicativo.setLayoutX(350);
        ratingAplicativo.setLayoutY(240);
        ratingAplicativo.setPartialRating(true);
        ratingAplicativo.setDisable(true);

        paneAvaliacoes.getChildren().addAll(ratingAplicativo,ratingAtendimento,ratingComida);

        float media = calculaMedia();
        ratingMedia.setRating(media);
        labelMedia.setText(String.format("%.1f",media));

        float media_atendimento = calculaMediaAtendimento();
        ratingAtendimento.setRating(media_atendimento);
        labelAtendimento.setText(String.format("%.1f",media_atendimento));

        float media_aplicativo = calculaMediaAplicativo();
        ratingAplicativo.setRating(media_aplicativo);
        labelAplicativo.setText(String.format("%.1f",media_aplicativo));

        float media_comida = calculaMediaComida();
        ratingComida.setRating(media_comida);
        labelComida.setText(String.format("%.1f",media_comida));

        criaGridPane(listaAvaliacao);

        listaComentarios.setFocusTraversable(false);
    }
}
