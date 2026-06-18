package config;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.ibatis.io.Resources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ConexionReporte {

    static String driver = "", url = "", username = "", password = "";
    static Connection conn = null;

    public static Connection AbrirConexion() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException {
        //ParametrosConexionXml(); /************* desde el xml ******************/
        ParametrosConexionProperty();
        /**
         * ********* desde .property *************
         */
        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);

        return conn;
    }

    public static void CerrarConexion() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionReporte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void ParametrosConexionProperty() throws IOException {
        driver = obtenerPropiedad("driver");
        url = obtenerPropiedad("url");
        username = obtenerPropiedad("user");
        password = obtenerPropiedad("password");
    }

    public static String obtenerPropiedad(String nombre) throws IOException {
        Reader reader = null;
        Properties propiedades = new Properties();
        reader = Resources.getResourceAsReader("config/ConexionReporte.properties");
        propiedades.load(reader);
        reader.close();
        return propiedades.getProperty(nombre);
    }

    public String obtenerCarpeta(String nombre) throws IOException {
        Reader reader = null;
        Properties propiedades = new Properties();
        reader = Resources.getResourceAsReader("config/ConexionReporte.properties");
        propiedades.load(reader);
        reader.close();
        return propiedades.getProperty(nombre);
    }

    public static void ParametrosConexionXml() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Reader reader = null;
        reader = Resources.getResourceAsReader("config/ArcDigSql.xml");

        Document dom = documentBuilder.parse(new InputSource(reader));
        org.w3c.dom.Element rootElement = dom.getDocumentElement();
        org.w3c.dom.NodeList nodosConexion = rootElement.getElementsByTagName("dataSource");

        for (int i = 0; i < nodosConexion.getLength(); i++) {
            org.w3c.dom.Element element = (Element) nodosConexion.item(i);
            org.w3c.dom.NodeList nodeList2 = element.getElementsByTagName("property");

            for (int j = 0; j < nodeList2.getLength(); j++) {
                org.w3c.dom.Element el = (Element) nodeList2.item(j);
                if (el.getAttribute("name").equals("driver")) {
                    driver = el.getAttribute("value");
                }
            }
            for (int j = 0; j < nodeList2.getLength(); j++) {
                org.w3c.dom.Element el = (Element) nodeList2.item(j);
                if (el.getAttribute("name").equals("url")) {
                    url = el.getAttribute("value");
                }
            }
            for (int j = 0; j < nodeList2.getLength(); j++) {
                org.w3c.dom.Element el = (Element) nodeList2.item(j);
                if (el.getAttribute("name").equals("username")) {
                    username = el.getAttribute("value");
                }
            }
            for (int j = 0; j < nodeList2.getLength(); j++) {
                org.w3c.dom.Element el = (Element) nodeList2.item(j);
                if (el.getAttribute("name").equals("password")) {
                    password = el.getAttribute("value");
                }
            }
        }

    }

    // metodo auxiliar para obtener el valor a partir del nombre del nodo
    public static String getTagValue(String tag, Element elemento) {
        NodeList lista = elemento.getElementsByTagName(tag).item(0).getChildNodes();
        Node valor = (Node) lista.item(0);
        return valor.getNodeValue();
    }
}
