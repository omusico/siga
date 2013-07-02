package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.areas_servicio.AreasServicioCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchAreasServicioReconocimientos extends BatchAbstractSubForm {

    public BatchAreasServicioReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);

	AreasServicioCalculateIndiceEstado index = new AreasServicioCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.AREA_SERVICIO_INDEX,
		DBFieldNames.AREA_SERVICIO_A,
		DBFieldNames.AREA_SERVICIO_B,
		DBFieldNames.AREA_SERVICIO_C,
		DBFieldNames.AREA_SERVICIO_D,
		DBFieldNames.AREA_SERVICIO_E);
	index.setListeners();
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.AREAS_SERVICIO_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_AREA_SERVICIO;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/areas_servicio_reconocimientos_metadata.xml")
		.getPath();
    }

}
