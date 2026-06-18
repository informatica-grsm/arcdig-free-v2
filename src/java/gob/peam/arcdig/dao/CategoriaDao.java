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
import gob.peam.arcdig.beans.Categoria;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class CategoriaDao {

    private SqlSessionFactory sqlSessionFactory;

    public CategoriaDao() {
        sqlSessionFactory = ArcDigConnectionFactory.getSqlSessionFactory();
    }

    public List<Categoria> listarCategorias() {
        List<Categoria> cate;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            cate = session.selectList("Categoria.getListCombo");

        } finally {
            session.close();
        }
        return cate;
    }

    public List<Categoria> listarCategoria(String filtro) {
        List<Categoria> cate;
        String c = "%" + filtro + "%";
        SqlSession session = sqlSessionFactory.openSession();
        try {
            cate = session.selectList("Categoria.getList", c);

        } finally {
            session.close();
        }
        return cate;
    }

    public void insert(Categoria bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Categoria.insert", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void update(Categoria bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Categoria.update", bean);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(Categoria bean) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Categoria.delete", bean);
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

    public Integer getRepetido(Categoria bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Categoria.getRepetido", bean);
        } finally {
            session.close();
        }
        return count;
    }

    public Integer getRepetidoUpdate(Categoria bean) {
        Integer count;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            count = (Integer) session.selectOne("Categoria.getRepetidoUpdate", bean);
        } finally {
            session.close();
        }
        return count;
    }

}
