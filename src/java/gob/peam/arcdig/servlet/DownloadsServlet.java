/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.servlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfWriter;
import config.ConexionReporte;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.dao.DocumentoDao;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
@WebServlet(name = "DownloadsServlet", urlPatterns = {"/Downloads.pdf"}, smallIcon = "")
public class DownloadsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer id = request.getParameter("id") == null ? 0 : Integer.parseInt(request.getParameter("id"));
        List<InputStream> pdfs_ = new ArrayList<InputStream>();
        String path = new ConexionReporte().obtenerCarpeta("carpeta");
        for (Documento item : new DocumentoDao().getAdjuntos(id)) {
            pdfs_.add(new FileInputStream(path + "//" + item.getRuta() + "//" + item.getId() + ".pdf"));
            //response.getWriter().println(path + "//" + item.getRuta() + "//" + item.getId() + ".pdf");
        }

        ServletOutputStream out = null;
        ServletContext srvcon = getServletContext(); 
        String ruta = srvcon.getRealPath("/tmp");
        OutputStream baos = new FileOutputStream(ruta+"/unionPdfs.pdf");
        
        Document document = new Document();

        try {
            List<InputStream> pdfs = pdfs_;
            List<com.itextpdf.text.pdf.PdfReader> readers = new ArrayList<com.itextpdf.text.pdf.PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                com.itextpdf.text.pdf.PdfReader pdfReader = new com.itextpdf.text.pdf.PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
           
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();
            com.itextpdf.text.pdf.PdfContentByte cb = writer.getDirectContent();

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<com.itextpdf.text.pdf.PdfReader> iteratorPDFReader = readers.iterator();

            while (iteratorPDFReader.hasNext()) {
                com.itextpdf.text.pdf.PdfReader pdfReader = iteratorPDFReader.next();

                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {

                    com.itextpdf.text.Rectangle rectangle = pdfReader.getPageSizeWithRotation(1);
                    document.setPageSize(rectangle);
                    document.newPage();

                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    switch (rectangle.getRotation()) {
                        case 0:
                            cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                            break;
                        case 90:
                            cb.addTemplate(page, 0, -1f, 1f, 0, 0, pdfReader
                                    .getPageSizeWithRotation(1).getHeight());
                            break;
                        case 180:
                            cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                            break;
                        case 270:
                            cb.addTemplate(page, 0, 1.0F, -1.0F, 0, pdfReader
                                    .getPageSizeWithRotation(1).getWidth(), 0);
                            break;
                        default:
                            break;
                    }
                    cb.beginText();
                    cb.getPdfDocument().getPageSize();
                    cb.endText();
                }

                pageOfCurrentReaderPDF = 0;
            }
            baos.flush();
            document.close();
            baos.close();
            
            response.sendRedirect("./tmp/unionPdfs.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    public String ruta = "";

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
