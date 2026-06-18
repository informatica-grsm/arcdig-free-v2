package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Categoria;
import gob.peam.arcdig.beans.Metadata;
import gob.peam.arcdig.beans.TipoDocumento;
import gob.peam.arcdig.dao.MetaDataDao;
import gob.peam.arcdig.dao.TipoDocumentoDao;
import gob.peam.arcdig.beans.TipoMetadata;
import gob.peam.arcdig.dao.CategoriaDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
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
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class AppTipoDocumento extends SelectorComposer<Component> {

    public AppTipoDocumento() {
    }

    List<TipoDocumento> items = (List<TipoDocumento>) new ArrayList<TipoDocumento>();
    private TipoDocumento SelectItem;
    private Boolean editar = false;
    private List<Metadata> metaData1 = (List<Metadata>) new ArrayList<Metadata>();
    private List<Metadata> metaData2 = (List<Metadata>) new ArrayList<Metadata>();

    Integer index1 = 0;
    Integer index2 = 0;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dibujarCateg();
        dibujarTipoDocumento();
    }

    @Wire
    Button aceptar;
    @Wire
    Bandbox filtro;
    @Wire
    Listbox listTipoDocumento, listCategoria, listCateg;

    @Wire
    Grid formulario;
    @Wire
    Hlayout control;
    @Wire
    Textbox nombre, descripcion, tFormato;

    @Wire
    Label nombError, desError;
    @Wire
    Checkbox estado;
    @Wire
    Rows rows, rowsY;
    @Wire
    Grid gridX, gridY;

    public void dibujarTipoDocumento() {
        String query = "";
        if (listCateg.getSelectedItem().getLabel().equals("--TODOS--")) {
            query = " where td.tido_nombre ilike '%" + filtro.getValue() + "%' and td.tido_estado = true and td.cate_id = c.cate_id ";
        } else {
            query = " where td.tido_nombre ilike '%" + filtro.getValue() + "%' and td.cate_id = '" + listCateg.getSelectedItem().getValue() + "' and td.tido_estado = true and td.cate_id = c.cate_id ";
        }
        items = new TipoDocumentoDao().listarTipoDocumentoCateg(query);
        listTipoDocumento.getItems().clear();
        int index = 0;

        for (TipoDocumento item : items) {
            index++;
            Listitem listitem = new Listitem();
            listitem.setAttribute("item", item);

            Listcell check = new Listcell();
            check.appendChild(new Label(""));

            Listcell nro = new Listcell();
            nro.appendChild(new Label(index + ""));
            //nro.appendChild(new Label(item.getId() + ""));

            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(item.getNombre()));

            Listcell detalle = new Listcell();
            detalle.appendChild(new Label(item.getDescripcion()));

            Listcell categoria = new Listcell();
            categoria.appendChild(new Label(item.getCateNombre()));

            Listcell estado = new Listcell();
            Image imgEstado = new Image();
            imgEstado.setSrc("./resources/img/true.png");
            imgEstado.setStyle("border:0px");
            estado.appendChild(imgEstado);
            Toolbarbutton botonSubTipo = new Toolbarbutton();

            Listcell subtipo = new Listcell();
            botonSubTipo.setIconSclass("z-icon-cog");
            botonSubTipo.setWidth("16px");
            botonSubTipo.setHeight("16px");
            subtipo.appendChild(botonSubTipo);
            final Listitem i = listitem;
            
            botonSubTipo.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    HashMap hm = new HashMap();
                    hm.put("item", i);
                    Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winSubTipo.zul", null, hm);
                    window2.doModal();
                }
            });

            listitem.appendChild(check);
            listitem.appendChild(nro);
            listitem.appendChild(nombre);
            listitem.appendChild(detalle);
            listitem.appendChild(categoria);
            listitem.appendChild(estado);
            listitem.appendChild(subtipo);

            listTipoDocumento.appendChild(listitem);
        }
    }

    public void dibujarCategoria() {
        List<Categoria> data = (List<Categoria>) new ArrayList<Categoria>();
        data = new CategoriaDao().listarCategorias();
        listCategoria.getItems().clear();
        for (Categoria item : data) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getCateId());
            listitem.setLabel(item.getCateNombre());
            listCategoria.appendChild(listitem);
        }
        listCategoria.setSelectedIndex(0);
    }

    public void dibujarCateg() {
        List<Categoria> data = (List<Categoria>) new ArrayList<Categoria>();
        data = new CategoriaDao().listarCategorias();
        listCateg.getItems().clear();

        Listitem listitem1 = new Listitem();
        listitem1.setValue(0);
        listitem1.setLabel("--TODOS--");
        listCateg.appendChild(listitem1);

        for (Categoria item : data) {

            Listitem listitem = new Listitem();
            listitem.setValue(item.getCateId());
            listitem.setLabel(item.getCateNombre());
            listCateg.appendChild(listitem);
        }
        listCateg.setSelectedItem(listitem1);
    }

    @Listen("onSelect = #listTipoDocumento")
    public void select() {
        Set item = listTipoDocumento.getSelectedItems();
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
        tFormato.setValue("");
        estado.setChecked(true);
        rows.getChildren().clear();
        metaData1.clear();
        index1 = 0;
        rowsY.getChildren().clear();
        metaData2.clear();
        index2 = 0;
    }

    @Listen("onClick = #cancelar")
    public void cancelar() {
        listTipoDocumento.setVisible(true);
        formulario.setVisible(false);
        control.setVisible(false);
    }

    @Wire
    Button privilegio;

    @Listen("onClick=#aceptar")
    public void aceptar() {
        desError.setValue("");
        nombError.setValue("");

        if (!"".equals(nombre.getValue())) {
            TipoDocumento bean = new TipoDocumento();
            bean.setNombre(nombre.getValue().toUpperCase());
            bean.setDescripcion(descripcion.getValue().toUpperCase());
            bean.setEstado(estado.isChecked());
            Integer i = 0;
            String meta = "";
            int k = 0;
            if ("z-icon-globe".equals(privilegio.getIconSclass())) {
                bean.setPrivacidad(1);
            } else if ("z-icon-user".equals(privilegio.getIconSclass())) {
                bean.setPrivacidad(2);
            } else {
                bean.setPrivacidad(3);
            }
            bean.setMetadata(tFormato.getValue());
            bean.setCateId(Integer.parseInt(listCategoria.getSelectedItem().getValue().toString()));

            if (editar) {
                bean.setId(SelectItem.getId());
                i = new TipoDocumentoDao().getRepetidoUpdate(bean);
            } else {
                i = new TipoDocumentoDao().getRepetido(bean);
            }
            if (i == 0) {
                if (editar) {
                    new TipoDocumentoDao().update(bean);
                } else {
                    new TipoDocumentoDao().insert(bean);
                    bean.setId(new TipoDocumentoDao().getMaxId());
                }

                new MetaDataDao().deleteTipoMeta(bean.getId());
                for (int j = 0; j < metaData2.size(); j++) {
                    boolean oblig = false;
                    String tipo = "string";
                    if (!"".equals(metaData2.get(j).getCampo())) {
                        if (metaData2.get(j).getObligado() != null) {
                            oblig = metaData2.get(j).getObligado();
                        }
                        if (metaData2.get(j).getTipoDato() != null) {
                            tipo = metaData2.get(j).getTipoDato();
                        }
                        TipoMetadata beanT = new TipoMetadata();
                        beanT.setTidoId(bean.getId());
                        beanT.setId(Integer.parseInt(metaData2.get(j).getCampo()));
                        beanT.setTimeEstado(true);
                        beanT.setTidoVersion(1);
                        beanT.setTipoDato(tipo);
                        beanT.setObligado(oblig);
                        beanT.setDefecto(metaData2.get(j).getDefecto());
                        beanT.setSecuencia(metaData2.get(j).getSecuencia());
                        new MetaDataDao().insertTipoMeta(beanT);
                    }
                }

                listTipoDocumento.setVisible(true);
                formulario.setVisible(false);
                control.setVisible(false);
                //dibujarTipoDocumento();
            } else {
                nombError.setValue("El nombre se esta repitiendo con otro registro");
            }
        } else {
            nombError.setValue("El nombre no puede dejarse en blanco");
        }
    }

    @Listen("onClick = #editar")
    public void editar() {
        dibujarCategoria();
        if (SelectItem != null) {
            rows.getChildren().clear();
            listTipoDocumento.setVisible(false);
            formulario.setVisible(true);
            control.setVisible(true);
            nombre.setValue(SelectItem.getNombre());
            descripcion.setValue(SelectItem.getDescripcion());
            estado.setChecked(SelectItem.getEstado());
            tFormato.setValue(SelectItem.getMetadata());

            if (SelectItem.getPrivacidad() == 1) {
                privilegio.setIconSclass("z-icon-globe");
            } else if (SelectItem.getPrivacidad() == 2) {
                privilegio.setIconSclass("z-icon-user");
            } else {
                privilegio.setIconSclass("z-icon-cog");
            }

            for (Listitem match : listCategoria.getItems()) {
                if (Objects.equals(SelectItem.getCateId(), match.getValue())) {
                    listCategoria.setSelectedItem(match);
                    break;
                }
            }

            editar = true;
            index1 = 0;
            metaData1.clear();

            gridY.setVisible(false);
            rowsY.getChildren().clear();
            metaData2.clear();
            index2 = 0;

            for (TipoMetadata tp : new MetaDataDao().listarTipoMetadata(SelectItem.getId())) {
                gridY.setVisible(true);
                metaData2.add(new Metadata(index2, tp.getId() + "", "", tp.getSecuencia()));
                Row row = new Row();
                Listbox lb = new Listbox();
                List<Metadata> lmd = (List<Metadata>) new ArrayList<Metadata>();
                lmd = new MetaDataDao().listarMetadataCombo();
                for (Metadata m : lmd) {
                    Listitem li = new Listitem();
                    li.setValue(m.getId());
                    li.setLabel(m.getCampo());
                    if (tp.getId() == m.getId()) {
                        li.setSelected(true);
                    }
                    lb.appendChild(li);
                }

                lb.setMold("select");
                lb.setHeight("25px");
                lb.setStyle("border-radius:4px; border:1px solid #CFCFCF;");
                lb.addEventListener(Events.ON_SELECT, new gob.peam.arcdig.view.AppTipoDocumento.changeListbox(index2));

                Listbox lb1 = new Listbox();
                lb1.setMold("select");
                lb1.setHeight("25px");
                lb1.setStyle("border-radius:4px; border:1px solid #CFCFCF;");
                Listitem li = new Listitem();

                li.setValue("string");
                li.setLabel("Texto simple");
                if ("string".equals(tp.getTipoDato())) {
                    li.setSelected(true);
                    metaData2.get(index2).setTipoDato(li.getValue().toString());
                }
                lb1.appendChild(li);
                Listitem li1 = new Listitem();
                li1.setValue("text");
                li1.setLabel("Texto Complejo");
                if ("text".equals(tp.getTipoDato())) {
                    li1.setSelected(true);
                    metaData2.get(index2).setTipoDato(li1.getValue().toString());
                }
                lb1.appendChild(li1);
                Listitem li2 = new Listitem();
                li2.setValue("date");
                li2.setLabel("Fecha");
                if ("date".equals(tp.getTipoDato())) {
                    li2.setSelected(true);
                    metaData2.get(index2).setTipoDato(li2.getValue().toString());
                }
                lb1.appendChild(li2);
                lb1.addEventListener(Events.ON_SELECT, new gob.peam.arcdig.view.AppTipoDocumento.changeListbox1(index2));

                Textbox tbs = new Textbox();
                tbs.setWidth("40px");
                tbs.setMaxlength(2);
                tbs.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.AppTipoDocumento.changeTextbox4(index2));
                tbs.setValue(tp.getSecuencia().toString());
                metaData2.get(index2).setSecuencia(tp.getSecuencia());

                Textbox tb = new Textbox();
                tb.setWidth("100%");
                tb.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.AppTipoDocumento.changeTextbox3(index2));
                tb.setValue(tp.getDefecto());
                metaData2.get(index2).setDefecto(tp.getDefecto());

                Checkbox ch = new Checkbox();
                ch.addEventListener(Events.ON_CHECK, new gob.peam.arcdig.view.AppTipoDocumento.changeCheck(index2));
                ch.setChecked(tp.getObligado());
                metaData2.get(index2).setObligado(tp.getObligado());

                Button b1 = new Button();
                b1.setIconSclass("z-icon-minus");
                b1.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.AppTipoDocumento.borrarMetadata1(metaData2.get(index2), row));
                index2++;

                row.appendChild(lb);
                row.appendChild(lb1);
                row.appendChild(tbs);
                row.appendChild(tb);
                row.appendChild(ch);
                row.appendChild(b1);

                rowsY.appendChild(row);
            }
        } else {
            showNotify("Porfavor seleccione un item para Editar", win, "warning", "top_right");
        }

    }

    class borrarMetadata implements EventListener {

        final Metadata m;
        final Row row;

        public borrarMetadata(Metadata m, Row row) {
            this.m = m;
            this.row = row;
        }

        public void onEvent(Event event) throws Exception {
            row.detach();
            metaData1.get(m.getId()).setCampo("");
            metaData1.get(m.getId()).setDetalle("");
        }
    }

    class borrarMetadata1 implements EventListener {

        final Metadata m;
        final Row row;

        public borrarMetadata1(Metadata m, Row row) {
            this.m = m;
            this.row = row;
        }

        public void onEvent(Event event) throws Exception {
            row.detach();
            metaData2.get(m.getId()).setCampo("");
        }
    }

    @Listen("onClick = #nuevo")
    public void nuevo() {
        rows.getChildren().clear();
        gridX.setVisible(false);
        listTipoDocumento.setVisible(false);
        formulario.setVisible(true);
        control.setVisible(true);
        nombre.setValue("");
        descripcion.setValue("");
        estado.setChecked(true);
        index1 = 0;
        metaData1.clear();
        tFormato.setValue("");
        gridY.setVisible(false);
        rowsY.getChildren().clear();
        metaData2.clear();
        index2 = 0;
        editar = false;
        dibujarCategoria();
    }

    @Listen("onClick = #eliminar")
    public void eliminar() {
        if (SelectItem != null) {
            Messagebox.show(
                    "Desea eliminar este Tipo de Documento?", "Dialogo de Confirmación ",
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event evt) throws Exception {
                    if (evt.getName().equals("onOK")) {
                        new TipoDocumentoDao().delete(SelectItem);
                        dibujarTipoDocumento();
                    }
                }
            });
        } else {
            showNotify("Porfavor seleccione un item para Eliminar", win, "warning", "top_right");
        }
    }

    @Listen("onClick = #update")
    public void actualizar() {
        listCateg.setSelectedIndex(0);
        filtro.setValue("");
        dibujarTipoDocumento();
    }

    @Listen("onChange = #filtro")
    public void filtro() {
        dibujarTipoDocumento();
    }

    @Listen("onSelect = #listCateg")
    public void filtroCateg() {
        dibujarTipoDocumento();
    }

    @Listen("onClick = #addRow")
    public void addRow() {
        gridX.setVisible(true);
        final Row row = new Row();
        final Textbox tb = new Textbox();
        metaData1.add(new Metadata(index1, "", "", null));
        tb.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.AppTipoDocumento.changeTextbox1(index1));

        tb.setPlaceholder("Escribir Texto");
        tb.setWidth("130px");
        final Textbox tb1 = new Textbox();
        tb1.setPlaceholder("Escribir Texto");
        tb1.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.AppTipoDocumento.changeTextbox2(index1));

        tb1.setWidth("130px");
        Button b1 = new Button();
        b1.setIconSclass("z-icon-minus");
        b1.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.AppTipoDocumento.borrarMetadata(metaData1.get(index1), row));
        index1++;

        row.appendChild(tb);
        row.appendChild(tb1);
        row.appendChild(b1);

        rows.appendChild(row);
    }

    @Listen("onClick = #addRowMeta")
    public void addRowMetada() {
        gridY.setVisible(true);
        final Row row = new Row();
        metaData2.add(new Metadata(index2, "", "", null));

        Listbox lb = new Listbox();
        List<Metadata> lmd = (List<Metadata>) new ArrayList<Metadata>();
        lmd = new MetaDataDao().listarMetadataCombo();
        for (Metadata m : lmd) {
            Listitem li = new Listitem();
            li.setValue(m.getId());
            li.setLabel(m.getCampo());
            lb.appendChild(li);
        }

        lb.setMold("select");
        lb.setHeight("24px");
        lb.setStyle("border-radius:4px; border:1px solid #CFCFCF;");
        lb.addEventListener(Events.ON_SELECT, new gob.peam.arcdig.view.AppTipoDocumento.changeListbox(index2));

        Listbox lb1 = new Listbox();
        lb1.setMold("select");
        lb1.setHeight("24px");
        lb1.setStyle("border-radius:4px; border:1px solid #CFCFCF;");
        Listitem li = new Listitem();

        li.setValue("string");
        li.setLabel("Texto simple");
        li.setSelected(true);
        lb1.appendChild(li);
        Listitem li1 = new Listitem();
        li1.setValue("text");
        li1.setLabel("Texto Complejo");
        lb1.appendChild(li1);
        Listitem li2 = new Listitem();
        li2.setValue("date");
        li2.setLabel("Fecha");
        lb1.appendChild(li2);

        lb1.addEventListener(Events.ON_SELECT, new gob.peam.arcdig.view.AppTipoDocumento.changeListbox1(index2));

        Textbox tbs = new Textbox();
        tbs.setWidth("40px");
        tbs.setMaxlength(2);
        tbs.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.AppTipoDocumento.changeTextbox4(index2));

        Textbox tb = new Textbox();
        tb.setWidth("100%");
        tb.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.AppTipoDocumento.changeTextbox3(index2));

        Checkbox ch = new Checkbox();
        ch.addEventListener(Events.ON_CHECK, new gob.peam.arcdig.view.AppTipoDocumento.changeCheck(index2));

        Button b1 = new Button();
        b1.setIconSclass("z-icon-minus");
        b1.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.AppTipoDocumento.borrarMetadata1(metaData2.get(index2), row));
        index2++;

        row.appendChild(lb);
        row.appendChild(lb1);
        row.appendChild(tbs);
        row.appendChild(tb);
        row.appendChild(ch);
        row.appendChild(b1);

        rowsY.appendChild(row);
    }

    class changeTextbox1 implements EventListener<InputEvent> {

        final Integer i;

        public changeTextbox1(Integer i) {
            this.i = i;
        }

        public void onEvent(InputEvent event) throws Exception {
            metaData1.get(i).setCampo(event.getValue());
        }
    }

    class changeTextbox2 implements EventListener<InputEvent> {

        final Integer i;

        public changeTextbox2(Integer i) {
            this.i = i;
        }

        public void onEvent(InputEvent event) throws Exception {
            metaData1.get(i).setDetalle(event.getValue());
        }
    }

    class changeTextbox3 implements EventListener<InputEvent> {

        final Integer i;

        public changeTextbox3(Integer i) {
            this.i = i;
        }

        public void onEvent(InputEvent event) throws Exception {
            metaData2.get(i).setDefecto(event.getValue());
        }
    }

    class changeTextbox4 implements EventListener<InputEvent> {

        final Integer i;

        public changeTextbox4(Integer i) {
            this.i = i;
        }

        public void onEvent(InputEvent event) throws Exception {
            try {
                //char enter = event.getValue().charAt(0);
                //if (Character.isDigit(enter)) {
                metaData2.get(i).setSecuencia(Integer.parseInt(event.getValue().toString()));
                //}
            } catch (Exception e) {
                metaData2.get(i).setSecuencia(0);
            }
        }

    }

    class changeListbox implements EventListener<SelectEvent> {

        final Integer i;

        public changeListbox(Integer i) {
            this.i = i;
        }

        public void onEvent(SelectEvent event) throws Exception {
            Set item = event.getSelectedItems();
            List<Listitem> emp = new ArrayList<Listitem>(item);
            metaData2.get(i).setCampo(emp.get(0).getValue().toString());
        }
    }

    class changeListbox1 implements EventListener<SelectEvent> {

        final Integer i;

        public changeListbox1(Integer i) {
            this.i = i;
        }

        public void onEvent(SelectEvent event) throws Exception {
            Set item = event.getSelectedItems();
            List<Listitem> emp = new ArrayList<Listitem>(item);
            metaData2.get(i).setTipoDato(emp.get(0).getValue().toString());
        }
    }

    class changeCheck implements EventListener<CheckEvent> {

        final Integer i;

        public changeCheck(Integer i) {
            this.i = i;
        }

        public void onEvent(CheckEvent event) throws Exception {
            metaData2.get(i).setObligado(event.isChecked());
        }
    }

}
