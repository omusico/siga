package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.senhalizacion_vertical.SenhalizacionVerticalForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.CommonMethodsForTestDBForms;

public class TestSenhalizacionVerticalForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getSchema() {
	return DBFieldNames.GIA_SCHEMA;
    }

    @Override
    protected String getTableName() {
	return SenhalizacionVerticalForm.TABLENAME;
    }

}
