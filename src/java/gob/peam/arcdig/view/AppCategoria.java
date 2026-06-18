package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Categoria;
import gob.peam.arcdig.dao.CategoriaDao;
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

public class AppCategoria extends SelectorComposer<Component> {

    public AppCategoria() {
    }
    List<Categoria> items = (List<Categoria>) new ArrayList<Categoria>();
    private Categoria SelectItem;
    private Boolean editar = false;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dibujarCategoria();
    }

    @Wire
    Button aceptar;
    @Wire
    Bandbox filtro;
    @Wire
    Listbox listCategoria;
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

    public void dibujarCategoria() {
        items = new CategoriaDao().listarCategoria(filtro.getValue());
        listCategoria.getItems().clear();
        int index = 0;

        for (Categoria item : items) {
            index++;
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(String.valueOf(index)));
            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(item.getCateNombre()));
            Listcell descripcion = new Listcell();
            descripcion.appendChild(new Label(item.getCateDescripcion()));

            Listcell estado = new Listcell();
            Image imgEstado = new Image();
            imgEstado.setSrc("./resources/img/" + item.getCateEstado() + ".png");
            imgEstado.setStyle("border:0px");
            estado.appendChild(imgEstado);
            listitem.appendChild(check);
            listitem.appendChild(nro);
            listitem.appendChild(nombre);
            listitem.appendChild(descripcion);
            listitem.appendChild(estado);

            listCategoria.appendChild(listitem);
        }
    }

    @Listen("onSelect = #listCategoria")
    public void select() {
        Set item = listCategoria.getSelectedItems();
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
        listCategoria.setVisible(true);
        formulario.setVisible(false);
        control.setVisible(false);
    }

    @Listen("onClick=#aceptar")
    public void aceptar() {
        desError.setValue("");
        nombError.setValue("");
        if (!"".equals(nombre.getValue())) {

            Categoria bean = new Categoria();
            bean.setCateNombre(nombre.getValue().toUpperCase());
            bean.setCateDescripcion(descripcion.getValue().toUpperCase());
            bean.setCateEstado(estado.isChecked());
            Integer i = 0;
                if (editar) {
                    bean.setCateId(SelectItem.getCateId());
                    i = new CategoriaDao().getRepetidoUpdate(bean);
                    //showNotify("editar", win, "info", "top_center");
                } else {
                    i = new CategoriaDao().getRepetido(bean);
                    //showNotify("nuevo", win, "info", "top_center");
                }
            if (i == 0) {
                if (editar) {
                    bean.setCateId(SelectItem.getCateId());
                    new CategoriaDao().update(bean);
                } else {
                    new CategoriaDao().insert(bean);
                }
                listCategoria.setVisible(true);
                formulario.setVisible(false);
                control.setVisible(false);
                dibujarCategoria();
            } else {
                nombError.setValue("El nombre se esta repitiendo con otro registro");
            }
        } else {
            nombError.setValue("El nombre no puede dejarse en blanco");
        }
    }

    @Listen("onClick = #editar")
    public void editar() {
        desError.setValue("");
        nombError.setValue("");
        if (SelectItem != null) {
            listCategoria.setVisible(false);
            formulario.setVisible(true);
            control.setVisible(true);
            nombre.setValue(SelectItem.getCateNombre());
            descripcion.setValue(SelectItem.getCateDescripcion());
            estado.setChecked(SelectItem.getCateEstado());
            editar = true;
        } else {
            showNotify("Porfavor seleccione un item para Editar", win, "warning", "top_right");
        }

    }

    @Listen("onClick = #nuevo")
    public void nuevo() {
        listCategoria.setVisible(false);
        formulario.setVisible(true);
        control.setVisible(true);
        nombre.setValue("");
        descripcion.setValue("");
        estado.setChecked(true);
        editar = false;
    }

    @Listen("onClick = #eliminar")
    public void eliminar() {
        if (SelectItem != null) {
            Messagebox.show(
                    "Desea eliminar esta Categoria?", "Dialogo de Confirmación ",
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event evt) throws Exception {
                            if (evt.getName().equals("onOK")) {
                                new CategoriaDao().delete(SelectItem);
                                dibujarCategoria();
                            }
                        }
                    });
        } else {
            showNotify("Porfavor seleccione un item para Eliminar", win, "warning", "top_right");
        }
    }

    @Listen("onClick = #update")
    public void actualizar() {
        dibujarCategoria();
    }

    @Listen("onChange = #filtro")
    public void buscar() {
        dibujarCategoria();
    }
        
}
