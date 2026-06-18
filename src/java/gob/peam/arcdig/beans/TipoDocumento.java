package gob.peam.arcdig.beans;

public class TipoDocumento {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private String metadata;
    private Integer privacidad;
    private Integer cateId;
    private String cateNombre;

    public TipoDocumento() {
    }

    public TipoDocumento(Integer id, String nombre, String descripcion, Boolean estado, String metadata, Integer privacidad, Integer cateId, String cateNombre) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.metadata = metadata;
        this.privacidad = privacidad;
        this.cateId = cateId;
        this.cateNombre = cateNombre;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getPrivacidad() {
        return privacidad;
    }

    public void setPrivacidad(Integer privacidad) {
        this.privacidad = privacidad;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getCateNombre() {
        return cateNombre;
    }

    public void setCateNombre(String cateNombre) {
        this.cateNombre = cateNombre;
    }

}
