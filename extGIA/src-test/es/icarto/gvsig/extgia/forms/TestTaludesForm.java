package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.taludes.TaludesForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestTaludesForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return TaludesForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getTable() {
	return "taludes";
    }

    @Override
    protected String getXmlFile() {
	return "rules/taludes_metadata.xml";
    }
}
