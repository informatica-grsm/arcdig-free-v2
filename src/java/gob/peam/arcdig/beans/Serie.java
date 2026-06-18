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
public class Serie {

    
    private String idSerie;
    private String idSistema;
    private String codSerieDoc;
    private String desSerieDoc;
    private String pageNum;
    private String pageSize;

    public Serie() {
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

    public String getCodSerieDoc() {
        return codSerieDoc;
    }

    public void setCodSerieDoc(String codSerieDoc) {
        this.codSerieDoc = codSerieDoc;
    }

    public String getDesSerieDoc() {
        return desSerieDoc;
    }

    public void setDesSerieDoc(String desSerieDoc) {
        this.desSerieDoc = desSerieDoc;
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
