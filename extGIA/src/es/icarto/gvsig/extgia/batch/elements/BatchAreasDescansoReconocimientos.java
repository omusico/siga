package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.areas_descanso.AreasDescansoCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchAreasDescansoReconocimientos extends
BatchAbstractSubForm {

    public BatchAreasDescansoReconocimientos(String formFile,
	    String dbTableName) {
	super(formFile, dbTableName);

	AreasDescansoCalculateIndiceEstado index = new AreasDescansoCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.AREA_DESCANSO_INDEX,
		DBFieldNames.AREA_DESCANSO_A,
		DBFieldNames.AREA_DESCANSO_B,
		DBFieldNames.AREA_DESCANSO_C);
	index.setListeners();
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.AREAS_DESCANSO_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_AREA_DESCANSO;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/areas_descanso_reconocimientos_metadata.xml")
		.getPath();
    }

}
