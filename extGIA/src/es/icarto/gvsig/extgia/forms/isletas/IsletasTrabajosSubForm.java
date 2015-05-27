package es.icarto.gvsig.extgia.forms.isletas;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;
import es.icarto.gvsig.extgia.forms.utils.VegetationCalculateMedicion;

@SuppressWarnings("serial")
public class IsletasTrabajosSubForm extends GIASubForm {
    public IsletasTrabajosSubForm() {
	super("isletas_trabajos");
	addCalculation(new VegetationCalculateMedicion(this));
    }

}
