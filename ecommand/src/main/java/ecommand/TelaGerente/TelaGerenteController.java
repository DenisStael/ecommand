package ecommand.TelaGerente;

import ecommand.Logout;
import ecommand.Main;
import ecommand.TelaCadastro.TelaCadastroController;
import ecommand.Usuario;
import java.io.IOException;

public class TelaGerenteController extends Logout {

    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        TelaGerenteController.usuario = usuario;
    }

    //Métodos de trocar de telas e suas respectivas telas

    public void acaoVoltar() throws IOException {
        acaoSair();
    }

    public void acaoCadastrarCozinheiro() throws IOException {
        TelaCadastroController.tipo = "Cozinheiro"; //Atribui ao atributo estático da tela de cadastro o tipo cozinheiro
        Main.trocaTela("TelaCadastro/telaCadastro.fxml");
    }

    public void acaoCadastrarGarcom() throws IOException {
        TelaCadastroController.tipo = "Garçom"; //Atribui ao atributo estático da tela de cadastro o tipo garçom
        Main.trocaTela("TelaCadastro/telaCadastro.fxml");
    }

    public void acaoCadastrarProduto() throws IOException {
        Main.trocaTela("TelaCadastrarProduto/telaCadastrarProduto.fxml");
    }

    public void acaoMontarCardapio() throws IOException {
        Main.trocaTela("TelaMontarCardapio/telaMontarCardapio.fxml");
    }

    public void acaoMontarPrato() throws IOException {
        Main.trocaTela("TelaMontarPrato/telaMontarPrato.fxml");
    }

    public void acaoEditarPrato() throws IOException {
        Main.trocaTela("TelaEditarPrato/telaEditarPrato.fxml");
    }

     public void acaoGerenciarEstoque() throws IOException {
        Main.trocaTela("TelaGerenciarEstoque/telaGerenciarEstoque.fxml");
     }

     public void acaoCadastrarMesas() throws IOException {
        Main.trocaTela("TelaCadastroMesa/telaCadastroMesa.fxml");
     }

    public void acaoVerComandas() throws IOException {
        Main.trocaTela("TelaGerente/telaComandas.fxml");
    }

    public void acaoEmitirRelatorio() throws IOException {
        Main.trocaTela("TelaGerente/telaRelatorioPrincipal.fxml");
    }

    public void acaoVerAvaliacoes() throws IOException {
        Main.trocaTela("TelaGerente/telaVisualizarAvaliacao.fxml");
    }
}
