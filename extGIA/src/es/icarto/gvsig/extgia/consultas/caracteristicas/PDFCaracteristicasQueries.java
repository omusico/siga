package es.icarto.gvsig.extgia.consultas.caracteristicas;

import es.icarto.gvsig.extgia.consultas.ConsultasFieldNames;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class PDFCaracteristicasQueries {

    public static final String getSenhalizacionVerticalQuery(ConsultasFilters filters) {
	return "SELECT distinct(gid), sv.id_elemento_senhalizacion, tr.item, tv.item, nv.item, pk, tipo_senhal, " +
		"codigo_senhal, leyenda, panel_complementario, codigo_panel, texto_panel, " +
		"fecha_fabricacion, fecha_instalacion, fecha_reposicion, tipo_sustentacion " +
		" FROM audasa_extgia.senhalizacion_vertical sv," +
		" audasa_extgia.senhalizacion_vertical_senhales se," +
		" audasa_extgia_dominios.tramo tr," +
		" audasa_extgia_dominios.tipo_via tv," +
		" audasa_extgia_dominios.nombre_via nv" +
		" WHERE sv.id_elemento_senhalizacion = se.id_elemento_senhalizacion" +
		" AND sv.tramo = tr.id AND sv.tipo_via = tv.id" +
		" AND sv.nombre_via = cast (nv.id as text)" +
		filters.getWhereClauseByLocationWidgets(true) +
		" ORDER BY sv.id_elemento_senhalizacion";
    }

    public static String getPDFCaracteristicasQuery(String element, ConsultasFilters filters) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Areas_Descanso:
	case Areas_Servicio:
	case Areas_Peaje:
	case Enlaces:
	case Taludes:
	case Muros:
	case Valla_Cierre:
	    return "SELECT distinct(gid), " + ConsultasFieldNames.getPDFCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getTramoTable() +
		    getTramoWhere()+
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";
	case Firme:
	    return "SELECT distinct(gid), " + ConsultasFieldNames.getPDFCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element +
		    filters.getWhereClauseByLocationWidgets(false) +
		    " ORDER BY gid";
	default:
	    return "SELECT distinct(gid), " + ConsultasFieldNames.getPDFCaracteristicasFieldNames(element) +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element + " el," +
		    getLocalizationTables() +
		    getLocalizationWhere()+
		    filters.getWhereClauseByLocationWidgets(true) +
		    " ORDER BY gid";

	}
    }

    private static String getLocalizationTables() {
	return " audasa_extgia_dominios.tramo tr," +
		" audasa_extgia_dominios.tipo_via tv," +
		" audasa_extgia_dominios.nombre_via nv";
    }

    private static String getLocalizationWhere() {
	return " WHERE el.tramo = tr.id" +
		" AND el.tipo_via = tv.id" +
		" AND el.nombre_via = cast (nv.id as text)";
    }

    private static String getTramoTable() {
	return " audasa_extgia_dominios.tramo tr";
    }

    private static String getTramoWhere() {
	return " WHERE el.tramo = tr.id";
    }
}
