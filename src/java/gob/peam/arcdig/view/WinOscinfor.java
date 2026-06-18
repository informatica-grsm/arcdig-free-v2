/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.ConexionReporte;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.JSONOscinfor;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.utils.FormatoFecha;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author alabajos
 */
public class WinOscinfor extends SelectorComposer<Component> {

    public WinOscinfor() {
    }

    @Wire
    Iframe iframe;
    @Wire
    Textbox buscar, filtro;

    @Wire
    Listbox lista, archivos;

    @Wire
    Iframe viewFrame;

    String path;

    List<Documento> documentos = (List<Documento>) new ArrayList<Documento>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        path = new ConexionReporte().obtenerCarpeta("carpeta");

    }

    @Listen("onClick =#icon-buscar")
    public void clickFiltro() {
        //filtro();
    }

    /*@Listen("onOK =#buscar")
    public void filtro() {
        Gson gson = new Gson();
        JSONOscinfor json = new JSONOscinfor();
        String jsonInString = getlistaSupervisados2("GORESAM", "*%2017$GORESAM-%", buscar.getValue().trim());
        viewFrame.setVisible(false);
        //json = gson.fromJson(jsonInString, JSONOscinfor.class);
        List<JSONOscinfor> list = gson.fromJson(jsonInString, new TypeToken<List<JSONOscinfor>>() {
        }.getType());

        int index = 0;
        lista.getItems().clear();
        for (JSONOscinfor item : list) {
            item.setId(new DocumentoDao().insertLog(item));
            index++;
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(String.valueOf(index)));
            Listcell cell1 = new Listcell();
            cell1.appendChild(new Label(item.getNUM_THABILITANTE()));

            Listcell cell2 = new Listcell();
            cell2.appendChild(new Label(item.getTITULAR()));

            Listcell cell3 = new Listcell();
            cell3.appendChild(new Label(item.getMODALIDAD()));

            Listcell cell4 = new Listcell();
            cell4.appendChild(new Label(item.getCADUCADO_TH()));

            Listcell cell6 = new Listcell();
            cell6.appendChild(new Label(item.getNOMBRE_POA()));

            Toolbarbutton botonEditar = new Toolbarbutton();
            botonEditar.setWidth("20px");
            botonEditar.setHeight("20px");
            botonEditar.setTooltiptext("Reporte en Pdf");
            botonEditar.setIconSclass("z-icon-file");
            final JSONOscinfor bean = item;
            botonEditar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    viewFrame.setVisible(true);
                    viewFrame.setSrc("./OscinforServlet?id=" + bean.getId());
                    filtro.setValue(bean.getNUM_THABILITANTE());
                    filtrar();
                }
            });

            Toolbarbutton botonBuscar = new Toolbarbutton();
            botonBuscar.setWidth("20px");
            botonBuscar.setHeight("20px");
            botonBuscar.setTooltiptext("Buscar Coincidencias en ArcDig");
            botonBuscar.setIconSclass("z-icon-search");

            botonBuscar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    filtro.setValue(bean.getNUM_THABILITANTE());
                    filtrar();
                }
            });

            Listcell cell5 = new Listcell();
            cell5.appendChild(botonBuscar);
            cell5.appendChild(botonEditar);

            listitem.appendChild(cell1);
            listitem.appendChild(cell2);
            listitem.appendChild(cell3);
            listitem.appendChild(cell4);
            listitem.appendChild(cell6);
            listitem.appendChild(cell5);
            //listitem.appendChild(cell6);

            lista.appendChild(listitem);
        }
    }*/

    @Listen("onChange = #filtro")
    public void filtrar() {
        List<Documento> items = (List<Documento>) new ArrayList<Documento>();
        HashMap hm = new HashMap();
        String vector = filtro.getValue().toString().trim().replace(" ", "%") + ":*";
        String filtrado = vector.toString().replaceAll("[^\\p{Alpha}\\p{Digit}\\p{Blank}\\p{Blank}]+", "");
        String c;
        if (filtro.getValue().toString().length() == 0) {
            c = "";
        } else {
            c = "and ( (to_tsquery('" + filtrado + "') @@ docu_search) or docu_titulo ilike '%" + filtro.getValue().toString().replace(" ", "%") + "%' )";
        }
        hm.put("filtro", c);
        //String rv = "//" + rutaVista.getLabel().replace("/", "//");

        items = new DocumentoDao().listarArchivos1(hm);
        dibujarArchivos(items);
    }

    /*private static String getlistaSupervisados2(java.lang.String usuario, java.lang.String password, java.lang.String varBusqueda) {
        gob.peam.arcdig.dao.WSDataOSINFOR service = new gob.peam.arcdig.dao.WSDataOSINFOR();
        gob.peam.arcdig.dao.WSDataOSINFORSoap port = service.getWSDataOSINFORSoap12();
        return port.getlistaSupervisados2(usuario, password, varBusqueda);
    }*/

    @Wire
    Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);

    }
    Documento selected;

    @Listen("onSelect = #archivos")
    public void pickSelect() {
        Set item = archivos.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);

        for (int i = 0; i < emp.size(); i++) {
            selected = documentos.get(emp.get(i).getIndex());
        }
    }

    public void dibujarArchivos(List<Documento> items) {
        archivos.getItems().clear();
        documentos = items;
        for (Documento item : items) {
            Listitem listitem = new Listitem();
            Listcell icon = new Listcell();
            icon.appendChild(new Label(""));
            Image img = new Image();
            try {
                img.setSrc("./resources/img/" + item.getMineType().getMineExt() + ".png");
            } catch (NullPointerException ex) {
                img.setSrc("./resources/img/pdf.png");
            }
            img.setStyle("border:0px");
            icon.appendChild(img);

            Listcell data = new Listcell();
            String str = path + "/" + item.getRuta();
            File f = new File(str + "/" + item.getId() + "." + item.getMineType().getMineExt());

            double bytes = f.length();
            double kilobytes = Math.round((bytes / 1024) * Math.pow(10, 0)) / Math.pow(10, 0);
            double megabytes = Math.round((kilobytes / 1024) * Math.pow(10, 2)) / Math.pow(10, 2);
            long ms = f.lastModified();
            String modificado = new FormatoFecha().fileModificated(ms);
            String mostrar = "";
            if (kilobytes > 1024) {
                mostrar = megabytes + " MB";
            } else {
                mostrar = kilobytes + " KB";
            }
            String resumen = this.cortarCadena(item.getResumen(), 60);
            Label l1 = new Label(resumen);
            l1.setStyle("font-style:italic");
            data.appendChild(l1);
            data.appendChild(new Separator());
            data.appendChild(new Label("Tamaño: " + mostrar + "; Modificado: " + modificado + "; ID: " + item.getId()));

            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(String.valueOf(item.getTitulo())));

            listitem.appendChild(new Listcell(""));
            listitem.appendChild(icon);
            listitem.appendChild(nombre);
            listitem.appendChild(data);

            Toolbarbutton botonBuscar = new Toolbarbutton();
            botonBuscar.setWidth("20px");
            botonBuscar.setHeight("20px");
            botonBuscar.setTooltiptext("Ver Asociar");
            botonBuscar.setIconSclass("z-icon-search");
            Listcell accion = new Listcell();
            botonBuscar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    pickVer();
                }
            });
            accion.appendChild(botonBuscar);
            listitem.appendChild(accion);

            listitem.setContext("editPopup");

            listitem.setId(item.getId().toString() + ";" + item.getMineType().getMineExt());

            archivos.appendChild(listitem);

        }
    }

    public String cortarCadena(String cadena, int max) {
        String cadenaNueva = "";
        if (cadena.length() > max) {
            String pedazo;
            pedazo = cadena.substring(max - 10, max);
            try {
                String cad1 = pedazo.split(" ")[0];
                pedazo = cadena.substring(0, max - 10) + cad1 + "...";
            } catch (NullPointerException ex) {
                pedazo = cadena;
            }
            cadenaNueva = pedazo;
        } else {
            cadenaNueva = cadena;
        }
        return cadenaNueva;
    }

    @Listen("onClick =#menuVerSocio")
    public void pickVer() {
        Integer tamRela = new DocumentoDao().getRelacion(selected.getDocuGroup());
        if (tamRela > 1) {
            HashMap hm = new HashMap();
            selected = new DocumentoDao().getOne(selected.getId());
            hm.put("documento", selected);
            Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winVer.zul", null, hm);
            window2.doModal();
        } else {
            showNotify("Este documento no contiene documentos asociados", win, "info", "top_center");
        }
    }

}
