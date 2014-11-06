package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.areas_servicio.AreasServicioCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchAreasServicioReconocimientos extends BatchAbstractSubForm {

    public BatchAreasServicioReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	addCalculation(new AreasServicioCalculateIndiceEstado(this));
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.AREAS_SERVICIO_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_AREA_SERVICIO;
    }
}
