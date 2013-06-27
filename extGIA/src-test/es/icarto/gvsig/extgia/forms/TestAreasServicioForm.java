package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.areas_servicio.AreasServicioForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestAreasServicioForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return AreasServicioForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/areas_servicio_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "areas_servicio";
    }

}
