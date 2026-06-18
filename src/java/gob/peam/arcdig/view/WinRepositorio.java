package gob.peam.arcdig.view;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Catalogo;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.Repositorio;
import gob.peam.arcdig.beans.RutaRapida;
import gob.peam.arcdig.dao.CatalogoDao;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.dao.RepositorioDao;
import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

public class WinRepositorio extends SelectorComposer<Component> {

    private static final long serialVersionUID = 8243942703081449079L;
    private List<Repositorio> repo = (List<Repositorio>) new ArrayList<Repositorio>();
    List<RutaRapida> rutas = (List<RutaRapida>) new ArrayList<RutaRapida>();
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
    Listbox archivos, listTmpDir;
    @Wire
    Textbox filtro;

    String cateNombre, etiqNombre;
    public String path;

    public String getRoot() {
        return root;
    }

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        path = new ConexionReporte().obtenerCarpeta("carpeta");
        cateNombre = (String) exec.getArg().get("cateNombre");
        etiqNombre = (String) exec.getArg().get("etiqNombre");
        //showNotify(cateNombre + "/" + etiqNombre, win, "info", "top_center");
        dibujarLista();
        dibujarRutas();

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
           //return 0;
        }

        public int getChildCount(Object parent) {
            File fs[] = ((File) parent).listFiles(new DirectoryOnlyFilter());
            return fs == null ? 0 : fs.length;
            //return 0;
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
                Treecell cell1 = new Treecell(f.getName(), "/resources/img/folder.png");
                //File fs[] = f.listFiles(new FileOnlyFilter());
                //Treecell cell2 = new Treecell(Integer.toString((fs == null ? 0 : fs.length)));
                cell1.setParent(row);
               // cell2.setParent(row);
                item.setValue(data);
                if (f.getName().equals(cateNombre)) {
                    item.setOpen(true);
                }
                if (f.getName().equals(etiqNombre)) {
                    item.setSelected(true);
                    completar(f);
                }
            }
        }
    }

    public void completar(File f) {
        String pathF = f.getAbsolutePath().substring(tamanio).replace("\\", "/");
        if ("/".equals(pathF.substring(0, 1))) {
            pathF = pathF.substring(1);
        }
        File[] fsF = f.listFiles(new FileOnlyFilter());
        List<Documento> itemsF = (List<Documento>) new ArrayList<Documento>();
        HashMap hmF = new HashMap();
        hmF.put("filtro", "");
        String rR = "//" + pathF.replace("/", "//");
        hmF.put("ruta", rR);
        itemsF = new DocumentoDao().listarArchivos(hmF);
        dibujarArchivos(itemsF);
        rutaVista.setLabel(pathF);
        listTmpDir.clearSelection();
        try {
            int leng = pathF.split("/").length;
            if (leng > 1) {
                ok.setDisabled(false);
            } else {
                ok.setDisabled(true);
            }

        } catch (NullPointerException ex) {
            ok.setDisabled(true);
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
            File[] fs = f.listFiles(new FileOnlyFilter());
            List<Documento> items = (List<Documento>) new ArrayList<Documento>();
            //HashMap hm = new HashMap();
            query.put("filtro", "");
            String rv = "//" + path.replace("/", "//");
            query.put("ruta", rv);
            
            dibujarLista(query);
            //items = new DocumentoDao().listarArchivos(hm);
            //dibujarArchivos(items);
            
            
            
            rutaVista.setLabel(path);
            listTmpDir.clearSelection();
            try {

                int len = path.split("/").length;
                if (len > 1) {
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
        hm.put("explorerTree", explorerTree);
        eq = EventQueues.lookup("dataRepositorio", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, hm));
        winRepositorio.detach();
    }

    @Listen("onClick = #cancel")
    public void cancel() {
        eq = EventQueues.lookup("cancelar", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, null));
        winRepositorio.detach();
    }

    @Listen("onChange = #filtro")
    public void filtrar() {
        List<Documento> items = (List<Documento>) new ArrayList<Documento>();
        //HashMap hm = new HashMap();
        String vector = filtro.getValue().toString().trim().replace(" ", "%") + ":*";
        String c;
        if (filtro.getValue().toString().length() == 0) {
            c = "";
        } else {
            c = "and ( (to_tsquery('" + vector + "') @@ docu_search)) ";
        }
        query.put("filtro", c);
        String rv = "//" + rutaVista.getLabel().replace("/", "//");
        query.put("ruta", rv);
       // items = new DocumentoDao().listarArchivos(hm);
        //dibujarArchivos(items);
        query.put("filtro", "");
        
        //String rv = "//" + SelectRuta.getRuta().replace("/", "//");
        
        query.put("ruta", rv);
        dibujarLista(query);

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

    public void dibujarRutas() {
        try {

            String dni = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("dni");
            rutas = new RepositorioDao().listarRutas(dni);
            listTmpDir.getItems().clear();
            for (RutaRapida item : rutas) {
                Listitem listitem = new Listitem();
                Listcell check = new Listcell();
                check.appendChild(new Label(""));
                Listcell nombre = new Listcell();
                nombre.appendChild(new Label(item.getRuta()));

                listitem.appendChild(check);
                listitem.appendChild(nombre);

                listTmpDir.appendChild(listitem);

            }
        } catch (NullPointerException ex) {

        }
    }

    RutaRapida SelectRuta;

    @Listen("onSelect = #listTmpDir")
    public void selectRepo() {
        Set item = listTmpDir.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            SelectRuta = rutas.get(emp.get(i).getIndex());
        }
        rutaVista.setLabel(SelectRuta.getRuta());
        ok.setDisabled(false);
        query.put("filtro", "");
        String rv = "//" + SelectRuta.getRuta().replace("/", "//");
        query.put("ruta", rv);
        dibujarLista(query);
        explorerTree.clearSelection();

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
