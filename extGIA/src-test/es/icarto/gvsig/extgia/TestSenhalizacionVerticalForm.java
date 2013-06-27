package es.icarto.gvsig.extgia;

import es.icarto.gvsig.extgia.forms.senhalizacion_vertical.SenhalizacionVerticalForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestSenhalizacionVerticalForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return SenhalizacionVerticalForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/senhalizacion_vertical_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "senhalizacion_vertical";
    }

}
