/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.Etiqueta;
import gob.peam.arcdig.beans.Sesion;
import gob.peam.arcdig.dao.PermisoDao;
import gob.peam.arcdig.dao.SesionDao;
import java.util.HashMap;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Label;

import javax.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Textbox;

/**
 *
 * @author alabajos
 */
public class Content extends SelectorComposer<Component> {

    private SqlSessionFactory sqlSessionFactory;

    public Content() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }
    @Wire
    Include contenido;
    @Wire
    Label labelEtiqueta;
    /*@Wire
     Label subModulo;*/
    @Wire
    Menuitem all;
    @Wire
    Button privilegio;
    @Wire
    A subModu;
    Boolean siete;
    private EventQueue eq;
    public String dni;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        HttpSession session = (HttpSession) Executions.getCurrent().getDesktop().getSession().getNativeSession();
        String usua = (String) session.getAttribute("user");
        dni = (String) session.getAttribute("dni");
        gob.peam.arcdig.beans.Permiso perm = new gob.peam.arcdig.beans.Permiso();
        try {
            String dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
            perm = new PermisoDao().getPermisos(dni);
            siete = !perm.getItem7();
        } catch (NullPointerException ex) {
            siete = false;
        }

        if (!siete) {
            todo = false;
            solo = true;
            all.setVisible(false);
            privilegio.setIconSclass("z-icon-user");
        }

        boolean flag = true;
        String url = Executions.getCurrent().getParameter("url") == null ? "" : Executions.getCurrent().getParameter("url");
        String id = Executions.getCurrent().getParameter("id") == null ? "" : Executions.getCurrent().getParameter("id");
        HashMap hm = new HashMap();
        hm.put("url", url);
        hm.put("id", id);
        if (usua == null) {
            flag = false;
            Executions.sendRedirect("./");
            /*Window window = (Window) Executions.createComponents("./resources/zkWindow/winLogin.zul", null, hm);
             window.doModal();*/

        } else {
            SesionDao sesionDao = new SesionDao();
            Sesion s = sesionDao.buscarSesion((String) session.getId());

            if (s == null) {
                flag = false;
                Executions.getCurrent().getDesktop().getSession().invalidate();
                /*Window window = (Window) Executions.createComponents("./resources/zkWindow/winLogin.zul", null, hm);
                 window.doModal();*/

                Executions.sendRedirect("./");
            } else if (s.isSesiEstado()) {
                flag = true;
            } else {
                flag = false;
                Executions.getCurrent().getDesktop().getSession().invalidate();
                /* Window window = (Window) Executions.createComponents("./resources/zkWindow/winLogin.zul", null, hm);
                 window.doModal();*/

                Executions.sendRedirect("./");
            }
        }

        //String url = Executions.getCurrent().getParameter("url") == null ? "" : Executions.getCurrent().getParameter("url");
        if (!"".equals(url)) {
            String zul = "/resources/apps/" + url + ".zul";
            contenido.setSrc(zul);
            try {
                Integer intId = Integer.parseInt(id);
                navigator(intId);
            } catch (Exception ex) {
                //subModulo.setValue("");

            }
        }
    }

    @Wire
    Textbox buscar;
    Boolean share = false, solo = false, todo = true, soloexp = false;

    @Listen("onClick = #share")
    public void share() {
        share = true;
        solo = false;
        todo = false;
        soloexp = false;
        buscar();
    }

    @Listen("onClick = #solo")
    public void solo() {
        solo = true;
        share = false;
        todo = false;
        soloexp = false;
        buscar();
    }

    @Listen("onClick = #all")
    public void all() {
        solo = false;
        share = false;
        todo = true;
        soloexp = false;
        buscar();
    }

    @Listen("onClick = #soloexp")
    public void soloexp() {
        solo = false;
        share = false;
        todo = false;
        soloexp = true;
        buscar();
    }

    @Listen("onChange=#buscar")
    public void buscar() {
        HashMap hm = new HashMap();
        hm.put("filtro", buscar.getValue().toString().toUpperCase());
        if (share) {
            hm.put("q", " and documento.docu_id in (select du.docu_id from documento_usuario du where du.pers_dni= '" + dni + "')");
        } else if (solo) {
            hm.put("q", " and (propietario = '" + dni + "' or documento.docu_id in (select du.docu_id from documento_usuario du where du.pers_dni= '" + dni + "'))");
        } else if (soloexp) {
            hm.put("q", " and docu_group is null");
        } else if (todo) {
            hm.put("q", "");
        }
        try {
            eq = EventQueues.lookup("buscarPublicacion", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        } catch (NullPointerException ex) {
            //Executions.sendRedirect("./zul?url=inicio&s="+buscar.getValue().toString().toUpperCase());
            contenido.setSrc("/resources/apps/inicio.zul");
            //subModulo.setValue("");
            eq = EventQueues.lookup("buscarPublicacion", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        }
    }

    public void navigator(Integer id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Etiqueta etiqueta = new Etiqueta();
            etiqueta = sqlSession.selectOne("Etiqueta.getNavigator", id);
            //labelEtiqueta.setValue(etiqueta.getDescripcion());
            if (etiqueta.getSubModulos() != null) {
                //subModulo.setValue(etiqueta.getSubModulos().get(0).getNombre());
                subModu.setLabel(etiqueta.getSubModulos().get(0).getNombre());
                subModu.setHref(etiqueta.getSubModulos().get(0).getUrl() + "&id=" + etiqueta.getSubModulos().get(0).getIdSubModulo());
                //subModulo.setIconSclass("z-icon-angle-double-right");
            }
        } finally {
            sqlSession.close();
        }

    }
}
