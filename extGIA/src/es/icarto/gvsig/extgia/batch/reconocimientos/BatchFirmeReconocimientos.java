package es.icarto.gvsig.extgia.batch.reconocimientos;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchFirmeReconocimientos extends BatchAbstractSubForm {

    public BatchFirmeReconocimientos(String formFile, String dbTableName) {
	super(dbTableName);
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.FIRME_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_FIRME;
    }
}
