/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import javax.servlet.http.HttpSession;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Iframe;

/**
 *
 * @author alabajos
 */
public class WinVisorApp3 extends SelectorComposer<Component> {

    public WinVisorApp3() {
    }

    @Wire
    Iframe iframe;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        String par = (String) exec.getArg().get("query");
        HttpSession session = (HttpSession) Executions.getCurrent().getDesktop().getSession().getNativeSession();
        session.setAttribute("query", par);
        iframe.setSrc("./AppReport3.pdf?format=pdf");

    }

}
