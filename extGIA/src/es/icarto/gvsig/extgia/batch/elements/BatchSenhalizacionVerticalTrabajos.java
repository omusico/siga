package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchSenhalizacionVerticalTrabajos extends BatchAbstractSubForm {

    public BatchSenhalizacionVerticalTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.SENHALIZACION_VERTICAL_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_ELEMENTO_SENHALIZACION;
    }

    @Override
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource(
			"rules/senhalizacion_vertical_trabajos_metadata.xml")
			.getPath();
    }

}
