/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author Cj.Legacy
 */
public class DocumentoEtiqueta {
    private Integer id;
    private Integer etiqId;
    private Integer docuId;

    public DocumentoEtiqueta() {
    }

    public DocumentoEtiqueta(Integer id, Integer etiqId, Integer docuId) {
        this.id = id;
        this.etiqId = etiqId;
        this.docuId = docuId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEtiqId() {
        return etiqId;
    }

    public void setEtiqId(Integer etiqId) {
        this.etiqId = etiqId;
    }

    public Integer getDocuId() {
        return docuId;
    }

    public void setDocuId(Integer docuId) {
        this.docuId = docuId;
    }
    
    
}
