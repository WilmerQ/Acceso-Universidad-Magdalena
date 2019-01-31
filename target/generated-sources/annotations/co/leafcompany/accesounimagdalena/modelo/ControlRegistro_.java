package co.leafcompany.accesounimagdalena.modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ControlRegistro.class)
public abstract class ControlRegistro_ extends co.leafcompany.accesounimagdalena.modelo.CamposComunesdeEntidad_ {

	public static volatile SingularAttribute<ControlRegistro, String> estadoTransaccion;
	public static volatile SingularAttribute<ControlRegistro, Long> codigo;
	public static volatile SingularAttribute<ControlRegistro, Usuario> usuario;
	public static volatile SingularAttribute<ControlRegistro, String> terminal;
	public static volatile SingularAttribute<ControlRegistro, String> mensaje;

}

