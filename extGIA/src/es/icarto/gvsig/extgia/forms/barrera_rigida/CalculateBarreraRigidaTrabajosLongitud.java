package es.icarto.gvsig.extgia.forms.barrera_rigida;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateBarreraRigidaTrabajosLongitud extends
CalculateDBForeignValue {

    public CalculateBarreraRigidaTrabajosLongitud(Map<String, String> foreingKey) {
	super(foreingKey);
    }

    @Override
    protected String getComponentName() {
	return DBFieldNames.LONGITUD;
    }

    @Override
    protected String getForeignField() {
	return DBFieldNames.BARRERA_RIGIDA_LONGITUD;
    }

    @Override
    protected String getTableName() {
	return DBFieldNames.BARRERA_RIGIDA_DBTABLENAME;
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_BARRERA_RIGIDA;
    }

}
