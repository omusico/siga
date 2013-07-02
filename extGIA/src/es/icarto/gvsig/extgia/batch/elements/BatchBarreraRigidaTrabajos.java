package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchBarreraRigidaTrabajos extends BatchAbstractSubForm {

    public BatchBarreraRigidaTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
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
			"rules/barrera_rigida_trabajos_metadata.xml")
			.getPath();
    }

}
