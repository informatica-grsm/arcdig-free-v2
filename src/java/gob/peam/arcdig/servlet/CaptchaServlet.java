package gob.peam.arcdig.servlet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jpgprog84
 */
@WebServlet(name = "CaptchaServlet", urlPatterns = {"/captcha.jpg"})
public class CaptchaServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 5447682430717125916L;

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
    @SuppressWarnings({"unused", "rawtypes"})
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("image/jpg");
        try {

//            Color backgroundColor = Color.orange;
//            Color borderColor = Color.red;
            Color textColor = new Color(0, 0, 0); //color de la letra
            Color circleColor = new Color(255, 255, 255); //color de los circulos
            Font textFont = new Font("Arial", Font.BOLD, 16);
            int charsToPrint = 6;
            int width = request.getParameter("width") != null ? Integer.parseInt(request.getParameter("width")) : 100;
            int height = request.getParameter("height") != null ? Integer.parseInt(request.getParameter("height")) : 40;
            int circlesToDraw = 16;
            float horizMargin = 20.0f;
            float imageQuality = 0.95f; // max is 1.0 (this is for jpeg)
            double rotationRange = 0.0; // this is radians
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

            //Draw an oval
            g.setColor(new Color(123, 207, 8));

            g.fillRect(0, 0, width, height);

            // lets make some noisey circles
            g.setColor(circleColor);
            for (int i = 0; i < circlesToDraw; i++) {
                int circleRadius = (int) (Math.random() * height / 2.0);
                int circleX = (int) (Math.random() * width - circleRadius);
                int circleY = (int) (Math.random() * height - circleRadius);
                g.drawOval(circleX, circleY, circleRadius * 2, circleRadius * 2);
            }

            g.setColor(textColor);
            g.setFont(textFont);

            FontMetrics fontMetrics = g.getFontMetrics();
            int maxAdvance = fontMetrics.getMaxAdvance();
            int fontHeight = fontMetrics.getHeight();

            // i removed 1 and l and i because there are confusing to users...
            // Z, z, and N also get confusing when rotated
            // 0, O, and o are also confusing...
            // lowercase G looks a lot like a 9 so i killed it
            // this should ideally be done for every language...
            // i like controlling the characters though because it helps prevent confusion
            String elegibleChars = "1234567890";
            char[] chars = elegibleChars.toCharArray();
            float spaceForLetters = -horizMargin * 2 + width;
            float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);
            AffineTransform transform = g.getTransform();
            StringBuilder finalString = new StringBuilder();
            for (int i = 0; i < charsToPrint; i++) {
                double randomValue = Math.random();
                int randomIndex = (int) Math.round(randomValue * (chars.length - 1));
                char characterToShow = chars[randomIndex];
                finalString.append(characterToShow);
                // this is a separate canvas used for the character so that
                // we can rotate it independently
                int charImageWidth = maxAdvance * 2;
                int charImageHeight = fontHeight * 2;
                int charWidth = fontMetrics.charWidth(characterToShow);
                int charDim = Math.max(maxAdvance, fontHeight);
                int halfCharDim = (int) (charDim / 2);
                BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
                Graphics2D charGraphics = charImage.createGraphics();
                charGraphics.translate(halfCharDim, halfCharDim);
                double angle = (Math.random() - 0.5) * rotationRange;
                charGraphics.transform(AffineTransform.getRotateInstance(angle));
                charGraphics.translate(-halfCharDim, -halfCharDim);
                charGraphics.setColor(textColor);
                charGraphics.setFont(textFont);
                int charX = (int) (0.5 * charDim - 0.5 * charWidth);
                charGraphics.drawString("" + characterToShow, charX,
                        (int) ((charDim - fontMetrics.getAscent())
                        / 2 + fontMetrics.getAscent()));
                float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
                int y = (int) ((height - charDim) / 2);
//System.out.println("x=" + x + " height=" + height + " charDim=" + charDim + " y=" + y + " advance=" + maxAdvance + " fontHeight=" + fontHeight + " ascent=" + fontMetrics.getAscent());
                g.drawImage(charImage, (int) x, y, charDim, charDim, null, null);

                charGraphics.dispose();
            }
            //Write the image as a jpg
            Iterator iter = ImageIO.getImageWritersByFormatName("JPG");
            if (iter.hasNext()) {
                ImageWriter writer = (ImageWriter) iter.next();
                ImageWriteParam iwp = writer.getDefaultWriteParam();
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwp.setCompressionQuality(imageQuality);
                writer.setOutput(ImageIO.createImageOutputStream(response.getOutputStream()));
                IIOImage imageIO = new IIOImage(bufferedImage, null, null);
                writer.write(null, imageIO, iwp);
            } else {
                throw new RuntimeException("no encoder found for jsp");
            }

            // let's stick the final string in the session
            request.getSession().setAttribute("captcha", finalString.toString());
            g.dispose();
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to build image", ioe);
        }
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
