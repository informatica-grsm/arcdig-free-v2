/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

import java.util.Date;
import java.util.List;

/**
 *
 * @author alabajos
 */
public class Documento {
    private Integer id;
    private String titulo;
    private String resumen;
    private String tituloMin;
    private String resumenMin;
    private String descripcion;
    private String fechaDocx;
    private Integer tidoId;
    private String mimeTypes;
    private String origenArchivo;
    private String ruta;
    private Double version;
    private Boolean estado;
    private Boolean activo;
    private String usuario;
    private Integer cateId;
    private Date docuFecha;
    private Integer usuaId;
    private String origen;
    private String metaData;
    private Integer idEtiqueta;
    private Integer cataId;
    private Boolean privado;
    private Boolean publico;
    private Integer estrId;
    private MineType mineType;
    private List<EtiquetaDocu> EtiquetaDocu;
    private String remain;
    private Boolean personalizado;
    private String propietario;
    private TipoDocumento tipoDocumento;
    private Categoria categoria;
    private Catalogo catalogo;
    private Date docuHora;
    private Integer docuGroup;
    private Integer relacion;
    private String docuCodigo;
    private Integer subTipo;
    private Integer orden;
    private Boolean solo;
    private Integer subGrupo;
    private Integer subGrupo1;
    private Integer subGrupo2;

    public Documento() {
    }

    public Documento(Integer id, String titulo, String resumen, String descripcion,  Integer tidoId, String mimeTypes, String origenArchivo, String ruta, Double version, Boolean estado, Boolean activo, String usuario, String fechaDocx, Integer cateId, Date docuFecha, Integer usuaId, String origen, String metaData, Integer idEtiqueta, Integer cataId, Boolean privado, Boolean publico, Integer estrId, MineType mineType, List<EtiquetaDocu> EtiquetaDocu, String remain, Boolean personalizado, String propietario, Catalogo catalogo, Date docuHora) {
        this.id = id;
        this.titulo = titulo;
        this.resumen = resumen;
        this.descripcion = descripcion;
        
        this.tidoId = tidoId;
        this.mimeTypes = mimeTypes;
        this.origenArchivo = origenArchivo;
        this.ruta = ruta;
        this.version = version;
        this.estado = estado;
        this.activo = activo;
        this.usuario = usuario;
        this.fechaDocx = fechaDocx;
        this.cateId = cateId;
        this.docuFecha = docuFecha;
        this.usuaId = usuaId;
        this.origen = origen;
        this.metaData = metaData;
        this.idEtiqueta = idEtiqueta;
        this.cataId = cataId;
        this.privado = privado;
        this.publico = publico;
        this.estrId = estrId;
        this.mineType = mineType;
        this.EtiquetaDocu = EtiquetaDocu;
        this.remain = remain;
        this.personalizado = personalizado;
        this.propietario = propietario;
        this.catalogo = catalogo;
        this.docuHora = docuHora;
        
    }

    public Boolean getSolo() {
        return solo;
    }

    public Integer getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(Integer subGrupo) {
        this.subGrupo = subGrupo;
    }
    

    public void setSolo(Boolean solo) {
        this.solo = solo;
    }
    
    

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
    
    

    public Integer getDocuGroup() {
        return docuGroup;
    }

    public void setDocuGroup(Integer docuGroup) {
        this.docuGroup = docuGroup;
    }

    public Integer getRelacion() {
        return relacion;
    }

    public void setRelacion(Integer relacion) {
        this.relacion = relacion;
    }

    public String getDocuCodigo() {
        return docuCodigo;
    }

    public void setDocuCodigo(String docuCodigo) {
        this.docuCodigo = docuCodigo;
    }

    public Integer getSubTipo() {
        return subTipo;
    }

    public void setSubTipo(Integer subTipo) {
        this.subTipo = subTipo;
    }

  
    
    
    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public Date getDocuHora() {
        return docuHora;
    }

    public void setDocuHora(Date docuHora) {
        this.docuHora = docuHora;
    }

    
    
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    
    public String getPropietario() {
        return propietario;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    
    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
    
    

    public String getTituloMin() {
        return tituloMin;
    }

    public void setTituloMin(String tituloMin) {
        this.tituloMin = tituloMin;
    }

    public String getResumenMin() {
        return resumenMin;
    }

    public void setResumenMin(String resumenMin) {
        this.resumenMin = resumenMin;
    }

    
    public Boolean getPersonalizado() {
        return personalizado;
    }

    public void setPersonalizado(Boolean personalizado) {
        this.personalizado = personalizado;
    }

    
    public List<EtiquetaDocu> getEtiquetaDocu() {
        return EtiquetaDocu;
    }

    public void setEtiquetaDocu(List<EtiquetaDocu> EtiquetaDocu) {
        this.EtiquetaDocu = EtiquetaDocu;
    }


    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }
    
    

    public MineType getMineType() {
        return mineType;
    }

    public void setMineType(MineType mineType) {
        this.mineType = mineType;
    }

    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

 

    public Integer getTidoId() {
        return tidoId;
    }

    public void setTidoId(Integer tidoId) {
        this.tidoId = tidoId;
    }

    public String getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(String mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public String getOrigenArchivo() {
        return origenArchivo;
    }

    public void setOrigenArchivo(String origenArchivo) {
        this.origenArchivo = origenArchivo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFechaDocx() {
        return fechaDocx;
    }

    public void setFechaDocx(String fechaDocx) {
        this.fechaDocx = fechaDocx;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Date getDocuFecha() {
        return docuFecha;
    }

    public void setDocuFecha(Date docuFecha) {
        this.docuFecha = docuFecha;
    }

    public Integer getUsuaId() {
        return usuaId;
    }

    public void setUsuaId(Integer usuaId) {
        this.usuaId = usuaId;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public Integer getIdEtiqueta() {
        return idEtiqueta;
    }

    public void setIdEtiqueta(Integer idEtiqueta) {
        this.idEtiqueta = idEtiqueta;
    }

    public Integer getCataId() {
        return cataId;
    }

    public void setCataId(Integer cataId) {
        this.cataId = cataId;
    }

    public Boolean getPrivado() {
        return privado;
    }

    public void setPrivado(Boolean privado) {
        this.privado = privado;
    }

    public Boolean getPublico() {
        return publico;
    }

    public void setPublico(Boolean publico) {
        this.publico = publico;
    }

    public Integer getEstrId() {
        return estrId;
    }

    public void setEstrId(Integer estrId) {
        this.estrId = estrId;
    }

    public Integer getSubGrupo1() {
        return subGrupo1;
    }

    public void setSubGrupo1(Integer subGrupo1) {
        this.subGrupo1 = subGrupo1;
    }

    public Integer getSubGrupo2() {
        return subGrupo2;
    }

    public void setSubGrupo2(Integer subGrupo2) {
        this.subGrupo2 = subGrupo2;
    }


    


    
}