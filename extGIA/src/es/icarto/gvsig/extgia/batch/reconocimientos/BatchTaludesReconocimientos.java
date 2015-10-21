package es.icarto.gvsig.extgia.batch.reconocimientos;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.taludes.TaludesCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchTaludesReconocimientos extends BatchAbstractSubForm {

    public BatchTaludesReconocimientos(String formFile, String dbTableName) {
	super(dbTableName);
	addCalculation(new TaludesCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.TALUDES_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_TALUD;
    }
}
