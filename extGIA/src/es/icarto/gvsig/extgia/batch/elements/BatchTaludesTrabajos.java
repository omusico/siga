package es.icarto.gvsig.extgia.batch.elements;

import java.util.HashMap;
import java.util.Map;

import es.icarto.gvsig.extgia.batch.BatchVegetationTrabajosAbstractSubForm;
import es.icarto.gvsig.extgia.forms.taludes.CalculateTaludesTrabajosLongitud;
import es.icarto.gvsig.extgia.forms.taludes.CalculateTaludesTrabajosMedicionComplementaria;
import es.icarto.gvsig.extgia.forms.taludes.CalculateTaludesTrabajosMedicionElemento;
import es.icarto.gvsig.extgia.forms.taludes.CalculateTaludesTrabajosMedicionUltimoTrabajo;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Elements;

@SuppressWarnings("serial")
public class BatchTaludesTrabajos extends BatchVegetationTrabajosAbstractSubForm {

    public BatchTaludesTrabajos(Elements parentElement) {
	super(parentElement);
    }

    @Override
    public String[] getColumnNames() {
	String[] columnNames = {"ID Talud",
		"Fecha",
		"Unidad",
		"Longitud",
		"Ancho",
		"Medición",
		"Medición elemento",
		"Medición complementaria",
		"Medición último trabajo",
		"Observaciones"
	};
	return columnNames;
    }

    //TODO: get from db with dbconnection
    @Override
    public String[] getColumnDbNames() {
	String[] columnNames = {"id_talud",
		"fecha",
		"unidad",
		"longitud",
		"ancho",
		"medicion",
		"medicion_elemento",
		"medicion_complementaria",
		"medicion_ultimo_trabajo",
		"observaciones"
	};
	return columnNames;
    }

    @Override
    public void getForeignValues(HashMap<String, String> values, String idValue) {
	Map<String, String> primaryKey = new HashMap<String, String>();
	primaryKey.put(getIdFieldName(), idValue);

	values.put(DBFieldNames.LONGITUD, new CalculateTaludesTrabajosLongitud(
		primaryKey).getForeignValue().getValue());
	values.put(DBFieldNames.MEDICION_ELEMENTO, new CalculateTaludesTrabajosMedicionElemento(
		primaryKey).getForeignValue().getValue());
	values.put(DBFieldNames.MEDICION_COMPLEMENTARIA, new CalculateTaludesTrabajosMedicionComplementaria(
		primaryKey).getForeignValue().getValue());
	values.put(DBFieldNames.MEDICION_ULTIMO_TRABAJO, new CalculateTaludesTrabajosMedicionUltimoTrabajo(
		primaryKey, values.get(DBFieldNames.UNIDAD)).getForeignValue().getValue());

    }

}
