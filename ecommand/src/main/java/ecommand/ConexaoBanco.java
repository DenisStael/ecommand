package ecommand;

import javax.swing.*;
import java.sql.*;

public class ConexaoBanco {
    private String url = "jdbc:postgresql://localhost:5432/ECommand";//Caminho da database PostgresSQL
    private String driver = "org.postgresql.Driver";//Driver do Postgres
    private String user = "ecommand";//Usuário configurado no Postgres
    private String pass = "oficina";//Senha de acesso ao Postgres
    private static Connection connection;//Variável de conexão do Banco

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        ConexaoBanco.connection = connection;
    }

    public void conectaBanco(){
        System.setProperty("jdbc.Drivers", driver);//Configura o driver
        try {
            connection = DriverManager.getConnection(url, user, pass);//Informa o caminho pra database, usuario e senha

            //Mensagem na tela informando se a conexão foi bem sucedida
            //JOptionPane.showMessageDialog(null, "Banco conectado com sucesso!");
        } catch (SQLException e) {
            //Mensagem informando que houve algum problema com a conexão
            JOptionPane.showMessageDialog(null, "Erro ao conectar com o Banco!\nErro: "+e.getMessage());

            //Fecha a aplicação caso não haja conexão
            Main.stage.close();
        }
    }

    public void desconectaBanco(){
        try {
            connection.close();//Termina a coxexão com o banco
            //JOptionPane.showMessageDialog(null, "Banco desconectado!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao desconectar do Banco!\n Erro: "+e.getMessage());
        }

    }

}
