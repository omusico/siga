package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.senhalizacion_vertical.SenhalizacionVerticalSenhalesSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.CommonMethodsForTestDBForms;

public class TestSenhalizacionVerticalSenhalesSubForm extends
CommonMethodsForTestDBForms {

    @Override
    protected String getSchema() {
	return DBFieldNames.GIA_SCHEMA;
    }

    @Override
    protected String getTableName() {
	return SenhalizacionVerticalSenhalesSubForm.TABLENAME;
    }

    @Override
    protected String getMetadataFile() {
	return "rules/" + getTableName() + "_metadata.xml";
    }

}
