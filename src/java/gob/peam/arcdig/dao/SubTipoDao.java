package gob.peam.arcdig.dao;

import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.SubTipo;
import gob.peam.arcdig.beans.SubModulo;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class SubTipoDao {

    private SqlSessionFactory sqlSessionFactory;

    public SubTipoDao() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }

    public List<SubTipo> listarSubTipo(String query, Integer tidoId) {
        HashMap hm = new HashMap();
        hm.put("c",query);
        hm.put("tidoId", tidoId);
        List<SubTipo> etiquetas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            etiquetas = session.selectList("TipoDocumento.listarSubTipo", hm);
            return etiquetas;
        } finally {
            session.close();
        }
    }

 

    public SubTipo insertarSubTipo(SubTipo etiqueta) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("TipoDocumento.insertSubTipo", etiqueta);
            session.commit();
        } finally {
            session.close();
        }
        return etiqueta;
    }
    
      public SubTipo updateSubTipo(SubTipo etiqueta) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("TipoDocumento.updateSubTipo", etiqueta);
            session.commit();
        } finally {
            session.close();
        }
        return etiqueta;
    }

    public boolean existeSubTipo(String descripcion) {
        boolean existe = false;
        List<SubTipo> etiquetas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            etiquetas = session.selectList("SubTipo.existeSubTipo", descripcion);
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

    public boolean actualizarSubTipo(SubTipo etiqueta) {
        boolean result = false;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("SubTipo.updateSubTipo", etiqueta);
            session.commit();
            result = true;
        } finally {
            session.close();
        }
        return result;
    }

    public SubTipo buscarSubTipo(String descripcion) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            SubTipo etiqueta = session.selectOne("SubTipo.buscarSubTipoPorDescripcion", descripcion);
            return etiqueta;
        } finally {
            session.close();
        }
    }

    public boolean tieneDependencias(Integer id) {
        boolean resul;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            List<SubModulo> subModulos = session.selectList("SubTipo.childrenSubTipo", id);
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

    public boolean eliminarSubTipo(Integer id) {
        boolean resul = false;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("TipoDocumento.deleteSubTipo", id);
            session.commit();
            resul = true;
        } finally {
            session.close();
        }
        return resul;
    }

    public List<SubTipo> listarMenusPorUsuarioyModulo(HashMap hm) {
        List<SubTipo> etiquetas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            etiquetas = session.selectList("SubTipo.buscarMenusPorUsuarioyModulo", hm);
            return etiquetas;
        } finally {
            session.close();
        }
    }

}
