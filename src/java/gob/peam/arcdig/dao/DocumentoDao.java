/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.dao;

import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.DocumentoUsuario;
import gob.peam.arcdig.beans.JSONOscinfor;
import gob.peam.arcdig.beans.Resol;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author Cj.Legacy
 */
public class DocumentoDao {

    private SqlSessionFactory sqlSessionFactory;

    public DocumentoDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

    public List<Documento> filtroDoc(String filtro) {
        List<Documento> doc;
        String c = "%" + filtro + "%";
        SqlSession session = sqlSessionFactory.openSession();
        try {
            doc = session.selectList("Documento.getList", c);

        } finally {
            session.close();
        }
        return doc;
    }

    @SuppressWarnings("unchecked")
    public List<Documento> listarDocumentos(HashMap hm
    ) {
        List<Documento> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getListDocumentos", hm);
        } finally {
            session.close();
        }
        return documentos;
    }

    @SuppressWarnings("unchecked")
    public List<Documento> getAdjuntos(Integer id
    ) {
        HashMap hm = new HashMap();
        hm.put("docuGroup", id);
        List<Documento> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getAdjuntos", hm);
        } finally {
            session.close();
        }
        return documentos;
    }
    
    @SuppressWarnings("unchecked")
    public List<Documento> getRelacionSubGrupo(Integer subgrupo, Integer id) {
        HashMap hm = new HashMap();
        hm.put("docuGroup", id);
        hm.put("subGroup", subgrupo);
        List<Documento> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getRelacionSubGrupo", hm);
        } finally {
            session.close();
        }
        return documentos;
    }
    
    @SuppressWarnings("unchecked")
    public List<Documento> getRelacionSubGrupo1(Integer subgrupo, Integer id) {
        HashMap hm = new HashMap();
        hm.put("docuGroup", id);
        hm.put("subGroup", subgrupo);
        List<Documento> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getRelacionSubGrupo1", hm);
        } finally {
            session.close();
        }
        return documentos;
    }
    
     @SuppressWarnings("unchecked")
    public List<Documento> getRelacionSubGrupo2(Integer subgrupo, Integer id) {
        HashMap hm = new HashMap();
        hm.put("docuGroup", id);
        hm.put("subGroup", subgrupo);
        List<Documento> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getRelacionSubGrupo2", hm);
        } finally {
            session.close();
        }
        return documentos;
    }

    public List<Resol> listarResol() {
        List<Resol> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getListResol");
        } finally {
            session.close();
        }
        return documentos;
    }

    @SuppressWarnings("unchecked")
    public List<Documento> listarDocumentosXCatalogo(HashMap hm) {
        List<Documento> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getListDocumentosXCatatalogo", hm);
        } finally {
            session.close();
        }
        return documentos;
    }

    @SuppressWarnings("unchecked")
    public List<Documento> listarArchivos(HashMap hm) {
        List<Documento> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getListArchivos", hm);
        } finally {
            session.close();
        }
        return documentos;
    }

    @SuppressWarnings("unchecked")
    public Integer listarArchivosTotal(HashMap hm) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            return (int) session.selectOne("Documento.getListArchivosTotal", hm);
        } finally {
            session.close();
        }

    }

    @SuppressWarnings("unchecked")
    public List<Documento> listarArchivos1(HashMap hm) {
        List<Documento> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getListArchivos1", hm);
        } finally {
            session.close();
        }
        return documentos;
    }

    @SuppressWarnings("unchecked")
    public List<Documento> listarDocs(HashMap hm) {
        List<Documento> documentos;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documentos = session.selectList("Documento.getListDocs", hm);
        } finally {
            session.close();
        }
        return documentos;
    }

    @SuppressWarnings("unchecked")
    public Documento getLastOne(HashMap hm) {
        Documento documento;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documento = session.selectOne("Documento.getListDocumentos", hm);
        } finally {
            session.close();
        }
        return documento;
    }

    @SuppressWarnings("unchecked")
    public Documento getDocumento(Integer id) {
        Documento documento;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documento = session.selectOne("Documento.getDocumento", id);
        } finally {
            session.close();
        }
        return documento;
    }

    @SuppressWarnings("unchecked")
    public Documento getOne(Integer id) {
        Documento documento;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            documento = session.selectOne("Documento.getOne", id);
        } finally {
            session.close();
        }
        return documento;
    }

    public int getIdMaxDocumento() {
        int max = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            max = session.selectOne("Documento.getMaxId");
        } finally {
            session.close();
        }
        return max;
    }

    public int insert(Documento bean, Integer groupId) {
        SqlSession session = sqlSessionFactory.openSession();
        Integer id = 0;
        try {
            bean.setDocuGroup(groupId);
            session.insert("Documento.insert", bean);
            session.commit();
            //log
            String dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
            id = session.selectOne("Documento.getMaxId");
            HashMap hm = new HashMap();
            hm.put("logTabla", "documento");
            hm.put("logDni", dni);
            hm.put("logDescripcion", "SE INSERTÓ UN NUEVO REGISTRO");
            hm.put("logTablaId", id);
            session.insert("Documento.insertLog", hm);
            session.commit();
            //update tosvector
            session.update("Documento.updateSearch", id);
            session.commit();
        } finally {
            session.close();
        }
        return id;
    }

    public int insertLog(JSONOscinfor bean) {
        SqlSession session = sqlSessionFactory.openSession();
        Integer id = 0;
        try {
            session.insert("Documento.insertOscinfor", bean);
            session.commit();
            return id = session.selectOne("Documento.getMaxOscinforId");
        } finally {
            session.close();
        }
    }

    public int insertDoc(Documento bean) {
        SqlSession session = sqlSessionFactory.openSession();
        Integer id = 0;
        try {
            session.insert("Documento.insert", bean);
            session.commit();
            //log
            String dni = "44429462";
            id = session.selectOne("Documento.getMaxId");
            HashMap hm = new HashMap();
            hm.put("logTabla", "documento");
            hm.put("logDni", dni);
            hm.put("logDescripcion", "SE INSERTÓ UN NUEVO REGISTRO");
            hm.put("logTablaId", id);
            session.insert("Documento.insertLog", hm);
            session.commit();
            //update tosvector
            session.update("Documento.updateSearch", id);
            session.commit();

        } finally {
            session.close();
        }
        return id;
    }

    public void insertPersonaDocumento(DocumentoUsuario bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Documento.insertPersonalDocumento", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void update(Documento bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Documento.update", bean);
            session.commit();
            //log
            String dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
            HashMap hm = new HashMap();
            hm.put("logTabla", "documento");
            hm.put("logDni", dni);
            hm.put("logDescripcion", "SE MODIFICÓ UN REGISTRO");
            hm.put("logTablaId", bean.getId());
            session.insert("Documento.insertLog", hm);
            session.commit();
            //update tosvector
            Integer id = bean.getId();
            session.update("Documento.updateSearch", id);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(Documento bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Documento.delete", bean);
            session.commit();
            //log
            String dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
            HashMap hm = new HashMap();
            hm.put("logTabla", "documento");
            hm.put("logDni", dni);
            hm.put("logDescripcion", "SE ELIMINÓ UN REGISTRO");
            hm.put("logTablaId", bean.getId());
            session.insert("Documento.insertLog", hm);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void deletePropiedad(Documento bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Documento.deletePropiedad", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void updateRuta(HashMap hm) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Documento.updateRuta", hm);
            session.commit();
            //log

            /* HashMap hmx = new HashMap();
             hmx.put("logTabla", "documento");
             hmx.put("logDni", hm.get("dni"));
             hmx.put("logDescripcion", "SE MOVIÓ UN ARCHIVO A LA RUTA: "+ hm.get("ruta"));
             hmx.put("logTablaId", hm.get("id"));
             session.insert("Documento.insertLog", hm);
             session.commit();*/
        } finally {
            session.close();
        }
    }

    public void insertReport1(HashMap hm) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Documento.insertReport1", hm);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void truncateReport1() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Documento.truncateReport1");
            session.commit();
        } finally {
            session.close();
        }
    }

    public List<Documento> listaBoletas() {
        SqlSession bdData = sqlSessionFactory.openSession();
        try {
            return bdData.selectList("Documento.listaBoletas");
        } finally {
            bdData.close();
        }
    }

    public void updateRuta1(HashMap hm) {
        SqlSession bdData = sqlSessionFactory.openSession();
        try {
            bdData.update("Documento.updateRuta1", hm);
            bdData.commit();
        } finally {
            bdData.close();
        }

    }

    public int getRelacion(Integer groupId) {

        SqlSession bdData = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("groupId", groupId);
        try {
            return bdData.selectOne("Documento.getRelacion", hm);
        } finally {
            bdData.close();
        }

    }

    public List<Documento> getRelacionById(Integer id) {

        SqlSession bdData = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("id", id);
        try {
            return bdData.selectList("Documento.getRelacionById", hm);
        } finally {
            bdData.close();
        }

    }

    public void adjuntar(Integer id, Integer grupo) {
        SqlSession bdData = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("grupo", grupo);
        hm.put("id", id);
        try {
            bdData.update("Documento.adjuntar", hm);
            bdData.commit();
        } finally {
            bdData.close();
        }
    }

    public void removeGroup(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("id", id);
        hm.put("group", id);
        try {
            session.delete("Documento.removeGroup", hm);
            session.commit();
            //log
            String dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();

            hm.put("logTabla", "documento");
            hm.put("logDni", dni);
            hm.put("logDescripcion", "SE REMOVIO DEL GRUPO");
            hm.put("logTablaId", id);
            session.insert("Documento.insertLog", hm);
            session.commit();
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void anexoDown(Documento doc, Integer orden) {
        doc.setOrden(orden);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Documento.anexoDown", doc);
            session.commit();
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void anexoUp(Documento doc, Integer orden) {
        doc.setOrden(orden);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Documento.anexoUp", doc);
            session.commit();
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void updateSolo(Integer id, Boolean solo) {
        SqlSession session = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("id", id);
        hm.put("solo", solo);

        try {
            session.update("Documento.updateSolo", hm);
            session.commit();
        } finally {
            session.close();
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public void updateSubgrupo(Integer id, Integer subgrupo) {
        SqlSession session = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("id", id);
        hm.put("subgrupo", subgrupo);

        try {
            session.update("Documento.updateSubgrupo", hm);
            session.commit();
        } finally {
            session.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    public void updateSubgrupo1(Integer id, Integer subgrupo) {
        SqlSession session = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("id", id);
        hm.put("subgrupo", subgrupo);

        try {
            session.update("Documento.updateSubgrupo1", hm);
            session.commit();
        } finally {
            session.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    public void updateSubgrupo2(Integer id, Integer subgrupo) {
        SqlSession session = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("id", id);
        hm.put("subgrupo", subgrupo);

        try {
            session.update("Documento.updateSubgrupo2", hm);
            session.commit();
        } finally {
            session.close();
        }
    }
}
