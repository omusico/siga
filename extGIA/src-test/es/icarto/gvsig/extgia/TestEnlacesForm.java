package es.icarto.gvsig.extgia;

import es.icarto.gvsig.extgia.forms.enlaces.EnlacesForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestEnlacesForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return EnlacesForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/enlaces_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "enlaces";
    }

}
