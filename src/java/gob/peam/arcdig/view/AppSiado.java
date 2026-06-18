package gob.peam.arcdig.view;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import config.ConexionReporte;
import gob.peam.arcdig.beans.DetSubTipoDoc;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.JSONSiadoOscinfor;
import gob.peam.arcdig.beans.JsonOscinforToken;
import gob.peam.arcdig.beans.Metadata;
import gob.peam.arcdig.beans.Sector;
import gob.peam.arcdig.beans.Serie;
import gob.peam.arcdig.beans.Siado;
import gob.peam.arcdig.beans.SiadoDetalle;
import gob.peam.arcdig.beans.SubSerie;
import gob.peam.arcdig.beans.SubTipoDoc;
import gob.peam.arcdig.beans.TipoDoc;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.dao.MetaDataDao;
import gob.peam.arcdig.dao.SiadoDao;
import gob.peam.arcdig.dao.SiadoDetalleDao;
import gob.peam.arcdig.utils.FormatoFecha;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Objects;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author Usuario
 */
public class AppSiado extends SelectorComposer<Component> {

    String usuario;
    List<Siado> items_siado = (List<Siado>) new ArrayList<Siado>();
    List<Documento> items_documents = (List<Documento>) new ArrayList<Documento>();

    private Siado SelectItem;

    private Boolean editar = false;
     String token ="";
     String ins ="";
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
       
        dibujarSiado();
        
        ins = new ConexionReporte().obtenerPropiedad("siado_IdInst");
        String usua = new ConexionReporte().obtenerPropiedad("siado_usuaro");
        usuario = usua;
        String password = new ConexionReporte().obtenerPropiedad("siado_password");
        String alias = new ConexionReporte().obtenerPropiedad("siado_alias");
        
        

        token = loginOsinfor(usua, password, alias);//METODO DE AUTENTICACIÓN
        //showNotify(token+"", win, "info");//
        getSerie();
        getTipoDoc();
        getSector();
        getDocuments();
        getSubSerie();
    }

    @Wire
    Div nDivVentana, nDivlistaRegistro;

    @Wire
    Listbox listSiado, nCmbSerie, nCmbSubSerie, nCmbTipoDoc, nCmbSubTipoDoc, nCmbDetSubTipoDoc, nCmbSector, gridDocuments;

    @Wire
    Bandbox filtro, filtroDoc;

    /*CREANDO GRID SIADO*/
    public void dibujarSiado() {
        items_siado = new SiadoDao().listarSiado(filtro.getValue());
        listSiado.getItems().clear();
        int index = 0;

        for (Siado item : items_siado) {
            index++;
            Listitem listitem = new Listitem();

            Listcell check = new Listcell();
            check.appendChild(new Label(""));

            Listcell titHabitante = new Listcell();
            titHabitante.appendChild(new Label(item.getTituloHabilitante()));
            Listcell nroExpediente = new Listcell();
            nroExpediente.appendChild(new Label(item.getNumeroTramite()));
            Listcell fechaEnvio = new Listcell();
            fechaEnvio.appendChild(new Label(item.getFechaEnvio()));
            Listcell usuarioEnvio = new Listcell();
            usuarioEnvio.appendChild(new Label(usuario));
            Listcell nroTramite = new Listcell();
            nroTramite.appendChild(new Label(item.getNumeroExpediente()));

            Listcell password = new Listcell();
            password.appendChild(new Label(item.getPassword()));

            Listcell estado_tramite = new Listcell();
            String description = (item.getEstadoTramiteId() == 1) ? "REGISTRADO" : "ENVIADO";

            estado_tramite.appendChild(new Label(description));

            /*Listcell estado = new Listcell();
            Image imgEstado = new Image();
            imgEstado.setSrc("./resources/img/" + item.getEstado() + ".png");
            imgEstado.setStyle("border:0px");
            estado.appendChild(imgEstado);*/
            Listcell cell14 = new Listcell();

            Toolbarbutton botonSend = new Toolbarbutton();
            botonSend.setIconSclass("z-icon-plane");
            botonSend.setWidth("16px");
            botonSend.setHeight("16px");
            botonSend.setTooltiptext("Enviar");
            cell14.appendChild(botonSend);

            /*Toolbarbutton botonEditar = new Toolbarbutton();
            botonEditar.setIconSclass("z-icon-eye");
            botonEditar.setWidth("16px");
            botonEditar.setHeight("16px");
            botonEditar.setTooltiptext("Ver");
            cell14.appendChild(botonEditar);*/
            
            Toolbarbutton botonEliminar = new Toolbarbutton();
            botonEliminar.setIconSclass("z-icon-trash-o");//trash
            botonEliminar.setWidth("16px");
            botonEliminar.setHeight("16px");
            botonEliminar.setTooltiptext("Eliminar");
            cell14.appendChild(botonEliminar);

            listitem.appendChild(check);
            listitem.appendChild(titHabitante);
            listitem.appendChild(nroExpediente);
            listitem.appendChild(fechaEnvio);
            listitem.appendChild(usuarioEnvio);
            listitem.appendChild(nroTramite);
            listitem.appendChild(password);
            listitem.appendChild(estado_tramite);
            listitem.appendChild(cell14);

            listSiado.appendChild(listitem);

            botonEliminar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    eliminarSiado(item);
                }
            });

            botonSend.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    sendSiado(item);
                }
            });
           /* botonEditar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    editarSiado(item);
                }
            });*/
        }
    }

    /*LIMPIANDO CAMPOS DEL FORM DE TRÁMITE SIADO*/
    public void clearForm() {
        nDivVentana.setVisible(true);
        nDivlistaRegistro.setVisible(false);
        nTxtTitulo.setValue("");
        nTxtAnhoFin.setValue("");
        //nTxtDepartamento.setValue("");
        //nTxtNro.setValue("");
        nTxtAnhoInicio.setValue("");
        nTxtDescripcion1.setValue("");
        nTxtTitular.setValue("");
        //nTxtPassword.setValue("");
        nTxtObservacion1.setValue("");
        listDocumento.getItems().clear();
    }

    @Listen("onSelect = #listSiado")
    public void select() {
        Set item = listSiado.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            SelectItem = items_siado.get(emp.get(i).getIndex());
        }
    }

    @Listen("onSelect = #nCmbSerie")
    public void selectSubSerie() {
        getSubSerie();

    }

    @Listen("onSelect = #nCmbTipoDoc")
    public void selectTipoDoc() {
        getSubTipoDoc();
    }

    @Listen("onSelect = #nCmbSubTipoDoc")
    public void selectDetSubTipoDoc() {
        getDetSubTipoDoc();
    }

    public void getSector() {
        try {
            //crear cliente
            Client client = ClientBuilder.newClient();
            //target to cliente
            String pathService = new ConexionReporte().obtenerCarpeta("service1");
            WebTarget target = client.target(pathService + "api/documentos/seccion?idIns=5");
            //showNotify(pathService+"siado/api/documentos/seriedocumental", win, "info", pathService);
            //convert Json to Class
            String jsonInString = target.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer "+ token).get(String.class);
            Gson gson = new Gson();
            //
            JsonObject json = gson.fromJson(jsonInString, JsonObject.class);

            List<Sector> list = gson.fromJson(json.get("result").toString(), new TypeToken<List<Sector>>() {
            }.getType());

            for (Sector s : list) {
                Listitem item = new Listitem();
                item.setLabel(s.getCodSec() + " - " + s.getDesSec());
                item.setValue(s.getIdSec());
                nCmbSector.appendChild(item);
                //System.out.println(s.getIdSec());
            }
            if (nCmbSector.getItemCount() != 0) {
                nCmbSector.setSelectedIndex(0);
            }

        } catch (IOException ex) {
            Logger.getLogger(AppSiado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*CARGAR DOCUMENTOS EN LA DIALOG GRID DOCUMENTO (VENTANA AGREGAR UN EXPEDIENTE)*/
    public void getDocuments() {
        String filter = filtroDoc.getValue().toString();
        items_documents = new DocumentoDao().filtroDoc(filter);
        gridDocuments.getItems().clear();

        int index = 0;
        for (Documento item : items_documents) {
            index++;
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(String.valueOf(index)));
            Listcell id = new Listcell();
            id.appendChild(new Label(item.getId().toString()));
            Listcell title = new Listcell();
            title.appendChild(new Label(item.getTitulo()));
            Listcell propietario = new Listcell();
            propietario.appendChild(new Label(item.getPropietario()));

            Listcell cell11 = new Listcell();
            Toolbarbutton botonAdd = new Toolbarbutton();
            botonAdd.setIconSclass("z-icon-plus");
            botonAdd.setWidth("16px");
            botonAdd.setHeight("16px");
            botonAdd.setTooltiptext("Enviar");
            cell11.appendChild(botonAdd);

            listitem.appendChild(check);
            listitem.appendChild(nro);
            listitem.appendChild(id);
            listitem.appendChild(title);
            listitem.appendChild(propietario);
            listitem.appendChild(cell11);

            gridDocuments.appendChild(listitem);

            botonAdd.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    addDetail(item);
                }
            });
        }
    }

    @Listen("onChange = #filtroDoc")
    public void buscarDoc() {
        getDocuments();
    }

    public void getSerie() {
        try {

            //crear cliente
            Client client = ClientBuilder.newClient();
            //target to cliente
            String pathService = new ConexionReporte().obtenerCarpeta("service1");
            
            WebTarget target = client.target(pathService + "api/documentos/seriedocumental");
            //showNotify(pathService+"siado/api/documentos/seriedocumental", win, "info", pathService);
            //convert Json to Class
            String jsonInString = target.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).get(String.class);
            Gson gson = new Gson();
            //
            JsonObject json = gson.fromJson(jsonInString, JsonObject.class);

            List<Serie> list = gson.fromJson(json.get("result").toString(), new TypeToken<List<Serie>>() {
            }.getType());

            for (Serie s : list) {
                Listitem item = new Listitem();
                item.setLabel(s.getCodSerieDoc() + " - " + s.getDesSerieDoc());
                item.setValue(s.getIdSerie());
                nCmbSerie.appendChild(item);
            }

            if (nCmbSerie.getItemCount() != 0) {
                nCmbSerie.setSelectedIndex(0);
                getSubSerie();
            }

            //convertedObject.get("name")
        } catch (IOException ex) {
            Logger.getLogger(AppSiado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getSubSerie() {
        try {
            nCmbSubSerie.getItems().clear();
            //crear cliente
            Client client = ClientBuilder.newClient();
            //target to cliente
            String pathService = new ConexionReporte().obtenerCarpeta("service1");
            WebTarget target = client.target(pathService + "api/documentos/subseriedocumental?IdSerie=" + nCmbSerie.getSelectedItem().getValue());
            //showNotify(pathService+"siado/api/documentos/seriedocumental", win, "info", pathService);
            //convert Json to Class
            String jsonInString = target.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer "+ token).get(String.class);
            Gson gson = new Gson();
            //
            JsonObject json = gson.fromJson(jsonInString, JsonObject.class);

            List<SubSerie> list = gson.fromJson(json.get("result").toString(), new TypeToken<List<SubSerie>>() {
            }.getType());
            //showNotify(json.get("result").toString(), win, "info", "top_center");

            for (SubSerie s : list) {
                Listitem item = new Listitem();
                item.setLabel(s.getCodSubSerieDoc() + " - " + s.getDesSubSerieDoc());
                item.setValue(s.getIdSubSerie());
                nCmbSubSerie.appendChild(item);
            }

            if (nCmbSubSerie.getItemCount() != 0) {
                nCmbSubSerie.setSelectedIndex(0);
            }

            //convertedObject.get("name")
        } catch (IOException ex) {
            Logger.getLogger(AppSiado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getTipoDoc() {
        try {
            nCmbTipoDoc.getItems().clear();
            //crear cliente
            Client client = ClientBuilder.newClient();
            //target to cliente
            String pathService = new ConexionReporte().obtenerCarpeta("service1");
            WebTarget target = client.target(pathService + "api/documentos/tipodocumental");
            //showNotify(pathService+"siado/api/documentos/seriedocumental", win, "info", pathService);
            //convert Json to Class
            String jsonInString = target.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer "+ token).get(String.class);
            Gson gson = new Gson();
            //
            JsonObject json = gson.fromJson(jsonInString, JsonObject.class);

            List<TipoDoc> list = gson.fromJson(json.get("result").toString(), new TypeToken<List<TipoDoc>>() {
            }.getType());
            //showNotify(json.get("result").toString(), win, "info", "top_center");

            for (TipoDoc s : list) {
                Listitem item = new Listitem();
                item.setLabel(s.getCodTipoDoc() + " - " + s.getDesTipoDoc());
                item.setValue(s.getIdTipoDoc());
                nCmbTipoDoc.appendChild(item);
            }

            if (nCmbTipoDoc.getItemCount() != 0) {
                nCmbTipoDoc.setSelectedIndex(0);
                getSubTipoDoc();
            }

            //convertedObject.get("name")
        } catch (IOException ex) {
            //showNotify("hola", win, "info", "top_center");
        }
    }

    public void getSubTipoDoc() {
        try {
            nCmbSubTipoDoc.getItems().clear();
            //crear cliente
            Client client = ClientBuilder.newClient();
            //target to cliente
            String pathService = new ConexionReporte().obtenerCarpeta("service1");
            WebTarget target = client.target(pathService + "api/documentos/subtipodocumental?IdTipoDoc=" + nCmbTipoDoc.getSelectedItem().getValue());
            //showNotify(pathService+"siado/api/documentos/seriedocumental", win, "info", pathService);
            //convert Json to Class
            String jsonInString = target.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer "+ token).get(String.class);
            Gson gson = new Gson();
            //
            JsonObject json = gson.fromJson(jsonInString, JsonObject.class);

            List<SubTipoDoc> list = gson.fromJson(json.get("result").toString(), new TypeToken<List<SubTipoDoc>>() {
            }.getType());
            //showNotify(json.get("result").toString(), win, "info", "top_center");

            for (SubTipoDoc s : list) {
                Listitem item = new Listitem();
                item.setLabel(s.getCodSubTipoDoc() + " - " + s.getDesSubTipoDoc());
                item.setValue(s.getIdSubTipoDoc());
                nCmbSubTipoDoc.appendChild(item);
            }

            if (nCmbSubTipoDoc.getItemCount() != 0) {
                nCmbSubTipoDoc.setSelectedIndex(0);
                getDetSubTipoDoc();
            }

            //convertedObject.get("name")
        } catch (IOException ex) {
            //showNotify("hola", win, "info", "top_center");
        }
    }

    public void getDetSubTipoDoc() {
        try {
            nCmbDetSubTipoDoc.getItems().clear();
            //crear cliente
            Client client = ClientBuilder.newClient();
            //target to cliente
            String pathService = new ConexionReporte().obtenerCarpeta("service1");
            WebTarget target = client.target(pathService + "api/documentos/detallesubtipodocumental?idSubTipoDoc=" + nCmbSubTipoDoc.getSelectedItem().getValue());
            //convert Json to text
            String jsonInString = target.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer "+ token).get(String.class);
            Gson gson = new Gson();
            //
            JsonObject json = gson.fromJson(jsonInString, JsonObject.class);
            //convert  json to List<class>
            List<DetSubTipoDoc> list = gson.fromJson(json.get("result").toString(), new TypeToken<List<DetSubTipoDoc>>() {
            }.getType());

            for (DetSubTipoDoc s : list) {
                Listitem item = new Listitem();
                item.setLabel(s.getCodDetSubTipoDoc() + " - " + s.getDesDetSubTipoDoc());
                item.setValue(s.getIdDetSubTipoDoc());
                nCmbDetSubTipoDoc.appendChild(item);
            }

            if (nCmbDetSubTipoDoc.getItemCount() != 0) {
                nCmbDetSubTipoDoc.setSelectedIndex(0);
            }

            //convertedObject.get("name")
        } catch (IOException ex) {
            //showNotify("hola", win, "info", "top_center");
        }
    }

    @Wire
    Popup popExpediente, popDocumento;

    @Wire
    Button addExp, addDoc;

    @Wire
    Textbox txtExpediente_, nTxtDepartamento, nTxtAnhoFin, nTxtAnhoInicio, nTxtDescripcion1, nTxtTitular, nTxtObservacion1, docId, docDescripcion;

    @Wire
    Listbox listDocumento, nCmbMesAnio, nCmbMesFin;

    @Wire
    Auxhead auxHead;

    @Wire
    Label nTxtTituloError;

    @Wire
    Textbox nTxtTitulo, numZafra, numero, descripcion, observacion, regente, anhoAprobacion, anhoFin, usuarioEnvio, fechaEnvio;

    @Wire
    Checkbox nChkRegente;

    @Listen("onClick = #addExp")
    public void addExp() {
        popExpediente.open(addExp);

    }

    /*AGREGANDO DOCUMENTOS AL DETALLE CLICK EN OPCIÓN(+)*/
    public void addDetail(Documento it) {
        List<Documento> docs = new ArrayList<Documento>();
        // docs = new DocumentoDao().getRelacionById(Integer.parseInt(txtExpediente_.getValue().toString()));
        docs = new DocumentoDao().getRelacionById(Integer.parseInt(it.getId().toString()));
        Integer index = 0;
        List<SiadoDetalle> siadoDetalle = new ArrayList<>();

        List<Documento> new_doc = new ArrayList<>();

        List<Listitem> item_ = listDocumento.getItems();

        for (Documento d : docs) {
            boolean state = false;

            //System.out.println(d.getId()+" dd"+ (item_.contains(d.getId())));
            for (Listitem i : item_) {
                //Integer id1 = ((Documento) i.getAttribute("nData")).getId();
                //Integer id2 = d.getId();
                // System.out.println(id1 + " - " + id2 + " state: " + (id1 = id2));
                //id1 = id2;
                //if (id1 == id2) {
                state = true;

                if (!i.isVisible()) {
                    i.setVisible(true);
                    //  }
                }
            }
            if (!state) {

                new_doc.add(d);
                if (Objects.equals(d.getTidoId(), 1)) {
                    if (d.getMetaData() != null && !"".equals(d.getMetaData())) {
                        for (Metadata meta : new MetaDataDao().getMetadata(d)) {

                            if (meta.getCampo().equals("TITULAR")) {
                                nTxtTitular.setValue(meta.getDetalle());
                            }

                            if (!d.getDocuCodigo().equals("")) {
                                nTxtTitulo.setValue(d.getDocuCodigo());
                            }

                            if (!d.getResumen().equals("")) {
                                nTxtDescripcion1.setValue(d.getResumen());
                            }
                        }
                    }
                }
            }
        }

        for (Documento item : new_doc) {
            index++;
            SiadoDetalle sd = new SiadoDetalle();
            sd.setDocumento(item);
            sd.setUsuarioEnvio(usuario);

            Listitem listitem = new Listitem();
            listitem.setAttribute("nData", item);
            Listcell check = new Listcell();
            check.appendChild(new Label(""));

            Listcell cell1 = new Listcell();
            cell1.appendChild(new Label(item.getTitulo()));

            Listcell cell2 = new Listcell();
            cell2.appendChild(new Label("Configurar"));

            Listcell cell3 = new Listcell();
            cell3.appendChild(new Label("Configurar"));

            Listcell cell4 = new Listcell();
            cell4.appendChild(new Label("Configurar"));

            Listcell cell5 = new Listcell();
            cell5.appendChild(new Label("Configurar"));

            Listcell cell6 = new Listcell();
            cell6.appendChild(new Label(item.getTitulo()));
            sd.setNumero(item.getTitulo());
            Listcell cell7 = new Listcell();
            cell7.appendChild(new Label(item.getResumenMin()));
            sd.setDescripcion(item.getResumenMin());
            Listcell cell8 = new Listcell();
            cell8.appendChild(new Label(""));

            Listcell cell9 = new Listcell();
            cell9.appendChild(new Label(""));

            Listcell cell10 = new Listcell();
            cell10.appendChild(new Label(""));

            Listcell cell11 = new Listcell();
            cell11.appendChild(new Label(""));

            Listcell cell12 = new Listcell();
            cell12.appendChild(new Label("Configurar"));

            Listcell cell13 = new Listcell();

            if (item.getMetaData() != null && !"".equals(item.getMetaData())) {
                for (Metadata meta : new MetaDataDao().getMetadata(item)) {

                    if (meta.getCampo().equals("AÑO INICIO REG.")) {
                        sd.setAnhoAprobacion(meta.getDetalle());
                         cell10.appendChild(new Label(meta.getDetalle()));
                    }
                    if (meta.getCampo().equals("AÑO FIN REG.")) {
                        sd.setAnhoFin(meta.getDetalle());
                         cell11.appendChild(new Label(meta.getDetalle()));
                    }

                    if (meta.getCampo().equals("NOMBRE REGENTE")) {
                        sd.setsRegente(meta.getDetalle());
                        cell9.appendChild(new Label(meta.getDetalle()));
                    }

                    if (meta.getCampo().equals("NRO. DE ZAFRA")) {
                        sd.setNumZafra(meta.getDetalle());
                    }
                }
            }

            // String str = new SimpleDateFormat("dd/MM/yyyy").format(item.getFechaDocx());
            cell13.appendChild(new Label(item.getFechaDocx()));
            
            Date fecha = new FormatoFecha().formatStringFechaCorta(item.getFechaDocx());
            String str = new SimpleDateFormat("yyy-MM-dd").format(fecha);
            sd.setFechaEnvio(str);

            Listcell cell14 = new Listcell();

            Toolbarbutton botonVer = new Toolbarbutton();
            botonVer.setIconSclass("z-icon-eye");
            botonVer.setWidth("16px");
            botonVer.setHeight("16px");
            botonVer.setTooltiptext("Ver Detalle del Documento");
            cell14.appendChild(botonVer);

            Toolbarbutton botonEditar = new Toolbarbutton();
            botonEditar.setIconSclass("z-icon-pencil");
            botonEditar.setWidth("16px");
            botonEditar.setHeight("16px");
            botonEditar.setTooltiptext("Editar");
            cell14.appendChild(botonEditar);

            Toolbarbutton botonEliminar = new Toolbarbutton();
            botonEliminar.setIconSclass("z-icon-trash-o");//trash
            botonEliminar.setWidth("16px");
            botonEliminar.setHeight("16px");
            botonEliminar.setTooltiptext("Eliminar");
            cell14.appendChild(botonEliminar);

            listitem.appendChild(check);
            listitem.appendChild(cell14);
            listitem.appendChild(cell1);
            listitem.appendChild(cell2);
            listitem.appendChild(cell3);
            listitem.appendChild(cell4);
            listitem.appendChild(cell5);
            listitem.appendChild(cell6);
            listitem.appendChild(cell7);
            listitem.appendChild(cell8);
            listitem.appendChild(cell9);
            listitem.appendChild(cell10);
            listitem.appendChild(cell11);
            listitem.appendChild(cell12);
            listitem.appendChild(cell13);
            

            siadoDetalle.add(sd);

            listitem.setAttribute("selected", sd);
            final Listitem i = listitem;

            /*BOTON EDITAR DE DETALLE DE TRAMITE SIADO*/
            botonEditar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    HashMap hm = new HashMap();
                    auxHead.setVisible(true);
                    i.setSelected(true);
                    editar(i);

                }
            });
            /*BOTON VER DE DETALLE DE TRAMITE SIADO (NO PROCESO)*/
            botonVer.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {

                }
            });

            /*BOTON ELIMINAR DE DETALLE DE TRAMITE SIADO*/
            final Integer idDoc = item.getId();
            botonEliminar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {

                    eliminar(listitem);
                }
            });

            listDocumento.appendChild(listitem);
        }
    }

    /*EDITAR EN EL FORM DE TRÁMITE SIADO*/
    public void editar(Listitem item) {

        SiadoDetalle select = (SiadoDetalle) item.getAttribute("selected");
        try {
            for (Listitem items : nCmbTipoDoc.getItems()) {
                if (items.getValue().toString().equals(select.getTipoDoc().getIdTipoDoc())) {
                    items.setSelected(true);
                    getSubTipoDoc();
                    for (Listitem items1 : nCmbSubTipoDoc.getItems()) {
                        if (items1.getValue().toString().equals(select.getSubTipoDoc().getIdSubTipoDoc())) {
                            items1.setSelected(true);
                            getDetSubTipoDoc();
                            for (Listitem items2 : nCmbDetSubTipoDoc.getItems()) {
                                if (items2.getValue().toString().equals(select.getDetSubTipoDoc().getIdDetSubTipoDoc())) {
                                    items2.setSelected(true);
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    break;
                }
            }
            //select.setFechaEnvio(new FormatoFecha().formatFechaCorta(new Date()));

        } catch (NullPointerException ex) {

        }

        numZafra.setValue(select.getNumZafra());
        numero.setValue(select.getNumero());
        descripcion.setValue(select.getDescripcion());
        observacion.setValue(select.getObservacion());
        regente.setValue(select.getsRegente());
        anhoAprobacion.setValue(select.getAnhoAprobacion());
        anhoFin.setValue(select.getAnhoFin());
        usuarioEnvio.setValue(nTxtTitular.getValue());
        fechaEnvio.setValue(select.getFechaEnvio());
        //System.out.println(select.getDocumento().getTitulo());
        docDescripcion.setValue(select.getDocumento().getTitulo());
        docId.setValue(String.valueOf(select.getDocumento().getId()));
        // documentoId.setValue(select.getDocumento().getId());
        auxHead.setFocus(true);

    }

    /*ELIMINAR DE DETALLE DE TRÁMITE SIADO UN ITEM (SE OCULTA)*/
    public void eliminar(Listitem item) {
        //System.out.println(item);
        item.setVisible(false);
        ///listDocumento.removeItemAt(idDoc);
    }

    @Listen("onClick = #ok")
    public void okOpcion() {
        SiadoDetalle siadoDetalle = new SiadoDetalle();
        TipoDoc td = new TipoDoc();
        td.setIdTipoDoc(nCmbTipoDoc.getSelectedItem().getValue().toString());
        td.setDesTipoDoc(nCmbTipoDoc.getSelectedItem().getLabel().toString());
        siadoDetalle.setTipoDoc(td);

        SubTipoDoc std = new SubTipoDoc();
        std.setIdSubTipoDoc(nCmbSubTipoDoc.getSelectedItem().getValue().toString());
        std.setDesSubTipoDoc(nCmbSubTipoDoc.getSelectedItem().getLabel().toString());
        siadoDetalle.setSubTipoDoc(std);

        DetSubTipoDoc dstd = new DetSubTipoDoc();
        dstd.setIdDetSubTipoDoc(nCmbDetSubTipoDoc.getSelectedItem().getValue().toString());
        dstd.setDesDetSubTipoDoc(nCmbDetSubTipoDoc.getSelectedItem().getLabel().toString());
        siadoDetalle.setDetSubTipoDoc(dstd);

        siadoDetalle.setNumZafra(numZafra.getValue().toUpperCase());
        siadoDetalle.setNumero(numero.getValue().toUpperCase());
        siadoDetalle.setDescripcion(descripcion.getValue().toUpperCase());
        siadoDetalle.setObservacion(observacion.getValue().toUpperCase());
        siadoDetalle.setsRegente(regente.getValue().toUpperCase());
        siadoDetalle.setAnhoAprobacion(anhoAprobacion.getValue().toUpperCase());
        siadoDetalle.setAnhoFin(anhoFin.getValue().toUpperCase());
        siadoDetalle.setUsuarioEnvio(usuarioEnvio.getValue().toUpperCase());

        siadoDetalle.setFechaEnvio(fechaEnvio.getValue());//

        Documento doc = new Documento();
        doc.setId(Integer.parseInt(docId.getValue()));
        doc.setTitulo(docDescripcion.getValue());
        siadoDetalle.setDocumento(doc);

        listDocumento.getSelectedItem().setAttribute("selected", siadoDetalle);
        Listitem item = listDocumento.getSelectedItem();

        //put documental
        /*Listcell cell0 = (Listcell) item.getChildren().get(1);
        cell0.getChildren().clear();
        cell0.appendChild(new Label("ever"));*/
        //put tipo documental
        Listcell cell1 = (Listcell) item.getChildren().get(3);
        cell1.getChildren().clear();
        cell1.appendChild(new Label(td.getDesTipoDoc()));

        //put suttipo Documental
        Listcell cell2 = (Listcell) item.getChildren().get(4);
        cell2.getChildren().clear();
        cell2.appendChild(new Label(std.getDesSubTipoDoc()));

        //put detalle subtipo
        Listcell cell3 = (Listcell) item.getChildren().get(5);
        cell3.getChildren().clear();
        cell3.appendChild(new Label(dstd.getDesDetSubTipoDoc()));

        //num zufra
        Listcell cell4 = (Listcell) item.getChildren().get(6);
        cell4.getChildren().clear();
        cell4.appendChild(new Label(siadoDetalle.getNumZafra()));

        //numero
        Listcell cell5 = (Listcell) item.getChildren().get(7);
        cell5.getChildren().clear();
        cell5.appendChild(new Label(siadoDetalle.getNumero()));

        //descripcion
        Listcell cell6 = (Listcell) item.getChildren().get(8);
        cell6.getChildren().clear();
        cell6.appendChild(new Label(siadoDetalle.getDescripcion()));

        //observacion
        Listcell cell7 = (Listcell) item.getChildren().get(9);
        cell7.getChildren().clear();
        cell7.appendChild(new Label(siadoDetalle.getObservacion()));

        //Regente
        Listcell cell8 = (Listcell) item.getChildren().get(10);
        cell8.getChildren().clear();
        cell8.appendChild(new Label(siadoDetalle.getsRegente()));

        //Anho Aprobacion
        Listcell cell9 = (Listcell) item.getChildren().get(11);
        cell9.getChildren().clear();
        cell9.appendChild(new Label(siadoDetalle.getAnhoAprobacion()));

        //Anho fin
        Listcell cell10 = (Listcell) item.getChildren().get(12);
        cell10.getChildren().clear();
        cell10.appendChild(new Label(siadoDetalle.getAnhoFin()));

        //Usuario
        Listcell cell11 = (Listcell) item.getChildren().get(13);
        cell11.getChildren().clear();
        cell11.appendChild(new Label(siadoDetalle.getUsuarioEnvio()));

        //Fecha envio
        Listcell cell12 = (Listcell) item.getChildren().get(14);
        cell12.getChildren().clear();
        cell12.appendChild(new Label(siadoDetalle.getFechaEnvio()));

        auxHead.setVisible(false);

    }

    @Listen("onClick = #addDoc")
    public void addDoc() {
        popDocumento.open(addDoc);
    }

    @Listen("onClick =#cancel")// cerrar input de detalle
    public void cancelDetail() {
        auxHead.setVisible(false);
    }

    @Listen("onClick =#cancelar")// cerrar pantallas form
    public void cancelOpcion() {
        this.clearForm();
        nDivlistaRegistro.setVisible(true);
        nDivVentana.setVisible(false);
    }

    /*VALIDACIÓN Y CREACIÓN  DE UN TRÁMITE SIADO*/
    @Listen("onClick = #save")
    public void guardar() {
        try {
            Boolean state = true;

            Siado bean = new Siado();
            Serie serie = new Serie();
            serie.setIdSerie(nCmbSerie.getSelectedItem().getValue());
            bean.setSerie(serie);
            SubSerie subSerie = new SubSerie();
            subSerie.setIdSubSerie(nCmbSubSerie.getSelectedItem().getValue());
            bean.setSubSerie(subSerie);

            Sector sector = new Sector();
            sector.setIdSec(nCmbSector.getSelectedItem().getValue());
            bean.setSector(sector);

            //System.out.println("hola " + nTxtTitulo.getValue());
            if (!"".equals(nTxtTitulo.getValue())) {
                if (!"".equals(nTxtAnhoFin.getValue())) {
                    if (!"".equals(nTxtDepartamento.getValue())) {
//                        if (!"".equals(nTxtNro.getValue())) {
                        if (!"".equals(nTxtAnhoInicio.getValue())) {
                            if (!"".equals(nTxtDescripcion1.getValue())) {
                                if (!"".equals(nTxtTitular.getValue())) {
                                    // if ("".equals(nTxtPassword.getValue())) {
                                    //    state = false;
                                    //      showNotify("Contraseña no debe ser vacio", win, "warning", "top_center");
                                    //   }
                                } else {
                                    state = false;
                                    showNotify("Titular no debe ser vacio", win, "warning", "top_center");
                                }
                            } else {
                                state = false;
                                showNotify("Descripción no debe ser vacio", win, "warning", "top_center");
                            }
                        } else {
                            state = false;
                            showNotify("Año inicio no debe ser vacio", win, "warning", "top_center");
                        }
                        /*} else {
                            state = false;
                            showNotify("Número SITD no debe ser vacio", win, "warning", "top_center");
                        }*/
                    } else {
                        state = false;
                        showNotify("Área ubicación no debe ser vacio", win, "warning", "top_center");
                    }
                } else {
                    state = false;
                    showNotify("año fin no debe ser vacio", win, "warning", "top_center");
                }
            } else {
                state = false;
                showNotify("Titulo habitante no debe ser vacio", win, "warning", "top_center");
            }
            if (state) {
                bean.setTituloHabilitante(nTxtTitulo.getValue().toUpperCase());
                bean.setMesInicio(nCmbMesAnio.getSelectedItem().getValue());
                bean.setAnhoFin(nTxtAnhoFin.getValue());
                bean.setAreaUbicacion(nTxtDepartamento.getValue().toUpperCase());
//                bean.setNumeroSITD(nTxtNro.getValue());
                bean.setAnhoInicio(nTxtAnhoInicio.getValue());
                bean.setDescripcion(nTxtDescripcion1.getValue().toUpperCase());
                bean.setTitular(nTxtTitular.getValue().toUpperCase());
                //      bean.setPassword(nTxtPassword.getValue());
                bean.setMesFin(nCmbMesFin.getSelectedItem().getValue());
                bean.setObservacion(nTxtObservacion1.getValue().toUpperCase());
                bean.setRegente(nChkRegente.isChecked());
                bean.setEstado(true);
                List<Listitem> item_ = listDocumento.getItems();
                if (item_.size() > 0) {
                    //int siadId = new SiadoDao().insert(bean);
                    for (Listitem i : item_) {
                        if (i.isVisible()) {
                            SiadoDetalle siadoDetalle = new SiadoDetalle();
                            siadoDetalle = (SiadoDetalle) i.getAttribute("selected");
                            if (siadoDetalle.getTipoDoc() != null) {
                                if (siadoDetalle.getSubTipoDoc() != null) {
                                    if (siadoDetalle.getDetSubTipoDoc() == null) {
                                        state = false;
                                        showNotify("Es necesario seleccionar detalle de sub tipo de documento", win, "error", "top_center");
                                    }
                                } else {
                                    state = false;
                                    showNotify("Es necesario seleccionar sub tipo de documento", win, "warning", "top_center");
                                }
                            } else {
                                // System.out.println(siadoDetalle.getTipoDoc());
                                state = false;
                                showNotify("Es necesario seleccionar tipo de documento", win, "warning", "top_center");
                            }
                        }
                    }
                } else {
                    state = false;
                    showNotify("Es necesario agregar documento", win, "warning", "top_center");
                }
                if (state) {

                    Date date = new Date();
                    String str = new SimpleDateFormat("yyyy-MM-dd").format(date);

                    bean.setFechaEnvio(str);
                    bean.setEstadoTramiteId(1);//REGISTRADO
                    int siadId = new SiadoDao().insert(bean);

                    for (Listitem i : item_) {
                        if (i.isVisible()) {
                            SiadoDetalle siadoDetalle = new SiadoDetalle();
                            siadoDetalle = (SiadoDetalle) i.getAttribute("selected");
                            Siado si = new Siado();
                            si.setSiadId(siadId);
                            siadoDetalle.setSiado(si);
                            new SiadoDetalleDao().insert(siadoDetalle);
                            nDivlistaRegistro.setVisible(true);
                            nDivVentana.setVisible(false);
                        }
                    }
                    showNotify("El trámite siado se guardó correctamente", win, "info", "top_center");
                    dibujarSiado();

                } else {
                    System.out.println("Falta validar");
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // throw new Exception(ex.getMessage());
        }
    }

    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

    @Listen("onClick = #nuevo")
    public void nuevoRegistro() {
        this.clearForm();
        editar = false;
    }

    /*EDITAR  GRID SIADO::: ESTÁ OCULTO */
    public void editarSiado(Siado item) {
        listDocumento.getItems().clear();
        nDivVentana.setVisible(true);
        nDivlistaRegistro.setVisible(false);
        nTxtTitulo.setValue(item.getTituloHabilitante());
        nTxtAnhoFin.setValue(item.getAnhoFin());
        nTxtDepartamento.setValue(item.getAreaUbicacion());
//        nTxtNro.setValue(item.getNumeroSITD());
        nTxtAnhoInicio.setValue(item.getAnhoInicio());
        nTxtDescripcion1.setValue(item.getDescripcion());
        nTxtTitular.setValue(item.getTitular());
        //  nTxtPassword.setValue(item.getPassword());
        nTxtObservacion1.setValue(item.getObservacion());
        nChkRegente.setChecked(item.getRegente());

        List<SiadoDetalle> siadoDetalle = new ArrayList<SiadoDetalle>();
        siadoDetalle = new SiadoDetalleDao().getRelacionById(item.getSiadId());
        for (SiadoDetalle sd : siadoDetalle) {

            Listitem listitem = new Listitem();

            listitem.setAttribute("nData", item);
            Listcell check = new Listcell();
            check.appendChild(new Label(""));

            Listcell cell1 = new Listcell();
            cell1.appendChild(new Label(sd.getDocumento().getTitulo()));
            Listcell cell2 = new Listcell();
            System.out.println(sd.getTipoDoc().getIdTipoDoc());
            cell2.appendChild(new Label(sd.getTipoDoc().getDesTipoDoc()));

            Listcell cell6 = new Listcell();
            cell6.appendChild(new Label(sd.getNumZafra()));

            Listcell cell7 = new Listcell();
            cell7.appendChild(new Label(sd.getNumero()));

            listitem.appendChild(check);
            listitem.appendChild(cell1);
            listitem.appendChild(cell2);
            listitem.appendChild(cell6);
            listitem.appendChild(cell7);

            listDocumento.appendChild(listitem);
        }
        editar = true;
    }

    /*ELIMINAR DEL GRID  SIADO*/
    public void eliminarSiado(Siado item) {
        System.out.println(item.getSiadId());
        Messagebox.show(
                "Desea eliminar este trámite siado?", "Dialogo de Confirmación ",
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event evt) throws Exception {
                if (evt.getName().equals("onOK")) {
                    new SiadoDao().delete(item);
                    dibujarSiado();
                }
            }
        });
    }

    /*ENVIO DE UN SOLO DOCUMENTO A OSINFOR*/
    public void sendSiado(Siado item) {
        List<SiadoDetalle> siadoDetalle = new ArrayList<SiadoDetalle>();
        siadoDetalle = new SiadoDetalleDao().getRelacionById(item.getSiadId());
        String nombres = "";
        for(SiadoDetalle sd : siadoDetalle){
            nombres += " "+sd.getDocumento().getTitulo()+", ";
        }
        
        Messagebox.show(
                "Desea enviar este trámite siado?, contiene los siguientes archivos: "+nombres, "Dialogo de Confirmación ",
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event evt) throws Exception {
                if (evt.getName().equals("onOK")) {
                    try {

                        /*documents*/
                        if (item.getEstadoTramiteId() == 2) {
                            showNotify("Los tramites no pueden ser reenviados...", win, "warning");
                        } else {
                            HashMap params = new HashMap();
                            params.put("Token", token);
                            params.put("IdInsDoc", ins);
                            params.put("IdSec", item.getSector().getIdSec());
                            params.put("IdSistema", 12);// IDSISTEMA OPTIMZAR
                            params.put("IdSerie", item.getSerie().getIdSerie());
                            params.put("IdSubSerie", item.getSubSerie().getIdSubSerie());
                            params.put("CodMesInicio", item.getMesInicio());
                            params.put("CodMesFin", item.getMesFin());
                            params.put("AnioInicio", item.getAnhoInicio());
                            params.put("AnioFin", item.getAnhoFin());
                            params.put("Persona", item.getTitular());
                            params.put("NumeroTHabilitante", item.getTituloHabilitante());
                            params.put("Descripcion", item.getDescripcion());
                            params.put("Observacion", item.getObservacion());
                            params.put("Area", item.getAreaUbicacion());
                            params.put("UsuarioCrea", usuario);

                            List<SiadoDetalle> siadoDetalle = new ArrayList<SiadoDetalle>();
                            siadoDetalle = new SiadoDetalleDao().getRelacionById(item.getSiadId());
                            List params_detail = new ArrayList<>();
                            for (SiadoDetalle sd : siadoDetalle) {
                                HashMap det = new HashMap();
                                det.put("IdTipoDoc", sd.getTipoDoc().getIdTipoDoc());
                                det.put("IdSubTipoDoc", sd.getSubTipoDoc().getIdSubTipoDoc());
                                det.put("IdDetSubTipoDoc", sd.getDetSubTipoDoc().getIdDetSubTipoDoc());
                                det.put("IdSistema", 12);// IDSISTEMA OPTIMZAR
                                det.put("NomPersona", sd.getUsuarioEnvio());
                                det.put("Fecha", sd.getFechaEnvio());
                                det.put("Descripcion", sd.getDescripcion());
                                det.put("Observacion", sd.getObservacion());
                                det.put("Numero", sd.getNumero());
                                det.put("Regente", sd.getsRegente());
                                det.put("NumZafra", sd.getNumZafra());
                                det.put("AnioAprobIni", sd.getAnhoAprobacion());
                                det.put("AnioAprobFin", sd.getAnhoFin());
                                /*url*/
                                String url;
                                String rutaLink = new ConexionReporte().obtenerCarpeta("link");
                                Integer tamRela = new DocumentoDao().getRelacion(sd.getDocumento().getDocuGroup());
                                if (tamRela > 1) {
                                    url = rutaLink + "/Downloads.pdf?id=" + sd.getDocumento().getDocuGroup();
                                } else {
                                    url = rutaLink + "/ArcDig.pdf?id=" + sd.getDocumento().getId();
                                }
                                det.put("Url", url);

                                params_detail.add(det);
                            }
                            
                            params.put("Lista", params_detail);
                            /*ENVIO DE TRÁMITE SIADO A OSINFOR*/
                            JSONSiadoOscinfor data_oscinfor = sendOsinfor(params);
                            System.out.println(data_oscinfor.toString());
                            /*ENVIO DE TRÁMITE SIADO A OSINFOR*/
                            if (data_oscinfor != null) {
                                String numero_expediente = data_oscinfor.getIdExpediente();
                                String numero_tramite = data_oscinfor.getIdTramite();
                                String password = data_oscinfor.getPassword();
                                Siado bean = new Siado();
                                bean.setNumeroExpediente(numero_expediente);
                                bean.setNumeroTramite(numero_tramite);
                                bean.setPassword(password);
                                bean.setEstadoTramiteId(2);
                                bean.setSiadId(item.getSiadId());
                                new SiadoDao().updateNumeros(bean);
                            }
                            dibujarSiado();
                        }

                    } catch (Exception ex) {
                        System.out.println("mal....");
                        showNotify("ERROR EN EL SERVIDOR DE OSCINFOR..." + ex.getMessage(), win, "warning");
                        System.out.println("mal al anenviar antes de actualizar");
                        System.out.println(ex.getMessage());
                    }

                }
            }
        });
    }

    /*ENVIO MULTIPLE DE DOCUMENTOS A OSINFOR*/
    @Listen("onClick = #sendMultiple")
    public void sendMultiple() {
        Set item = listSiado.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        if (emp.size() > 0) {
            Boolean state = false;
            for (int i = 0; i < emp.size(); i++) {
                SelectItem = items_siado.get(emp.get(i).getIndex());
                if (SelectItem.getEstadoTramiteId() == 2) {
                    state = true;
                }
            }
            String message = (state) ? "Trámites enviados no serán reenviados.Desea continuar?" : "Desea enviar este trámite siado?";
            Messagebox.show(
                    message, "Dialogo de Confirmación ",
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event evt) throws Exception {
                    if (evt.getName().equals("onOK")) {
                        try {
                            String ins = new ConexionReporte().obtenerPropiedad("siado_IdInst");
                            
                            for (int i = 0; i < emp.size(); i++) {

                                SelectItem = items_siado.get(emp.get(i).getIndex());
                                if (SelectItem.getEstadoTramiteId() == 1) {
                                    HashMap params = new HashMap();
                                    List params_detail = new ArrayList<>();
                                    params.put("Token", token);
                                    params.put("IdInsDoc", ins);
                                    params.put("IdSec", SelectItem.getSector().getIdSec());
                                    params.put("IdSistema", 12);// IDSISTEMA OPTIMZAR
                                    params.put("IdSerie", SelectItem.getSerie().getIdSerie());
                                    params.put("IdSubSerie", SelectItem.getSubSerie().getIdSubSerie());
                                    params.put("CodMesInicio", SelectItem.getMesInicio());
                                    params.put("CodMesFin", SelectItem.getMesFin());
                                    params.put("AnioInicio", SelectItem.getAnhoInicio());
                                    params.put("AnioFin", SelectItem.getAnhoFin());
                                    params.put("Persona", SelectItem.getTitular());
                                    params.put("NumeroTHabilitante", SelectItem.getTituloHabilitante());
                                    params.put("Descripcion", SelectItem.getDescripcion());
                                    params.put("Observacion", SelectItem.getObservacion());
                                    params.put("Area", SelectItem.getAreaUbicacion());
                                    params.put("UsuarioCrea", "A.LABAJOS");

                                    List<SiadoDetalle> siadoDetalle = new ArrayList<SiadoDetalle>();
                                    siadoDetalle = new SiadoDetalleDao().getRelacionById(SelectItem.getSiadId());
                                    for (SiadoDetalle sd : siadoDetalle) {

                                        HashMap det = new HashMap();
                                        det.put("IdTipoDoc", sd.getTipoDoc().getIdTipoDoc());
                                        det.put("IdSubTipoDoc", sd.getSubTipoDoc().getIdSubTipoDoc());
                                        det.put("IdDetSubTipoDoc", sd.getDetSubTipoDoc().getIdDetSubTipoDoc());
                                        det.put("IdSistema", 12);// IDSISTEMA OPTIMZAR
                                        det.put("NomPersona", sd.getUsuarioEnvio());
                                        det.put("Fecha", sd.getFechaEnvio());
                                        det.put("Descripcion", sd.getDescripcion());
                                        det.put("Observacion", sd.getObservacion());
                                        det.put("Numero", sd.getNumero());
                                        det.put("Regente", sd.getsRegente());
                                        det.put("NumZafra", sd.getNumZafra());
                                        det.put("AnioAprobIni", sd.getAnhoAprobacion());
                                        det.put("AnioAprobFin", sd.getAnhoFin());
                                        /*url*/
                                        String url;
                                        String rutaLink = new ConexionReporte().obtenerCarpeta("link");
                                        Integer tamRela = new DocumentoDao().getRelacion(sd.getDocumento().getDocuGroup());
                                        if (tamRela > 1) {
                                            url = rutaLink + "/Downloads.pdf?id=" + sd.getDocumento().getDocuGroup();
                                        } else {
                                            url = rutaLink + "/ArcDig.pdf?id=" + sd.getDocumento().getId();
                                        }
                                        det.put("Url", url);

                                        params_detail.add(det);
                                    }
                                    params.put("Lista", params_detail);

                                    /*ENVIO DE TRÁMITE SIADO A OSINFOR*/
                                    JSONSiadoOscinfor data_oscinfor = sendOsinfor(params);
                                    /*ENVIO DE TRÁMITE SIADO A OSINFOR*/
                                    if (data_oscinfor != null) {
                                        String numero_expediente = data_oscinfor.getIdExpediente();
                                        String numero_tramite = data_oscinfor.getIdTramite();

                                        Siado bean = new Siado();

                                        bean.setNumeroExpediente(numero_expediente);
                                        bean.setNumeroTramite(numero_tramite);
                                        bean.setEstadoTramiteId(2);
                                        bean.setSiadId(SelectItem.getSiadId());
                                        //System.out.println("ok1..");
                                        new SiadoDao().updateNumeros(bean);
                                        //System.out.println(SelectItem.getSiadId());
                                    }
                                }

                            }

                            dibujarSiado();

                        } catch (Exception ex) {
                            showNotify("INTENTE ENVIAR NUEVAMENTE...", win, "warning");/*problemas de token y de IdSec*/
                            System.out.println(ex.getMessage());
                        }

                    }
                }
            });
        } else {
            showNotify("Porfavor seleccione un item para enviar", win, "warning", "top_center");
        }

    }

    @Listen("onChange = #filtro")
    public void buscar() {
        dibujarSiado();
    }

    @Listen("onClick = #update")
    public void actualizar() {
        dibujarSiado();
    }

    /*AUTENTICACIÓN CON OSINFOR*/
    public String loginOsinfor(String usuario, String password, String alias) {
        try {
            String token = "";
            
            HashMap params = new HashMap();
            
            params.put("usuario", usuario);
            params.put("password", password);
            params.put("alias", alias);
            //params.put("sistemaid", sistemaid);// IDSISTEMA OPTIMZAR
            
            //crear cliente
            Client client = ClientBuilder.newClient();
            //target to cliente
            String pathService = new ConexionReporte().obtenerCarpeta("siado_url");
            WebTarget target = client.target(pathService);
            //convert Json to Class
            Gson gson1 = new Gson();        
            String jsonInString = target.request().post(Entity.entity(gson1.toJson(params), MediaType.APPLICATION_JSON), String.class);
            
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(jsonInString, JsonObject.class);
            JsonOscinforToken dat = gson.fromJson(gson.toJson(json), JsonOscinforToken.class);
            token = dat.getToken();
            
            return token;
        } catch (Exception ex) {
            return "";
        }
    }

    /*ENVIO A OSINFOR PRUEBA*/
    public JSONSiadoOscinfor sendOsinfor(HashMap params) {
        JSONSiadoOscinfor dat_response = null;
        try {
            Gson gson1 = new Gson();
            String jsonx = gson1.toJson(params);
            System.out.print(jsonx);
            //showNotify(jsonx, win, "info");
            
            Client client = ClientBuilder.newClient();
            //target to cliente
            //System.out.println("aaaa");
            String pathService = new ConexionReporte().obtenerCarpeta("service1");
            WebTarget target = client.target(pathService + "api/documentos/EnviarDocumento");
            //convert Json to Class
            //  String jsonInString = target.request(MediaType.APPLICATION_JSON).post(Entity);
            //showNotify(gson1.toJson(params)+"", win, "info");
            /// System.out.println(gson1.toJson(params)+"");
            String jsonInString = target.request().header(HttpHeaders.AUTHORIZATION, "Bearer "+ token).post(Entity.entity(gson1.toJson(params), MediaType.APPLICATION_JSON), String.class);
//            asdsad
            
            
            Gson gson = new Gson();

            JsonObject json = gson.fromJson(jsonInString, JsonObject.class);
           
            JSONSiadoOscinfor data = gson.fromJson(gson.toJson(json), JSONSiadoOscinfor.class);
            if (data.getStatusCode() == 200) {
                dat_response = data;
                showNotify("ENVIADO CORRECTAMENTE", win, "info");
                return data;
            } else {
                showNotify("ERROR EN EL SERVIDOR DE OSCINFOR", win, "warning");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " cause " + ex.getCause());
            // showNotify("ERROR AL CREAR EL TRÁMITE SIADO, ELIMINE Y VUELVE A CREAR", win, "warning");
        }

        return dat_response;
    }

    private void showNotify(String msg, Component ref, String type) {
        Clients.showNotification(msg, type, ref, "middle_center", 3000);
    }

}
