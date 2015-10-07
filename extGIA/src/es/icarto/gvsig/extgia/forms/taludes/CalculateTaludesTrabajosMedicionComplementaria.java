package es.icarto.gvsig.extgia.forms.taludes;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateForeignValue;
import es.icarto.gvsig.extgia.forms.ForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateTaludesTrabajosMedicionComplementaria extends
CalculateDBForeignValue implements CalculateForeignValue {

    public CalculateTaludesTrabajosMedicionComplementaria(
	    Map<String, String> foreingKey) {
	super(foreingKey);
    }

    @Override
    public ForeignValue getForeignValue() {
	return new ForeignValue(getComponentName(), getValue());
    }

    @Override
    public String getComponentName() {
	return DBFieldNames.MEDICION_COMPLEMENTARIA;
    }

    @Override
    protected String getForeignField() {
	return DBFieldNames.SUP_COMPLEMENTARIA;
    }

    @Override
    protected String getTableName() {
	return DBFieldNames.TALUDES_DBTABLENAME;
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_TALUD;
    }

}
