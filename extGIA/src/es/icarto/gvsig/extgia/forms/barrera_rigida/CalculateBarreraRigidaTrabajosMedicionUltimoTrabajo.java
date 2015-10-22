package es.icarto.gvsig.extgia.forms.barrera_rigida;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateBarreraRigidaTrabajosMedicionUltimoTrabajo extends
CalculateDBForeignValueLastJob {

    public CalculateBarreraRigidaTrabajosMedicionUltimoTrabajo(
	    Map<String, String> foreingKey, String unidad) {
	super(unidad, foreingKey, DBFieldNames.MEDICION_ULTIMO_TRABAJO,
		DBFieldNames.BARRERA_RIGIDA_TRABAJOS_DBTABLENAME,
		DBFieldNames.ID_BARRERA_RIGIDA);
    }

}
