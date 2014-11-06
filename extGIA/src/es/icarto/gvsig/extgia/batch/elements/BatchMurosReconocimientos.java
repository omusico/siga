package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.muros.MurosCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchMurosReconocimientos extends BatchAbstractSubForm {

    public BatchMurosReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	addCalculation(new MurosCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.MUROS_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_MUROS;
    }
}
