package es.icarto.gvsig.extgia.forms.firme;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;

@SuppressWarnings("serial")
public class FirmeReconocimientosSubForm extends AbstractSubForm {

    public FirmeReconocimientosSubForm(String formFile, String dbTableName,
	    JTable embebedTable, String idElementField, String idElementValue,
	    String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/firme_reconocimientos_metadata.xml")
		.getPath();
    }

}