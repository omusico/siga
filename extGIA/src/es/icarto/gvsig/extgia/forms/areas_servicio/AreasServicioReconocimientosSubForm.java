package es.icarto.gvsig.extgia.forms.areas_servicio;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class AreasServicioReconocimientosSubForm extends AbstractSubForm {

    public AreasServicioReconocimientosSubForm(String formFile,
	    String dbTableName, JTable embebedTable, String idElementField,
	    String idElementValue, String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);

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
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource(
			"rules/areas_servicio_reconocimientos_metadata.xml")
		.getPath();
    }

}
