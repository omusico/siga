package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class SenhalizacionVerticalReconocimientosSubForm extends
	BasicAbstractSubForm {

    public SenhalizacionVerticalReconocimientosSubForm() {
	super("senhalizacion_vertical_reconocimientos");
	addCalculation(new SenhalizacionVerticalCalculateIndiceEstado(this));
    }
}
