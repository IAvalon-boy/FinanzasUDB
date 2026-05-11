package com.finanzas.servlet;

import com.finanzas.dao.EntradaDAO;
import com.finanzas.modelo.Entrada;
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

@WebServlet("/entradas")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class EntradaServlet extends HttpServlet {

    private final EntradaDAO entradaDAO = new EntradaDAO();

    // Carpeta donde se guardarán las imágenes (fuera del WAR)
    private static final String UPLOAD_DIR = System.getProperty("user.home") + File.separator + "finanzas_uploads";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!verificarSesion(req, resp)) return;
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        String accion = req.getParameter("accion");

        if ("ver".equals(accion)) {
            List<Entrada> entradas = entradaDAO.listarPorUsuario(usuario.getId());
            req.setAttribute("entradas", entradas);
            req.getRequestDispatcher("/views/ver_entradas.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/views/registrar_entrada.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!verificarSesion(req, resp)) return;
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");

        String tipo = req.getParameter("tipoEntrada");
        String montoStr = req.getParameter("monto");
        String fechaStr = req.getParameter("fecha");

        if (tipo == null || tipo.trim().isEmpty() ||
            montoStr == null || montoStr.trim().isEmpty() ||
            fechaStr == null || fechaStr.trim().isEmpty()) {
            req.setAttribute("error", "Todos los campos son requeridos.");
            req.getRequestDispatcher("/views/registrar_entrada.jsp").forward(req, resp);
            return;
        }

        try {
            Entrada entrada = new Entrada();
            entrada.setTipoEntrada(tipo.trim());
            entrada.setMonto(new BigDecimal(montoStr));
            entrada.setFecha(LocalDate.parse(fechaStr));
            entrada.setUsuarioId(usuario.getId());

            Part filePart = req.getPart("factura");
            if (filePart != null && filePart.getSize() > 0) {
                // Crear la carpeta si no existe
                Path uploadPath = Paths.get(UPLOAD_DIR);
                Files.createDirectories(uploadPath);

                // Generar nombre único para el archivo
                String extension = obtenerExtension(filePart.getSubmittedFileName());
                String nombreArchivo = UUID.randomUUID().toString() + extension;

                // Guardar el archivo
                filePart.write(uploadPath.resolve(nombreArchivo).toString());

                // Guardar solo el nombre del archivo en la BD
                entrada.setFacturaPath(nombreArchivo);

                System.out.println("Archivo guardado en: " + uploadPath.resolve(nombreArchivo));
            }

            if (entradaDAO.registrar(entrada)) {
                resp.sendRedirect(req.getContextPath() + "/entradas?accion=ver&exito=1");
            } else {
                req.setAttribute("error", "Error al registrar la entrada.");
                req.getRequestDispatcher("/views/registrar_entrada.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Monto invalido.");
            req.getRequestDispatcher("/views/registrar_entrada.jsp").forward(req, resp);
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