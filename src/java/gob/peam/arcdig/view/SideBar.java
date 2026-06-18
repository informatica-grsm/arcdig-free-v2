package gob.peam.arcdig.view;

import gob.peam.arcdig.beans.Etiqueta;
import gob.peam.arcdig.beans.SubModulo;
import gob.peam.arcdig.dao.EtiquetaDao;
import java.util.HashMap;
import java.util.List;
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
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Window;

public class SideBar extends SelectorComposer<Component> {

    @Wire
    Hlayout main;
    @Wire
    Div sidebar;
    @Wire
    Menubar menubar;
    @Wire
    Menuitem firts;
    @Wire
    A toggler;
    private EventQueue eq;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        eq = EventQueues.lookup("indexSidebar", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                listarMenuPorUsuario();
            }
        });
        if (Executions.getCurrent().getDesktop().getSession().getAttribute("idUsuario") != null) {
            listarMenuPorUsuario();
        }
    }

    public void listarMenuPorUsuario() {
        EtiquetaDao etiqueta = new EtiquetaDao();
        List<Etiqueta> etiquetas = null;
        HashMap hm = new HashMap();
        hm.put("idUsuario", Executions.getCurrent().getDesktop().getSession().getAttribute("idUsuario"));
        etiquetas = etiqueta.listarMenusPorUsuarioyModulo(hm);

        if (!etiquetas.isEmpty()) {
            for (Etiqueta eti : etiquetas) {

                Menu menuEtiqueta = new Menu();
                menuEtiqueta.setWidth("180px");
                menuEtiqueta.setLabel(eti.getDescripcion());
                menuEtiqueta.setIconSclass("icon icon-white " + eti.getIcono());
                Menupopup menupopEtiqueta = new Menupopup();
                menupopEtiqueta.setSclass("navpp");
                if (!eti.getSubModulos().isEmpty()) {
                    for (SubModulo sub : eti.getSubModulos()) {
                        Menuitem menuItemSub = new Menuitem();
                        menuItemSub.setIconSclass("z-icon-angle-double-right");
                        menuItemSub.setSclass("sub-modu");
                        menuItemSub.setLabel(sub.getNombre());
                        menuItemSub.setHref(sub.getUrl() + "&id=" + sub.getIdSubModulo());
                        menupopEtiqueta.appendChild(menuItemSub);
                    }
                    menuEtiqueta.appendChild(menupopEtiqueta);
                }
                menubar.insertBefore(menuEtiqueta, firts);
            }

        }
    }

    //Evento Click en Enlaces de submodulos
    class enlaceSubModulo implements EventListener {

        SubModulo subModulo;

        public enlaceSubModulo(SubModulo subModulo) {
            this.subModulo = subModulo;
        }

        @Override
        public void onEvent(Event event) throws Exception {
            Executions.getCurrent().getDesktop().setAttribute("hola", "asd");
        }
    }
    // Toggle sidebar to collapse or expand

    @Listen("onClick = #toggler")
    public void toggleSidebarCollapsed() {
        /*if (navbar.()) {
         sidebar.setSclass("sidebar");
         navbar.setCollapsed(false);
         calitem.setTooltip("calpp, position=end_center, delay=0");
         toggler.setIconSclass("z-icon-angle-double-left");
         } else {
         sidebar.setSclass("sidebar sidebar-min");
         navbar.setCollapsed(true);
         calitem.setTooltip("");
         toggler.setIconSclass("z-icon-angle-double-right");
         }*/
        // Force the hlayout contains sidebar to recalculate its size
        Clients.resize(sidebar.getRoot().query("#main"));
    }
    @Wire
    private Window win;

    private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "warning", ref, "middle_center", 3000);
    }
}
