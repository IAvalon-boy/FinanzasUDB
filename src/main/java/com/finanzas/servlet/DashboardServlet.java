package com.finanzas.servlet;

import com.finanzas.dao.EntradaDAO;
import com.finanzas.dao.SalidaDAO;
import com.finanzas.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final EntradaDAO entradaDAO = new EntradaDAO();
    private final SalidaDAO salidaDAO = new SalidaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int uid = usuario.getId();

        BigDecimal totalEntradas = entradaDAO.totalPorUsuario(uid);
        BigDecimal totalSalidas = salidaDAO.totalPorUsuario(uid);
        BigDecimal balance = totalEntradas.subtract(totalSalidas);

        req.setAttribute("totalEntradas", totalEntradas);
        req.setAttribute("totalSalidas", totalSalidas);
        req.setAttribute("balance", balance);

        req.getRequestDispatcher("/views/dashboard.jsp").forward(req, resp);
    }
}
