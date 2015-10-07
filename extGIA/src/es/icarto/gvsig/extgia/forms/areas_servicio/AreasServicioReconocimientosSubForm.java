package es.icarto.gvsig.extgia.forms.areas_servicio;

import es.icarto.gvsig.extgia.forms.GIASubForm;

@SuppressWarnings("serial")
public class AreasServicioReconocimientosSubForm extends GIASubForm {

    public AreasServicioReconocimientosSubForm() {
	super("areas_servicio_reconocimientos");
	addCalculation(new AreasServicioCalculateIndiceEstado(this));
    }

}
