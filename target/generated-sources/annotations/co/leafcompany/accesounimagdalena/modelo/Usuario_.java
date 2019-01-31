package co.leafcompany.accesounimagdalena.modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Usuario.class)
public abstract class Usuario_ extends co.leafcompany.accesounimagdalena.modelo.CamposComunesdeEntidad_ {

	public static volatile SingularAttribute<Usuario, Long> idTarjeta;
	public static volatile SingularAttribute<Usuario, Date> fechaultimaValidacion;
	public static volatile SingularAttribute<Usuario, String> nombreCompleto;
	public static volatile SingularAttribute<Usuario, String> cargo;
	public static volatile SingularAttribute<Usuario, String> dependencia;

}

