/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.vista;

import co.leafcompany.accesounimagdalena.base.ConfiguracionGeneral;
import co.leafcompany.accesounimagdalena.clases.datosTabla;
import co.leafcompany.accesounimagdalena.logica.CommonsBean;
import co.leafcompany.accesounimagdalena.logica.LogicaTablas;
import co.leafcompany.accesounimagdalena.modelo.Adjunto;
import co.leafcompany.accesounimagdalena.modelo.ControlRegistro;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author wilme
 */
@ViewScoped
@ManagedBean(name = "MbTablas")
public class MbTablas implements Serializable {

    //private List<Date> fechasExistentes;
    private List<String> fechasExistentesToString;
    private String ruta;
    @EJB
    private CommonsBean cb;
    @EJB
    private LogicaTablas lt;
    private ControlRegistro cr;
    private List<ControlRegistro> crs;
    private int idTab = 0;

    List<datosTabla> tablas;

    public MbTablas() {
    }

    @PostConstruct
    public void init() {
        ruta = ConfiguracionGeneral.RUTA;

        fechasExistentesToString = new ArrayList<>();
//        List<Date> listWithoutDuplicates = new ArrayList<>();
//        List<ControlRegistro> temp = cb.getAll(ControlRegistro.class);
//        for (ControlRegistro t : temp) {
//            listWithoutDuplicates.add(t.getFechaCreacion());
//        }
//        fechasExistentes = listWithoutDuplicates.stream().distinct().collect(Collectors.toList());
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//        for (Date d : fechasExistentes) {
//            fechasExistentesToString.add(format.format(d));
//        }
//        List<String> aux = fechasExistentesToString;
//        fechasExistentesToString = aux.stream().distinct().collect(Collectors.toList());
        List<String> temp = lt.obtenerFechasToString();
        for (int i = 0; i < temp.size(); i++) {
            fechasExistentesToString.add(i, temp.get(temp.size() - 1 - i));
        }

        //fechasExistentesToString =;
        idTab = fechasExistentesToString.size() - 1;

        tablas = new ArrayList<>();
        for (String f : fechasExistentesToString) {
            tablas.add(new datosTabla(f, registroXFechas(f)));
        }
    }

    public List<ControlRegistro> registroXFechas(String fecha) {
        try {
            return lt.obtenerregistrosXfecha(fecha);
        } catch (Exception e) {
            //System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            List<ControlRegistro> controlRegistros = new ArrayList<>();
            return controlRegistros;
        }
    }

//    public List<ControlRegistro> listToBuscar(String fecha) {
//        try {
//            System.out.println("listobuscar " + fecha);
//            for (int i = 0; i < fechasExistentesToString.size(); i++) {
//                System.out.println("listobuscar fehcas existentes " + fechasExistentesToString.get(i));
//                if (fechasExistentesToString.get(i).equals(fecha)) {
//                    System.out.println("list to buscar " + i);
////                    return crss2[i];
//                    return crss.get(i);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return null;
//    }
    public void onTabChange(TabChangeEvent event) {
        System.out.println("Tab Changed Active Tab: " + event.getTab().getTitle());
//        List<ControlRegistro> crs;
//        registroXFechas(event.getTab().getTitle());
    }

    public List<String> buscarImagenes() {
        try {
            List<String> nombresImagenes = new ArrayList<>();
            List<Adjunto> adjuntos = cb.getByOneField(Adjunto.class, "controlRegistro", cr);
            if (!adjuntos.isEmpty()) {
                for (int a = 0; a < adjuntos.size(); a++) {
                    nombresImagenes.add("" + a);
                }
                return nombresImagenes;
            } else {
                nombresImagenes.add("0");
                return nombresImagenes;
            }
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
            return null;
        }
    }

    public boolean buscarXPlaca(Object value, Object filter, Locale locale) {
        try {
            Object[] properties = value.toString().split(";");
            System.out.println("properties[0] " + properties[0].toString());
            System.out.println("properties[1] " + properties[1].toString());
            System.out.println("aqui en buscaxplaca " + filter.toString());

            int posicion = 0;

            for (int i = 0; i < fechasExistentesToString.size(); i++) {
                System.out.println("listobuscar fehcas existentes " + fechasExistentesToString.get(i));
                if (fechasExistentesToString.get(i).equals(properties[1].toString())) {
                    System.out.println("list to buscar " + i);
                    posicion = i;
                }
            }
            System.out.println("posicion " + posicion);
            if (properties[0].toString().equals("placa")) {
                crs = lt.obtenerRegistroXplaca(properties[1].toString(), filter.toString());
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getFechasExistentesToString() {
        return fechasExistentesToString;
    }

    public void setFechasExistentesToString(List<String> fechasExistentesToString) {
        this.fechasExistentesToString = fechasExistentesToString;
    }

    public ControlRegistro getCr() {
        return cr;
    }

    public void setCr(ControlRegistro cr) {
        this.cr = cr;
    }

    public List<ControlRegistro> getCrs() {
        return crs;
    }

    public void setCrs(List<ControlRegistro> crs) {
        this.crs = crs;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getIdTab() {
        return idTab;
    }

    public void setIdTab(int idTab) {
        this.idTab = idTab;
    }

    public List<datosTabla> getTablas() {
        return tablas;
    }

    public void setTablas(List<datosTabla> tablas) {
        this.tablas = tablas;
    }

}
