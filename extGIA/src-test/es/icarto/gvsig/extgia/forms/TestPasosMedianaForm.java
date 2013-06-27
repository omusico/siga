package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.pasos_mediana.PasosMedianaForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestPasosMedianaForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return PasosMedianaForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/pasos_mediana_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "pasos_mediana";
    }

}
