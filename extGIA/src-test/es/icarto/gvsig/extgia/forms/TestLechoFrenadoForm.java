package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.lecho_frenado.LechoFrenadoForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestLechoFrenadoForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return LechoFrenadoForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/lecho_frenado_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "lecho_frenado";
    }

}
