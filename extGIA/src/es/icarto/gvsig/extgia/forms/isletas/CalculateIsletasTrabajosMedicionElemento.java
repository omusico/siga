package es.icarto.gvsig.extgia.forms.isletas;

import java.util.Map;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateForeignValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class CalculateIsletasTrabajosMedicionElemento extends
CalculateDBForeignValue implements CalculateForeignValue {

    public CalculateIsletasTrabajosMedicionElemento(
	    Map<String, String> foreingKey) {
	super(foreingKey, DBFieldNames.MEDICION_ELEMENTO,
		DBFieldNames.SUPERFICIE_BAJO_BIONDA,
		DBFieldNames.ISLETAS_DBTABLENAME, DBFieldNames.ID_ISLETA);
    }

}
