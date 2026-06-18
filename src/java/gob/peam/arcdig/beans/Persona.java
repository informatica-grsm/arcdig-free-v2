/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

import gob.peam.arcdig.dao.GrupoDao;
import gob.peam.arcdig.dao.PersonaDao;
import gob.peam.arcdig.dao.UsuarioDao;



import gob.peam.arcdig.utils.Encriptar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author jpgprog84
 */
public class Persona extends SelectorComposer<Component> {

    /**
     *
     */
    private static final long serialVersionUID = -738831721435256068L;
    private Integer idPersona;
    private String dni;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String iniciales;
    private boolean estado;
    private Dependencia dependencia;
    private Usuario usuario;
    private String cargo;
    private String idGlobal;
    private String charId;
    private Boolean check;

    @Wire
    Listbox area;
    @Wire
    Listbox grupo;
    Integer option;
    @Wire
    Button activarPersona;
    @Wire
    Button btn1;
    @Wire
    Button activar;
    @Wire
    Textbox nombre_p;
    @Wire
    Textbox apellidoPaterno_p;
    @Wire
    Textbox apellidoMaterno_p;
    @Wire
    Textbox documento;
    @Wire
    Textbox usuario_p;
    @Wire
    Textbox password_p;
    @Wire
    Label nombreError;
    @Wire
    Label paternoError;
    @Wire
    Label maternoError;
    @Wire
    Label documentoError;
    @Wire
    Window formPersona;
    private EventQueue eq;
    @Wire
    private Window win;

    public Persona() {
    }

    public Persona(Integer idPersona, String dni, String nombre, String apellidoPaterno, String apellidoMaterno, String iniciales, boolean estado, Dependencia dependencia, Usuario usuario, String cargo, String idGlobal, String charId, Boolean check) {
        this.idPersona = idPersona;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.iniciales = iniciales;
        this.estado = estado;
        this.dependencia = dependencia;
        this.usuario = usuario;
        this.cargo = cargo;
        this.idGlobal = idGlobal;
        this.charId = charId;
        this.check = check;
    }

    public String getApellidoMaterno() {
        return this.apellidoMaterno;
    }

    public Boolean getCheck() {
        return this.check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return this.apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getCargo() {
        return this.cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCharId() {
        return this.charId;
    }

    public void setCharId(String charId) {
        this.charId = charId;
    }

    public Dependencia getDependencia() {
        return this.dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public String getDni() {
        return this.dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public boolean isEstado() {
        return this.estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getIdGlobal() {
        return this.idGlobal;
    }

    public void setIdGlobal(String idGlobal) {
        this.idGlobal = idGlobal;
    }

    public Integer getIdPersona() {
        return this.idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public String getIniciales() {
        return this.iniciales;
    }

    public void setIniciales(String iniciales) {
        this.iniciales = iniciales;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String toString() {
        return this.nombre + " " + this.apellidoPaterno + " " + this.apellidoMaterno;
    }

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        this.option = (Integer) exec.getArg().get("option");
        Integer id = (Integer) exec.getArg().get("id");
        this.cargarDatos(this.option, id);
    }

    public void cargarDatos(Integer op, Integer id) {
       
        

       
       List<Grupo> grupos = new GrupoDao().listar();

        for(Grupo g: grupos){
           
            Listitem item = new Listitem();
            item.setLabel(g.getNombre());
            item.setValue(g.getIdGrupo());
            this.grupo.appendChild(item);
        }

        if (!this.grupo.getItems().isEmpty()) {
            this.grupo.setSelectedIndex(0);
        }

        if (op == 2) {
            PersonaDao dao = new PersonaDao();
            new Persona();
            Persona persona = dao.getBuscarPersona(id);
            this.nombre_p.setValue(persona.getNombre());
            this.apellidoPaterno_p.setValue(persona.getApellidoPaterno());
            this.apellidoMaterno_p.setValue(persona.getApellidoMaterno());
            this.documento.setValue(persona.getDni());
            this.usuario_p.setValue(persona.getUsuario().getLogin());
            this.password_p.setValue(persona.getUsuario().getLogin());
            this.idPersona = id;
            int index = 0;

            Iterator var6;
            Listitem item;
           /* for (var6 = this.area.getItems().iterator(); var6.hasNext(); ++index) {
                item1 = (Listitem) var6.next();
                if (persona.getDependencia() != null && item1.getValue() == persona.getDependencia().getIdDependencia()) {
                    this.area.setSelectedIndex(index);
                    break;
                }
            }*/

            index = 0;

            for (var6 = this.grupo.getItems().iterator(); var6.hasNext(); ++index) {
                item = (Listitem) var6.next();
                if (persona.getUsuario().getGrupo() != null && item.getValue() == persona.getUsuario().getGrupo().getIdGrupo()) {
                    this.grupo.setSelectedIndex(index);
                    break;
                }
            }

            if (!persona.isEstado()) {
                this.activarPersona.setDisabled(false);
                this.activarPersona.setStyle("display:block");

                try {
                    if (persona.getUsuario().getEstado()) {
                        this.btn1.setVisible(true);
                    }
                } catch (NullPointerException var9) {
                }
            } else if ("NO ASIGNADO".equals(persona.getUsuario().getLogin())) {
                this.activar.setDisabled(false);
                this.activar.setStyle("display:block");
            }

            try {
                if (persona.getUsuario().getEstado()) {
                    this.btn1.setVisible(true);
                } else {
                    this.activar.setDisabled(false);
                    this.activar.setStyle("display:block");
                }
            } catch (NullPointerException var8) {
            }
        }

    }

    @Listen("onClick = #btn1")
    public void btn1() {
        UsuarioDao dao = new UsuarioDao();
        dao.desactivarUsuarioIdPers(this.idPersona);
        this.btn1.setVisible(false);
        this.activar.setDisabled(false);
        this.activar.setStyle("display:block");
    }

    @Listen("onClick  = #savePersona")
    public void guardarPersona() {
        try {
            if (this.validarPersona()) {
                Persona pers = new Persona();
                pers.setApellidoMaterno(this.apellidoMaterno_p.getValue().toUpperCase().trim());
                pers.setApellidoPaterno(this.apellidoPaterno_p.getValue().toUpperCase().trim());
                pers.setNombre(this.nombre_p.getValue().toUpperCase().trim());
                pers.setDni(this.documento.getValue().toUpperCase().trim());
                pers.setIdPersona(this.idPersona);
                Usuario usr = new Usuario();
                Grupo grup = new Grupo();
                grup.setIdGrupo((Integer) this.grupo.getSelectedItem().getValue());
                usr.setGrupo(grup);
                pers.setUsuario(usr);
                Dependencia depe = new Dependencia();
                depe.setIdDependencia((Integer) this.area.getSelectedItem().getValue());
                pers.setDependencia(depe);
                PersonaDao dao = new PersonaDao();
                HashMap hm = dao.actualizarSoloPersona(pers, this.option);
                if ((Integer) hm.get("error") == 1) {
                    this.documento.setStyle("box-shadow: 0px 1px 1px  #f56e42 inset, 0px 0px 8px #f56e42; border-color:#f86e42");
                    this.documentoError.setValue("No dejar en blanco.");
                } else {
                    this.formPersona.detach();
                    this.eq = EventQueues.lookup("listaPersona", "desktop", false);
                    this.eq.publish(new Event("", (Component) null, ""));
                }
            }
        } catch (NullPointerException var7) {
            this.formPersona.detach();
        }

    }

    @Listen("onClick = #save")
    public void guardar() throws Exception {
        try {
            if (this.validarPersona()) {
                Persona pers = new Persona();
                pers.setApellidoMaterno(this.apellidoMaterno_p.getValue().toUpperCase().trim());
                pers.setApellidoPaterno(this.apellidoPaterno_p.getValue().toUpperCase().trim());
                pers.setNombre(this.nombre_p.getValue().toUpperCase().trim());
                pers.setDni(this.documento.getValue().toUpperCase().trim());
                pers.setIdPersona(this.idPersona);
                Usuario usr = new Usuario();
                usr.setLogin(this.documento.getValue().toUpperCase().trim());
                usr.setClave(Encriptar.md5(Encriptar.md5(Encriptar.md5(this.documento.getValue().trim()))));
                Grupo grup = new Grupo();
                grup.setIdGrupo((Integer) this.grupo.getSelectedItem().getValue());
                usr.setGrupo(grup);
                pers.setUsuario(usr);
                Dependencia depe = new Dependencia();
                depe.setIdDependencia(1);
                pers.setDependencia(depe);
                PersonaDao dao = new PersonaDao();
                HashMap hm = dao.actualizarPersona(pers, this.option);
                if ((Integer) hm.get("error") == 1) {
                    this.documento.setStyle("box-shadow: 0px 1px 1px  #f56e42 inset, 0px 0px 8px #f56e42; border-color:#f86e42");
                    this.documentoError.setValue("No dejar en blanco.");
                } else {
                    this.formPersona.detach();
                    this.eq = EventQueues.lookup("listaPersona", "desktop", false);
                    this.eq.publish(new Event("", (Component) null, ""));
                }
            }
        } catch (NullPointerException var7) {
            this.formPersona.detach();
        }

    }

    private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "info", ref, "middle_center", 3000);
    }

    public Boolean validarPersona() {
        Boolean flag = true;
        this.nombre_p.setStyle("");
        this.nombreError.setStyle("");
        this.apellidoPaterno_p.setStyle("");
        this.paternoError.setStyle("");
        this.apellidoMaterno_p.setStyle("");
        this.maternoError.setStyle("");
        this.documento.setStyle("");
        this.documentoError.setStyle("");
        if ("".equals(this.nombre_p.getValue())) {
            this.nombre_p.setStyle("box-shadow: 0px 1px 1px  #f56e42 inset, 0px 0px 8px #f56e42; border-color:#f86e42");
            this.nombreError.setValue("No dejar en blanco.");
            flag = false;
        }

        if ("".equals(this.apellidoPaterno_p.getValue())) {
            this.apellidoPaterno_p.setStyle("box-shadow: 0px 1px 1px  #f56e42 inset, 0px 0px 8px #f56e42; border-color:#f86e42");
            this.paternoError.setValue("No dejar en blanco.");
            flag = false;
        }

        if ("".equals(this.apellidoMaterno_p.getValue())) {
            this.apellidoMaterno_p.setStyle("box-shadow: 0px 1px 1px  #f56e42 inset, 0px 0px 8px #f56e42; border-color:#f86e42");
            this.maternoError.setValue("No dejar en blanco.");
            flag = false;
        }

        if ("".equals(this.documento.getValue())) {
            this.documento.setStyle("box-shadow: 0px 1px 1px  #f56e42 inset, 0px 0px 8px #f56e42; border-color:#f86e42");
            this.documentoError.setValue("No dejar en blanco.");
            flag = false;
        }

        return flag;
    }

    @Listen("onChange = #documento")
    public void cambioUsuario() {
        this.usuario_p.setValue(this.documento.getValue());
        this.password_p.setValue(this.documento.getValue());
    }

    @Listen("onClick = #close")
    public void cerrar() {
        this.formPersona.detach();
    }

    @Listen("onClick = #activar")
    public void activar() {
        this.usuario_p.setValue(this.documento.getValue());
        this.password_p.setValue(this.documento.getValue());
        UsuarioDao dao = new UsuarioDao();
        dao.activarUsuarioIdPers(this.idPersona);
        this.btn1.setVisible(true);
        this.activar.setDisabled(true);
        this.activar.setVisible(false);
    }

    @Listen("onClick = #activarPersona")
    public void activarPersona() {
        this.usuario_p.setValue(this.documento.getValue());
        this.password_p.setValue(this.documento.getValue());
        PersonaDao dao = new PersonaDao();
        dao.activarPersona(this.idPersona);
        this.formPersona.detach();
        this.eq = EventQueues.lookup("listaPersona", "desktop", false);
        this.eq.publish(new Event("", (Component) null, ""));
    }
}
