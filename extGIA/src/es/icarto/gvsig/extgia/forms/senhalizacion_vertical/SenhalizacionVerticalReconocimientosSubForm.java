package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import es.icarto.gvsig.extgia.forms.GIASubForm;

@SuppressWarnings("serial")
public class SenhalizacionVerticalReconocimientosSubForm extends
	GIASubForm {

    public SenhalizacionVerticalReconocimientosSubForm() {
	super("senhalizacion_vertical_reconocimientos");
	addCalculation(new SenhalizacionVerticalCalculateIndiceEstado(this));
    }
}
