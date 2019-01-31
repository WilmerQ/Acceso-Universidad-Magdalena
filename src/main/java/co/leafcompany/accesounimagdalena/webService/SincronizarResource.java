/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.webService;

import co.leafcompany.accesounimagdalena.logica.CommonsBean;
import co.leafcompany.accesounimagdalena.logica.LogicaUnimagdalena;
import co.leafcompany.accesounimagdalena.modelo.ControlRegistro;
import co.leafcompany.accesounimagdalena.modelo.Usuario;
import co.leafcompany.accesounimagdalena.webService.base.ResponseMessenger;
import java.text.SimpleDateFormat;
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
@Path("sincronizar")
public class SincronizarResource {

    @Context
    private UriInfo context;
    @EJB
    private LogicaUnimagdalena unimagdalena;
    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of SincronizarResource
     */
    public SincronizarResource() {
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{idtarjeta}/{fecha}/{hora}/{estado}/{terminal}")
    public Response getJson(
            @PathParam("idtarjeta") Long idtarjeta,
            @PathParam("fecha") String date,
            @PathParam("hora") String hora,
            @PathParam("estado") String estado,
            @PathParam("terminal") String terminal) {
        try {
            System.out.println("idtarjeta recibido" + idtarjeta);
            System.out.println("fecha recibido" + date);
            System.out.println("hora recibido" + hora);
            System.out.println("estado recibido" + estado);
            System.out.println("terminal recibido" + terminal);

            String temp = date + " " + hora;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date fechaRecibida = formatter.parse(temp);
            System.out.println("fecha: nueva " + fechaRecibida);

            ControlRegistro cr = new ControlRegistro();
            if (idtarjeta != null && date.trim().length() > 2 && hora.trim().length() > 2 && estado.trim().length() > 2 && terminal.trim().length() > 2) {
                Usuario u = (Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "idTarjeta", idtarjeta);
                if (u == null) {
                    System.out.println("usuario no existe en base de datos local");
                    u = unimagdalena.ValidarUsuario(idtarjeta);
                    if (u != null) {
                        System.out.println("usuario existe en el servidor de la unimag");
                        cb.guardar(u);
                        cr.setUsuario((Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "idTarjeta", idtarjeta));
                        cr.setTerminal(terminal);
                        cr.setMensaje("validado por base de datos local");
                        cr.setEstadoTransaccion(estado);
                        cr.setFechaCreacion(fechaRecibida);
                        cb.guardar(cr);
                        return new ResponseMessenger().getResponseOk("se sincronizo");
                    } else {
                        System.out.println("codigo no existe en ningun sitio");
                        cr.setCodigo(idtarjeta);
                        cr.setTerminal(terminal);
                        cr.setEstadoTransaccion(estado);
                        cr.setMensaje("no se encontro el codigo de la tarjeta");
                        cr.setFechaCreacion(fechaRecibida);
                        cb.guardar(cr);
                        return new ResponseMessenger().getResponseOk("se sincronizo");
                    }
                } else if (u.getEstado()) {
                    System.out.println("usuario existe en base de datos local y esta activo");
                    cr.setUsuario(u);
                    cr.setTerminal(terminal);
                    cr.setMensaje("validado por base de datos local");
                    cr.setEstadoTransaccion(estado);
                    cr.setFechaCreacion(fechaRecibida);
                    cb.guardar(cr);
                    return new ResponseMessenger().getResponseOk("se sincronizo");
                } else {
                    System.out.println("se debe verificar nuevamente usuario");
                    u = unimagdalena.ValidarUsuario(idtarjeta);
                    if (u != null) {
                        System.out.println("reverificacion usuario existe en el servidor de la unimag");
                        Usuario u1 = (Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "idTarjeta", u.getIdTarjeta());
                        u1.setEstado(Boolean.TRUE);
                        cb.guardar(u1);
                        cr.setUsuario(u1);
                        cr.setMensaje("valido en base de datos unimagdalena");
                        cr.setTerminal(terminal);
                        cr.setEstadoTransaccion(estado);
                        cr.setFechaCreacion(fechaRecibida);
                        cb.guardar(cr);
                        return new ResponseMessenger().getResponseOk("se sincronizo");
                    } else {
                        System.out.println("reverificacion - codigo no existe en ningun sitio");
                        cr.setCodigo(idtarjeta);
                        cr.setTerminal(terminal);
                        cr.setMensaje("no se encontro el codigo de la tarjeta");
                        cr.setEstadoTransaccion(estado);
                        cr.setFechaCreacion(fechaRecibida);
                        cb.guardar(cr);
                        return new ResponseMessenger().getResponseOk("se sincronizo");
                    }
                }
            } else {
                return new ResponseMessenger().getResponseError("Error url");
            }
        } catch (Exception e) {
            return new ResponseMessenger().getResponseError("Error interno");
        }
    }

}
