package es.icarto.gvsig.extgia.forms.taludes;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateTaludesTrabajosMedicionElemento extends
	CalculateDBForeignValue {

    public CalculateTaludesTrabajosMedicionElemento(
	    Map<String, String> foreignKey) {
	super(foreignKey, DBFieldNames.MEDICION_ELEMENTO,
		DBFieldNames.SUP_TOTAL_ANALITICA,
		DBFieldNames.TALUDES_DBTABLENAME, DBFieldNames.ID_TALUD);
    }

}
