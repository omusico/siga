package es.icarto.gvsig.extgia.forms.isletas;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class IsletasReconocimientosSubForm extends AbstractSubForm {

    public IsletasReconocimientosSubForm(String formFile, String dbTableName,
	    JTable embebedTable, String idElementField, String idElementValue,
	    String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);


	IsletasCalculateIndiceEstado index = new IsletasCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.ISLETAS_INDEX,
		DBFieldNames.ISLETAS_A
		);
	index.setListeners();
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/isletas_reconocimientos_metadata.xml")
		.getPath();
    }

}
