package es.icarto.gvsig.extgia.batch.elements;

import java.util.HashMap;
import java.util.Map;

import es.icarto.gvsig.extgia.batch.BatchVegetationTrabajosAbstractSubForm;
import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Elements;

@SuppressWarnings("serial")
public class BatchBarreraRigidaTrabajos extends
BatchVegetationTrabajosAbstractSubForm {

    public BatchBarreraRigidaTrabajos(Elements parentElement) {
	super(parentElement);
    }

    @Override
    public String[] getColumnNames() {
	String[] columnNames = { "ID Barrera Rígida", "Fecha", "Unidad",
		"Longitud", "Ancho", "Medición", "Medición último trabajo",
	"Observaciones" };
	return columnNames;
    }

    @Override
    public String[] getColumnDbNames() {
	String[] columnNames = { "id_barrera_rigida", "fecha", "unidad",
		"longitud", "ancho", "medicion", "medicion_ultimo_trabajo",
	"observaciones" };
	return columnNames;
    }

    @Override
    public void getForeignValues(HashMap<String, String> values, String idValue) {
	Map<String, String> primaryKey = new HashMap<String, String>();
	primaryKey.put(getIdFieldName(), idValue);

	values.put(DBFieldNames.LONGITUD, new CalculateDBForeignValue(
		primaryKey, DBFieldNames.LONGITUD,
		DBFieldNames.BARRERA_RIGIDA_LONGITUD,
		DBFieldNames.BARRERA_RIGIDA_DBTABLENAME,
		DBFieldNames.ID_BARRERA_RIGIDA).getForeignValue().getValue());

	values.put(DBFieldNames.MEDICION_ULTIMO_TRABAJO,

	new CalculateDBForeignValueLastJob(values.get(DBFieldNames.UNIDAD),
		primaryKey, DBFieldNames.MEDICION_ULTIMO_TRABAJO,
		DBFieldNames.BARRERA_RIGIDA_TRABAJOS_DBTABLENAME,
		DBFieldNames.ID_BARRERA_RIGIDA).getForeignValue().getValue());
    }

}
