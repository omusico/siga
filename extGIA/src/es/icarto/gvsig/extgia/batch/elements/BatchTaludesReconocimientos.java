package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.taludes.TaludesCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchTaludesReconocimientos extends BatchAbstractSubForm {

    public BatchTaludesReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);

	TaludesCalculateIndiceEstado index = new TaludesCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.TALUDES_INDEX,
		DBFieldNames.TALUDES_A,
		DBFieldNames.TALUDES_B,
		DBFieldNames.TALUDES_C,
		DBFieldNames.TALUDES_D);
	index.setListeners();
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
		.getResource("rules/taludes_reconocimientos_metadata.xml")
		.getPath();
    }

}
