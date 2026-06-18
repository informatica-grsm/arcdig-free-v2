/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.Notificacion;

import java.util.HashMap;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Window;

/**
 *
 * @author alabajos
 */
public class WinExpiraEmail extends SelectorComposer<Component> {

    private SqlSessionFactory sqlSessionFactory;
    private EventQueue eq3;


    @Wire
    Datebox fechaVencimiento;

    @Wire
    Spinner veces;



    public WinExpiraEmail() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

    }

    @Listen("onClick = #save")
    public void ok() {

        HashMap hm = new HashMap();
        Notificacion bean = new Notificacion();
        bean.setFechaVencimiento(fechaVencimiento.getValue());
        bean.setVeces(veces.getValue());

        hm.put("notificacion", bean);
        eq3 = EventQueues.lookup("dataNotificacion", EventQueues.DESKTOP, false);
        eq3.publish(new Event("", null, hm));
        WinExpiraEmail.detach();
        
    }

    @Listen("onClick = #cancelar")
    public void cerrar() {
        WinExpiraEmail.detach();
    }

    @Wire
    private Window win, WinExpiraEmail;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

}
