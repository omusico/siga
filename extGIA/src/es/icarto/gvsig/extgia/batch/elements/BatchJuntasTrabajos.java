package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchJuntasTrabajos extends BatchAbstractSubForm {

    public BatchJuntasTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.JUNTAS_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_JUNTA;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/juntas_trabajos_metadata.xml")
		.getPath();
    }

}
