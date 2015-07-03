package es.icarto.gvsig.extgia.batch.elements;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import es.icarto.gvsig.extgia.batch.BatchVegetationTrabajosAbstractSubForm;
import es.icarto.gvsig.extgia.forms.taludes.CalculateTaludesTrabajosMedicionComplementaria;
import es.icarto.gvsig.extgia.forms.taludes.CalculateTaludesTrabajosMedicionElemento;
import es.icarto.gvsig.extgia.forms.taludes.CalculateTaludesTrabajosMedicionUltimoTrabajo;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchTaludesTrabajos extends BatchVegetationTrabajosAbstractSubForm {

    public BatchTaludesTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.TALUDES_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_TALUD;
    }

    @Override
    protected String getBasicName() {
	return "vegetation_trabajos";
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
    public Integer[] getColumnDbTypes() {
	Integer[] columnTypes = {Types.VARCHAR,
		Types.DATE,
		Types.VARCHAR,
		Types.INTEGER,
		Types.NUMERIC,
		Types.NUMERIC,
		Types.INTEGER,
		Types.INTEGER,
		Types.NUMERIC,
		Types.VARCHAR
	};
	return columnTypes;
    }

    @Override
    public void getForeignValues(HashMap<String, String> values, String idValue) {
	Map<String, String> primaryKey = new HashMap<String, String>();
	primaryKey.put(getIdFieldName(), idValue);

	values.put(DBFieldNames.MEDICION_ELEMENTO, new CalculateTaludesTrabajosMedicionElemento(
		primaryKey).getForeignValue().getValue());
	values.put(DBFieldNames.MEDICION_COMPLEMENTARIA, new CalculateTaludesTrabajosMedicionComplementaria(
		primaryKey).getForeignValue().getValue());
	values.put(DBFieldNames.MEDICION_ULTIMO_TRABAJO, new CalculateTaludesTrabajosMedicionUltimoTrabajo(
		primaryKey, values.get(DBFieldNames.UNIDAD)).getForeignValue().getValue());

    }
}
