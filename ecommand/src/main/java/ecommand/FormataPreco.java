package ecommand;

import java.text.DecimalFormat;

public class FormataPreco {

    public static String formatarFloat(float numero){
        String retorno = "";
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try{
            retorno = decimalFormat.format(numero);
        }catch(Exception ex){
            System.out.println("Erro ao formatar numero: " + ex);
        }
        return retorno;
    }
}
