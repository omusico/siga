package es.icarto.gvsig.extgia.batch.reconocimientos;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.isletas.IsletasCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchIsletasReconocimientos extends BatchAbstractSubForm {

    public BatchIsletasReconocimientos(String formFile, String dbTableName) {
	super(dbTableName);
	addCalculation(new IsletasCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.ISLETAS_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_ISLETA;
    }
}
