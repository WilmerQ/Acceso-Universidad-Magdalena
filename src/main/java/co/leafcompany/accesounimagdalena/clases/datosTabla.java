/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.clases;

import co.leafcompany.accesounimagdalena.modelo.ControlRegistro;
import java.util.List;

/**
 *
 * @author wilme
 */
public class datosTabla {

    public datosTabla() {
    }

    public datosTabla(String fecha, List<ControlRegistro> registros) {
        this.fecha = fecha;
        this.registros = registros;
    }

    private String fecha;
    private List<ControlRegistro> registros;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<ControlRegistro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<ControlRegistro> registros) {
        this.registros = registros;
    }

}
