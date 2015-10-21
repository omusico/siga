package es.icarto.gvsig.extgia.batch.reconocimientos;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.areas_descanso.AreasDescansoCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.Elements;

@SuppressWarnings("serial")
public class BatchAreasDescansoReconocimientos extends BatchAbstractSubForm {

    public BatchAreasDescansoReconocimientos(String formFile, String dbTableName) {
	super(dbTableName);
	addCalculation(new AreasDescansoCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return Elements.Areas_Descanso.layerName;
    }

    @Override
    public String getIdFieldName() {
	return Elements.Areas_Descanso.pk;
    }

}
