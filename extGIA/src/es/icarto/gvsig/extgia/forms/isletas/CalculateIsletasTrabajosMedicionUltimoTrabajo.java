package es.icarto.gvsig.extgia.forms.isletas;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.CalculateForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateIsletasTrabajosMedicionUltimoTrabajo extends
CalculateDBForeignValueLastJob implements CalculateForeignValue {

    public CalculateIsletasTrabajosMedicionUltimoTrabajo(
	    Map<String, String> foreingKey) {
	super(foreingKey);
    }

    public CalculateIsletasTrabajosMedicionUltimoTrabajo(
	    Map<String, String> foreingKey, String unidad) {
	super(foreingKey, unidad);
    }

    @Override
    public String getComponentName() {
	return DBFieldNames.MEDICION_ULTIMO_TRABAJO;
    }

    @Override
    protected String getTableName() {
	return DBFieldNames.ISLETAS_TRABAJOS_DBTABLENAME;
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_ISLETA;
    }

}
