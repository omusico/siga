package es.icarto.gvsig.extgex.forms;

import es.icarto.gvsig.navtableforms.CommonMethodsForTestDBForms;

public class TestFormExpropiationsLine extends CommonMethodsForTestDBForms {

    @Override
    protected String getSchema() {
	return "audasa_expropiaciones";
    }

    @Override
    protected String getTableName() {
	return FormExpropiationLine.TABLENAME;
    }

}
