package es.icarto.gvsig.extgia.forms.isletas;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.utils.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.utils.CalculateForeignValue;
import es.icarto.gvsig.extgia.forms.utils.ForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateIsletasTrabajosMedicionElemento extends
CalculateDBForeignValue implements CalculateForeignValue {

    public CalculateIsletasTrabajosMedicionElemento(
	    Map<String, String> foreingKey) {
	super(foreingKey);
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
	return DBFieldNames.SUPERFICIE_BAJO_BIONDA;
    }

    @Override
    protected String getTableName() {
	return DBFieldNames.ISLETAS_DBTABLENAME;
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_ISLETA;
    }

}
