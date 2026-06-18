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
import gob.peam.arcdig.beans.Notificacion;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class NotificacionDao {

    private SqlSessionFactory sqlSessionFactory;

    public NotificacionDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

    public List<Notificacion> listarNotificacions() {
        List<Notificacion> cate;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            cate = session.selectList("Notificacion.getListCombo");

        } finally {
            session.close();
        }
        return cate;
    }

    public List<Notificacion> listarNotificacion(String filtro) {
        List<Notificacion> cate;
        String c = "%" + filtro + "%";
        SqlSession session = sqlSessionFactory.openSession();
        try {
            cate = session.selectList("Notificacion.getList", c);

        } finally {
            session.close();
        }
        return cate;
    }

    public void insert(Notificacion bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            
            session.insert("Notificacion.insert", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void update(Notificacion bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Notificacion.update", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(Notificacion bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Notificacion.limpiar", bean);
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

    public Integer getRepetido(Notificacion bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Notificacion.getRepetido", bean);
        } finally {
            session.close();
        }
        return count;
    }

    public Integer getRepetidoUpdate(Notificacion bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Notificacion.getRepetidoUpdate", bean);
        } finally {
            session.close();
        }
        return count;
    }

}
