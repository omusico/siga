package es.icarto.gvsig.extgia.batch.elements;

import java.util.HashMap;

import es.icarto.gvsig.extgia.batch.BatchVegetationTrabajosAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.Elements;

@SuppressWarnings("serial")
public class BatchSenhalizacionVerticalTrabajos extends BatchVegetationTrabajosAbstractSubForm {

    public BatchSenhalizacionVerticalTrabajos(Elements parentElement) {
	super(parentElement);
    }

    @Override
    public String[] getColumnNames() {
	String[] columnNames = {"ID Señalización",
		"Fecha",
		"Unidad",
		"Medición",
		"Observaciones"
	};
	return columnNames;
    }

    @Override
    public String[] getColumnDbNames() {
	String[] columnNames = {"id_elemento_senhalizacion",
		"fecha",
		"unidad",
		"medicion",
		"observaciones"
	};
	return columnNames;
    }

    @Override
    public void getForeignValues(HashMap<String, String> values, String idValue) {
	// TODO Auto-generated method stub

    }

}
