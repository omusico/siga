package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.senhalizacion_variable.SenhalizacionVariableForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestSenhalizacionVariableForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return SenhalizacionVariableForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/senhalizacion_variable_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "senhalizacion_variable";
    }

}
