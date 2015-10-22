package es.icarto.gvsig.extgia.forms.isletas;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.CalculateForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateIsletasTrabajosMedicionUltimoTrabajo extends
CalculateDBForeignValueLastJob implements CalculateForeignValue {
    public CalculateIsletasTrabajosMedicionUltimoTrabajo(
	    Map<String, String> foreingKey, String unidad) {
	super(unidad, foreingKey, DBFieldNames.MEDICION_ULTIMO_TRABAJO,
		DBFieldNames.ISLETAS_TRABAJOS_DBTABLENAME,
		DBFieldNames.ID_ISLETA);
    }
}
