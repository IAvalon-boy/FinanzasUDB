<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Control Finanzas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<c:set var="menu" value="dashboard"/>
<div class="layout">
    <jsp:include page="menu.jsp"/>
    <div class="main">
        <div class="topbar">
            <h2>Dashboard</h2>
            <div class="topbar-user">
                <div class="avatar">${fn:substring(sessionScope.usuario.nombre,0,1)}</div>
                <span>${sessionScope.usuario.nombre}</span>
            </div>
        </div>
        <div class="content">
            <p style="font-size:13px;color:var(--gris);margin-bottom:20px">
                Bienvenido, <strong>${sessionScope.usuario.nombre}</strong>. Aqui tienes un resumen de tus finanzas.
            </p>

            <div class="cards-grid">
                <div class="card">
                    <div class="card-stat">
                        <div>
                            <div class="label">Total Entradas</div>
                            <div class="value" style="color:var(--verde)">
                                $<fmt:formatNumber value="${totalEntradas}" pattern="#,##0.00"/>
                            </div>
                        </div>
                        <div class="icon-box green">+</div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-stat">
                        <div>
                            <div class="label">Total Salidas</div>
                            <div class="value" style="color:var(--rojo)">
                                $<fmt:formatNumber value="${totalSalidas}" pattern="#,##0.00"/>
                            </div>
                        </div>
                        <div class="icon-box red">-</div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-stat">
                        <div>
                            <div class="label">Balance</div>
                            <div class="value ${balance >= 0 ? 'balance-positive' : 'balance-negative'}">
                                $<fmt:formatNumber value="${balance}" pattern="#,##0.00"/>
                            </div>
                        </div>
                        <div class="icon-box blue">=</div>
                    </div>
                </div>
            </div>

            <div class="card" style="max-width:500px">
                <div style="font-size:13px;font-weight:600;color:var(--gris-oscuro);margin-bottom:14px">
                    Acciones rapidas
                </div>
                <div style="display:flex;gap:10px;flex-wrap:wrap">
                    <a href="${pageContext.request.contextPath}/entradas" class="btn btn-primary">Registrar Entrada</a>
                    <a href="${pageContext.request.contextPath}/salidas" class="btn btn-outline">Registrar Salida</a>
                    <a href="${pageContext.request.contextPath}/balance" class="btn btn-outline">Ver Balance</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
