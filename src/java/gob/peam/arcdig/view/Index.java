/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;


import javax.servlet.http.HttpSession;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.Window;

/**
 *
 * @author Cj.Legacy
 */
public class Index extends SelectorComposer<Component> {

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        HttpSession session = (HttpSession) Executions.getCurrent().getDesktop().getSession().getNativeSession();
        String usua = (String) session.getAttribute("user");
        boolean flag = true;
        if (usua != null) {
            flag = false;
            Executions.sendRedirect("./zul?url=inicio");
        } else {
            Window window = (Window) Executions.createComponents("./resources/zkWindow/winLogin.zul", null, null);
            window.doModal();
        }

    }
}
