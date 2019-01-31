/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.logica;

import co.leafcompany.accesounimagdalena.clases.candidate;
import co.leafcompany.accesounimagdalena.clases.jsonResOpenalpr;
import co.leafcompany.accesounimagdalena.modelo.ControlRegistro;
import co.leafcompany.accesounimagdalena.modelo.Placa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 *
 * @author wilme
 */
@Stateless
@LocalBean
public class LogicaOpenalpr implements Serializable {

    @EJB
    private CommonsBean cb;

    public List<String> getPlaca(byte[] placa, Long idControlRegistro) {
        try {
            System.out.println("----------------------");
            System.out.println("Fecha inicio " + new Date());
//            String secret_key = "sk_DEMODEMODEMODEMODEMODEMO";
            String secret_key = "sk_1d4df9282c157d7e7d0a6efc";
            // Read image file to byte array
//            Path path = Paths.get("/storage/projects/alpr/samples/testing/car1.jpg");
//            byte[] data = Files.readAllBytes(path);
            // Encode file bytes to base64
            byte[] encoded = Base64.getEncoder().encode(placa);

            // Setup the HTTPS connection to api.openalpr.com
            URL url = new URL("https://api.openalpr.com/v2/recognize_bytes?recognize_vehicle=1&country=us&secret_key=" + secret_key);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setFixedLengthStreamingMode(encoded.length);
            http.setDoOutput(true);

            // Send our Base64 content over the stream
            try (OutputStream os = http.getOutputStream()) {
                os.write(encoded);
            }

            int status_code = http.getResponseCode();
            if (status_code == 200) {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        http.getInputStream()));
                String json_content = "";
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    json_content += inputLine;
                }
                in.close();

//                System.out.println(json_content);
                ControlRegistro cr = (ControlRegistro) cb.getByOneFieldWithOneResult(ControlRegistro.class, "id", idControlRegistro);
                Gson g = new GsonBuilder().create();
                jsonResOpenalpr ro = g.fromJson(json_content, jsonResOpenalpr.class);
                if (!ro.getResults().isEmpty()) {
                    List<String> plates = new ArrayList();
                    for (candidate c : ro.getResults().get(0).getCandidates()) {
                        System.out.println("---------------");
                        System.out.println("candidate " + c.getPlate());
                        System.out.println("candidate" + c.getConfidence());
                        System.out.println("candidate" + c.getMatches_template());
                        if (c.getPlate().length() >= 6) {
                            plates.add(c.getPlate());
                            System.out.println("placa agregada " + c.getPlate());
                            Placa p = new Placa();
                            p.setConfidence(c.getConfidence());
                            p.setControlRegistro(cr);
                            p.setPlate(c.getPlate());
                            cb.guardar(p);
                        }
                    }
                    System.out.println("Fecha fin 2" + new Date());
                    return plates;
                } else {
                    System.out.println("no hay placa");
                    return null;
                }
            } else {
                System.out.println("Got non-200 response: " + status_code);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
            return null;
        } catch (Exception ex) {
            Logger.getLogger(LogicaOpenalpr.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void actualizarRegistrosSinPlaca(){
        try {
            
        } catch (Exception e) {
        }
    }

}
