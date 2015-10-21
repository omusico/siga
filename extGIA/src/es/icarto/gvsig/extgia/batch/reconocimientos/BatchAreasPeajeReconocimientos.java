package es.icarto.gvsig.extgia.batch.reconocimientos;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchAreasPeajeReconocimientos extends BatchAbstractSubForm {

    public BatchAreasPeajeReconocimientos(String formFile, String dbTableName) {
	super(dbTableName);
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.AREAS_PEAJE_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_AREA_PEAJE;
    }
}
