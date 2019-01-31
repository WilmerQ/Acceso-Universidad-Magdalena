/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.webService;

import co.leafcompany.accesounimagdalena.clases.respuestaSoapUnimagdalena;
import co.leafcompany.accesounimagdalena.logica.CommonsBean;
import co.leafcompany.accesounimagdalena.logica.LogicaUnimagdalena;
import co.leafcompany.accesounimagdalena.modelo.ControlRegistro;
import co.leafcompany.accesounimagdalena.modelo.Usuario;
import co.leafcompany.accesounimagdalena.webService.base.ResponseMessenger;
import com.google.gson.Gson;
import java.util.Date;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author wilme
 */
@Path("validaciones")
public class ValidacionesResource {

    @Context
    private UriInfo context;
    @EJB
    private LogicaUnimagdalena unimagdalena;
    @EJB
    private CommonsBean cb;

    public ValidacionesResource() {
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{idtarjeta}/{terminal}")
    public Response getJson(@PathParam("idtarjeta") Long idtarjeta, @PathParam("terminal") String terminal) {
        try {
            System.out.println("id tarjeta recibido" + idtarjeta);
            System.out.println("terminal recibido" + terminal);
            Gson gson = new Gson();
            respuestaSoapUnimagdalena su = unimagdalena.ValidarEstudiante(idtarjeta.toString());
            if (su != null) {
                if (su.getSuccess()) {
                    //new Thread(() -> {
                    try {
                        System.out.println("dentro del hilo");
                        Usuario u = (Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "idTarjeta", idtarjeta);
                        if (u == null) {
                            System.out.println("no existe");
                            u = new Usuario();
                            u.setIdTarjeta(idtarjeta);
                            u.setNombreCompleto(su.getNombreCompleto());
                            u.setCargo(su.getCargo());
                            u.setDependencia(su.getDependencia());
                            cb.guardar(u);
                            ControlRegistro cr = new ControlRegistro();
                            cr.setUsuario((Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "idTarjeta", idtarjeta));
                            cr.setTerminal(terminal);
                            cr.setMensaje("validado por base de datos unimagdalena");
                            cr.setEstadoTransaccion("aceptado");
                            cr.setFechaCreacion(new Date());
                            cb.guardar(cr);
                        } else {
                            System.out.println("ya existe");
                            u.setVersion(u.getVersion() + 1);
                            ControlRegistro cr = new ControlRegistro();
                            cr.setUsuario((Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "idTarjeta", idtarjeta));
                            cr.setTerminal(terminal);
                            cr.setMensaje("validado por base de datos local");
                            cr.setEstadoTransaccion("aceptado");
                            cr.setFechaCreacion(new Date());
                            cb.guardar(cr);
                        }
                    } catch (Exception e) {
                        System.out.println("ValidacionesResource - catch - hilo " + e.getMessage());
                    }
                    //}).start();
                    return new ResponseMessenger().getResponseOk(gson.toJson(su));
                } else {
                    //return new ResponseMessenger().getResponseError("{\"mensaje\":" + su.getMensaje() + "}");
                    ControlRegistro cr = new ControlRegistro();
                    cr.setTerminal(terminal);
                    cr.setCodigo(idtarjeta);
                    cr.setMensaje("validado por base de datos unimagdalena");
                    cr.setEstadoTransaccion("rechazado");
                    cr.setFechaCreacion(new Date());
                    cb.guardar(cr);
                    return new ResponseMessenger().getResponseError(su.getMensaje());
                }
            } else {
                ControlRegistro cr = new ControlRegistro();
                cr.setTerminal(terminal);
                cr.setCodigo(idtarjeta);
                cr.setMensaje("validado por base de datos unimagdalena y local");
                cr.setEstadoTransaccion("rechazado");
                cr.setFechaCreacion(new Date());
                cb.guardar(cr);
                return new ResponseMessenger().getResponseError("No encontrado");
            }
        } catch (Exception e) {
            System.out.println("ValidacionesResource -catch ERROR" + e.getMessage());
            return new ResponseMessenger().getResponseError("Error interno");
        }
    }
}
