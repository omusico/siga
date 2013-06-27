package es.icarto.gvsig.extgia;

import es.icarto.gvsig.extgia.forms.barrera_rigida.BarreraRigidaForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestBarreraRigidaForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return BarreraRigidaForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/barrera_rigida_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "barrera_rigida";
    }

}