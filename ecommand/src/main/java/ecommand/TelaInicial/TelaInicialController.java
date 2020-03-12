package ecommand.TelaInicial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ecommand.Main;
import ecommand.TelaLogin.TelaLoginController;
import javafx.scene.control.Button;

public class TelaInicialController{

    @FXML
    private Button botaoGerente, botaoGarcom, botaoCozinheiro;

    //Método de ação do botão Gerente, Garcom e Cozinheiro (Tela de Login)
    @FXML
    private void acaoBotao(ActionEvent event) throws Exception{

        Main.stage.setResizable(false);
        //Verifica se o evento foi acionado por uma instancia Button
        if(event.getSource() instanceof Button) {

            //Atribui a fonte com as informações do evento ao objeto 'botao'
            Button botao = (Button) event.getSource();

            //Verificação do ID de qual botão foi acionado para passar a informação do tipo de usuário à tela Login
            if(botao.equals(botaoGerente))
                TelaLoginController.setTipo("Gerente");
            else if(botao.equals(botaoGarcom))
                TelaLoginController.setTipo("Garçom");
            else if(botao.equals(botaoCozinheiro))
                TelaLoginController.setTipo("Cozinheiro");

            //Chamada da Scene telaLogin
            Main.trocaTela("TelaLogin/telaLogin.fxml");
        }
    }

    //Método de ação do botão Mesa
    public void acaoMesa() throws Exception{
        Main.trocaTela("TelaMesa/telaMesa.fxml");
    }


}
