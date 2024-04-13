package com.odonto.com.odonto.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.odonto.com.odonto.modelos.Factura;
import com.odonto.com.odonto.modelos.Usuario;
import com.odonto.com.odonto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import java.net.URL;
import com.itextpdf.text.pdf.PdfPTable;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class FacturasController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteController clienteController;
    @GetMapping("/facturas/pdf/{id}")
    public void generarFacturaPDF(@PathVariable("id") int facturaId, HttpServletResponse response, Model modelo) throws IOException {
        List<Factura> factura= usuarioService.findAllFactura(facturaId);
        Factura factura1 = factura.get(0);
        LocalTime horaActual = LocalTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaFormateada = horaActual.format(formato);
        Document document = new Document();


        try {
            // Configurar la respuesta HTTP para indicar que se generará un archivo PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"factura.pdf\"");

            // Crear el escritor PDF y el flujo de salida
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

            // Establecer las fuentes y los estilos
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font fontData = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Abrir el documento
            document.open();
        /*    URL logoUrl = new URL("https://i.postimg.cc/XYNw6qyK/logo.jpg");
            Image logo = Image.getInstance(logoUrl);
            logo.setAlignment(Element.ALIGN_RIGHT);
            logo.scaleToFit(200, 200); // Ajusta el tamaño del logo según tus necesidades
            // Agregar el título*/

            Image logo = Image.getInstance("classpath:templates/logo.jpg");
            logo.scaleToFit(200, 200);
            Paragraph title = new Paragraph("Grupo Odontologico Las Palmas", fontTitle);

            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 3});
            headerTable.setSpacingAfter(20);

            PdfPCell logoCell = new PdfPCell();
            logoCell.addElement(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(logoCell);

            PdfPCell titleCell = new PdfPCell(new Paragraph("         Grupo Odontologico Las Palmas", fontTitle));
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            titleCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(titleCell);

            document.add(headerTable);



            document.add(Chunk.NEWLINE);
            // Agregar los datos de la factura
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3});
            table.setSpacingAfter(10);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            addCell(table, "ID:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(factura1.getFactura_id()), fontData, BaseColor.WHITE);

            addCell(table, "Prospecto:", fontHeader, BaseColor.WHITE);
            addCell(table, factura1.getProspecto(), fontData, BaseColor.WHITE);

            addCell(table, "Documento:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(factura1.getId_cliente()), fontData, BaseColor.WHITE);

            addCell(table, "Nombre Cliente:", fontHeader, BaseColor.WHITE);
            addCell(table, factura1.getNombreCliente(), fontData, BaseColor.WHITE);

            addCell(table, "Odontologo(a):", fontHeader, BaseColor.WHITE);
            addCell(table, factura1.getNombreVendedor(), fontData, BaseColor.WHITE);

            addCell(table, "Hora:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(horaFormateada), fontData, BaseColor.WHITE);

            addCell(table, "Fecha:", fontHeader, BaseColor.WHITE);
            addCell(table, factura1.getFecha().toString(), fontData, BaseColor.WHITE);

            addCell(table, "Total:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(factura1.getTotal()), fontData, BaseColor.WHITE);

            addCell(table, "Saldo:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(factura1.getSaldo()), fontData, BaseColor.WHITE);

            document.add(table);
            document.add(new Paragraph("\n"));
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0));
            lineSeparator.setLineWidth(1);
            lineSeparator.setPercentage(100);
            document.add(lineSeparator);



            document.add(new Paragraph("Comprometidos con tu SALUD ORAL"));
            document.add(new Paragraph("CITAS -3053272575 -3182821808"));
            document.add(new Paragraph("carrera 39 # 19-20 nuevo Alvernia, Tulua 763021"));

            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Firma:"));



        } catch (DocumentException e) {

            e.printStackTrace();
        }
        document.close();

    }
    private void addCell(PdfPTable table, String text, Font font, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(5);
        table.addCell(cell);
    }
    @GetMapping("/clientes23")
    public String listarClientes2(Model modelo){

        modelo.addAttribute("clientes",usuarioService.findAllClientes());
        return "clientes";

    }

}
