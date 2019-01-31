/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.logica;

import co.leafcompany.accesounimagdalena.modelo.ControlRegistro;
import co.leafcompany.accesounimagdalena.modelo.Placa;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wilme
 */
@Stateless
@LocalBean
public class LogicaTablas implements Serializable {

    @PersistenceContext(unitName = "AccesoUnimagdalenaV1PU")
    private EntityManager em;

    @EJB
    private CommonsBean cb;

//    public List<ControlRegistro> ValidarUsuario(String fecha) {
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//            Date fecha1 = format.parse(fecha);
//            List<ControlRegistro> controlRegistros = new ArrayList<>();
//            List<ControlRegistro> registros = cb.getAll(ControlRegistro.class);
//            for (ControlRegistro r : registros) {
//                Long l = fecha1.getTime() + 86400000;
//                Date fecha2 = new Date(l);
//                if (r.getFechaCreacion().after(fecha1) && r.getFechaCreacion().before(fecha2)) {
//                    controlRegistros.add(r);
//                }
//            }
//            System.out.println("registros para " + fecha + " son: " + controlRegistros.size());
//            return controlRegistros;
//        } catch (ParseException e) {
////            System.out.println("error-----" + e.getMessage());
//            e.printStackTrace();
//            List<ControlRegistro> temp = new ArrayList<>();
//            return temp;
//        }
//    }
    public List<ControlRegistro> ValidarUsuarioSql(String fecha) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date d = new Date(dateFormat.parse(fecha).getTime() + 86400000);
//            System.out.println("fecha inferior " + d);
//            System.out.println("fecha superior " + fecha);
            String temp = dateFormat.format(d);
            temp = temp.replaceAll("/", "-");
            fecha = fecha.replaceAll("/", "-");
            Query query = em.createNativeQuery("SELECT cr.*\n"
                    + "FROM \n"
                    + "public.controlregistro cr\n"
                    + "where cr.fechacreacion BETWEEN '" + fecha + "' and '" + temp + "'\n"
                    + "order by cr.id;", ControlRegistro.class);
//            System.out.println("registros para " + fecha + " son: " + ((List<ControlRegistro>) query.getResultList()).size());
            return (List<ControlRegistro>) query.getResultList();
        } catch (ParseException e) {
//            System.out.println("error-----" + e.getMessage());
            e.printStackTrace();
            List<ControlRegistro> temp = new ArrayList<>();
            return temp;
        }
    }

    public List<ControlRegistro> obtenerregistrosXfecha(String fecha) {
        try {
//            System.out.println("obtenerregistrosXfecha " + fecha);
//            List<ControlRegistro> temp = ValidarUsuario(fecha);
            List<ControlRegistro> temp = ValidarUsuarioSql(fecha);
            if (!temp.isEmpty()) {
                for (ControlRegistro controlRegistro1 : temp) {
                    List<Placa> placas = cb.getByOneField(Placa.class, "controlRegistro", controlRegistro1);
                    if (!placas.isEmpty()) {
                        controlRegistro1.setPlacas(placas);
//                        System.out.println("placas asinagas a control registro 1 " + controlRegistro1.getPlacas().size());
                    } else {
//                        System.out.println("control registro 1 sin placa" + controlRegistro1.getId());
                    }
                }
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            List<ControlRegistro> controlRegistros = new ArrayList<>();
            return controlRegistros;
        }
    }

    public List<ControlRegistro> obtenerRegistroXplaca(String fecha, String value) {
        try {
            value = value.toUpperCase();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date d = new Date(dateFormat.parse(fecha).getTime() + 86400000);
            System.out.println("fecha inferior " + d);
            System.out.println("fecha superior " + fecha);
            String temp = dateFormat.format(d);
            temp = temp.replaceAll("/", "-");
            fecha = fecha.replaceAll("/", "-");
            Query query = em.createNativeQuery("SELECT cr.*\n"
                    + "FROM \n"
                    + "public.placa p\n"
                    + "join public.controlregistro cr on p.controlregistro_id = cr.id\n"
                    + "and cr.fechacreacion BETWEEN '" + fecha + "' and '" + temp + "'\n"
                    + "and p.plate like '%" + value + "%'\n"
                    + "group by cr.id;", ControlRegistro.class);
            return (List<ControlRegistro>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            List<ControlRegistro> aux = new ArrayList<>();
            return aux;
        }
    }

    public List<Placa> obtenerPlacas(String temp) {
        try {
            return em.createQuery("SELECT p FROM Placa p WHERE p.plate LIKE :t").setParameter("t", temp).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> obtenerFechasToString() {
        try {
            Query query = em.createNativeQuery("select distinct \n"
                    + "to_char(cr.fechacreacion, 'YYYY/MM/DD') as fc\n"
                    + "from \n"
                    + "controlregistro as cr\n"
                    + "order by fc desc \n"
                    + "limit 5;");
            //System.out.println("registros para " + fecha + " son: " + ((List<ControlRegistro>) query.getResultList()).size());
            return (List<String>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            List<String> aList = new ArrayList<>();
            return aList;
        }
    }
}
