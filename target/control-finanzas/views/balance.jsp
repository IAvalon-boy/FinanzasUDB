<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="en_US" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Balance - Control Finanzas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
</head>
<body>
<c:set var="menu" value="balance"/>
<div class="layout">
    <jsp:include page="menu.jsp"/>
    <div class="main">
        <div class="topbar">
            <h2>Reporte de Balance</h2>
            <div style="display:flex;align-items:center;gap:10px">
                <a href="${pageContext.request.contextPath}/balance?exportar=pdf"
                   class="btn btn-primary btn-sm">
                    Exportar PDF
                </a>
                <div class="topbar-user">
                    <div class="avatar">${fn:substring(sessionScope.usuario.nombre,0,1)}</div>
                    <span>${sessionScope.usuario.nombre}</span>
                </div>
            </div>
        </div>

        <div class="content">
            <!-- Tablas lado a lado -->
            <div class="report-grid">

                <!-- Entradas -->
                <div class="table-card">
                    <div class="table-header">
                        <h3>Entradas</h3>
                    </div>
                    <c:choose>
                        <c:when test="${empty reporte.entradas}">
                            <div class="empty-state">Sin entradas</div>
                        </c:when>
                        <c:otherwise>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Tipo</th>
                                        <th>Monto</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="e" items="${reporte.entradas}">
                                        <tr>
                                            <td>${e.tipoEntrada}</td>
                                            <td>$<fmt:formatNumber value="${e.monto}" pattern="#,##0.00"/></td>
                                        </tr>
                                    </c:forEach>
                                    <tr style="background:var(--verde-bg)">
                                        <td><strong>TOTAL</strong></td>
                                        <td><strong style="color:var(--verde)">
                                            $<fmt:formatNumber value="${reporte.totalEntradas}" pattern="#,##0.00"/>
                                        </strong></td>
                                    </tr>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Salidas -->
                <div class="table-card">
                    <div class="table-header">
                        <h3>Salidas</h3>
                    </div>
                    <c:choose>
                        <c:when test="${empty reporte.salidas}">
                            <div class="empty-state">Sin salidas</div>
                        </c:when>
                        <c:otherwise>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Tipo</th>
                                        <th>Monto</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${reporte.salidas}">
                                        <tr>
                                            <td>${s.tipoSalida}</td>
                                            <td>$<fmt:formatNumber value="${s.monto}" pattern="#,##0.00"/></td>
                                        </tr>
                                    </c:forEach>
                                    <tr style="background:var(--rojo-claro)">
                                        <td><strong>TOTAL</strong></td>
                                        <td><strong style="color:var(--rojo)">
                                            $<fmt:formatNumber value="${reporte.totalSalidas}" pattern="#,##0.00"/>
                                        </strong></td>
                                    </tr>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Balance resultante -->
            <div class="${reporte.balance >= 0 ? 'balance-card' : 'balance-card negative'}"
                 style="max-width:560px">
                <div>
                    <div class="label">Balance Mensual Resultante</div>
                    <div style="font-size:11px;opacity:.7">Total Entradas - Total Salidas</div>
                </div>
                <div class="amount">
                    $<fmt:formatNumber value="${reporte.balance}" pattern="#,##0.00"/>
                </div>
            </div>

            <!-- Grafico de pastel -->
            <div class="card" style="max-width:480px;margin-top:20px">
                <div style="font-size:13px;font-weight:600;color:var(--gris-oscuro);margin-bottom:14px">
                    Grafico de Balance: Entradas vs Salidas
                </div>
                <canvas id="pieChart" width="380" height="250"></canvas>
            </div>
        </div>
    </div>
</div>

<script>
const ctx = document.getElementById('pieChart');
const pctEnt = ${reporte.porcentajeEntradas};
const pctSal = ${reporte.porcentajeSalidas};

new Chart(ctx, {
    type: 'pie',
    data: {
        labels: [
            'Entradas (' + pctEnt.toFixed(1) + '%)',
            'Salidas (' + pctSal.toFixed(1) + '%)'
        ],
        datasets: [{
            data: [pctEnt, pctSal],
            backgroundColor: ['#2d7a4f', '#dc2626'],
            borderColor: ['#1e5e3a', '#b91c1c'],
            borderWidth: 1
        }]
    },
    options: {
        responsive: false,
        plugins: {
            legend: {
                position: 'right',
                labels: {
                    font: { family: 'Poppins', size: 12 },
                    color: '#374151'
                }
            }
        }
    }
});
</script>
</body>
</html>
