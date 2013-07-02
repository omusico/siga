package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchEnlacesReconocimientos extends BatchAbstractSubForm {

    public BatchEnlacesReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.ENLACES_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_ENLACE;
    }

    @Override
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource(
			"rules/enlaces_reconocimientos_metadata.xml")
			.getPath();
    }

}
