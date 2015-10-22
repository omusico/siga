package es.icarto.gvsig.extgia.forms.taludes;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateTaludesTrabajosLongitud extends CalculateDBForeignValue {

    public CalculateTaludesTrabajosLongitud(Map<String, String> foreingKey) {
	super(foreingKey, DBFieldNames.LONGITUD, DBFieldNames.TALUDES_LONGITUD,
		DBFieldNames.TALUDES_DBTABLENAME, DBFieldNames.ID_TALUD);
    }
}
