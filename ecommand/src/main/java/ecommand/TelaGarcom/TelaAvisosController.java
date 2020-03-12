package ecommand.TelaGarcom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ecommand.Aviso;
import ecommand.ConexaoBanco;
import javax.swing.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TelaAvisosController implements Initializable {
    private ObservableList<Aviso> solicitacoes = FXCollections.observableArrayList();
    @FXML
    public TableView<Aviso> tabelaAvisos;
    @FXML
    public TableColumn colunaNumMesa,colunaAviso;

    private void preencheLista(){
        try {
            Statement stmt = ConexaoBanco.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from aviso;");

            while (rs.next()){
                solicitacoes.add(new Aviso(rs.getInt("id_mesa"),rs.getString("informacao")));
            }

            colunaNumMesa.setCellValueFactory(new PropertyValueFactory<>("numeroMesa"));
            colunaAviso.setCellValueFactory(new PropertyValueFactory<>("informacao"));
            tabelaAvisos.setItems(solicitacoes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Erro "+e);
        }
    }

    public void removeLista(int numeroMesa){
        try {
            PreparedStatement ps =  ConexaoBanco.getConnection().prepareStatement
                    ("delete from aviso where id_mesa = ?;");
            ps.setInt(1,numeroMesa);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insereLista(int numeroMesa, String informacao){
        try {
            PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement
                    ("insert into aviso(id_mesa, informacao)values(?,?);");
            ps.setInt(1,numeroMesa);
            ps.setString(2,informacao);
            ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Essa mesa já possui um chamado em espera!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preencheLista();
    }
}
