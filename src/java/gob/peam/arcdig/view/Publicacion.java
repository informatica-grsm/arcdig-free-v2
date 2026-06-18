package gob.peam.arcdig.view;

import com.qoppa.pdf.PDFException;
import com.qoppa.pdfSecure.PDFSecure;
import config.ConexionReporte;
import gob.peam.arcdig.beans.Catalogo;
import gob.peam.arcdig.beans.Categoria;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.DocumentoEtiqueta;
import gob.peam.arcdig.beans.EtiquetaDocu;
import gob.peam.arcdig.beans.Metadata;
import gob.peam.arcdig.beans.TipoDocumento;
import gob.peam.arcdig.dao.CatalogoDao;
import gob.peam.arcdig.dao.CategoriaDao;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.dao.EtiquetaDocuDao;
import gob.peam.arcdig.dao.MetaDataDao;
import gob.peam.arcdig.dao.PermisoDao;
import gob.peam.arcdig.dao.PersonaDao;
import gob.peam.arcdig.dao.TipoDocumentoDao;
import gob.peam.arcdig.utils.FormatoFecha;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Span;
import org.zkoss.zul.Splitter;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class Publicacion extends SelectorComposer<Component> {

    public long limit = 20;
    public long offset = 0;
    private EventQueue eq;
    private Listitem selectTipoDocu;
    private Listitem selectCateDocu;
    public List<Catalogo> catas = (List<Catalogo>) new ArrayList<Catalogo>();
    private Boolean tres, cuatro, cinco, seis, siete, ocho;
    public Documento selectItem;
    @Wire
    A iconRelacion;
    @Wire
    Rows rows;
    @Wire
    Div verMas;
    @Wire
    Grid gridPublicacion;
    @Wire
    Vbox caja1, caja2;
    @Wire
    Iframe vista;
    @Wire
    Splitter s1;
    @Wire
    Button download, visor, visorOri, firma;
    @Wire
    Span iconMetadata;
    String c = "";
    String query = "";
    String q = "";
    String auxq = "";

    public Publicacion() {

    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        int idPers = 0;
        gob.peam.arcdig.beans.Permiso perm = new gob.peam.arcdig.beans.Permiso();
        String dni = "";
        try {
            dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
            idPers = Integer.parseInt(Executions.getCurrent().getDesktop().getSession().getAttribute("usuaId").toString());
            perm = new PermisoDao().getPermisos(dni);
            tres = !perm.getItem3();
            cuatro = !perm.getItem4();
            cinco = !perm.getItem5();
            seis = !perm.getItem6();
            siete = !perm.getItem7();
            ocho = !perm.getItem8();
        } catch (NullPointerException ex) {
            tres = false;
            cuatro = false;
            cinco = false;
            seis = false;
            siete = false;
            ocho = false;
        }
        if (siete) {
            if (seis) {
                String xquery = "";
                int index = 0;
                for (Catalogo c : new CatalogoDao().listarUsuarioCatalogo(idPers)) {
                    if (index == 0) {
                        xquery += " cata_id = " + c.getCataId();
                        catas.add(c);
                    } else {
                        xquery += " or cata_id = " + c.getCataId();
                        catas.add(c);
                    }
                    index++;
                }
                if (index > 0) {
                    q = "and ( (" + xquery + " or docu_publico = true))";
                } else {
                    q = "";
                }
            } else {
                q = "and docu_privado = false";
            }
        } else {

            q = " and (propietario = '" + dni + "' or documento.docu_id in (select du.docu_id from documento_usuario du where du.pers_dni= '" + dni + "'))";
        }
        auxq = q;
        dibujarPublicaciones(q);

        eq = EventQueues.lookup("dataPublicar", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                addRow((Documento) hm.get("Item"));
            }
        });

        eq = EventQueues.lookup("buscarPublicacion", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                String vector = "";
                String filtrado = hm.get("filtro").toString().replaceAll("[^\\p{Alpha}\\p{Digit}\\p{Blank}\\p{Blank}]+", "");
                vector = filtrado.trim().replace(" ", "%") + ":*";
                if (!hm.get("q").toString().equals("")) {
                    q = hm.get("q").toString();
                } else {
                    q = auxq;
                }
                if (filtrado.length() == 0) {
                    c = "";
                } else {
                    c = "and ( (to_tsquery('" + vector + "') @@ docu_search) or docu_titulo ilike '%" + hm.get("filtro").toString() + "%' or  str_normalize(docu_codigo) ilike str_normalize('"+hm.get("filtro").toString() +"') ) ";
                }
                rows.getChildren().clear();

                dibujarPublicaciones(c + q);
            }
        });

        eq = EventQueues.lookup("buscarQuery", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                query = hm.get("query").toString();
                rows.getChildren().clear();

                if (!"".equals(query)) {
                    dibujarPublicaciones(c + query + q);
                } else {
                    dibujarPublicaciones(c + q);
                }
            }
        });

        eq = EventQueues.lookup("editMetadata", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                selectItem.setTidoId((Integer) hm.get("tipoDocu"));

                List<Metadata> metaData = (List<Metadata>) new ArrayList<Metadata>();
                metaData = (List<Metadata>) hm.get("metaData");

                if (!metaData.isEmpty()) {
                    String itemMeta = "<metadata>";
                    for (Metadata meta : metaData) {
                        if (!"".equals(meta.getDetalle()) || meta.getDetalle() != null) {
                            itemMeta += "<docu_metadata><meta_id>" + meta.getId() + "</meta_id><meta_nombre>" + meta.getCampo() + "</meta_nombre><meta_descripcion>" + meta.getDetalle() + "</meta_descripcion></docu_metadata>";
                        }
                    }
                    itemMeta += "</metadata>";
                    selectItem.setMetaData(itemMeta);
                }
                
                new DocumentoDao().update(selectItem);
                selectItem.setTidoId((Integer) lTipoDocu.getSelectedItem().getValue());
                selectTipoDocu = lTipoDocu.getSelectedItem();
                labelTipoDoc.setValue(lTipoDocu.getSelectedItem().getLabel());
                labelTipoDoc.setVisible(true);
                lTipoDocu.setVisible(false);
                divMetadata.setVisible(false);
                
                if (selectItem.getMetaData() != null && !"".equals(selectItem.getMetaData())) {
                    listMetadata.getItems().clear();
                    divMetadata.setVisible(true);
                    
                    for (Metadata meta : new MetaDataDao().getMetadata(selectItem)) {
                        Listitem listitem = new Listitem();

                        Listcell campo = new Listcell();
                        campo.setStyle("font-weight:bold; color:black");
                        campo.appendChild(new Label(meta.getCampo()));

                        Listcell dos_puntos = new Listcell();
                        dos_puntos.setStyle("font-weight:bold; color:black");
                        dos_puntos.appendChild(new Label(":"));

                        Listcell dato = new Listcell();
                        try {
                            dato.appendChild(new Label(meta.getDetalle()));
                        } catch (NullPointerException ex) {
                        }

                        listitem.appendChild(campo);
                        listitem.appendChild(dos_puntos);
                        listitem.appendChild(dato);
                        listMetadata.appendChild(listitem);
                    }

                }
            }
        });

        eq = EventQueues.lookup("dataEdiEtiqueta", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                List<Listitem> etiquetas = (List<Listitem>) hm.get("etiquetas");
                modificarEtiquetas(etiquetas);
            }
        });

    }

    @Listen("onClick = #siguiente")
    public void siguiente() {
        offset += 20;
        limit += 20;
        if (!"".equals(query)) {
            dibujarPublicaciones(c + query + q);
        } else {
            dibujarPublicaciones(c + q);
        }
    }

    public void dibujarPublicaciones(String filtro) {

        HashMap hm = new HashMap();
        hm.put("limit", limit);
        hm.put("offset", offset);
        hm.put("date", new Date(new Date().getTime()));
        hm.put("c", filtro);
        Date hoy = new Date(new Date().getTime());
        List<Documento> docus = (List<Documento>) new ArrayList<Documento>();

        docus = new DocumentoDao().listarDocumentos(hm);

        Execution exec = Executions.getCurrent();
        HttpServletRequest request = (HttpServletRequest) exec.getNativeRequest();
        String documento = null;
        try {
            documento = request.getSession().getAttribute("dni").toString();
        } catch (NullPointerException ex) {
            exec.getSession().invalidate();
        }

        if (docus.isEmpty()) {
            verMas.setStyle("display:none");
        } else {
            verMas.setStyle("display:block");
        }
        for (Documento item : docus) {
            Row row = new Row();
            Div div1 = new Div();
            Div div2 = new Div();

            Image img1 = new Image();
            if (item.getMimeTypes() != null) {
                img1.setSrc("./resources/img/pdf1.png");
                img1.setStyle("border:0px; padding:0px; cursor:pointer");
            } else {
                img1.setSrc("./resources/img/file.png");
                img1.setStyle("border:0px; padding:0px; cursor:pointer");
            }
            img1.setWidth("48px");
            try {
                img1.setTooltiptext(item.getMineType().getMineNombre());
            } catch (NullPointerException e) {
                img1.setTooltiptext("Configure tipo de Archivos");
            }

            img1.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.Publicacion.detalle(item));

            div1.appendChild(img1);

            /*Configurando el cuerpo de la publicacion*/
            div2.setSclass("body");
            A a1 = new A();
            a1.setLabel(item.getTitulo());
            a1.setSclass("name");
            a1.setStyle("font-size:15px;");

            a1.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.Publicacion.detalle(item));

            if (new FormatoFecha().esMismoDia(hoy, item.getDocuFecha())) {
                Label label9 = new Label();
                label9.setSclass("label label-success arrowed arrowed-in-right");
                label9.setValue("Nuevo!");
                label9.setStyle("margin-left:4px");
                a1.appendChild(label9);
            }
            A a6 = new A();
            a6.setSclass("time");
            if (item.getPublico()) {
                a6.setIconSclass("z-icon-globe");
                a6.setTooltiptext("Público");
            } else if (item.getPrivado()) {
                a6.setIconSclass("z-icon-user");
                a6.setTooltiptext("Privado");
            } else {
                a6.setIconSclass("z-icon-cog");
                a6.setTooltiptext("Personalizado");
            }

            A a2 = new A();
            a2.appendChild(a6);
            String remain = new FormatoFecha().formatear(item.getRemain());
            a2.setLabel(remain);
            try {
                if (Integer.parseInt(remain.split(" ")[0]) > 30) {
                    if ("dias".equals(remain.split(" ")[1])) {
                        a2.setLabel(new FormatoFecha().formatFechaCorta(item.getDocuFecha()));
                    }
                }
            } catch (NumberFormatException ex) {
                a2.setLabel("Hace un momento");
                //showNotify(ex.toString(), win, "info", "top_right");
            }

            a2.setSclass("time green");
            a2.setStyle("right:30px");
            a2.setIconSclass("z-icon-clock-o");

            Div div3 = new Div();
            div3.setSclass("text");

            Span span1 = new Span();

            Div div4 = new Div();
            div4.setStyle("right:40px");
            div4.setSclass("tools action");

            A a3 = new A();
            a3.setIconSclass("z-icon-pencil blue bigger-125");

            A a4 = new A();
            a4.setIconSclass("z-icon-trash-o red bigger-125");
            final Documento bean = item;
            final Row ritem = row;
            a4.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    Messagebox.show(
                            "Esta Seguro de borrar este Documento?", "Diálogo de Confirmación ",
                            Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION,
                            new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event evt) throws Exception {
                            if (evt.getName().equals("onOK")) {
                                if (new DocumentoDao().getRelacion(bean.getDocuGroup()) > 1) {
                                    Messagebox.show(
                                            "Advertencia, este documento esta relacionado a un expediente, confirme para borrar", "Diálogo de Confirmación ",
                                            Messagebox.OK | Messagebox.CANCEL,
                                            Messagebox.EXCLAMATION,
                                            new org.zkoss.zk.ui.event.EventListener() {
                                        @Override
                                        public void onEvent(Event evt) throws Exception {
                                            if (evt.getName().equals("onOK")) {
                                                borrarPublicacion(bean, ritem);
                                            }
                                        }
                                    });
                                } else {
                                     borrarPublicacion(bean, ritem);
                                }

                            }
                        }
                    });
                }
            });

            A a5 = new A();
            div2.appendChild(a1);

            div2.appendChild(a2);

            /*agregando documento si  existe */
            Div div5 = new Div();

            if (!item.getEtiquetaDocu().isEmpty()) {
                div5.setSclass("z-box-archivo");
                div5.setStyle("cursor:pointer; padding:0px; border:0px;");
                Div div6 = new Div();
                div6.setStyle("float:left");
                Image img = new Image();
                img.setSrc("./resources/img/tag.png");
                img.setStyle("padding:0px; width:20px; border:0px");
                div6.appendChild(img);
                div5.appendChild(div6);

                Div div7 = new Div();
                for (EtiquetaDocu eti : item.getEtiquetaDocu()) {
                    Label label = new Label();
                    label.setSclass("arrowed arrowed-in-right");
                    label.setStyle("font-size:8px; color:orange");
                    label.setValue(eti.getNombre());

                    div7.appendChild(label);
                }

                Separator separator = new Separator();
                separator.setSclass("double16 soid");
                separator.setBar(false);

                Label label1 = new Label();
                label1.setValue("Metadata");
                label1.setStyle("text-align: justify");

                div5.appendChild(div7);
                div2.appendChild(div5);
            }

            Separator separator1 = new Separator();
            separator1.setSclass("double16 soid");
            separator1.setBar(true);

            try {
                if (cuatro) {
                    Boolean edita = false;
                    for (Catalogo c : catas) {
                        if (Objects.equals(item.getCataId(), c.getCataId())) {

                            edita = true;
                            break;
                        }
                    }

                    if (edita) {
                        div4.appendChild(a4);
                    }
                }
            } catch (NullPointerException ex) {
            }

            //agregando a la rows
            div3.appendChild(span1);

            if (item.getResumen() != null && !"".equals(item.getResumen())) {
                div3.appendChild(new Html(this.cortarCadena(item.getResumen(), 280)));
                div3.setStyle("text-align: justify; font-size:12px");
            } else {
                div3.appendChild(new Html("<em style='color:#ccc; font-size:12px'>NO CONTIENE RESUMEN</em>"));
            }

            div3.appendChild(separator1);
            div2.appendChild(div3);
            div2.appendChild(div4);

            row.appendChild(div1);
            row.appendChild(div2);
            rows.appendChild(row);
        }
    }

    public String cortarCadena(String cadena, int max) {
        String cadenaNueva = "";
        if (cadena.length() > max) {
            String pedazo;
            pedazo = cadena.substring(max - 10, max);
            try {
                try {
                    String cad1 = pedazo.split(" ")[0];
                    pedazo = cadena.substring(0, max - 10) + cad1 + "...";
                } catch (NullPointerException ex) {
                    pedazo = cadena;
                }
            } catch (IndexOutOfBoundsException ex) {

            }
            cadenaNueva = pedazo;
        } else {
            cadenaNueva = cadena;
        }
        return cadenaNueva;
    }

    public void addRow(Documento item) {
        limit += 2;
        offset += 2;
        Execution exec = Executions.getCurrent();
        HttpServletRequest request = (HttpServletRequest) exec.getNativeRequest();

        String documento = request.getSession().getAttribute("dni").toString();
        Row row = new Row();
        Div div1 = new Div();
        Div div2 = new Div();
        //div1.setSclass("user");

        Image img1 = new Image();
        if (item.getMimeTypes() != null) {
            img1.setSrc("./resources/img/pdf.png");
            img1.setStyle("border:0px; padding:0px; cursor:pointer");
        } else {
            img1.setSrc("./resources/img/file.png");
            img1.setStyle("border:0px; padding:0px; cursor:pointer");
        }
        img1.setWidth("48px");
        img1.setTooltiptext(item.getMineType().getMineNombre());
        img1.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.Publicacion.detalle(item));
        div1.appendChild(img1);

        /*Configurando el cuerpo de la publicacion*/
        div2.setSclass("body");
        A a1 = new A();
        a1.setLabel(item.getTitulo());
        a1.setSclass("name");
        a1.setStyle("font-size:15px; ");
        a1.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.Publicacion.detalle(item));

        //if (new FormatoFecha().esMismoDia(hoy, item.getDocuFecha())) {
        Label label9 = new Label();
        label9.setSclass("label label-success arrowed arrowed-in-right");
        label9.setValue("Nuevo!");
        label9.setStyle("margin-left:4px");
        //}

        //a1.setHref("./link_");
        A a6 = new A();

        a6.setSclass("time");
        /*if (item.getPrivacidad() == 1) {
         a6.setIconSclass("z-icon-group");*/
        if (item.getPublico()) {
            a6.setIconSclass("z-icon-globe");
            a6.setTooltiptext("Público");
        } else if (item.getPrivado()) {
            a6.setIconSclass("z-icon-user");
            a6.setTooltiptext("Privado");
        } else {
            a6.setIconSclass("z-icon-cog");
            a6.setTooltiptext("Personalizado");
        }

        A a2 = new A();

        a2.appendChild(a6);

        a2.setLabel("Hace un instante");
        a2.setSclass("time green");
        a2.setStyle("right:30px");
        a2.setIconSclass("z-icon-clock-o");

        Div div3 = new Div();

        div3.setSclass("text");

        Span span1 = new Span();

        Div div4 = new Div();

        div4.setSclass("tools action");
        div4.setStyle("right:40px");
        /*if (item.getResumen()
         != null && !"".equals(item.getResumen())) {
         span1.setSclass("z-icon-quote-left");
         }*/

        A a3 = new A();

        a3.setIconSclass("z-icon-pencil blue bigger-125");

        A a4 = new A();

        a4.setIconSclass("z-icon-trash-o red bigger-125");
        final Documento bean = item;
        final Row ritem = row;

        a4.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {

            public void onEvent(Event event) throws Exception {
                Messagebox.show(
                        "Esta Seguro de borrar esta publicación?", "Dialogo de Confirmación ",
                        Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION,
                        new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event evt) throws Exception {
                        if (evt.getName().equals("onOK")) {

                            if (new DocumentoDao().getRelacion(bean.getDocuGroup()) > 1) {
                                Messagebox.show(
                                        "Advertencia, este documento esta relacionado a un expediente, confirme para borrar", "Diálogo de Confirmación ",
                                        Messagebox.OK | Messagebox.CANCEL,
                                        Messagebox.EXCLAMATION,
                                        new org.zkoss.zk.ui.event.EventListener() {
                                    @Override
                                    public void onEvent(Event evt) throws Exception {
                                        if (evt.getName().equals("onOK")) {
                                            borrarPublicacion(bean, ritem);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        A a5 = new A();
        div2.appendChild(a1);

        div2.appendChild(a2);

        /*agregando documento si lo existe */
        Div div5 = new Div();

        if (!item.getEtiquetaDocu().isEmpty()) {
            div5.setSclass("z-box-archivo");
            div5.setStyle("cursor:pointer; padding:0px; border:0px;");
            Div div6 = new Div();
            div6.setStyle("float:left");
            Image img = new Image();
            img.setSrc("./resources/img/tag.png");
            img.setStyle("padding:0px; width:20px; border:0px");
            div6.appendChild(img);
            div5.appendChild(div6);

            Div div7 = new Div();
            for (EtiquetaDocu eti : item.getEtiquetaDocu()) {
                Label label = new Label();
                label.setSclass("arrowed arrowed-in-right");
                label.setStyle("font-size:8px; color:orange");
                label.setValue(eti.getNombre());

                div7.appendChild(label);
            }

            Separator separator = new Separator();
            separator.setSclass("double16 soid");
            separator.setBar(false);

            Label label1 = new Label();
            label1.setValue("Metadata");
            label1.setStyle("text-align:justify");

            //div7.appendChild(separator);
            //div7.appendChild(label1);
            div5.appendChild(div7);
            div2.appendChild(div5);
        }

        Separator separator1 = new Separator();
        separator1.setSclass("double16 soid");
        separator1.setBar(true);

        try {
            if (cuatro) {
                Boolean edita = false;
                for (Catalogo c : catas) {
                    if (Objects.equals(item.getCataId(), c.getCataId())) {
                        edita = true;
                        break;
                    }
                }
                if (edita) {
                    //div4.appendChild(a3);
                    div4.appendChild(a4);
                }
            }
        } catch (NullPointerException ex) {
        }

        //agregando a la rows
        div3.appendChild(span1);

        //div3.appendChild(div5);
        if (item.getResumen() != null && !"".equals(item.getResumen())) {
            div3.appendChild(new Html(this.cortarCadena(item.getResumen(), 280)));
            div3.setStyle("text-align: justify; font-size:12px");
        } else {
            div3.appendChild(new Html("<em style='color:#ccc; font-size:12px'>NO CONTIENE RESUMEN</em>"));
        }
        div3.appendChild(separator1);
        div2.appendChild(div3);
        div2.appendChild(div4);

        row.appendChild(div1);
        row.appendChild(div2);

        rows.insertBefore(row, rows.getFirstChild());
    }

    public void borrarPublicacion(Documento bean, Row ritem) throws IOException {
        new EtiquetaDocuDao().delete(bean.getId());
        new DocumentoDao().delete(bean);
        String path = new ConexionReporte().obtenerCarpeta("carpeta");
        try {
            //String rutaServer = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
            String Oriruta = path + bean.getRuta() + "//" + bean.getId() + "." + bean.getMineType().getMineExt();
            String auxPath = "";
            if (path.contains("\\")) {
                auxPath = (path + Oriruta).replace("/", "\\");
                //path += "\\" + auxPath;
            } else {
                auxPath = (path + Oriruta).replace("\\", "//");
                //path +=  auxPath;
            }

            // showNotify(auxPath, win, "info", "top_center");
            File file = new File(Oriruta);
            file.delete();
        } catch (NullPointerException ex) {

        }
        ritem.detach();
        showNotify("Se borró la publicación correctamente", win, "info", "top_right");
    }

    @Wire
    Label labelTitulo, labelResumen, labelTipoDoc, labelFechaDoc, labelCate, labelEtiqueta, labelInfo, labelRuta, labelID, labelRelacion, labelLink, labelLinkSolo,labelLinkSolo1,labelLinkSolo2, labelCodigo;
    @Wire
    A iconTitulo, iconResumen, iconTipoDocu, iconFecha, iconCate, iconEtiqueta, iconCodigo;
    @Wire
    Listcell cellEtiqueta;
    @Wire
    Div divMetadata;
    @Wire
    Listbox listMetadata, lTipoDocu, lCateDocu;
    @Wire
    Textbox tTitulo, tResumen, tCodigo;
    @Wire
    Datebox dFecha;
    @Wire
    Button expediente;

    class detalle implements EventListener {

        final Documento item;

        public detalle(Documento item) {
            this.item = item;
        }

        public void onEvent(Event event) throws Exception {
            s1.setVisible(true);
            caja2.setVisible(true);
            MouseEvent me = (MouseEvent) event;

            Documento item = new DocumentoDao().getOne(this.item.getId());

            selectItem = item;
            Execution exec = Executions.getCurrent();
            HttpServletRequest request = (HttpServletRequest) exec.getNativeRequest();
            String documento = request.getSession().getAttribute("dni").toString();
            Boolean original = false;
            Boolean edita = false;
            try {
                if (tres) {
                    for (Catalogo c : catas) {
                        //showNotify(c.getCataId() + "", win, "info", "top_center");
                        if (Objects.equals(this.item.getCataId(), c.getCataId())) {
                            edita = true;
                            break;
                        }
                    }
                    original = true;
                    if (edita) {
                        iconTitulo.setVisible(true);
                        iconResumen.setVisible(true);
                        iconTipoDocu.setVisible(true);
                        iconFecha.setVisible(true);
                        iconCate.setVisible(true);
                        iconCodigo.setVisible(true);
                        iconMetadata.setVisible(true);
                    } else {
                        iconTitulo.setVisible(false);
                        iconResumen.setVisible(false);
                        iconTipoDocu.setVisible(false);
                        iconFecha.setVisible(false);
                        iconCate.setVisible(false);
                        iconCodigo.setVisible(false);
                        iconMetadata.setVisible(false);
                        original = false;
                    }

                    dibujarTipoDocumento(item.getTidoId());
                    dibujarCategoria(item.getCateId());
                } else {
                    original = false;
                    iconTitulo.setVisible(false);
                    iconResumen.setVisible(false);
                    iconTipoDocu.setVisible(false);
                    iconFecha.setVisible(false);
                    iconCate.setVisible(false);
                    iconCodigo.setVisible(false);
                    iconMetadata.setVisible(false);
                }

                if (ocho) {
                    iconEtiqueta.setVisible(true);
                } else {
                    iconEtiqueta.setVisible(false);
                }
            } catch (NullPointerException ex) {
                original = false;
                iconTitulo.setVisible(false);
                iconResumen.setVisible(false);
                iconTipoDocu.setVisible(false);
                iconFecha.setVisible(false);
                iconCate.setVisible(false);
                iconEtiqueta.setVisible(false);
                iconMetadata.setVisible(false);
                iconCodigo.setVisible(false);
            }

            if (cinco && original) {
                visorOri.setDisabled(false);
                visorOri.setVisible(true);
            } else {
                visorOri.setDisabled(true);
                visorOri.setVisible(false);
            }

            download.setHref("./Download.pdf?id=" + item.getId());
            caja1.setWidth(porcentaje(getScrnWid(), 0.5) + "px");

            tTitulo.setWidth(porcentaje(getScrnWid(), 0.24) + "px");
            tResumen.setWidth(porcentaje(getScrnWid(), 0.24) + "px");
            tCodigo.setWidth(porcentaje(getScrnWid(), 0.24) + "px");
            
            gridPublicacion.setWidth(porcentaje(getScrnWid(), 0.5) + "px");
            caja2.setWidth(porcentaje(getScrnWid(), 0.5) - 50 + "px");
            if (me.getPageY() > 450) {
                caja2.setStyle("margin-top:" + (me.getPageY() - 450) + "px");
            } else {
                caja2.setStyle("margin-top:0px");
            }
            vista.setSrc("./ArcDig.pdf?id=" + item.getId());
            vista.setWidth(porcentaje(getScrnWid(), 0.5) - 50 + "px");

            String ruta = new ConexionReporte().obtenerCarpeta("carpeta");
            if (verificarFirma(ruta + item.getRuta() + "//" + item.getId() + ".pdf")) {
                firma.setDisabled(false);
                firma.setStyle("color:green");
            } else {
                firma.setDisabled(true);
                firma.setStyle("color:gray");
            }
            s1.setOpen(true);

            labelID.setValue(item.getId().toString());
            labelTitulo.setValue(item.getTitulo());
            labelCodigo.setValue(item.getDocuCodigo());
            labelResumen.setValue(item.getResumen());
            labelTipoDoc.setValue(item.getTipoDocumento().getNombre());
            labelFechaDoc.setValue(item.getFechaDocx());
            labelCate.setValue(item.getCategoria().getCateNombre());
            String rutaLink = new ConexionReporte().obtenerCarpeta("link");

            Integer tamRela = new DocumentoDao().getRelacion(item.getDocuGroup());
            
            
            if (tamRela > 1) {
                labelRelacion.setValue("Este documento contiene " + (new DocumentoDao().getRelacion(item.getDocuGroup()) - 1) + " archivo(s) asociado");
                iconRelacion.setVisible(true);
                labelLink.setValue(rutaLink + "/Downloads.pdf?id=" + item.getDocuGroup());
                expediente.setDisabled(false);
                
                
                labelLinkSolo.setValue(rutaLink + "/downloadsolo.pdf?id=" + item.getId());
                labelLinkSolo1.setValue(rutaLink + "/downloadsolo1.pdf?id=" + item.getId());
                labelLinkSolo2.setValue(rutaLink + "/downloadsolo2.pdf?id=" + item.getId());
            } else {
                iconRelacion.setVisible(true);
                labelRelacion.setValue("Este documento no tiene archivos asociados");
                labelLink.setValue(rutaLink + "/ArcDig.pdf?id=" + item.getId());
                expediente.setDisabled(true);
            }

            tTitulo.setValue(item.getTitulo());
            tResumen.setValue(item.getResumen());
            tCodigo.setValue(item.getDocuCodigo());
            dFecha.setValue(new FormatoFecha().formatStringFechaCorta(item.getFechaDocx()));

            try {
                labelRuta.setValue(item.getRuta().replace("//", "\\"));
                labelRuta.setTooltiptext(item.getRuta().replace("//", "\\"));
            } catch (NullPointerException e) {
            }

            cellEtiqueta.getChildren().clear();
            if (!item.getEtiquetaDocu().isEmpty()) {
                for (int i = 0; i < item.getEtiquetaDocu().size(); i++) {
                    Label label = new Label();
                    label.setValue(item.getEtiquetaDocu().get(i).getNombre());
                    label.setSclass("label label-warning arrowed arrowed-in-right");
                    cellEtiqueta.appendChild(label);
                }
            } else {
                labelEtiqueta.setValue("No tiene");
            }

            labelInfo.setValue("Subido el " + new FormatoFecha().formatFechaCorta(item.getDocuFecha()) + " Por " + new PersonaDao().getNombrePersona(item.getUsuaId()));

            listMetadata.getItems().clear();
            divMetadata.setVisible(false);
            try{
            if (item.getMetaData() != null && !"".equals(item.getMetaData())) {
                    int count = 0;

                    for (Metadata meta : new MetaDataDao().getMetadata(item)) {
                        count++;
                        Listitem listitem = new Listitem();

                        Listcell campo = new Listcell();
                        campo.setStyle("font-weight:bold; color:black");
                        campo.appendChild(new Label(meta.getCampo()));

                        Listcell dos_puntos = new Listcell();
                        dos_puntos.setStyle("font-weight:bold; color:black");
                        dos_puntos.appendChild(new Label(":"));

                        Listcell dato = new Listcell();
                        try {
                            dato.appendChild(new Label(meta.getDetalle()));
                        } catch (NullPointerException ex) {
                        }

                        listitem.appendChild(campo);
                        listitem.appendChild(dos_puntos);
                        listitem.appendChild(dato);
                        listMetadata.appendChild(listitem);
                    }

                    if (count > 0) {
                        divMetadata.setVisible(true);
                        //showNotify(item.getMetaData(), win, "info", "top_center");
                    }
                }
            } catch(NullPointerException ex){
            }
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
        } catch (Exception ex) {
            return false;
        }
    }

    @Listen("onClientInfo = #gridPublicacion")
    public void info(ClientInfoEvent evt) {
        int height = evt.getDesktopHeight();
        int width = evt.getDesktopWidth();
        showNotify(width + "", win, "info", "top_center");
    }

    @Listen("onOpen = #s1")
    public void clickS1() {
        if (!s1.isOpen()) {
            gridPublicacion.setWidth((getScrnWid() - 20) + "px");
            caja1.setStyle(getScrnWid() + "px");
        } else {

        }
    }

    @Listen("onClick = #visor")
    public void visor() {
        HashMap hm = new HashMap();
        hm.put("original", false);
        hm.put("id", selectItem.getId());
        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winVisor.zul", null, hm);
        window1.doModal();
    }

    @Listen("onClick = #visorOri")
    public void visorOri() {
        HashMap hm = new HashMap();
        hm.put("original", true);
        hm.put("id", selectItem.getId());
        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winVisor.zul", null, hm);
        window1.doModal();
    }

    //Edicion Titulo
    @Listen("onClick = #iconTitulo")
    public void editTitulo(Event evt) {
        evt.stopPropagation();
        if (labelTitulo.isVisible()) {
            showNotify("Vuelva Hacer Click en el Lápiz Para guardar!", win, "info", "top_right");
            labelTitulo.setVisible(false);
            if (selectItem.getTitulo() != null) {
                if (selectItem.getTituloMin() != null) {
                    tTitulo.setValue(selectItem.getTituloMin());
                } else {
                    tTitulo.setValue(selectItem.getTitulo());
                }
            } else {
                tTitulo.setValue(labelTitulo.getValue());
            }
            tTitulo.setVisible(true);
        } else {
            selectItem.setTituloMin(tTitulo.getValue());
            selectItem.setTitulo(tTitulo.getValue().toUpperCase());
            labelTitulo.setValue(tTitulo.getValue().toUpperCase());
            tTitulo.setVisible(false);
            labelTitulo.setVisible(true);

            new DocumentoDao().update(selectItem);
        }
    }

    @Listen("onBlur=#tTitulo")
    public void doTitulo() {
        selectItem.setTituloMin(tTitulo.getValue());
        selectItem.setTitulo(tTitulo.getValue().toUpperCase());
        labelTitulo.setValue(tTitulo.getValue().toUpperCase());
        tTitulo.setVisible(false);
        labelTitulo.setVisible(true);
        new DocumentoDao().update(selectItem);
    }

    //Edicion Codigo
    @Listen("onClick = #iconCodigo")
    public void editCodigo(Event evt) {
        evt.stopPropagation();
        if (labelCodigo.isVisible()) {
            showNotify("Vuelva Hacer Click en el Lápiz Para guardar!", win, "info", "top_right");
            labelCodigo.setVisible(false);
            if (selectItem.getDocuCodigo() != null) {
                tCodigo.setValue(selectItem.getDocuCodigo());
            } else {
                tCodigo.setValue(labelCodigo.getValue());
            }
            tCodigo.setVisible(true);
        } else {
            selectItem.setDocuCodigo(tCodigo.getValue());
            labelCodigo.setValue(tCodigo.getValue().toUpperCase());
            tCodigo.setVisible(false);
            labelCodigo.setVisible(true);

            new DocumentoDao().update(selectItem);
        }
    }

    @Listen("onBlur=#tCodigo")
    public void doCodigo() {
        selectItem.setDocuCodigo(tCodigo.getValue());
        labelCodigo.setValue(tCodigo.getValue().toUpperCase());
        tCodigo.setVisible(false);
        labelCodigo.setVisible(true);
        new DocumentoDao().update(selectItem);
    }
    
    //Edicion Resumen
    @Listen("onClick = #iconResumen")
    public void editResumen(Event evt) {
        evt.stopPropagation();
        if (labelResumen.isVisible()) {
            showNotify("Vuelva Hacer Click en el Lápiz Para guardar!", win, "info", "top_right");
            labelResumen.setVisible(false);
            if (selectItem.getResumen() != null) {
                if (selectItem.getResumenMin() != null) {
                    tResumen.setValue(selectItem.getResumenMin());
                } else {
                    tResumen.setValue(selectItem.getResumen());
                }
            } else {
                tResumen.setValue(labelResumen.getValue());
            }
            tResumen.setVisible(true);
        } else {
            selectItem.setResumenMin(tResumen.getValue());
            selectItem.setResumen(tResumen.getValue().toUpperCase());
            labelResumen.setValue(tResumen.getValue().toUpperCase());
            tResumen.setVisible(false);
            labelResumen.setVisible(true);

            new DocumentoDao().update(selectItem);
        }
    }

    @Listen("onBlur=#tResumen")
    public void doResumen() {
        selectItem.setResumenMin(tResumen.getValue());
        selectItem.setResumen(tResumen.getValue().toUpperCase());
        labelResumen.setValue(tResumen.getValue().toUpperCase());
        tResumen.setVisible(false);
        labelResumen.setVisible(true);
        new DocumentoDao().update(selectItem);
    }

    //Edicion Tipo documento
    @Listen("onClick = #iconTipoDocu")
    public void ediTipoDocu(Event evt) {
        evt.stopPropagation();
        if (labelTipoDoc.isVisible()) {
            showNotify("Seleccione un Tipo documento Para cambiarlo o vuelva a presionar en editar para Cancelar!", win, "info", "top_right");
            labelTipoDoc.setVisible(false);
            lTipoDocu.setVisible(true);

        } else {
            selectTipoDocu();
        }
    }

    @Listen("onSelect = #lTipoDocu")
    public void selectTipoDocu() {
        if (selectTipoDocu != lTipoDocu.getSelectedItem()) {
            if (selectItem.getMetaData() != null && !"".equals(selectItem.getMetaData())) {
                Messagebox.show(
                        "Si cambia de Tipo Documento perderá la Metadata, esta seguro de cambiarlo?", "Dialogo de Confirmación ",
                        Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION,
                        new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event evt) throws Exception {
                        if (evt.getName().equals("onOK")) {

                            HashMap hm = new HashMap();
                            hm.put("tidoId", (Integer) lTipoDocu.getSelectedItem().getValue());
                            final int tidoId = (Integer) lTipoDocu.getSelectedItem().getValue();
                            if (((List<Metadata>) new MetaDataDao().listarMetaData(hm)).isEmpty()) {
                                selectItem.setTidoId((Integer) lTipoDocu.getSelectedItem().getValue());
                                selectTipoDocu = lTipoDocu.getSelectedItem();
                                labelTipoDoc.setValue(lTipoDocu.getSelectedItem().getLabel());
                                labelTipoDoc.setVisible(true);
                                lTipoDocu.setVisible(false);
                                divMetadata.setVisible(false);
                                selectItem.setMetaData(null);
                                new DocumentoDao().update(selectItem);
                            } else {
                                Messagebox.show(
                                        "Este tipo documento necesita ingresar la metadata desea continuar?", "Dialogo de Confirmación ",
                                        Messagebox.OK | Messagebox.CANCEL,
                                        Messagebox.EXCLAMATION,
                                        new org.zkoss.zk.ui.event.EventListener() {
                                    @Override
                                    public void onEvent(Event evt) throws Exception {
                                        if (evt.getName().equals("onOK")) {
                                            HashMap hm = new HashMap();
                                            hm.put("tipoDocu", tidoId);
                                            List<Metadata> metaData = (List<Metadata>) new ArrayList<Metadata>();
                                            hm.put("metaData", metaData);
                                            hm.put("edit", true);
                                            Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winMetaData.zul", null, hm);
                                            window2.doModal();
                                        } else {
                                            lTipoDocu.setSelectedItem(selectTipoDocu);
                                        }
                                    }
                                });
                            }
                        } else {
                            lTipoDocu.setSelectedItem(selectTipoDocu);
                        }
                    }
                });
            } else {
                HashMap hm = new HashMap();
                hm.put("tidoId", (Integer) lTipoDocu.getSelectedItem().getValue());
                final int tidoId = (Integer) lTipoDocu.getSelectedItem().getValue();
                if (((List<Metadata>) new MetaDataDao().listarMetaData(hm)).isEmpty()) {
                    selectItem.setTidoId((Integer) lTipoDocu.getSelectedItem().getValue());
                    selectTipoDocu = lTipoDocu.getSelectedItem();
                    labelTipoDoc.setValue(lTipoDocu.getSelectedItem().getLabel());
                    labelTipoDoc.setVisible(true);
                    lTipoDocu.setVisible(false);
                    new DocumentoDao().update(selectItem);
                } else {
                    Messagebox.show(
                            "Este tipo documento necesita ingresar la metadata desea continuar?", "Dialogo de Confirmación ",
                            Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.EXCLAMATION,
                            new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event evt) throws Exception {
                            if (evt.getName().equals("onOK")) {
                                HashMap hm = new HashMap();
                                hm.put("tipoDocu", tidoId);
                                List<Metadata> metaData = (List<Metadata>) new ArrayList<Metadata>();
                                hm.put("metaData", metaData);
                                hm.put("edit", true);
                                Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winMetaData.zul", null, hm);
                                window2.doModal();
                            } else {
                                lTipoDocu.setSelectedItem(selectTipoDocu);
                            }
                        }
                    });
                }
            }
        } else {
            labelTipoDoc.setValue(lTipoDocu.getSelectedItem().getLabel());
            labelTipoDoc.setVisible(true);
            lTipoDocu.setVisible(false);
        }
    }

    @Listen("onBlur=#lTipoDocu")

    public void onBlurTipoDocu() {
        selectTipoDocu();
    }

    //Edicion de la Fecha del Documento
    //Edicion Titulo
    @Listen("onClick = #iconFecha")
    public void editFecha() {
        if (labelFechaDoc.isVisible()) {
            showNotify("Vuelva Hacer Click en el Lápiz Para guardar!", win, "info", "top_right");
            labelFechaDoc.setVisible(false);
            try {
                dFecha.setValue(new FormatoFecha().formatStringFechaCorta(selectItem.getFechaDocx()));
            } catch (NullPointerException ex) {
                dFecha.setValue(null);
            }
            dFecha.setVisible(true);
        } else {
            selectItem.setFechaDocx(new FormatoFecha().formatFechaCorta(dFecha.getValue()));
            labelFechaDoc.setValue(new FormatoFecha().formatFechaCorta(dFecha.getValue()));
            dFecha.setVisible(false);
            labelFechaDoc.setVisible(true);
            new DocumentoDao().update(selectItem);
        }
    }

    @Listen("onBlur=#dFecha")
    public void doFecha() {
        selectItem.setFechaDocx(new FormatoFecha().formatFechaCorta(dFecha.getValue()));
        labelFechaDoc.setValue(new FormatoFecha().formatFechaCorta(dFecha.getValue()));
        dFecha.setVisible(false);
        labelFechaDoc.setVisible(true);
        new DocumentoDao().update(selectItem);
    }

    //Edicion Categoria documento
    @Listen("onClick = #iconCate")
    public void editCateDocu(Event evt) {
        evt.stopPropagation();
        if (labelCate.isVisible()) {
            showNotify("Seleccione  una nueva Categoria Para cambiarlo  o vuelva a presionar en editar para Cancelar!", win, "info", "top_right");
            labelCate.setVisible(false);
            lCateDocu.setVisible(true);
        } else {
            selectCateDocu();
        }
    }

    @Listen("onSelect = #lCateDocu")
    public void selectCateDocu() {
        selectItem.setCateId((Integer) lCateDocu.getSelectedItem().getValue());
        selectCateDocu = lCateDocu.getSelectedItem();
        labelCate.setValue(lCateDocu.getSelectedItem().getLabel());
        labelCate.setVisible(true);
        lCateDocu.setVisible(false);
        new DocumentoDao().update(selectItem);
    }

    @Listen("onBlur=#lCateDocu")
    public void onBlurCateDocu() {
        selectCateDocu();
    }

    //Edicion de Etiquetas
    @Listen("onClick = #iconEtiqueta")
    public void editEtiqueta(Event evt) {
        evt.stopPropagation();
        HashMap hm = new HashMap();
        List<Listitem> items = new ArrayList<Listitem>();
        for (EtiquetaDocu item : selectItem.getEtiquetaDocu()) {
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
        hm.put("instance", "edit");
        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winEdiEtiqueta.zul", null, hm);
        window1.doModal();
    }

    public void dibujarTipoDocumento(Integer id) {
        List<TipoDocumento> items = (List<TipoDocumento>) new ArrayList<TipoDocumento>();
        items = new TipoDocumentoDao().listarTipoDocumento();
        lTipoDocu.clearSelection();
        lTipoDocu.getItems().clear();
        int select = 0;
        int index = 0;
        for (TipoDocumento item : items) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getId());
            listitem.setLabel(item.getNombre());
            if (Objects.equals(id, item.getId())) {
                select = index;
                selectTipoDocu = listitem;
            }
            index++;
            lTipoDocu.appendChild(listitem);
        }
        lTipoDocu.setSelectedIndex(select);
    }

    public void dibujarCategoria(Integer id) {
        List<Categoria> items = (List<Categoria>) new ArrayList<Categoria>();

        items = new CategoriaDao().listarCategorias();
        lCateDocu.clearSelection();
        lCateDocu.getItems().clear();
        int select = 0;
        int index = 0;
        for (Categoria item : items) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getCateId());
            listitem.setLabel(item.getCateNombre());
            if (Objects.equals(id, item.getCateId())) {
                select = index;
                selectCateDocu = listitem;
            }
            index++;
            lCateDocu.appendChild(listitem);
        }
        lCateDocu.setSelectedIndex(select);

    }

    //metodo modificar Etiquetas
    public void modificarEtiquetas(List<Listitem> items) {
        new EtiquetaDocuDao().delete(selectItem.getId());
        for (Listitem eti : items) {
            DocumentoEtiqueta de = new DocumentoEtiqueta();
            de.setDocuId(selectItem.getId());
            de.setEtiqId(Integer.parseInt(eti.getId()));
            new EtiquetaDocuDao().insert(de);
        }
        cellEtiqueta.getChildren().clear();
        selectItem = new DocumentoDao().getOne(selectItem.getId());
        if (!selectItem.getEtiquetaDocu().isEmpty()) {
            for (int i = 0; i < selectItem.getEtiquetaDocu().size(); i++) {
                Label label = new Label();
                label.setValue(selectItem.getEtiquetaDocu().get(i).getNombre());
                label.setSclass("label label-warning arrowed arrowed-in-right");
                cellEtiqueta.appendChild(label);
            }
        } else {
            labelEtiqueta.setValue("No tiene");
        }
    }

    @Listen("onClick = #iconMetadata")
    public void iconMetadata(Event evt) {
        evt.stopPropagation();
        HashMap hm = new HashMap();
        selectItem = new DocumentoDao().getOne(selectItem.getId());
        //showNotify(new MetaDataDao().getMetadata(selectItem)+"", win, "info", "top-center");
        hm.put("tipoDocu", this.selectItem.getTidoId());
        hm.put("metaData", new MetaDataDao().getMetadata(selectItem));
        hm.put("edit", true);
        Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winMetaData.zul", null, hm);
        window2.doModal();
    }

    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

    public static Integer getScrnWid() {
        // Get the default toolkit

        Integer strWidth = (Integer) Executions.getCurrent().getDesktop().getSession().getAttribute("width");
        return strWidth;
    }

    public Integer porcentaje(int num, Double porcentaje) {
        Double perc = num * porcentaje;
        return perc.intValue();
    }

    @Listen("onClick = #iconRelacion, #expediente")
    public void winVer() {
        HashMap hm = new HashMap();
        selectItem = new DocumentoDao().getOne(selectItem.getId());
        hm.put("documento", selectItem);
        Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winVer.zul", null, hm);
        window2.doModal();
    }

    @Listen("onClick=#firma")
    public void firma() throws IOException {
        HashMap hm = new HashMap();
        String path = new ConexionReporte().obtenerCarpeta("carpeta");
        hm.put("tmp", path + selectItem.getRuta() + "//" + selectItem.getId() + ".pdf");
        Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winFirma.zul", null, hm);
        window2.doModal();
    }
}
