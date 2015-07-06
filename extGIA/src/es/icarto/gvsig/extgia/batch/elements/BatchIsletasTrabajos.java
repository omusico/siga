package es.icarto.gvsig.extgia.batch.elements;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import es.icarto.gvsig.extgia.batch.BatchVegetationTrabajosAbstractSubForm;
import es.icarto.gvsig.extgia.forms.isletas.CalculateIsletasTrabajosMedicionElemento;
import es.icarto.gvsig.extgia.forms.isletas.CalculateIsletasTrabajosMedicionUltimoTrabajo;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchIsletasTrabajos extends BatchVegetationTrabajosAbstractSubForm {

    public BatchIsletasTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.ISLETAS_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_ISLETA;
    }

    @Override
    public String getDbTableName() {
	return "isletas_trabajos";
    }

    @Override
    protected String getBasicName() {
	return "vegetation_trabajos";
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
    public Integer[] getColumnDbTypes() {
	Integer[] columnTypes = {Types.VARCHAR,
		Types.DATE,
		Types.VARCHAR,
		Types.INTEGER,
		Types.NUMERIC,
		Types.NUMERIC,
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

	values.put(DBFieldNames.MEDICION_ELEMENTO, new CalculateIsletasTrabajosMedicionElemento(
		primaryKey).getForeignValue().getValue());
	values.put(DBFieldNames.MEDICION_ULTIMO_TRABAJO, new CalculateIsletasTrabajosMedicionUltimoTrabajo(
		primaryKey, values.get(DBFieldNames.UNIDAD)).getForeignValue().getValue());

    }

}
