/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.utils;

import gob.peam.arcdig.beans.Sesion;
import gob.peam.arcdig.dao.SesionDao;
import java.sql.Date;
import java.sql.Time;
import org.zkoss.zk.ui.http.HttpSessionListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Cj.Legacy
 */
public class SessionCleaner extends HttpSessionListener {

    

   private static Map<String, Object> MapOfVeryBigDataPerSession = new HashMap<String, Object>();

    // called while session created
 
    @Override
    public void sessionCreated(HttpSessionEvent evt) {
        super.sessionCreated(evt);
        HttpSession session = evt.getSession();
        //System.out.println(" session created " + session.getId());
        MapOfVeryBigDataPerSession.put(session.getId(), session.getId());
        //System.out.println(" put data into MapOfVeryBigDataPerSession, size = " + MapOfVeryBigDataPerSession.size());
        //System.out.println();

    }

    // called while session destroy

    @Override
    public void sessionDestroyed(HttpSessionEvent evt) {
        super.sessionDestroyed(evt);
        try {
            HttpSession session = evt.getSession();
            
            SesionDao dao = new SesionDao();
            Sesion s = dao.buscarSesion(session.getId());
            s.setSesiFechaSalida(new java.util.Date(new java.util.Date().getTime()));
            s.setSesiEstado(false);
            dao.actualizarSesion(s);
            MapOfVeryBigDataPerSession.remove(session.getId());
        } catch (NullPointerException ex) {
            
        }
        //System.out.println(" remove data from MapOfVeryBigDataPerSession, size = " + MapOfVeryBigDataPerSession.size());
        //System.out.println();
    }
}
