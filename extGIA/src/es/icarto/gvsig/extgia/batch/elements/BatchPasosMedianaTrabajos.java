package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchPasosMedianaTrabajos extends BatchAbstractSubForm {

    public BatchPasosMedianaTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.PASOS_MEDIANA_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_PASO_MEDIANA;
    }

    @Override
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource("rules/pasos_mediana_trabajos_metadata.xml")
		.getPath();
    }

}
