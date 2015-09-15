package es.icarto.gvsig.extgia.consultas.caracteristicas;

import es.icarto.gvsig.commons.utils.Field;
import es.icarto.gvsig.extgia.consultas.ConsultasFieldNames;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class PDFCaracteristicasQueries {

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
	    return "SELECT gid, " + ConsultasFieldNames.getPDFCaracteristicasFieldNames(element)
		    + CSVCaracteristicasQueries.getFromClauseCaracteristicas(element)
		    + filters.getWhereClauseByLocationWidgets()
		    + " ORDER BY el.id_elemento_senhalizacion";
	default:
	    return "SELECT gid, "
		    + ConsultasFieldNames.getPDFCaracteristicasFieldNames(element)
		    + CSVCaracteristicasQueries.getFromClauseCaracteristicas(element)
		    + filters.getWhereClauseByLocationWidgets()
		    + " ORDER BY gid";

	}
    }

}
