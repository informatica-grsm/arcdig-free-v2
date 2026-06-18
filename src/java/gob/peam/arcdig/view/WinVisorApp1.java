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
public class WinVisorApp1 extends SelectorComposer<Component> {

    public WinVisorApp1() {
    }

    @Wire
    Iframe iframe;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();

        iframe.setSrc("./AppReport1.pdf?format=pdf");

    }

}
