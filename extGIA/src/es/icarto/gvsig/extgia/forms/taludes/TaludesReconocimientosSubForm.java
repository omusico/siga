package es.icarto.gvsig.extgia.forms.taludes;

import es.icarto.gvsig.extgia.forms.GIASubForm;

@SuppressWarnings("serial")
public class TaludesReconocimientosSubForm extends GIASubForm {

    public TaludesReconocimientosSubForm() {
	super("taludes_reconocimientos");
	addCalculation(new TaludesCalculateIndiceEstado(this));
    }

}
