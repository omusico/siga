package es.icarto.gvsig.extgia.batch.elements;

import java.util.HashMap;
import java.util.Map;

import es.icarto.gvsig.extgia.batch.BatchVegetationTrabajosAbstractSubForm;
import es.icarto.gvsig.extgia.forms.isletas.CalculateIsletasTrabajosMedicionElemento;
import es.icarto.gvsig.extgia.forms.isletas.CalculateIsletasTrabajosMedicionUltimoTrabajo;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Elements;

@SuppressWarnings("serial")
public class BatchIsletasTrabajos extends BatchVegetationTrabajosAbstractSubForm {

    public BatchIsletasTrabajos(Elements parentElement) {
	super(parentElement);
    }

    @Override
    public String[] getColumnNames() {
	String[] columnNames = {"ID Isleta",
		"Fecha",
		"Unidad",
		"Longitud",
		"Ancho",
		"Medición",
		"Medición elemento",
		"Medición último trabajo",
		"Observaciones"
	};
	return columnNames;
    }

    @Override
    public String[] getColumnDbNames() {
	String[] columnNames = {"id_isleta",
		"fecha",
		"unidad",
		"longitud",
		"ancho",
		"medicion",
		"medicion_elemento",
		"medicion_ultimo_trabajo",
		"observaciones"
	};
	return columnNames;
    }

    @Override
    public void getForeignValues(HashMap<String, String> values, String idValue) {
	Map<String, String> primaryKey = new HashMap<String, String>();
	primaryKey.put(getIdFieldName(), idValue);

	values.put(DBFieldNames.MEDICION_ELEMENTO, new CalculateIsletasTrabajosMedicionElemento(
		primaryKey).getForeignValue().getValue());
	values.put(DBFieldNames.MEDICION_ULTIMO_TRABAJO, new CalculateIsletasTrabajosMedicionUltimoTrabajo(
		primaryKey, values.get(DBFieldNames.UNIDAD)).getForeignValue().getValue());

    }

}
