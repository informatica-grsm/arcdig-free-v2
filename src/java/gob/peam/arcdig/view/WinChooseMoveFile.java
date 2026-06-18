/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Catalogo;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.Repositorio;
import gob.peam.arcdig.dao.CatalogoDao;
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
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

/**
 *
 * @author Cj.Legacy
 */
public class WinChooseMoveFile extends SelectorComposer<Component> {

    private static final long serialVersionUID = 8243942703081449079L;
    private List<Repositorio> repo = (List<Repositorio>) new ArrayList<Repositorio>();
    String root = "";
    String rutaHtml = "";
    public Integer tamanio;
    public Boolean primera = true;
    private int indice = 0;

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
    public String path;

    public String getRoot() {
        return root;
    }

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        path = new ConexionReporte().obtenerCarpeta("carpeta");
        //if (exec.getArg().get("explorerTree") != null) {
        //    explorerTree = (Tree) exec.getArg().get("explorerTree");
        //} else {
        dibujarLista();
        //}
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

            List<Catalogo> catalogos = (List<Catalogo>) new ArrayList<Catalogo>();
            boolean mostrar = false;
            File f = (File) data;
            if (primera) {
                catalogos = new CatalogoDao().listarUsuarioCatalogo((Integer) Executions.getCurrent().getDesktop().getSession().getAttribute("idPersona"));
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
                //showNotify(f.getParent()+"", win, "info", "top_center");
                Treecell cell1 = new Treecell(f.getName(), "/resources/img/folder.png");
                File fs[] = f.listFiles(new WinChooseMoveFile.FileOnlyFilter());
                Treecell cell2 = new Treecell(Integer.toString((fs == null ? 0 : fs.length)));
                cell1.setParent(row);
                cell2.setParent(row);
                item.setValue(data);
            }
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
            String path = f.getAbsolutePath().substring(tamanio).replace("\\", "/");
            if ("/".equals(path.substring(0, 1))) {
                path = path.substring(1);
            }
            //File[] fs = f.listFiles(new FileOnlyFilter());
            /*List<Documento> items = (List<Documento>) new ArrayList<Documento>();
             HashMap hm = new HashMap();
             hm.put("filtro", "%%");
             hm.put("ruta", path+"/" + path);*/
            //items = new DocumentoDao().listarArchivos(hm);
            // dibujarArchivos(items);
            //explorerBox.setModel(new ListModelList(fs));
            rutaVista.setLabel(path);

            try {

                int len = path.split("/").length;
                if (len <= 1) {
                    ok.setDisabled(true);
                } else {
                    ok.setDisabled(false);
                }

            } catch (NullPointerException ex) {
                ok.setDisabled(false);
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

    @Listen("onClick = #ok")
    public void ok() {
        HashMap hm = new HashMap();
        hm.put("rutaSave", rutaVista.getLabel());
        eq = EventQueues.lookup("dataMove", EventQueues.DESKTOP, false);
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
