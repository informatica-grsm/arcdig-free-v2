package gob.peam.arcdig.dao;

import gob.peam.arcdig.beans.TipoMetadata;
import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.Metadata;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class MetaDataDao {

    private SqlSessionFactory sqlSessionFactory;

    public MetaDataDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

    public void insertTipoMeta(TipoMetadata bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Metadata.insertTipoMeta", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void deleteTipoMeta(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Metadata.deleteTipoMeta", id);
            session.commit();
        } finally {
            session.close();
        }
    }

    public List<TipoMetadata> listarTipoMetadata(Integer id) {
        List<TipoMetadata> items;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("Metadata.getListTipoMetada", id);
        } finally {
            session.close();
        }
        return items;
    }

    public List<Metadata> listarMetaData(HashMap hm) {
        List<Metadata> items;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("Metadata.getListMetadata", hm);
        } finally {
            session.close();
        }
        return items;
    }

    public List<Metadata> listarCamposTipoDoc(HashMap hm) {
        List<Metadata> items;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("Metadata.getListCamposTipoDoc", hm);
        } finally {
            session.close();
        }
        return items;
    }

    public List<Metadata> listarMetadata(String filtro) {
        List<Metadata> items;
        String c = "%" + filtro + "%";
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("Metadata.getList", c);

        } finally {
            session.close();
        }
        return items;
    }

    public List<Metadata> listarMetadataCombo() {
        List<Metadata> items;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("Metadata.getListMetadataCombo");
        } finally {
            session.close();
        }
        return items;
    }

    public String getTagValue(String tag, Element elemento) {
        NodeList lista = elemento.getElementsByTagName(tag).item(0).getChildNodes();
        Node valor = (Node) lista.item(0);
        return valor.getNodeValue();
    }

    public List<Metadata> getMetadata(Documento selected) {
        List<Metadata> metadata = (List<Metadata>) new ArrayList<Metadata>();
        if (selected != null) {
            if (selected.getMetaData() != null && !"".equals(selected.getMetaData())) {
                try {
                    String metaData = null;
                    metaData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + selected.getMetaData().trim();
                    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(metaData));
                    Document doc = docBuilder.parse(is);
                    doc.getDocumentElement().normalize();
                    for (int i = 0; i < doc.getElementsByTagName("docu_metadata").getLength(); i++) {
                        Node documento = doc.getElementsByTagName("docu_metadata").item(i);
                        Element elemento = (Element) documento;
                        try {
                            metadata.add(new Metadata(Integer.parseInt(getTagValue("meta_id", elemento)), getTagValue("meta_nombre", elemento).toString(), getTagValue("meta_descripcion", elemento).toString(), null));
                        } catch (NullPointerException ex) {
                            Logger.getLogger(MetaDataDao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (SAXException ex) {
                    Logger.getLogger(MetaDataDao.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MetaDataDao.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(MetaDataDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return metadata;
    }

    public List<Metadata> getMetadataTido(String selected) {
        List<Metadata> metadata = (List<Metadata>) new ArrayList<Metadata>();

        if (selected != null && !"".equals(selected)) {

            try {
                String metaData = null;
                metaData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + selected.trim();

                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(metaData));

                Document doc = docBuilder.parse(is);
                doc.getDocumentElement().normalize();

                for (int i = 0; i < doc.getElementsByTagName("docu_metadata").getLength(); i++) {
                    Node documento = doc.getElementsByTagName("docu_metadata").item(i);
                    Element elemento = (Element) documento;

                    metadata.add(new Metadata(Integer.parseInt(getTagValue("meta_id", elemento)), getTagValue("meta_nombre", elemento).toString(), getTagValue("meta_descripcion", elemento).toString(), null));
                }
            } catch (SAXException ex) {
                Logger.getLogger(MetaDataDao.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MetaDataDao.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(MetaDataDao.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return metadata;
    }

    public void insert(Metadata bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Metadata.insert", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void update(Metadata bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Metadata.update", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(Metadata bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Metadata.delete", bean);
            session.commit();
        } catch (Exception ex) {
            showNotify("NO SE PUEDE ELIMINAR PORQUE YA ESTA ASOCIADO CON OTROS REGISTROS", win);
        } finally {
            session.close();
        }
    }
    @Wire
    Window win;

    private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "warning", ref, "middle_center", 3000);
    }

    public Integer getRepetidoUpdate(Metadata bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Metadata.getRepetidoUpdate", bean);
        } finally {
            session.close();
        }
        return count;
    }

    public Integer getRepetido(Metadata bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Metadata.getRepetido", bean);
        } finally {
            session.close();
        }
        return count;
    }

    public Integer getMaxId() {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Metadata.getMaxId");
        } finally {
            session.close();
        }
        return count;
    }

    public TipoMetadata getTipoDato(HashMap hm) {
        TipoMetadata item;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            item = session.selectOne("Metadata.getTipoDato", hm);
        } finally {
            session.close();
        }
        return item;
    }

    public Integer getId(String nombre) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return session.selectOne("Metadata.getId", nombre);
        } finally {
            session.close();
        }
    }

    public String getCampo(String id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return session.selectOne("Metadata.getCampo", id);
        } finally {
            session.close();
        }
    }
}
