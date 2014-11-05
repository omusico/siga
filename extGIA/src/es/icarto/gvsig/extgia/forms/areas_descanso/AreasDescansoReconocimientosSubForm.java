package es.icarto.gvsig.extgia.forms.areas_descanso;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class AreasDescansoReconocimientosSubForm extends BasicAbstractSubForm {

    public AreasDescansoReconocimientosSubForm() {
	addCalculation(new AreasDescansoCalculateIndiceEstado(this));
    }

    @Override
    protected String getBasicName() {
	return "areas_descanso_reconocimientos";
    }

}
