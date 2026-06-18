package gob.peam.arcdig.view;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.DocumentoEtiqueta;
import gob.peam.arcdig.beans.DocumentoUsuario;
import gob.peam.arcdig.beans.Metadata;
import gob.peam.arcdig.beans.MineType;
import gob.peam.arcdig.beans.Notificacion;
import gob.peam.arcdig.beans.Persona;
import gob.peam.arcdig.beans.RutaRapida;
import gob.peam.arcdig.beans.TipoDocumento;
import gob.peam.arcdig.beans.TipoMetadata;
import gob.peam.arcdig.dao.CatalogoDao;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.dao.EtiquetaDocuDao;
import gob.peam.arcdig.dao.MetaDataDao;
import gob.peam.arcdig.dao.NotificacionDao;
import gob.peam.arcdig.dao.PermisoDao;
import gob.peam.arcdig.dao.PersonaDao;
import gob.peam.arcdig.dao.PersonalDao;
import gob.peam.arcdig.dao.RepositorioDao;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tree;

import org.zkoss.zul.Window;

public class Inicio extends SelectorComposer<Component> {

    private EventQueue eq;
    private String ruta, rutaHtml, rutaSave;
    private List<Listitem> personalizado = new ArrayList<Listitem>();
    private List<Listitem> propietarios = new ArrayList<Listitem>();
    private List<Listitem> etiquetas = new ArrayList<Listitem>();
    private List<Listitem> compartidos = new ArrayList<Listitem>();
    private List<Listitem> correo = new ArrayList<Listitem>();
    private List<Metadata> metaData = (List<Metadata>) new ArrayList<Metadata>();
    private List<Metadata> metaData2 = (List<Metadata>) new ArrayList<Metadata>();
    private Notificacion notificacion = new Notificacion();
    private Integer selectedTipo;
    private Integer selectedCate;
    private Integer selectedSubTipo;
    private MineType mine;
    private Documento selectDocu;
    private Button privilegio;
    private Datebox fechaDocu;
    private Tree explorerTree;
    private String tidoTitulo;
    private String cateNombre, etiqNombre;
    Grid formularioTipo;
    gob.peam.arcdig.beans.Permiso perm = new gob.peam.arcdig.beans.Permiso();

    public String textTitulo = "", textResumen = "", textCodigo="";

    Listbox tipoDocu, cateDocu;
    @Wire
    Button addDocu;

    String regreso = "NO";
    Integer cateId, tidoId;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        try {
            String dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
            perm = new PermisoDao().getPermisos(dni);
            addDocu.setVisible(!perm.getItem1());
        } catch (NullPointerException ex) {
            addDocu.setVisible(false);
        }

        eq = EventQueues.lookup("beforeSubir", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                metaData = (List<Metadata>) hm.get("metaData");
                metaData2 = (List<Metadata>) hm.get("metaData2");
                formularioTipo = (Grid) hm.get("formularioTipo");
                tidoTitulo = (String) hm.get("tidoTitulo");
                selectedTipo = (Integer) hm.get("tipoDocu");
                selectedCate = (Integer) hm.get("cateDocu");
                fechaDocu = (Datebox) hm.get("fechaDocu");
                personalizado = (List<Listitem>) hm.get("personalizado");
                correo = (List<Listitem>) hm.get("correo");
                notificacion = (Notificacion) hm.get("notificacion");
                privilegio = (Button) hm.get("privilegio");
                iconPdf();
            }
        });

        eq = EventQueues.lookup("beforeEtiqueta", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                etiquetas = (List<Listitem>) hm.get("etiquetas");
                cateId = (Integer) hm.get("cateId");
                tidoId = (Integer) hm.get("tidoId");
                regreso = "SI";
                addDocu();
            }
        });

        eq = EventQueues.lookup("beforeRepositorio", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                rutaSave = (String) hm.get("rutaSave");
                if (perm.getItem8()) {
                    addDocu();
                } else {
                    etiquetar();
                }
            }
        });

        //Success boton Correcto Formulario WinUpload
        eq = EventQueues.lookup("dataSubida", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                metaData = (List<Metadata>) hm.get("metaData");
                metaData2 = (List<Metadata>) hm.get("metaData2");
                formularioTipo = (Grid) hm.get("formularioTipo");
                tidoTitulo = (String) hm.get("tidoTitulo");
                selectedTipo = (Integer) hm.get("tipoDocu");
                selectedCate = (Integer) hm.get("cateDocu");
                cateNombre = (String) hm.get("cateNombre");
                fechaDocu = (Datebox) hm.get("fechaDocu");
                personalizado = (List<Listitem>) hm.get("personalizado");
                correo = (List<Listitem>) hm.get("correo");
                privilegio = (Button) hm.get("privilegio");
                notificacion = (Notificacion) hm.get("notificacion");

                dataSubida(hm);
                if (perm.getItem8()) {
                    publicar();
                } else {
                    etiquetar();
                }

            }
        });

        eq = EventQueues.lookup("dataEtiqueta", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                etiquetas = (List<Listitem>) hm.get("etiquetas");
                etiqNombre = (String) hm.get("etiqNombre");
                publicar();
            }
        });

        eq = EventQueues.lookup("dataPropiedad", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                propietarios = (List<Listitem>) hm.get("personas");
            }
        });

        eq = EventQueues.lookup("dataCompartidos", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                compartidos = (List<Listitem>) hm.get("compartidos");

            }
        });

        eq = EventQueues.lookup("dataRepositorio", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                explorerTree = (Tree) hm.get("explorerTree");
                rutaSave = (String) hm.get("rutaSave");
                save();
            }
        });

        eq = EventQueues.lookup("cancelar", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                iconBorrar();
            }
        });

        eq = EventQueues.lookup("dataEditar", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                selectDocu = (Documento) hm.get("bean");
                editar();
            }
        });

        eq = EventQueues.lookup("dataMetaData", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                metaData = (List<Metadata>) hm.get("metaData");
                selectedTipo = (Integer) hm.get("tipoDocu");
                selectedCate = (Integer) hm.get("cateDocu");
                fechaDocu = (Datebox) hm.get("fechaDocu");
                personalizado = (List<Listitem>) hm.get("personas");
                privilegio = (Button) hm.get("privilegio");
                if (perm.getItem8()) {
                } else {
                    etiquetar();
                }
            }
        });
    }

    @Listen("onClick = #compartir")
    public void compartido() {
        HashMap hm = new HashMap();
        hm.put("compartidos", compartidos);
        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winPersonal.zul", null, hm);
        window1.doModal();
    }

    @Listen("onClick = #propiedad")
    public void propietario() {
        HashMap hm = new HashMap();
        hm.put("personas", propietarios);
        hm.put("propiedad", true);
        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winPersona.zul", null, hm);
        window1.doModal();
    }

    @Listen("onClick = #etiquetar")
    public void etiquetar() {
        HashMap hm = new HashMap();
        hm.put("etiquetas", etiquetas);
        hm.put("ruta", rutaHtml);
        hm.put("modificar", false);
        hm.put("tido_id", selectedTipo);
        hm.put("cate_id", selectedCate);
        hm.put("instance", "upload");
        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winEtiqueta.zul", null, hm);
        window1.doModal();
    }

    @Listen("onClick=#addDocu")
    public void addDocu() {
        HashMap hm = new HashMap();
        hm.put("titulo", textTitulo);
        hm.put("codigo", textCodigo);
        hm.put("resumen", textResumen);
        hm.put("metaData", metaData);
        hm.put("metaData2", metaData2);
        hm.put("formularioTipo", formularioTipo);
        hm.put("tipoDocu", selectedTipo == null ? 0 : selectedTipo);
        hm.put("cateDocu", selectedCate == null ? 0 : selectedCate);
         hm.put("subTipo", selectedSubTipo == null ? 0 : selectedSubTipo);
        hm.put("ruta", rutaHtml);
        hm.put("rutaCompleta", ruta);
        hm.put("mine", mine);
        hm.put("regreso", regreso);
        hm.put("cateId", cateId);
        hm.put("tidoId", tidoId);
        Window window = (Window) Executions.createComponents("/resources/zkWindow/winUpload.zul", null, hm);
        window.doModal();
    }

    @Listen("onClick=#iconBorrar")
    public void iconBorrar() {
        try {
            textTitulo = "";
            textResumen = "";
            textCodigo = "";
            addDocu.setDisabled(false);
            File file = new File(ruta);
            file.delete();
            ruta = null;
            rutaHtml = null;
            selectedCate = 0;
            selectedTipo = 0;
            fechaDocu = null;
            privilegio = null;
            metaData.clear();
            regreso = "NO";
        } catch (NullPointerException ex) {
        }
    }

    @Listen("onClick=#iconPdf")
    public void iconPdf() throws Exception {
        try {
            HashMap hm = new HashMap();
            hm.put("titulo", textTitulo);
            hm.put("resumen", textResumen);
            hm.put("codigo", textCodigo);
            hm.put("metaData", metaData);
            hm.put("metaData2", metaData2);
            hm.put("tipoDocu", selectedTipo == null ? 0 : selectedTipo);
            hm.put("cateDocu", selectedCate == null ? 0 : selectedCate);
            hm.put("subTipo", selectedSubTipo == null ? 0 : selectedSubTipo);
            hm.put("ruta", rutaHtml);
            hm.put("rutaCompleta", ruta);
            hm.put("mine", mine);
            Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winUpload.zul", null, hm);
            window2.doModal();
        } catch (NullPointerException ex) {
        }
    }

    @Listen("onClick=#iconVer")
    public void iconVer() throws Exception {
        try {
            HashMap hm = new HashMap();
            hm.put("ruta", rutaHtml);
            hm.put("titulo", textTitulo);
            hm.put("resumen", textResumen);
            hm.put("codigo", textCodigo);
            Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winUpload.zul", null, hm);
            window1.doModal();
        } catch (NullPointerException ex) {

        }
    }

    @Listen("onClick = #publicar")
    public void publicar() {

        boolean flag = true;
        if ("z-icon-cog".equals(privilegio.getIconSclass())) {
            if (personalizado.isEmpty()) {
                flag = false;
            }
        }
        HashMap hm = new HashMap();
        hm.put("cateNombre", cateNombre);
        hm.put("etiqNombre", etiqNombre);
        hm.put("explorerTree", explorerTree);
        Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winRepositorio.zul", null, hm);
        window2.doModal();

    }

    public void dataSubida(HashMap hm) {
        textTitulo = hm.get("titulo").toString();
        textResumen = hm.get("resumen").toString();
        textCodigo = hm.get("codigo").toString();
        ruta = hm.get("ruta").toString();
        rutaHtml = hm.get("rutaHtml").toString();
        mine = (MineType) hm.get("mine");

    }

    public void save() throws IOException {

        Documento bean = new Documento();
        if ("z-icon-globe".equals(privilegio.getIconSclass())) {
            bean.setPublico(true);
            bean.setPrivado(false);
            bean.setPersonalizado(false);
        } else if ("z-icon-user".equals(privilegio.getIconSclass())) {
            bean.setPrivado(true);
            bean.setPublico(false);
            bean.setPersonalizado(false);
        } else {
            bean.setPrivado(false);
            bean.setPublico(false);
            bean.setPersonalizado(true);
        }

        String metaHTML = "";
        bean.setTidoId(selectedTipo);
        bean.setSubTipo(selectedSubTipo);
        
        String itemMeta2 = "";
        if (!metaData2.isEmpty()) {
            TipoDocumento td = new TipoDocumento();
            td.setNombre(tidoTitulo);
            td.setPrivacidad(2);
            td.setDescripcion("");
            td.setEstado(true);
            new TipoDocumentoDao().insert(td);
            td.setId(new TipoDocumentoDao().getMaxId());
            bean.setTidoId(td.getId());
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

                    try {
                        if (metaData2.get(j).getManual()) {
                            Metadata m = new Metadata();
                            m.setCampo(metaData2.get(j).getCampo().toUpperCase());
                            m.setEstado(true);
                            m.setManual(true);
                            new MetaDataDao().insert(m);
                            metaData2.get(j).setCampo(new MetaDataDao().getMaxId() + "");
                        }
                    } catch (NullPointerException ex) {
                    }
                    TipoMetadata tm = new TipoMetadata(bean.getTidoId(), Integer.parseInt(metaData2.get(j).getCampo()), true, 1, tipo, oblig, "", metaData2.get(j).getSecuencia());
                    new MetaDataDao().insertTipoMeta(tm);
                }
            }

            for (Metadata meta : metaData2) {
                if (!"".equals(meta.getCampo())) {
                    itemMeta2 += "<docu_metadata><meta_id>" + meta.getCampo() + "</meta_id><meta_nombre>" + new MetaDataDao().getCampo(meta.getCampo()) + "</meta_nombre><meta_descripcion>" + meta.getDetalle() + "</meta_descripcion></docu_metadata>";
                    metaHTML += "<tr style='border-bottom: 1px solid black;'><td colspan='1'>" + new MetaDataDao().getCampo(meta.getCampo()) + ":</td><td colspan='3'>" + meta.getDetalle() + "</td></tr>";
                }
            }

        }

        if (!metaData.isEmpty()) {
            String itemMeta = "<metadata>";
            itemMeta += itemMeta2;
            for (Metadata meta : metaData) {
                itemMeta += "<docu_metadata><meta_id>" + meta.getId() + "</meta_id><meta_nombre>" + meta.getCampo() + "</meta_nombre><meta_descripcion>" + meta.getDetalle() + "</meta_descripcion></docu_metadata>";
                metaHTML += "<tr style='border-bottom: 1px solid black;'><td colspan='1'>" + meta.getCampo() + ":</td><td colspan='3'>" + meta.getDetalle() + "</td></tr>";
            }
            itemMeta += "</metadata>";
            bean.setMetaData(itemMeta);
        } else if (!metaData2.isEmpty()) {
            String itemMeta = "<metadata>" + itemMeta2 + "</metadata>";
            bean.setMetaData(itemMeta);
        }

        bean.setTitulo(textTitulo.toUpperCase().trim());
        bean.setTituloMin(textTitulo.trim());
        bean.setResumen(textResumen.toUpperCase().trim());
        bean.setResumenMin(textResumen.trim());
        bean.setDocuCodigo(textCodigo.trim());

        bean.setFechaDocx(new FormatoFecha().formatFechaCorta(fechaDocu.getValue()));

        bean.setMimeTypes(mine.getMineNombre());

        bean.setUsuaId((Integer) Executions.getCurrent().getDesktop().getSession().getAttribute("idUsuario"));

        try {
            bean.setCataId(new CatalogoDao().getIdCatalogo(rutaSave.split("/")[0]));
        } catch (NullPointerException ex) {

        }
        bean.setEstado(true);

        insertarArchivo();
        bean.setRuta("//" + rutaSave.replace("/", "//"));
        bean.setCateId(selectedCate);
        bean.setPropietario((String) Executions.getCurrent().getDesktop().getSession().getAttribute("dni"));
        int idMax = new DocumentoDao().getIdMaxDocumento() + 1;
        Integer max = new DocumentoDao().insert(bean, idMax);
        RutaRapida url = new RutaRapida();
        url.setRuta(rutaSave);
        url.setCataId(bean.getCataId());
        url.setDni(bean.getPropietario());

        new RepositorioDao().insert(url);
        for (Listitem eti : etiquetas) {
            DocumentoEtiqueta de = new DocumentoEtiqueta();
            de.setDocuId(max);
            de.setEtiqId(Integer.parseInt(eti.getId()));
            new EtiquetaDocuDao().insert(de);
        }

        DocumentoUsuario du = new DocumentoUsuario();
        if (bean.getPersonalizado()) {
            for (Listitem per : personalizado) {
                Persona p = new PersonaDao().get(per.getId());
                du.setDni(p.getDni());
                du.setDocuId(max);
                new DocumentoDao().insertPersonaDocumento(du);
            }
        }

        /*
         * * envio de correo
         */
        String cc = "";
        for (Listitem item : correo) {
            Persona p = new PersonaDao().get(item.getId());
            String c = new PersonalDao().getCorreo(p.getDni());
            if (!c.equals("")) {
                cc += new PersonalDao().getCorreo(p.getDni()) + ", ";
            }
            notificacion.setDocuId(max);
            notificacion.setPersDni(p.getDni());
            new NotificacionDao().insert(notificacion);

        }
        if (!cc.equals("")) {
            cc = cc.substring(0, cc.length() - 2);
        }
        if (!correo.isEmpty()) {
            String host = new ConexionReporte().obtenerCarpeta("host_correo");
            String dominio = new ConexionReporte().obtenerCarpeta("dominio");
            String direccion = new ConexionReporte().obtenerCarpeta("direccion");
            //Correo c = "noresponder@regionsanmartin.gob.pe";
            String to = "admin@" + dominio;
            String from = "noresponder@" + dominio;
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            Session mailSession = Session.getDefaultInstance(properties);
            String msn1 = "<table border='1' collspacing='0' cellspacing ='0' id='tableta' style=\"width:100%; border:1px solid black; margin:20px auto 0px auto\">"
                    + "<tr style='border-bottom: 1px solid black;'><th colspan='4' style='font-weight: bold; text-align:center; padding:5px;'>Notificaci&#243;n Electr&#243;nica del Sistema de Gesti&#243;n de Archivo Digitales</th></tr>"
                    + "<tr style='border-bottom: 1px solid black;'><td>Fecha de Registro:</td><td>" + bean.getFechaDocx() + "</td><td>N&#176; de Registro:</td><td>" + bean.getId() + "</td></tr>"
                    + "<tr style='border-bottom: 1px solid black;'><td colspan='1'>Documento:</td><td colspan='3'>" + new TipoDocumentoDao().get(bean.getTidoId()).getNombre() + "</td></tr>"
                    + "<tr style='border-bottom: 1px solid black;'><td colspan='1'>Titulo:</td><td colspan='3'>" + bean.getTitulo() + "</td></tr>"
                    + "<tr style='border-bottom: 1px solid black;'><td colspan='1'>Resumen:</td><td colspan='3'>" + bean.getResumen() + "</td></tr>"
                    + metaHTML
                    + "</table>"
                    + "<h2 style='font-size:10pt; color: blue; width:100%; text-align:center'>  <a href='" + direccion + "/ArcDig/ArcDig.pdf?id=" + bean.getId() + "&format=pdf' target='_blank'>Imprimir Aqu&#237;</a></h2>";
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
                message.setSubject(bean.getTitulo());
                Multipart mp = new MimeMultipart();

                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(msn1, "text/html");
                mp.addBodyPart(htmlPart);

                message.setContent(mp);
                Transport.send(message);
                //result = "Se Envió el mensaje correctamente";
            } catch (MessagingException mex) {
                showNotify("Error: Al enviar el correo al destinatario...." + cc, win, "warning", "top_center");
            }
        }

        HashMap hm = new HashMap();
        hm.put("limit", etiquetas.isEmpty() ? 1 : etiquetas.size());
        hm.put("offset", 0);
        hm.put("date", new Date(new Date().getTime()));
        Documento xbean = new DocumentoDao().getLastOne(hm);

        HashMap hmx = new HashMap();
        hmx.put("Item", xbean);
        //showNotify(xbean.getEtiquetaDocu().size()+"", win, "info", "top_center");
        eq = EventQueues.lookup("dataPublicar", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, hmx));
        iconBorrar();
    }

    public void insertarArchivo() throws IOException {
        Execution exec = Executions.getCurrent();

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        String SAVE_PATH = new ConexionReporte().obtenerCarpeta("carpeta");
        if (ruta != null) {
            File file = new File(ruta);
            try {
                try {
                    //guardar el archivo del temporal a los repositorios del ArcDig.
                    String rootPath;

                    String rutaServerAux = null;
                    InputStream fis = new FileInputStream(file);
                    in = new BufferedInputStream(fis);

                    String rootPathOri = SAVE_PATH;

                    rutaServerAux = rootPathOri + "/" + rutaSave;

                    File theDir = new File(rutaServerAux);

                    if (!theDir.exists()) {
                        //theDir.mkdirs();
                    }

                    int id = new DocumentoDao().getIdMaxDocumento() + 1;

                    File file2 = new File(rutaServerAux + "//" + id + "." + mine.getMineExt());

                    if (file2.isFile()) {
                        file2.delete();
                    }

                    OutputStream fout = new FileOutputStream(file2);
                    out = new BufferedOutputStream(fout);

                    byte buffer[] = new byte[245760];
                    int ch = in.read(buffer);

                    while (ch != -1) {
                        out.write(buffer, 0, ch);
                        ch = in.read(buffer);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
        }
    }


    public void editar() {
        //textTitulo.setFocus(true);
    }

    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }
}
