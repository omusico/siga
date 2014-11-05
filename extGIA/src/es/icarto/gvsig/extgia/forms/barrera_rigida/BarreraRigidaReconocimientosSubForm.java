package es.icarto.gvsig.extgia.forms.barrera_rigida;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class BarreraRigidaReconocimientosSubForm extends BasicAbstractSubForm {

    public BarreraRigidaReconocimientosSubForm() {
	super("barrera_rigida_reconocimientos");
	addCalculation(new BarreraRigidaCalculateIndiceEstado(this));
    }
}
