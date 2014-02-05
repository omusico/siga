package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.lineas_suministro.LineasSuministroForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestLineasSuministroForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return LineasSuministroForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/lineas_suministro_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "lineas_suministro";
    }

}
