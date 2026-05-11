<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<aside class="sidebar">
    <div class="sidebar-brand">
        <h1>Control Finanzas</h1>
        <p>${sessionScope.usuario.nombre}</p>
    </div>
    <nav class="sidebar-nav">
        <div class="nav-label">Principal</div>
        <a href="${pageContext.request.contextPath}/dashboard"
           class="nav-item ${menu == 'dashboard' ? 'active' : ''}">
            <span class="icon">&#9646;</span> Dashboard
        </a>
        <div class="nav-label">Transacciones</div>
        <a href="${pageContext.request.contextPath}/entradas"
           class="nav-item ${menu == 'reg_entrada' ? 'active' : ''}">
            <span class="icon">+</span> Registrar Entrada
        </a>
        <a href="${pageContext.request.contextPath}/salidas"
           class="nav-item ${menu == 'reg_salida' ? 'active' : ''}">
            <span class="icon">-</span> Registrar Salida
        </a>
        <div class="nav-label">Reportes</div>
        <a href="${pageContext.request.contextPath}/entradas?accion=ver"
           class="nav-item ${menu == 'ver_entradas' ? 'active' : ''}">
            <span class="icon">&#9654;</span> Ver Entradas
        </a>
        <a href="${pageContext.request.contextPath}/salidas?accion=ver"
           class="nav-item ${menu == 'ver_salidas' ? 'active' : ''}">
            <span class="icon">&#9660;</span> Ver Salidas
        </a>
        <a href="${pageContext.request.contextPath}/balance"
           class="nav-item ${menu == 'balance' ? 'active' : ''}">
            <span class="icon">=</span> Mostrar Balance
        </a>
    </nav>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/logout">
            <span>&#8594;</span> Cerrar Sesion
        </a>
    </div>
</aside>
