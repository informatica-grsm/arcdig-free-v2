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
public class DocumentoUsuario {
    private String dni;
    private Integer docuId;

    public DocumentoUsuario() {
    }

    public DocumentoUsuario(String dni, Integer docuId) {
        this.dni = dni;
        this.docuId = docuId;
    }

   
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Integer getDocuId() {
        return docuId;
    }

    public void setDocuId(Integer docuId) {
        this.docuId = docuId;
    }

    
    
    
}
