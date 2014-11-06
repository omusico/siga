package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchVallaCierreTrabajos extends BatchAbstractSubForm {

    public BatchVallaCierreTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.VALLA_CIERRE_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_VALLA_CIERRE;
    }
}
