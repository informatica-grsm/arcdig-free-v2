/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.dao;


import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.Grupo;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author jpgprog84
 */
public class GrupoDao {

    private final SqlSessionFactory sqlSessionFactory;

    public GrupoDao() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }

    public List<Grupo> listar() {
        
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.selectList("Grupo.listar");
        } finally {
            sqlSession.close();
        }
      
    }

}
