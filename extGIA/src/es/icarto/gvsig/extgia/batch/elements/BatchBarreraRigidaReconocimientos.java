package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.barrera_rigida.BarreraRigidaCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchBarreraRigidaReconocimientos extends BatchAbstractSubForm {

    public BatchBarreraRigidaReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);

	BarreraRigidaCalculateIndiceEstado index = new BarreraRigidaCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.BARRERA_RIGIDA_INDEX,
		DBFieldNames.BARRERA_RIGIDA_A,
		DBFieldNames.BARRERA_RIGIDA_B,
		DBFieldNames.BARRERA_RIGIDA_C,
		DBFieldNames.BARRERA_RIGIDA_D);
	index.setListeners();
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.BARRERA_RIGIDA_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_BARRERA_RIGIDA;
    }

    @Override
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource(
			"rules/barrera_rigida_reconocimientos_metadata.xml")
			.getPath();
    }

}
