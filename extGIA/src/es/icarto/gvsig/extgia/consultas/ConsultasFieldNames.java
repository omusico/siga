package es.icarto.gvsig.extgia.consultas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.icarto.gvsig.extgia.consultas.caracteristicas.AreasDescansoCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.BarreraRigidaCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.IsletasCaracteristicasReport;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ConsultasFieldNames {

    public static String getTrabajosFieldNames(String elementId) {
	return elementId + ", fecha, unidad, medicion_contratista, medicion_audasa, " +
		"observaciones, fecha_certificado";
    }

    public static String getFirmeTrabajosFieldNames(String elementId) {
	return elementId + ", fecha, pk_inicial, pk_final, sentido, " +
		"descripcion, fecha_certificado";
    }

    public static String getReconocimientosFieldNames(String elementId) {
	return elementId + ", nombre_revisor, fecha_inspeccion, indice_estado, observaciones";
    }

    public static String getFirmeReconocimientosFieldNames(String elementId) {
	return elementId + ", tipo_inspeccion, nombre_revisor, aparato_medicion," +
		"fecha_inspeccion, observaciones";
    }

    public static String getCaracteristicasFieldNames(String element) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Taludes:
	    break;
	case Isletas:
	    return "id_isleta, tipo_via, nombre_via, pk_inicial, pk_final, tipo_isleta," +
	    "superficie_bajo_bionda, posibilidad_empleo_vehiculos, observaciones";
	case Barrera_Rigida:
	    return "id_barrera_rigida, tipo_via, nombre_via, pk_inicial, pk_final, obstaculo_protegido" +
	    ", longitud, codigo, tipo, metodo_constructivo, perfil, observaciones";
	case Areas_Servicio:
	    break;
	case Areas_Descanso:
	    return "nombre, tipo_via, nombre_via, municipio, pk, fecha_puesta_servicio, sup_total," +
	    "sup_pavimentada, aceras, bordillos, zona_siega, zona_ajardinada, riego," +
	    "aparcamiento_camion_bus, area_picnic, fuentes_potables, observaciones";
	case Juntas:
	    break;
	case Pasos_Mediana:
	    break;
	case Senhalizacion_Vertical:
	    break;
	case Valla_Cierre:
	    break;
	case Firme:
	    break;
	}
	return null;
    }

    public static void createCaracteristicasReport(String[] element, String outputFile,
	    ResultSet rs, String[] filters) {
	switch (DBFieldNames.Elements.valueOf(element[0])) {
	case Taludes:
	    break;
	case Isletas:
	    new IsletasCaracteristicasReport(element[1], outputFile, rs, filters);
	    break;
	case Barrera_Rigida:
	    new BarreraRigidaCaracteristicasReport(element[1], outputFile, rs, filters);
	    break;
	case Areas_Servicio:
	    break;
	case Areas_Descanso:
	    new AreasDescansoCaracteristicasReport(element[1], outputFile, rs, filters);
	    break;
	case Juntas:
	    break;
	case Pasos_Mediana:
	    break;
	case Senhalizacion_Vertical:
	    break;
	case Valla_Cierre:
	    break;
	case Firme:
	    break;
	}
    }

    public static String getElementId(String element) {
	PreparedStatement statement;
	String query = "SELECT id_fieldname FROM audasa_extgia_dominios.elemento " +
		"WHERE id = '" + element + "';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.next();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static String getWhereClauseByLocationWidgets(String area, String baseContratista, String tramo) {
	String query = "";
	if (area != null) {
	    query = " WHERE area_mantenimiento =  '" + area + "'";
	}
	if (baseContratista != null) {
	    query = " WHERE base_contratista =  '" + baseContratista + "'";
	}
	if (tramo != null) {
	    query = " WHERE tramo =  '" + tramo + "'";
	}
	return query;
    }

}
