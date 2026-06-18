/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.view;

import com.qoppa.pdf.form.SignatureField;
import com.qoppa.pdfSecure.PDFSecure;
import gob.peam.arcdig.utils.FormatoFecha;
import java.io.InputStream;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;


/**
 *
 * @author alabajos
 */
public class WinFirma extends SelectorComposer<Component> {

    private EventQueue eq, eq1;
    Integer usuaId;
    Boolean flag = false;
    String tmp = "";
    
    @Wire
    Listbox nListFirmas;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution exec = Executions.getCurrent();

        
        tmp = (String) exec.getArg().get("tmp");
        
       
        listar();
    }

    InputStream archMedia;
    int nIndex1 = 0;

    public void listar() {
        try {
            PDFSecure pdfDoc = new PDFSecure(tmp, null);
            if (pdfDoc.getSignatureFields() != null && pdfDoc.getSignatureFields().size() >= 1) {

                // Signature Name / Signature Alias 
                for (int i = 0; i < pdfDoc.getSignatureFields().size(); i++) {
                    SignatureField signField = (SignatureField) pdfDoc.getSignatureFields().get(i);
                    Listitem listitem = new Listitem();
                    listitem.setWidgetAttribute("nId1", this.nIndex1 + "");
                    Integer n = this.nIndex1;
                    Integer n2 = this.nIndex1 = Integer.valueOf(this.nIndex1 + 1);

                    Listcell nro = new Listcell();
                    nro.appendChild((Component) new Label(String.valueOf(this.nIndex1)));
          
                    Listcell cel1 = new Listcell();
                    cel1.setStyle("text-align:left;");
                    cel1.appendChild((Component) new Label(signField.getSignName()));
                    
                    Listcell cel2 = new Listcell();
                    cel2.setStyle("text-align:left;");
                    cel2.appendChild((Component) new Label(signField.getSignLocation()));
                    
                    Listcell cel3 = new Listcell();
                    cel3.setStyle("text-align:left;");
                    cel3.appendChild((Component) new Label(signField.getSignReason()));
                    
                    Listcell cel4 = new Listcell();
                    cel4.setStyle("text-align:left;");
                    cel4.appendChild((Component) new Label(new FormatoFecha().fechayHora(signField.getSignDateTime())));
                    
              
                    
                    listitem.appendChild((Component) nro);
                    listitem.appendChild((Component) cel1);
                    listitem.appendChild((Component) cel4);
                    listitem.appendChild((Component) cel3);
                    listitem.appendChild((Component) cel2);
                    
                    nListFirmas.appendChild(listitem);
                  
                }
            }

        } catch (Exception e) {
            showNotify(e.getMessage(), win, "warning", "top_center");
            e.printStackTrace();
        }
    }

    @Listen("onClick = #cancelar")
    public void cancelar() {
        winFirma.detach();
    }

    @Wire
    Window winFirma, win;

    private void showNotify(String msg, Component ref, String tipo, String posicion) {
        Clients.showNotification(msg, tipo, ref, posicion, 3000);
    }

}
