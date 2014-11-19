package es.icarto.gvsig.extgia.forms.pasos_mediana;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;

@SuppressWarnings("serial")
public class PasosMedianaReconocimientosSubForm extends GIASubForm {

    public PasosMedianaReconocimientosSubForm() {
	super("pasos_mediana_reconocimientos");
	addCalculation(new PasosMedianaCalculateIndiceEstado(this));
    }

}
