/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.MineType;
import gob.peam.arcdig.dao.MineTypeDao;
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
public class AppMineType extends SelectorComposer<Component> {

    public AppMineType() {
    }
    List<MineType> items = (List<MineType>) new ArrayList<MineType>();
    private MineType SelectItem;
    private Boolean editar = false;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dibujarMineType();
    }

    @Wire
    Button aceptar;
    @Wire
    Bandbox filtro;
    @Wire
    Listbox listMineType;
    @Wire
    Grid formulario;
    @Wire
    Hlayout control;
    @Wire
    Textbox nombre, descripcion;
    @Wire
    Label nombError, desError;
    @Wire
    Checkbox estado;

    public void dibujarMineType() {
        items = new MineTypeDao().listarMineType(filtro.getValue());
        listMineType.getItems().clear();
        int index = 0;

        for (MineType item : items) {
            index++;
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(String.valueOf(index)));
            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(item.getMineNombre()));
            Listcell descripcion = new Listcell();
            descripcion.appendChild(new Label(item.getMineExt()));

            Listcell estado = new Listcell();
            Image imgEstado = new Image();
            imgEstado.setSrc("./resources/img/" + item.getMineEstado() + ".png");
            imgEstado.setStyle("border:0px");
            estado.appendChild(imgEstado);
            listitem.appendChild(check);
            listitem.appendChild(nro);
            listitem.appendChild(nombre);
            listitem.appendChild(descripcion);
            listitem.appendChild(estado);

            listMineType.appendChild(listitem);
        }
    }

    @Listen("onSelect = #listMineType")
    public void select() {
        Set item = listMineType.getSelectedItems();
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
        descripcion.setValue("");
        //ruta.setValue(SelectItem.getCataCarpeta());
        estado.setChecked(false);
    }

    @Listen("onClick = #cancelar")
    public void cancelar() {
        listMineType.setVisible(true);
        formulario.setVisible(false);
        control.setVisible(false);
    }

    @Listen("onClick=#aceptar")
    public void aceptar() {
        desError.setValue("");
        nombError.setValue("");
        if (!"".equals(nombre.getValue())) {

            MineType bean = new MineType();
            bean.setMineNombre(nombre.getValue().toLowerCase());
            bean.setMineExt(descripcion.getValue().toLowerCase());
            bean.setMineEstado(estado.isChecked());
            Integer i = 0;
            if (editar) {
                bean.setMineId(SelectItem.getMineId());
                i = new MineTypeDao().getRepetidoUpdate(bean);
            } else {
                i = new MineTypeDao().getRepetido(bean);
            }
            if (i == 0) {
                if (editar) {
                    bean.setMineId(SelectItem.getMineId());
                    new MineTypeDao().update(bean);
                } else {
                    new MineTypeDao().insert(bean);
                }

                listMineType.setVisible(true);
                formulario.setVisible(false);
                control.setVisible(false);
                dibujarMineType();
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
            listMineType.setVisible(false);
            formulario.setVisible(true);
            control.setVisible(true);
            nombre.setValue(SelectItem.getMineNombre());
            descripcion.setValue(SelectItem.getMineExt());
            estado.setChecked(SelectItem.getMineEstado());
            editar = true;
        } else {
            showNotify("Porfavor seleccione un item para Editar", win, "warning", "top_right");
        }

    }

    @Listen("onClick = #nuevo")
    public void nuevo() {
        listMineType.setVisible(false);
        formulario.setVisible(true);
        control.setVisible(true);
        nombre.setValue("");
        descripcion.setValue("");
        editar = false;
        estado.setChecked(true);
    }

    @Listen("onClick = #eliminar")
    public void eliminar() {
        if (SelectItem != null) {
            Messagebox.show(
                    "Desea eliminar este Tipo de Achivo?", "Dialogo de Confirmación ",
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event evt) throws Exception {
                            if (evt.getName().equals("onOK")) {
                                new MineTypeDao().delete(SelectItem);
                                dibujarMineType();
                            }
                        }
                    });
        } else {
            showNotify("Porfavor seleccione un item para Eliminar", win, "warning", "top_right");
        }
    }

    @Listen("onClick = #update")
    public void actualizar() {
        dibujarMineType();
    }
}
