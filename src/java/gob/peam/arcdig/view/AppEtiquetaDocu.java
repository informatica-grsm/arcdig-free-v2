/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.EtiquetaDocu;
import gob.peam.arcdig.dao.EtiquetaDocuDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author alabajos
 */
public class AppEtiquetaDocu extends SelectorComposer<Component> {

    public AppEtiquetaDocu() {
    }
    List<EtiquetaDocu> items = (List<EtiquetaDocu>) new ArrayList<EtiquetaDocu>();
    private EtiquetaDocu SelectItem;
    private Boolean editar = false;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dibujarEtiquetaDocu();
    }

    @Wire
    Button aceptar;
    @Wire
    Bandbox filtro;
    @Wire
    Listbox listEtiquetaDocu;
    @Wire
    Grid formulario;
    @Wire
    Hlayout control;
    @Wire
    Textbox nombre, nivel;
    @Wire
    Label nombError;
    @Wire
    Checkbox estado;

    public void dibujarEtiquetaDocu() {
        items = new EtiquetaDocuDao().listarEtiquetaDocu(filtro.getValue());
        listEtiquetaDocu.getItems().clear();
        int index = 0;

        for (EtiquetaDocu item : items) {
            index++;
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(String.valueOf(index)));
            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(item.getNombre()));
            /*Listcell descripcion = new Listcell();
             descripcion.appendChild(new Label(item.getDescripcion()));*/

            Listcell estado = new Listcell();
            Image imgEstado = new Image();
            imgEstado.setSrc("./resources/img/" + item.getEstado() + ".png");
            imgEstado.setStyle("border:0px");
            estado.appendChild(imgEstado);
            listitem.appendChild(check);
            listitem.appendChild(nro);
            listitem.appendChild(nombre);
            //listitem.appendChild(descripcion);
            listitem.appendChild(estado);

            listEtiquetaDocu.appendChild(listitem);
        }
    }

    @Listen("onSelect = #listEtiquetaDocu")
    public void select() {
        Set item = listEtiquetaDocu.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            SelectItem = items.get(emp.get(i).getIndex());
        }
    }

    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 4000);
    }

    @Listen("onClick = #clear")
    public void clear() {
        nombre.setValue("");
        //ruta.setValue(SelectItem.getCataCarpeta());
        estado.setChecked(false);
    }

    @Listen("onClick = #cancelar")
    public void cancelar() {
        listEtiquetaDocu.setVisible(true);
        formulario.setVisible(false);
        control.setVisible(false);
    }

    @Listen("onClick=#aceptar")
    public void aceptar() {

        nombError.setValue("");
        if (!"".equals(nombre.getValue())) {

            EtiquetaDocu bean = new EtiquetaDocu();
            bean.setNombre(nombre.getValue());
            bean.setEstado(estado.isChecked());
           // bean.setNivel(Integer.parseInt(nivel.getValue()));
            Integer i = 0;
            if (editar) {
                bean.setId(SelectItem.getId());
                 i = new EtiquetaDocuDao().getRepetidoUpdate(bean);
            } else {
                 i = new EtiquetaDocuDao().getRepetido(bean);
            }
            if (i == 0) {
                if (editar) {
                    bean.setId(SelectItem.getId());
                    new EtiquetaDocuDao().update(bean);
                } else {
                    new EtiquetaDocuDao().insert(bean);
                }
                listEtiquetaDocu.setVisible(true);
                formulario.setVisible(false);
                control.setVisible(false);
                dibujarEtiquetaDocu();
            } else {
                nombError.setValue("El nombre se esta repitiendo con otro registro");
            }
        } else {
            nombError.setValue("El nombre no puede dejarse en blanco");
        }
    }

    @Listen("onClick = #editar")
    public void editar() {
        if (SelectItem != null) {
            listEtiquetaDocu.setVisible(false);
            formulario.setVisible(true);
            control.setVisible(true);
            nombre.setValue(SelectItem.getNombre());

            estado.setChecked(SelectItem.getEstado());
            editar = true;
        } else {
            showNotify("Porfavor seleccione un item para Editar", win, "warning", "top_right");
        }

    }

    @Listen("onClick = #nuevo")
    public void nuevo() {
        listEtiquetaDocu.setVisible(false);
        formulario.setVisible(true);
        control.setVisible(true);
        nombre.setValue("");

        estado.setChecked(true);
        editar = false;
    }

    @Listen("onClick = #eliminar")
    public void eliminar() {
        if (SelectItem != null) {
            Messagebox.show(
                    "Desea eliminar esta Etiqueta?", "Dialogo de Confirmación ",
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event evt) throws Exception {
                            if (evt.getName().equals("onOK")) {
                                new EtiquetaDocuDao().delete(SelectItem);
                                dibujarEtiquetaDocu();
                            }
                        }
                    });
        } else {
            showNotify("Porfavor seleccione un item para Eliminar", win, "warning", "top_right");
        }
    }

    @Listen("onClick = #update")
    public void actualizar() {
        dibujarEtiquetaDocu();
    }

    @Listen("onChange = #filtro")
    public void buscar() {
        dibujarEtiquetaDocu();
    }
}
