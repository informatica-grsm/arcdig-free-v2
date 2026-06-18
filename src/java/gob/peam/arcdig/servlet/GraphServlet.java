/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.servlet;

import com.google.gson.Gson;
import gob.peam.arcdig.beans.Catalogo;
import gob.peam.arcdig.beans.Circle;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.dao.CatalogoDao;
import gob.peam.arcdig.dao.DocumentoDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jpgprog84
 */
@WebServlet(name = "Graph", urlPatterns = {"/Graph"})
public class GraphServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 8326534055234264474L;

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

        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        if ("circle".equals(action)) {
            circle(request, response);
        }

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

    private void circle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Gson g = new Gson();
        List<Circle> c = new ArrayList<Circle>();
        String[] color = new String[10];
        color[0] = "#68BC31";
        color[1] = "#2091CF";
        color[2] = "#AF4E96";
        color[3] = "#DA5430";
        color[4] = "#CCC";
        int index = 0;
        long total = 0;
        Date fecha = new Date(new Date().getTime());
        Calendar calInicio = Calendar.getInstance();

        calInicio.setTime(fecha);

        for (Catalogo catalogo : new CatalogoDao().listarComboCatalogo()) {
            HashMap hm = new HashMap();
            if (request.getParameter("ref").equals("uno")) {
                hm.put("query", "and date_part('year', docu_fecha) = cast('" + calInicio.get(Calendar.YEAR) + "' as integer)");
            } else if (request.getParameter("ref").equals("dos")) {
                int day1 = calInicio.getFirstDayOfWeek();
                calInicio.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calInicio.add(Calendar.DAY_OF_YEAR, -7);
                Date fecha1 = calInicio.getTime();

                calInicio.add(Calendar.DAY_OF_YEAR, 6);
                Date fecha2 = calInicio.getTime();
                hm.put("query", "and (docu_fecha>='" + fecha1 + "' and docu_fecha<='" + fecha2 + "')");
            } else if (request.getParameter("ref").equals("tres")) {
                hm.put("query", "and date_part('month', docu_fecha) = cast('" + calInicio.get(Calendar.MONTH) + "' as integer)");
            } else if (request.getParameter("ref").equals("cuatro")) {
                hm.put("query", "and date_part('month', docu_fecha) = cast('" + (calInicio.get(Calendar.MONTH) - 1) + "' as integer)");
            } else {
                hm.put("query", "and date_part('year', docu_fecha) = cast('" + (calInicio.get(Calendar.YEAR) - 1) + "' as integer)");
            }

            hm.put("cataId", catalogo.getCataId());

            List<Documento> ld = new DocumentoDao().listarDocumentosXCatalogo(hm);
            total += ld.size();
            if (!ld.isEmpty()) {
                c.add(new Circle(catalogo.getCataNombre() + " (" + ld.size() + " Reg.)", ld.size(), null));
            }
            index++;
        }
        /*index = 0;
         for(Circle ci: c){
         double percent = Math.round((ci.getData()/total)*100) / Math.pow(10, 2);
         c.get(index).setData((int) percent);
         }*/
        String arg = g.toJson(c);
        response.getWriter().print(arg);
    }
}
