package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.senhalizacion_vertical.SenhalizacionVerticalCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchSenhalizacionVerticalReconocimientos extends
BatchAbstractSubForm {

    public BatchSenhalizacionVerticalReconocimientos(String formFile,
	    String dbTableName) {
	super(formFile, dbTableName);

	SenhalizacionVerticalCalculateIndiceEstado index = new SenhalizacionVerticalCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.SENHALIZACION_VERTICAL_INDEX,
		DBFieldNames.SENHALIZACION_VERTICAL_A,
		DBFieldNames.SENHALIZACION_VERTICAL_B,
		DBFieldNames.SENHALIZACION_VERTICAL_C,
		DBFieldNames.SENHALIZACION_VERTICAL_D,
		DBFieldNames.SENHALIZACION_VERTICAL_E);
	index.setListeners();
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.SENHALIZACION_VERTICAL_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_ELEMENTO_SENHALIZACION;
    }

    @Override
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource(
			"rules/senhalizacion_vertical_reconocimientos_metadata.xml")
			.getPath();
    }

}
