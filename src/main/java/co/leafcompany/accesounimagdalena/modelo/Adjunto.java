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
public class Adjunto extends CamposComunesdeEntidad implements Serializable {

    private String uuid;
    @ManyToOne
    private ControlRegistro controlRegistro;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ControlRegistro getControlRegistro() {
        return controlRegistro;
    }

    public void setControlRegistro(ControlRegistro controlRegistro) {
        this.controlRegistro = controlRegistro;
    }

}
