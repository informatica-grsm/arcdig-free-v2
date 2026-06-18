/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;



/**
 *
 * @author jpgprog84
 */
public class Permisos  {

    private PermisosPrimaryKey primaryKey;
    private Rol rol;
    private SubModulo subModulo;
    private boolean nuevo;
    private boolean editar;
    private boolean eliminar;
    private boolean imprimir;
    private boolean ver;
    private boolean publicar;

    public Permisos() {
    }

    public Permisos(PermisosPrimaryKey primaryKey, Rol rol, SubModulo subModulo, boolean nuevo, boolean editar, boolean eliminar, boolean imprimir, boolean ver, boolean publicar) {
        this.primaryKey = primaryKey;
        this.rol = rol;
        this.subModulo = subModulo;
        this.nuevo = nuevo;
        this.editar = editar;
        this.eliminar = eliminar;
        this.imprimir = imprimir;
        this.ver = ver;
        this.publicar = publicar;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public boolean isEliminar() {
        return eliminar;
    }

    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }

    public boolean isImprimir() {
        return imprimir;
    }

    public void setImprimir(boolean imprimir) {
        this.imprimir = imprimir;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    public PermisosPrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PermisosPrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isPublicar() {
        return publicar;
    }

    public void setPublicar(boolean publicar) {
        this.publicar = publicar;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public SubModulo getSubModulo() {
        return subModulo;
    }

    public void setSubModulo(SubModulo subModulo) {
        this.subModulo = subModulo;
    }

    public boolean isVer() {
        return ver;
    }

    public void setVer(boolean ver) {
        this.ver = ver;
    }
  
}
