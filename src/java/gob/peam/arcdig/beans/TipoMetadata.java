package gob.peam.arcdig.beans;

public class TipoMetadata {

    private Integer tidoId;
    private Integer id;
    private Boolean timeEstado;
    private Integer tidoVersion;
    private String tipoDato;
    private Boolean obligado;
    private String defecto;
    private Integer secuencia;

    public TipoMetadata() {
    }

    public TipoMetadata(Integer tidoId, Integer id, Boolean timeEstado, Integer tidoVersion, String tipoDato, Boolean obligado, String defecto, Integer secuencia) {
        this.tidoId = tidoId;
        this.id = id;
        this.timeEstado = timeEstado;
        this.tidoVersion = tidoVersion;
        this.tipoDato = tipoDato;
        this.obligado = obligado;
        this.defecto = defecto;
        this.secuencia = secuencia;
    }

    public Integer getTidoId() {
        return tidoId;
    }

    public void setTidoId(Integer tidoId) {
        this.tidoId = tidoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getTimeEstado() {
        return timeEstado;
    }

    public void setTimeEstado(Boolean timeEstado) {
        this.timeEstado = timeEstado;
    }

    public Integer getTidoVersion() {
        return tidoVersion;
    }

    public void setTidoVersion(Integer tidoVersion) {
        this.tidoVersion = tidoVersion;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Boolean getObligado() {
        return obligado;
    }

    public void setObligado(Boolean obligado) {
        this.obligado = obligado;
    }

    public String getDefecto() {
        return defecto;
    }

    public void setDefecto(String defecto) {
        this.defecto = defecto;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

}
