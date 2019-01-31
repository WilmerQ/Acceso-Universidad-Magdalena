/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.base;

/**
 *
 * @author wilme
 */
public class ConfiguracionGeneral {

    public ConfiguracionGeneral() {
        String sSistemaOperativo = System.getProperty("os.name");
        System.out.println("------------------ " + sSistemaOperativo);
        if (sSistemaOperativo.contains("Windows")) {
            RUTA = "C:\\Users\\wilme\\Desktop\\prueba\\";
        } else if (sSistemaOperativo.contains("Linux")) {
            RUTA = "/home/acira/accesoAdjuntos/";
        }
    }

    public static String RUTA;

    //ruta para almacenar archivos
    //static final public String RUTA = "D:\\AciraAdjuntos\\";
    //static final public String RUTA = "C:\\Users\\wilme\\Desktop\\prueba\\";
//    static final public String RUTA = "/home/acira/accesoAdjuntos/";
//static final public String RUTA = "/home/acira/AciraAdjuntos/";
}
