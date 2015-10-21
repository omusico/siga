package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.barrera_rigida.BarreraRigidaCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchBarreraRigidaReconocimientos extends BatchAbstractSubForm {

    public BatchBarreraRigidaReconocimientos(String formFile, String dbTableName) {
	super(dbTableName);
	addCalculation(new BarreraRigidaCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.BARRERA_RIGIDA_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_BARRERA_RIGIDA;
    }
}
