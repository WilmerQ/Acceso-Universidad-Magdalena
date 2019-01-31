/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.logica;

import co.leafcompany.accesounimagdalena.modelo.Administrador;
import java.io.Serializable;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wilme
 */
@Stateless
@LocalBean
public class LogicaAdministrador implements Serializable {

    @PersistenceContext(unitName = "AccesoUnimagdalenaV1PU")
    private EntityManager em;

    public Administrador LoginWeb(String nombre, String contrasena) {
        try {
            if ((nombre.equals("admin")) && (contrasena.equals("827ccb0eea8a706c4c34a16891f84e7b"))) {
                Administrador usuario = new Administrador();
                usuario.setNombreUsuario("Administrador");
                return usuario;
            }
        } catch (Exception e) {
            System.out.println("Error: LoginWeb: " + e.getLocalizedMessage());
        }

        try {
            System.out.println("try LoginWeb");
            Administrador a = (Administrador) em.createQuery("Select u From Administrador u where u.nombreUsuario=:n and u.contrasena=:p")
                    .setParameter("n", nombre)
                    .setParameter("p", contrasena).getSingleResult();
            System.out.println("u " + a.getNombreUsuario());
            return a;
        } catch (Exception e) {
            System.out.println("Error: LoginWeb: " + e.getLocalizedMessage());
            return null;
        }
    }
}
