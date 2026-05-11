package com.finanzas.servlet;

import com.finanzas.dao.SalidaDAO;
import com.finanzas.modelo.Salida;
import com.finanzas.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebServlet("/salidas")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class SalidaServlet extends HttpServlet {

    private final SalidaDAO salidaDAO = new SalidaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!verificarSesion(req, resp)) return;
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        String accion = req.getParameter("accion");

        if ("ver".equals(accion)) {
            List<Salida> salidas = salidaDAO.listarPorUsuario(usuario.getId());
            req.setAttribute("salidas", salidas);
            req.getRequestDispatcher("/views/ver_salidas.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/views/registrar_salida.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!verificarSesion(req, resp)) return;
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");

        String tipo = req.getParameter("tipoSalida");
        String montoStr = req.getParameter("monto");
        String fechaStr = req.getParameter("fecha");

        if (tipo == null || tipo.trim().isEmpty() ||
            montoStr == null || montoStr.trim().isEmpty() ||
            fechaStr == null || fechaStr.trim().isEmpty()) {
            req.setAttribute("error", "Todos los campos son requeridos.");
            req.getRequestDispatcher("/views/registrar_salida.jsp").forward(req, resp);
            return;
        }

        try {
            Salida salida = new Salida();
            salida.setTipoSalida(tipo.trim());
            salida.setMonto(new BigDecimal(montoStr));
            salida.setFecha(LocalDate.parse(fechaStr));
            salida.setUsuarioId(usuario.getId());

            Part filePart = req.getPart("factura");
            if (filePart != null && filePart.getSize() > 0) {
                String uploadDir = getServletContext().getRealPath("/uploads");
                Files.createDirectories(Paths.get(uploadDir));
                String extension = obtenerExtension(filePart.getSubmittedFileName());
                String nombreArchivo = UUID.randomUUID().toString() + extension;
                filePart.write(uploadDir + File.separator + nombreArchivo);
                salida.setFacturaPath("uploads/" + nombreArchivo);
            }

            if (salidaDAO.registrar(salida)) {
                resp.sendRedirect(req.getContextPath() + "/salidas?accion=ver&exito=1");
            } else {
                req.setAttribute("error", "Error al registrar la salida.");
                req.getRequestDispatcher("/views/registrar_salida.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Monto invalido.");
            req.getRequestDispatcher("/views/registrar_salida.jsp").forward(req, resp);
        }
    }

    private boolean verificarSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) return ".jpg";
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".")).toLowerCase();
    }
}
