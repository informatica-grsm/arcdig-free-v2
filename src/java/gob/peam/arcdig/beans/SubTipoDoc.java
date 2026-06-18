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
public class SubTipoDoc {
    private String idSubTipoDoc;
    private String idTipoDoc;
    private String idSistema;
    private String codSubTipoDoc;
    private String desSubTipoDoc;
    private String pageNum;
    private String pageSize;

    public SubTipoDoc() {
    }

    public String getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(String idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public String getIdSubTipoDoc() {
        return idSubTipoDoc;
    }

    public void setIdSubTipoDoc(String idSubTipoDoc) {
        this.idSubTipoDoc = idSubTipoDoc;
    }

    public String getCodSubTipoDoc() {
        return codSubTipoDoc;
    }

    public void setCodSubTipoDoc(String codSubTipoDoc) {
        this.codSubTipoDoc = codSubTipoDoc;
    }

    public String getDesSubTipoDoc() {
        return desSubTipoDoc;
    }

    public void setDesSubTipoDoc(String desSubTipoDoc) {
        this.desSubTipoDoc = desSubTipoDoc;
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
