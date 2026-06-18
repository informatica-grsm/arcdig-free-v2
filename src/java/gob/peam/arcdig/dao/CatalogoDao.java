package gob.peam.arcdig.dao;

import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.Carpeta;
import gob.peam.arcdig.beans.Catalogo;
import gob.peam.arcdig.beans.CatalogoUsuario;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.postgresql.util.PSQLException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class CatalogoDao {

    private SqlSessionFactory sqlSessionFactory;

    public CatalogoDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

    public List<Catalogo> listarUsuarioCatalogo(Integer usuaId) {
        List<Catalogo> catalogo;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            catalogo = session.selectList("Catalogo.getUsuaCatalogo", usuaId);

        } finally {
            session.close();
        }
        return catalogo;
    }

    public List<CatalogoUsuario> listarCatalogoUsuario(Catalogo bean) {
        List<CatalogoUsuario> cu;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            cu = session.selectList("Catalogo.getListCatalogoUsuario", bean);
        } finally {
            session.close();
        }
        return cu;
    }

    public List<Catalogo> listarCatalogo(String filtro) {
        List<Catalogo> catalogo;
        String c = "%" + filtro + "%";
        SqlSession session = sqlSessionFactory.openSession();
        try {
            catalogo = session.selectList("Catalogo.getListCatalogo", c);
        } finally {
            session.close();
        }
        return catalogo;
    }

    public List<Catalogo> listarComboCatalogo() {
        List<Catalogo> catalogo;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            catalogo = session.selectList("Catalogo.getListComboCatalogo");
        } finally {
            session.close();
        }
        return catalogo;
    }

    public Integer getIdCatalogo(String catalogo) {
        Integer id;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            id = (Integer) session.selectOne("Catalogo.getIdCatalogo", catalogo);

        } finally {
            session.close();
        }
        return id;
    }

    public Integer getRepetido(Catalogo catalogo) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Catalogo.getRepetido", catalogo);
        } finally {
            session.close();
        }
        return count;
    }
    
    
    public Integer getCatalogo(Catalogo catalogo) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Catalogo.getCatalogo", catalogo);
        } finally {
            session.close();
        }
        return count;
    }
    
    public Carpeta getCarpeta(String nombre) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return (Carpeta) session.selectOne("Catalogo.getCarpeta", nombre);
        } finally {
            session.close();
        }
    }

    public Integer getRepetidoUpdate(Catalogo catalogo) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Catalogo.getRepetidoUpdate", catalogo);
        } finally {
            session.close();
        }
        return count;
    }

    public void insert(Catalogo bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Catalogo.insert", bean);
            session.commit();
        } finally {
            session.close();
        }
    }
    
    public void insertCarpeta(Carpeta bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Catalogo.insertCarpeta", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void update(Catalogo bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Catalogo.update", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(Catalogo bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Catalogo.delete", bean);
            session.commit();
        } catch (Exception ex) {
            showNotify("NO SE PUEDE ELIMINAR PORQUE YA ESTA ASOCIADO CON OTROS REGISTROS", win);
        } finally {
            session.close();
        }
    }
    
    public void deleteCarpeta(String nombre) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Catalogo.deleteCarpeta", nombre);
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

    public void deleteUsuario(Catalogo bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Catalogo.deleteUsuario", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void insertUsuario(CatalogoUsuario bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Catalogo.insertUsuario", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

}
