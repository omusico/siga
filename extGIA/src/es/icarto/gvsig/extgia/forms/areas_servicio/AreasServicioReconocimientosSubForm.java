package es.icarto.gvsig.extgia.forms.areas_servicio;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class AreasServicioReconocimientosSubForm extends BasicAbstractSubForm {

    public AreasServicioReconocimientosSubForm() {
	super("areas_servicio_reconocimientos");
	addCalculation(new AreasServicioCalculateIndiceEstado(this));
    }
}
