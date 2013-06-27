package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.valla_cierre.VallaCierreForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestVallaCierreForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return VallaCierreForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/valla_cierre_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "valla_cierre";
    }

}
