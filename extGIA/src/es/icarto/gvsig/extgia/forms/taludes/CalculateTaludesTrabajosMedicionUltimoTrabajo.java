package es.icarto.gvsig.extgia.forms.taludes;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.CalculateForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateTaludesTrabajosMedicionUltimoTrabajo extends
CalculateDBForeignValueLastJob implements CalculateForeignValue {

    public CalculateTaludesTrabajosMedicionUltimoTrabajo(
	    Map<String, String> foreingKey, String unidad) {
	super(unidad, foreingKey, DBFieldNames.MEDICION_ULTIMO_TRABAJO,
		DBFieldNames.TALUDES_TRABAJOS_DBTABLENAME,
		DBFieldNames.ID_TALUD);
    }

}
