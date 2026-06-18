package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Metadata;
import gob.peam.arcdig.beans.TipoDocumento;
import gob.peam.arcdig.beans.TipoMetadata;
import gob.peam.arcdig.dao.MetaDataDao;
import gob.peam.arcdig.dao.TipoDocumentoDao;
import gob.peam.arcdig.utils.FormatoFecha;

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
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class WinMetaData extends SelectorComposer<Component> {

    private static final long serialVersionUID = 8243942703081449079L;
    private List<Metadata> metaData = (List<Metadata>) new ArrayList<Metadata>();

    private EventQueue eq;
    @Wire
    Listbox tipoDocu;
    @Wire
    Window winMetaData;
    @Wire
    Button ok, ver;
    @Wire
    Rows rows;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        Integer tipoId = (Integer) exec.getArg().get("tipoDocu");
        metaData = (List<Metadata>) exec.getArg().get("metaData");
        dibujarTipoDocumento(tipoId);
    }

    @Listen("onClick = #cancel")
    public void cancel() {
        winMetaData.detach();
    }

    @Listen("onClick = #ok")
    public void ok() {
        HashMap hm = new HashMap();
        hm.put("metaData", metaData);
        hm.put("tipoDocu", tipoDocu.getSelectedItem().getValue());
        winMetaData.detach();
        eq = EventQueues.lookup("editMetadata", EventQueues.DESKTOP, false);
        eq.publish(new Event("", null, hm));

    }

    public void dibujarTipoDocumento(Integer tipoId) {
        List<TipoDocumento> items = (List<TipoDocumento>) new ArrayList<TipoDocumento>();

        items = new TipoDocumentoDao().listarTipoDocumento();
        tipoDocu.getItems().clear();
        for (TipoDocumento item : items) {
            Listitem listitem = new Listitem();
            listitem.setValue(item.getId());
            listitem.setLabel(item.getNombre());
            if (tipoId == item.getId()) {
                listitem.setSelected(true);
            }
            tipoDocu.appendChild(listitem);

        }
        dibujarMetadata(tipoId);

    }

    @Listen("onSelect = #tipoDocu")
    public void selectTipoDocu() {
        metaData.clear();
        dibujarMetadata((Integer) tipoDocu.getSelectedItem().getValue());
    }

    public void dibujarMetadata(Integer tidoId) {
        List<Metadata> items = (List<Metadata>) new ArrayList<Metadata>();
        HashMap hm = new HashMap();
        hm.put("tidoId", tidoId);
        items = new MetaDataDao().listarCamposTipoDoc(hm);

        if (metaData.isEmpty()) {
            metaData = items;
        }

        rows.getChildren().clear();

        boolean flag = true;
        for (Metadata itex : items) {

            for (Metadata item : metaData) {
                if (item.getCampo().equals(itex.getCampo())) {
                    flag = true;
                    break;
                } else {
                    flag = false;
                }
            }

            if (!flag) {
                hm.put("id", itex.getId());
                TipoMetadata tipo = new MetaDataDao().getTipoDato(hm);
                Metadata m = new Metadata();
                m.setId(itex.getId());
                m.setCampo(itex.getCampo());
                m.setDetalle(tipo.getDefecto() == null ? "" : tipo.getDefecto());
                metaData.add(m);
            }
        }

        for (Metadata item : metaData) {
            hm.put("id", item.getId());
            TipoMetadata tipo = new MetaDataDao().getTipoDato(hm);
            Row row = new Row();
            Label label = new Label();
            label.setValue(item.getCampo());
            final Textbox tb = new Textbox();

            final Datebox db = new Datebox();
            db.setFormat("dd/MM/yy");
            db.setReadonly(false);

            if (!metaData.isEmpty()) {
                for (int i = 0; i < metaData.size(); i++) {
                    if (item.getCampo().equals(metaData.get(i).getCampo())) {
                        if (tipo.getTipoDato().equals("date")) {
                            db.setValue(new FormatoFecha().formatStringFechaCorta(metaData.get(i).getDetalle()));
                        } else if (tipo.getTipoDato().equals("string")) {
                            tb.setValue(metaData.get(i).getDetalle());
                        } else {
                            tb.setRows(4);
                            tb.setCols(180);
                            tb.setWidth("100%");
                            tb.setValue(metaData.get(i).getDetalle());
                        }
                        break;
                    }
                }
            }
            if (tipo.getTipoDato().equals("date")) {
                db.addEventListener(Events.ON_CHANGE, new gob.peam.arcdig.view.WinMetaData.changeTextbox(item));
            } else {
                tb.addEventListener(Events.ON_CHANGING, new gob.peam.arcdig.view.WinMetaData.changeTextbox(item));
            }
            tb.setWidth("100%");
            tb.setPlaceholder("Escribir Texto");

            row.appendChild(label);
            if (tipo.getTipoDato().equals("date")) {
                row.appendChild(db);
            } else {
                row.appendChild(tb);
            }
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
            boolean flag = false;
            for (int i = 0; i < metaData.size(); i++) {
                if (item.getCampo().equals(metaData.get(i).getCampo())) {
                    metaData.get(i).setDetalle(event.getValue());
                    break;
                }
            }
        }
    }
    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

}
