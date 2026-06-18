/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author alabajos
 */
public class JSONOscinforDataSource implements JRDataSource {

    private List<JSONOscinfor> lista = new ArrayList<JSONOscinfor>();
    private int indice = -1;

    @Override
    public boolean next() throws JRException {
        return ++indice < lista.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;
        if (("ARESOLUCION_NUM").equals(jrf.getName())) {
            valor = lista.get(indice).getARESOLUCION_NUM();
        } else if (("TITULAR").equals(jrf.getName())) {
            valor = lista.get(indice).getTITULAR();
        } else if (("NOMBRE_POA").equals(jrf.getName())) {
            valor = lista.get(indice).getNOMBRE_POA();
        }
        return valor;
    }

    public void add(JSONOscinfor bean) {
        this.lista.add(bean);
    }
}
