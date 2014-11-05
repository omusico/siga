package es.icarto.gvsig.extgia.forms.muros;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class MurosReconocimientosSubForm extends BasicAbstractSubForm {

    public MurosReconocimientosSubForm() {
	super("muros_reconocimientos");
	addCalculation(new MurosCalculateIndiceEstado(this));
    }
}
