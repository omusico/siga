package es.icarto.gvsig.extgia.forms.barrera_rigida;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;
import es.icarto.gvsig.extgia.forms.utils.VegetationCalculateMedicion;

@SuppressWarnings("serial")
public class BarreraRigidaTrabajosSubForm extends GIASubForm {
    public BarreraRigidaTrabajosSubForm() {
	super("barrera_rigida_trabajos");
	addCalculation(new VegetationCalculateMedicion(this));
    }

}
