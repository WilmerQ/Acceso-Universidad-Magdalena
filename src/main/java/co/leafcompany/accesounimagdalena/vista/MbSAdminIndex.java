/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.vista;

import co.leafcompany.accesounimagdalena.base.Md5;
import co.leafcompany.accesounimagdalena.logica.CommonsBean;
import co.leafcompany.accesounimagdalena.modelo.Administrador;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author wilme
 */
@ViewScoped
@ManagedBean(name = "MbSAdminIndex")
public class MbSAdminIndex implements Serializable {
    
    private Administrador a;
    private String confirmacion;
    private List<Administrador> administradors;
    
    @EJB
    private CommonsBean cb;
    
    public MbSAdminIndex() {
    }
    
    @PostConstruct
    public void init() {
        a = new Administrador();
        administradors = cb.getAll(Administrador.class);
    }
    
    public void accionGuardar() {
//        if (VerificarFormulario()) {

        String temp = Md5.getEncoddedString(a.getContrasena());
        a.setContrasena(temp);
        if (cb.guardar(a)) {
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "Exitoso", "Se ha guardado");
            init();
        } else {
            mostrarMensaje(FacesMessage.SEVERITY_FATAL, "Error", "Ha fallado al guardar");
        }
//        }
    }

//    public Boolean VerificarFormulario() {
//        Boolean resultado = Boolean.TRUE;
//        if (a.getNombrecompleto().trim().length() == 0) {
//            resultado = Boolean.FALSE;
//            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue nombre de la ciudad");
//        } else {
//            try {
//                Ciudad u = (Ciudad) cb.getByOneFieldWithOneResult(Ciudad.class, "nombre", ciudad.getNombre());
//                if (u != null) {
//                    resultado = Boolean.FALSE;
//                    mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Nombre de usuario ya existe");
//                }
//            } catch (Exception ex) {
//                Logger.getLogger(MbCiudad.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        if (ciudad.getCodigo() == null) {
//            resultado = Boolean.FALSE;
//            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue codigo de ciudad");
//        } else {
//            try {
//                Ciudad u = (Ciudad) cb.getByOneFieldWithOneResult(Ciudad.class, "codigo", ciudad.getCodigo());
//                if (u != null) {
//                    resultado = Boolean.FALSE;
//                    mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "codigo de ciudad ya existe");
//                }
//            } catch (Exception ex) {
//                Logger.getLogger(MbCiudad.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return resultado;
//    }
    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }
    
    public void resetAdministrador() {
        a = new Administrador();
    }
    
    public void accionCargarAdministrador(Administrador administrador) {
        this.a = administrador;
    }
    
    public Administrador getA() {
        return a;
    }
    
    public void setA(Administrador a) {
        this.a = a;
    }
    
    public String getConfirmacion() {
        return confirmacion;
    }
    
    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }
    
    public List<Administrador> getAdministradors() {
        return administradors;
    }
    
    public void setAdministradors(List<Administrador> administradors) {
        this.administradors = administradors;
    }
    
}
