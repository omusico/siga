package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchIsletasTrabajos extends BatchAbstractSubForm {

    public BatchIsletasTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.ISLETAS_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_ISLETA;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/isletas_trabajos_metadata.xml")
		.getPath();
    }

}
