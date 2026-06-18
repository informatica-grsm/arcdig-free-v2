/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.servlet;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Entidad;
import gob.peam.arcdig.dao.EntidadDao;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.util.JRLoader;
import org.xml.sax.SAXException;

/**
 *
 * @author alabajos
 */
@WebServlet(name = "AppReport4", urlPatterns = {"/AppReport4.pdf"})
public class AppReport4 extends HttpServlet {

    public HttpServletRequest req;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        String formato = request.getParameter("format");

        req = request;

        //String reporte = request.getParameter("report");
        try {
            ServletOutputStream ouputStream = null;

            if ("pdf".equals(formato)) {

                byte[] bs = JasperExportManager.exportReportToPdf(crearReporte(formato));

                response.setContentType("application/pdf");
                response.setContentLength(bs.length);
                ouputStream = response.getOutputStream();
                ouputStream.write(bs, 0, bs.length);
                ouputStream.flush();
                ouputStream.close();

            } else if ("excel".equals(formato)) {
                JasperPrint print2 = crearReporte(formato);
                ServletContext srvcon = getServletContext();
                String rutafisica = srvcon.getRealPath("//resources//reportes//");
                JRXlsExporter exportador = new JRXlsExporter();
                exportador.setParameter(JRExporterParameter.JASPER_PRINT, print2);
                exportador.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutafisica + "/reporte.xls");
                exportador.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, true);
                exportador.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
                exportador.setParameter(JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER, false);
                exportador.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
                exportador.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
                exportador.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
                exportador.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
                exportador.exportReport();
                FileInputStream entrada = new FileInputStream(rutafisica + "/reporte.xls");
                byte[] lectura = new byte[entrada.available()];
                entrada.read(lectura);
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;filename = reporte.xls");
                response.setContentLength(lectura.length);
                response.getOutputStream().write(lectura);
                response.getOutputStream().flush();
                response.getOutputStream().close();
                entrada.close();

            }
        } catch (JRException | SQLException | ClassNotFoundException | ParserConfigurationException | SAXException | IOException ex) {
            try {
                response.getWriter().print(ex);
            } catch (IOException ex1) {
                Logger.getLogger(AppReport4.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public JasperPrint crearReporte(String formato) throws JRException, SQLException, ServletException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        ServletContext srvcon = getServletContext();
        String rutafisica = srvcon.getRealPath("//resources//reportes//");
        JasperReport masterReport = null;

        Map parametro = new HashMap();
        if ("pdf".equals(formato)) {
            masterReport = (JasperReport) JRLoader.loadObject(rutafisica + "/registro_x_tipo.jasper");
        } else {
            masterReport = (JasperReport) JRLoader.loadObject(rutafisica + "/registro_x_tipo_xls.jasper");
        }
        Entidad  enti = new Entidad();
        enti = new EntidadDao().seleccionarEntidad();
        // String rutaLogo = srvcon.getRealPath("//reportes//sin_valido.png");
        // String rutaLogoH = srvcon.getRealPath("//reportes//sin_valido_h.png");
        Locale locale = new Locale("es", "ES");
        parametro.put(JRParameter.REPORT_LOCALE, locale);
        parametro.put("entidad", enti.getNombre());
        parametro.put("query", req.getSession().getAttribute("query").toString());
        //parametro.put("ruta_logo", rutaLogo);
        //parametro.put("ruta_logo_h", rutaLogoH);

        JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, ConexionReporte.AbrirConexion());
        ConexionReporte.CerrarConexion();
        return jasperPrint;
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
