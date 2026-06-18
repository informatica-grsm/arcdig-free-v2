/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

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
public class WinVisor extends SelectorComposer<Component> {

    public WinVisor() {
    }

    @Wire
    Iframe iframe;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();
        int id = (Integer) exec.getArg().get("id");
        boolean original = (Boolean) exec.getArg().get("original");

        if (!original) {
            iframe.setSrc("./ArcDig.pdf?id=" + id);
        } else {
            iframe.setSrc("./OriArc.pdf?id=" + id);
        }

    }

}
