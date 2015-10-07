package es.icarto.gvsig.extgia.forms.barrera_rigida;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.CalculateForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateBarreraRigidaTrabajosMedicionUltimoTrabajo extends
CalculateDBForeignValueLastJob implements CalculateForeignValue {

    public CalculateBarreraRigidaTrabajosMedicionUltimoTrabajo(
	    Map<String, String> foreingKey) {
	super(foreingKey);
    }

    public CalculateBarreraRigidaTrabajosMedicionUltimoTrabajo(
	    Map<String, String> foreingKey, String unidad) {
	super(foreingKey, unidad);
    }

    @Override
    public String getComponentName() {
	return DBFieldNames.MEDICION_ULTIMO_TRABAJO;
    }

    @Override
    protected String getTableName() {
	return DBFieldNames.BARRERA_RIGIDA_TRABAJOS_DBTABLENAME;
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_BARRERA_RIGIDA;
    }

}
