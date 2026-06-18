/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.dao;

import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.MineType;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author Cj.Legacy
 */
public class MineTypeDao {
    private SqlSessionFactory sqlSessionFactory;
    
    public MineTypeDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }
    
    @SuppressWarnings("unchecked")
    public List<MineType> listarMineTypes() {
        List<MineType> etiquetas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            etiquetas = session.selectList("MineType.getListCombo");
        } finally {
            session.close();
        }
        return etiquetas;
    }
    
    public List<MineType> listarMineType(String filtro) {
        List<MineType> cate;
         String c = "%"+filtro+"%";
        SqlSession session = sqlSessionFactory.openSession();
        try {
            cate = session.selectList("MineType.getList", c);

        } finally {
            session.close();
        }
        return cate;
    }

    public void insert(MineType bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("MineType.insert", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void update(MineType bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("MineType.update", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(MineType bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("MineType.delete", bean);
            session.commit();
        } finally {
            session.close();
        }
    }
    
    public Integer getRepetidoUpdate(MineType bean) {
       Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("MineType.getRepetidoUpdate", bean);
        } finally {
            session.close();
        }
         return count;
    }
    
    public Integer getRepetido(MineType bean) {
       Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("MineType.getRepetido", bean);
        } finally {
            session.close();
        }
         return count;
    }

}
