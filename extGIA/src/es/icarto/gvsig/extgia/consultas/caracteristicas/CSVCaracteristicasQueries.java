package es.icarto.gvsig.extgia.consultas.caracteristicas;

import es.icarto.gvsig.extgia.consultas.ConsultasFieldNames;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CSVCaracteristicasQueries {

    public static String getCSVCaracteristicasQuery(String element) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Areas_Descanso:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    ", (select count(id_ramal) from audasa_extgia.areas_descanso_ramales ra " +
		    "where ra.id_area_descanso = el.id_area_descanso) as \"Nº Ramales\"" +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTables() +
		    ", audasa_extgia.areas_descanso_ramales ra" +
		    getLocalizationWhere();
	case Areas_Servicio:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    ", (select count(id_ramal) from audasa_extgia.areas_servicio_ramales ra " +
		    "where ra.id_area_servicio = el.id_area_servicio) as \"Nº Ramales\"" +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTables() +
		    ", audasa_extgia.areas_servicio_ramales ra" +
		    getLocalizationWhere();
	case Firme:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    " audasa_extgia_dominios.area_mantenimiento am," +
		    " audasa_extgia_dominios.base_contratista bc," +
		    " audasa_extgia_dominios.tramo tr" +
		    " WHERE el.area_mantenimiento = am.id" +
		    " AND el.base_contratista = bc.id" +
		    " AND el.tramo = tr.id";
	default:
	    return "SELECT " + ConsultasFieldNames.getCSVCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTables() +
		    getLocalizationWhere();
	}

    }

    private static String getLocalizationTables() {
	return " audasa_extgia_dominios.area_mantenimiento am," +
		" audasa_extgia_dominios.base_contratista bc," +
		" audasa_extgia_dominios.tramo tr," +
		" audasa_extgia_dominios.tipo_via tv," +
		" audasa_extgia_dominios.nombre_via nv";
    }

    private static String getLocalizationWhere() {
	return " WHERE el.area_mantenimiento = am.id" +
		" AND el.base_contratista = bc.id" +
		" AND el.tramo = tr.id" +
		" AND el.tipo_via = tv.id" +
		" AND el.nombre_via = cast (nv.id as text)";
    }

}
