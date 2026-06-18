package gob.peam.arcdig.view;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Catalogo;
import gob.peam.arcdig.beans.Categoria;
import gob.peam.arcdig.beans.TipoDocumento;
import gob.peam.arcdig.dao.CatalogoDao;
import gob.peam.arcdig.dao.CategoriaDao;
import gob.peam.arcdig.dao.PermisoDao;
import gob.peam.arcdig.dao.TipoDocumentoDao;
import gob.peam.arcdig.utils.FormatoFecha;
import java.io.File;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class Permiso extends SelectorComposer<Component> {

    @Wire
    Div nav;
    @Wire
    Include contenido;
    @Wire
    Menuitem firts, oscinfor;
    /* @Wire
     Label subModulo;*/
    @Wire
    Listbox tiempoSearch, catalogoSearch, categoriaSearch, tipoSearch;
    String query = "", query1 = "", query2 = "", query3 = "";
    String[] qlist = new String[4];
    @Wire
    Label credito, nombre;
    @Wire
    Datebox fechaFinal, fechaInicio;
    @Wire
    Div header;

    @Listen("onClick= #header, #link")
    public void home() {
        Executions.sendRedirect("./zul?url=inicio");
    }

    private EventQueue eq;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date fecha = new Date(new Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int anho = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es", "PE"));
        String weekdays[] = dfs.getWeekdays();
        String nameOfDay = weekdays[day];
        String nameDay = nameOfDay.substring(0, 1).toUpperCase() + nameOfDay.substring(1);

        gob.peam.arcdig.beans.Permiso perm = new gob.peam.arcdig.beans.Permiso();
        try {
            String dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
            String nombre = Executions.getCurrent().getDesktop().getSession().getAttribute("nombreUsuario").toString();
            credito.setValue("@Todos los Derechos Reservados Gobierno Regional de San Martin - " + anho);
            this.nombre.setValue("Bienvenid@ " + nombre + "; Hoy: " + nameDay + ", " + new FormatoFecha().fechayHora(fecha));
            perm = new PermisoDao().getPermisos(dni);

            //verificando si tiene permisoss para acceso a oscinfor
            try {
                if (perm.getItem9()) {
                    oscinfor.setVisible(false);
                }
            } catch (NullPointerException ex) {
                oscinfor.setVisible(false);
            }

            //verificando. . . si existe o esta montado los Catalogos
            String path = new ConexionReporte().obtenerCarpeta("carpeta");

            //String root = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/"+path;
            Boolean mostrar = false;

            for (Catalogo cata : new CatalogoDao().listarCatalogo("")) {
                File file = new File(path + "/" + cata.getCataCarpeta());
                if (file.exists()) {
                    mostrar = true;
                    break;
                } else {
                    mostrar = false;
                }
            }
            /* if (!mostrar) {
             Messagebox.show(
             "Los Catalogos estan en mantenimiento, consulte con soporte técnico para mas información (PEAM)", "!Aviso",
             Messagebox.OK,
             Messagebox.INFORMATION,
             new org.zkoss.zk.ui.event.EventListener() {
             @Override
             public void onEvent(Event evt) throws Exception {
             if (evt.getName().equals("onOK")) {
             Executions.getCurrent().getDesktop().getSession().invalidate();
             Executions.getCurrent().sendRedirect("./");
             }
             }
             });
             }*/
            //verficando si la cuenta esta ACtiva para ArcDig o tiene permisos
            try {
                if (!perm.getActivo()) {
                    Messagebox.show(
                            "Su Cuenta no tiene acceso a este sistema, puede solicitarlo con soporte técnico.", "!Aviso",
                            Messagebox.OK,
                            Messagebox.INFORMATION,
                            new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event evt) throws Exception {
                            if (evt.getName().equals("onOK")) {
                                Executions.getCurrent().getDesktop().getSession().invalidate();
                                Executions.getCurrent().sendRedirect("./");
                            }
                        }
                    });
                } else {
                    nav.setVisible(true);
                    contenido.setVisible(true);
                    if (perm.getItem2()) {
                        firts.setVisible(false);
                    }
                    listTiempo();
                    listCatalogo();
                    listCategoria();
                    dibujarTipoDocumento();
                }
            } catch (NullPointerException ex) {
                Messagebox.show(
                        "Su Cuenta no tiene acceso a este sistema, puede solicitarlo con soporte técnico.", "!Aviso",
                        Messagebox.OK,
                        Messagebox.INFORMATION,
                        new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event evt) throws Exception {
                        if (evt.getName().equals("onOK")) {
                            Executions.getCurrent().getDesktop().getSession().invalidate();
                            Executions.getCurrent().sendRedirect("./");
                        }
                    }
                });
            }
        } catch (NullPointerException ex) {

        }
    }

    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

    public void listTiempo() {
        Listitem item1 = new Listitem();
        item1.setLabel("Todo");
        item1.setValue(0);
        item1.setSelected(true);

        Listitem item2 = new Listitem();
        item2.setLabel("1 día");
        item2.setValue(1);
        Listitem item3 = new Listitem();
        item3.setLabel("1 Semana");
        item3.setValue(2);
        Listitem item4 = new Listitem();
        item4.setLabel("1 Mes");
        item4.setValue(3);
        tiempoSearch.appendChild(item1);
        tiempoSearch.appendChild(item2);
        tiempoSearch.appendChild(item3);
        tiempoSearch.appendChild(item4);
    }

    public void listCatalogo() {
        List<Catalogo> items = (List<Catalogo>) new ArrayList<Catalogo>();
        items = new CatalogoDao().listarComboCatalogo();
        catalogoSearch.getItems().clear();
        Listitem listitemz = new Listitem();
        listitemz.setValue(0);
        listitemz.setLabel("Todo");
        listitemz.setSelected(true);
        catalogoSearch.appendChild(listitemz);
        for (Catalogo item : items) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getCataId());
            listitem.setLabel(item.getCataNombre());
            catalogoSearch.appendChild(listitem);
        }
    }

    public void listCategoria() {
        List<Categoria> items = (List<Categoria>) new ArrayList<Categoria>();

        items = new CategoriaDao().listarCategorias();
        categoriaSearch.getItems().clear();
        Listitem listitemz = new Listitem();
        listitemz.setValue(0);
        listitemz.setLabel("Todo");
        listitemz.setSelected(true);
        categoriaSearch.appendChild(listitemz);
        for (Categoria item : items) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getCateId());
            listitem.setLabel(item.getCateNombre());
            categoriaSearch.appendChild(listitem);
        }
    }

    public void dibujarTipoDocumento() {
        List<TipoDocumento> items = (List<TipoDocumento>) new ArrayList<TipoDocumento>();

        items = new TipoDocumentoDao().listarTipoDocumento();
        tipoSearch.getItems().clear();
        Listitem listitemz = new Listitem();
        listitemz.setValue(0);
        listitemz.setLabel("Todo");
        listitemz.setSelected(true);
        tipoSearch.appendChild(listitemz);
        for (TipoDocumento item : items) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getId());
            listitem.setLabel(item.getNombre());
            tipoSearch.appendChild(listitem);
        }
    }

    @Listen("onChange = #fechaInicio, #fechaFinal")
    public void changeFecha(InputEvent evt) {
        //showNotify("hola", nav, query, query);
        if (fechaInicio.getValue() == null && fechaFinal.getValue() == null) {
            qlist[1] = "";
        } else {
            Date fi, ff;
            try {
                fi = fechaInicio.getValue();
                ff = fechaFinal.getValue();

                qlist[1] = "cast(documento.docu_fecha_docx as date) >='" + fi + "' and cast(documento.docu_fecha_docx as date) <= '" + ff + "' ";

            } catch (NullPointerException ex) {
                try {
                    if (fechaInicio.getValue().toString().equals("")) {
                        qlist[1] = "cast(documento.docu_fecha_docx as date)>='" + fechaInicio.getValue() + "' ";
                    }
                } catch (NullPointerException exa) {
                    qlist[1] = "cast(documento.docu_fecha_docx as date)<='" + fechaFinal.getValue() + "' ";
                }
            }

            query = "";
            for (int i = 0; i < 4; i++) {
                if (!("".equals(qlist[i]) || qlist[i] == null)) {
                    query += " and " + qlist[i];
                }
            }

            HashMap hm = new HashMap();
            hm.put("query", query);
            try {
                eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
                eq.publish(new Event("", null, hm));
            } catch (NullPointerException ex) {
                //Executions.sendRedirect("./zul?url=inicio&s="+buscar.getValue().toString().toUpperCase());
                contenido.setSrc("/resources/apps/inicio.zul");
//            subModulo.setValue("");
                eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
                eq.publish(new Event("", null, hm));
            }
        }
    }

    @Listen("onSelect = #tiempoSearch")
    public void selectTiempo() {

        int value = Integer.parseInt(tiempoSearch.getSelectedItem().getValue().toString());
        Date today = new Date(new Date().getTime());
        Date nuevoDia = null;

        if (value == 1) {
            nuevoDia = new FormatoFecha().restarDia(1, today);
        } else if (value == 2) {
            nuevoDia = new FormatoFecha().restarDia(7, today);
        } else if (value == 3) {
            nuevoDia = new FormatoFecha().restarDia(31, today);
        }

        if (value == 0) {
            qlist[1] = "";
        } else {
            qlist[1] = "documento.docu_fecha>='" + nuevoDia + "'";
        }

        query = "";
        for (int i = 0; i < 4; i++) {
            if (!("".equals(qlist[i]) || qlist[i] == null)) {
                query += " and " + qlist[i];
            }
        }

        HashMap hm = new HashMap();
        hm.put("query", query);
        try {
            eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        } catch (NullPointerException ex) {
            //Executions.sendRedirect("./zul?url=inicio&s="+buscar.getValue().toString().toUpperCase());
            contenido.setSrc("/resources/apps/inicio.zul");
//            subModulo.setValue("");
            eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        }
    }

    @Listen("onSelect = #catalogoSearch")
    public void selectCatalogo() {
        int value = Integer.parseInt(catalogoSearch.getSelectedItem().getValue().toString());
        if (value == 0) {
            qlist[1] = "";
        } else {
            qlist[1] = "documento.cata_id='" + value + "'";
        }

        query = "";
        for (int i = 0; i < 4; i++) {
            if (!("".equals(qlist[i]) || qlist[i] == null)) {
                query += " and " + qlist[i];
            }
        }

        HashMap hm = new HashMap();
        hm.put("query", query);
        try {
            eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        } catch (NullPointerException ex) {
            //Executions.sendRedirect("./zul?url=inicio&s="+buscar.getValue().toString().toUpperCase());
            contenido.setSrc("/resources/apps/inicio.zul");
//            subModulo.setValue("");
            eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        }
    }

    @Listen("onSelect = #categoriaSearch")
    public void selectCategoria() {
        int value = Integer.parseInt(categoriaSearch.getSelectedItem().getValue().toString());
        if (value == 0) {
            qlist[2] = "";
        } else {
            qlist[2] = query2 = "documento.cate_id='" + value + "'";
        }
        query = "";
        for (int i = 0; i < 4; i++) {
            if (!("".equals(qlist[i]) || qlist[i] == null)) {
                query += " and " + qlist[i];
            }
        }

        HashMap hm = new HashMap();
        hm.put("query", query);
        try {
            eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        } catch (NullPointerException ex) {
            //Executions.sendRedirect("./zul?url=inicio&s="+buscar.getValue().toString().toUpperCase());
            contenido.setSrc("/resources/apps/inicio.zul");
//            subModulo.setValue("");
            eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        }
    }

    @Listen("onSelect = #tipoSearch")
    public void selectTipo() {
        int value = Integer.parseInt(tipoSearch.getSelectedItem().getValue().toString());
        if (value == 0) {
            qlist[3] = "";
        } else {
            qlist[3] = "documento.tido_id='" + value + "'";
        }

        query = "";
        for (int i = 0; i < 4; i++) {
            if (!("".equals(qlist[i]) || qlist[i] == null)) {
                query += " and " + qlist[i];
            }
        }

        HashMap hm = new HashMap();
        hm.put("query", query);
        try {
            eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        } catch (NullPointerException ex) {
            //Executions.sendRedirect("./zul?url=inicio&s="+buscar.getValue().toString().toUpperCase());
            contenido.setSrc("/resources/apps/inicio.zul");
//            subModulo.setValue("");
            eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, false);
            eq.publish(new Event("", null, hm));
        }
    }
    


}
