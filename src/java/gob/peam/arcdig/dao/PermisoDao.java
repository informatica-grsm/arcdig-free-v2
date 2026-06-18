/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.dao;

/**
 *
 * @author Cj.Legacy
 */
import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.Permiso;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class PermisoDao {

    private SqlSessionFactory sqlSessionFactory;

    public PermisoDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

     public List<Permiso> listarPermiso() {
        List<Permiso> perm;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            perm = session.selectList("Permiso.getList");
        } finally {
            session.close();
        }
        return perm;
    }
    
    public Permiso getPermisos(String dni) {
        Permiso item;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            item = session.selectOne("Permiso.getPermisos", dni);
        } finally {
            session.close();
        }
        return item;
    }

    public void insert(Permiso bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Permiso.insert", bean);
            session.commit();
        } finally {
            session.close();
        }
    }
    
     public void delete(Permiso bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Permiso.delete", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    
    
}
