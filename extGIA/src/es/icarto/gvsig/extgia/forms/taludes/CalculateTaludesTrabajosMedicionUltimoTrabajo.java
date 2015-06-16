package es.icarto.gvsig.extgia.forms.taludes;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.utils.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.utils.CalculateForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateTaludesTrabajosMedicionUltimoTrabajo extends
CalculateDBForeignValueLastJob implements CalculateForeignValue {

    public CalculateTaludesTrabajosMedicionUltimoTrabajo(
	    Map<String, String> foreignKey) {
	super(foreignKey);
    }

    public CalculateTaludesTrabajosMedicionUltimoTrabajo(
	    Map<String, String> foreingKey, String unidad) {
	super(foreingKey,unidad);
    }

    @Override
    public String getComponentName() {
	return DBFieldNames.MEDICION_ULTIMO_TRABAJO;
    }

    @Override
    protected String getTableName() {
	return DBFieldNames.TALUDES_TRABAJOS_DBTABLENAME;
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_TALUD;
    }

}
