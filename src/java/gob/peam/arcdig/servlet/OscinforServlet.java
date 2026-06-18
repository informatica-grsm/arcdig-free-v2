/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.servlet;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Entidad;
import gob.peam.arcdig.dao.EntidadDao;
import java.io.IOException;
import java.io.PrintWriter;
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
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.xml.sax.SAXException;

/**
 *
 * @author alabajos
 */
@WebServlet(name = "OscinforServlet", urlPatterns = {"/OscinforServlet"})
public class OscinforServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");

        try {
            ServletOutputStream ouputStream = null;

            byte[] bs = JasperExportManager.exportReportToPdf(crearReporte(id));

            response.setContentType("application/pdf");
            response.setContentLength(bs.length);
            ouputStream = response.getOutputStream();
            ouputStream.write(bs, 0, bs.length);
            ouputStream.flush();
            ouputStream.close();

        } catch (JRException | SQLException | ClassNotFoundException | ParserConfigurationException | SAXException | IOException ex) {
            try {
                response.getWriter().print(ex);
            } catch (IOException ex1) {
                Logger.getLogger(AppReport4.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void supervisados(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

        } catch (Exception ex) {
            response.getWriter().println("Hubo problemas de conexion con el patrocinador: " + ex.getMessage());
        }
    }

    public JasperPrint crearReporte(String id) throws JRException, SQLException, ServletException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        ServletContext srvcon = getServletContext();
        String rutafisica = srvcon.getRealPath("//resources//reportes//");
        JasperReport masterReport = null;

        Map parametro = new HashMap();

        masterReport = (JasperReport) JRLoader.loadObject(rutafisica + "/oscinfor.jasper");

        Locale locale = new Locale("es", "ES");
        parametro.put(JRParameter.REPORT_LOCALE, locale);
        try {
            parametro.put("id", Integer.parseInt(id)-1);
        } catch (NullPointerException ex) {
            parametro.put("id", 0);
        }

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
