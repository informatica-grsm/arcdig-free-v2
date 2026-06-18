
package gob.peam.arcdig.view;

import config.ConexionReporte;
import gob.peam.arcdig.beans.Carpeta;
import gob.peam.arcdig.beans.Persona;
import gob.peam.arcdig.dao.CatalogoDao;
import gob.peam.arcdig.dao.PersonaDao;
import java.io.File;
import java.util.HashMap;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class WinCarpeta extends SelectorComposer<Component> {

    @Wire
    Window winCarpeta;
    private String ruta;
    @Wire
    Textbox nombre, propietario;
    @Wire
    Label nomError, lruta;
    String ts;
    @Wire
    Listbox tipo;
    Persona empleado;
    @Wire
    Button add;
    @Wire
    Row rowPropietario;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        String ruta = (String) exec.getArg().get("carpeta");
        String path = new ConexionReporte().obtenerCarpeta("carpeta");
        this.ruta = "";
        if (path.contains("\\")) {
            String auxPath = path + "\\" + ruta.replace("/", "\\");
            ts = "\\";
            this.ruta += "\\" + auxPath;
        } else {
            String auxPath = path + "/" + ruta.replace("\\", "/");
            ts = "/";
            this.ruta += "/" + auxPath;
        }
        //showNotify(this.ruta, win, "info", "top_middle");
        lruta.setValue(ruta);
        Carpeta carp = new CatalogoDao().getCarpeta(lruta.getValue());

        if (carp != null) {
            tipo.setSelectedIndex(1);
            //showNotify(carp.getPropietario(), win, "info", "top_middle");
            empleado = new PersonaDao().getPersona(carp.getPropietario());
            propietario.setValue(empleado.getNombre());
            rowPropietario.setVisible(true);
        }

        eq = EventQueues.lookup("dataEmpleado", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                HashMap hm = (HashMap) event.getData();
                empleado = (Persona) hm.get("empleado");
                propietario.setValue(empleado.getNombre());
            }
        });

    }

    private EventQueue eq;

    @Listen("onClick = #aceptar")
    public void aceptar() {
        nomError.setValue("");
        File f = new File(this.ruta + ts + remove1(nombre.getValue().toUpperCase()));
        //showNotify(this.ruta + "/" + nombre.getValue(), win, "info", "top_center");
        if (!"".equals(nombre.getValue().trim())) {
            if (f.isDirectory()) {
                nomError.setValue("Este nombre de carpeta ya Existe, no se puede crear");
            } else {
                //try {
                f.mkdir();

                try {
                    Carpeta carpeta = new Carpeta();

                    carpeta.setRuta(lruta.getValue() + "/" + remove1(nombre.getValue().toUpperCase()));
                    carpeta.setPropietario(empleado.getDni());
                    try {
                        carpeta.setTipo(Integer.parseInt(tipo.getSelectedItem().getValue().toString()));
                    } catch (NullPointerException ex) {
                        carpeta.setTipo(0);
                    }
                    // showNotify("hola", win, "info", "top_center");
                    new CatalogoDao().insertCarpeta(carpeta);
                } catch (NullPointerException ex) {
                    nomError.setValue(ex.getMessage());
                }
                
                eq = EventQueues.lookup("refresh", EventQueues.DESKTOP, false);
                eq.publish(new Event("", null, ""));
                winCarpeta.detach();
                    //eq.close();
                /*} catch (Exception ex) {
                 nomError.setValue("El nombre de la carpeta ya existe, o contiene caracteres raros");
                 }*/
            }
        } else {
            nomError.setValue("No deberia ser vacío");
        }
    }

    @Listen("onClick = #add")
    public void agregar() {
        Window window1 = (Window) Executions.createComponents("/resources/zkWindow/winPersonalSingle.zul", null, null);
        window1.doModal();
    }

    public static String remove1(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarón los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }//remove1

    @Listen("onClick = #cerrar")
    public void cancelar() {
        winCarpeta.detach();
    }

    @Listen("onSelect=#tipo")
    public void selectTipo() {
        if (!tipo.getSelectedItem().getValue().equals("1")) {
            rowPropietario.setVisible(true);
        } else {
            rowPropietario.setVisible(false);
        }
    }

    @Wire
    private Window win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }
}
