package es.icarto.gvsig.extgia.batch.elements;

import java.util.HashMap;
import java.util.Map;

import es.icarto.gvsig.extgia.batch.BatchVegetationTrabajosAbstractSubForm;
import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Elements;

@SuppressWarnings("serial")
public class BatchTaludesTrabajos extends
	BatchVegetationTrabajosAbstractSubForm {

    public BatchTaludesTrabajos(Elements parentElement) {
	super(parentElement);
    }

    @Override
    public String[] getColumnNames() {
	String[] columnNames = { "ID Talud", "Fecha", "Unidad", "Longitud",
		"Ancho", "Medición", "Medición elemento",
		"Medición complementaria", "Medición último trabajo",
		"Observaciones" };
	return columnNames;
    }

    // TODO: get from db with dbconnection
    @Override
    public String[] getColumnDbNames() {
	String[] columnNames = { "id_talud", "fecha", "unidad", "longitud",
		"ancho", "medicion", "medicion_elemento",
		"medicion_complementaria", "medicion_ultimo_trabajo",
		"observaciones" };
	return columnNames;
    }

    @Override
    public void getForeignValues(HashMap<String, String> values, String idValue) {
	Map<String, String> primaryKey = new HashMap<String, String>();
	primaryKey.put(getIdFieldName(), idValue);

	values.put(DBFieldNames.LONGITUD, new CalculateDBForeignValue(
		primaryKey, DBFieldNames.LONGITUD,
		DBFieldNames.TALUDES_LONGITUD,
			DBFieldNames.TALUDES_DBTABLENAME, DBFieldNames.ID_TALUD)
		.getForeignValue().getValue());

	values.put(DBFieldNames.MEDICION_ELEMENTO,

		new CalculateDBForeignValue(primaryKey, DBFieldNames.MEDICION_ELEMENTO,
			DBFieldNames.SUP_TOTAL_ANALITICA,
			DBFieldNames.TALUDES_DBTABLENAME, DBFieldNames.ID_TALUD)
	.getForeignValue().getValue());

	values.put(
		DBFieldNames.MEDICION_COMPLEMENTARIA,
		new CalculateDBForeignValue(getForeignKey(),
			DBFieldNames.MEDICION_COMPLEMENTARIA,
			DBFieldNames.SUP_COMPLEMENTARIA,
			DBFieldNames.TALUDES_DBTABLENAME, DBFieldNames.ID_TALUD)
			.getForeignValue().getValue());
	values.put(
		DBFieldNames.MEDICION_ULTIMO_TRABAJO,
		new CalculateDBForeignValueLastJob(values
			.get(DBFieldNames.UNIDAD), primaryKey,
			DBFieldNames.MEDICION_ULTIMO_TRABAJO,
			DBFieldNames.TALUDES_TRABAJOS_DBTABLENAME,
			DBFieldNames.ID_TALUD).getForeignValue().getValue());

    }

}
