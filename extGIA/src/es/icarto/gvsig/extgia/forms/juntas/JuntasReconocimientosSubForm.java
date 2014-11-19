package es.icarto.gvsig.extgia.forms.juntas;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;

@SuppressWarnings("serial")
public class JuntasReconocimientosSubForm extends GIASubForm {

    public JuntasReconocimientosSubForm() {
	super("juntas_reconocimientos");
	addCalculation(new JuntasCalculateIndiceEstado(this));
    }

}
