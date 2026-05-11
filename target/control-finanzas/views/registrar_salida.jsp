<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Salida - Control Finanzas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<c:set var="menu" value="reg_salida"/>
<div class="layout">
    <jsp:include page="menu.jsp"/>
    <div class="main">
        <div class="topbar">
            <h2>Registrar Salida</h2>
            <div class="topbar-user">
                <div class="avatar">${fn:substring(sessionScope.usuario.nombre,0,1)}</div>
                <span>${sessionScope.usuario.nombre}</span>
            </div>
        </div>
        <div class="content">
            <div class="form-card">
                <div class="form-title">Nueva Salida</div>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/salidas"
                      enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="tipoSalida">Tipo de Salida</label>
                        <input type="text" id="tipoSalida" name="tipoSalida"
                               placeholder="Ej: Luz, Agua, Renta..."
                               value="${param.tipoSalida}" required>
                    </div>
                    <div class="form-group">
                        <label for="monto">Monto de Salida ($)</label>
                        <input type="number" id="monto" name="monto"
                               step="0.01" min="0.01"
                               placeholder="0.00"
                               value="${param.monto}" required>
                    </div>
                    <div class="form-group">
                        <label for="fecha">Fecha de Salida</label>
                        <input type="date" id="fecha" name="fecha"
                               value="${param.fecha}" required>
                    </div>
                    <div class="form-group">
                        <label for="factura">Factura de Salida (imagen opcional)</label>
                        <input type="file" id="factura" name="factura"
                               accept="image/*,.pdf">
                        <p style="font-size:11px;color:var(--gris);margin-top:4px">
                            Formatos: JPG, PNG, PDF. Maximo 5 MB.
                        </p>
                    </div>
                    <div style="display:flex;gap:10px;margin-top:8px">
                        <button type="submit" class="btn btn-primary">Guardar Salida</button>
                        <a href="${pageContext.request.contextPath}/salidas?accion=ver"
                           class="btn btn-outline">Cancelar</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
