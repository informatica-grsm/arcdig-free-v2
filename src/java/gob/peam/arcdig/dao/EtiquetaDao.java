package gob.peam.arcdig.dao;

import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.Etiqueta;
import gob.peam.arcdig.beans.SubModulo;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class EtiquetaDao {

    private SqlSessionFactory sqlSessionFactory;

    public EtiquetaDao() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }

    public List<Etiqueta> listarEtiqueta() {
        List<Etiqueta> etiquetas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            etiquetas = session.selectList("Etiqueta.seleccionarEtiqueta");
            return etiquetas;
        } finally {
            session.close();
        }
    }

 

    public Etiqueta insertarEtiqueta(Etiqueta etiqueta) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Etiqueta.insertEtiqueta", etiqueta);
            session.commit();
        } finally {
            session.close();
        }
        return etiqueta;
    }

    public boolean existeEtiqueta(String descripcion) {
        boolean existe = false;
        List<Etiqueta> etiquetas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            etiquetas = session.selectList("Etiqueta.existeEtiqueta", descripcion);
            if (etiquetas.size() > 0) {
                existe = true;
            } else {
                existe = false;
            }
        } finally {
            session.close();
        }
        return existe;
    }

    public boolean actualizarEtiqueta(Etiqueta etiqueta) {
        boolean result = false;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Etiqueta.updateEtiqueta", etiqueta);
            session.commit();
            result = true;
        } finally {
            session.close();
        }
        return result;
    }

    public Etiqueta buscarEtiqueta(String descripcion) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Etiqueta etiqueta = session.selectOne("Etiqueta.buscarEtiquetaPorDescripcion", descripcion);
            return etiqueta;
        } finally {
            session.close();
        }
    }

    public boolean tieneDependencias(Integer id) {
        boolean resul;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            List<SubModulo> subModulos = session.selectList("Etiqueta.childrenEtiqueta", id);
            if (subModulos.size() > 0) {
                resul = true;
            } else {
                resul = false;
            }
            return resul;
        } finally {
            session.close();
        }
    }

    public boolean eliminarEtiqueta(Integer id) {
        boolean resul = false;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Etiqueta.deleteEtiqueta", id);
            session.commit();
            resul = true;
        } finally {
            session.close();
        }
        return resul;
    }

    public List<Etiqueta> listarMenusPorUsuarioyModulo(HashMap hm) {
        List<Etiqueta> etiquetas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            etiquetas = session.selectList("Etiqueta.buscarMenusPorUsuarioyModulo", hm);
            return etiquetas;
        } finally {
            session.close();
        }
    }

}
