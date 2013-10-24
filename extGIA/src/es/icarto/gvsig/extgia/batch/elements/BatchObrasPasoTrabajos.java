package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchObrasPasoTrabajos extends BatchAbstractSubForm {

    public BatchObrasPasoTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.OBRAS_PASO_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	// TODO Auto-generated method stub
	return DBFieldNames.ID_OBRA_PASO;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/obras_paso_trabajos_metadata.xml")
		.getPath();
    }

}
