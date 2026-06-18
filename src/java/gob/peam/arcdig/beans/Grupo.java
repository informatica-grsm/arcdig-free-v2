/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author jpgprog84
 */
public class Grupo {

    /**
     *
     */
    private static final long serialVersionUID = 4602466772673024436L;
    private Integer idGrupo;
    private String charId;
    private String nombre;
    private boolean estado;
    private Boolean check;
    
    private Boolean visible;

    public Grupo() {
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Grupo(Integer idGrupo, String nombre, boolean estado, String charId, Boolean check) {
        this.idGrupo = idGrupo;
        this.nombre = nombre;
        this.estado = estado;
        this.charId = String.valueOf(idGrupo);
        this.check = check;
    }

    public String getCharId() {
        return String.valueOf(idGrupo);
    }

    public void setCharId(String charId) {
        this.charId = charId;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
