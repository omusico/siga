package es.icarto.gvsig.extgia.forms.valla_cierre;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;

@SuppressWarnings("serial")
public class VallaCierreReconocimientosSubForm extends GIASubForm {

    public VallaCierreReconocimientosSubForm() {
	super("valla_cierre_reconocimientos");
	addCalculation(new VallaCierreCalculateIndiceEstado(this));
    }

}
