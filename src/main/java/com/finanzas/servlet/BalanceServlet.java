package com.finanzas.servlet;

import com.finanzas.dao.EntradaDAO;
import com.finanzas.dao.SalidaDAO;
import com.finanzas.modelo.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@WebServlet("/balance")
public class BalanceServlet extends HttpServlet {

    private final EntradaDAO entradaDAO = new EntradaDAO();
    private final SalidaDAO salidaDAO = new SalidaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!verificarSesion(req, resp)) return;
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");

        String exportar = req.getParameter("exportar");
        List<Entrada> entradas = entradaDAO.listarPorUsuario(usuario.getId());
        List<Salida> salidas = salidaDAO.listarPorUsuario(usuario.getId());
        ReporteBalance reporte = new ReporteBalance(entradas, salidas);

        if ("pdf".equals(exportar)) {
            exportarPDF(resp, reporte);
            return;
        }

        req.setAttribute("reporte", reporte);
        req.getRequestDispatcher("/views/balance.jsp").forward(req, resp);
    }

    private void exportarPDF(HttpServletResponse resp, ReporteBalance reporte) throws IOException {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=reporte-balance.pdf");

        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("es", "SV"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            Document doc = new Document(PageSize.A4, 40, 40, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(doc, resp.getOutputStream());
            doc.open();

            // Fuentes
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD,
                new BaseColor(33, 93, 55));
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL,
                new BaseColor(50, 50, 50));
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD,
                new BaseColor(33, 93, 55));
            Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,
                new BaseColor(33, 93, 55));

            // Titulo
            String hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph titulo = new Paragraph("Reporte de Balance Financiero", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(5);
            doc.add(titulo);

            Paragraph fecha = new Paragraph("Generado el: " + hoy, normalFont);
            fecha.setAlignment(Element.ALIGN_CENTER);
            fecha.setSpacingAfter(20);
            doc.add(fecha);

            // -- Tabla de Entradas --
            Paragraph tEntradas = new Paragraph("Entradas", subTitleFont);
            tEntradas.setSpacingAfter(6);
            doc.add(tEntradas);

            PdfPTable tablaEntradas = new PdfPTable(3);
            tablaEntradas.setWidthPercentage(100);
            tablaEntradas.setWidths(new float[]{1, 3, 2});

            addCell(tablaEntradas, "N", headerFont, new BaseColor(33, 93, 55), Element.ALIGN_CENTER);
            addCell(tablaEntradas, "Tipo de Entrada", headerFont, new BaseColor(33, 93, 55), Element.ALIGN_LEFT);
            addCell(tablaEntradas, "Monto", headerFont, new BaseColor(33, 93, 55), Element.ALIGN_RIGHT);

            int i = 1;
            for (Entrada e : reporte.getEntradas()) {
                BaseColor bg = (i % 2 == 0) ? new BaseColor(240, 248, 243) : BaseColor.WHITE;
                addCell(tablaEntradas, String.valueOf(i), normalFont, bg, Element.ALIGN_CENTER);
                addCell(tablaEntradas, e.getTipoEntrada(), normalFont, bg, Element.ALIGN_LEFT);
                addCell(tablaEntradas, fmt.format(e.getMonto()), normalFont, bg, Element.ALIGN_RIGHT);
                i++;
            }
            addCell(tablaEntradas, "", boldFont, new BaseColor(200, 230, 210), Element.ALIGN_CENTER);
            addCell(tablaEntradas, "TOTAL", boldFont, new BaseColor(200, 230, 210), Element.ALIGN_LEFT);
            addCell(tablaEntradas, fmt.format(reporte.getTotalEntradas()), boldFont,
                new BaseColor(200, 230, 210), Element.ALIGN_RIGHT);

            tablaEntradas.setSpacingAfter(16);
            doc.add(tablaEntradas);

            // -- Tabla de Salidas --
            Paragraph tSalidas = new Paragraph("Salidas", subTitleFont);
            tSalidas.setSpacingAfter(6);
            doc.add(tSalidas);

            PdfPTable tablaSalidas = new PdfPTable(3);
            tablaSalidas.setWidthPercentage(100);
            tablaSalidas.setWidths(new float[]{1, 3, 2});

            addCell(tablaSalidas, "N", headerFont, new BaseColor(180, 60, 60), Element.ALIGN_CENTER);
            addCell(tablaSalidas, "Tipo de Salida", headerFont, new BaseColor(180, 60, 60), Element.ALIGN_LEFT);
            addCell(tablaSalidas, "Monto", headerFont, new BaseColor(180, 60, 60), Element.ALIGN_RIGHT);

            int j = 1;
            for (Salida s : reporte.getSalidas()) {
                BaseColor bg = (j % 2 == 0) ? new BaseColor(252, 243, 243) : BaseColor.WHITE;
                addCell(tablaSalidas, String.valueOf(j), normalFont, bg, Element.ALIGN_CENTER);
                addCell(tablaSalidas, s.getTipoSalida(), normalFont, bg, Element.ALIGN_LEFT);
                addCell(tablaSalidas, fmt.format(s.getMonto()), normalFont, bg, Element.ALIGN_RIGHT);
                j++;
            }
            addCell(tablaSalidas, "", boldFont, new BaseColor(240, 200, 200), Element.ALIGN_CENTER);
            addCell(tablaSalidas, "TOTAL", boldFont, new BaseColor(240, 200, 200), Element.ALIGN_LEFT);
            addCell(tablaSalidas, fmt.format(reporte.getTotalSalidas()), boldFont,
                new BaseColor(240, 200, 200), Element.ALIGN_RIGHT);
            tablaSalidas.setSpacingAfter(16);
            doc.add(tablaSalidas);

            // Balance
            PdfPTable tablaBalance = new PdfPTable(2);
            tablaBalance.setWidthPercentage(60);
            tablaBalance.setHorizontalAlignment(Element.ALIGN_LEFT);
            Font balanceFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,
                new BaseColor(33, 93, 55));
            addCell(tablaBalance, "Balance Resultante", balanceFont,
                new BaseColor(33, 93, 55), Element.ALIGN_LEFT);

            BaseColor balanceBg = reporte.getBalance().compareTo(BigDecimal.ZERO) >= 0
                ? new BaseColor(200, 240, 210) : new BaseColor(255, 220, 220);
            Font balanceValFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,
                reporte.getBalance().compareTo(BigDecimal.ZERO) >= 0
                    ? new BaseColor(20, 100, 50) : new BaseColor(160, 30, 30));
            addCell(tablaBalance, fmt.format(reporte.getBalance()), balanceValFont,
                balanceBg, Element.ALIGN_RIGHT);
            tablaBalance.setSpacingAfter(20);
            doc.add(tablaBalance);

            // Grafico de pastel (usando iText Chart simulation con rectangulos)
            doc.add(new Paragraph("Grafico de Balance: Entradas vs Salidas", subTitleFont));

            // Dibujar grafico simple con PdfContentByte
            PdfContentByte cb = writer.getDirectContent();
            float cx = 150, cy = doc.top() - 550, r = 60;
            double startAngle = 0;

            // Entradas (verde)
            double entAngle = 3.6 * reporte.getPorcentajeEntradas();
            cb.setColorFill(new BaseColor(33, 130, 70));
            drawPieSlice(cb, cx, cy, r, (float) startAngle, (float) (startAngle + entAngle));
            startAngle += entAngle;

            // Salidas (rojo)
            cb.setColorFill(new BaseColor(200, 60, 60));
            drawPieSlice(cb, cx, cy, r, (float) startAngle, (float) (startAngle + (360 - entAngle)));

            // Leyenda
            cb.setColorFill(new BaseColor(33, 130, 70));
            cb.rectangle(cx + r + 20, cy + 10, 12, 12);
            cb.fill();
            cb.setColorFill(new BaseColor(200, 60, 60));
            cb.rectangle(cx + r + 20, cy - 10, 12, 12);
            cb.fill();

            Font legFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.DARK_GRAY);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                new Phrase(String.format("Entradas: %.1f%%", reporte.getPorcentajeEntradas()), legFont),
                cx + r + 38, cy + 14, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                new Phrase(String.format("Salidas: %.1f%%", reporte.getPorcentajeSalidas()), legFont),
                cx + r + 38, cy - 6, 0);

            doc.close();

        } catch (DocumentException e) {
            throw new IOException("Error al generar PDF: " + e.getMessage(), e);
        }
    }

    private void drawPieSlice(PdfContentByte cb, float cx, float cy, float r,
                               float startDeg, float endDeg) {
        cb.moveTo(cx, cy);
        double startRad = Math.toRadians(startDeg);
        double endRad = Math.toRadians(endDeg);
        cb.lineTo((float)(cx + r * Math.cos(startRad)), (float)(cy + r * Math.sin(startRad)));
        int steps = 30;
        for (int k = 1; k <= steps; k++) {
            double angle = startRad + (endRad - startRad) * k / steps;
            cb.lineTo((float)(cx + r * Math.cos(angle)), (float)(cy + r * Math.sin(angle)));
        }
        cb.lineTo(cx, cy);
        cb.closePathFillStroke();
    }

    private void addCell(PdfPTable table, String text, Font font,
                         BaseColor bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(align);
        cell.setPadding(6);
        cell.setBorderColor(new BaseColor(220, 220, 220));
        table.addCell(cell);
    }

    private boolean verificarSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
