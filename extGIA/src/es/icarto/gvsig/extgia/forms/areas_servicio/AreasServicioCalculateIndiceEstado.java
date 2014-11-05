package es.icarto.gvsig.extgia.forms.areas_servicio;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_SERVICIO_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_SERVICIO_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_SERVICIO_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_SERVICIO_D;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_SERVICIO_E;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_SERVICIO_INDEX;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class AreasServicioCalculateIndiceEstado extends Calculation {
    private static final BigDecimal weigth = new BigDecimal("0.2");

    public AreasServicioCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return AREA_SERVICIO_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { AREA_SERVICIO_A, AREA_SERVICIO_B,
		AREA_SERVICIO_C, AREA_SERVICIO_D, AREA_SERVICIO_E };
    }

    @Override
    protected String calculate() {

	BigDecimal value = new BigDecimal(0);

	value = value.add(operandValue(AREA_SERVICIO_A).multiply(weigth));
	value = value.add(operandValue(AREA_SERVICIO_B).multiply(weigth));
	value = value.add(operandValue(AREA_SERVICIO_C).multiply(weigth));
	value = value.add(operandValue(AREA_SERVICIO_D).multiply(weigth));
	value = value.add(operandValue(AREA_SERVICIO_E).multiply(weigth));

	return formatter.format(value);
    }

}
