package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.obras_paso.ObrasPasoForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestObrasPasoForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return ObrasPasoForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/obras_paso_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "obras_paso";
    }

}
