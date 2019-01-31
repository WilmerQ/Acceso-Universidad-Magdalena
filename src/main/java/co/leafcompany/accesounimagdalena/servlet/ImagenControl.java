/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.servlet;

import co.leafcompany.accesounimagdalena.base.ConfiguracionGeneral;
import co.leafcompany.accesounimagdalena.logica.CommonsBean;
import co.leafcompany.accesounimagdalena.modelo.Adjunto;
import co.leafcompany.accesounimagdalena.modelo.ControlRegistro;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import static java.lang.Math.toIntExact;

/**
 *
 * @author wilme
 */
@WebServlet(urlPatterns = {"/imagenservlet"})
public class ImagenControl extends HttpServlet {

    @EJB
    private CommonsBean cb;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idDeImagen = request.getParameter("id");
        String numeroImagen = request.getParameter("nmu");
        System.out.println("+++++++++ " + idDeImagen);
        System.out.println("+++++++++ " + numeroImagen);

        try {
//            Gson gson = new Gson();
            Long numeroImg = Long.parseLong(numeroImagen);
            ControlRegistro cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "id", Long.parseLong(idDeImagen));
            List<Adjunto> adjuntos = (List<Adjunto>) cb.getByOneField(Adjunto.class, "controlRegistro", cr);
            if (!adjuntos.isEmpty()) {
                if (adjuntos.size() == 1) {
                    File f = new File(ConfiguracionGeneral.RUTA + "imagenes" + File.separator + adjuntos.get(0).getUuid() + ".jpg");
                    byte[] data = FileUtils.readFileToByteArray(f);
                    response.setContentType("image");
                    response.setContentLength(data.length);
                    response.getOutputStream().write(data);
                } else {
                    if (numeroImg != null) {
                        if (numeroImg >= 0 && numeroImg < adjuntos.size()) {
                            System.out.println("aqui con numero valido");
                            File f = new File(ConfiguracionGeneral.RUTA + "imagenes" + File.separator + adjuntos.get(toIntExact(numeroImg)).getUuid() + ".jpg");
                            byte[] data = FileUtils.readFileToByteArray(f);
                            response.setContentType("image");
                            response.setContentLength(data.length);
                            response.getOutputStream().write(data);
                        } else {
                            System.out.println("numero fuera de rango");
                            File f = new File(ConfiguracionGeneral.RUTA + "imagenes" + File.separator + adjuntos.get(0).getUuid() + ".jpg");
                            byte[] data = FileUtils.readFileToByteArray(f);
                            response.setContentType("image");
                            response.setContentLength(data.length);
                            response.getOutputStream().write(data);
                        }
                    } else {
                        System.out.println("numero null");
                        File f = new File(ConfiguracionGeneral.RUTA + "imagenes" + File.separator + adjuntos.get(0).getUuid() + ".jpg");
                        byte[] data = FileUtils.readFileToByteArray(f);
                        response.setContentType("image");
                        response.setContentLength(data.length);
                        response.getOutputStream().write(data);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: ImagenEventos-doGet: " + e.getLocalizedMessage());
        }

    }

}
