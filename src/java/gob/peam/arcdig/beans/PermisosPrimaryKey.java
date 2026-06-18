/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author jpgprog84
 */
public class PermisosPrimaryKey {
    //private Rol rol;

    private SubModulo subModulo;
    private Rol rol;

    public PermisosPrimaryKey() {
    }

    public PermisosPrimaryKey(SubModulo subModulo, Rol rol) {
        this.subModulo = subModulo;
        this.rol = rol;
    }

    public SubModulo getSubModulo() {
        return subModulo;
    }

    public void setSubModulo(SubModulo subModulo) {
        this.subModulo = subModulo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
