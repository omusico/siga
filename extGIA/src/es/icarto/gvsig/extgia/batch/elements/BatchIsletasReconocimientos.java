package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.isletas.IsletasCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchIsletasReconocimientos extends BatchAbstractSubForm {

    public BatchIsletasReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);

	IsletasCalculateIndiceEstado index = new IsletasCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.ISLETAS_INDEX,
		DBFieldNames.ISLETAS_A
		);
	index.setListeners();
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
		.getResource("rules/isletas_reconocimientos_metadata.xml")
		.getPath();
    }

}
