package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.SubTipo;
import gob.peam.arcdig.beans.TipoDocumento;
import gob.peam.arcdig.dao.SubTipoDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;

public class WinSubTipo extends SelectorComposer<Component> {

    private EventQueue eq;

    @Wire
    Textbox gTxtBuscar;

    @Wire
    Div gDivVentana;

    @Wire
    Listbox gList, cmbSubTipo;
    SubTipo selected;

    @Listen("onClick = #gBtnNuevo")
    public void gBtnNuevo() {
        gDivVentana.setVisible(false);
        nDivVentana.setVisible(true);
    }

    //@Listen("onClick =#gBtnEditar")
   // public void gBtnEditar() {
    public void editarSubTipo(SubTipo item){
        if (gList.getSelectedItem() != null) {
            gDivVentana.setVisible(false);
            nDivVentana.setVisible(true);
            //SubTipo s = (SubTipo) gList.getSelectedItem().getAttribute("item");
            nTxtNombre.setValue(item.getNombre());
            op1 = 1;

        } else {
            showNotify("SELECCIONE un subtipo porfavor", win, "warning", "middle_center");
        }
    }

    @Listen("onClick = #gBtnActualizar")
    public void gBtnActualizar() {
        gTxtBuscar.setText("");
        buscar();
    }

    @Listen("onClick = #gBtnBuscar")
    public void buscar() {
        String query = " where  str_normalize(cont_nombre) ilike str_normalize('%" + gTxtBuscar.getValue().trim() + "%') or str_normalize(obra_nombre) ilike str_normalize('%" + gTxtBuscar.getValue().trim() + "%') ORDER BY obra_nombre ASC ";
        listarSubTipo(query);

    }

    @Listen(Events.ON_OK + " = #gTxtBuscar")
    public void gTxtBuscar(Event event) {
        buscar();
    }

    private void limpiarGrid() {

        gTxtBuscar.setText("");
    }

    private void listarSubTipo(String query) {
        List<SubTipo> datos = (List<SubTipo>) new ArrayList<SubTipo>();
        datos = new SubTipoDao().listarSubTipo(query, ((TipoDocumento) selectedTipo.getAttribute("item")).getId());

        gList.getItems().clear();
        int index = 0;

        for (SubTipo item : datos) {
            index++;

            Listitem listitem = new Listitem();
            listitem.setAttribute("item", item);

            Listcell nro = new Listcell();
            nro.setStyle("text-align:center; font-size: 7pt;");
            nro.appendChild(new Label(index + ""));

            Listcell cell1 = new Listcell();
            cell1.setStyle("text-align:left; font-size: 7pt;");
            cell1.appendChild(new Label(item.getNombre()));

            Listcell cell2 = new Listcell();

            Toolbarbutton botonEditar = new Toolbarbutton();
            botonEditar.setIconSclass("z-icon-pencil");//trash
            botonEditar.setWidth("16px");
            botonEditar.setHeight("16px");
            botonEditar.setTooltiptext("Editar");
            cell2.appendChild(botonEditar);

            Toolbarbutton botonEliminar = new Toolbarbutton();
            botonEliminar.setIconSclass("z-icon-trash-o");//trash
            botonEliminar.setWidth("16px");
            botonEliminar.setHeight("16px");
            botonEliminar.setTooltiptext("Eliminar");
            cell2.appendChild(botonEliminar);

            listitem.appendChild(nro);
            listitem.appendChild(cell1);
            listitem.appendChild(cell2);
            listitem.setAttribute("item", item);
            gList.appendChild(listitem);

            botonEditar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    editarSubTipo(item);

                }
            });

            botonEliminar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    eliminarSubTipo(item);
                }
            });
        }
    }

    //........................................................................
    @Wire
    Div nDivVentana;

    @Wire
    Textbox nTxtNombreSubTipo, nTxtTelefonoSubTipo, nTxtDetalleSubTipo, nTxtSubTipo;

    @Wire
    Datebox fecha;

    Integer op = 0, op1 = 0;

    public void eliminarSubTipo(SubTipo item) {
        Messagebox.show(
                "Desea eliminar este sub tipo?", "Dialogo de Confirmación ",
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event evt) throws Exception {
                if (evt.getName().equals("onOK")) {
                    new SubTipoDao().eliminarSubTipo(item.getId());
                    String query = " where  str_normalize(cont_nombre) ilike str_normalize('%" + gTxtBuscar.getValue().trim() + "%') or str_normalize(obra_nombre) ilike str_normalize('%" + gTxtBuscar.getValue().trim() + "%') ORDER BY obra_nombre ASC ";
                    listarSubTipo(query);
                }
            }
        });
    }

    @Listen("onClick = #nBtnGuardar")
    public void nBtnGuardar() {
        //String tipo = nListTipoPersona.getSelectedItem().getLabel();
        guardar();
    }

    @Listen("onClick = #nBtnSalir")
    public void nBtnSalir() {
        gDivVentana.setVisible(true);
        gDivVentana.setFocus(true);
        nDivVentana.setVisible(false);
        limpiarNuevo();
    }

    private void guardar() {

        if (!validarNuevo().equals("ok")) {
            showNotify(validarNuevo(), win, "warning", "top_center");
            return;
        }

        SubTipo bean = new SubTipo();

        bean.setNombre(nTxtNombre.getValue().toUpperCase().trim());
        bean.setTidoId(((TipoDocumento) selectedTipo.getAttribute("item")).getId());
        bean.setEstado(true);

        if (op1 == 0) {

            new SubTipoDao().insertarSubTipo(bean);
        } else {
            bean.setId(((SubTipo) gList.getSelectedItem().getAttribute("item")).getId());
            new SubTipoDao().updateSubTipo(bean);
        }

        gDivVentana.setVisible(true);
        gDivVentana.setFocus(true);
        nDivVentana.setVisible(false);

        buscar();
        limpiarNuevo();
        showNotify("Se guardó Satisfactoriamente", win, "info", "top_center");

    }

    private String validarNuevo() {

        if (nTxtNombre.getText().equals("")) {
            return "Debe ingresar el nombre de la SubTipo";
        }

        return "ok";
    }

    @Wire
    Textbox nTxtNombre;

    private void limpiarNuevo() {

        nTxtNombre.setText("");
        op1 = 0;
        buscar();
    }
    Listitem selectedTipo = new Listitem();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        Execution exec = Executions.getCurrent();
        selectedTipo = (Listitem) exec.getArg().get("item");
        //showNotify(((TipoDocumento) selectedTipo.getAttribute("item")).getId()+"", win, "info", "top_center");

        buscar();
        // dibujarSubTipo(0);
    }

    @Wire
    private Window win, winSubTipo;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

    @Wire
    Div label, label1;

    @Listen("onClick = #add")
    public void add() {
        nTxtSubTipo.setVisible(true);
        cmbSubTipo.setVisible(false);
        label1.setVisible(true);
        label.setVisible(false);
        op = 0;
    }

    @Listen("onClick =#cancel")
    public void cancel() {

        nTxtNombre.setValue("");
        label1.setVisible(false);
        label.setVisible(true);
    }

    @Listen("onClick =#edit")
    public void editar() {
        op = 1;
        nTxtSubTipo.setVisible(true);
        cmbSubTipo.setVisible(false);
        label1.setVisible(true);
        label.setVisible(false);

        SubTipo bean = new SubTipo();
        //showNotify(cmbSubTipo.getSelectedItem().getValue()+"", win, "info", "top_center");
        //bean = new SubTipoDao().get(cmbSubTipo.getSelectedItem().getValue().toString());
        nTxtSubTipo.setValue(bean.getNombre());

    }

    /* @Listen("onClick = #remove")
    public void remove() {
        Messagebox.show(
                "ESTA SEGURO DE ELIMINAR ITEM: " + cmbSubTipo.getSelectedItem().getLabel() + "?", "Aviso! ",
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.INFORMATION,
                new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event evt) throws Exception {
                if (evt.getName().equals("onOK")) {
                    new SubTipoDao().delete(cmbSubTipo.getSelectedItem().getValue().toString());
                    dibujarSubTipo(0);
                }
            }
        });
    }*/
}
