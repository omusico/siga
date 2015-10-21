package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchSenhalizacionVariableReconocimientos extends
	BatchAbstractSubForm {

    public BatchSenhalizacionVariableReconocimientos(String formFile,
	    String dbTableName) {
	super(dbTableName);
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.SENHALIZACION_VARIABLE_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_SENHAL_VARIABLE;
    }
}
