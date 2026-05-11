package com.finanzas.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;

@WebServlet("/uploads/*")
public class ArchivoServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "C:\\finanzas_uploads";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Quitar el "/" inicial
        String nombreArchivo = pathInfo.substring(1);

        // Seguridad: evitar path traversal (ej: ../../etc/passwd)
        if (nombreArchivo.contains("..") || nombreArchivo.contains("/") || nombreArchivo.contains("\\")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nombre de archivo inválido.");
            return;
        }

        Path archivo = Paths.get(UPLOAD_DIR, nombreArchivo);

        if (!Files.exists(archivo)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado.");
            return;
        }

        // Detectar el tipo de contenido (image/jpeg, application/pdf, etc.)
        String mimeType = getServletContext().getMimeType(nombreArchivo);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        resp.setContentType(mimeType);
        resp.setContentLengthLong(Files.size(archivo));

        // Enviar el archivo al navegador
        Files.copy(archivo, resp.getOutputStream());
        resp.getOutputStream().flush();
    }
}