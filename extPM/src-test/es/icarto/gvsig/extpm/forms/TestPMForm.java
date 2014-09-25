package es.icarto.gvsig.extpm.forms;

import es.icarto.gvsig.navtableforms.CommonMethodsForTestDBForms;

public class TestPMForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getSchema() {
	return "audasa_pm";
    }

    @Override
    protected String getTableName() {
	return "exp_pm";
    }
}
