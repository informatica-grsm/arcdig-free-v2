package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Catalogo;
import gob.peam.arcdig.beans.CatalogoUsuario;
import gob.peam.arcdig.dao.CatalogoDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class AppCatalogo extends SelectorComposer<Component> {

    public AppCatalogo() {
    }
    List<Catalogo> items = (List<Catalogo>) new ArrayList<Catalogo>();
    private Catalogo SelectItem;
    private Boolean editar = false;
    private EventQueue eq;
    private String rutaSave;
    @Wire
    A rutear;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dibujarCatalogo();

        eq = EventQueues.lookup("dataCatalogo", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                rutaSave = (String) hm.get("rutaSave");
                rutear.getChildren().clear();
                rutear.appendChild(new Label(" " + rutaSave));
            }
        });

        eq = EventQueues.lookup("dataPersona", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                List<Listitem> personas = (List<Listitem>) hm.get("personas");
                guardarPersonas(personas);
            }
        });

    }

    @Wire
    Button aceptar;
    @Wire
    Bandbox filtro;
    @Wire
    Listbox listCatalogo;
    @Wire
    Grid formulario;
    @Wire
    Hlayout control;
    @Wire
    Textbox nombre, descripcion;
    @Wire
    Label ruta, nombError, desError, rutError;
    @Wire
    Checkbox estado;

    public void dibujarCatalogo() {
        items = new CatalogoDao().listarCatalogo(filtro.getValue());
        listCatalogo.getItems().clear();
        int index = 0;

        for (Catalogo item : items) {
            index++;
            Listitem listitem = new Listitem();
            Listcell check = new Listcell();
            check.appendChild(new Label(""));
            Listcell nro = new Listcell();
            nro.appendChild(new Label(String.valueOf(index)));
            Listcell nombre = new Listcell();
            nombre.appendChild(new Label(item.getCataNombre()));
            Listcell descripcion = new Listcell();
            descripcion.appendChild(new Label(item.getCataDescripcion()));

            Listcell carpeta = new Listcell();
            carpeta.appendChild(new Label(item.getCataCarpeta()));

            Listcell estado = new Listcell();
            Image imgEstado = new Image();
            imgEstado.setSrc("./resources/img/" + item.getCataEstado() + ".png");
            imgEstado.setStyle("border:0px");
            estado.appendChild(imgEstado);
            listitem.appendChild(check);
            listitem.appendChild(nro);
            listitem.appendChild(nombre);
            listitem.appendChild(descripcion);
            listitem.appendChild(carpeta);
            listitem.appendChild(estado);

            listCatalogo.appendChild(listitem);
        }
    }

    @Listen("onSelect = #listCatalogo")
    public void select() {
        Set item = listCatalogo.getSelectedItems();
        List<Listitem> emp = new ArrayList<Listitem>(item);
        for (int i = 0; i < emp.size(); i++) {
            SelectItem = items.get(emp.get(i).getIndex());
        }
    }

    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 4000);
    }

    @Listen("onClick = #clear")
    public void clear() {
        nombre.setValue("");
        descripcion.setValue("");
        rutear.getChildren().clear();
        estado.setChecked(false);
    }

    @Listen("onClick = #cancelar")
    public void cancelar() {
        listCatalogo.setVisible(true);
        formulario.setVisible(false);
        control.setVisible(false);
    }

    @Listen("onClick=#aceptar")
    public void aceptar() {
        desError.setValue("");
        nombError.setValue("");
        rutError.setValue("");
        if (!"".equals(nombre.getValue())) {
            if (rutaSave != null) {
                Catalogo bean = new Catalogo();
                bean.setCataNombre(nombre.getValue().toUpperCase());
                bean.setCataDescripcion(descripcion.getValue().toUpperCase());
                bean.setCataCarpeta(rutaSave.replace("/", ""));

                bean.setCataEstado(estado.isChecked());
                try {
                    bean.setCataId(SelectItem.getCataId());
                } catch (NullPointerException ex) {

                }
                Integer i = 0;
                if (editar) {
                    bean.setCataId(SelectItem.getCataId());
                    i = new CatalogoDao().getRepetidoUpdate(bean);
                } else {
                    i = new CatalogoDao().getRepetido(bean);
                }
                if (i == 0) {
                    if (editar) {
                        //bean.setCataId(SelectItem.getCataId());
                        new CatalogoDao().update(bean);
                    } else {
                        new CatalogoDao().insert(bean);
                    }
                    listCatalogo.setVisible(true);
                    formulario.setVisible(false);
                    control.setVisible(false);
                    dibujarCatalogo();
                } else {
                    nombError.setValue("El nombre se esta repitiendo con otro registro");
                }
            } else {
                rutError.setValue("Elija una carpeta como respositorio porfavor.");
            }
        } else {
            nombError.setValue("El nombre no puede dejarse en blanco");
        }
    }

    @Listen("onClick = #editar")
    public void editar() {
        desError.setValue("");
        nombError.setValue("");
        rutError.setValue("");
        if (SelectItem != null) {
            listCatalogo.setVisible(false);
            formulario.setVisible(true);
            control.setVisible(true);
            nombre.setValue(SelectItem.getCataNombre());
            descripcion.setValue(SelectItem.getCataDescripcion());
            rutear.getChildren().clear();
            rutear.appendChild(new Label(SelectItem.getCataCarpeta() == null ? "" : " " + SelectItem.getCataCarpeta()));
            rutaSave = SelectItem.getCataCarpeta() == null ? "" : SelectItem.getCataCarpeta();
            estado.setChecked(SelectItem.getCataEstado());
            editar = true;
        } else {
            showNotify("Porfavor seleccione un item para Editar", win, "warning", "top_right");
        }

    }

    @Listen("onClick = #nuevo")
    public void nuevo() {
        listCatalogo.setVisible(false);
        formulario.setVisible(true);
        control.setVisible(true);
        nombre.setValue("");
        descripcion.setValue("");
        rutear.getChildren().clear();
        estado.setChecked(true);
        editar = false;
    }

    @Listen("onClick = #eliminar")
    public void eliminar() {
        if (SelectItem != null) {
            Messagebox.show(
                    "Desea eliminar este Catalogo?", "Dialogo de Confirmación ",
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event evt) throws Exception {
                    if (evt.getName().equals("onOK")) {
                        new CatalogoDao().delete(SelectItem);
                        dibujarCatalogo();
                    }
                }
            });
        } else {
            showNotify("Porfavor seleccione un item para Eliminar", win, "warning", "top_right");
        }
    }

    @Listen("onClick = #rutear")
    public void catalogar() {
        boolean flag = true;
        HashMap hm = new HashMap();
        hm.put("catalogar", flag);
        Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winChooseCatalogo.zul", null, hm);
        window2.doModal();
    }

    @Listen("onClick = #update")
    public void actualizar() {
        dibujarCatalogo();
    }

    @Listen("onChange = #filtro")
    public void buscar() {
        dibujarCatalogo();
    }

    @Listen("onClick = #acceso")
    public void config() {
        if (SelectItem != null) {
            HashMap hm = new HashMap();
            List<CatalogoUsuario> listCataUsua = (List<CatalogoUsuario>) new ArrayList<CatalogoUsuario>();
            listCataUsua = new CatalogoDao().listarCatalogoUsuario(SelectItem);
            List<Listitem> personas = new ArrayList<Listitem>();

            for (CatalogoUsuario cu : listCataUsua) {
                Listitem li = new Listitem();
                li.setId(cu.getPersId().toString());
                personas.add(li);
            }

            hm.put("personas", personas);
            hm.put("propiedad", false);

            Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winPersona.zul", null, hm);
            window1.doModal();
        } else {
            showNotify("Porfavor seleccione un item para Elegir quienes pueden acceder a este Catalogo!", win, "warning", "top_right");
        }
    }

    public void guardarPersonas(List<Listitem> personas) {
        //showNotify("hola", win,"info", "top_center");
        new CatalogoDao().deleteUsuario(SelectItem);
        for (Listitem p : personas) {

            CatalogoUsuario bean = new CatalogoUsuario();
            bean.setCataId(SelectItem.getCataId());

            bean.setPersId(Integer.parseInt(p.getId()));
            new CatalogoDao().insertUsuario(bean);
        }
    }

}
