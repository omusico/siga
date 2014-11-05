package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.SENHALIZACION_VERTICAL_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.SENHALIZACION_VERTICAL_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.SENHALIZACION_VERTICAL_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.SENHALIZACION_VERTICAL_D;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.SENHALIZACION_VERTICAL_E;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.SENHALIZACION_VERTICAL_INDEX;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class SenhalizacionVerticalCalculateIndiceEstado extends Calculation {
    private static final BigDecimal weigthA = new BigDecimal("0.2");
    private static final BigDecimal weigthB = new BigDecimal("0.1");
    private static final BigDecimal weigthC = new BigDecimal("0.1");
    private static final BigDecimal weigthD = new BigDecimal("0.35");
    private static final BigDecimal weigthE = new BigDecimal("0.25");

    public SenhalizacionVerticalCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return SENHALIZACION_VERTICAL_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { SENHALIZACION_VERTICAL_A,
		SENHALIZACION_VERTICAL_B, SENHALIZACION_VERTICAL_C,
		SENHALIZACION_VERTICAL_D, SENHALIZACION_VERTICAL_E };
    }

    @Override
    protected String calculate() {

	BigDecimal value = new BigDecimal(0);

	value = value.add(operandValue(SENHALIZACION_VERTICAL_A).multiply(
		weigthA));
	value = value.add(operandValue(SENHALIZACION_VERTICAL_B).multiply(
		weigthB));
	value = value.add(operandValue(SENHALIZACION_VERTICAL_C).multiply(
		weigthC));
	value = value.add(operandValue(SENHALIZACION_VERTICAL_D).multiply(
		weigthD));
	value = value.add(operandValue(SENHALIZACION_VERTICAL_E).multiply(
		weigthE));

	return formatter.format(value);
    }

}
