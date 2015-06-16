package es.icarto.gvsig.extgia.forms.taludes;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.utils.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.utils.CalculateForeignValue;
import es.icarto.gvsig.extgia.forms.utils.ForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateTaludesTrabajosMedicionElemento extends
CalculateDBForeignValue implements CalculateForeignValue {

    public CalculateTaludesTrabajosMedicionElemento(
	    Map<String, String> foreignKey) {
	super(foreignKey);
    }

    @Override
    public ForeignValue getForeignValue() {
	return new ForeignValue(getComponentName(), getValue());
    }

    @Override
    public String getComponentName() {
	return DBFieldNames.MEDICION_ELEMENTO;
    }

    @Override
    protected String getForeignField() {
	return DBFieldNames.SUP_TOTAL_ANALITICA;
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
