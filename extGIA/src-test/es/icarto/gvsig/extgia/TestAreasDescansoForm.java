package es.icarto.gvsig.extgia;

import es.icarto.gvsig.extgia.forms.areas_descanso.AreasDescansoForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestAreasDescansoForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return AreasDescansoForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/areas_descanso_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "areas_descanso";
    }

}
