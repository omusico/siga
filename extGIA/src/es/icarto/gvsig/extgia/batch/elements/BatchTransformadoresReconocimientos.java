package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchTransformadoresReconocimientos extends BatchAbstractSubForm {

    public BatchTransformadoresReconocimientos(String formFile,
	    String dbTableName) {
	super(dbTableName);
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.TRANSFORMADORES_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_TRANSFORMADORES;
    }
}
