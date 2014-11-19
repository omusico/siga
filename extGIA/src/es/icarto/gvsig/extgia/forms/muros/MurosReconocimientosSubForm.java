package es.icarto.gvsig.extgia.forms.muros;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;

@SuppressWarnings("serial")
public class MurosReconocimientosSubForm extends GIASubForm {

    public MurosReconocimientosSubForm() {
	super("muros_reconocimientos");
	addCalculation(new MurosCalculateIndiceEstado(this));
    }
}
