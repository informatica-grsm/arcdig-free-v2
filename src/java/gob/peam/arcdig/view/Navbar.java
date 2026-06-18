package gob.peam.arcdig.view;

import java.util.HashMap;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Span;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

public class Navbar extends SelectorComposer<Component> {

    @Wire
    A atask, anoti, amsg, countNoti;
    @Wire
    Menu nombre;
    @Wire
    Vlayout bandeja;
    //private SqlSessionFactory sqlSessionFactory;

    public Navbar() {
        // sqlSessionFactory = AdministracionConnectionFactory.getSqlSessionFactory();
    }

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //cargarUsuario();
        //cargarNotificacion();
    }

    class revisado implements EventListener {

        String tabla;

        public revisado(String tabla) {
            this.tabla = tabla;
        }

        public void onEvent(Event event) throws Exception {
            //  SqlSession sqlSession = sqlSessionFactory.openSession();
            HashMap hm = new HashMap();
            hm.put("idP", (String) Executions.getCurrent().getDesktop().getSession().getAttribute("idGlobal"));
            hm.put("tabla", tabla);
            try {
                // sqlSession.update("Notify.updateVerify", hm);
                // sqlSession.commit();

            } finally {
                //    sqlSession.close();

            }

        }
    }

    public void cargarNotificacion() {
///        List<Notify> noti = this.getGrupoList();

        Integer contador = 0;
        /*    for (Notify n : noti) {

         A item = new A();
         item.setIconSclass("btn btn-xs no-hover btn-pink z-icon-envelope");
         /*switch (n.getTabla()) {
         case "P":
         item.appendChild(new Label("SOBRE PERMISOS "));
         item.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.revisado("P"));

         break;
         case "V":
         item.appendChild(new Label("SOBRE VACACIONES "));
         item.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.Navbar.revisado("V"));

         break;
         case "L":
         item.appendChild(new Label("SOBRE LICENCIAS "));
         item.addEventListener(Events.ON_CLICK, new gob.peam.arcdig.view.Navbar.revisado("L"));

         break;
         }*/
 /* Label lab = new Label();
         lab.setValue("+" + n.getCantidad());
         lab.setClass("badge badge-info pull-right");
         item.appendChild(lab);
         bandeja.appendChild(item);
         contador += n.getCantidad();*/
        // }
        A see = new A();
        //see.setLabel("Ver todas ");
        //see.setHref("#");
        Span s = new Span();
        //s.setClass("z-icon-arrow-right");
        /*see.appendChild(s);
         bandeja.appendChild(see);
         Label nro = new Label();
         nro.setValue(contador + "");
         nro.setClass("badge badge-important");
         anoti.appendChild(nro);
         countNoti.setLabel("Notificaciones: " + contador);*/

    }

    /* private List<Notify> getGrupoList() {
     SqlSession sessionSQL = sqlSessionFactory.openSession();
     Execution exec = Executions.getCurrent();
     String idP = (String) exec.getDesktop().getSession().getAttribute("idGlobal");
     HashMap hm = new HashMap();
     hm.put("idP", idP);
     hm.put("date", new Date(new java.util.Date().getTime()));
     List<Notify> noti = null;
     try {
     noti = sessionSQL.selectList("Notify.getGrupoList", hm);
     } finally {
     sessionSQL.close();
     }
     return noti;
     }

     public void cargarUsuario() {
     String nombre = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("nombreUsuario");
     try {
     nombre = (nombre.split(",")[0]).split(" ")[0];
     this.nombre.setLabel("Bienvenido, " + nombre);
     } catch (NullPointerException ex) {
     }
     }

     @Listen("onOpen = #taskpp")
     public void toggleTaskPopup(OpenEvent event) {
     toggleOpenClass(event.isOpen(), atask);
     }

     @Listen("onOpen = #notipp")
     public void toggleNotiPopup(OpenEvent event) {
     toggleOpenClass(event.isOpen(), anoti);
     }

     @Listen("onOpen = #msgpp")
     public void toggleMsgPopup(OpenEvent event) {
     toggleOpenClass(event.isOpen(), amsg);
     }*/
    // Toggle open class to the component
    public void toggleOpenClass(Boolean open, Component component) {
        HtmlBasedComponent comp = (HtmlBasedComponent) component;
        String scls = comp.getSclass();
        if (open) {
            comp.setSclass(scls + " open");
        } else {
            comp.setSclass(scls.replace(" open", ""));
        }
    }

    @Listen("onClick= #oscinfor")
    public void oscinfor() {

        Window window2 = (Window) Executions.createComponents("/resources/zkWindow/winOscinfor.zul", null, null);
        window2.doModal();
    }

    @Listen("onClick = #salir")
    public void salir() {
        Executions.getCurrent().getDesktop().getSession().invalidate();
        Executions.getCurrent().sendRedirect("./");

    }
    @Wire
    private Window win;

    private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "warning", ref, "middle_center", 3000);
    }
}
