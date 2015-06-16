package es.icarto.gvsig.extgia.forms.taludes;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.utils.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.utils.CalculateForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateTaludesTrabajosLongitud extends CalculateDBForeignValue implements CalculateForeignValue {

    public CalculateTaludesTrabajosLongitud(Map<String, String> foreingKey) {
	super(foreingKey);
    }

    @Override
    public String getComponentName() {
	return DBFieldNames.LONGITUD;
    }

    @Override
    protected String getTableName() {
	return DBFieldNames.TALUDES_DBTABLENAME;
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_TALUD;
    }

    @Override
    protected String getForeignField() {
	return DBFieldNames.TALUDES_LONGITUD;
    }


}
