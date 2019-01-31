/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.vista;

import co.leafcompany.accesounimagdalena.base.ConfiguracionGeneral;
import co.leafcompany.accesounimagdalena.base.Md5;
import co.leafcompany.accesounimagdalena.base.SessionOperations;
import co.leafcompany.accesounimagdalena.logica.CommonsBean;
import co.leafcompany.accesounimagdalena.logica.LogicaAdministrador;
import co.leafcompany.accesounimagdalena.modelo.Administrador;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author wilme
 */
@SessionScoped
@ManagedBean(name = "MbUsuario")
public class MbUsuario implements Serializable {

    private Administrador administrador;
    private Boolean autenticado;
    private Boolean isusuario;
    private Boolean isadmin;
    private String mensaje;

    //Loguin
    private String nombreDeUsuaio;
    private String password;

    @EJB
    LogicaAdministrador logicaAdministrador;
    @EJB
    CommonsBean cb;

    public MbUsuario() {
    }

    @PostConstruct
    public void init() {
        ConfiguracionGeneral cg = new ConfiguracionGeneral();
        mensaje = "";
        administrador = (Administrador) SessionOperations.getSessionValue("USUARIO");
        if (administrador == null) {
            administrador = new Administrador();
            autenticado = Boolean.FALSE;
            isusuario = Boolean.FALSE;
            isadmin = Boolean.FALSE;
            SessionOperations.setSessionValue("USER", Boolean.FALSE);
        } else {
            autenticado = Boolean.TRUE;
            isusuario = Boolean.TRUE;
        }
    }

    public String accionLogin() {
        if (verificarFormulario()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            autenticado = false;
            isusuario = false;
            isadmin = false;
            Administrador u = logicaAdministrador.LoginWeb(nombreDeUsuaio, Md5.getEncoddedString(password));
            SessionOperations.setSessionValue("USER", Boolean.FALSE);
            if (u != null) {
                mensaje = "";
                String url;
                administrador = u;
                autenticado = true;
                if (u.getNombreUsuario().equals("Administrador")) {
                    isadmin = true;
                    administrador.setNombreUsuario("Administrador");
                    administrador.setNombrecompleto("Administrador");
                    SessionOperations.setSessionValue("ADMIN", Boolean.TRUE);
                    SessionOperations.setSessionValue("USER", Boolean.FALSE);
                    System.out.println("logueo admin");
                    url = "sadmin/index.xhtml";
                } else {
                    isusuario = true;
                    SessionOperations.setSessionValue("USER", Boolean.TRUE);
                    SessionOperations.setSessionValue("ADMIN", Boolean.FALSE);
                    System.out.println("logueo user");
                    url = "admin/index.xhtml";
                }
                SessionOperations.setSessionValue("USUARIO", administrador);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, u.getNombreUsuario(), "Bienvenido"));
                redirect(url);
                init();
            } else {
                mensaje = "Verifique sus Credenciales";
            }
        }
        return null;
    }

    public String accionLogout() {
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        try {
            SessionOperations.setSessionValue("USER", Boolean.FALSE);
            SessionOperations.setSessionValue("ADMIN", Boolean.FALSE);
            context.getExternalContext().invalidateSession();
        } catch (Exception e) {

        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salida", "Se ha cerrado la sesion correctamente"));
        String patch = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
        redirect(patch);
        return null;
    }

    public Boolean verificarFormulario() {
        Boolean resultado = Boolean.TRUE;
        if (nombreDeUsuaio.length() == 0) {
            //System.out.println("nombreDeUsuaio.length() " + nombreDeUsuaio.length());
            resultado = Boolean.FALSE;
            mensaje = "Error: Inserte Nombre de usuario";
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Inserte Nombre de usuario");
        }

        if (password.trim().length() == 0) {
            resultado = Boolean.FALSE;
            mensaje = "Error: Inserte su contraseña ";
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Inserte su  Contraseña");
        } else {
            String[] campos = password.split(" ");
            if (campos.length > 1) {
                resultado = Boolean.FALSE;
                mensaje = "Error: la Contraseña no permite espacios";
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "la Contraseña no permite espacios");
            }
        }
        return resultado;
    }

    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }

    private void redirect(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(MbUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombreDeUsuaio() {
        return nombreDeUsuaio;
    }

    public void setNombreDeUsuaio(String nombreDeUsuaio) {
        this.nombreDeUsuaio = nombreDeUsuaio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

}
