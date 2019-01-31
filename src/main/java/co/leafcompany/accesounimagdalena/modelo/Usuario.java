/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;

/**
 *
 * @author wilme
 */
@Entity
public class Usuario extends CamposComunesdeEntidad implements Serializable {

    public Usuario() {
    }

    private String nombreCompleto;
    private String cargo;
    private String dependencia;
    private Long idTarjeta;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaultimaValidacion;

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }

    public Long getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(Long idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public Date getFechaultimaValidacion() {
        return fechaultimaValidacion;
    }

    public void setFechaultimaValidacion(Date fechaultimaValidacion) {
        this.fechaultimaValidacion = fechaultimaValidacion;
    }

}
