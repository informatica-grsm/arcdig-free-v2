/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.EtiquetaDocu;
import gob.peam.arcdig.dao.EtiquetaDocuDao;

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
import org.zkoss.zul.Iframe;
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
public class WinEtiqueta extends SelectorComposer<Component> {

    private static final long serialVersionUID = 8243942703081449079L;
    private List<EtiquetaDocu> etiquetas = (List<EtiquetaDocu>) new ArrayList<EtiquetaDocu>();
    private Boolean modificar = false;

    private static List<Listitem> uno = new ArrayList<Listitem>();
    private static List<Listitem> dos = new ArrayList<Listitem>();
    private EventQueue eq;
    @Wire
    Listbox candidateLb;
    @Wire
    Listbox chosenLb;
    @Wire
    Window winEtiqueta;
    @Wire
    Iframe viewFrame;
    @Wire
    Textbox filtro;
    @Wire
    Button ok;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        try {
            modificar = (Boolean) exec.getArg().get("modificar");
        } catch (NullPointerException exe) {
            modificar = false;
        }
        dos = (List<Listitem>) exec.getArg().get("etiquetas");
        etiquetas = new EtiquetaDocuDao().getAll();
        String ruta = (String) exec.getArg().get("ruta");

        if (ruta != null) {
            viewFrame.setSrc(ruta);
        }
        if (!dos.isEmpty()) {
            dibujarEtiquetaDocu(etiquetas, true, true);
        } else {
            dibujarEtiquetaDocu(etiquetas, true, false);
        }

    }

    public void dibujarEtiquetaDocu(List<EtiquetaDocu> etiquetas, boolean primera, boolean relleno) {
        candidateLb.getItems().clear();
        candidateLb.setMultiple(true);
        candidateLb.setCheckmark(true);

        for (EtiquetaDocu item : etiquetas) {
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));

            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(String.valueOf(item.getNombre())));

            listitem.appendChild(check);
            listitem.appendChild(nombre);
            listitem.setSelected(true);
            listitem.setId(item.getId().toString());
            boolean si = true;
            if (relleno) {
                for (Listitem relle : dos) {
                    if (relle.getId().equals(listitem.getId())) {
                        si = false;
                        break;
                    }
                }
            }
            if (si) {
                for (int i = 0; i < candidateLb.getItems().size(); i++) {
                    if (candidateLb.getItems().get(i).getId().equals(item.getId().toString())) {
                        candidateLb.getItems().get(i).detach();
                    }
                }
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
            //etiquetas.get(emp.get(i).getIndex()).setCheck(true);
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
        eq = EventQueues.lookup("cancelar", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, null));
        winEtiqueta.detach();
    }

    @Listen("onClick = #before")
    public void before() {
        HashMap hm = new HashMap();
        hm.put("etiquetas", chosenLb.getItems());
        eq = EventQueues.lookup("beforeEtiqueta", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, hm));
        winEtiqueta.detach();
    }

    @Listen("onClick = #ok")
    public void ok() {
        HashMap hm = new HashMap();
        hm.put("etiquetas", chosenLb.getItems());
        if (!modificar) {
            eq = EventQueues.lookup("dataEtiqueta", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        } else {
            eq = EventQueues.lookup("dataEdiEtiqueta", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        }
        winEtiqueta.detach();
    }

    @Listen("onChange = #filtro")
    public void buscar() {
        List<EtiquetaDocu> filtrado = (List<EtiquetaDocu>) new ArrayList<EtiquetaDocu>();
        for (Listitem item : uno) {
            for (EtiquetaDocu eti : etiquetas) {
                if (item.getId().equals(eti.getId().toString())) {
                    if (eti.getNombre().toUpperCase().contains(filtro.getValue().toUpperCase().trim())) {
                        filtrado.add(eti);
                    }
                }
            }
        }

        dibujarEtiquetaDocu(filtrado, false, false);

    }

    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

}
