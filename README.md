# Control de Finanzas
Sistema web de control de entradas y salidas financieras.
**Stack:** Java 17 + Jakarta EE 10 + Payara 7.2026.2 + MySQL + JSP/CSS

---

## Requisitos previos
- JDK 17
- Maven 3.8+
- MySQL 8.x
- Payara Server 7.2026.2

## Acceder al sistema

Abre el navegador en:
```
http://localhost:8080/control-finanzas
```

---

## Estructura del proyecto

```
control-finanzas/
├── sql/
│   └── schema.sql                    # Script de base de datos
├── src/main/
│   ├── java/com/finanzas/
│   │   ├── modelo/
│   │   │   ├── Usuario.java          # Clase usuario
│   │   │   ├── Entrada.java          # Clase entrada
│   │   │   ├── Salida.java           # Clase salida
│   │   │   └── ReporteBalance.java   # Clase reporte de balance
│   │   ├── dao/
│   │   │   ├── LoginDAO.java         # Acceso a datos de login
│   │   │   ├── EntradaDAO.java       # Acceso a datos de entradas
│   │   │   └── SalidaDAO.java        # Acceso a datos de salidas
│   │   ├── servlet/
│   │   │   ├── LoginServlet.java     # Controlador login
│   │   │   ├── DashboardServlet.java # Controlador dashboard
│   │   │   ├── EntradaServlet.java   # Controlador entradas
│   │   │   ├── SalidaServlet.java    # Controlador salidas
│   │   │   ├── BalanceServlet.java   # Controlador balance + PDF
│   │   │   └── LogoutServlet.java    # Controlador logout
│   │   └── util/
│   │       └── ConexionDB.java       # Conexion MySQL
│   └── webapp/
│       ├── css/
│       │   └── style.css             # Estilos (Poppins, gris/verde)
│       ├── views/
│       │   ├── login.jsp
│       │   ├── menu.jsp              # Sidebar (include)
│       │   ├── dashboard.jsp
│       │   ├── registrar_entrada.jsp
│       │   ├── registrar_salida.jsp
│       │   ├── ver_entradas.jsp
│       │   ├── ver_salidas.jsp
│       │   └── balance.jsp           # Reporte con Chart.js
│       ├── uploads/                  # Carpeta de facturas subidas
│       ├── index.jsp
│       └── WEB-INF/
│           └── web.xml
└── pom.xml
```

---

## Funcionalidades

| Funcionalidad                | Descripcion                                         |
|------------------------------|-----------------------------------------------------|
| Login con sesion             | Autenticacion con SHA-256 en MySQL                  |
| Dashboard                    | Resumen de totales y balance rapido                 |
| Registrar entrada            | Tipo, monto, fecha, foto de factura                 |
| Registrar salida             | Tipo, monto, fecha, foto de factura                 |
| Ver entradas                 | Tabla con imagen clickeable para ver factura grande |
| Ver salidas                  | Tabla con imagen clickeable para ver factura grande |
| Reporte de balance           | Tabla doble + balance resultante + grafico de pastel|
| Exportar PDF                 | Genera PDF con tablas, balance y grafico de pastel  |
| Logout                       | Invalida la sesion                                  |

---

## Notas de seguridad
- Las contrasenas se almacenan con SHA-256 en MySQL (funcion `SHA2`)
- Las sesiones expiran automaticamente en 60 minutos
- Se validan todos los campos requeridos en servidor
