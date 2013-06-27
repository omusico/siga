package es.icarto.gvsig.extgia;

import es.icarto.gvsig.extgia.forms.juntas.JuntasForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestJuntasForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return JuntasForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/juntas_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "juntas";
    }

}
