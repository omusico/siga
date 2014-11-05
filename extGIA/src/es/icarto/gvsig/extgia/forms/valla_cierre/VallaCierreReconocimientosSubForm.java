package es.icarto.gvsig.extgia.forms.valla_cierre;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class VallaCierreReconocimientosSubForm extends BasicAbstractSubForm {

    public VallaCierreReconocimientosSubForm() {
	super("valla_cierre_reconocimientos");
	addCalculation(new VallaCierreCalculateIndiceEstado(this));
    }

}
