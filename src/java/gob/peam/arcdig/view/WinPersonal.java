/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Personal;
import gob.peam.arcdig.dao.PersonalDao;
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
public class WinPersonal extends SelectorComposer<Component> {

    private static final long serialVersionUID = 8243942703081449079L;
    private List<Personal> empleados = (List<Personal>) new ArrayList<Personal>();

    private static List<Listitem> uno = new ArrayList<Listitem>();
    private static List<Listitem> dos = new ArrayList<Listitem>();

    private EventQueue eq;
    @Wire
    Listbox candidateLb;
    @Wire
    Listbox chosenLb;
    @Wire
    Window winPersonal;
    @Wire
    Textbox filtro;
    @Wire
    Button ok;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        dos = (List<Listitem>) exec.getArg().get("compartidos");
        //propiedad = (Boolean) exec.getArg().get("propiedad");

        empleados = new PersonalDao().getAll();
        if (!dos.isEmpty()) {
            dibujarPersonal(empleados, true, true);
        } else {
            dibujarPersonal(empleados, true, false);
        }
    }

    public void dibujarPersonal(List<Personal> empleados, boolean primera, boolean relleno) {

        candidateLb.getItems().clear();

        for (Personal item : empleados) {
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell dni = new Listcell();
            dni.appendChild(new Label(String.valueOf(item.getDocumento())));
            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(String.valueOf(item.getNombreCompletoNPM())));

            listitem.appendChild(check);
            listitem.appendChild(dni);
            listitem.appendChild(nombre);

            listitem.setSelected(true);

            boolean si = true;
            if (relleno) {
                for (Listitem relle : dos) {
                    if (relle.getId().equals(item.getDocumento())) {
                        si = false;
                        break;
                    }
                }
            }
            if (si) {
                
                for (int i = 0; i < candidateLb.getItems().size(); i++) {
                    if (candidateLb.getItems().get(i).getId().equals(item.getDocumento())) {
                        candidateLb.getItems().get(i).detach();
                    }
                }
                listitem.setId(item.getDocumento());
                candidateLb.appendChild(listitem);
            } else {
                chosenLb.appendChild(listitem);
            }

            if (primera) {
                if (si) {
                    uno.add(listitem);
                }
            }
        }

    }

    @Listen("onClick = #chooseBtn")
    public void chooseBtn() {
        Set item = candidateLb.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            chosenLb.appendChild(emp.get(i));
            chosenLb.removeItemFromSelection(emp.get(i));
            for (int j = 0; j < uno.size(); j++) {
                if (uno.get(j).getId().equals(emp.get(i).getId())) {
                    uno.remove(j);
                }
            }
        }
        if (!chosenLb.getItems().isEmpty()) {
            ok.setDisabled(false);
        }
    }

    @Listen("onClick = #removeBtn")
    public void removeRow() {
        Set item = chosenLb.getSelectedItems();

        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            uno.add(emp.get(i));
            candidateLb.appendChild(emp.get(i));
            candidateLb.removeItemFromSelection(emp.get(i));
        }
        //if (chosenLb.getItems().isEmpty()) {
        //    ok.setDisabled(true);
        //}
    }

    @Listen("onClick = #cancel")
    public void cancel() {
        winPersonal.detach();
    }

    @Listen("onClick = #ok")
    public void ok() {
        HashMap hm = new HashMap();
        List<Listitem> select = new ArrayList<Listitem>();

        for (Listitem item : chosenLb.getItems()) {
            select.add(item);
        }

        hm.put("compartidos", select);

        eq = EventQueues.lookup("dataCompartidos", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, hm));
        winPersonal.detach();
    }

    @Listen("onChange = #filtro")
    public void buscar() {

        List<Personal> filtrado = (List<Personal>) new ArrayList<Personal>();
        for (Listitem item : uno) {
            for (Personal pers : empleados) {
                if (item.getId().equals(pers.getDocumento())) {
                    if (pers.getNombreCompletoNPM().toUpperCase().contains(filtro.getValue().toUpperCase().trim()) || pers.getDocumento().trim().toUpperCase().equals(filtro.getValue().trim().toUpperCase())) {
                        filtrado.add(pers);
                    }
                }
            }
        }
       
            dibujarPersonal(filtrado, false, false);
        
    }

    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

}
