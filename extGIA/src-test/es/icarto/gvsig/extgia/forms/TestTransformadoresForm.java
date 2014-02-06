package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.transformadores.TransformadoresForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestTransformadoresForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return TransformadoresForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/transformadores_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "transformadores";
    }

}
