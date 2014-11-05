package es.icarto.gvsig.extgia.forms.taludes;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class TaludesReconocimientosSubForm extends BasicAbstractSubForm {

    public TaludesReconocimientosSubForm() {
	super("taludes_reconocimientos");
	addCalculation(new TaludesCalculateIndiceEstado(this));
    }
}
