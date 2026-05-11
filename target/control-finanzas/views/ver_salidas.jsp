<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ver Salidas - Control Finanzas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<c:set var="menu" value="ver_salidas"/>
<div class="layout">
    <jsp:include page="menu.jsp"/>
    <div class="main">
        <div class="topbar">
            <h2>Salidas Registradas</h2>
            <div class="topbar-user">
                <div class="avatar">${fn:substring(sessionScope.usuario.nombre,0,1)}</div>
                <span>${sessionScope.usuario.nombre}</span>
            </div>
        </div>
        <div class="content">
            <c:if test="${param.exito == '1'}">
                <div class="alert alert-success">Salida registrada correctamente.</div>
            </c:if>

            <div class="table-card">
                <div class="table-header">
                    <h3>Listado de Salidas</h3>
                    <a href="${pageContext.request.contextPath}/salidas" class="btn btn-primary btn-sm">
                        + Nueva Salida
                    </a>
                </div>

                <c:choose>
                    <c:when test="${empty salidas}">
                        <div class="empty-state">No hay salidas registradas aun.</div>
                    </c:when>
                    <c:otherwise>
                        <table>
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Tipo de Salida</th>
                                    <th>Monto</th>
                                    <th>Fecha</th>
                                    <th>Factura</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="s" items="${salidas}" varStatus="st">
                                    <tr>
                                        <td>${st.count}</td>
                                        <td>
                                            <span class="badge badge-red">${s.tipoSalida}</span>
                                        </td>
                                        <td><strong style="color:var(--rojo)">$<fmt:formatNumber value="${s.monto}" pattern="#,##0.00"/></strong></td>
                                        <td>${s.fecha}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty s.facturaPath}">
                                                    <img src="${pageContext.request.contextPath}/${s.facturaPath}"
                                                         class="factura-img"
                                                         onclick="abrirModal(this.src)"
                                                         alt="Factura"
                                                         onerror="this.outerHTML='<span style=\'color:var(--gris);font-size:11px\'>archivo</span>'">
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color:var(--gris);font-size:12px">Sin factura</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<!-- Modal imagen -->
<div class="modal-overlay" id="modalOverlay" onclick="cerrarModal()">
    <span class="modal-close">&times;</span>
    <img src="" class="modal-img" id="modalImg" onclick="event.stopPropagation()">
</div>

<script>
function abrirModal(src) {
    document.getElementById('modalImg').src = src;
    document.getElementById('modalOverlay').classList.add('active');
}
function cerrarModal() {
    document.getElementById('modalOverlay').classList.remove('active');
}
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') cerrarModal();
});
</script>
</body>
</html>
