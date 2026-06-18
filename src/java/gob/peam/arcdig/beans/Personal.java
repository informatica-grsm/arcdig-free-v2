/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;



import java.util.Date;

/**
 *
 * @author alabajos
 */
public class Personal {
    private String id;
    private Integer idRP; 
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int sexo;
    private String nombreSexo;
    private String estadoCivil;
    private Date fechaNacimiento;
    private String tipoDocumento;
    private int emisoDocPais;
    private String documento;
    private String ruc;
    private String emailLab;
    private String anexo;
    private Date fechaIngresoPublic;
    private Date fechaIngresoEnti;
    private String nivelEducativo;
    private int formProfesional;
    private String emailAlternativo;
    private Boolean discapacidad;
    private int nacionalidad;
    private int codigoPostal;
    private String telefono;
    private String celular;
    private String domicilio;
    private String nombreCompletoNPM;
    private String nombreCompletoPMN;
    private Dependencia dependencia;
    private String tipo;
    private String RegLaboral;
    private String RegPensionario;
    private String codigoCussp;
    private Integer banco;
    private String nroCuenta;
    private String seguro;
    private String codigoSeguro;
    private String codigoColegiado;
    
    private Boolean check; 
    private Boolean marca;
    private Integer idG;
    private String comision;
    private String afp;
    private String idC;
    private Date fechaCese;
    
    private String fotoPerfil;
    private String fotoPortada;

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFotoPortada() {
        return fotoPortada;
    }

    public void setFotoPortada(String fotoPortada) {
        this.fotoPortada = fotoPortada;
    }
    
    
    
    public Personal() {
    }

  

    public Personal(String id, Integer idRP , String nombre, String apellidoPaterno, String apellidoMaterno, int sexo, String nombreSexo, String estadoCivil, Date fechaNacimiento, String tipoDocumento, int emisoDocPais, String documento, String ruc, String emailLab, String anexo, Date fechaIngresoPublic, Date fechaIngresoEnti, String nivelEducativo, int formProfesional, String emailAlternativo, Boolean discapacidad, int nacionalidad, int codigoPostal, String telefono, String celular, String domicilio, String nombreCompletoNPM, String nombreCompletoPMN, Dependencia dependencia, String tipo, String RegLaboral, String RegPensionario, String codigoCussp, Integer banco, String nroCuenta, String seguro, String codigoSeguro, String codigoColegiado,  Boolean check, String afp, String comision, String idC, Date fechaCese) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.sexo = sexo;
        this.nombreSexo = nombreSexo;
        this.estadoCivil = estadoCivil;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoDocumento = tipoDocumento;
        this.emisoDocPais = emisoDocPais;
        this.documento = documento;
        this.ruc = ruc;
        this.emailLab = emailLab;
        this.anexo = anexo;
        this.fechaIngresoPublic = fechaIngresoPublic;
        this.fechaIngresoEnti = fechaIngresoEnti;
        this.nivelEducativo = nivelEducativo;
        this.formProfesional = formProfesional;
        this.emailAlternativo = emailAlternativo;
        this.discapacidad = discapacidad;
        this.nacionalidad = nacionalidad;
        this.codigoPostal = codigoPostal;
        this.telefono = telefono;
        this.celular = celular;
        this.domicilio = domicilio;
        this.nombreCompletoNPM = nombreCompletoNPM;
        this.nombreCompletoPMN = nombreCompletoPMN;
        this.dependencia = dependencia;
        this.tipo = tipo;
        this.RegLaboral = RegLaboral;
        this.RegPensionario = RegPensionario;
        this.codigoCussp = codigoCussp;
        this.banco = banco;
        this.nroCuenta = nroCuenta;
        this.seguro = seguro;
        this.codigoSeguro = codigoSeguro;
        this.codigoColegiado = codigoColegiado;
        
        this.idRP = idRP;
        this.check = check;
        this.afp = afp;
        this.comision = comision;
        this.fechaCese = fechaCese;
        this.idC = idC;
        
    }

  
    
    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public Date getFechaCese() {
        return fechaCese;
    }

    public void setFechaCese(Date fechaCese) {
        this.fechaCese = fechaCese;
    }

    public String getComision() {
        return comision;
    }

    public void setComision(String comision) {
        this.comision = comision;
    }

    public String getAfp() {
        return afp;
    }

    public void setAfp(String afp) {
        this.afp = afp;
    }

    public Personal(String id, String documento) {
        this.id = id;
        this.documento = documento;
    }

    public Boolean getMarca() {
        return marca;
    }

    public void setMarca(Boolean marca) {
        this.marca = marca;
    }

    public Integer getIdG() {
        return idG;
    }

    public void setIdG(Integer idG) {
        this.idG = idG;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
    
    

    
    public Integer getIdRP() {
        return idRP;
    }

    public void setIdRP(Integer idRP) {
        this.idRP = idRP;
    }
    
    
    
    public String getAnexo() {
        return anexo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Boolean getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Boolean discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEmailAlternativo() {
        return emailAlternativo;
    }

    public void setEmailAlternativo(String emailAlternativo) {
        this.emailAlternativo = emailAlternativo;
    }

    public String getEmailLab() {
        return emailLab;
    }

    public void setEmailLab(String emailLab) {
        this.emailLab = emailLab;
    }

    public int getEmisoDocPais() {
        return emisoDocPais;
    }

    public void setEmisoDocPais(int emisoDocPais) {
        this.emisoDocPais = emisoDocPais;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Date getFechaIngresoEnti() {
        return fechaIngresoEnti;
    }

    public void setFechaIngresoEnti(Date fechaIngresoEnti) {
        this.fechaIngresoEnti = fechaIngresoEnti;
    }

    public Date getFechaIngresoPublic() {
        return fechaIngresoPublic;
    }

    public void setFechaIngresoPublic(Date fechaIngresoPublic) {
        this.fechaIngresoPublic = fechaIngresoPublic;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getFormProfesional() {
        return formProfesional;
    }

    public void setFormProfesional(int formProfesional) {
        this.formProfesional = formProfesional;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(int nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCompletoNPM() {
        return nombreCompletoNPM;
    }

    public void setNombreCompletoNPM(String nombreCompletoNPM) {
        this.nombreCompletoNPM = nombreCompletoNPM;
    }

    public String getNombreCompletoPMN() {
        return nombreCompletoPMN;
    }

    public void setNombreCompletoPMN(String nombreCompletoPMN) {
        this.nombreCompletoPMN = nombreCompletoPMN;
    }

    public String getNombreSexo() {
        return nombreSexo;
    }

    public void setNombreSexo(String nombreSexo) {
        this.nombreSexo = nombreSexo;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getRegLaboral() {
        return RegLaboral;
    }

    public void setRegLaboral(String RegLaboral) {
        this.RegLaboral = RegLaboral;
    }

    public String getRegPensionario() {
        return RegPensionario;
    }

    public void setRegPensionario(String RegPensionario) {
        this.RegPensionario = RegPensionario;
    }

    public String getCodigoCussp() {
        return codigoCussp;
    }

    public void setCodigoCussp(String codigoCussp) {
        this.codigoCussp = codigoCussp;
    }

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getNroCuenta() {
        return nroCuenta;
    }

    public void setNroCuenta(String nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    public String getSeguro() {
        return seguro;
    }

    public void setSeguro(String seguro) {
        this.seguro = seguro;
    }

    public String getCodigoSeguro() {
        return codigoSeguro;
    }

    public void setCodigoSeguro(String codigoSeguro) {
        this.codigoSeguro = codigoSeguro;
    }

    public String getCodigoColegiado() {
        return codigoColegiado;
    }

    public void setCodigoColegiado(String codigoColegiado) {
        this.codigoColegiado = codigoColegiado;
    }
    
}
