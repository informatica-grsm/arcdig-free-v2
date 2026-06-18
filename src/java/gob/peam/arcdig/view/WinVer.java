/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import com.qoppa.pdf.PDFException;
import com.qoppa.pdfSecure.PDFSecure;
import config.ConexionReporte;
import gob.peam.arcdig.beans.Documento;
import gob.peam.arcdig.beans.Persona;
import gob.peam.arcdig.dao.DocumentoDao;
import gob.peam.arcdig.dao.PermisoDao;
import gob.peam.arcdig.dao.PersonaDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author alabajos
 */
public class WinVer extends SelectorComposer<Component> {

    public WinVer() {
    }

    @Wire
    Iframe iframe;

    @Wire
    Label lTipoDocumento, lFecha, lCategoria, lTitulo, lResumen, lSubido, lRuta;
    List<Documento> array = (List<Documento>) new ArrayList<Documento>();
    List<Documento> array1 = (List<Documento>) new ArrayList<Documento>();
    List<Documento> array2 = (List<Documento>) new ArrayList<Documento>();
    Documento selected;
    @Wire
    A lDigital;

    @Wire
    Window dialog;

    @Wire
    Iframe viewFrame;

    @Wire
    Textbox txtGrupo, txtGrupo1, txtGrupo2;

    @Wire
    Listbox list;
    Boolean tres = true;

    Boolean diez = true;
    Integer grupo;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        Documento docu = (Documento) exec.getArg().get("documento");
        lDigital.setHref("/Downloads.pdf?id=" + docu.getDocuGroup());
        grupo = docu.getDocuGroup();

        gob.peam.arcdig.beans.Permiso perm = new gob.peam.arcdig.beans.Permiso();
        String dni = "";
        try {
            dni = Executions.getCurrent().getDesktop().getSession().getAttribute("dni").toString();
            //idPers = Integer.parseInt(Executions.getCurrent().getDesktop().getSession().getAttribute("usuaId").toString());
            perm = new PermisoDao().getPermisos(dni);
            tres = !perm.getItem3();
            diez = !perm.getItem10();

        } catch (NullPointerException ex) {
            tres = false;
            diez = false;

        }
        if (diez) {
            add.setVisible(true);
        } else {
            add.setVisible(false);
        }
        dialog.setTitle("Vista detallada del trámite seleccionado: " + lTipoDocumento.getValue());

        mostrarAdjunto(docu.getDocuGroup());
        if (!"".equals(docu.getRuta())) {
            viewFrame.setSrc("./ArcDig.pdf?id=" + selected.getId());
        }
    }

    public void mostrarAdjunto(Integer group) throws IOException, PDFException {
        list.getItems().clear();
        array = new DocumentoDao().getAdjuntos(group);
        int index = 0;
        for (Documento item : array) {

            Listitem listitem = new Listitem();
            listitem.setWidgetAttribute("index", index + "");
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(item.getTitulo()));

            Listcell id = new Listcell();
            id.appendChild(new Label(item.getId() + ""));

            Listcell cell1 = new Listcell();
            cell1.appendChild(new Label(item.getResumen()));

            Listcell cell2 = new Listcell();
            cell2.appendChild(new Label(item.getTipoDocumento().getNombre()));

            Listcell cell3 = new Listcell();
            cell3.appendChild(new Label(item.getCategoria().getCateNombre()));

            Toolbarbutton botonVer = new Toolbarbutton();
            final Documento item1 = item;
            final Integer group1 = group;

            botonVer.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    Messagebox.show(
                            "Advertencia esta apunto de retirar este documento del grupo de este Expediente?", "Dialogo de Confirmación ",
                            Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.EXCLAMATION,
                            new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event evt) throws Exception {
                            if (evt.getName().equals("onOK")) {
                                new DocumentoDao().removeGroup(item1.getId());
                                mostrarAdjunto(group1);
                            } else {
                                //lTipoDocu.setSelectedItem(selectTipoDocu);
                            }
                        }
                    });

                }
            });

            Toolbarbutton botonCero = new Toolbarbutton();
            String width = "20px";
            String height = "20px";
            botonCero.setIconSclass("z-icon-leaf");
            botonCero.setWidth(width);
            botonCero.setHeight(height);
            botonCero.setStyle("color: #38D138");
            botonCero.setTooltiptext("Verificar Firma(s) Digital(es)");
            String path = new ConexionReporte().obtenerCarpeta("carpeta");
            Listcell cell_ = new Listcell();
            if (verificarFirma(path + item.getRuta() + "/" + item.getId() + ".pdf")) {
                cell_.appendChild(botonCero);
            } else {
                cell_.appendChild(new Label(""));
            }

            botonCero.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    HashMap hm = new HashMap();
                    String path = new ConexionReporte().obtenerCarpeta("carpeta");
                    hm.put("tmp", path + item1.getRuta() + "//" + item1.getId() + ".pdf");
                    Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winFirma.zul", null, hm);
                    window2.doModal();
                }
            });

            botonVer.setIconSclass("z-icon-minus");
            botonVer.setWidth(width);
            botonVer.setHeight(height);
            botonVer.setTooltiptext("Remover del grupo");
            Listcell cell4 = new Listcell();
            Listcell cell5 = new Listcell();
            Listcell cell6 = new Listcell();
            Listcell cell7 = new Listcell();

            Toolbarbutton botonUp = new Toolbarbutton();
            Toolbarbutton botonDown = new Toolbarbutton();
            Toolbarbutton botonSolo = new Toolbarbutton();
            Toolbarbutton botonSolo1 = new Toolbarbutton();
            Toolbarbutton botonSolo2 = new Toolbarbutton();

            botonUp.setIconSclass("z-icon-arrow-up");
            botonUp.setWidth("16px");
            botonUp.setHeight("16px");

            botonUp.setTooltiptext("Subir Posición");

            botonDown.setIconSclass("z-icon-arrow-down");
            botonDown.setWidth("16px");
            botonDown.setHeight("16px");
            index++;

            try {
                botonSolo.setLabel(item.getSubGrupo() + "");
            } catch (NullPointerException ex) {
            }

            botonSolo.setWidth("16px");
            botonSolo.setHeight("16px");

            try {
                botonSolo1.setLabel(item.getSubGrupo1() + "");
            } catch (NullPointerException ex) {
            }

            botonSolo1.setWidth("16px");
            botonSolo1.setHeight("16px");

            try {
                botonSolo2.setLabel(item.getSubGrupo2() + "");
            } catch (NullPointerException ex) {
            }

            botonSolo2.setWidth("16px");
            botonSolo2.setHeight("16px");

            if (diez) {
                cell4.appendChild(botonVer);
                cell5.appendChild(botonSolo);
                cell6.appendChild(botonSolo1);
                cell7.appendChild(botonSolo2);
                if (array.size() > 1) {
                    if (index > 1) {
                        cell4.appendChild(botonUp);
                    }
                    if (array.size() != index) {
                        cell4.appendChild(botonDown);
                    }
                }

            }

            final int i = index - 1;
            botonDown.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    new DocumentoDao().anexoDown(array.get(i), array.get(i).getOrden() + 1);
                    new DocumentoDao().anexoUp(array.get(i + 1), array.get(i + 1).getOrden() - 1);
                    mostrarAdjunto(group1);
                }
            });

            botonUp.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    new DocumentoDao().anexoDown(array.get(i), array.get(i).getOrden() - 1);
                    new DocumentoDao().anexoUp(array.get(i - 1), array.get(i - 1).getOrden() + 1);
                    mostrarAdjunto(group1);
                }
            });

            botonSolo.addEventListener(Events.ON_DOUBLE_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    //new DocumentoDao().updateSolo(item.getId(), !item.getSolo());
                    popSubGrupo.open(event.getTarget());
                    try {
                        txtGrupo.setValue(array.get(i).getSubGrupo() + "");
                    } catch (NullPointerException ex) {
                        txtGrupo.setValue(array.get(i).getSubGrupo() + "");
                    }
                }
            });

            botonSolo1.addEventListener(Events.ON_DOUBLE_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    //new DocumentoDao().updateSolo(item.getId(), !item.getSolo());
                    popSubGrupo1.open(event.getTarget());
                    try {
                        txtGrupo1.setValue(array.get(i).getSubGrupo1() + "");
                    } catch (NullPointerException ex) {
                        txtGrupo1.setValue(array.get(i).getSubGrupo1() + "");
                    }
                }
            });

            botonSolo2.addEventListener(Events.ON_DOUBLE_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event event) throws Exception {
                    //new DocumentoDao().updateSolo(item.getId(), !item.getSolo());
                    popSubGrupo2.open(event.getTarget());
                    try {
                        txtGrupo2.setValue(array.get(i).getSubGrupo2() + "");
                    } catch (NullPointerException ex) {
                        txtGrupo2.setValue(array.get(i).getSubGrupo2() + "");
                    }
                }
            });

            listitem.appendChild(check);
            listitem.appendChild(id);
            listitem.appendChild(cell_);
            listitem.appendChild(nro);
            listitem.appendChild(cell1);
            listitem.appendChild(cell2);

            listitem.appendChild(cell3);
            listitem.appendChild(cell4);
            listitem.appendChild(cell5);
            listitem.appendChild(cell6);
            listitem.appendChild(cell7);
            list.appendChild(listitem);
        }

        list.setSelectedIndex(0);
        select();

        //String ruta = rootPath + "unionPdfs.pdf";
    }

    public boolean verificarFirma(String ruta1) throws PDFException {
        try {
            PDFSecure pdfDoc = new PDFSecure(ruta1, null);
            if (pdfDoc.getSignatureFields() != null && pdfDoc.getSignatureFields().size() >= 1) {
                return true;
            } else {
                return false;
            }
        } catch (PDFException ex) {
            return false;
        }
    }

    @Listen("onSelect = #list")
    public void select() throws IOException {
        Set item = list.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            selected = array.get(emp.get(i).getIndex());
        }
        if (selected != null) {
            Documento docu = new DocumentoDao().getOne(selected.getId());
            lTipoDocumento.setValue(docu.getTipoDocumento().getNombre());
            lFecha.setValue(docu.getFechaDocx());

            lCategoria.setValue(docu.getCategoria().getCateNombre());
            lDigital.setIconSclass("z-icon-download");
            lDigital.setStyle("font-size:14pt");
            lTitulo.setValue(docu.getTitulo());
            lResumen.setValue(docu.getResumenMin());
            lRuta.setValue(docu.getRuta());
            Persona pers = new PersonaDao().buscarPersonaUsuario(docu.getUsuaId());
            lSubido.setValue(pers.getNombre() + " " + pers.getApellidoPaterno() + " " + pers.getApellidoMaterno());

            //String path = new ConexionReporte().obtenerCarpeta("carpeta");
            viewFrame.setSrc("./ArcDig.pdf?id=" + selected.getId());
        }
    }

    @Listen("onClick = #cerrar")
    public void cerrar() {
        dialog.detach();
    }

    @Wire
    Popup popAsociar, popSubGrupo, popSubGrupo1, popSubGrupo2;

    @Wire
    Button add;

    @Wire
    Textbox txtAsociar;

    @Listen("onClick = #add")
    public void asociar() {
        popAsociar.open(add);
    }

    @Listen(Events.ON_OK + " = #txtAsociar")
    public void OnOkbtnAsociar() {
        btnAsociar();
    }

    @Listen("onClick =#btnAgrupar")
    public void btnAgrupar() {
        Integer sub = Integer.parseInt(txtGrupo.getValue());

        Documento doc = selected;

        new DocumentoDao().updateSubgrupo(doc.getId(), sub);
        popSubGrupo.close();

        Listitem item = list.getSelectedItem();

        Listcell cell = (Listcell) item.getChildren().get(8);

        Toolbarbutton boton = (Toolbarbutton) cell.getChildren().get(0);
        boton.setLabel(txtGrupo.getValue());

        int index = Integer.parseInt(item.getWidgetAttribute("index"));

        array.get(index).setSubGrupo(sub);
    }

    @Listen("onClick =#btnAgrupar1")
    public void btnAgrupar1() {
        Integer sub = Integer.parseInt(txtGrupo1.getValue());

        Documento doc = selected;

        new DocumentoDao().updateSubgrupo1(doc.getId(), sub);
        popSubGrupo1.close();
        Listitem item = list.getSelectedItem();

        Listcell cell = (Listcell) item.getChildren().get(9);

        Toolbarbutton boton = (Toolbarbutton) cell.getChildren().get(0);
        boton.setLabel(txtGrupo1.getValue());

        int index = Integer.parseInt(item.getWidgetAttribute("index"));

        array.get(index).setSubGrupo1(sub);

    }

    @Listen("onClick =#btnAgrupar2")
    public void btnAgrupar2() {

        Integer sub = Integer.parseInt(txtGrupo2.getValue());

        Documento doc = selected;

        new DocumentoDao().updateSubgrupo2(doc.getId(), sub);
        popSubGrupo2.close();
        
        Listitem item = list.getSelectedItem();

        Listcell cell = (Listcell) item.getChildren().get(10);

        Toolbarbutton boton = (Toolbarbutton) cell.getChildren().get(0);
        boton.setLabel(txtGrupo2.getValue());

        int index = Integer.parseInt(item.getWidgetAttribute("index"));

        array.get(index).setSubGrupo2(sub);

    }

    @Listen("onClick =#btnAsociar")
    public void btnAsociar() {
        Documento doc = new Documento();
        try {
            doc = new DocumentoDao().getDocumento(Integer.parseInt(txtAsociar.getValue().trim()));
        } catch (NullPointerException ex) {
            doc = null;
        }

        if (doc != null) {
            final Documento bean = doc;
            Messagebox.show(
                    "SE ASOCIARA EL DOCUMENTO: " + doc.getTitulo() + " a este expediente", "Aviso! ",
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.INFORMATION,
                    new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event evt) throws Exception {
                    if (evt.getName().equals("onOK")) {
                        new DocumentoDao().adjuntar(bean.getId(), grupo);
                        txtAsociar.setValue("");
                        mostrarAdjunto(grupo);
                    }
                }
            }
            );
        } else {
            showNotify("Este documento ingresado no existe", win, "warning", "middle_center");

        }
    }

    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

}
