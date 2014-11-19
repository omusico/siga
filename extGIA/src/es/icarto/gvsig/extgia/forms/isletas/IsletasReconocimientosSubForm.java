package es.icarto.gvsig.extgia.forms.isletas;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;

@SuppressWarnings("serial")
public class IsletasReconocimientosSubForm extends GIASubForm {

    public IsletasReconocimientosSubForm() {
	super("isletas_reconocimientos");
	addCalculation(new IsletasCalculateIndiceEstado(this));
    }

}
