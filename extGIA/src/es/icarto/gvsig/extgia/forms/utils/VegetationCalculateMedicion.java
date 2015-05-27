package es.icarto.gvsig.extgia.forms.utils;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MEDICION;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.LONGITUD;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.ANCHO;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class VegetationCalculateMedicion extends Calculation {

    public VegetationCalculateMedicion(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return MEDICION;
    }

    @Override
    protected String[] operandNames() {
	return new String[] {LONGITUD, ANCHO};
    }

    @Override
    protected String calculate() {
	BigDecimal value = new BigDecimal(1);
	
	value = operandValue(LONGITUD).multiply(value);
	value = operandValue(ANCHO).multiply(value);
	
	return formatter.format(value);
    }

}
