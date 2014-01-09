package es.icarto.gvsig.extgia.consultas.caracteristicas;

import es.icarto.gvsig.extgia.consultas.ConsultasFieldNames;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CSVCaracteristicasQueries {

    public static String getCSVCaracteristicasQuery(String element, ConsultasFilters filters) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Areas_Descanso:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    ", (select count(id_ramal) from audasa_extgia.areas_descanso_ramales ra " +
		    "where ra.id_area_descanso = el.id_area_descanso) as \"Nº Ramales\"" +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    ", audasa_extgia.areas_descanso_ramales ra" +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Areas_Servicio:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    ", (select count(id_ramal) from audasa_extgia.areas_servicio_ramales ra " +
		    "where ra.id_area_servicio = el.id_area_servicio) as \"Nº Ramales\"" +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    ", audasa_extgia.areas_servicio_ramales ra" +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Enlaces:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    ", (select count(id_ramal) from audasa_extgia.enlaces_ramales ra " +
		    "where ra.id_enlace = el.id_enlace) " + "|| ' | ' || " +
		    "(select array_to_string(array_agg(clave_carretera), ';') " +
		    "from audasa_extgia.enlaces_carreteras_enlazadas ce " +
		    "where ce.id_enlace = el.id_enlace)" +
		    "as \"Nº Ramales | Carreteras Enlazadas\"" +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTables() +
		    getLocalizationWhere() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Senhalizacion_Vertical:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    " audasa_extgia.senhalizacion_vertical_senhales se," +
		    getLocalizationTablesWithSentido() +
		    getLocalizationWhereWithSentido() +
		    " AND el.id_elemento_senhalizacion = se.id_elemento_senhalizacion" +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " order by el.id_elemento_senhalizacion";
	case Firme:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    " audasa_extgia_dominios.area_mantenimiento am," +
		    " audasa_extgia_dominios.base_contratista bc," +
		    " audasa_extgia_dominios.tramo tr," +
		    " audasa_extgia_dominios.municipio mu" +
		    " WHERE el.area_mantenimiento = am.id" +
		    " AND el.base_contratista = bc.id" +
		    " AND el.tramo = tr.id" +
		    " AND el.municipio = mu.id" +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Valla_Cierre:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentidoAndPF() +
		    getLocalizationWhereWithSentidoAndPF() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Taludes:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentidoAndPF() +
		    getLocalizationWhereWithSentidoAndPF() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Juntas:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Isletas:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Barrera_Rigida:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Obras_Paso:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Obras_Desague:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Areas_Peaje:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Lecho_Frenado:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Muros:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentidoAndPF() +
		    getLocalizationWhereWithSentidoAndPF() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Senhalizacion_Variable:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTablesWithSentido() +
		    getLocalizationWhereWithSentido() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	default:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTables() +
		    getLocalizationWhere() +
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	}

    }

    private static String getLocalizationTables() {
	return " audasa_extgia_dominios.area_mantenimiento am," +
		" audasa_extgia_dominios.base_contratista bc," +
		" audasa_extgia_dominios.tramo tr," +
		" audasa_extgia_dominios.tipo_via tv," +
		" audasa_extgia_dominios.nombre_via nv," +
		" audasa_extgia_dominios.municipio mu";
    }

    private static String getLocalizationWhere() {
	return " WHERE el.area_mantenimiento = am.id" +
		" AND el.base_contratista = bc.id" +
		" AND el.tramo = tr.id" +
		" AND el.tipo_via = tv.id" +
		" AND el.nombre_via = cast (nv.id as text)" +
		" AND el.municipio = mu.id";
    }

    private static String getLocalizationTablesWithSentido() {
	return " audasa_extgia_dominios.area_mantenimiento am," +
		" audasa_extgia_dominios.base_contratista bc," +
		" audasa_extgia_dominios.tramo tr," +
		" audasa_extgia_dominios.tipo_via tv," +
		" audasa_extgia_dominios.nombre_via nv," +
		" audasa_extgia_dominios.municipio mu," +
		" audasa_extgia_dominios.sentido st";
    }

    private static String getLocalizationWhereWithSentido() {
	return " WHERE el.area_mantenimiento = am.id" +
		" AND el.base_contratista = bc.id" +
		" AND el.tramo = tr.id" +
		" AND el.tipo_via = tv.id" +
		" AND el.nombre_via = cast (nv.id as text)" +
		" AND el.municipio = mu.id" +
		" AND el.sentido = st.id";
    }

    private static String getLocalizationTablesWithSentidoAndPF() {
	return " audasa_extgia_dominios.area_mantenimiento am," +
		" audasa_extgia_dominios.base_contratista bc," +
		" audasa_extgia_dominios.tramo tr," +
		" audasa_extgia_dominios.tipo_via tv," +
		" audasa_extgia_dominios.nombre_via nv," +
		" audasa_extgia_dominios.tipo_via tvf," +
		" audasa_extgia_dominios.nombre_via nvf," +
		" audasa_extgia_dominios.municipio mu," +
		" audasa_extgia_dominios.sentido st";
    }

    private static String getLocalizationWhereWithSentidoAndPF() {
	return " WHERE el.area_mantenimiento = am.id" +
		" AND el.base_contratista = bc.id" +
		" AND el.tramo = tr.id" +
		" AND el.tipo_via = tv.id" +
		" AND el.nombre_via = cast (nv.id as text)" +
		" AND el.tipo_via_pf = tv.id" +
		" AND el.nombre_via_pf = cast (nv.id as text)" +
		" AND el.municipio = mu.id" +
		" AND el.tipo_via_pf = tvf.id" +
		" AND el.nombre_via_pf = cast (nvf.id as text)" +
		" AND el.sentido = st.id";
    }

}
