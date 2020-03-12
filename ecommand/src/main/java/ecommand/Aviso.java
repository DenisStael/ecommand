package ecommand;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Aviso {
   private SimpleIntegerProperty numeroMesa;
   private SimpleStringProperty informacao;

    public Aviso(int numeroMesa, String informacao) {
        this.numeroMesa = new SimpleIntegerProperty(numeroMesa);
        this.informacao = new SimpleStringProperty(informacao);
    }

    public int getNumeroMesa() {
        return numeroMesa.get();
    }

    public SimpleIntegerProperty numeroMesaProperty() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa.set(numeroMesa);
    }

    public String getInformacao() {
        return informacao.get();
    }

    public SimpleStringProperty informacaoProperty() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao.set(informacao);
    }

}
