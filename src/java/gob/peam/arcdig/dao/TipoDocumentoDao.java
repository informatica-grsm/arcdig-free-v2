package gob.peam.arcdig.dao;

import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.SubTipo;
import gob.peam.arcdig.beans.TipoDocumento;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class TipoDocumentoDao {

    private SqlSessionFactory sqlSessionFactory;

    public TipoDocumentoDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

    public List<TipoDocumento> listarTipoDocumento() {
        List<TipoDocumento> items;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("TipoDocumento.getListCombo");
        } finally {
            session.close();
        }
        return items;
    }

    public TipoDocumento get(Integer id) {
        TipoDocumento item;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            item = session.selectOne("TipoDocumento.get", id);
        } finally {
            session.close();
        }
        return item;
    }

    public List<TipoDocumento> listarTipoDocumentoMetaData() {
        List<TipoDocumento> items;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("TipoDocumento.getListMeta");
        } finally {
            session.close();
        }
        return items;
    }

    public List<TipoDocumento> listarTipoDocumento(String filtro) {
        List<TipoDocumento> tido;
        String c = "%" + filtro + "%";
        SqlSession session = sqlSessionFactory.openSession();
        try {
            tido = session.selectList("TipoDocumento.getList", c);
        } finally {
            session.close();
        }
        return tido;
    }

    public List<TipoDocumento> listarTipoDocumentoCateg(String query) {
        SqlSession session = sqlSessionFactory.openSession();
        List<TipoDocumento> list;
        HashMap hm = new HashMap();
        hm.put("c", query);
        try {
            list = session.selectList("TipoDocumento.getListComboCateg", hm);
        } finally {
            session.close();
        }
        return list;
    }  
    
    public List<SubTipo> subTipoDocumento(Integer tidoId) {
        SqlSession session = sqlSessionFactory.openSession();
        List<SubTipo> list;
        HashMap hm = new HashMap();
        hm.put("tidoId", tidoId);
        hm.put("c", "");
        try {
            list = session.selectList("TipoDocumento.listarSubTipo", hm);
        } finally {
            session.close();
        }
        return list;
    }  

    public void insert(TipoDocumento bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("TipoDocumento.insert", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void update(TipoDocumento bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("TipoDocumento.update", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(TipoDocumento bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("TipoDocumento.delete", bean);
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

    public Integer getRepetido(TipoDocumento bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("TipoDocumento.getRepetido", bean);
        } finally {
            session.close();
        }
        return count;
    }

    public Integer getMaxId() {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("TipoDocumento.getMaxId");
        } finally {
            session.close();
        }
        return count;
    }

    public Integer getRepetidoUpdate(TipoDocumento bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("TipoDocumento.getRepetidoUpdate", bean);
        } finally {
            session.close();
        }
        return count;
    }

}
