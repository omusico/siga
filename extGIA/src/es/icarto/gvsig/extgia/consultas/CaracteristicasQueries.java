package es.icarto.gvsig.extgia.consultas;

import es.icarto.gvsig.commons.utils.Field;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CaracteristicasQueries {

    public static String getPDFCaracteristicasQuery(String element,
	    ConsultasFilters<Field> filters) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Firme:
	    return "SELECT gid, "
	    + ConsultasFieldNames
	    .getPDFCaracteristicasFieldNames(element)
	    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Senhalizacion_Vertical:
	    return "SELECT gid, "
		    + ConsultasFieldNames
			    .getPDFCaracteristicasFieldNames(element)
		    + CaracteristicasQueries
			    .getFromClauseCaracteristicas(element)
		    + filters.getWhereClauseByLocationWidgets()
		    + " ORDER BY el.id_elemento_senhalizacion";
	default:
	    return "SELECT gid, "
	    + ConsultasFieldNames
			    .getPDFCaracteristicasFieldNames(element)
	    + CaracteristicasQueries
			    .getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";

	}
    }

    public static String getReconocimientosTrabajosQuery(String element,
	    ConsultasFilters<Field> filters, String fields, String elementId,
	    String tipoConsulta) {
	String query = "";
	if (element.equalsIgnoreCase("senhalizacion_vertical")) {
	    query = "SELECT "
		    + fields
		    + getFromClauseReconocimientosTrabajos(element, elementId,
			    tipoConsulta);
	} else {
	    query = "SELECT "
		    + fields
		    + getFromClauseReconocimientosTrabajos(element, elementId,
			    tipoConsulta);
	}

	return query;
    }

    public static String getFromClauseReconocimientosTrabajos(String element,
	    String elementId, String tipoConsulta) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Senhalizacion_Vertical:
	    return " FROM "
		    + DBFieldNames.GIA_SCHEMA
		    + "."
		    + element
		    + "_"
		    + tipoConsulta
		    + " AS sub JOIN "
		    + DBFieldNames.GIA_SCHEMA
		    + "."
		    + element
		    + " AS el ON sub."
		    + elementId
		    + "= el."
		    + elementId
		    + " LEFT OUTER JOIN audasa_extgia.senhalizacion_vertical_senhales se ON (el.id_elemento_senhalizacion = se.id_elemento_senhalizacion) "
		    + CaracteristicasQueries.get(element);
	default:
	    return " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + "_"
		    + tipoConsulta + " AS sub JOIN " + DBFieldNames.GIA_SCHEMA
		    + "." + element + " AS el ON sub." + elementId + "= el."
		    + elementId + CaracteristicasQueries.get(element);
	}

    }

    public static String getFromClauseCaracteristicas(String element) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Senhalizacion_Vertical:
	    return " FROM  audasa_extgia.senhalizacion_vertical_senhales se LEFT OUTER JOIN audasa_extgia.senhalizacion_vertical el ON (el.id_elemento_senhalizacion = se.id_elemento_senhalizacion) "
		    + get(element);
	default:
	    return " FROM audasa_extgia." + element + " el " + get(element);
	}
    }

    public static String get(String element) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Senhalizacion_Vertical:
	    return getLocalizationTablesWithSentido();
	case Areas_Descanso:
	case Areas_Servicio:
	case Areas_Mantenimiento:
	case Juntas:
	case Isletas:
	case Barrera_Rigida:
	case Obras_Paso:
	case Obras_Desague:
	case Areas_Peaje:
	case Lecho_Frenado:
	case Senhalizacion_Variable:
	case Transformadores:
	    return getLocalizationTablesWithSentido();

	case Firme:
	    return getFirmeLocalizationTables();

	case Valla_Cierre:
	case Taludes:
	case Muros:
	case Lineas_Suministro:
	    return getLocalizationTablesWithSentidoAndPF();

	case Enlaces:
	default:
	    return getLocalizationTables();
	}

    }

    public static String getCSVCaracteristicasQuery(String element,
	    ConsultasFilters<Field> filters) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Areas_Descanso:
	    return "SELECT " + ConsultasFieldNames.areasDescansoCSVFieldNames()
	    + getFieldCountRamales(element)
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Areas_Servicio:
	    return "SELECT " + ConsultasFieldNames.areasServicioCSVFieldNames()
	    + getFieldCountRamales(element)
		    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Areas_Mantenimiento:
	    return "SELECT "
	    + ConsultasFieldNames.areasMantenimientoCSVFieldNames()
	    + getFieldCountRamales(element)
		    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Enlaces:
	    return "SELECT "
	    + ConsultasFieldNames.enlacesCSVFieldNames()
	    + ", (select count(id_ramal) from audasa_extgia.enlaces_ramales ra "
	    + "where ra.id_enlace = el.id_enlace) "
	    + "|| ' | ' || "
	    + "(select array_to_string(array_agg(clave_carretera), ';') "
	    + "from audasa_extgia.enlaces_carreteras_enlazadas ce "
	    + "where ce.id_enlace = el.id_enlace)"
	    + "as \"Nº Ramales | Carreteras Enlazadas\""
		    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Senhalizacion_Vertical:
	    return "SELECT " + ConsultasFieldNames.senhalizacionCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY el.id_elemento_senhalizacion";
	case Firme:
	    return "SELECT " + ConsultasFieldNames.firmeCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Valla_Cierre:
	    return "SELECT " + ConsultasFieldNames.vallaCierreCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Taludes:
	    return "SELECT " + ConsultasFieldNames.taludesCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Juntas:
	    return "SELECT " + ConsultasFieldNames.juntasCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Isletas:
	    return "SELECT " + ConsultasFieldNames.isletasCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Barrera_Rigida:
	    return "SELECT " + ConsultasFieldNames.barreraRigidaCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Obras_Paso:
	    return "SELECT " + ConsultasFieldNames.obrasPasoCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Obras_Desague:
	    return "SELECT " + ConsultasFieldNames.obrasDesagueCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Areas_Peaje:
	    return "SELECT " + ConsultasFieldNames.areasPeajeCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Lecho_Frenado:
	    return "SELECT " + ConsultasFieldNames.lechoFrenadoCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Muros:
	    return "SELECT " + ConsultasFieldNames.murosCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Senhalizacion_Variable:
	    return "SELECT "
	    + ConsultasFieldNames.senhalizacionVariableCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Lineas_Suministro:
	    return "SELECT "
	    + ConsultasFieldNames.lineasSuministroCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Transformadores:
	    return "SELECT "
	    + ConsultasFieldNames.transformadoresCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	case Pasos_Mediana:
	    return "SELECT " + ConsultasFieldNames.pasosMedianaCSVFieldNames()
	    + getFromClauseCaracteristicas(element)
	    + filters.getWhereClauseByLocationWidgets()
	    + " ORDER BY gid";
	}
	return null;

    }

    private static String getFieldCountRamales(String element) {
	String table = element.toLowerCase();
	String elementId = ConsultasFieldNames.getElementId(element);
	return String
		.format(", (select count(id_ramal) from audasa_extgia.%s_ramales ra where ra.%s = el.%s) as \"Nº Ramales\"",
			table, elementId, elementId);
    }

    // area_mantenimiento, base_contratista, tramo, municipio, tipo_via,
    // nombre_via
    private static String getLocalizationTables() {
	return getJoinedArea() + getJoinedBase() + getJoinedTramo()
		+ getJoinedMunicipio() + getJoinedTipoVia()
		+ getJoinedNombreVia();
    }

    // area_mantenimiento, base_contratista, tramo, municipio, tipo_via,
    // nombre_via, sentido
    private static String getLocalizationTablesWithSentido() {
	return getJoinedArea() + getJoinedBase() + getJoinedTramo()
		+ getJoinedMunicipio() + getJoinedTipoVia()
		+ getJoinedNombreVia() + getJoinedSentido();
    }

    // area_mantenimiento, base_contratista, tramo, municipio, tipo_via,
    // nombre_via, sentido + pf
    private static String getLocalizationTablesWithSentidoAndPF() {
	return getJoinedArea()
		+ getJoinedBase()
		+ getJoinedTramo()
		+ getJoinedMunicipio()
		+ getJoinedTipoVia()
		+ getJoinedNombreVia()
		+ getJoinedSentido()
		+ " LEFT OUTER JOIN audasa_extgia_dominios.tipo_via tvf ON (tvf.id = el.tipo_via_pf AND tvf.id_tramo = el.tramo AND tvf.id_bc = el.base_contratista) LEFT OUTER JOIN audasa_extgia_dominios.nombre_via nvf ON (cast (nvf.id as text) = el.nombre_via_pf AND nvf.id_tv = el.tipo_via AND nvf.id_bc = el.base_contratista AND nvf.id_tramo = el.tramo) ";
    }

    // area_mantenimiento, base_contratista, tramo, municipio
    private static String getFirmeLocalizationTables() {
	return getJoinedArea() + getJoinedBase() + getJoinedTramo()
		+ getJoinedMunicipio();
    }

    private static String getJoinedArea() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.area_mantenimiento am ON am.id = el.area_mantenimiento ";
    }

    private static String getJoinedBase() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.base_contratista bc ON bc.id = el.base_contratista ";
    }

    public static String getJoinedTramo() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.tramo tr ON (tr.id = el.tramo AND tr.id_bc = el.base_contratista) ";
    }

    private static String getJoinedMunicipio() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.municipio mu ON (mu.id = el.municipio AND mu.id_tramo = el.tramo AND mu.id_bc = el.base_contratista)";
    }

    public static String getJoinedTipoVia() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.tipo_via tv ON (tv.id = el.tipo_via AND tv.id_tramo = el.tramo AND tv.id_bc = el.base_contratista) ";
    }

    public static String getJoinedNombreVia() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.nombre_via nv ON (cast (nv.id as text) = el.nombre_via AND nv.id_tv = el.tipo_via AND nv.id_bc = el.base_contratista AND nv.id_tramo = el.tramo) ";
    }

    public static String getJoinedSentido() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.sentido st ON el.sentido = st.id ";
    }

}
