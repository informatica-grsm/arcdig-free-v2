/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.dao;


import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.SiadoDetalle;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
/**
 *
 * @author ecr
 */
public class SiadoDetalleDao {
    
    private SqlSessionFactory sqlSessionFactory;

    public SiadoDetalleDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }
    public int insert(SiadoDetalle bean) {
        int sideId = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            //System.out.println("siado dao....");
            session.insert("SiadoDetalle.insert", bean);
             sideId = bean.getSideId();
            session.commit();
        } finally {
            System.out.println("xxx..");
            session.close();
        }

        return sideId;
    }
    
        public List<SiadoDetalle> getRelacionById(Integer id) {

        SqlSession bdData = sqlSessionFactory.openSession();
        HashMap hm = new HashMap();
        hm.put("id", id);
        try {
            return bdData.selectList("SiadoDetalle.getRelacionById", hm);
        } finally {
            bdData.close();
        }

    }

}
