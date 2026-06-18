/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Persona;
import gob.peam.arcdig.dao.PersonaDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Cj.Legacy
 */
public class WinPersonalSingle extends SelectorComposer<Component> {

    private static final long serialVersionUID = 8243942703081449079L;
    private List<Persona> empleados = (List<Persona>) new ArrayList<Persona>();

    private EventQueue eq;
    @Wire
    Listbox listPersonal;

    Persona personal;

    @Wire
    Window winPersonal;
    @Wire
    Textbox filtro;
    @Wire
    Button ok;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        dibujarPersonal("");
    }

    public void dibujarPersonal(String query) {
        listPersonal.getItems().clear();
        empleados = new PersonaDao().listarPersona(query);

        for (Persona item : empleados) {
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell dni = new Listcell();
            dni.appendChild(new Label(String.valueOf(item.getDni())));
            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(String.valueOf(item.getNombre())));

            listitem.appendChild(check);
            listitem.appendChild(dni);
            listitem.appendChild(nombre);
            //listitem.setId(item.getIdPersona().toString());

            listPersonal.appendChild(listitem);
        }

    }

    @Listen("onClick = #cancel")
    public void cancel() {
        winPersonal.detach();
    }

    @Listen("onClick = #ok")
    public void ok() {
        HashMap hm = new HashMap();
        //List<Listitem> select = new ArrayList<Listitem>();
        Set item = listPersonal.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            personal = empleados.get(emp.get(i).getIndex());
        }

        hm.put("empleado", personal);
        eq = EventQueues.lookup("dataEmpleado", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, hm));
        //eq.close();
        winPersonal.detach();
    }

    @Listen("onChange = #filtro")
    public void buscar() {
        dibujarPersonal(filtro.getValue().toString());
    }


    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

}
