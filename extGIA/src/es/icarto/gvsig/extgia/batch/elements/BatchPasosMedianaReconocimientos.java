package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.pasos_mediana.PasosMedianaCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchPasosMedianaReconocimientos extends BatchAbstractSubForm {

    public BatchPasosMedianaReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);

	PasosMedianaCalculateIndiceEstado index = new PasosMedianaCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.PASO_MEDIANA_INDEX,
		DBFieldNames.PASO_MEDIANA_A,
		DBFieldNames.PASO_MEDIANA_B,
		DBFieldNames.PASO_MEDIANA_C,
		DBFieldNames.PASO_MEDIANA_D);
	index.setListeners();
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
		.getResource("rules/pasos_mediana_reconocimientos_metadata.xml")
		.getPath();
    }

}
