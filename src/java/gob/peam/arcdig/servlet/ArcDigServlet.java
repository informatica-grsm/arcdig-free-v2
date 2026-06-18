/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.servlet;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import config.ConexionReporte;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.dao.DocumentoDao;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
@WebServlet(name = "ArcDigServlet", urlPatterns = {"/ArcDig.pdf"}, smallIcon = "")
public class ArcDigServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //SesionDao sesionDao = new SesionDao();
        //Indicamos la ruta de la imagen
        response.setContentType("application/pdf;charset=UTF-8");
        /*if (!sesionDao.verificarSesion(request, response)) {
         response.sendRedirect("./Login");
         } else {*/
        Integer id = request.getParameter("id") == null ? 0 : Integer.parseInt(request.getParameter("id"));
        Documento bean = new Documento();
        DocumentoDao dao = new DocumentoDao();
        bean = dao.getDocumento(id);
        String path = new ConexionReporte().obtenerCarpeta("carpeta");
        String carpeta = path + "//" + bean.getRuta();
        try {
            ServletContext srvcon = getServletContext();
            //String ruta = srvcon.getRealPath("/");
            File file = new File(carpeta + "//" + bean.getId() + "." + bean.getMineType().getMineExt());

            if (file.isFile()) {
                ServletOutputStream out = null;

                //FileInputStream ficheroInput = new FileInputStream(ruta + "//" + bean.getId() + ".pdf");
                //int tamanoInput = ficheroInput.available();
                //byte[] datosPDF = new byte[tamanoInput];
                //ficheroInput.read(datosPDF, 0, tamanoInput);
                //si queremos que empieze a descargar en vez de solo ver. desactivar linea de abajo.
                //response.setHeader("Content-disposition", "attachment; filename=Documento.pdf");
                //response.setContentType(bean.getMimeTypes());
                
                //out = response.getOutputStream();
                //out.write(datosPDF, 0, datosPDF.length);
                //ficheroInput.close();
                //response.sendRedirect("./" + bean.getDocuRuta() + "//" + bean.getId() + ".pdf");
                try {
                    InputStream is = new FileInputStream(carpeta + "//" + bean.getId() + "." + bean.getMineType().getMineExt());
                    // We create a reader with the InputStream
                    PdfReader reader = new PdfReader(is, null);

                    // We create an OutputStream for the new PDF
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    // Now we create the PDF
                    PdfStamper stamper = new PdfStamper(reader, baos);
                    //
                    String rutax = srvcon.getRealPath("//resources/ttf//arial.ttf");
                    BaseFont bf = BaseFont.createFont(rutax, BaseFont.CP1252, true);

                    PdfGState gs = new PdfGState();

                    gs.setFillOpacity(0.30F);

                    gs.setStrokeOpacity(0.30F);

                    for (int nPag = 1; nPag <= reader.getNumberOfPages(); nPag++) {

                        Rectangle tamPagina = reader.getPageSizeWithRotation(nPag);

                        PdfContentByte over = stamper.getOverContent(nPag);

                        over.beginText();

                        writeTextToDocument(bf, tamPagina, over, gs, "COPIA INFORMATIVA");

                        over.endText();

                    }
                    stamper.close();
                    reader.close();
                    // We write the PDF bytes to the OutputStream
                    OutputStream os = response.getOutputStream();
                    
                    baos.writeTo(os);
                    os.flush();
                    
                    os.close();
                    is.close();
                    baos.close();
                    
                } catch (DocumentException e) {
                    throw new IOException(e.getMessage());
                }

            } else {
                response.sendRedirect("./resources/reportes/nodisponible.pdf");
            }
        } catch (Exception ex) {
            response.sendRedirect("./resources/reportes/nodisponible.pdf");
            //request.getRequestDispatcher("./resources/reportes/nodisponible.pdf").forward(request, response);
           // response.getWriter().print(ex.getMessage());
        }
        // }
    }
    public String ruta = "";

    public String listarDirectorio(File f, String separador, String Origen, int indice, int ind, String[] tab) throws IOException {
        File[] ficheros = f.listFiles();

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
                        ruta = listarDirectorio(ficheros[x], nuevo_separador, Origen, indice, ind, tab);
                        ind--;
                    }
                }
            }

        }
        return ruta;
    }

    private static void writeTextToDocument(BaseFont bf, Rectangle tamPagina, PdfContentByte over, PdfGState gs, String texto) {

        over.setGState(gs);

        over.setRGBColorFill(220, 220, 220);

        over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_STROKE);

        over.setFontAndSize(bf, 46);

        float anchoDiag = (float) Math.sqrt(Math.pow((tamPagina.getHeight() - 60), 2) + Math.pow((tamPagina.getWidth() - 60), 2));

        float porc = (float) 100 * (anchoDiag / bf.getWidthPoint(texto, 46));

        over.setHorizontalScaling(porc);

        double angPage = (1) * Math.atan((tamPagina.getHeight() - 60) / (tamPagina.getWidth() - 60));

        over.setTextMatrix((float) Math.cos(angPage),
                (float) Math.sin(angPage),
                (float) ((-1F) * Math.sin(angPage)),
                (float) Math.cos(angPage),
                30F,
                10F);
        over.showText(texto);
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
    /*@Override
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException {
     processRequest(request, response);
     }*/
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
       // processRequest(request, response);
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
