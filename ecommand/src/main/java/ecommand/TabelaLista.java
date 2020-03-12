package ecommand;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TabelaLista {

    /*método que mostra a tabela com todos os pratos em estoque e
    recebe como parâmetros a tabela que será apresentada, suas colunas e a string sql*/
    public void mostraTabelaPratos(TableView tabelaPratos, TableColumn colunaPrato, TableColumn colunaDescricao, TableColumn colunaCod, TableColumn colunaPreco, String sql) {
        ObservableList<Prato> listaPratos = FXCollections.observableArrayList();
        try {
            listaPratos.clear();//limpa a lista
            Statement stmt = ConexaoBanco.getConnection().createStatement();//cria declaração sql
            ResultSet rs = stmt.executeQuery(sql); //executa a declaração e armazena o resultado

            //enquanto há resultados na consulta, registra os pratos na lista
            while (rs.next()) {
                listaPratos.add(new Prato(rs.getString("nome"), rs.getInt("codprato"),
                        rs.getString("descricao"), rs.getFloat("preco"), rs.getString("imagem")));
            }
            rs.close(); //fecha o resultset

            //atribui às colunas da tabela os valores
            colunaPrato.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colunaCod.setCellValueFactory(new PropertyValueFactory<>("codprato"));
            colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
            colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

            //insere os itens na tabela
            tabelaPratos.setItems(listaPratos);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao apresentar pratos!\n" + e);
        }
    }

    public void mostraPratosAtendimento(TableView tabelaPratos, TableColumn colunaPrato, TableColumn colunaDescricao, TableColumn colunaCod, TableColumn<Prato, Integer> colunaStatus, String sql) {
        ObservableList<Prato> listaPratos = FXCollections.observableArrayList();
        try {
            listaPratos.clear();//limpa a lista
            Statement stmt = ConexaoBanco.getConnection().createStatement();//cria declaração sql
            ResultSet rs = stmt.executeQuery(sql); //executa a declaração e armazena o resultado

            //enquanto há resultados na consulta, registra os pratos na lista
            while (rs.next()) {
                listaPratos.add(new Prato(rs.getString("nome"), rs.getInt("codprato"),
                        rs.getString("descricao"), rs.getFloat("preco"), rs.getString("imagem"),
                        rs.getInt("codcozinheiro"), rs.getInt("codgarcom")));
            }

            colunaStatus.setCellFactory(new Callback<TableColumn<Prato, Integer>, TableCell<Prato, Integer>>() {
                @Override
                public TableCell<Prato, Integer> call(TableColumn<Prato, Integer> param) {
                    return new TableCell<Prato, Integer>(){
                        @Override
                        protected void updateItem(Integer item, boolean empty){
                            if(!empty){
                                int index = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
                                int codcozinheiro = param.getTableView().getItems().get(index).getCodcozinheiro();
                                int codgarcom = param.getTableView().getItems().get(index).getCodgarcom();
                                if(codcozinheiro == 0 && codgarcom == 0){
                                    setTextFill(Color.WHITE);
                                    //setStyle("-fx-font-weight: bold");
                                    setStyle("-fx-background-color: #e05555");
                                    setText("Em preparo");
                                } else if(codcozinheiro !=0 && codgarcom != 0) {
                                    setTextFill(Color.WHITE);
                                    //setStyle("-fx-font-weight: bold");
                                    setStyle("-fx-background-color:  #00b33c");
                                    setText("Entregue");
                                } else if(codcozinheiro != 0 && codgarcom == 0){
                                    setTextFill(Color.BLACK);
                                    //setStyle("-fx-font-weight: bold");
                                    setStyle("-fx-background-color: #e7ee68");
                                    setText("Finalizado");
                                }
                            }
                        }
                    };
                }
            });

            //atribui às colunas da tabela os valores
            colunaPrato.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colunaCod.setCellValueFactory(new PropertyValueFactory<>("codprato"));
            colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

            //insere os itens na tabela
            tabelaPratos.setItems(listaPratos);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao apresentar pratos!\n" + e);
        }
    }

    public GridPane criaGridPane(String nome, String preco, String descricao, int codigo, String imagem, String nomeBotao) {
        GridPane gridPane = new GridPane();
        gridPane.getColumnConstraints().add(new ColumnConstraints(100));
        gridPane.getColumnConstraints().add(new ColumnConstraints(320));
        gridPane.getColumnConstraints().add(new ColumnConstraints(30));
        gridPane.getColumnConstraints().add(new ColumnConstraints(50));
        gridPane.getStylesheets().add(String.valueOf(getClass().getResource("css/TableStyle.css")));
        gridPane.getStyleClass().add("gridPane");
        ImageView imageView = new ImageView(new Image("file:///" + imagem));
        Label lblNome = new Label(nome);
        lblNome.getStyleClass().add("nome");
        Label lblPreco = new Label(preco);
        lblPreco.getStyleClass().add("preco");
        Label lblDescricao = new Label(descricao);
        Label lblCodigo = new Label(Integer.toString(codigo));
        Label lblCifrao = new Label("R$");
        Button botao = new Button(nomeBotao);
        botao.setPrefHeight(20);
        if(nomeBotao.equals("Adicionar")){
            botao.getStyleClass().add("botaoAdicionar");
        } else {
            botao.getStyleClass().add("botaoRemover");
        }
        botao.setCursor(Cursor.HAND);
        lblCifrao.getStyleClass().add("cifrao");
        imageView.setFitWidth(95);
        imageView.setFitHeight(90);
        lblDescricao.setPrefWidth(380);
        lblDescricao.setWrapText(true);
        lblDescricao.getStyleClass().add("descricao");
        gridPane.setMaxWidth(380);
        gridPane.setPadding(new Insets(2, 2, 2, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        lblCodigo.setVisible(false);
        gridPane.add(lblCodigo, 2, 2); // codigo: indice 0
        gridPane.add(imageView, 0, 0, 1, 3); //imagem: indice 1
        GridPane.setHalignment(lblNome, HPos.LEFT);
        gridPane.add(lblNome, 1, 0); //nome: indice 2
        gridPane.add(lblCifrao, 2, 0);//cifrao indice 3
        GridPane.setHalignment(lblPreco, HPos.LEFT);
        gridPane.add(lblPreco, 3, 0); //preco: indice 4
        gridPane.add(lblDescricao, 1, 2, 1, 1); //preco: indice 5
        gridPane.add(botao,2,2,2,2);
        return gridPane;
    }

    public void listaPrato(ListView listaPrato, String sql) {
        ObservableList<GridPane> listaPratos = FXCollections.observableArrayList();
        try {
            Statement stmt = ConexaoBanco.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                listaPratos.add(criaGridPane(rs.getString("nome"), Float.toString(rs.getFloat("preco")),
                        rs.getString("descricao"), rs.getInt("codprato"), rs.getString("imagem"), "Adicionar"));
            }
            rs.close();

            listaPrato.setItems(listaPratos);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao apresentar pratos!\n" + e);
        }
    }


    //--------------------TABELAS E LISTAS DE PEDIDOS------------------//

    public void mostraTabelaPedido(TableView tabelaPedido, TableColumn colunaCodPedido, TableColumn colunaIdMesa, TableColumn colunaPreco, String sql) {
        ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
        try {
            Statement stmt = ConexaoBanco.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                listaPedidos.add(new Pedido(rs.getInt("codpedido"), rs.getInt("id_mesa"), rs.getFloat("precototal")));
            }
            rs.close();

            colunaCodPedido.setCellValueFactory(new PropertyValueFactory<>("codpedido"));
            colunaIdMesa.setCellValueFactory(new PropertyValueFactory<>("idmesa"));
            colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

            tabelaPedido.setItems(listaPedidos);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao apresentar pedidos!\n" + e);
        }
    }

    public void mostraTabelaPedidos(TableView tabelaGarcom, TableColumn colunaCodPedido, TableColumn colunaIdMesa, TableColumn colunaObservacao, String sql) {
        ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
        try {
            Statement stmt = ConexaoBanco.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                listaPedidos.add(new Pedido(rs.getInt("codpedido"), rs.getInt("id_mesa"), rs.getString("observacao")));
            }
            rs.close();

            colunaCodPedido.setCellValueFactory(new PropertyValueFactory<>("codpedido"));
            colunaIdMesa.setCellValueFactory(new PropertyValueFactory<>("idmesa"));
            colunaObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));

            tabelaGarcom.setItems(listaPedidos);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao apresentar pedidos!\n" + e);
        }
    }

    public void mostraTabelaPedidosComanda(TableView tabelaGarcom, TableColumn colunaCodPedido, TableColumn colunaIdMesa, TableColumn colunaPreco, TableColumn codcomanda, String sql) {
        ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
        try {
            Statement stmt = ConexaoBanco.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                listaPedidos.add(new Pedido(rs.getInt("codcomanda"), rs.getInt("codpedido"),
                        rs.getInt("id_mesa") ,rs.getFloat("precototal")));
            }
            rs.close();

            colunaCodPedido.setCellValueFactory(new PropertyValueFactory<>("codpedido"));
            colunaIdMesa.setCellValueFactory(new PropertyValueFactory<>("idmesa"));
            colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
            codcomanda.setCellValueFactory(new PropertyValueFactory<>("status"));

            tabelaGarcom.setItems(listaPedidos);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao apresentar pedidos!\n" + e);
        }
    }

    public void mostraPedidosAtendimento(TableView<Pedido> tabelaGarcom, TableColumn<Pedido, Integer> colunaCodPedido, TableColumn<Pedido, String> colunaIdMesa, TableColumn<Pedido, String> colunaObservacao, TableColumn<Pedido, Integer> colunaStatus, String sql, String sql_2) {
        ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
        try {
            int status = 0;
            Statement stmt = ConexaoBanco.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement(sql_2);

            while (rs.next()) {
                ps.setInt(1,rs.getInt("codpedido"));
                ResultSet rs2 = ps.executeQuery();

                while (rs2.next()){
                    if(rs2.getInt("codcozinheiro") == 0){
                        status = -1;
                        break;
                    } else if(rs2.getInt("codcozinheiro") != 0){
                        status = 0;
                    }

                    if(status == 0){
                        if(rs2.getInt("codgarcom") == 0){
                            status = 0;
                            break;
                        } else if(rs2.getInt("codgarcom") != 0){
                            status = 1;
                        }
                    }
                }
                listaPedidos.add(new Pedido(rs.getInt("codpedido"), rs.getInt("id_mesa"),
                        rs.getString("observacao"), status));
            }

            colunaStatus.setCellFactory(new Callback<TableColumn<Pedido, Integer>, TableCell<Pedido, Integer>>() {
                @Override
                public TableCell<Pedido, Integer> call(TableColumn<Pedido, Integer> param) {
                    return new TableCell<Pedido, Integer>(){
                        @Override
                        protected void updateItem(Integer item, boolean empty){
                            if(!empty){
                                int index = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
                                int status = param.getTableView().getItems().get(index).getStatus();
                                if(status == 0){
                                    setTextFill(Color.BLACK);
                                    //setStyle("-fx-font-weight: bold");
                                    setStyle("-fx-background-color: #e7ee68");
                                    setText("Finalizado");
                                } else if(status == -1){
                                    setTextFill(Color.WHITE);
                                    //setStyle("-fx-font-weight: bold");
                                    setStyle("-fx-background-color: #e05555");
                                    setText("Em preparo");
                                } else if(status == 1){
                                    setTextFill(Color.WHITE);
                                    //setStyle("-fx-font-weight: bold");
                                    setStyle("-fx-background-color:  #00b33c");
                                    setText("Entregue");
                                }
                            }
                        }
                    };
                }
            });

            colunaCodPedido.setCellValueFactory(new PropertyValueFactory<>("codpedido"));
            colunaIdMesa.setCellValueFactory(new PropertyValueFactory<>("idmesa"));
            colunaObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
            colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            tabelaGarcom.setItems(listaPedidos);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao apresentar pedidos!\n" + e);
        }
    }
}
