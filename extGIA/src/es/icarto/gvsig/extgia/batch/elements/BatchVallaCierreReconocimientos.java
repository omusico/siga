package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.valla_cierre.VallaCierreCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchVallaCierreReconocimientos extends BatchAbstractSubForm {

    public BatchVallaCierreReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	addCalculation(new VallaCierreCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.VALLA_CIERRE_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_VALLA_CIERRE;
    }
}
