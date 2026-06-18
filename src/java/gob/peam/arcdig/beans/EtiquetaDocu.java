package gob.peam.arcdig.beans;

public class EtiquetaDocu {

    private Integer id;
    private String nombre;
    private Boolean estado;
    private Integer tidoId;
    private String tidoNombre;
    private Integer nivel;

    public EtiquetaDocu() {
    }

    public EtiquetaDocu(Integer id, String nombre, Boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getTidoId() {
        return tidoId;
    }

    public void setTidoId(Integer tidoId) {
        this.tidoId = tidoId;
    }

    public String getTidoNombre() {
        return tidoNombre;
    }

    public void setTidoNombre(String tidoNombre) {
        this.tidoNombre = tidoNombre;
    }

}
