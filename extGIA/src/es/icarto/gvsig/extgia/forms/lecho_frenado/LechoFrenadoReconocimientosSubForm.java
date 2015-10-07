package es.icarto.gvsig.extgia.forms.lecho_frenado;

import es.icarto.gvsig.extgia.forms.GIASubForm;

@SuppressWarnings("serial")
public class LechoFrenadoReconocimientosSubForm extends GIASubForm {

    public LechoFrenadoReconocimientosSubForm() {
	super("lecho_frenado_reconocimientos");
	addCalculation(new LechoFrenadoCalculateIndiceEstado(this));
    }

}
