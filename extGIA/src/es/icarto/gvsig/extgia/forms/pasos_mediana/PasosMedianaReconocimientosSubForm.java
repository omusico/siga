package es.icarto.gvsig.extgia.forms.pasos_mediana;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class PasosMedianaReconocimientosSubForm extends AbstractSubForm {

    public PasosMedianaReconocimientosSubForm(String formFile,
	    String dbTableName, JTable embebedTable, String idElementField,
	    String idElementValue, String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);

	PasosMedianaCalculateIndiceEstado index = new PasosMedianaCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.PASO_MEDIANA_INDEX,
		DBFieldNames.PASO_MEDIANA_A,
		DBFieldNames.PASO_MEDIANA_B,
		DBFieldNames.PASO_MEDIANA_C,
		DBFieldNames.PASO_MEDIANA_D);
	index.setListeners();
    }

    @Override
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource("rules/pasos_mediana_reconocimientos_metadata.xml")
		.getPath();
    }

}
