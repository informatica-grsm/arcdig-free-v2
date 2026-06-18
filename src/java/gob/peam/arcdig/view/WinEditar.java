/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Categoria;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.DocumentoEtiqueta;
import gob.peam.arcdig.beans.EtiquetaDocu;
import gob.peam.arcdig.beans.Metadata;
import gob.peam.arcdig.beans.Notificacion;
import gob.peam.arcdig.beans.Persona;
import gob.peam.arcdig.beans.TipoDocumento;
import gob.peam.arcdig.dao.CategoriaDao;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.dao.EtiquetaDocuDao;
import gob.peam.arcdig.dao.MetaDataDao;
import gob.peam.arcdig.dao.NotificacionDao;
import gob.peam.arcdig.dao.PermisoDao;
import gob.peam.arcdig.dao.PersonaDao;
import gob.peam.arcdig.dao.TipoDocumentoDao;
import gob.peam.arcdig.utils.FormatoFecha;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Cj.Legacy
 */
public class WinEditar extends SelectorComposer<Component> {

    @Wire
    Textbox textTitulo, textResumen, textCodigo;

    private List<Metadata> metaData = (List<Metadata>) new ArrayList<Metadata>();
    private List<Listitem> etiqs = new ArrayList<Listitem>();
    private List<Listitem> personalizado = new ArrayList<Listitem>();
    Notificacion notificacion = new Notificacion();
    List<Listitem> correo = new ArrayList<Listitem>();
    private Boolean ocho;

    private Boolean editEtiqueta = false;

    private String rutaHtml;

    @Wire
    Listbox tipoDocu, cateDocu;

    @Wire
    Iframe viewFrame;

    public Documento doc;

    private EventQueue eq, eq1, eq2, eq3, eq4;

    @Wire
    Window winUpload;

    @Wire
    Datebox fechaDocu;

    @Wire
    Rows rows;

    @Wire
    Button privilegio;

    @Wire
    Div etiquetas;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        gob.peam.arcdig.beans.Permiso perm = new gob.peam.arcdig.beans.Permiso();
        String dni = "";
        int idPers = 0;
        
        try {
            dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
            idPers = Integer.parseInt(Executions.getCurrent().getDesktop().getSession().getAttribute("usuaId").toString());
            perm = new PermisoDao().getPermisos(dni);
            ocho = !perm.getItem8();
        } catch (NullPointerException ex) {
            ocho = false;
        }
        
        Listitem item = (Listitem) exec.getArg().get("bean");
        eq1 = EventQueues.lookup("dataNotificacion", EventQueues.DESKTOP, true);
        eq1.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                notificacion = (Notificacion) hm.get("notificacion");
            }
        });

        eq2 = EventQueues.lookup("dataPropiedad", EventQueues.DESKTOP, true);
        eq2.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                personalizado = (List<Listitem>) hm.get("personas");
            }
        });

        eq3 = EventQueues.lookup("dataPersona", EventQueues.DESKTOP, true);
        eq3.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                correo = (List<Listitem>) hm.get("personas");
                if (!correo.isEmpty()) {
                    Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winExpiraEmail.zul", null, null);
                    window1.doModal();
                }
                //guardarPersonas(personas);
            }
        });

        Integer id = Integer.parseInt((item.getId().split(";"))[0]);

        doc = new DocumentoDao().getOne(id);
        try {
            textTitulo.setValue(doc.getTituloMin() == null ? doc.getTitulo() : doc.getTituloMin());
        } catch (NullPointerException ex) {
            textTitulo.setValue("");
        }

        try {
            textResumen.setValue(doc.getResumenMin() == null ? doc.getResumen() : doc.getResumenMin());
        } catch (NullPointerException ex) {
            textResumen.setValue("");
        }
        
        try{
            textCodigo.setValue(doc.getDocuCodigo() == null ? doc.getDocuCodigo(): doc.getDocuCodigo());
        }catch(NullPointerException ex){
            textCodigo.setValue("");
        }

        try {
            fechaDocu.setValue(new FormatoFecha().formatStringFechaCorta(doc.getFechaDocx()));
        } catch (NullPointerException ex) {
            fechaDocu.setValue(null);
        }
        Integer tidoId = doc.getTidoId();
        Integer cateId = doc.getCateId();

        if (doc.getMetaData() != null && !"".equals(doc.getMetaData())) {
            //divMetadata.setVisible(true);
            metaData = new MetaDataDao().getMetadata(doc);
            dibujarMetadata(doc.getTidoId());
        }

        try {
            if (doc.getPublico() == true && doc.getPrivado() == false && doc.getPersonalizado() == false) {
                privilegio.setIconSclass("z-icon-globe");
            } else if (doc.getPublico() == false && doc.getPrivado() == true && doc.getPersonalizado() == false) {
                privilegio.setIconSclass("z-icon-user");
            } else {
                privilegio.setIconSclass("z-icon-cog");
            }
        } catch (NullPointerException ex) {

        }

        dibujarCategoria(cateId);
        dibujarTipoDocumento(tidoId);

        if (!doc.getEtiquetaDocu().isEmpty()) {
            for (int i = 0; i < doc.getEtiquetaDocu().size(); i++) {
                Label label = new Label();
                label.setValue(doc.getEtiquetaDocu().get(i).getNombre());
                label.setSclass("label label-warning arrowed arrowed-in-right");
                etiquetas.appendChild(label);
            }
        } else {
            etiquetas.appendChild(new Label("No tiene"));
        }

        eq4 = EventQueues.lookup("dataEdiEtiqueta", EventQueues.DESKTOP, true);
        eq4.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                etiqs = (List<Listitem>) hm.get("etiquetas");
                editEtiqueta = true;
                modificarEtiquetas();
            }
        });

        viewFrame.setSrc("./ArcDig.pdf?id=" + doc.getId());
    }

    public void dibujarMetadata(Integer tidoId) {
        List<Metadata> items = (List<Metadata>) new ArrayList<Metadata>();
        HashMap hm = new HashMap();
        hm.put("tidoId", tidoId);
        items = new MetaDataDao().listarMetaData(hm);

        if (metaData.isEmpty()) {
            metaData = items;
        }

        rows.getChildren().clear();
        for (Metadata item : items) {
            Row row = new Row();
            Label label = new Label();
            label.setValue(item.getCampo());
            final Textbox tb = new Textbox();
            if (!metaData.isEmpty()) {
                for (int i = 0; i < metaData.size(); i++) {
                    if (item.getCampo().equals(metaData.get(i).getCampo())) {
                        tb.setValue(metaData.get(i).getDetalle());
                        break;
                    }
                }
            }

            tb.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.WinEditar.changeTextbox(item));

            tb.setPlaceholder("Escribir Texto");
            Label label1 = new Label();
            row.appendChild(label);
            row.appendChild(tb);
            row.appendChild(label1);
            rows.appendChild(row);
        }

    }

    class changeTextbox implements EventListener<InputEvent> {

        final Metadata item;

        public changeTextbox(Metadata meta) {
            this.item = meta;
        }

        public void onEvent(InputEvent event) throws Exception {
            //showNotify(event.getValue(), win, "info", "top_right");
            for (int i = 0; i < metaData.size(); i++) {
                if (item.getCampo().equals(metaData.get(i).getCampo())) {
                    metaData.get(i).setDetalle(event.getValue());
                    break;
                }
            }
        }
    }

    public void dibujarCategoria(Integer cateId) {
        List<Categoria> items = (List<Categoria>) new ArrayList<Categoria>();

        items = new CategoriaDao().listarCategorias();
        cateDocu.getItems().clear();
        for (Categoria item : items) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getCateId());
            listitem.setLabel(item.getCateNombre());
            if (cateId == item.getCateId()) {
                listitem.setSelected(true);
            }
            cateDocu.appendChild(listitem);
        }

        if (!items.isEmpty()) {
            if (cateId == 0) {
                cateDocu.setSelectedIndex(0);
            }
        }
    }

    @Listen("onSelect = #tipoDocu")
    public void selectTipoDocu() {
        metaData.clear();
        Integer tidoId = (Integer) tipoDocu.getSelectedItem().getValue();
        TipoDocumento td = new TipoDocumento();
        td = new TipoDocumentoDao().get(tidoId);
        String text = "";
        try {
            for (String str : td.getMetadata().split("%")) {
                text += "[" + str + "]";
            }
        } catch (NullPointerException ex) {
        }

        if (td.getPrivacidad() == 1) {
            privilegio.setIconSclass("z-icon-globe");
        } else if (td.getPrivacidad() == 2) {
            privilegio.setIconSclass("z-icon-user");
        } else {
            privilegio.setIconSclass("z-icon-cog");
        }
        dibujarMetadata(tidoId);
    }

    public void dibujarTipoDocumento(Integer tidoId) {
        List<TipoDocumento> items = (List<TipoDocumento>) new ArrayList<TipoDocumento>();

        items = new TipoDocumentoDao().listarTipoDocumento();
        tipoDocu.getItems().clear();
        for (TipoDocumento item : items) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getId());
            listitem.setLabel(item.getNombre());
            if (tidoId == item.getId()) {
                listitem.setSelected(true);
            }
            tipoDocu.appendChild(listitem);

        }
        if (!items.isEmpty()) {
            if (tipoDocu.getSelectedItem() == null) {
                tipoDocu.setSelectedIndex(0);
            }
        }

        //dibujarMetadata((Integer) tipoDocu.getSelectedItem().getValue());
    }

    @Listen("onClick=#close")
    public void close() {
        winUpload.detach();
    }

    @Listen("onClick = #notificar")
    public void config() {
        HashMap hm = new HashMap();
        hm.put("personas", correo);
        hm.put("propiedad", false);

        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winPersona.zul", null, hm);
        window1.doModal();

    }

    @Listen("onClick = #personaliza")
    public void personaliza() {
        HashMap hm = new HashMap();
        hm.put("personas", personalizado);
        hm.put("propiedad", true);

        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winPersona.zul", null, hm);
        window1.doModal();

    }

    @Listen("onClick=#save")
    public void save() {
        if ("z-icon-globe".equals(privilegio.getIconSclass())) {
            doc.setPublico(true);
            doc.setPrivado(false);
            doc.setPersonalizado(false);
        } else if ("z-icon-user".equals(privilegio.getIconSclass())) {
            doc.setPrivado(true);
            doc.setPublico(false);
            doc.setPersonalizado(false);
        } else {
            doc.setPrivado(false);
            doc.setPublico(false);
            doc.setPersonalizado(true);
        }

        doc.setTituloMin(textTitulo.getValue());
        doc.setTitulo(textTitulo.getValue().toUpperCase());
       

        doc.setResumenMin(textResumen.getValue());
        doc.setResumen(textResumen.getValue().toUpperCase());
        doc.setDocuCodigo(textCodigo.getValue().trim());

        doc.setTidoId(Integer.parseInt(tipoDocu.getSelectedItem().getValue().toString()));
        doc.setCateId(Integer.parseInt(cateDocu.getSelectedItem().getValue().toString()));

        doc.setFechaDocx(new FormatoFecha().formatFechaCorta(fechaDocu.getValue()));
        
        

        if (!metaData.isEmpty()) {
            String itemMeta = "<metadata>";
            for (Metadata meta : metaData) {
                itemMeta += "<docu_metadata><meta_id>" + meta.getId() + "</meta_id><meta_nombre>" + meta.getCampo() + "</meta_nombre><meta_descripcion>" + meta.getDetalle() + "</meta_descripcion></docu_metadata>";
            }
            itemMeta += "</metadata>";
            doc.setMetaData(itemMeta);
        }
        if (editEtiqueta) {
            new EtiquetaDocuDao().delete(doc.getId());
            for (Listitem eti : etiqs) {
                DocumentoEtiqueta de = new DocumentoEtiqueta();
                de.setDocuId(doc.getId());
                de.setEtiqId(Integer.parseInt(eti.getId()));
                new EtiquetaDocuDao().insert(de);
            }
        }
        if (!correo.isEmpty()) {
            notificacion.setDocuId(doc.getId());
            new NotificacionDao().delete(notificacion);
            for (Listitem item : correo) {
                Persona p = new PersonaDao().get(item.getId());
                notificacion.setPersDni(p.getDni());
                new NotificacionDao().insert(notificacion);
            }
        }
        if(remplazo_){
            try {
                byte[] archinuevo =  this.archMedia_remp.getByteData();
                String carpeta = new ConexionReporte().obtenerPropiedad("carpeta");
                
                writeBytesToFile(archinuevo,carpeta+File.separator+doc.getRuta()+File.separator+doc.getId()+".pdf" );
            } catch (IOException ex) {
                Logger.getLogger(WinEditar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        new DocumentoDao().update(doc);
        winUpload.detach();
        eq = EventQueues.lookup("actualizar", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, null));
        eq1.close();
        eq2.close();
        eq3.close();
        eq4.close();

    }

        private static void writeBytesToFile(byte[] bFile, String fileDest) {
        try (FileOutputStream fileOuputStream = new FileOutputStream(fileDest)) {
            fileOuputStream.write(bFile);
            fileOuputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    @Listen("onClick = #etiquetas")
    public void editEtiquetas() {
        if (ocho) {
            HashMap hm = new HashMap();
            List<Listitem> items = new ArrayList<Listitem>();
            for (EtiquetaDocu item : doc.getEtiquetaDocu()) {
                Listitem listitem = new Listitem();
                Listcell check = new Listcell();
                check.appendChild(new Label(""));
                Listcell nombre = new Listcell();
                nombre.appendChild(new Label(String.valueOf(item.getNombre())));

                listitem.appendChild(check);
                listitem.appendChild(nombre);
                listitem.setSelected(true);
                listitem.setId(item.getId().toString());
                items.add(listitem);
            }
            hm.put("etiquetas", items);
            hm.put("modificar", true);
            Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winEdiEtiqueta.zul", null, hm);
            window1.doModal();
        }
    }

    public void modificarEtiquetas() {
        //new EtiquetaDocuDao().delete(doc.getId());
        /*for (Listitem eti : items) {
         DocumentoEtiqueta de = new DocumentoEtiqueta();
         de.setDocuId(doc.getId());
         de.setEtiqId(Integer.parseInt(eti.getId()));
         new EtiquetaDocuDao().insert(de);
         }*/
        etiquetas.getChildren().clear();
        doc = new DocumentoDao().getOne(doc.getId());
        if (!etiqs.isEmpty()) {
            for (Listitem eti : etiqs) {
                EtiquetaDocu item = new EtiquetaDocuDao().getEtiqueta(Integer.parseInt(eti.getId()));
                Label label = new Label();
                label.setValue(item.getNombre());
                label.setSclass("label label-warning arrowed arrowed-in-right");
                etiquetas.appendChild(label);
            }
        } else {
            etiquetas.appendChild(new Label("No tiene"));
        }

    }
    
    @Wire
    Media archMedia_remp;
    Boolean remplazo_=false;
    @Listen("onUpload = #up")
    public void nBtnSubirArchivo(UploadEvent event) throws IOException {

        archMedia_remp = event.getMedia();
        int mediaSize = archMedia_remp.getStreamData().available();
        if ("application/pdf".equals(archMedia_remp.getContentType())) {
            if ("application/pdf".equals(archMedia_remp.getContentType())) {
                if (mediaSize <= 104857600) {//maximo 100 MB = 104857600 bytes
                    //nTxtArchivo.setValue(archMedia_remp.getName());    

                   

                    guardarTemp("remplazo");

                 
                    viewFrame.setStyle("border:4px solid red");
                   
                    viewFrame.setSrc( "./tmp/"+namePDF);

                    /*................................................................*/
                    remplazo_ = true;

                    return;
                } else {
                    showNotify("Limite máximo hasta 100 MB", win, "info", "top_center");
                }
            }
        } else {
            showNotify("Solo archivos tipo PDF", win, "warning", "top_center");
        }
    }
    
    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }
    
    String namePDF = "";
    String pathOrigin = "";
    String rutaArchivoTemporal ="";
    private String guardarTemp(String tipo) {

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        String rootPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/tmp");

        try {

            //crea las carpetas ...............................................            
            File carpeta = new File(rootPath);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            int aleatorio = generarNumeroAleatorio(rootPath);
            namePDF = aleatorio + ".pdf";

            //.................................................................
            InputStream fin = null;
            if (tipo.equals("remplazo")) {
                fin = this.archMedia_remp.getStreamData();
            }
            if (tipo.equals("copiarTemp")) {
                File initialFile = new File(pathOrigin);
                InputStream targetStream = new FileInputStream(initialFile);
                fin = targetStream;
            }

            in = new BufferedInputStream(fin);

            File file = new File(rootPath + "/" + aleatorio + ".pdf");
            rutaArchivoTemporal = rootPath + "/" + aleatorio + ".pdf";

            OutputStream fout = new FileOutputStream(file);
            out = new BufferedOutputStream(fout);
            byte buffer[] = new byte[245760];
            int ch = in.read(buffer);
            while (ch != -1) {
                out.write(buffer, 0, ch);
                ch = in.read(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return rutaArchivoTemporal;
    }

    private Integer generarNumeroAleatorio(String rootPath) {
        int valorInicial = 10000;
        int valorFinal = 99999;

        int i = 1;
        int numero = 0;

        while (i > 0) {
            numero = (int) (Math.random() * (valorFinal - valorInicial + 1) + valorInicial);

            File archivo = new File(rootPath + "\\" + numero + ".pdf");
            if (!archivo.exists()) {
                i = 0;
            }

        }
        return numero;

    }

}
