package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.muros.MurosForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestMurosForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return MurosForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/muros_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "muros";
    }

}
