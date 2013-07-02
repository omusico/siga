package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.valla_cierre.VallaCierreCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchVallaCierreReconocimientos extends BatchAbstractSubForm {

    public BatchVallaCierreReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);

	VallaCierreCalculateIndiceEstado index = new VallaCierreCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.VALLA_CIERRE_INDEX,
		DBFieldNames.VALLA_CIERRE_A,
		DBFieldNames.VALLA_CIERRE_B,
		DBFieldNames.VALLA_CIERRE_C);
	index.setListeners();
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.VALLA_CIERRE_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_VALLA_CIERRE;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/valla_cierre_reconocimientos_metadata.xml")
		.getPath();
    }

}
