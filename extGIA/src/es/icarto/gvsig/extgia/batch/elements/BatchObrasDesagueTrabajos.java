package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchObrasDesagueTrabajos extends BatchAbstractSubForm {

    public BatchObrasDesagueTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.OBRAS_DESAGUE_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_OBRA_DESAGUE;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/obras_desague_trabajos_metadata.xml")
		.getPath();
    }

}
