package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchTaludesTrabajos extends BatchAbstractSubForm {

    public BatchTaludesTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.TALUDES_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_TALUD;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/taludes_trabajos_metadata.xml")
		.getPath();
    }

}
