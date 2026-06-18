package gob.peam.arcdig.view;

import com.qoppa.pdf.PDFException;
import com.qoppa.pdfSecure.PDFSecure;
import config.ConexionReporte;
import gob.peam.arcdig.beans.Catalogo;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.DocumentoEtiqueta;
import gob.peam.arcdig.beans.DocumentoUsuario;
import gob.peam.arcdig.beans.Metadata;
import gob.peam.arcdig.beans.MineType;
import gob.peam.arcdig.beans.Notificacion;
import gob.peam.arcdig.beans.Persona;
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
import gob.peam.arcdig.dao.TipoDocumentoDao;
import gob.peam.arcdig.utils.FormatoFecha;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

public class Repositorio extends SelectorComposer<Component> {

    @Wire
    public Tree explorerTree;
    String root = "";
    public Integer tamanio;
    public Boolean primera = true;
    private Integer selectedTipo;
    private Integer selectedCate;
    public String textTitulo = "", textResumen = "", textCodigo = "";
    private List<Metadata> metaData = (List<Metadata>) new ArrayList<Metadata>();
    private List<Metadata> metaData2 = (List<Metadata>) new ArrayList<Metadata>();
    List<Catalogo> catalogos = (List<Catalogo>) new ArrayList<Catalogo>();
    private String ruta, rutaHtml, rutaSave;
    private List<Listitem> personalizado = new ArrayList<Listitem>();
    private List<Listitem> etiquetas = new ArrayList<Listitem>();
    private List<Listitem> correo = new ArrayList<Listitem>();
    public List<Catalogo> catas = (List<Catalogo>) new ArrayList<Catalogo>();
    List<Documento> documentos = (List<Documento>) new ArrayList<Documento>();
    private Notificacion notificacion = new Notificacion();
    private Button privilegio;
    private Datebox fechaDocu;
    private MineType mine;
    private Boolean tres, cuatro, cinco, seis, siete, primer, ocho, diez;
    private String tidoTitulo;
    private Boolean seleccion = false;
    public String legajo = "";
    private String cateNombre, etiqNombre;

    String q = "";
    String path;
    @Wire
    Treecol rutaVista;
    @Wire
    Bandbox filtro;
    Integer asociado = 0;
    @Wire
    Listbox archivos;
    boolean invitado;

    @Wire
    Button opcion1, opcion2;

    @Wire
    Menuitem oriFile, editFile, deleteFile, newFile, moveFile, deleteCarpeta;
    private EventQueue eq;
    int mes_ = 0;
    String regreso = "NO";
    String path_real;
    Boolean permEti = false;
    @Wire
    Menuitem menuAdjuntar;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        gob.peam.arcdig.beans.Permiso perm = new gob.peam.arcdig.beans.Permiso();
        path = new ConexionReporte().obtenerCarpeta("carpeta");
        catalogos = new CatalogoDao().listarUsuarioCatalogo((Integer) Executions.getCurrent().getDesktop().getSession().getAttribute("idPersona"));
        String dni = "";

        //showNotify(path, win, "info", "top-center");
        try {
            dni = exec.getDesktop().getSession().getAttribute("dni").toString();
            perm = new PermisoDao().getPermisos(dni);
            tres = !perm.getItem3();
            primer = !perm.getItem1();
            cuatro = !perm.getItem4();
            cinco = !perm.getItem5();
            seis = !perm.getItem6();
            siete = !perm.getItem7();
            ocho = !perm.getItem8();
            diez = !perm.getItem10();
            permEti = perm.getItem8();
            if (perm.getTipo() == 1) {
                invitado = true;
            } else {
                invitado = false;
            }
        } catch (NullPointerException ex) {
            tres = false;
            cuatro = false;
            cinco = false;
            seis = false;
            siete = false;
            ocho = false;
            diez = false;
            invitado = false;
        }
        try {
            if (primer) {
                newFile.setVisible(true);
            } else {
                newFile.setVisible(false);
            }

            if (cuatro) {
                deleteCarpeta.setVisible(true);
                deleteFile.setVisible(true);
            } else {
                deleteCarpeta.setVisible(false);
                deleteFile.setVisible(false);
            }
            if (tres) {
                moveFile.setVisible(true);
                editFile.setVisible(true);
            } else {
                editFile.setVisible(false);
                moveFile.setVisible(false);
            }
            if (tres) {
                oriFile.setVisible(true);
            } else {
                oriFile.setVisible(false);
            }
            if (siete) {
                if (!seis) {
                    q = "and docu_privado = false";
                }
            } else {
                q = " and (propietario = '" + dni + "' or documento.docu_id in (select du.docu_id from documento_usuario du where du.pers_dni= '" + dni + "'))";
            }

            if (invitado) {
                opcion1.detach();
                opcion2.detach();
                menuAdjuntar.detach();
            }
        } catch (NullPointerException ex) {
        }
        dibujarLista();

        eq = EventQueues.lookup("cancelar", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                iconBorrar();
            }
        });
        eq = EventQueues.lookup("refresh", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                rutaVista.setLabel(rutaVista.getLabel() + event.getData().toString());
                actualizarCarpeta();
            }
        });

        eq = EventQueues.lookup("beforeSubir", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                metaData = (List<Metadata>) hm.get("metaData");
                metaData2 = (List<Metadata>) hm.get("metaData2");
                tidoTitulo = (String) hm.get("tidoTitulo");
                selectedTipo = (Integer) hm.get("tipoDocu");
                selectedCate = (Integer) hm.get("cateDocu");
                fechaDocu = (Datebox) hm.get("fechaDocu");
                legajo = (String) hm.get("legajo");
                personalizado = (List<Listitem>) hm.get("personalizado");
                correo = (List<Listitem>) hm.get("correo");
                privilegio = (Button) hm.get("privilegio");
                notificacion = (Notificacion) hm.get("notificacion");
                iconPdf();
            }
        });

        eq = EventQueues.lookup("beforeEtiqueta", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                regreso = "SI";
                etiquetas = (List<Listitem>) hm.get("etiquetas");
                addDocu();

            }
        });

        eq = EventQueues.lookup("beforeRepositorio", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                rutaSave = (String) hm.get("rutaSave");
                if (permEti) {
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
                tidoTitulo = (String) hm.get("tidoTitulo");
                selectedTipo = (Integer) hm.get("tipoDocu");
                selectedCate = (Integer) hm.get("cateDocu");
                fechaDocu = (Datebox) hm.get("fechaDocu");
                legajo = (String) hm.get("legajo");
                personalizado = (List<Listitem>) hm.get("personalizado");
                correo = (List<Listitem>) hm.get("correo");
                privilegio = (Button) hm.get("privilegio");
                notificacion = (Notificacion) hm.get("notificacion");
                rutaSave = rutaVista.getLabel().toString();
                cateNombre = (String) hm.get("cateNombre");
                dataSubida(hm);
                if (!ocho) {
                    if (asociado == 0) {
                        save();
                    } else {
                        publicar();
                    }

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
                if (asociado == 0) {
                    save();
                } else {
                    publicar();
                }
            }
        });

        eq = EventQueues.lookup("dataMove", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                moverArchivos(hm);

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

        eq = EventQueues.lookup("actualizar", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                List<Documento> items = (List<Documento>) new ArrayList<Documento>();
                //HashMap hml = new HashMap();
                query.put("filtro", "" + q);
                String rv = "//" + rutaVista.getLabel().replace("/", "//");
                query.put("ruta", rv);
                dibujarLista(query);
            }
        });
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

    @Listen("onClick = #updateCarpeta")
    public void actualizarCarpeta() {
        seleccion = true;
        explorerTree.setItemRenderer(new ExplorerTreeRenderer());
    }

    public void dibujarLista() throws IOException {
        try {
            root = path;
            tamanio = root.length();
            explorerTree.setModel(new ExploerTreeModel(new File(root)));
            explorerTree.setItemRenderer(new ExplorerTreeRenderer());
            //explorerTree.addEventListener(Events.ON_SELECT, new DirectorySelectListener());
        } catch (NullPointerException ex) {

        }
    }

    public void dataSubida(HashMap hm) {

        textTitulo = hm.get("titulo").toString();
        textResumen = hm.get("resumen").toString();
        textCodigo = hm.get("codigo").toString();

        ruta = hm.get("ruta").toString();
        rutaHtml = hm.get("rutaHtml").toString();
        mine = (MineType) hm.get("mine");
    }

    class ExploerTreeModel extends AbstractTreeModel {

        public ExploerTreeModel(File root) {
            super(root);
        }

        public Object getChild(Object parent, int index) {
            File fs[] = ((File) parent).listFiles(new Repositorio.DirectoryOnlyFilter());
            return fs == null ? null : fs[index];
            //return 0;
        }

        public int getChildCount(Object parent) {
            File fs[] = ((File) parent).listFiles(new Repositorio.DirectoryOnlyFilter());
            return fs == null ? 0 : fs.length;

        }

        public boolean isLeaf(Object node) {
            return getChildCount(node) == 0;
        }

    }

    class ExplorerTreeRenderer implements TreeitemRenderer {

        public void render(Treeitem item, Object data, int i) throws Exception {
            boolean mostrar = false;
            File f = (File) data;
            if (primera) {
                for (Catalogo cata : catalogos) {
                    if (f.getName().equals(cata.getCataCarpeta())) {
                        mostrar = true;
                        break;
                    } else {
                        mostrar = false;
                    }
                }
            }

            String src[] = f.getParent().replace("\\", "/").split("/");
            String carpeta = src[src.length - 1];
            String carpeta1 = "";
            try {
                String src1[] = path.replace("\\", "/").split("/");
                carpeta1 = src1[src1.length - 1];
            } catch (NullPointerException ex) {
                carpeta1 = path;
            }

            if (mostrar || !carpeta1.equals(carpeta)) {
                Treerow row = new Treerow();
                row.setParent(item);

                Treecell cell1 = new Treecell(f.getName(), "/resources/img/folder.png");
                //File fs[] = f.listFiles(new Repositorio.FileOnlyFilter());
                // File dir = new File(f.getPath());
                // int numberOfSubfolders = 0;
                //File listDir[] = dir.listFiles();
                // for (int x = 0; x < listDir.length; x++) {
                //    if (listDir[x].isDirectory()) {
                //        numberOfSubfolders++;
                //    }
                // }
                //Treecell cell2 = new Treecell(Integer.toString((fs == null ? 0 : numberOfSubfolders)));
                //Treecell cell3 = new Treecell(Integer.toString((fs == null ? 0 : fs.length)));
                cell1.setParent(row);
                //cell2.setParent(row);
                ///cell2.setStyle("text-align:center;");
                //cell3.setParent(row);
                //cell3.setStyle("text-align:center;");
                item.setValue(data);
                item.setOpen(false);
                if (seleccion) {
                    String path_ = (f.getPath());
                    if (path_.contains("\\")) {
                        path_ = path_.replace("\\", "/");
                    }

                    if (((path + "/" + rutaVista.getLabel())).equals(path_)) {
                        //showNotify(path + "/" + rutaVista.getLabel()+"path_:"+path_, win, "info", "top_center");
                        item.setSelected(true);
                        seleccion = false;
                        //item.setOpen(true);
                    }
                    //item.setOpen(true);

                } else {
                    //item.setOpen(false);
                }

//                item.setSelected(true);
            }
        }
    }

    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    /*class ExploerBoxRenderer implements ListitemRenderer {

        public void render(Listitem item, Object data, int i) throws Exception {
            File f = (File) data;
            new Listcell(f.getName()).setParent(item);
            new Listcell(Long.toString(f.length())).setParent(item);
            new Listcell(format.format(f.lastModified())).setParent(item);
            Listcell actCell = new Listcell();
            Button btn = new Button("Descargar");
            btn.setParent(actCell);
            //btn.addEventListener(Events.ON_CLICK, new Repositorio.FileDownloadListener(f));

            actCell.setParent(item);
        }
    }*/
    
    @Listen("onSelect = #explorerTree")
    public void DirectorySelect() {
          File f = (File) explorerTree.getSelectedItem().getValue();
            path_real = f.getAbsolutePath();
            String pathx = f.getAbsolutePath().substring(tamanio).replace("\\", "/");
            if ("/".equals(pathx.substring(0, 1))) {
                pathx = pathx.substring(1);
            }
                     
            query.put("filtro", "" + q);
            String rv = "//" + pathx.replace("/", "//");
            query.put("ruta", rv);
            dibujarLista(query);
            rutaVista.setLabel(pathx);
    }

    
    class DirectoryOnlyFilter implements FileFilter {

        public boolean accept(File f) {
            return f.isDirectory() && !f.isHidden();
        }
    }

    class FileOnlyFilter implements FileFilter {

        public boolean accept(File f) {
            return f.isFile() && !f.isHidden();
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

    @Listen("onClick = #newCarpeta")
    public void newCarpeta() {
        if (rutaVista.getLabel().equals("./")) {
            showNotify("Seleccione una Carpeta Raiz", win, "warning", "top_center");
        } else {
            HashMap hm = new HashMap();
            hm.put("carpeta", rutaVista.getLabel());
            Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winCarpeta.zul", null, hm);
            window2.doModal();
        }

    }
    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

    @Listen("onClick = #deleteCarpeta")
    public void deleteCarpeta() {
        if (rutaVista.getLabel().contains("/")) {
            if (!"./".equals(rutaVista.getLabel())) {
                final File directorio = new File(path + "/" + rutaVista.getLabel());  //directorio actual
                String[] lista = directorio.list();
                if (lista.length > 0) {
                    showNotify("Tenga cuidado al borrar carpetas que contengan información, Esto no se puede borrar", win, "info", "top_center");
                } else {
                    Messagebox.show(
                            "Esta Seguro de borrar esta carpeta?", "Dialogo de Confirmación ",
                            Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION,
                            new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event evt) throws Exception {
                            if (evt.getName().equals("onOK")) {
                                directorio.delete();
                                new CatalogoDao().deleteCarpeta(rutaVista.getLabel());
                                String rutaNueva = rutaVista.getLabel();
                                Integer tam = (rutaNueva.split("/")[rutaNueva.split("/").length - 1]).length();
                                rutaNueva = rutaNueva.substring(0, rutaNueva.length() - (tam + 1));
                                rutaVista.setLabel(rutaNueva);
                                actualizarCarpeta();
                            }
                        }
                    });
                }

            } else {
                showNotify("Seleccione una carpeta vacía para borrar", win, "info", "top_center");
            }
        } else {
            showNotify("No puede borrar los catalogos o carpetas raíces", win, "info", "top_center");
        }
    }

    @Listen("onClick = #editFile")
    public void editFile() {
        final Set<Listitem> items = archivos.getSelectedItems();
        if (items.isEmpty()) {
            showNotify("Selecccione un archivo para realizar esta acción", win, "info", "top_center");
        } else {
            for (Listitem item : items) {
                HashMap hm = new HashMap();
                hm.put("bean", item);
                Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winEditar.zul", null, hm);
                window1.doModal();
                break;
            }

        }
    }

    @Listen("onClick = #deleteFile")
    public void deleteFile() {
        final Set<Listitem> items = archivos.getSelectedItems();
        if (items.isEmpty()) {
            showNotify("Selecccione un archivo para realizar esta acción", win, "info", "top_center");
        } else {
            final String ruta = path + "//" + rutaVista.getLabel();
            Messagebox.show(
                    "Esta Seguro de borrar el/los archivos Seleccionado(s)?", "Dialogo de Confirmación ",
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event evt) throws Exception {
                    if (evt.getName().equals("onOK")) {
                        for (Listitem item : items) {
                            Integer id = Integer.parseInt((item.getId().split(";"))[0]);
                            new EtiquetaDocuDao().delete(id);

                            Documento bean = new Documento();
                            bean.setId(id);
                            new DocumentoDao().deletePropiedad(bean);
                            new DocumentoDao().delete(bean);
                            File f = null;
                            if (ruta.contains("\\")) {
                                f = new File(ruta + "\\" + item.getId().replace(";", "."));
                            } else {
                                f = new File(ruta + "//" + item.getId().replace(";", "."));
                            }
                            f.delete();
                            //actualizarCarpeta();
                        }
                        HashMap hm = new HashMap();
                        List<Documento> docs = (List<Documento>) new ArrayList<Documento>();
                        query.put("filtro", "");
                        String rv = "//" + rutaVista.getLabel().replace("/", "//");
                        query.put("ruta", rv);
                        dibujarLista(query);
                    }
                }
            });

        }

    }

    @Listen("onClick = #moveFile")
    public void moveFile() {
        final Set<Listitem> items = archivos.getSelectedItems();
        if (items.isEmpty()) {
            showNotify("Selecccione un archivo para realizar esta acción", win, "info", "top_center");
        } else {
            Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winChooseMoveFile.zul", null, null);
            window2.doModal();
        }
    }

    public void moverArchivos(HashMap hm) {
        Set<Listitem> items = archivos.getSelectedItems();
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        String rutaX = hm.get("rutaSave").toString();
        HashMap hmx = new HashMap();
        String dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
        try {
            hmx.put("cataId", new CatalogoDao().getIdCatalogo(rutaX.split("/")[0]));
        } catch (NullPointerException ex) {

        }
        for (Listitem item : items) {
            File f = null;
            try {
                String ruta = path + "//" + rutaVista.getLabel();
                f = new File(ruta + "//" + item.getId().replace(";", "."));
                InputStream fis = new FileInputStream(f);
                in = new BufferedInputStream(fis);

                File file = new File(path + "/" + rutaX + "/" + item.getId().replace(";", "."));

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

                    Integer id = Integer.parseInt((item.getId().split(";"))[0]);
                    hmx.put("id", id);
                    hmx.put("ruta", "//" + rutaX.replace("/", "//"));
                    hmx.put("dni", dni);
                    new DocumentoDao().updateRuta(hmx);
                    f.delete();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        List<Documento> docs = (List<Documento>) new ArrayList<Documento>();
        query.put("filtro", "" + q);
        String rv = "//" + rutaVista.getLabel().replace("/", "//");
        query.put("ruta", rv);
        dibujarLista(query);
    }

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
        query.put("filtro", c + q);
        String rv = "//" + rutaVista.getLabel().replace("/", "//");
        query.put("ruta", rv);
        dibujarLista(query);
    }

    @Listen("onClick = #viewFile")
    public void visor() {
        Set<Listitem> items = archivos.getSelectedItems();
        if (items.isEmpty()) {
            showNotify("Selecccione un archivo para realizar esta acción", win, "info", "top_center");
        } else {
            for (Listitem item : items) {
                HashMap hm = new HashMap();
                hm.put("original", false);
                hm.put("id", Integer.parseInt((item.getId().split(";"))[0]));
                Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winVisor.zul", null, hm);
                window1.doModal();
                break;
            }
        }
    }

    @Listen("onClick=#newFile")
    public void addDocu() {
        int len = rutaVista.getLabel().split("/").length;
        if (len > 1) {
            HashMap hm = new HashMap();
            hm.put("titulo", textTitulo);
            hm.put("codigo", textCodigo);
            hm.put("resumen", textResumen);
            hm.put("metaData", metaData);
            hm.put("personalizado", personalizado);
            hm.put("correo", correo);
            hm.put("metaData2", metaData2);
            hm.put("tipoDocu", selectedTipo == null ? 0 : selectedTipo);
            hm.put("cateDocu", selectedCate == null ? 0 : selectedCate);
            hm.put("ruta", rutaHtml);
            hm.put("rutaVista", rutaVista.getLabel());
            hm.put("rutaCompleta", ruta);
            Window window = (Window) Executions.createComponents("/resources/zkWindow/winUpload.zul", null, hm);
            window.doModal();
        } else {
            showNotify("No Puede subir Archivos Aqui", win, "info", "top_center");
        }
    }

    @Listen("onClick = #etiquetar")
    public void etiquetar() {
        HashMap hm = new HashMap();
        hm.put("etiquetas", etiquetas);
        hm.put("ruta", rutaHtml);
        hm.put("modificar", false);
        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winEtiqueta.zul", null, hm);
        window1.doModal();
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
        bean.setTidoId(selectedTipo);
        String itemMeta2 = "";
        String metaHTML = "";
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
        bean.setDocuCodigo(textCodigo.trim());
        bean.setTituloMin(textTitulo.trim());
        bean.setResumen(textResumen.toUpperCase().trim());
        bean.setResumenMin(textResumen.trim());

        bean.setFechaDocx(
                new FormatoFecha().formatFechaCorta(fechaDocu.getValue()));
        bean.setMimeTypes(mine.getMineNombre());
        bean.setUsuaId(
                (Integer) Executions.getCurrent().getDesktop().getSession().getAttribute("idUsuario"));

        try {
            bean.setCataId(new CatalogoDao().getIdCatalogo(rutaSave.split("/")[0]));
        } catch (NullPointerException ex) {

        }

        bean.setEstado(true);
        insertarArchivo();

        bean.setRuta("//" + rutaSave.replace("/", "//"));
        bean.setCateId(selectedCate);

        bean.setPropietario((String) Executions.getCurrent().getDesktop().getSession().getAttribute("dni"));
        int idMax = 0;
        if (asociado != 0) {
            idMax = asociado;
        } else {
            idMax = new DocumentoDao().getIdMaxDocumento() + 1;
        }

        new DocumentoDao().insert(bean, idMax);
        Integer max = new DocumentoDao().getIdMaxDocumento();

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

        if (!legajo.equals("")) {
            try {
                du.setDni(legajo);
                du.setDocuId(max);
                new DocumentoDao().insertPersonaDocumento(du);
            } catch (Exception ex) {
            }
        }

        HashMap hm = new HashMap();

        hm.put("limit", etiquetas.size());
        hm.put("offset", 0);
        hm.put("date", new Date(new Date().getTime()));
        Documento xbean = new DocumentoDao().getLastOne(hm);

        HashMap hmx = new HashMap();

        hmx.put("Item", xbean);
        //showNotify(xbean.getEtiquetaDocu().size()+"", win, "info", "top_center");
        //eq = EventQueues.lookup("dataPublicar", EventQueues.DESKTOP, false);
        //eq.publish(new Event("", null, hmx));
        iconBorrar();
        List<Documento> items = (List<Documento>) new ArrayList<Documento>();
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
        //HashMap hml = new HashMap();

        query.put("filtro", "" + q);
        String rv = "//" + rutaVista.getLabel().replace("/", "//");
       // actualizarCarpeta();
        query.put("ruta", rv);
        dibujarLista(query);
        asociado = 0;
    }

    public void insertarArchivo() {
        Execution exec = Executions.getCurrent();

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        String SAVE_PATH = path;
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

    @Listen("onClick=#iconBorrar")
    public void iconBorrar() {
        try {
            textTitulo = "";
            textResumen = "";
            textCodigo = "";
            File file = new File(ruta);
            file.delete();
            ruta = null;
            rutaHtml = null;
            selectedCate = 0;
            selectedTipo = 0;
            fechaDocu = null;
            privilegio = null;
            metaData.clear();
        } catch (NullPointerException ex) {
        }
    }

    @Listen("onClick=#iconPdf")
    public void iconPdf() throws Exception {
        try {
            HashMap hm = new HashMap();
            hm.put("titulo", textTitulo);
            hm.put("codigo", textCodigo);
            hm.put("resumen", textResumen);
            hm.put("ruta", rutaHtml);
            hm.put("rutaCompleta", ruta);
            Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winUpload.zul", null, hm);
            window2.doModal();
        } catch (NullPointerException ex) {
        }
    }

    @Listen("onClick = #oriFile")
    public void visorOri() {
        Set<Listitem> items = archivos.getSelectedItems();
        if (items.isEmpty()) {
            showNotify("Selecccione un archivo para realizar esta acción", win, "info", "top_center");
        } else {
            for (Listitem item : items) {
                HashMap hm = new HashMap();
                hm.put("original", true);
                hm.put("id", Integer.parseInt((item.getId().split(";"))[0]));
                Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winVisor.zul", null, hm);
                window1.doModal();
                break;
            }
        }
    }

    Integer cateId, tidoId;

    @Listen("onClick =#menuAdjuntar")
    public void menuAdjuntar() {

        final Set<Listitem> items = archivos.getSelectedItems();
        if (items.isEmpty()) {
            showNotify("Selecccione un archivo para realizar esta acción", win, "info", "top_center");
        } else if (items.size() > 1) {
            showNotify("Selecccione solo un archivo para realizar esta acción", win, "info", "top_center");
        } else {
            if (diez) {
                HashMap hm = new HashMap();
                hm.put("titulo", textTitulo);
                hm.put("codigo", textCodigo);
                hm.put("resumen", textResumen);
                hm.put("metaData", metaData);
                hm.put("metaData2", metaData2);
                //hm.put("formularioTipo", formularioTipo);
                hm.put("tipoDocu", selectedTipo == null ? 0 : selectedTipo);
                hm.put("cateDocu", selectedCate == null ? 0 : selectedCate);
                hm.put("ruta", rutaHtml);
                hm.put("rutaCompleta", ruta);
                hm.put("mine", mine);
                hm.put("regreso", regreso);

                Window window = (Window) Executions.createComponents("/resources/zkWindow/winUpload.zul", null, hm);
                window.doModal();
                asociado = selected.getId();
            } else {
                showNotify("No tiene privilegios para realizar esta acción", win, "info", "top_center");
            }
        }

    }

    @Listen("onClick = #update")
    public void refresh() {
        //List<Documento> items = (List<Documento>) new ArrayList<Documento>();
        //HashMap hml = new HashMap();
        query.put("filtro", "" + q);
        String rv = "//" + rutaVista.getLabel().replace("/", "//");
        query.put("ruta", rv);

        dibujarLista(query);
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

    @Listen("onClick = #menuVerFirma")
    public void pickFirma() throws IOException, PDFException {
        String path = new ConexionReporte().obtenerCarpeta("carpeta");

        if (verificarFirma(path + selected.getRuta() + "//" + selected.getId() + ".pdf")) {
            HashMap hm = new HashMap();
            hm.put("tmp", path + selected.getRuta() + "//" + selected.getId() + ".pdf");
            Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winFirma.zul", null, hm);
            window2.doModal();
        } else {
            showNotify("Este documento no contiene Firma digital", win, "warning", "top_center");
        }
    }

    public boolean verificarFirma(String ruta1) throws PDFException {
        try {
            PDFSecure pdfDoc = new PDFSecure(ruta1, null);
            if (pdfDoc.getSignatureFields() != null && pdfDoc.getSignatureFields().size() >= 1) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException ex) {
            return false;
        }
    }

    HashMap query = new HashMap();
    @Wire
    Paging pagingData;

    @Listen("onPaging = #pagingData")
    public void paginar() {
        int pgno = pagingData.getActivePage();
        int ofs = pgno * PAGE_SIZE;

        query.put("offset", ofs);
        query.put("limit", PAGE_SIZE);

        List<Documento> items = new DocumentoDao().listarArchivos(query);
        dibujarArchivos(items);

    }
    int PAGE_SIZE = 0;

    public void dibujarLista(final HashMap hm) {
        int total = new DocumentoDao().listarArchivosTotal(hm);

        PAGE_SIZE = pagingData.getPageSize();

        if (total > PAGE_SIZE) {
            pagingData.setVisible(true);
        } else {
            pagingData.setVisible(false);
        }
        pagingData.setTotalSize(total);
        pagingData.setPageSize(PAGE_SIZE);

        hm.put("offset", 0);
        hm.put("limit", PAGE_SIZE);
        List<Documento> items = new DocumentoDao().listarArchivos(hm);
        dibujarArchivos(items);
    }

}
