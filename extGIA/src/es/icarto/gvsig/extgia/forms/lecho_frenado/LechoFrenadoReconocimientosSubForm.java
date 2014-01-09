package es.icarto.gvsig.extgia.forms.lecho_frenado;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class LechoFrenadoReconocimientosSubForm extends AbstractSubForm {

    public LechoFrenadoReconocimientosSubForm(String formFile,
	    String dbTableName, JTable embebedTable, String idElementField,
	    String idElementValue, String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);

	LechoFrenadoCalculateIndiceEstado index = new LechoFrenadoCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.LECHO_FRENADO_INDEX,
		DBFieldNames.LECHO_FRENADO_A,
		DBFieldNames.LECHO_FRENADO_B,
		DBFieldNames.LECHO_FRENADO_C);
	index.setListeners();
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/lecho_frenado_reconocimientos_metadata.xml")
		.getPath();
    }

}
