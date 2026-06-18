package gob.peam.arcdig.view;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.Repositorio;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.dao.RepositorioDao;
import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

public class WinChooseCatalogo extends SelectorComposer<Component> {

    private static final long serialVersionUID = 8243942703081449079L;
    private List<Repositorio> repo = (List<Repositorio>) new ArrayList<Repositorio>();
    String root = "";
    String rutaHtml = "";
    public Integer tamanio;
    public Boolean primera = true;
    private int indice = 0;
    public String path;

    private EventQueue eq;

    @Wire
    public Tree explorerTree;
    @Wire
    Window winRepositorio;
    @Wire
    Button ok;
    @Wire
    Treecol rutaVista;
    @Wire
    Listbox archivos;
    @Wire
    Textbox filtro;
    @Wire
    Button newCarpeta;

    @Listen("onClick = #newCarpeta")
    public void newCarpeta() {
        HashMap hm = new HashMap();
        hm.put("carpeta", "");
        Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winCarpeta.zul", null, hm);
        window2.doModal();
    }

    @Listen("onClick = #deleteCarpeta")
    public void deleteCarpeta() {
        if (!"./".equals(rutaVista.getLabel())) {

            final File directorio = new File(path+"/" + rutaVista.getLabel());  //directorio actual
            String[] lista = directorio.list();
            if (lista.length > 0) {
                showNotify("Tenga cuidado al borrar carpetas que contengan información, Esto no se puede borrar!", win, "info", "top_center");
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
                                    dibujarLista();
                                }
                            }
                        });

            }

        } else {
            showNotify("Seleccione una carpeta vacía para borrar", win, "info", "top_center");
        }
    }

    public String getRoot() {
        return root;
    }

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        path = new ConexionReporte().obtenerCarpeta("carpeta");
        eq = EventQueues.lookup("refresh", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                dibujarLista();
            }
        });
        rutaVista.setLabel("./");
        dibujarLista();       
    }

    public void dibujarLista() {
        try {
            root = path;
            tamanio = root.length();
            explorerTree.setModel(new ExploerTreeModel(new File(root)));
            explorerTree.setItemRenderer(new ExplorerTreeRenderer());
            explorerTree.addEventListener(Events.ON_SELECT, new DirectorySelectListener());

        } catch (NullPointerException ex) {

        }
    }

    public List<Repositorio> getCarpetas(Integer nivel, Integer padre) {
        HashMap hm = new HashMap();
        hm.put("nivel", nivel);
        hm.put("padre", padre);
        return new RepositorioDao().listarCarpeta(hm);
    }

    class ExploerTreeModel extends AbstractTreeModel {

        public ExploerTreeModel(File root) {
            super(root);
        }

        public Object getChild(Object parent, int index) {
            File fs[] = ((File) parent).listFiles(new DirectoryOnlyFilter());
            return fs == null ? null : fs[index];
        }

        public int getChildCount(Object parent) {
            File fs[] = ((File) parent).listFiles(new DirectoryOnlyFilter());
            return fs == null ? 0 : fs.length;
        }

        public boolean isLeaf(Object node) {
            return getChildCount(node) == 0;
        }
    }

    class ExplorerTreeRenderer implements TreeitemRenderer {

        public void render(Treeitem item, Object data, int i) throws Exception {

            File f = (File) data;         
            Treerow row = new Treerow();
            row.setParent(item);
            Treecell cell1 = new Treecell(f.getName(), "/resources/img/folder.png");
            File fs[] = f.listFiles(new FileOnlyFilter());
            Treecell cell2 = new Treecell(Integer.toString((fs == null ? 0 : fs.length)));
            cell1.setParent(row);
            cell2.setParent(row);
            item.setValue(data);
            //}
            primera = false;
        }
    }

    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    class ExploerBoxRenderer implements ListitemRenderer {

        public void render(Listitem item, Object data, int i) throws Exception {
            File f = (File) data;
            new Listcell(f.getName()).setParent(item);
            new Listcell(Long.toString(f.length())).setParent(item);
            new Listcell(format.format(f.lastModified())).setParent(item);
            Listcell actCell = new Listcell();
            Button btn = new Button("Descargar");
            btn.setParent(actCell);
            btn.addEventListener(Events.ON_CLICK, new FileDownloadListener(f));

            actCell.setParent(item);
        }
    }

    class FileDownloadListener implements EventListener {

        File file;

        public FileDownloadListener(File file) {
            this.file = file;
        }

        public void onEvent(Event event) throws Exception {
            Filedownload.save(file, null);
        }
    }

    class DirectorySelectListener implements EventListener {
        
        public void onEvent(Event event) throws Exception {
            File f = (File) explorerTree.getSelectedItem().getValue();
            String pathx = f.getAbsolutePath().substring(tamanio).replace("\\", "/");
            if ("/".equals(pathx.substring(0, 1))) {
                pathx = pathx.substring(1);
            }
            File[] fs = f.listFiles(new FileOnlyFilter());
            rutaVista.setLabel(pathx);
            try {

                int len = pathx.split("/").length;
                if (len <= 1) {
                    ok.setDisabled(false);
                } else {
                    ok.setDisabled(true);
                }

            } catch (NullPointerException ex) {
                ok.setDisabled(true);
            }
        }
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

    @Listen("onClick = #before")
    public void before() {
        HashMap hm = new HashMap();
        hm.put("rutaSave", rutaVista.getLabel());
        eq = EventQueues.lookup("beforeRepositorio", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, hm));
        winRepositorio.detach();
    }

    @Listen("onClick = #ok")
    public void ok() {
        HashMap hm = new HashMap();
        hm.put("rutaSave", rutaVista.getLabel());
        //hm.put("explorerTree", explorerTree);
        eq = EventQueues.lookup("dataCatalogo", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, hm));
        winRepositorio.detach();
    }

    @Listen("onClick = #cancel")
    public void cancel() {
        winRepositorio.detach();
    }

    @Listen("onChange = #filtro")
    public void filtrar() {
        List<Documento> items = (List<Documento>) new ArrayList<Documento>();
        HashMap hm = new HashMap();
        hm.put("filtro", "%" + filtro.getValue().toString() + "%");
        hm.put("ruta", rutaVista.getLabel().toString());
        items = new DocumentoDao().listarArchivos(hm);
        dibujarArchivos(items);
    }

    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

    public void dibujarArchivos(List<Documento> items) {
        archivos.getItems().clear();
        for (Documento item : items) {
            Listitem listitem = new Listitem();
            Listcell icon = new Listcell();
            icon.appendChild(new Label(""));
            Image img = new Image();
            img.setSrc("./resources/img/" + item.getMineType().getMineExt() + ".png");
            img.setStyle("border:0px");
            icon.appendChild(img);

            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(String.valueOf(item.getTitulo())));

            listitem.appendChild(icon);
            listitem.appendChild(nombre);

            archivos.appendChild(listitem);

        }
    }

}
