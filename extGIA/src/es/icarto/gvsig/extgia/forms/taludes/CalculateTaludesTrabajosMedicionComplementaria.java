package es.icarto.gvsig.extgia.forms.taludes;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateTaludesTrabajosMedicionComplementaria extends
	CalculateDBForeignValue implements CalculateForeignValue {

    public CalculateTaludesTrabajosMedicionComplementaria(
	    Map<String, String> foreingKey) {
	super(foreingKey, DBFieldNames.MEDICION_COMPLEMENTARIA,
		DBFieldNames.SUP_COMPLEMENTARIA,
		DBFieldNames.TALUDES_DBTABLENAME, DBFieldNames.ID_TALUD);
    }
}
