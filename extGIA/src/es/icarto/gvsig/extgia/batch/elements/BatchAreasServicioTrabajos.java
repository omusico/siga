package es.icarto.gvsig.extgia.batch.elements;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchAreasServicioTrabajos extends BatchAbstractSubForm {

    public BatchAreasServicioTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
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
		.getResource("rules/areas_servicio_trabajos_metadata.xml")
		.getPath();
    }

}
