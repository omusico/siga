package es.icarto.gvsig.extgia.consultas.caracteristicas;

import es.icarto.gvsig.commons.utils.Field;
import es.icarto.gvsig.extgia.consultas.ConsultasFieldNames;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class PDFCaracteristicasQueries {

    public static String getPDFCaracteristicasQuery(String element,
	    ConsultasFilters<Field> filters) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Areas_Descanso:
	case Areas_Mantenimiento:
	case Areas_Servicio:
	case Areas_Peaje:
	case Enlaces:
	case Taludes:
	case Valla_Cierre:
	    return "SELECT gid, "
		    + ConsultasFieldNames
			    .getPDFCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el" + CSVCaracteristicasQueries.getJoinedTramo()
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Firme:
	    return "SELECT gid, "
		    + ConsultasFieldNames
			    .getPDFCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";
	case Senhalizacion_Vertical:
	    return "SELECT gid, el.id_elemento_senhalizacion, tr.item, tv.item, nv.item, pk, tipo_senhal, "
		    + "codigo_senhal, leyenda, panel_complementario, codigo_panel, texto_panel, "
		    + "fecha_fabricacion, fecha_instalacion, fecha_reposicion, tipo_sustentacion "
		    + " FROM "
		    + DBFieldNames.GIA_SCHEMA
		    + "."
		    + element
		    + " el LEFT OUTER JOIN audasa_extgia.senhalizacion_vertical_senhales se ON el.id_elemento_senhalizacion = se.id_elemento_senhalizacion"
		    + CSVCaracteristicasQueries.getJoinedTramo() + CSVCaracteristicasQueries.getJoinedTipoVia() + CSVCaracteristicasQueries.getJoinedNombreVia()
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY el.id_elemento_senhalizacion";
	default:
	    return "SELECT gid, "
		    + ConsultasFieldNames.getPDFCaracteristicasFieldNames(element)
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " el" + CSVCaracteristicasQueries.getJoinedTramo() + CSVCaracteristicasQueries.getJoinedTipoVia() + CSVCaracteristicasQueries.getJoinedNombreVia()
		    + filters.getWhereClauseByLocationWidgets(false)
		    + " ORDER BY gid";

	}
    }

}
