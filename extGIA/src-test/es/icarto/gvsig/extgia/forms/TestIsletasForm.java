package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.isletas.IsletasForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestIsletasForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return IsletasForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getTable() {
	return "isletas";
    }

    @Override
    protected String getXmlFile() {
	return "rules/isletas_metadata.xml";
    }
}
