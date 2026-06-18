/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.Usuario;
import gob.peam.arcdig.dao.UsuarioDao;
import gob.peam.arcdig.utils.Encriptar;
import javax.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author alabajos
 */
public class Config extends SelectorComposer<Component> {

    private SqlSessionFactory sqlSessionFactory;

    public Config() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

    }
    @Wire
    Button aceptar;

    @Listen("onClick = #rechazar")
    public void rechazar() {
        actual.setValue("");
        nuevo.setValue("");
        password.setValue("");

    }
    @Wire
    Textbox actual;
    @Wire
    Textbox password;
    @Wire
    Textbox nuevo;
    @Wire
    Label usrError;
    @Wire
    Label passError;
    @Wire
    Label reeError;
    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 4000);
    }

    @Listen("onClick=#aceptar")
    public void aceptar() throws Exception {
        usrError.setValue("");
        passError.setValue("");
        reeError.setValue("");

        UsuarioDao usuarioDao = new UsuarioDao();
        Execution exec = Executions.getCurrent();
        HttpServletRequest request = (HttpServletRequest) exec.getNativeRequest();
        String usua = request.getSession().getAttribute("user").toString();

        Usuario usuario = usuarioDao.buscarUsuarioPorLogin(usua.toUpperCase().trim());

        if (usuario.getClave().equals(Encriptar.md5(Encriptar.md5(Encriptar.md5(this.actual.getValue().trim()))))) {

            if (this.password.getValue().trim().equals(this.nuevo.getValue().trim())) {
                usuario.setClave(Encriptar.md5(Encriptar.md5(Encriptar.md5(this.password.getValue().trim()))));
                SqlSession session = sqlSessionFactory.openSession();
                try {
                    session.update("Usuario.updateContrasena", usuario);
                    session.commit();
                    rechazar();
                    showNotify("La Contraseña Se cambió Correctamente", win, "info", "right_top");

                } finally {
                    session.close();
                }
            } else {
                reeError.setValue("Las Contraseñas no coinciden");
            }
        } else {
            usrError.setValue("Contraseña actual no es correcta");
        }

    }

}
