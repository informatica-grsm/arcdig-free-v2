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
public class SubSerie {
    private String idSubSerie;
    private String idSerie;
    
    private String idSistema;
    private String codSubSerieDoc;
    private String desSubSerieDoc;
    private String pageNum;
    private String pageSize;

    public SubSerie() {
    }
    
    

    public String getIdSubSerie() {
        return idSubSerie;
    }

    public void setIdSubSerie(String idSubSerie) {
        this.idSubSerie = idSubSerie;
    }

    public String getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(String idSerie) {
        this.idSerie = idSerie;
    }

    public String getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(String idSistema) {
        this.idSistema = idSistema;
    }

    public String getCodSubSerieDoc() {
        return codSubSerieDoc;
    }

    public void setCodSubSerieDoc(String codSubSerieDoc) {
        this.codSubSerieDoc = codSubSerieDoc;
    }

    public String getDesSubSerieDoc() {
        return desSubSerieDoc;
    }

    public void setDesSubSerieDoc(String desSubSerieDoc) {
        this.desSubSerieDoc = desSubSerieDoc;
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
