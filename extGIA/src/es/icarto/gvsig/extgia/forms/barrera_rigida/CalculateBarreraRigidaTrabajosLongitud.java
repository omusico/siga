package es.icarto.gvsig.extgia.forms.barrera_rigida;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateBarreraRigidaTrabajosLongitud extends
	CalculateDBForeignValue {

    public CalculateBarreraRigidaTrabajosLongitud(Map<String, String> foreingKey) {
	super(foreingKey, DBFieldNames.LONGITUD,
		DBFieldNames.BARRERA_RIGIDA_LONGITUD,
		DBFieldNames.BARRERA_RIGIDA_DBTABLENAME,
		DBFieldNames.ID_BARRERA_RIGIDA);
    }

}
