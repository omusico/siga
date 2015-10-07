package es.icarto.gvsig.extgia.forms.barrera_rigida;

import es.icarto.gvsig.extgia.forms.GIASubForm;

@SuppressWarnings("serial")
public class BarreraRigidaReconocimientosSubForm extends GIASubForm {

    public BarreraRigidaReconocimientosSubForm() {
	super("barrera_rigida_reconocimientos");
	addCalculation(new BarreraRigidaCalculateIndiceEstado(this));
    }

}
