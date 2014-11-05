package es.icarto.gvsig.extgia.forms.taludes;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.TALUDES_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.TALUDES_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.TALUDES_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.TALUDES_D;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.TALUDES_INDEX;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class TaludesCalculateIndiceEstado extends Calculation {

    private static final BigDecimal weigthA = new BigDecimal("0.3");
    private static final BigDecimal weigthB = new BigDecimal("0.25");
    private static final BigDecimal weigthC = new BigDecimal("0.2");
    private static final BigDecimal weigthD = new BigDecimal("0.25");

    public TaludesCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return TALUDES_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { TALUDES_A, TALUDES_B, TALUDES_C, TALUDES_D };
    }

    @Override
    protected String calculate() {

	BigDecimal value = new BigDecimal(0);

	value = value.add(operandValue(TALUDES_A).multiply(weigthA));
	value = value.add(operandValue(TALUDES_B).multiply(weigthB));
	value = value.add(operandValue(TALUDES_C).multiply(weigthC));
	value = value.add(operandValue(TALUDES_D).multiply(weigthD));

	return formatter.format(value);
    }
}
