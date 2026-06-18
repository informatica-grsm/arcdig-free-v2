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

public class WinPersona extends SelectorComposer<Component> {

    private static final long serialVersionUID = 8243942703081449079L;

    private List<Persona> empleados = (List<Persona>) new ArrayList<Persona>();

    private static List<Listitem> uno = new ArrayList<Listitem>();
    private static List<Listitem> dos = new ArrayList<Listitem>();
    private static Boolean propiedad = false;
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
        dos = (List<Listitem>) exec.getArg().get("personas");
        propiedad = (Boolean) exec.getArg().get("propiedad");

        empleados = new PersonaDao().getAllOficina("010409");
        if (!dos.isEmpty()) {
            dibujarPersonal(empleados, true, true);
        } else {
            dibujarPersonal(empleados, true, false);
        }
    }

    public void dibujarPersonal(List<Persona> empleados, boolean primera, boolean relleno) {

        candidateLb.getItems().clear();

        candidateLb.setMultiple(true);
        candidateLb.setCheckmark(true);

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
            listitem.setSelected(true);
            //listitem.setId(item.getIdPersona().toString());

            boolean si = true;
            if (relleno) {
                for (Listitem relle : dos) {
                    if (relle.getId().equals(item.getIdPersona().toString())) {
                        si = false;
                        break;
                    }
                }
            }
            if (si) {
                for (int i = 0; i < candidateLb.getItems().size(); i++) {
                    if (candidateLb.getItems().get(i).getId().equals(item.getIdPersona().toString())) {
                        candidateLb.getItems().get(i).detach();
                    }
                }
                listitem.setId(item.getIdPersona().toString());
                candidateLb.appendChild(listitem);
            } else {
                listitem.setId(item.getIdPersona().toString());
                chosenLb.appendChild(listitem);
            }

            if (primera) {
                if (si) {
                    uno.add(listitem);
                }
            }
        }

    }

    @Listen("onSelect = #candidateLb")
    public void select() {
        Set item = candidateLb.getSelectedItems();

        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < empleados.size(); i++) {
            empleados.get(i).setCheck(false);
        }
        for (int i = 0; i < emp.size(); i++) {
            empleados.get(emp.get(i).getIndex()).setCheck(true);
        }
    }

    @Listen("onSelect = #chosenLb")
    public void selectChoosen() {
        Set item = chosenLb.getSelectedItems();

        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < empleados.size(); i++) {
            empleados.get(i).setCheck(false);
        }
        for (int i = 0; i < emp.size(); i++) {
            empleados.get(emp.get(i).getIndex()).setCheck(true);
        }
    }

    @Listen("onClick = #chooseBtn")
    public void chooseBtn() {
        Set item = candidateLb.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        List<Listitem> dos = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            chosenLb.appendChild(emp.get(i));
            chosenLb.removeItemFromSelection(emp.get(i));
            for (int j = 0; j < uno.size(); j++) {
                if (uno.get(j).getId().equals(emp.get(i).getId())) {
                    uno.remove(j);
                } else {
                    //dos.add(items);
                }
            }
        }
        if (!chosenLb.getItems().isEmpty()) {
            ok.setDisabled(false);
        }
        //if (!dos.isEmpty()) {
        //    uno = dos;
        //}
    }

    @Listen("onClick = #removeBtn")
    public void removeRow() {
        Set item = chosenLb.getSelectedItems();

        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            //empleados.get(emp.get(i).getIndex()).setCheck(true);
            uno.add(emp.get(i));
            candidateLb.appendChild(emp.get(i));
            candidateLb.removeItemFromSelection(emp.get(i));

            //showNotify(emp.get(i).getId(), win, "info", "top_right");
        }
        /*if (chosenLb.getItems().isEmpty()) {
            ok.setDisabled(true);
        }*/
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

        hm.put("personas", select);
        if (!propiedad) {
            eq = EventQueues.lookup("dataPersona", EventQueues.DESKTOP, false);
        } else {
            eq = EventQueues.lookup("dataPropiedad", EventQueues.DESKTOP, false);
        }
        eq.publish(new Event("", null, hm));
        winPersonal.detach();
    }

    @Listen("onChange = #filtro")
    public void buscar() {
        List<Persona> filtrado = (List<Persona>) new ArrayList<Persona>();
        for (Listitem item : uno) {
            for (Persona pers : empleados) {
                if (item.getId().equals(pers.getIdPersona().toString())) {
                    if (pers.getNombre().toUpperCase().contains(filtro.getValue().toUpperCase().trim()) || pers.getDni().trim().toUpperCase().equals(filtro.getValue().trim().toUpperCase())) {
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
