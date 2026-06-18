package gob.peam.arcdig.dao;

import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.DocumentoEtiqueta;
import gob.peam.arcdig.beans.EtiquetaDocu;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class EtiquetaDocuDao {

    private SqlSessionFactory sqlSessionFactory;

    public EtiquetaDocuDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

    public List<EtiquetaDocu> listarEtiquetaDocus(HashMap hm) {
        List<EtiquetaDocu> items;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("EtiquetaDocu.listarEtiquetaDocu", hm);
        } finally {
            session.close();
        }
        return items;
    }

    public EtiquetaDocu getEtiqueta(Integer id) {
        EtiquetaDocu etiqueta;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            etiqueta = session.selectOne("EtiquetaDocu.getEtiquetaDocu", id);
            return etiqueta;
        } finally {
            session.close();
        }
    }

    public void insert(DocumentoEtiqueta bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("EtiquetaDocu.insert", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("EtiquetaDocu.delete", id);
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

    public List<EtiquetaDocu> getAll() {
        HashMap hm = new HashMap();
        hm.put("c", "%%");
        return listarEtiquetaDocus(hm);
    }

    public List<EtiquetaDocu> listarEtiquetaDocu(String filtro) {
        List<EtiquetaDocu> eti;
        String c = "'%" + filtro + "%'";
        SqlSession session = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("c", c);
        try {
            eti = session.selectList("EtiquetaDocu.getList", hm);
        } finally {
            session.close();
        }
        return eti;
    }

    public List<EtiquetaDocu> getListTipo(String query) {
        List<EtiquetaDocu> eti;
        SqlSession session = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("c", query);
        try {
            eti = session.selectList("EtiquetaDocu.getListTipo", hm);
        } finally {
            session.close();
        }
        return eti;
    }

    public List<EtiquetaDocu> getEtiqTipo(String filtro) {
        List<EtiquetaDocu> eti;
        String c = "'%" + filtro + "%'";
        SqlSession session = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("c", c);
        try {
            eti = session.selectList("EtiquetaDocu.getEtiqTipo", hm);
        } finally {
            session.close();
        }
        return eti;
    }

    public void insert(EtiquetaDocu bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("EtiquetaDocu.insertEtiqueta", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void update(EtiquetaDocu bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("EtiquetaDocu.update", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public EtiquetaDocu getTipo(String query) {
        SqlSession session = sqlSessionFactory.openSession();
        EtiquetaDocu datos = new EtiquetaDocu();
        HashMap hm = new HashMap();
        hm.put("c", query);
        try {
            datos = session.selectOne("EtiquetaDocu.getTipo", hm);
        } finally {
            session.close();
        }
        return datos;
    }

    public void delete(EtiquetaDocu bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("EtiquetaDocu.deleteEtiqueta", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public Integer getRepetidoUpdate(EtiquetaDocu bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("EtiquetaDocu.getRepetidoUpdate", bean);
        } finally {
            session.close();
        }
        return count;
    }

    public Integer getRepetido(EtiquetaDocu bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("EtiquetaDocu.getRepetido", bean);
        } finally {
            session.close();
        }
        return count;
    }

}
