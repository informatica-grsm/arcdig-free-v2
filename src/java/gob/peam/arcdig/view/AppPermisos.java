package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Permiso;
import gob.peam.arcdig.beans.Persona;
import gob.peam.arcdig.dao.PermisoDao;
import gob.peam.arcdig.dao.PersonaDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

public class AppPermisos extends SelectorComposer<Component> {

    public AppPermisos() {
    }
    List<Persona> items = (List<Persona>) new ArrayList<Persona>();
    private Persona SelectItem;
    private Boolean editar = false;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dibujarPersonas();
        
        
    }

    @Wire
    Button aceptar;
    @Wire
    Bandbox filtro;
    @Wire
    Listbox listUsuario, ltipo, lb;
    @Wire
    Vlayout formulario;
    @Wire
    Hlayout control;
    @Wire
    Listitem item1, item2, item3, item4, item5, item6, item7, item8, item9, item10;
    @Wire
    Radiogroup g1;

    public void dibujarPersonas() {
        String query = " WHERE p.pers_dni||' '||p.pers_apellido_paterno||' '||p.pers_apellido_materno||', '||p.pers_nombre ilike '%" + filtro.getValue() + "%'   ";
        items = new PersonaDao().listarPersonalOficina(query);
        listUsuario.getItems().clear();
        int index = 0;

        for (Persona item : items) {
            index++;
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(String.valueOf(index)));
            Listcell dni = new Listcell();
            dni.appendChild(new Label(item.getDni()));

            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(item.getNombre()));

            listitem.appendChild(check);
            listitem.appendChild(nro);
            listitem.appendChild(dni);
            listitem.appendChild(nombre);
            listUsuario.appendChild(listitem);
        }
    }

    @Listen("onSelect = #listUsuario")
    public void select() {
        Set item = listUsuario.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            SelectItem = items.get(emp.get(i).getIndex());
        }
    }

    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 4000);
    }

    @Listen("onClick = #cancelar")
    public void cancelar() {
        listUsuario.setVisible(true);
        formulario.setVisible(false);
        control.setVisible(false);
    }

    @Listen("onClick=#aceptar")
    public void aceptar() {
        Permiso perm = new Permiso();
        perm.setDni(SelectItem.getDni());
        perm.setActivo(Boolean.parseBoolean(g1.getSelectedItem().getValue().toString()));
        perm.setItem1(item1.isSelected());
        perm.setItem2(item2.isSelected());
        perm.setItem3(item3.isSelected());
        perm.setItem4(item4.isSelected());
        perm.setItem5(item5.isSelected());
        perm.setItem6(item6.isSelected());
        perm.setItem7(item7.isSelected());
        perm.setItem8(item8.isSelected());
        perm.setItem9(item9.isSelected());
        perm.setItem10(item10.isSelected());

        perm.setTipo(Integer.parseInt(ltipo.getSelectedItem().getValue().toString()));
        new PermisoDao().delete(perm);
        new PermisoDao().insert(perm);
        listUsuario.setVisible(true);
        formulario.setVisible(false);
        control.setVisible(false);

    }

    @Listen("onClick = #acceso")
    public void acceso() {
        if (SelectItem != null) {
            listUsuario.setVisible(false);
            formulario.setVisible(true);
            control.setVisible(true);
            editar = true;
            Permiso perm = new PermisoDao().getPermisos(SelectItem.getDni());
            try {
                if (perm.getTipo() == 1) {
                    ltipo.setSelectedIndex(0);
                } else {
                    ltipo.setSelectedIndex(1);
                }

                item1.setSelected(perm.getItem1());
                item2.setSelected(perm.getItem2());
                item3.setSelected(perm.getItem3());
                item4.setSelected(perm.getItem4());
                item5.setSelected(perm.getItem5());
                item6.setSelected(perm.getItem6());
                item7.setSelected(perm.getItem7());
                item8.setSelected(perm.getItem8());
                item9.setSelected(perm.getItem9());
                item10.setSelected(perm.getItem10());

                if (perm.getActivo()) {
                    g1.setSelectedIndex(0);
                } else {
                    g1.setSelectedIndex(1);
                }
            } catch (NullPointerException ex) {

            }
        } else {
            showNotify("Porfavor seleccione un item para Configurar", win, "warning", "top_right");
        }

    }

    @Listen("onClick = #update")
    public void actualizar() {
        dibujarPersonas();
    }

    @Listen("onChange = #filtro")
    public void buscar() {

        dibujarPersonas();
    }

    @Listen("onSelect = #ltipo")
    public void onSelectltipo() {
        if ("1".equals(ltipo.getSelectedItem().getValue().toString())) {
            item1.setSelected(true);
            item2.setSelected(true);
            item3.setSelected(true);
            item4.setSelected(true);
            item5.setSelected(true);
            item6.setSelected(true);
            item7.setSelected(false);
            item8.setSelected(true);
            item9.setSelected(true);
            item10.setSelected(true);
        } else {
            item1.setSelected(false);
            item2.setSelected(false);
            item3.setSelected(false);
            item4.setSelected(false);
            item5.setSelected(false);
            item6.setSelected(false);
            item7.setSelected(false);
            item8.setSelected(true);
            item9.setSelected(false);
            item10.setSelected(false);
        }
    }

    @Listen("onClick = #nuevo")
    public void nuevo() {
        HashMap hm = new HashMap();
        hm.put("option", 1);
        hm.put("id", 0);

        Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winUsuario.zul", null, hm);
        window2.doModal();
    }

    @Listen("onClick = #edit")
    public void editar() {
        HashMap hm = new HashMap();
        hm.put("option", 2);
        hm.put("id", SelectItem.getIdPersona());

        Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winUsuario.zul", null, hm);
        window2.doModal();
    }

}
