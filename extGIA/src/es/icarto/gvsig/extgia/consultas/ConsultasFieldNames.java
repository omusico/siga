package es.icarto.gvsig.extgia.consultas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.icarto.gvsig.extgia.consultas.caracteristicas.AreasDescansoCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.AreasServicioCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.BarreraRigidaCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.EnlacesCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.IsletasCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.JuntasCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.PasosMedianaCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.VallaCierreCaracteristicasReport;
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

    public static String getPDFCaracteristicasFieldNames(String element) {
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
	    return "nombre, tipo_via, nombre_via, municipio, pk, fecha_puesta_servicio, sup_total," +
	    "sup_pavimentada, aceras, bordillos, zona_siega, zona_ajardinada, riego," +
	    "cafeteria_rest_bar, aparcamiento_camion_bus, area_picnic, fuentes_potables, observaciones";
	case Areas_Descanso:
	    return "nombre, tipo_via, nombre_via, municipio, pk, fecha_puesta_servicio, sup_total," +
	    "sup_pavimentada, aceras, bordillos, zona_siega, zona_ajardinada, riego," +
	    "aparcamiento_camion_bus, area_picnic, fuentes_potables, observaciones";
	case Enlaces:
	    return "nombre, tipo_via, nombre_via, municipio, pk, n_salida, tipo_enlace, alumbrado," +
	    "observaciones";
	case Juntas:
	    return "tramo, tipo_via, nombre_via, pk, numero_junta, ancho, modulo, elemento," +
	    "codigo_elemento, descripcion, observaciones";
	case Pasos_Mediana:
	    return "tramo, tipo_via, nombre_via, pk, longitud, numero_postes, cierre, longitud_cierre," +
	    "cuneta_entubada, observaciones";
	case Senhalizacion_Vertical:
	    break;
	case Valla_Cierre:
	    return "pk_inicial, pk_final, tipo_valla, longitud, altura, n_panhos, n_puertas," +
	    "n_postes_simples, n_postes_tripode, pastor_electrico, observaciones";
	case Firme:
	    break;
	}
	return null;
    }

    public static String getCSVCaracteristicasFieldNames(String element) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Taludes:
	    return taludesCSVFieldNames();
	case Isletas:
	    return isletasCSVFieldNames();
	case Barrera_Rigida:
	    return barreraRigidaCSVFieldNames();
	case Areas_Servicio:
	    return areasServicioCSVFieldNames();
	case Areas_Descanso:
	    return areasDescansoCSVFieldNames();
	case Enlaces:
	    return enlacesCSVFieldNames();
	case Juntas:
	    return juntasCSVFieldNames();
	case Pasos_Mediana:
	    return pasosMedianaCSVFieldNames();
	case Senhalizacion_Vertical:
	    return senhalizacionCSVFieldNames();
	case Valla_Cierre:
	    return vallaCierreCSVFieldNames();
	case Firme:
	    return firmeCSVFieldNames();
	}
	return null;
    }

    private static String localizationCSVFieldNames() {
	return "am.item as \"Area Mantenimiento\"," +
		"bc.item as \"Base Contratista\"," +
		"tr.item as \"Tramo\"," +
		"tv.item as \"Tipo Vía\"," +
		"nv.item as \"Nombre Vía\",";
    }

    private static String taludesCSVFieldNames() {
	return "id_talud as \"ID Talud\"," +
		"numero_talud as \"Nº Talud\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"tipo_talud as \"Tipo Talud\"," +
		"roca as \"Roca\"," +
		"arboles as \"Árboles\"," +
		"gunita as \"Gunita\"," +
		"escollera as \"Escollera\"," +
		"maleza as \"Maleza\"," +
		"malla as \"Malla\"," +
		"observaciones as \"Observaciones\"," +
		"arcen as \"Arcén\"," +
		"barrera_seguridad as \"Barrera Seguridad\"," +
		"cuneta_pie as \"Cuneta Pie\"," +
		"cuneta_pie_revestida as \"Cuneta Pie Revestida\"," +
		"cuneta_cabeza as \"Cuneta Cabezada\"," +
		"cuneta_cabeza_revestida as \"Cuneta Cabeza Revestida\"," +
		"berma as \"Berma\"," +
		"longitud as \"Longitud\"," +
		"sector_inclinacion as \"Sector Inclinación\"," +
		"inclinacion_media as \"Inclinación Media\"," +
		"altura_max_talud as \"Altura Máxima Talud\"," +
		"sup_total_analitica as \"Superficie Total Analítica\"," +
		"sup_mecanizada_analitica as \"Superficie Mecanizada Analítica\"," +
		"sup_manual_analitica as \"Superficie Manual Analítica\"," +
		"sup_restada_analitica as \"Superficie Restada Analítica\"," +
		"sup_total_campo as \"Superficie Total Campo\"," +
		"sup_mecanizada_campo as \"Superficie Mecanizada Campo\"," +
		"sup_restada_campo as \"Superficie Restada Campo\"," +
		"sup_manual_campo as \"Superficie Manual Campo\"," +
		"sup_complementaria as \"Superficie Complementaria\"," +
		"concepto as \"Concepto\"";
    }

    private static String isletasCSVFieldNames() {
	return "id_isleta as \"ID Isleta\"," +
		"numero_isleta as \"Nº Isleta\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"tipo_isleta as \"Tipo Isleta\"," +
		"superficie_bajo_bionda as \"Superficie Bajo Bionda\"," +
		"posibilidad_empleo_vehiculos as \"Posibilidad Empleo Vehículos\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String barreraRigidaCSVFieldNames() {
	return "id_barrera_rigida as \"ID Barrera Rígida\"," +
		"numero_barrera_rigida as \"Nº Barrera Rígida\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"obstaculo_protegido as \"Obstáculo Protegido\"," +
		"longitud as \"Longitud\"," +
		"codigo as \"Código\"," +
		"tipo as \"Tipo\"," +
		"metodo_constructivo as \"Método Constructivo\"," +
		"perfil as \"Perfil\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String areasServicioCSVFieldNames() {
	return "id_area_servicio as \"ID Área Servicio\"," +
		"nombre as \"Nombre\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"fecha_puesta_servicio as \"Fecha Puesta Servicio\"," +
		"sup_total as \"Superficie Total\"," +
		"sup_pavimentada as \"Superficie Pavimentada\"," +
		"aceras as \"Aceras\"," +
		"bordillos as \"Bordillos\"," +
		"zona_siega as \"Zona Siega\"," +
		"zona_ajardinada as \"Zona Ajardinada\"," +
		"riego as \"Riego\"," +
		"cafeteria_rest_bar as \"Cafetería\"," +
		"aparcamiento_camion_bus as \"Aparcamientos\"," +
		"area_picnic as \"Área Picnic\"," +
		"fuentes_potables as \"Fuentes Potables\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String areasDescansoCSVFieldNames() {
	return "id_area_descanso as \"ID Área Descanso\"," +
		"nombre as \"Nombre\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"fecha_puesta_servicio as \"Fecha Puesta Servicio\"," +
		"sup_total as \"Superficie Total\"," +
		"sup_pavimentada as \"Superficie Pavimentada\"," +
		"aceras as \"Aceras\"," +
		"bordillos as \"Bordillos\"," +
		"zona_siega as \"Zona Siega\"," +
		"zona_ajardinada as \"Zona Ajardinada\"," +
		"riego as \"Riego\"," +
		"aparcamiento_camion_bus as \"Aparcamientos\"," +
		"area_picnic as \"Área Picnic\"," +
		"fuentes_potables as \"Fuentes Potables\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String pasosMedianaCSVFieldNames() {
	return "id_paso_mediana as \"ID Paso Mediana\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"longitud as \"Longitud\"," +
		"numero_postes as \"Nº Postes\"," +
		"cierre as \"Cierre\"," +
		"longitud_cierre as \"Longitud Cierre\"," +
		"cuneta_entubada as \"Cuneta Entubada\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String juntasCSVFieldNames() {
	return "id_junta as \"ID Junta\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"numero_junta as \"Nº Junta\"," +
		"ancho as \"Ancho\"," +
		"modulo as \"Módulo\"," +
		"elemento as \"Elemento\"," +
		"codigo_elemento as \"Código Elemento\"," +
		"descripcion as \"Descripción\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String enlacesCSVFieldNames() {
	return "id_enlace as \"ID Enlace\"," +
		"nombre as \"Nombre\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"n_salida as \"Nº Salida\"," +
		"tipo_enlace as \"Tipo Enlace\"," +
		"alumbrado as \"Alumbrado\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String senhalizacionCSVFieldNames() {
	return "id_elemento_senhalizacion as \"ID Elemento\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"tipo_sustentacion as \"Tipo Sustentación\"," +
		"material_sustentacion as \"Material Sustentación\"," +
		"tipo_poste as \"Tipo Poste\"," +
		"numero_postes as \"Nº Postes\"," +
		"anclaje as \"Anclaje\"," +
		"cimentacion_especial as \"Cimentación Especial\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String vallaCierreCSVFieldNames() {
	return "id_valla as \"ID Valla\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"tipo_valla as \"Tipo Valla\"," +
		"longitud as \"Longitud\"," +
		"altura as \"Altura\"," +
		"n_panhos as \"Nº Paños\"," +
		"n_puertas as \"Nº Puertas\"," +
		"n_postes_simples as \"Nº Postes Simples\"," +
		"n_postes_tripode as \"Nº Postes Trípode\"," +
		"pastor_electrico as \"Pastor Eléctrico\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String firmeCSVFieldNames() {
	return "id_firme as \"ID Firme\"," +
		"fecha_inauguracion as \"Fecha Inauguración\"," +
		"fecha_apertura as \"Fecha Apertura\"," +
		"unidad_constructiva as \"Unidad Constructiva\"," +
		"am.item as \"Area Mantenimiento\"," +
		"bc.item as \"Base Contratista\"," +
		"tr.item as \"Tramo\"," +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"explanada_cm as \"Explanada (cm)\"," +
		"zahorra_artificial as \"Zahorra Artificial (cm)\"," +
		"suelo_cemento_cm as \"Suelo Cemento (cm)\"," +
		"grava_cemento_cm as \"Grava Cemento (cm)\"," +
		"mbc_base_cm as \"MBC Base (cm)\"," +
		"mbc_intermedia_cm as \"MBC Intermedia (cm)\"," +
		"mbc_rodadura_cm as \"MBC Rodadura (cm)\"," +
		"explanada as \"Materiales: Explanada\"," +
		"zahorra_artificial as \"Materiales: Zahorra Artificial\"," +
		"suelo_cemento as \"Materiales: Suelo Cemento\"," +
		"gc_arido_grueso as \"Grava-Cemento: Árido Grueso\"," +
		"gc_arido_fino as \"Grava-Cemento: Árido Fino\"," +
		"gc_cemento as \"Grava-Cemento: Cemento\"," +
		"mbc_bas_huso as \"MBC Base: Huso (cm)\"," +
		"mbc_bas_arido_grueso as \"MBC Base: Árido Grueso (cm)\"," +
		"mbc_bas_arido_fino \"MBC Base: Árido Fino (cm)\"," +
		"mbc_bas_filler as \"MBC Base: Filler\"," +
		"mbc_bas_ligante as \"MBC Base: Ligante\"," +
		"mbc_rod_huso as \"MBC Rodadura: Huso (cm)\"," +
		"mbc_rod_arido_grueso as \"MBC Rodadura: Árido Grueso (cm)\"," +
		"mbc_rod_arido_fino \"MBC Rodadura: Árido Fino (cm)\"," +
		"mbc_rod_filler as \"MBC Rodadura: Filler\"," +
		"mbc_rod_ligante as \"MBC Rodadura: Ligante\"," +
		"observaciones as \"Observaciones\"";
    }

    public static void createCaracteristicasReport(String[] element, String outputFile,
	    ResultSet rs, ConsultasFilters filters) {
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
	    new AreasServicioCaracteristicasReport(element[1], outputFile, rs, filters);
	    break;
	case Areas_Descanso:
	    new AreasDescansoCaracteristicasReport(element[1], outputFile, rs, filters);
	    break;
	case Enlaces:
	    new EnlacesCaracteristicasReport(element[1], outputFile, rs, filters);
	    break;
	case Juntas:
	    new JuntasCaracteristicasReport(element[1], outputFile, rs, filters);
	    break;
	case Pasos_Mediana:
	    new PasosMedianaCaracteristicasReport(element[1], outputFile, rs, filters);
	    break;
	case Senhalizacion_Vertical:
	    break;
	case Valla_Cierre:
	    new VallaCierreCaracteristicasReport(element[1], outputFile, rs, filters);
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

}
