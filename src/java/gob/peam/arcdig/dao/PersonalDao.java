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
import config.PersonalConnectionFactory;

import gob.peam.arcdig.beans.Personal;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class PersonalDao {

    private SqlSessionFactory sqlSessionFactory;
    
    public PersonalDao() {
        sqlSessionFactory = PersonalConnectionFactory.getSqlSessionFactory();
    }

    public List<Personal> listarPersonas(HashMap hm) {
        List<Personal> personas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            personas = session.selectList("Personal.getList", hm);
           
        } finally {
            session.close();
        }
         return personas;
    }
    
    public String getCorreo(String dni) {
        String c;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            c = session.selectOne("Personal.getCorreo", dni);
        } finally {
            session.close();
        }
         return c;
    }
    
    

    public  List<Personal> getAll() {
        HashMap hm = new HashMap();
        hm.put("c", "%%");
        return listarPersonas(hm);
    }

}
