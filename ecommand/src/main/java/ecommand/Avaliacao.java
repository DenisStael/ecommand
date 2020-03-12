package ecommand;

public class Avaliacao {
    private int cod_avaliacao;
    private int nota_comida;
    private int nota_atendimento;
    private int nota_aplicativo;
    private String comentario;

    public Avaliacao(int cod_avaliacao, int nota_comida, int nota_atendimento, int nota_aplicativo, String comentario) {
        this.cod_avaliacao = cod_avaliacao;
        this.nota_comida = nota_comida;
        this.nota_atendimento = nota_atendimento;
        this.nota_aplicativo = nota_aplicativo;
        this.comentario = comentario;
    }

    public int getCod_avaliacao() {
        return cod_avaliacao;
    }

    public void setCod_avaliacao(int cod_avaliacao) {
        this.cod_avaliacao = cod_avaliacao;
    }

    public int getNota_comida() {
        return nota_comida;
    }

    public void setNota_comida(int nota_comida) {
        this.nota_comida = nota_comida;
    }

    public int getNota_atendimento() {
        return nota_atendimento;
    }

    public void setNota_atendimento(int nota_atendimento) {
        this.nota_atendimento = nota_atendimento;
    }

    public int getNota_aplicativo() {
        return nota_aplicativo;
    }

    public void setNota_aplicativo(int nota_aplicativo) {
        this.nota_aplicativo = nota_aplicativo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
