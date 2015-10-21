package es.icarto.gvsig.extgia.batch.reconocimientos;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchEnlacesReconocimientos extends BatchAbstractSubForm {

    public BatchEnlacesReconocimientos(String formFile, String dbTableName) {
	super(dbTableName);
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.ENLACES_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_ENLACE;
    }
}
