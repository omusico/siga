package es.icarto.gvsig.extgia.forms.taludes;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;
import es.icarto.gvsig.extgia.forms.utils.VegetationCalculateMedicion;

@SuppressWarnings("serial")
public class TaludesTrabajosSubForm extends GIASubForm {
    public TaludesTrabajosSubForm() {
	super("taludes_trabajos");
	addCalculation(new VegetationCalculateMedicion(this));
    }

}
