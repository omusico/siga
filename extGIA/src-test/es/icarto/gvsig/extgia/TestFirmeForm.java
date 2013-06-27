package es.icarto.gvsig.extgia;

import es.icarto.gvsig.extgia.forms.firme.FirmeForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestFirmeForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return FirmeForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/firme_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "firme";
    }

}
