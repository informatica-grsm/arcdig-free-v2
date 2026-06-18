package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Carpeta;
import gob.peam.arcdig.beans.Categoria;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.Metadata;
import gob.peam.arcdig.beans.MineType;
import gob.peam.arcdig.beans.Notificacion;
import gob.peam.arcdig.beans.Persona;
import gob.peam.arcdig.beans.SubTipo;
import gob.peam.arcdig.beans.TipoDocumento;
import gob.peam.arcdig.beans.TipoMetadata;
import gob.peam.arcdig.dao.CatalogoDao;
import gob.peam.arcdig.dao.CategoriaDao;
import gob.peam.arcdig.dao.MetaDataDao;
import gob.peam.arcdig.dao.MineTypeDao;
import gob.peam.arcdig.dao.PersonaDao;
import gob.peam.arcdig.dao.TipoDocumentoDao;
import gob.peam.arcdig.utils.FormatoFecha;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class WinUpload extends SelectorComposer<Component> {

    private static final long serialVersionUID = 8243942703081449079L;
    private List<Metadata> metaData = (List<Metadata>) new ArrayList<Metadata>();
    private List<Listitem> personalizado = new ArrayList<Listitem>();

    Notificacion notificacion = new Notificacion();

    private static final String SAVE_TEMP_PATH = "/tmp";
    private File file;
    private String ruta;
    private String rutaHtml;
    private String mimeTypes;
    public String formatType;
    public String documento;
    public String legajo = "";
    private EventQueue eq, eq1, eq2, eq3;
    private Documento selected;
    private MineType mine;
    List<Listitem> correo = new ArrayList<Listitem>();

    @Wire
    Listbox tipoDocu, cateDocu;
    @Wire
    Button privilegio;
    @Wire
    Datebox fechaDocu;

    public Documento getSelected() {
        return selected;
    }

    public void setSelected(Documento selected) {
        this.selected = selected;
    }
    @Wire
    Iframe viewFrame;
    @Wire
    Window win;
    @Wire
    Label mensaje, tituError, metaNombre, metaDNI;
    @Wire
    Button save, close, up;
    @Wire
    Grid formularioTipo, gridY;
    @Wire
    Rows rowsY;
    @Wire
    Div gridContent;

    String rutaVista = "";
    Integer categ_id, tido_id;

    public WinUpload() {
    }

    private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "warning", ref, "middle_center", 3000);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();

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
                //showNotify("hola", win);
                if (!correo.isEmpty()) {
                    Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winExpiraEmail.zul", null, null);
                    window1.doModal();
                }
            }
        });

        Persona empleado;

        String rutaTmp = null;
        String regreso = null;
        try {
            metaData = (List<Metadata>) exec.getArg().get("metaData");
            regreso = (String) exec.getArg().get("regreso");
            textTitulo.setValue(exec.getArg().get("titulo").toString());
            textResumen.setValue(exec.getArg().get("resumen").toString());
            textCodigo.setValue(exec.getArg().get("codigo").toString());

            textResumen.setStyle("width:100%; border-radius:4px; border:1px solid #CFCFCF;");
            textTitulo.setStyle("width:100%; border-radius:4px; border:1px solid #CFCFCF;");
            dibujarCategoria();
            categ_id = cateDocu.getSelectedItem().getValue();
            dibujarTipoDocumento(categ_id);
            dibujarMetadata((Integer) tipoDocu.getSelectedItem().getValue());
            if (regreso.equals("NO")) {

                //dibujarTipoDocumento(categ_id);
            }
            if (regreso.equals("SI")) {
                categ_id = (Integer) exec.getArg().get("cateDocu");
                tido_id = (Integer) exec.getArg().get("tipoDocu");
                for (Listitem match : cateDocu.getItems()) {
                    if (Objects.equals(categ_id, match.getValue())) {
                        cateDocu.setSelectedItem(match);
                        break;
                    }
                }
                dibujarTipoDocumento(categ_id);
                for (Listitem match1 : tipoDocu.getItems()) {
                    if (Objects.equals(tido_id, match1.getValue())) {
                        tipoDocu.setSelectedItem(match1);
                        break;
                    }
                }
                dibujarMetadata(tido_id);
            }

            rutaTmp = exec.getArg().get("ruta").toString();

        } catch (NullPointerException ex) {
            selected = (Documento) exec.getArg().get("item");
        }
        try {
            rutaVista = exec.getArg().get("rutaVista").toString();
            Carpeta carp = new CatalogoDao().getCarpeta(rutaVista);
            if (carp != null) {
                empleado = new PersonaDao().getPersona(carp.getPropietario());
                metaDNI.setValue(empleado.getDni());
                metaNombre.setValue(empleado.getNombre());
                gridY.setVisible(true);
                String dni = new MetaDataDao().getId("DNI").toString();
                legajo = empleado.getDni();
                metaData2.add(new Metadata(index2, dni, metaDNI.getValue(), null));
                index2++;
                String personal = new MetaDataDao().getId("PERSONAL").toString();
                metaData2.add(new Metadata(index2, personal, metaNombre.getValue(), null));
                index2++;
            } else {
                rowsY.getChildren().clear();
                //metaData2.clear();
            }
        } catch (NullPointerException ex) {
            rowsY.getChildren().clear();

        }

        try {
            if (!metaData2.isEmpty()) {

            }
        } catch (NullPointerException ex) {
        }

        if (selected != null) {
            uploaddiv.setStyle("display:none");
            datos.setStyle("display:block");
            viewFrame.setSrc("./MiniDoc?id=" + selected.getId());
            viewFrame.setStyle("display:block");
            textTitulo.setValue(selected.getTitulo());
            textResumen.setValue(selected.getResumen());
            textTitulo.setReadonly(true);
            textResumen.setReadonly(true);
            save.setStyle("display:none");
            mine = selected.getMineType();
            close.setLabel("Cerrar");
        } else if (rutaTmp != null) {
            up.setStyle("display:block");
            uploaddiv.setStyle("display:none");
            datos.setStyle("display:block");
            viewFrame.setSrc(rutaTmp);
            rutaHtml = rutaTmp;

            ruta = exec.getArg().get("rutaCompleta").toString();
            viewFrame.setStyle("display:block");
            textTitulo.setValue(exec.getArg().get("titulo").toString());
            textResumen.setValue(exec.getArg().get("resumen").toString());
            mine = (MineType) exec.getArg().get("mine");
            save.setDisabled(false);
            close.setLabel("Cerrar");
        }
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

    public void dibujarCategoria() {
        List<Categoria> items = (List<Categoria>) new ArrayList<Categoria>();

        items = new CategoriaDao().listarCategorias();
        cateDocu.getItems().clear();
        for (Categoria item : items) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getCateId());
            listitem.setLabel(item.getCateNombre());
            cateDocu.appendChild(listitem);
        }
        cateDocu.setSelectedIndex(0);
    }

    public void dibujarTipoDocumento(Integer categ_id) {
        List<TipoDocumento> items = (List<TipoDocumento>) new ArrayList<TipoDocumento>();
        String query = " where td.cate_id = '" + categ_id + "' and td.cate_id = c.cate_id ";

        items = new TipoDocumentoDao().listarTipoDocumentoCateg(query);
        tipoDocu.getItems().clear();
        if (items.size() != 0) {
            for (TipoDocumento item : items) {
                Listitem listitem = new Listitem();
                listitem.setValue(item.getId());
                listitem.setLabel(item.getNombre());
                tipoDocu.appendChild(listitem);
            }
            if (!items.isEmpty()) {
                if (tipoDocu.getSelectedItem() == null) {
                    tipoDocu.setSelectedIndex(0);
                    subTipoDocumento(Integer.parseInt(tipoDocu.getSelectedItem().getValue().toString()));
                }
            }
            TipoDocumento td = new TipoDocumento();
            td = new TipoDocumentoDao().get((Integer) tipoDocu.getSelectedItem().getValue());
            if (td.getPrivacidad() == 1) {
                privilegio.setIconSclass("z-icon-globe");
            } else if (td.getPrivacidad() == 2) {
                privilegio.setIconSclass("z-icon-user");
            } else {
                privilegio.setIconSclass("z-icon-cog");
            }
            String text = "";
            try {
                for (String str : td.getMetadata().split("%")) {
                    text += "[" + str + "]";
                }
            } catch (NullPointerException ex) {
            }
            if (!"".equals(td.getMetadata())) {
                tituError.setValue("Formato: " + text);
            }
        }
    }
    @Wire
    Listbox subTipoDocu;

    public void subTipoDocumento(Integer tido_id) {
        List<SubTipo> items = (List<SubTipo>) new ArrayList<SubTipo>();

        items = new TipoDocumentoDao().subTipoDocumento(tido_id);
        subTipoDocu.getItems().clear();
        if (items.size() != 0) {
            for (SubTipo item : items) {
                Listitem listitem = new Listitem();
                listitem.setValue(item.getId());
                listitem.setLabel(item.getNombre());
                subTipoDocu.appendChild(listitem);
            }
            if (!items.isEmpty()) {
                if (subTipoDocu.getSelectedItem() == null) {
                    subTipoDocu.setSelectedIndex(0);
                }
            }
            // TipoDocumento td = new TipoDocumento();
            //  //td = new TipoDocumentoDao().get((Integer) subTipoDocu.getSelectedItem().getValue());
            /*
            if (td.getPrivacidad() == 1) {
                privilegio.setIconSclass("z-icon-globe");
            } else if (td.getPrivacidad() == 2) {
                privilegio.setIconSclass("z-icon-user");
            } else {
                privilegio.setIconSclass("z-icon-cog");
            }
            String text = "";
            try {
                for (String str : td.getMetadata().split("%")) {
                    text += "[" + str + "]";
                }
            } catch (NullPointerException ex) {
            }
            if (!"".equals(td.getMetadata())) {
                tituError.setValue("Formato: " + text);
            }*/
        }
    }

    @Listen("onClick = #addTipo")
    public void addTipo() {
        if (!formularioTipo.isVisible()) {
            formularioTipo.setVisible(true);
            tipoDocu.setDisabled(true);
            rows.setVisible(false);
            metaData.clear();
        } else {
            formularioTipo.setVisible(false);
            tipoDocu.setDisabled(false);
            metaData2.clear();
        }
    }

    @Listen("onSelect = #cateDocu")
    public void selectCateTipoDocu() {
        try {
            categ_id = cateDocu.getSelectedItem().getValue();
            dibujarTipoDocumento(categ_id);
            dibujarMetadata((Integer) tipoDocu.getSelectedItem().getValue());
        } catch (NullPointerException ex) {

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
        if (!"".equals(text)) {
            tituError.setValue("Formato: " + text);
        }
        try {
            if (td.getPrivacidad() == 1) {
                privilegio.setIconSclass("z-icon-globe");
            } else if (td.getPrivacidad() == 2) {
                privilegio.setIconSclass("z-icon-user");
            } else {
                privilegio.setIconSclass("z-icon-cog");
            }
        } catch (NullPointerException ex) {
            privilegio.setIconSclass("z-icon-globe");
        }

        dibujarMetadata(tidoId);
        subTipoDocumento(tidoId);
    }

//-----lista de documentos. filtrado con metadata
    public String getTagValue(String tag, Element elemento) {
        NodeList lista = elemento.getElementsByTagName(tag).item(0).getChildNodes();
        Node valor = (Node) lista.item(0);
        return valor.getNodeValue();
    }

    public void dibujarMetadata(Integer tidoId) {
        List<Metadata> items = (List<Metadata>) new ArrayList<Metadata>();
        HashMap hm = new HashMap();
        hm.put("tidoId", tidoId);
        items = new MetaDataDao().listarCamposTipoDoc(hm);

        if (metaData.isEmpty()) {
            metaData = items;
        }
        Carpeta carp = new CatalogoDao().getCarpeta(rutaVista);
        rows.getChildren().clear();
        for (Metadata item : items) {
            hm.put("id", item.getId());
            Row row = new Row();
            Label label = new Label();
            label.setValue(item.getCampo());
            final Textbox tb = new Textbox();

            final Datebox db = new Datebox();
            db.setFormat("dd/MM/yyyy");
            db.setReadonly(false);

            TipoMetadata tipo = new MetaDataDao().getTipoDato(hm);

            if (!metaData.isEmpty()) {
                for (int i = 0; i < metaData.size(); i++) {
                    if (item.getCampo().equals(metaData.get(i).getCampo())) {
                        if (tipo.getTipoDato().equals("date")) {
                            try {
                                db.setValue(new FormatoFecha().formatStringFechaCorta(metaData.get(i).getDetalle()));

                            } catch (NullPointerException ex) {
                            }
                        } else if (tipo.getTipoDato().equals("string")) {
                            tb.setWidth("300px");
                            if (carp != null) {
                                if (metaData.get(i).getCampo().equals("DNI")) {
                                    metaData.get(i).setDetalle(metaData2.get(0).getDetalle());
                                    tb.setValue(metaData2.get(0).getDetalle());
                                    tb.setDisabled(true);
                                } else if (metaData.get(i).getCampo().equals("PERSONAL")) {
                                    metaData.get(i).setDetalle(metaData2.get(1).getDetalle());
                                    tb.setValue(metaData2.get(1).getDetalle());
                                    tb.setDisabled(true);
                                }
                            } else {
                                tb.setValue(metaData.get(i).getDetalle());
                            }
                        } else {
                            tb.setRows(4);
                            tb.setCols(180);
                            tb.setWidth("500px");
                            tb.setValue(metaData.get(i).getDefecto());
                        }
                        break;
                    }
                }
            }

            if (tipo.getTipoDato().equals("date")) {
                db.addEventListener(Events.ON_CHANGE, new gob.peam.arcdig.view.WinUpload.changeTextbox(item));
            } else {
                tb.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.WinUpload.changeTextbox(item));
            }

            tb.setPlaceholder("Escribir " + item.getCampo().toLowerCase());
            Label label1 = new Label();
            row.appendChild(label);
            if (tipo.getTipoDato().equals("date")) {
                row.appendChild(db);
            } else {
                row.appendChild(tb);
            }
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
            //showNotify(event.getValue(), win);
            for (int i = 0; i < metaData.size(); i++) {
                if (item.getCampo().equals(metaData.get(i).getCampo())) {
                    metaData.get(i).setDetalle(event.getValue());
                    break;
                }
            }
        }
    }
    @Wire
    Label labelX;
    @Wire
    Window winUpload;

    @Listen("onClick=#close")
    public void close() {
        eq = EventQueues.lookup("cancelar", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, null));
        winUpload.detach();
    }

    @Wire
    Button attachBtn, guardarCampo;
    @Wire
    Div datos, uploaddiv;
    @Wire
    Textbox textTitulo, textResumen, textTipoDoc, textCodigo;

    @Listen("onUpload=#attachBtn, #up")
    public void buscar(UploadEvent event) {
        MineTypeDao mineDao = new MineTypeDao();
        try {
            org.zkoss.util.media.Media media = event.getMedia();
            boolean flag = false;
            mimeTypes = media.getContentType();

            for (MineType mine : mineDao.listarMineTypes()) {

                if (mine.getMineNombre().equals(mimeTypes)) {
                    formatType = mine.getMineExt();
                    this.mine = mine;
                    flag = true;
                    break;
                }
            }
            if (flag) {
                save.setDisabled(false);
                saveFile(media);
                up.setStyle("display:block");
            } else {
                showNotify("Este tipo de archivo no esta permitido", win);
                //attachBtn.setDisabled(false);
            }

        } catch (Exception e) {
            //showNotify(e.toString(), win);
        }
    }

    private void saveFile(Media media) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            InputStream fin = media.getStreamData();
            String rootPath;
            in = new BufferedInputStream(fin);

            rootPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
            Random r = new Random();
            int aleatorio = r.nextInt();
            File carpeta = new File(rootPath + SAVE_TEMP_PATH);
            if (carpeta.exists()) {
                carpeta.mkdir();
            }

            file = new File(rootPath + SAVE_TEMP_PATH + "//" + media.getName());

            if (file.isFile()) {
                file = new File(rootPath + SAVE_TEMP_PATH + "//" + aleatorio + media.getName());
                ruta = rootPath + SAVE_TEMP_PATH + "//" + aleatorio + media.getName();
                rutaHtml = SAVE_TEMP_PATH + "//" + aleatorio + media.getName();
                // origen = aleatorio + media.getName();
            } else {
                ruta = rootPath + SAVE_TEMP_PATH + "//" + media.getName();
                rutaHtml = SAVE_TEMP_PATH + "//" + media.getName();
                //   origen = media.getName();
            }

            mimeTypes = media.getContentType();
            //format = media.getFormat();

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
                datos.setStyle("display:block");
                uploaddiv.setStyle("display:none");
                textResumen.setStyle("width:100%; border-radius:4px; border:1px solid #CFCFCF;");

                textTitulo.setStyle("width:100%; border-radius:4px; border:1px solid #CFCFCF;");

                textTitulo.setValue(media.getName().replace(".pdf", "").replace(".PDF", ""));

                viewFrame.setStyle("display:block; border: 1px solid gray;");
                viewFrame.setSrc(rutaHtml);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //-------------------boton guardar

    @Wire
    Datebox date;

    @Listen("onClick=#save")
    public void guardar() {
        tituError.setValue("");
        Boolean flag = true;
        if (ruta != null) {
            if (!"".equals(textTitulo.getValue().toString().trim())) {
                if (formularioTipo.isVisible()) {
                    if (textTipoDoc.getValue().equals("")) {
                        flag = false;
                        showNotify("Por favor Ingrese el nombre del Tipo de documento", win);
                    }
                    for (Metadata meta : metaData2) {
                        try {
                            if (meta.getManual()) {
                                if (new MetaDataDao().getId(meta.getCampo().toUpperCase()) != null) {
                                    flag = false;
                                    showNotify("El campo ingresado: " + meta.getCampo() + "; ya existe en nuestra lista de campos personalizados", win);
                                }
                            }
                        } catch (NullPointerException ex) {
                        }
                    }
                    TipoDocumento td = new TipoDocumento();
                    td.setNombre(td.getNombre());
                    if (new TipoDocumentoDao().getRepetido(td) != 0) {
                        flag = false;
                        showNotify("Este nombre de tipo de documento ya Existe", win);
                    }
                }
                if (flag) {

                    HashMap hm = new HashMap();
                    hm.put("ruta", ruta);
                    hm.put("rutaHtml", rutaHtml);
                    hm.put("titulo", textTitulo.getValue());
                    hm.put("resumen", textResumen.getValue());
                    hm.put("mine", mine);
                    hm.put("metaData", metaData);
                    hm.put("legajo", legajo);
                    hm.put("codigo", textCodigo.getValue());

                    if (formularioTipo.isVisible()) {
                        hm.put("metaData2", metaData2);
                        hm.put("tidoTitulo", textTipoDoc.getValue().toUpperCase());
                        hm.put("formularioTipo", formularioTipo);
                    } else {
                        List<Metadata> metadata3 = (List<Metadata>) new ArrayList<Metadata>();
                        hm.put("metaData2", metadata3);
                        hm.put("formularioTipo", new Grid());
                        hm.put("tidoTitulo", "");
                    }

                    hm.put("tipoDocu", tipoDocu.getSelectedItem().getValue());
                    hm.put("cateDocu", cateDocu.getSelectedItem().getValue());
                    hm.put("cateNombre", cateDocu.getSelectedItem().getLabel());
                    hm.put("privilegio", privilegio);
                    hm.put("fechaDocu", fechaDocu);
                    hm.put("personalizado", personalizado);
                    hm.put("correo", correo);
                    hm.put("notificacion", notificacion);
                    try {
                        hm.put("subTipo", subTipoDocu.getSelectedItem().getValue());
                    } catch(NullPointerException ex){
                        hm.put("subTipo", 1);
                    }

                    eq = EventQueues.lookup("dataSubida", EventQueues.DESKTOP, false);
                    eq.publish(new Event("", null, hm));

                    eq1.close();
                    eq2.close();
                    eq3.close();
                    winUpload.detach();
                }
            } else {
                tituError.setValue("No dejar en blanco porfavor");
            }
        }

    }

    public List<Metadata> metadata = (List<Metadata>) new ArrayList<Metadata>();
    @Wire
    Rows rows;

    class borrarMetatada implements EventListener {

        int i;
        Row row;

        public borrarMetatada(int i, Row row) {
            this.i = i;
            this.row = row;
        }

        public void onEvent(Event event) throws Exception {
            metadata.remove(i);
            row.detach();
        }
    }

    //-----------agregar tipo de documento - - - - -- -/
    private List<Metadata> metaData2 = (List<Metadata>) new ArrayList<Metadata>();
    public Integer index2 = 0;

    @Listen("onClick = #addRowMeta")
    public void addRowMetada() {
        gridY.setVisible(true);
        final Row row = new Row();
        metaData2.add(new Metadata(index2, "", "", null));

        Listbox lb = new Listbox();
        List<Metadata> lmd = (List<Metadata>) new ArrayList<Metadata>();
        lmd = new MetaDataDao().listarMetadataCombo();
        for (Metadata m : lmd) {
            Listitem li = new Listitem();
            li.setValue(m.getId());
            li.setLabel(m.getCampo());
            lb.appendChild(li);
        }
        Div div = new Div();

        lb.setMold("select");
        lb.setHeight("25px");
        lb.addEventListener(Events.ON_SELECT, new gob.peam.arcdig.view.WinUpload.changeListbox(index2));
        lb.setWidth("180px");

        //Oculto textbox
        Textbox tbh = new Textbox();
        tbh.setVisible(false);
        tbh.setHeight("25px");
        tbh.setWidth("180px");
        tbh.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.WinUpload.changeTextbox4(index2));

        div.appendChild(lb);
        div.appendChild(tbh);

        Listbox lb1 = new Listbox();
        lb1.setMold("select");
        lb1.setHeight("25px");
        lb1.setWidth("120px");

        Listitem li = new Listitem();
        li.setValue("string");
        li.setLabel("Texto simple");
        li.setSelected(true);
        lb1.appendChild(li);
        Listitem li1 = new Listitem();
        li1.setValue("text");
        li1.setLabel("Texto Complejo");
        lb1.appendChild(li1);
        Listitem li2 = new Listitem();
        li2.setValue("date");
        li2.setLabel("Fecha");
        lb1.appendChild(li2);

        lb1.addEventListener(Events.ON_SELECT, new gob.peam.arcdig.view.WinUpload.changeListbox1(index2));

        Textbox tb = new Textbox();
        tb.setWidth("140px");

        tb.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.WinUpload.changeTextbox3(index2));

        /*Checkbox ch = new Checkbox();
         ch.addEventListener(Events.ON_CHECK, new gob.peam.arcdig.view.WinUpload.changeCheck(index2));*/
        Button b1 = new Button();
        b1.setIconSclass("z-icon-minus");
        b1.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.WinUpload.borrarMetadata1(metaData2.get(index2), row));

        Button b2 = new Button();
        b2.setIconSclass("z-icon-cog");
        b2.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.WinUpload.cambiarObjeto(lb, tbh, metaData2.get(index2)));

        Div div2 = new Div();
        div2.appendChild(b1);
        div2.appendChild(b2);

        index2++;

        row.appendChild(div);
        row.appendChild(lb1);
        row.appendChild(tb);
        //row.appendChild(ch);
        row.appendChild(div2);

        rowsY.appendChild(row);
    }

    class changeListbox implements EventListener<SelectEvent> {

        final Integer i;

        public changeListbox(Integer i) {
            this.i = i;
        }

        public void onEvent(SelectEvent event) throws Exception {
            Set item = event.getSelectedItems();
            List<Listitem> emp = new ArrayList<Listitem>(item);
            metaData2.get(i).setCampo(emp.get(0).getValue().toString());
        }
    }

    class changeTextbox3 implements EventListener<InputEvent> {

        final Integer i;

        public changeTextbox3(Integer i) {
            this.i = i;
        }

        public void onEvent(InputEvent event) throws Exception {
            metaData2.get(i).setDetalle(event.getValue());
        }
    }

    class changeTextbox4 implements EventListener<InputEvent> {

        final Integer i;

        public changeTextbox4(Integer i) {
            this.i = i;
        }

        public void onEvent(InputEvent event) throws Exception {
            metaData2.get(i).setCampo(event.getValue());
        }
    }

    class borrarMetadata1 implements EventListener {

        final Metadata m;
        final Row row;

        public borrarMetadata1(Metadata m, Row row) {
            this.m = m;
            this.row = row;
        }

        public void onEvent(Event event) throws Exception {
            row.detach();
            metaData2.get(m.getId()).setCampo("");
        }
    }

    class cambiarObjeto implements EventListener {

        final Listbox lb;
        final Textbox tb;
        final Metadata m;

        public cambiarObjeto(Listbox lb, Textbox tb, Metadata m) {
            this.lb = lb;
            this.tb = tb;
            this.m = m;
        }

        public void onEvent(Event event) throws Exception {
            //showNotify(tb.isVisible(), win);
            if (!tb.isVisible()) {
                tb.setValue("");
                metaData2.get(m.getId()).setManual(true);
                tb.setVisible(true);
                lb.setVisible(false);
            } else {
                metaData2.get(m.getId()).setManual(false);
                tb.setVisible(false);
                lb.setVisible(true);
            }

        }
    }

    class changeListbox1 implements EventListener<SelectEvent> {

        final Integer i;

        public changeListbox1(Integer i) {
            this.i = i;
        }

        public void onEvent(SelectEvent event) throws Exception {
            Set item = event.getSelectedItems();
            List<Listitem> emp = new ArrayList<Listitem>(item);
            metaData2.get(i).setTipoDato(emp.get(0).getValue().toString());
        }
    }

    class changeCheck implements EventListener<CheckEvent> {

        final Integer i;

        public changeCheck(Integer i) {
            this.i = i;
        }

        public void onEvent(CheckEvent event) throws Exception {
            metaData2.get(i).setObligado(event.isChecked());
        }
    }
}
