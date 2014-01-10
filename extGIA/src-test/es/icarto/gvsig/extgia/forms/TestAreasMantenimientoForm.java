package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.areas_mantenimiento.AreasMantenimientoForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestAreasMantenimientoForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return AreasMantenimientoForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/areas_mantenimiento_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "areas_mantenimiento";
    }

}
