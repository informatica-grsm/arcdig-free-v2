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
public class Permiso {
    private String dni;
    private Boolean activo;
    private Boolean item1;
    private Boolean item2;
    private Boolean item3;
    private Boolean item4;
    private Boolean item5;
    private Boolean item6;
    private Boolean item7;
    private Boolean item8;
    private Boolean item9;
    private Boolean item10;
    
    private Integer tipo;

    public Permiso(String dni, Boolean activo, Boolean item1, Boolean item2, Boolean item3, Boolean item4, Boolean item5, Integer tipo, Boolean item6, Boolean item7, Boolean item8) {
        this.dni = dni;
        this.activo = activo;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.tipo = tipo;
        this.item6 = item6;
        this.item7 = item7;
        this.item8 = item8;
    }

    public Boolean getItem10() {
        return item10;
    }

    public void setItem10(Boolean item10) {
        this.item10 = item10;
    }
    
    

    public Boolean getItem9() {
        return item9;
    }

    public void setItem9(Boolean item9) {
        this.item9 = item9;
    }
    
    

    public Boolean getItem7() {
        return item7;
    }

    public void setItem7(Boolean item7) {
        this.item7 = item7;
    }

    public Boolean getItem8() {
        return item8;
    }

    public void setItem8(Boolean item8) {
        this.item8 = item8;
    }
    
    

    public Permiso() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getItem1() {
        return item1;
    }

    public void setItem1(Boolean item1) {
        this.item1 = item1;
    }

    public Boolean getItem2() {
        return item2;
    }

    public void setItem2(Boolean item2) {
        this.item2 = item2;
    }

    public Boolean getItem3() {
        return item3;
    }

    public void setItem3(Boolean item3) {
        this.item3 = item3;
    }

    public Boolean getItem4() {
        return item4;
    }

    public void setItem4(Boolean item4) {
        this.item4 = item4;
    }

    public Boolean getItem5() {
        return item5;
    }

    public void setItem5(Boolean item5) {
        this.item5 = item5;
    }

    public Boolean getItem6() {
        return item6;
    }

    public void setItem6(Boolean item6) {
        this.item6 = item6;
    }

    
    
    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
    
}
