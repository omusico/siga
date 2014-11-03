package es.icarto.gvsig.extgia.forms.barrera_rigida;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BarreraRigidaReconocimientosSubForm extends BasicAbstractSubForm {

    public BarreraRigidaReconocimientosSubForm(String formFile,
	    String dbTableName, JTable embebedTable, String idElementField,
	    String idElementValue, String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);

	BarreraRigidaCalculateIndiceEstado index = new BarreraRigidaCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.BARRERA_RIGIDA_INDEX,
		DBFieldNames.BARRERA_RIGIDA_A,
		DBFieldNames.BARRERA_RIGIDA_B,
		DBFieldNames.BARRERA_RIGIDA_C,
		DBFieldNames.BARRERA_RIGIDA_D);
	index.setListeners();
    }

    @Override
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource(
			"rules/barrera_rigida_reconocimientos_metadata.xml")
		.getPath();
    }

}
