package es.icarto.gvsig.extgia.consultas.caracteristicas;

import es.icarto.gvsig.commons.queries.Field;
import es.icarto.gvsig.extgia.consultas.ConsultasFieldNames;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CSVCaracteristicasQueries {

    public static String get(String element) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Areas_Descanso:
	case Areas_Servicio:
	case Areas_Mantenimiento:
	case Senhalizacion_Vertical:
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
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + getFieldCountRamales(element) + " FROM audasa_extgia."
		    + element + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Areas_Servicio:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + getFieldCountRamales(element) + " FROM audasa_extgia."
		    + element + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Areas_Mantenimiento:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + getFieldCountRamales(element) + " FROM audasa_extgia."
		    + element + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Enlaces:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + ", (select count(id_ramal) from audasa_extgia.enlaces_ramales ra "
		    + "where ra.id_enlace = el.id_enlace) "
		    + "|| ' | ' || "
		    + "(select array_to_string(array_agg(clave_carretera), ';') "
		    + "from audasa_extgia.enlaces_carreteras_enlazadas ce "
		    + "where ce.id_enlace = el.id_enlace)"
		    + "as \"Nº Ramales | Carreteras Enlazadas\"" + " FROM "
		    + DBFieldNames.GIA_SCHEMA + "." + element + " el "
		    + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Senhalizacion_Vertical:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM  audasa_extgia.senhalizacion_vertical_senhales se LEFT OUTER JOIN audasa_extgia.senhalizacion_vertical el ON (el.id_elemento_senhalizacion = se.id_elemento_senhalizacion) "
		    + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY el.id_elemento_senhalizacion";
	case Firme:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Valla_Cierre:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Taludes:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Juntas:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Isletas:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Barrera_Rigida:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Obras_Paso:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Obras_Desague:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Areas_Peaje:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Lecho_Frenado:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Muros:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Senhalizacion_Variable:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Lineas_Suministro:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Transformadores:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	default:
	    return "SELECT "
		    + ConsultasFieldNames
			    .getCSVCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el " + get(element)
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	}

    }

    private static String getFieldCountRamales(String element) {
	String table = element.toLowerCase();
	String elementId = ConsultasFieldNames.getElementId(element);
	return String
		.format(", (select count(id_ramal) from audasa_extgia.%s_ramales ra where ra.%s = el.%s) as \"Nº Ramales\"",
			table, elementId, elementId);
    }

    private static String getLocalizationTables() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.area_mantenimiento am ON am.id = el.area_mantenimiento LEFT OUTER JOIN audasa_extgia_dominios.base_contratista bc ON bc.id = el.base_contratista LEFT OUTER JOIN audasa_extgia_dominios.tramo tr ON (tr.id = el.tramo AND tr.id_bc = el.base_contratista) LEFT OUTER JOIN audasa_extgia_dominios.municipio mu ON mu.id = el.municipio LEFT OUTER JOIN audasa_extgia_dominios.tipo_via tv ON (tv.id = el.tipo_via AND tv.id_tramo = el.tramo AND tv.id_bc = el.base_contratista) LEFT OUTER JOIN audasa_extgia_dominios.nombre_via nv ON (cast (nv.id as text) = el.nombre_via AND nv.id_tv = el.tipo_via AND nv.id_bc = el.base_contratista AND nv.id_tramo = el.tramo) ";
    }

    private static String getLocalizationTablesWithSentido() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.area_mantenimiento am ON am.id = el.area_mantenimiento LEFT OUTER JOIN audasa_extgia_dominios.base_contratista bc ON bc.id = el.base_contratista LEFT OUTER JOIN audasa_extgia_dominios.tramo tr ON (tr.id = el.tramo AND tr.id_bc = el.base_contratista) LEFT OUTER JOIN audasa_extgia_dominios.municipio mu ON mu.id = el.municipio LEFT OUTER JOIN audasa_extgia_dominios.tipo_via tv ON (tv.id = el.tipo_via AND tv.id_tramo = el.tramo AND tv.id_bc = el.base_contratista) LEFT OUTER JOIN audasa_extgia_dominios.nombre_via nv ON (cast (nv.id as text) = el.nombre_via AND nv.id_tv = el.tipo_via AND nv.id_bc = el.base_contratista AND nv.id_tramo = el.tramo) LEFT OUTER JOIN audasa_extgia_dominios.sentido st ON el.sentido = st.id ";
    }

    private static String getLocalizationTablesWithSentidoAndPF() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.area_mantenimiento am ON am.id = el.area_mantenimiento LEFT OUTER JOIN audasa_extgia_dominios.base_contratista bc ON bc.id = el.base_contratista LEFT OUTER JOIN audasa_extgia_dominios.tramo tr ON (tr.id = el.tramo AND tr.id_bc = el.base_contratista) LEFT OUTER JOIN audasa_extgia_dominios.municipio mu ON mu.id = el.municipio LEFT OUTER JOIN audasa_extgia_dominios.tipo_via tv ON (tv.id = el.tipo_via AND tv.id_tramo = el.tramo AND tv.id_bc = el.base_contratista) LEFT OUTER JOIN audasa_extgia_dominios.nombre_via nv ON (cast (nv.id as text) = el.nombre_via AND nv.id_tv = el.tipo_via AND nv.id_bc = el.base_contratista AND nv.id_tramo = el.tramo) LEFT OUTER JOIN audasa_extgia_dominios.sentido st ON el.sentido = st.id LEFT OUTER JOIN audasa_extgia_dominios.tipo_via tvf ON (tvf.id = el.tipo_via_pf AND tvf.id_tramo = el.tramo AND tvf.id_bc = el.base_contratista) LEFT OUTER JOIN audasa_extgia_dominios.nombre_via nvf ON (cast (nvf.id as text) = el.nombre_via_pf AND nvf.id_tv = el.tipo_via AND nvf.id_bc = el.base_contratista AND nvf.id_tramo = el.tramo) ";
    }

    private static String getFirmeLocalizationTables() {
	return " LEFT OUTER JOIN audasa_extgia_dominios.area_mantenimiento am ON am.id = el.area_mantenimiento LEFT OUTER JOIN audasa_extgia_dominios.base_contratista bc ON bc.id = el.base_contratista LEFT OUTER JOIN audasa_extgia_dominios.tramo tr ON (tr.id = el.tramo AND tr.id_bc = el.base_contratista) LEFT OUTER JOIN audasa_extgia_dominios.municipio mu ON mu.id = el.municipio ";
    }

}
