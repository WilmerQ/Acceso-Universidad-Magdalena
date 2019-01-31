/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author wilme
 */
@Entity
public class Placa extends CamposComunesdeEntidad implements Serializable {

    @ManyToOne
    private ControlRegistro controlRegistro;
    private Double confidence;
    private String plate;

    public ControlRegistro getControlRegistro() {
        return controlRegistro;
    }

    public void setControlRegistro(ControlRegistro controlRegistro) {
        this.controlRegistro = controlRegistro;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

}
