package ecommand.TelaCadastro;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ecommand.ConexaoBanco;
import ecommand.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class TelaCadastroController implements Initializable {

    public static String tipo; //String que recebe da tela do Gerente o tipo de usuário que está sendo cadastrado

    //Atributos da Tela
    @FXML
    private TextField txtNome; //caixa de texto
    @FXML
    private PasswordField txtSenha, txtConfirmaSenha; //campo de senha
    @FXML
    private Label labelTipo;

    //Método de voltar para a tela anterior
    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaGerente/telaGerente.fxml"); //Chama a tela
    }

    //Método de enviar cadastro de usuário ao banco
    public void acaoEnvioCadastro() {

        //Checa se os campos estão preenchidos
        if(!txtNome.getText().isEmpty() && !txtSenha.getText().isEmpty() && !txtConfirmaSenha.getText().isEmpty()){

            //Checa se as senhas digitadas correspondem
            if(txtSenha.getText().equals(txtConfirmaSenha.getText())){
                try {
                    //Declaração SQL de inserção no banco
                    PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("insert into usuario(nome,senha,tipo)VALUES(?,?,?);");

                    //Insere os valores nos determinados parâmetros da declaração SQL
                    ps.setString(1, txtNome.getText());
                    ps.setString(2, txtSenha.getText());
                    ps.setString(3, tipo);

                    ps.executeUpdate(); //Executa a declaração SQL

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Usuário cadastrado com sucesso!");
                    alert.show();//Mensagem de Sucesso
                    if(tipo.equals("Gerente"))
                        Main.trocaTela("TelaInicial/telaInicial.fxml");
                } catch (Exception e) {
                    //Mensagem de erro
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Erro ao cadastrar usuário!\nErro: "+e);
                    alert.show();
                }
                //Limpa os campos de texto
                txtNome.clear(); txtSenha.clear(); txtConfirmaSenha.clear();

            } else {
                //Mensagem de erro na verificação das senhas
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("As senhas são diferentes!");
                alert.show();
            }
            //Mensagem de erro por haver campos vazios
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Preencha todos os campos!");
            alert.show();
        }
        }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelTipo.setText("Cadastrar "+tipo);
    }
}
