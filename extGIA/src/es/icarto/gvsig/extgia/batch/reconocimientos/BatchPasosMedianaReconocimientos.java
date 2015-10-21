package es.icarto.gvsig.extgia.batch.reconocimientos;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.pasos_mediana.PasosMedianaCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchPasosMedianaReconocimientos extends BatchAbstractSubForm {

    public BatchPasosMedianaReconocimientos(String formFile, String dbTableName) {
	super(dbTableName);
	addCalculation(new PasosMedianaCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.PASOS_MEDIANA_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_PASO_MEDIANA;
    }
}
