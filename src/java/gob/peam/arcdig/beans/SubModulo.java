package gob.peam.arcdig.beans;

import java.io.Serializable;

public class SubModulo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6766192706575589593L;
    private Integer idSubModulo;
    private String nombre;
    private String url;
    private boolean estado;
    private Etiqueta etiqueta;
    private Modulo modulo;
    private Permisos permisos;

    public SubModulo() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SubModulo(Integer idSubModulo, String nombre, String url, boolean estado) {
        super();
        this.idSubModulo = idSubModulo;
        this.nombre = nombre;
        this.url = url;
        this.estado = estado;
    }

    public Integer getIdSubModulo() {
        return idSubModulo;
    }

    public void setIdSubModulo(Integer idSubModulo) {
        this.idSubModulo = idSubModulo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Etiqueta getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Permisos getPermisos() {
        return permisos;
    }

    public void setPermisos(Permisos permisos) {
        this.permisos = permisos;
    }
}
