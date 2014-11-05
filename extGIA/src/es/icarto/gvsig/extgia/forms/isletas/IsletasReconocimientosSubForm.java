package es.icarto.gvsig.extgia.forms.isletas;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class IsletasReconocimientosSubForm extends BasicAbstractSubForm {

    public IsletasReconocimientosSubForm() {
	super("isletas_reconocimientos");
	addCalculation(new IsletasCalculateIndiceEstado(this));
    }

}
