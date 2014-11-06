package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.areas_descanso.AreasDescansoCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchAreasDescansoReconocimientos extends BatchAbstractSubForm {

    public BatchAreasDescansoReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);

	addCalculation(new AreasDescansoCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.AREAS_DESCANSO_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_AREA_DESCANSO;
    }

}
