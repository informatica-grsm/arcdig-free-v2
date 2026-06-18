/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Dependencia;
import gob.peam.arcdig.beans.Sesion;
import gob.peam.arcdig.beans.Usuario;
import gob.peam.arcdig.dao.SesionDao;
import gob.peam.arcdig.dao.UsuarioDao;
import gob.peam.arcdig.utils.Encriptar;
import javax.servlet.http.HttpSession;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class WinLogin extends SelectorComposer<Component> {

    @Wire
    Image captcha;
    public String url, id;

    @Listen("onClick = #aceptar")
    public void onOk() throws Exception {
        verificar();
    }

    @Listen("onOK = #usuario")
    public void enter1() throws Exception {
        verificar();
    }

    @Listen("onOK = #password")
    public void enter2() throws Exception {
        verificar();
    }

    @Listen("onOK = #txtcaptcha")
    public void enter3() throws Exception {
        verificar();
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        this.url = (String) exec.getArg().get("url");
        this.id = (String) exec.getArg().get("id");
        
        eq = EventQueues.lookup("loguear", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                verificar();
            }
        });
    }

    @Wire
    Textbox usuario;
    @Wire
    Textbox password;
    @Wire
    Textbox txtcaptcha;
    @Wire
    Window formLogin;

    private EventQueue eq;

    public void verificar() throws Exception {
        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario usuario = usuarioDao.buscarUsuarioPorLogin(this.usuario.getValue().toUpperCase().trim());
        if (usuario != null) {
            if (usuario.getClave().equals(Encriptar.md5(Encriptar.md5(Encriptar.md5(this.password.getValue().trim()))))) {

                String captcha = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("captcha");
                if (captcha.equals(this.txtcaptcha.getValue())) {
                    HttpSession session = (HttpSession) Executions.getCurrent().getDesktop().getSession().getNativeSession();
                    session.setAttribute("user", usuario.getLogin());
                    session.setAttribute("nombreUsuario", usuario.getPersona().getNombre());
                    session.setAttribute("iddependencia", usuario.getPersona().getDependencia().getIdDependencia());
                    session.setAttribute("ip", Executions.getCurrent().getLocalAddr());
                    session.setAttribute("idUsuario", usuario.getIdUsuario());
                    session.setAttribute("usuaId", usuario.getPersona().getIdPersona());
                    session.setAttribute("idPersona", usuario.getPersona().getIdPersona());
                    session.setAttribute("idGlobal", usuario.getPersona().getIdGlobal());
                    session.setAttribute("dni", usuario.getPersona().getDni());

                    SesionDao dao = new SesionDao();
                    Sesion s = new Sesion();
                    Dependencia d = new Dependencia();
                    d.setIdDependencia(usuario.getPersona().getDependencia().getIdDependencia());

                    s.setDependencia(d);
                    s.setSesiEstado(true);

                    s.setSesiIp(Executions.getCurrent().getLocalAddr());
                    s.setSesiId(session.getId());

                    Usuario u = new Usuario();
                    u.setIdUsuario(usuario.getIdUsuario());
                    s.setUsuario(u);
                    dao.insertarSesion(s, url, id);
                } else {
                    showNotify("Captcha Incorrecto", win);
                }
            } else {
                showNotify("Contraseña Incorrecto", win);
            }
        } else {
            showNotify("Usuario Incorrecto", win);
        }

    }
    
    @Wire
    private Window win;

    private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "warning", ref, "middle_center", 3000);
    }
}
