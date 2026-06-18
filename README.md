# ArcDigAR

Sistema web Java para gestión documental (ARC-DIG), construido sobre Java EE, ZK Framework, iBatis y despliegue WAR con Ant/NetBeans.

## Descripción
ArcDigAR centraliza operaciones de catálogo, categorías, metadatos, permisos, repositorios y flujos de integración (por ejemplo, OSINFOR/SIADO), con interfaz web basada en ZUL/JSP y backend Java.

## Stack tecnológico
- Java (aplicación web)
- Java EE / Servlet API
- ZK Framework (vistas `.zul`)
- iBatis / mapeos XML
- Ant (build con `build.xml`)
- NetBeans Web Project (`nbproject/`)

## Estructura principal
- `src/java/`: código fuente Java
- `web/`: recursos web, vistas, `WEB-INF`
- `docs/`: documentación funcional y de soporte
- `nbproject/`: configuración de proyecto NetBeans
- `build.xml`: script principal de compilación/despliegue

## Requisitos
- JDK 8 o compatible con el proyecto
- Apache Ant
- Servidor de aplicaciones compatible con Java EE (por ejemplo, Tomcat/GlassFish)
- Dependencias del proyecto configuradas según `nbproject/project.properties`

## Ejecución (referencia)
1. Abrir el proyecto en NetBeans.
2. Verificar rutas de librerías y propiedades locales en `nbproject/private/private.properties`.
3. Ejecutar tareas Ant desde NetBeans o consola:
   - Compilar: `ant clean compile`
   - Empaquetar: `ant war`
4. Desplegar el WAR generado en el servidor de aplicaciones.

## Notas
- Este repositorio incluye recursos SQL, reportes Jasper y archivos de configuración de integración.
- Revisar `docs/` para informes y contexto operativo.

## Licencia
Este proyecto se distribuye bajo la licencia **GNU Affero General Public License v3.0**.
Consulta el archivo [LICENSE](LICENSE) para el texto completo.
