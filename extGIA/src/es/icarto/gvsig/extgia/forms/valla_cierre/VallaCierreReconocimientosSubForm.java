package es.icarto.gvsig.extgia.forms.valla_cierre;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class VallaCierreReconocimientosSubForm extends BasicAbstractSubForm {

    public VallaCierreReconocimientosSubForm(String formFile,
	    String dbTableName, JTable embebedTable, String idElementField,
	    String idElementValue, String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);

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
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/valla_cierre_reconocimientos_metadata.xml")
		.getPath();
    }

}
