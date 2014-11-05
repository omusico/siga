package es.icarto.gvsig.extgia.forms.lecho_frenado;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class LechoFrenadoReconocimientosSubForm extends BasicAbstractSubForm {

    public LechoFrenadoReconocimientosSubForm() {
	super("lecho_frenado_reconocimientos");
	addCalculation(new LechoFrenadoCalculateIndiceEstado(this));
    }

}
