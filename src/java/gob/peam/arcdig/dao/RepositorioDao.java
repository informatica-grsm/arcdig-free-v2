/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.dao;


import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.Repositorio;
import gob.peam.arcdig.beans.RutaRapida;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author jpgprog84
 */
public class RepositorioDao {

    private SqlSessionFactory sqlSessionFactory;

    public RepositorioDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

    public List<Repositorio> listarCarpeta(HashMap hm) {
        List<Repositorio> repo;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            repo = session.selectList("Repositorio.listarCarpetas", hm);
            return repo;
        } finally {
            session.close();
        }
    }
    
    public List<RutaRapida> listarRutas(String dni) {
        List<RutaRapida> ruta;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            ruta = session.selectList("Repositorio.listarRutas", dni);
        } finally {
            session.close();
        }
        return ruta;
    }
    
    public void insert(RutaRapida bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Repositorio.insertRuta", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    
}
