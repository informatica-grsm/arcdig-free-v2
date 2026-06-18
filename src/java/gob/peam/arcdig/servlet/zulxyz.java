/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.servlet;



import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.dao.DocumentoDao;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alabajos
 */
@WebServlet(name = "zulxyz", urlPatterns = {"/zulxyz"})
public class zulxyz extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        //request.getRequestDispatcher("./WEB-INF/asistencia/zul/printAsistencia.zul").forward(request, response);
        //AlgoritmoDao dao = new AlgoritmoDao();

        //response.getWriter().print(dao.etiquetar());
        //response.getWriter().print(dao.metadata());
        //dao.cambiarUsuario();
        List<Documento> lista = new DocumentoDao().listaBoletas();

        for (Documento bean : lista) {
            String carpeta = "";
            try {
                carpeta = bean.getRuta();
            } catch (NullPointerException ex) {
                carpeta = "CATALOGOS";
            }

            // response.getWriter().print(bean.getId()+"");

            ServletContext srvcon = getServletContext();
            String ruta = srvcon.getRealPath("//" + carpeta + "//");
            File file = new File(ruta + "//" + bean.getId() + ".pdf");

            if (!file.exists()) {
                String buscado = srvcon.getRealPath("//CATALOGOS//");
                File directorio = new File(buscado);
                int indice = 0;
                String[] tabs = new String[1000];
                int ind = 0;
                this.ruta = "";

                String nueva_ruta = listarDirectorio(directorio, "/", +bean.getId() + ".pdf", indice, ind, tabs, "");
                HashMap hm = new HashMap();
                hm.put("id", bean.getId());
                hm.put("docuRuta", nueva_ruta);
                new DocumentoDao().updateRuta1(hm);
            }
        }
    }
    
    private String ruta = "";

    public String listarDirectorio(File f, String separador, String Origen, int indice, int ind, String[] tab, String r) throws IOException {
        File[] ficheros = f.listFiles();
        String ruta = r;
        if (indice == 0) {

            for (int x = 0; x < ficheros.length; x++) {
                if (indice == 0) {
                    tab[ind] = ficheros[x].getName();
                    tab[ind + 1] = null;
                }
                if (ficheros[x].getName().equals(Origen)) {
                    indice = 1;
                    for (int i = 0; i < tab.length; i++) {
                        if (tab[i + 1] != null && !(tab[i + 1]).equals("")) {
                            ruta += "//" + tab[i];
                        } else {
                            break;
                        }
                    }

                    break;
                } else {
                    if (ficheros[x].isDirectory()) {
                        ind++;
                        String nuevo_separador;
                        nuevo_separador = separador + "";
                        ruta = listarDirectorio(ficheros[x], nuevo_separador, Origen, indice, ind, tab, ruta);
                        ind--;
                    }
                }
            }
        }
        return ruta;
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
