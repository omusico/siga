package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.juntas.JuntasCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchJuntasReconocimientos extends BatchAbstractSubForm {

    public BatchJuntasReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);

	JuntasCalculateIndiceEstado index = new JuntasCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.JUNTAS_INDEX,
		DBFieldNames.JUNTAS_A,
		DBFieldNames.JUNTAS_B,
		DBFieldNames.JUNTAS_C,
		DBFieldNames.JUNTAS_D,
		DBFieldNames.JUNTAS_E);
	index.setListeners();
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
		.getResource("rules/juntas_reconocimientos_metadata.xml")
		.getPath();
    }

}
