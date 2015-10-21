package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.juntas.JuntasCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchJuntasReconocimientos extends BatchAbstractSubForm {

    public BatchJuntasReconocimientos(String formFile, String dbTableName) {
	super(dbTableName);
	addCalculation(new JuntasCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.JUNTAS_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_JUNTA;
    }
}
