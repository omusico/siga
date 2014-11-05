package es.icarto.gvsig.extgia.forms.juntas;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class JuntasReconocimientosSubForm extends BasicAbstractSubForm {

    public JuntasReconocimientosSubForm() {
	super("juntas_reconocimientos");
	addCalculation(new JuntasCalculateIndiceEstado(this));
    }

}
