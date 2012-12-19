package es.icarto.gvsig.extgia.forms.juntas;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class JuntasReconocimientosSubForm extends AbstractSubForm {

    public JuntasReconocimientosSubForm(String formFile, String dbTableName,
	    JTable embebedTable, String idElementField, String idElementValue,
	    String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);

	JuntasCalculateIndiceEstado index = new JuntasCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.JUNTAS_INDEX,
		DBFieldNames.JUNTAS_A,
		DBFieldNames.JUNTAS_B,
		DBFieldNames.JUNTAS_C,
		DBFieldNames.JUNTAS_D,
		DBFieldNames.JUNTAS_E);
	index.setListeners();
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/juntas_reconocimientos_metadata.xml")
		.getPath();
    }

}
