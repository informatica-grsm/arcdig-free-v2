/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.servlet;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.DocumentoEtiqueta;
import gob.peam.arcdig.beans.Resol;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.dao.EtiquetaDocuDao;
import gob.peam.arcdig.utils.FormatoFecha;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alabajos
 */
@WebServlet(name = "Migracionxyz", urlPatterns = {"/Migracionxyz"})
public class Migracionxyz extends HttpServlet {

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

        /* TODO output your page here. You may use following sample code. */
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html>");
        response.getWriter().println("<head>");
        response.getWriter().println("<title>Servlet Migracionxyz</title>");
        response.getWriter().println("</head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>Servlet Migracionxyz at " + request.getContextPath() + "</h1>");

        response.getWriter().println("<h2>Documentos " + new DocumentoDao().listarResol().size() + "</h2>");

        List<Resol> resols = new DocumentoDao().listarResol();

        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
        for (int i = 0; i < resols.size(); i++) {
            Documento bean = new Documento();
            bean.setPrivado(false);
            bean.setPublico(true);
            bean.setPersonalizado(false);

            String itemMeta2 = "";
            /*itemMeta2 = "<metadata>";
            itemMeta2 += "<docu_metadata><meta_id>31</meta_id><meta_nombre>ANEXO</meta_nombre><meta_descripcion></meta_descripcion></docu_metadata>";
            itemMeta2 += "</metadata>";*/
            bean.setMetaData(itemMeta2);

            bean.setTitulo(resols.get(i).getTitulo().toUpperCase());
            bean.setTituloMin(resols.get(i).getTitulo());
            
            try {
                bean.setResumen(resols.get(i).getDetalle1().toUpperCase() == null ? "" : resols.get(i).getDetalle1().toUpperCase());
                bean.setResumenMin(resols.get(i).getDetalle1());
            } catch (Exception ex) {
                bean.setResumen("");
                bean.setResumenMin("");
            }

            bean.setFechaDocx(new FormatoFecha().formatFechaCorta(resols.get(i).getFecha()));
            bean.setMimeTypes("application/pdf");
            String anho = bean.getFechaDocx().substring(6);
            bean.setUsuaId(1);
            bean.setCataId(55);
            bean.setCateId(13);
            bean.setTidoId(66);

            bean.setPropietario("44429462");
            String ruta = "D:/REPOSITORIO/PORTAL/"+resols.get(i).getPath();
            String rutaSave = "//CONSEJO REGIONAL//ACTA DE LA COMISION//" + anho;
            try {
                insertarArchivo(ruta, rutaSave);
            } catch (Exception ex) {
                response.getWriter().println(ex.getMessage());
            }
            bean.setRuta(rutaSave);
            request.getSession().setAttribute("dni", "44429462");
            Integer max = new DocumentoDao().insertDoc(bean);

            DocumentoEtiqueta de = new DocumentoEtiqueta();
            de.setDocuId(max);
            de.setEtiqId(89);

            new EtiquetaDocuDao().insert(de);
        }

    }

    public void insertarArchivo(String ruta, String rutaSave) throws IOException {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        String SAVE_PATH = new ConexionReporte().obtenerCarpeta("carpeta");
        if (ruta != null) {
            try {
                File file = new File(ruta);
                try {
                    try {
                        //guardar el archivo del temporal a los repositorios del ArcDig.
                        String rootPath;

                        String rutaServerAux = null;
                        InputStream fis = new FileInputStream(file);
                        in = new BufferedInputStream(fis);

                        String rootPathOri = SAVE_PATH;

                        rutaServerAux = rootPathOri + "//" + rutaSave;

                        File theDir = new File(rutaServerAux);
                        if (!theDir.exists()) {
                            theDir.mkdirs();
                        }

                        int id = new DocumentoDao().getIdMaxDocumento() + 1;

                        File file2 = new File(rutaServerAux + "//" + id + ".pdf");

                        if (file2.isFile()) {
                            file2.delete();
                        }

                        OutputStream fout = new FileOutputStream(file2);
                        out = new BufferedOutputStream(fout);

                        byte buffer[] = new byte[245760];
                        int ch = in.read(buffer);

                        while (ch != -1) {
                            out.write(buffer, 0, ch);
                            ch = in.read(buffer);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } finally {

                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {

            }
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

}
