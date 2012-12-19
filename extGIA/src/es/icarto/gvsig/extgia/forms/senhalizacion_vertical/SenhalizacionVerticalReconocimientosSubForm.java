package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class SenhalizacionVerticalReconocimientosSubForm extends AbstractSubForm {

    public SenhalizacionVerticalReconocimientosSubForm(String formFile,
	    String dbTableName, JTable embebedTable, String idElementField,
	    String idElementValue, String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);

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
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource(
			"rules/senhalizacion_vertical_reconocimientos_metadata.xml")
		.getPath();
    }

}
