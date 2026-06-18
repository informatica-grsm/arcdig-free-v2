/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Catalogo;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.Persona;
import gob.peam.arcdig.beans.TipoDocumento;
import gob.peam.arcdig.dao.CatalogoDao;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.dao.PermisoDao;
import gob.peam.arcdig.dao.PersonaDao;
import gob.peam.arcdig.dao.TipoDocumentoDao;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

/**
 *
 * @author pc-lab
 */
public class AppReport1 extends SelectorComposer<Component> {

    List<Persona> items = (List<Persona>) new ArrayList<Persona>();
    List<Documento> docs = (List<Documento>) new ArrayList<Documento>();
    List<Persona> itemsAux = (List<Persona>) new ArrayList<Persona>();
    List<Catalogo> catalogos = (List<Catalogo>) new ArrayList<Catalogo>();
    List<TipoDocumento> tipoDocs = (List<TipoDocumento>) new ArrayList<TipoDocumento>();
    List<gob.peam.arcdig.beans.Permiso> perm = (List<gob.peam.arcdig.beans.Permiso>) new ArrayList<gob.peam.arcdig.beans.Permiso>();

    private String queryPers;
    @Wire
    Listbox listUsuario, listItem;
    @Wire
    Bandbox filtro;
    @Wire
    Datebox fechaInicio1, fechaFinal1;
    Integer ind = 0;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dibujarPersonas();
    }

    public void dibujarPersonas() {
        items = new PersonaDao().listarPersona(filtro.getValue());
        perm = new PermisoDao().listarPermiso();
        
        itemsAux.clear();
        for (Persona a : items) {
            for (gob.peam.arcdig.beans.Permiso pe : perm) {
                if (a.getDni().equals(pe.getDni())) {
                    itemsAux.add(a);
                }
            }
        }
        listUsuario.getItems().clear();

        for (Persona item : itemsAux) {

            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));

            Listcell dni = new Listcell();
            dni.appendChild(new Label(item.getDni()));

            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(item.getNombre()));

            listitem.appendChild(check);

            listitem.appendChild(dni);
            listitem.appendChild(nombre);
            listUsuario.appendChild(listitem);
        }
    }

    public void dibujarCatalogo() {
        catalogos = new CatalogoDao().listarCatalogo(filtro.getValue());
        listUsuario.getItems().clear();

        for (Catalogo item : catalogos) {
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(String.valueOf(item.getCataId())));
            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(item.getCataNombre()));

            listitem.appendChild(check);
            listitem.appendChild(nro);
            listitem.appendChild(nombre);

            listUsuario.appendChild(listitem);
        }
    }
    
    public void dibujarTipoDocumento() {
        tipoDocs = new TipoDocumentoDao().listarTipoDocumento(filtro.getValue());
        listUsuario.getItems().clear();

        for (TipoDocumento item : tipoDocs) {
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(String.valueOf(item.getId())));
            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(item.getNombre()));

            listitem.appendChild(check);
            listitem.appendChild(nro);
            listitem.appendChild(nombre);

            listUsuario.appendChild(listitem);
        }
    }

    @Listen("onSelect =#listItem")
    public void itemUsuario() {

        ind = listItem.getSelectedItem().getIndex();
        if (ind == 0) {
            listUsuario.setVisible(true);
            dibujarPersonas();
        } else if (ind == 1) {
            listUsuario.setVisible(true);
            dibujarCatalogo();
        } else if(ind==2){
            listUsuario.setVisible(false);
            listUsuario.getItems().clear();
        } else if(ind==3){
            listUsuario.setVisible(true);
            dibujarTipoDocumento();
        } else {
            listUsuario.setVisible(false);
            fechaInicio1.setVisible(false);
            fechaFinal1.setVisible(false);
           
            dibujarTipoDocumento();
        }
    }

    @Listen("onSelect = #listUsuario")
    public void select() {
        if (ind == 0) {
            Set item = listUsuario.getSelectedItems();
            //print.setStyle("display:none");
            List<Listitem> emp = new ArrayList<Listitem>(item);
            for (int i = 0; i < itemsAux.size(); i++) {
                itemsAux.get(i).setCheck(false);
            }
            queryPers = " and (";
            for (int i = 0; i < emp.size(); i++) {
                itemsAux.get(emp.get(i).getIndex()).setCheck(true);
                if(itemsAux.get(emp.get(i).getIndex()).getUsuario().getIdUsuario()!=null){
                    if (i < emp.size() - 1) {
                        queryPers += " documento.usua_id='" + itemsAux.get(emp.get(i).getIndex()).getUsuario().getIdUsuario()+ "' or ";
                    } else {
                        queryPers += " documento.usua_id='" + itemsAux.get(emp.get(i).getIndex()).getUsuario().getIdUsuario()+ "'  ";
                    }
                }
            }
            queryPers += ")";
        } else if (ind == 1) {

            Set item = listUsuario.getSelectedItems();
            //print.setStyle("display:none");
            List<Listitem> emp = new ArrayList<Listitem>(item);
            queryPers = " and (";
            for (int i = 0; i < emp.size(); i++) {
                //itemsAux.get(emp.get(i).getIndex()).setCheck(true);
                if (i < emp.size() - 1) {
                    queryPers += " documento.cata_id='" + catalogos.get(emp.get(i).getIndex()).getCataId() + "' or ";
                } else {
                    queryPers += " documento.cata_id='" + catalogos.get(emp.get(i).getIndex()).getCataId() + "'  ";
                }
            }
            queryPers += ")";
        } else if (ind == 3) {

            Set item = listUsuario.getSelectedItems();
            //print.setStyle("display:none");
            List<Listitem> emp = new ArrayList<Listitem>(item);
            queryPers = " and (";
            for (int i = 0; i < emp.size(); i++) {
                //itemsAux.get(emp.get(i).getIndex()).setCheck(true);
                if (i < emp.size() - 1) {
                    queryPers += " documento.tido_id='" + tipoDocs.get(emp.get(i).getIndex()).getId() + "' or ";
                } else {
                    queryPers += " documento.tido_id='" + tipoDocs.get(emp.get(i).getIndex()).getId()+ "'  ";
                }
            }
            queryPers += ")";
        }
    }

    @Listen("onClick=#aceptar")
    public void reportar() {
        HashMap hm = new HashMap();
        new DocumentoDao().truncateReport1();
        if (ind == 0) {
            if (fechaFinal1.getValue() != null) {
                queryPers += " and docu_fecha <= '" + fechaFinal1.getValue() + "' ";
            }

            if (fechaInicio1.getValue() != null) {
                queryPers += " and docu_fecha >= '" + fechaInicio1.getValue() + "' ";
            }

            hm.put("query", queryPers);

            docs = new DocumentoDao().listarDocs(hm);
            
            for (Documento doc : docs) {
                hm.put("titulo", doc.getTitulo());
                hm.put("fecha", doc.getDocuHora());
                for (Persona item : itemsAux) {
                    if (item.getUsuario().getIdUsuario().equals(doc.getUsuaId())) {
                        hm.put("dni", item.getDni());
                        hm.put("nombre", item.getNombre());
                        break;
                    }
                }
                hm.put("tipoDocumento", doc.getTipoDocumento().getNombre());
                new DocumentoDao().insertReport1(hm);
            }

            if (!docs.isEmpty()) {
                Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winVisorApp1.zul", null, hm);
                window1.doModal();
            } else {
                showNotify("No exiten registros en los parametros ingresados", win, "info", "middle_center");
            }
        } else if (ind == 1) {
            if (fechaFinal1.getValue() != null) {
                queryPers += " and documento.docu_fecha <= '" + fechaFinal1.getValue() + "' ";
            }

            if (fechaInicio1.getValue() != null) {
                queryPers += " and documento.docu_fecha >= '" + fechaInicio1.getValue() + "' ";
            }
            hm.put("query", queryPers);
            Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winVisorApp2.zul", null, hm);
            window2.doModal();
        } else if (ind == 2) {
            queryPers = "";
            if (fechaFinal1.getValue() != null) {
                queryPers += " and documento.docu_fecha <= '" + fechaFinal1.getValue() + "' ";
            }

            if (fechaInicio1.getValue() != null) {
                queryPers += " and documento.docu_fecha >= '" + fechaInicio1.getValue() + "' ";
            }
            hm.put("query", queryPers);
            Window window3 = (Window) Executions.createComponents("/resources/zkWindow/winVisorApp3.zul", null, hm);
            window3.doModal();
        } else if (ind == 3) {
            //queryPers = "";
            if (fechaFinal1.getValue() != null) {
                queryPers += " and documento.docu_fecha <= '" + fechaFinal1.getValue() + "' ";
            }

            if (fechaInicio1.getValue() != null) {
                queryPers += " and documento.docu_fecha >= '" + fechaInicio1.getValue() + "' ";
            }
           // showNotify(queryPers, win, "info", "top_center");
            hm.put("query", queryPers);
            Window window4 = (Window) Executions.createComponents("/resources/zkWindow/winVisorApp4.zul", null, hm);
            window4.doModal();
        } else if(ind==4){
            if (fechaFinal1.getValue() != null) {
                queryPers += " and documento.docu_fecha <= '" + fechaFinal1.getValue() + "' ";
            }

            if (fechaInicio1.getValue() != null) {
                queryPers += " and documento.docu_fecha >= '" + fechaInicio1.getValue() + "' ";
            }
           // showNotify(queryPers, win, "info", "top_center");
            hm.put("query", queryPers);
            Window window5 = (Window) Executions.createComponents("/resources/zkWindow/winVisorApp5.zul", null, hm);
            window5.doModal();
        }
    }

    @Listen("onChange = #filtro")
    public void buscar() {
        if (ind == 0) {
            dibujarPersonas();
        } else if(ind==1) {
            dibujarCatalogo();
        } else if(ind>3){
            dibujarTipoDocumento();
        }
    }

    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 4000);
    }
}
