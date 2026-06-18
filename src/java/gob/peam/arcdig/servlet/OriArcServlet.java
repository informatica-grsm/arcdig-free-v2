/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.servlet;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.dao.DocumentoDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alabajos
 */
@WebServlet(name = "OriArcServlet", urlPatterns = {"/OriArc.pdf"},  smallIcon = "")
public class OriArcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
        Integer id = request.getParameter("id") == null ? 0 : Integer.parseInt(request.getParameter("id"));
        DocumentoDao dao = new DocumentoDao();
        Documento bean = dao.getDocumento(id);
        String path = new ConexionReporte().obtenerCarpeta("carpeta");
        String carpeta = path+"//" + bean.getRuta();
        try {
            File file = new File(carpeta + "//" + bean.getId() + "." + bean.getMineType().getMineExt());

            if (file.isFile()) {
                ServletOutputStream out = null;
                FileInputStream ficheroInput = new FileInputStream(carpeta + "//" + bean.getId() + "." + bean.getMineType().getMineExt());
                int tamanoInput = ficheroInput.available();
                byte[] datosPDF = new byte[tamanoInput];
                ficheroInput.read(datosPDF, 0, tamanoInput);
                response.setContentType(bean.getMimeTypes());
                response.setContentLength(datosPDF.length);
                out = response.getOutputStream();
                out.write(datosPDF, 0, datosPDF.length);
                ficheroInput.close();
                out.close();
                    
                
            } else {
                    response.sendRedirect("./resources/reportes/nodisponible.pdf");
                    
            }
        } catch (Exception ex) {
            response.sendRedirect("./resources/reportes/nodisponible.pdf");
        }
    }
    public String ruta = "";
}
