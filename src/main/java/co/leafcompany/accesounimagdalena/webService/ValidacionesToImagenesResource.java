/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.webService;

import co.leafcompany.accesounimagdalena.base.ConfiguracionGeneral;
import co.leafcompany.accesounimagdalena.clases.respuestaSoapUnimagdalena;
import co.leafcompany.accesounimagdalena.logica.CommonsBean;
import co.leafcompany.accesounimagdalena.logica.LogicaOpenalpr;
import co.leafcompany.accesounimagdalena.logica.LogicaUnimagdalena;
import co.leafcompany.accesounimagdalena.modelo.Adjunto;
import co.leafcompany.accesounimagdalena.modelo.ControlRegistro;
import co.leafcompany.accesounimagdalena.modelo.Usuario;
import co.leafcompany.accesounimagdalena.webService.base.ResponseMessenger;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * REST Web Service
 *
 * @author wilme
 */
@Path("validacionesimagenes")
public class ValidacionesToImagenesResource {

    @Context
    private UriInfo context;
    @EJB
    private LogicaUnimagdalena unimagdalena;
    @EJB
    private CommonsBean cb;
    @EJB
    private LogicaOpenalpr openalpr;

    public ValidacionesToImagenesResource() {
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json;charset=UTF-8")
    @Path("/sincronizar/{idtarjeta}/{fecha}/{hora}/{estado}/{terminal}")
    public Response sincronizarResponse(MultipartFormDataInput input,
            @PathParam("idtarjeta") Long idtarjeta,
            @PathParam("fecha") String date,
            @PathParam("hora") String hora,
            @PathParam("estado") String estado,
            @PathParam("terminal") String terminal) {
        try {
            System.out.println("------------------------------");
            System.out.println("metodo sincronizar con imagen");
            System.out.println("idtarjeta recibido" + idtarjeta);
            System.out.println("fecha recibido" + date);
            System.out.println("hora recibido" + hora);
            System.out.println("estado recibido" + estado);
            System.out.println("terminal recibido" + terminal);

            byte[] imgToValidarPlaca;

            String temp1 = date + " " + hora;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date fechaRecibida = formatter.parse(temp1);
            System.out.println("fecha: nueva " + fechaRecibida);

            Adjunto adjunto = new Adjunto();
            try {
                Map<String, List<InputPart>> temp = input.getFormDataMap();
                temp.forEach((k, v) -> System.out.println("Key: " + k + ": Value: " + v.size()));
                String uuid = UUID.randomUUID().toString();
                InputPart filePart = temp.get("file").get(0);
                InputStream inputStream = filePart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                String fileName = ConfiguracionGeneral.RUTA + "imagenes" + File.separator + uuid + ".jpg";
                if (!writeFile(bytes, fileName)) {
                    System.out.println("Error: subirAnexo3: problema al guardar en el modelo");
                    return new ResponseMessenger().getResponseError("Problema interno del servidor");
                }
                adjunto.setUuid(uuid);
                imgToValidarPlaca = bytes;
            } catch (IOException e) {
                System.out.println("Error: subirAnexo2 " + e.getMessage());
                return new ResponseMessenger().getResponseError("Problema interno del servidor");
            }

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

//                        System.out.println("fecha despues " + cr.getFechaCreacion());
                        cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "fechaCreacion", cr.getFechaCreacion());
                        adjunto.setControlRegistro(cr);
                        cb.guardar(adjunto);
                        openalpr.getPlaca(imgToValidarPlaca, cr.getId());
                        return new ResponseMessenger().getResponseOk("se sincronizo");
                    } else {
                        System.out.println("codigo no existe en ningun sitio");
                        cr.setCodigo(idtarjeta);
                        cr.setTerminal(terminal);
                        cr.setEstadoTransaccion(estado);
                        cr.setMensaje("no se encontro el codigo de la tarjeta");
                        cr.setFechaCreacion(fechaRecibida);
                        cb.guardar(cr);

                        System.out.println("fecha despues " + cr.getFechaCreacion());
                        cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "fechaCreacion", cr.getFechaCreacion());
                        adjunto.setControlRegistro(cr);
                        cb.guardar(adjunto);
                        openalpr.getPlaca(imgToValidarPlaca, cr.getId());
                        return new ResponseMessenger().getResponseOk("se sincronizo");
                    }
                } else if (u.getEstado()) {
                    System.out.println("usuario existe en base de datos local y esta activo");
                    cr.setUsuario(u);
                    cr.setTerminal(terminal);
                    cr.setMensaje("validado por base de datos local");
                    cr.setEstadoTransaccion(estado);
                    cr.setFechaCreacion(fechaRecibida);
                    Date aux1 = cr.getFechaCreacion();
                    cb.guardar(cr);

                    System.out.println("fecha despues " + cr.getFechaCreacion());
                    cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "fechaCreacion", aux1);
                    adjunto.setControlRegistro(cr);
                    cb.guardar(adjunto);
                    openalpr.getPlaca(imgToValidarPlaca, cr.getId());
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

                        System.out.println("fecha despues " + cr.getFechaCreacion());
                        cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "fechaCreacion", cr.getFechaCreacion());
                        adjunto.setControlRegistro(cr);
                        cb.guardar(adjunto);
                        openalpr.getPlaca(imgToValidarPlaca, cr.getId());
                        return new ResponseMessenger().getResponseOk("se sincronizo");
                    } else {
                        System.out.println("reverificacion - codigo no existe en ningun sitio");
                        cr.setCodigo(idtarjeta);
                        cr.setTerminal(terminal);
                        cr.setMensaje("no se encontro el codigo de la tarjeta");
                        cr.setEstadoTransaccion(estado);
                        cr.setFechaCreacion(fechaRecibida);
                        cb.guardar(cr);

                        System.out.println("fecha despues " + cr.getFechaCreacion());
                        cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "fechaCreacion", cr.getFechaCreacion());
                        adjunto.setControlRegistro(cr);
                        cb.guardar(adjunto);
                        openalpr.getPlaca(imgToValidarPlaca, cr.getId());
                        return new ResponseMessenger().getResponseOk("se sincronizo");
                    }
                }
            } else {
                return new ResponseMessenger().getResponseError("Error url");
            }
        } catch (Exception e) {
            System.out.println("Error: subirAnexo2 " + e.getLocalizedMessage());
            return new ResponseMessenger().getResponseError("Problema interno del servidor");
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/validar/{idtarjeta}/{terminal}")
    public Response validarResponse(@PathParam("idtarjeta") Long idtarjeta, @PathParam("terminal") String terminal) {
        try {
            System.out.println("---------------------------------");
            System.out.println("metodo validar con imagenes");
            System.out.println("id tarjeta recibido " + idtarjeta);
            System.out.println("terminal recibido " + terminal);
            Gson gson = new Gson();
            respuestaSoapUnimagdalena su = unimagdalena.ValidarEstudiante(idtarjeta.toString());
            if (su != null) {
                if (su.getSuccess()) {
                    ControlRegistro cr = new ControlRegistro();
                    try {
                        Usuario u = (Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "idTarjeta", idtarjeta);
                        if (u == null) {
                            System.out.println("no existe");
                            u = new Usuario();
                            u.setIdTarjeta(idtarjeta);
                            u.setNombreCompleto(su.getNombreCompleto());
                            u.setCargo(su.getCargo());
                            u.setDependencia(su.getDependencia());
                            cb.guardar(u);

                            cr.setUsuario((Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "idTarjeta", idtarjeta));
                            cr.setTerminal(terminal);
                            cr.setMensaje("validado por base de datos unimagdalena");
                            cr.setEstadoTransaccion("aceptado");
                            cr.setFechaCreacion(new Date());
                            cb.guardar(cr);
                        } else {
                            System.out.println("ya existe");
                            u.setVersion(u.getVersion() + 1);
//                            ControlRegistro cr = new ControlRegistro();
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
                    System.out.println("fecha despues " + cr.getFechaCreacion());
                    cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "fechaCreacion", cr.getFechaCreacion());
                    su.setIdToCarga(cr.getId());

                    return new ResponseMessenger().getResponseOk(gson.toJson(su));
                } else {
                    Usuario u = null;
                    try {
                        u = new Usuario();
                        u.setIdTarjeta(idtarjeta);
                        u.setNombreCompleto(su.getNombreCompleto());
                        u.setCargo(su.getCargo());
                        u.setDependencia(su.getDependencia());
                        cb.guardar(u);
                    } catch (Exception e) {
                        System.out.println("inactivo sin datos");
                    }
                    ControlRegistro cr = new ControlRegistro();
                    cr.setTerminal(terminal);
                    cr.setCodigo(idtarjeta);
                    cr.setMensaje("- inactivo confirmado por base de datos unimagdalena");
                    cr.setEstadoTransaccion("rechazado");
                    cr.setFechaCreacion(new Date());
                    if (u != null) {
                        cr.setUsuario((Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "idTarjeta", idtarjeta));
                    }
                    cb.guardar(cr);

                    System.out.println("fecha despues " + cr.getFechaCreacion());
                    cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "fechaCreacion", cr.getFechaCreacion());
                    su.setIdToCarga(cr.getId());

                    return new ResponseMessenger().getResponseError(gson.toJson(su));
                }
            } else {
                ControlRegistro cr = new ControlRegistro();
                cr.setTerminal(terminal);
                cr.setCodigo(idtarjeta);
                cr.setMensaje(" - confirmado por base de datos unimagdalena y local");
                cr.setEstadoTransaccion("rechazado");
                cr.setFechaCreacion(new Date());
                cb.guardar(cr);

                System.out.println("fecha despues " + cr.getFechaCreacion());
                cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "fechaCreacion", cr.getFechaCreacion());
                su.setIdToCarga(cr.getId());

                return new ResponseMessenger().getResponseError(gson.toJson(su));
            }
        } catch (Exception e) {
            System.out.println("ValidacionesResource -catch ERROR" + e.toString());
            e.printStackTrace();
            return new ResponseMessenger().getResponseError("Error interno");
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json;charset=UTF-8")
    @Path("/subir/{IdToCarga}")
    public Response subirAnexo(MultipartFormDataInput input,
            @PathParam("IdToCarga") Long IdToCarga) {
        try {
            System.out.println("---------------------------");
            System.out.println("metodo sincronizar con imagen");

            Adjunto adjunto = new Adjunto();

            byte[] imgToValidarPlaca;

            try {
                Map<String, List<InputPart>> temp = input.getFormDataMap();
                temp.forEach((k, v) -> System.out.println("Key: " + k + ": Value: " + v.size()));
                String uuid = UUID.randomUUID().toString();
                InputPart filePart = temp.get("file").get(0);
                InputStream inputStream = filePart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                String fileName = ConfiguracionGeneral.RUTA + "imagenes" + File.separator + uuid + ".jpg";
                if (!writeFile(bytes, fileName)) {
                    System.out.println("Error: subirAnexo3: problema al guardar en el modelo");
                    return new ResponseMessenger().getResponseError("Problema interno del servidor");
                }
                imgToValidarPlaca = bytes;
                adjunto.setUuid(uuid);
            } catch (IOException e) {
                System.out.println("Error: subirAnexo2 " + e.getMessage());
                return new ResponseMessenger().getResponseError("Problema interno del servidor");
            }

            if (IdToCarga != null) {
                ControlRegistro cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "id", IdToCarga);
                if (cr != null) {
                    adjunto.setControlRegistro(cr);
                    cb.guardar(adjunto);
                    openalpr.getPlaca(imgToValidarPlaca, cr.getId());
                    return new ResponseMessenger().getResponseOk("se sincronizo");
                } else {
                    return new ResponseMessenger().getResponseError("IdToCarga no existe");
                }
            } else {
                return new ResponseMessenger().getResponseError("Error url");
            }
        } catch (Exception e) {
            System.out.println("Error: subirAnexo2 " + e.getLocalizedMessage());
            return new ResponseMessenger().getResponseError("Problema interno del servidor");
        }
    }

    private Boolean writeFile(byte[] content, String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fop = new FileOutputStream(file);
            fop.write(content);
            fop.flush();
            fop.close();
            System.out.println("Guargo archivo");
            return Boolean.TRUE;
        } catch (IOException e) {
            System.out.println("Error: writeFile: " + e.getLocalizedMessage());
            return Boolean.FALSE;
        }
    }

}
