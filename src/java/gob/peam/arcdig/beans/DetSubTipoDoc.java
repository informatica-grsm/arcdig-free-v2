/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author Usuario
 */
public class DetSubTipoDoc {
    private String idDetSubTipoDoc;
    private String idSubTipoDoc;
    private String idTipoDoc;
    private String idSistema;
    private String codDetSubTipoDoc;
    private String desDetSubTipoDoc;
    private String pageNum;
    private String pageSize;

    public DetSubTipoDoc() {
    }

    public String getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(String idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public String getIdDetSubTipoDoc() {
        return idDetSubTipoDoc;
    }

    public void setIdDetSubTipoDoc(String idDetSubTipoDoc) {
        this.idDetSubTipoDoc = idDetSubTipoDoc;
    }

    public String getIdSubTipoDoc() {
        return idSubTipoDoc;
    }

    public void setIdSubTipoDoc(String idSubTipoDoc) {
        this.idSubTipoDoc = idSubTipoDoc;
    }

    public String getCodDetSubTipoDoc() {
        return codDetSubTipoDoc;
    }

    public void setCodDetSubTipoDoc(String codDetSubTipoDoc) {
        this.codDetSubTipoDoc = codDetSubTipoDoc;
    }

    public String getDesDetSubTipoDoc() {
        return desDetSubTipoDoc;
    }

    public void setDesDetSubTipoDoc(String desDetSubTipoDoc) {
        this.desDetSubTipoDoc = desDetSubTipoDoc;
    }

    
    public String getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(String idSistema) {
        this.idSistema = idSistema;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
    
    
}
