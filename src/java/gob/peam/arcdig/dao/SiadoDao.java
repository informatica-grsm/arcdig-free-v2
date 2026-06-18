/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.dao;

import config.ArcDigConnectionFactory;
import gob.peam.arcdig.beans.Siado;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

/**
 *
 * @author ecr
 */
public class SiadoDao {

    private SqlSessionFactory sqlSessionFactory;

    public SiadoDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

    public List<Siado> listarSiado(String filtro) {
        List<Siado> siado;
        String c = "%" + filtro + "%";
        SqlSession session = sqlSessionFactory.openSession();
        try {
            siado = session.selectList("Siado.getList", c);

        } finally {
            session.close();
        }
        return siado;
    }

    public int insert(Siado bean) {
        int siadId = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            //System.out.println("siado dao....");
            session.insert("Siado.insert", bean);
            siadId = bean.getSiadId();
            session.commit();
        } finally {
            System.out.println("xxx..");
            session.close();
        }

        return siadId;
    }

    public void update(Siado bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Siado.update", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void updateNumeros(Siado bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            //System.out.println(bean);
            session.update("Siado.updateNumeros", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(Siado bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Siado.delete", bean);
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

    public Integer getIdSiado(Integer idSiado) {
        Integer id;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            id = (Integer) session.selectOne("Siado.getIdSiado", idSiado);

        } finally {
            session.close();
        }
        return id;
    }
}
