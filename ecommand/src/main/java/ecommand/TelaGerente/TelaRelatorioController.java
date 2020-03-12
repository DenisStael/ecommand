package ecommand.TelaGerente;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ecommand.ConexaoBanco;
import ecommand.Main;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TelaRelatorioController {
    @FXML
    private DatePicker txtDataInicio, txtDataFim;
    private ResultSet rs, rs2;
    private float valorTotal;

    public void acaoVoltar() throws IOException {
        Main.trocaTela("TelaGerente/telaGerente.fxml");
    }

    public void acaoGerarRelatorioEstoque() {
                Document doc = new Document();
                FileChooser f = new FileChooser();
                f.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                File file = f.showSaveDialog(new Stage());
                if (file != null) {
                    try {
                        PdfWriter.getInstance(doc, new FileOutputStream(file.getAbsolutePath()));
                        doc.open();
                        PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("select nome,descricao,quantidade,medida from produto order by nome");
                        rs = ps.executeQuery();
                        doc.add(new Paragraph("                     RELATÓRIO DO ESTOQUE ATUAL:                 "));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph(""));
                        PdfPTable tabelaEstoque = new PdfPTable(4);
                        PdfPCell cel1e = new PdfPCell(new Paragraph("Nome do produto"));
                        PdfPCell cel2e = new PdfPCell(new Paragraph("Descrição"));
                        PdfPCell cel3e = new PdfPCell(new Paragraph("Unidade de medida"));
                        PdfPCell cel4e = new PdfPCell(new Paragraph("Quantidade"));
                        tabelaEstoque.addCell(cel1e);
                        tabelaEstoque.addCell(cel2e);
                        tabelaEstoque.addCell(cel3e);
                        tabelaEstoque.addCell(cel4e);
                        while (rs.next()) {
                            //listaProdutos = rs.getString("nome");
                            cel1e = new PdfPCell(new Paragraph(rs.getString("nome")));
                            cel2e = new PdfPCell(new Paragraph(rs.getString("descricao")));
                            cel3e = new PdfPCell(new Paragraph(rs.getString("medida")));
                            cel4e = new PdfPCell(new Paragraph("" + rs.getInt("quantidade")));
                            tabelaEstoque.addCell(cel1e);
                            tabelaEstoque.addCell(cel2e);
                            tabelaEstoque.addCell(cel3e);
                            tabelaEstoque.addCell(cel4e);
                        }
                        doc.add(tabelaEstoque);
                        doc.close();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Relatório gerado com sucesso!");
                        alert.show();
                    } catch (DocumentException e) {
                        JOptionPane.showMessageDialog(null, e);
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(null, e);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Defina um lugar para salvar o Relarório!");
                    alert.show();
                }
    }

    public void acaoGerarRelatorioPedidos() {
        LocalDate data = txtDataInicio.getValue();
        LocalDate data2 = txtDataFim.getValue();

        if (data != null && data2 != null) {
            if ((data.isEqual(data2) || data.isBefore(data2))) {
                Document doc = new Document();
                FileChooser f = new FileChooser();
                f.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                File file = f.showSaveDialog(new Stage());
                if (file != null) {
                    try {
                        PdfWriter.getInstance(doc, new FileOutputStream(file.getAbsolutePath()));
                        doc.open();
                        PreparedStatement ps = ConexaoBanco.getConnection().prepareStatement("select p.nome,count(p.codprato) as quantidade\n" +
                                "from prato p, pedido pe, pedidoprato pp\n" +
                                "where p.codprato = pp.codprato and pe.codpedido = pp.codpedido and pe.data_pedido between ? and ?\n" +
                                "group by p.nome\n" +
                                "order by p.nome; ");
                        ps.setDate(1, Date.valueOf(data));
                        ps.setDate(2, Date.valueOf(data2));
                        rs = ps.executeQuery();
                        doc.add(new Paragraph("         RELATÓRIO DAS VENDAS DO RESTAURANTE:"));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph("         Relação de pratos pedidos neste período: "));
                        doc.add(new Paragraph("                                                "));
                        PdfPTable tabelaPedido = new PdfPTable(2);
                        PdfPCell cel1p = new PdfPCell(new Paragraph("Nome"));
                        PdfPCell cel2p = new PdfPCell(new Paragraph("Quantidade"));
                        tabelaPedido.addCell(cel1p);
                        tabelaPedido.addCell(cel2p);
                        while (rs.next()) {
                            cel1p = new PdfPCell(new Paragraph("" + rs.getString("nome")));
                            cel2p = new PdfPCell(new Paragraph("" + rs.getInt("quantidade")));
                            tabelaPedido.addCell(cel1p);
                            tabelaPedido.addCell(cel2p);
                        }
                        doc.add(tabelaPedido);
                        PreparedStatement ps2 = ConexaoBanco.getConnection().prepareStatement
                                ("select codpedido,precototal, data_pedido from pedido where data_pedido between ? and ? order by data_pedido,codpedido;");
                        ps2.setDate(1, Date.valueOf(data));
                        ps2.setDate(2, Date.valueOf(data2));
                        rs2 = ps2.executeQuery();

                        doc.add(new Paragraph("                 RELATÓRIO DE ENTRADA             "));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph("                                                "));
                        doc.add(new Paragraph("         Relação de Pedidos deste período: "));
                        doc.add(new Paragraph("                                                "));


                        PdfPTable tabelaVendas = new PdfPTable(3);
                        PdfPCell cel1v = new PdfPCell(new Paragraph("Código"));
                        PdfPCell cel2v = new PdfPCell(new Paragraph("Preço total"));
                        PdfPCell cel3v = new PdfPCell(new Paragraph("Data"));
                        tabelaVendas.addCell(cel1v);
                        tabelaVendas.addCell(cel2v);
                        tabelaVendas.addCell(cel3v);
                        while (rs2.next()) {
                            cel1v = new PdfPCell(new Paragraph("" + rs2.getInt("codpedido")));
                            cel2v = new PdfPCell(new Paragraph("" + rs2.getFloat("precototal")));
                            cel3v = new PdfPCell(new Paragraph("" + rs2.getDate("data_pedido")));
                            tabelaVendas.addCell(cel1v);
                            tabelaVendas.addCell(cel2v);
                            tabelaVendas.addCell(cel3v);
                            valorTotal = valorTotal + rs2.getFloat("precototal");
                        }
                        doc.add(tabelaVendas);
                        doc.add(new Paragraph("        Lucro bruto total: " + valorTotal));
                        doc.close();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Relatório gerado com sucesso!");
                        alert.show();
                    } catch (DocumentException e) {
                        JOptionPane.showMessageDialog(null, e);
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(null, e);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Defina um lugar para salvar o Relarório!");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("A data de inicio não pode ser depois da data de fim!");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("As datas não podem ficar vazias!");
            alert.show();
        }
    }

    public void acaoHoje() {
        System.out.println(LocalDate.now());
        txtDataInicio.setValue(LocalDate.now());
        txtDataFim.setValue(LocalDate.now());
    }
}
