/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.dao;

import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.Sesion;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author jpgprog84
 */
public class SesionDao {

    private SqlSessionFactory sqlSessionFactory;

    public SesionDao() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }

    public Boolean insertarSesion(Sesion sesion, String url, String id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Sesion.insertSesion", sesion);
            session.commit();

        } finally {
            session.close();
            if (url != null && id != null ) {
                Executions.sendRedirect("./zul?url=" + url + "&id=" + id);
            } else {
                Executions.sendRedirect("./zul?url=inicio");
            }
        }
        return true;
    }

    public boolean actualizarSesion(Sesion sesion) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Sesion.updateSesion", sesion);
            session.commit();

        } finally {
            session.close();
        }
        return true;
    }

    public Sesion buscarSesion(String sesiKey) {
        Sesion sesion;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sesion = session.selectOne("Sesion.buscarSesion", sesiKey);

        } finally {
            session.close();
        }
        return sesion;
    }

    public List<Sesion> listarSesionesActivas(HashMap hm) {
        List<Sesion> list;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Sesion.listarSesionesActivas", hm);

        } finally {
            session.close();
        }
        return list;
    }

    public Integer listarTotalSesiones(HashMap hm) {
        List<Sesion> list;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Sesion.listarTotalSesiones", hm);
        } finally {
            session.close();
        }
        return list.size();
    }

    public boolean verificarSesion(HttpServletRequest request, HttpServletResponse response) {

        Sesion s = buscarSesion(request.getSession().getId());
        if (s == null) {
            return false;
        } else if (s.isSesiEstado()) {
            return true;
        } else {
            request.getSession().invalidate();
            request.getSession(true);
            return false;
        }

    }
}
