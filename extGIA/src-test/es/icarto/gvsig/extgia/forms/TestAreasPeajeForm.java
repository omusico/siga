package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.areas_peaje.AreasPeajeForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestAreasPeajeForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return AreasPeajeForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/areas_peaje_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "areas_pejae";
    }

}
