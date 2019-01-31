/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.logica;

import co.leafcompany.accesounimagdalena.modelo.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wilme
 */
@Stateless
@LocalBean
public class LogicaSincronizarBDLocal implements Serializable {

    @PersistenceContext(unitName = "AccesoUnimagdalenaV1PU")
    private EntityManager em;

    @EJB
    private CommonsBean cb;
    @EJB
    private LogicaUnimagdalena lu;

    @Schedule(hour = "3", minute = "0", second = "0")
    public void verificarUsuarios() {
        System.out.println("ejecutando tarea automatica para verificar los usuarios activos");
        try {
            Date d = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            System.out.println("format.format(d)" + format.format(d));
            if ((format.format(d).equals("03:00"))) {
                System.out.println("hora para actualizar");
                List<Usuario> aux = new ArrayList<>();
                List<Usuario> usuarios = cb.getAll(Usuario.class);
                usuarios.forEach((u) -> {
//                System.out.println("************* antes de 1");
                    Usuario temp = lu.ValidarUsuario(u.getIdTarjeta());
//                System.out.println("************* antes de 2");
                    if (temp == null) {
//                    System.out.println("************* antes de 3");
                        u.setEstado(Boolean.FALSE);
                        u.setFechaultimaValidacion(new Date());
                        aux.add(u);
                    } else {
//                    System.out.println("************* antes de 4");
                        u.setEstado(Boolean.TRUE);
                        u.setFechaultimaValidacion(new Date());
                        aux.add(u);
                    }
                });
                for (Usuario u : aux) {
                    try {
                        System.out.println("antes de actualizar");
                        System.out.println("u " + u.getCargo());
                        System.out.println("u " + u.getDependencia());
                        System.out.println("u " + u.getNombreCompleto());
                        System.out.println("u " + u.getCargo());
                        System.out.println("u " + u.getCargo());
                        em.merge(u);
                        System.out.println("despues de actualizar");
                    } catch (Exception e) {
                        System.out.println("error " + e.getMessage());
                    }
                }
            } else {
                System.out.println("no es hora de actualizar");
            }
        } catch (Exception e) {
            System.out.println("ERROR: LogicaSincronizarBDLocal - verificarUsuarios: " + e.getMessage());
        }
    }

}
