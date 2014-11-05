package es.icarto.gvsig.extgia.forms.valla_cierre;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.VALLA_CIERRE_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.VALLA_CIERRE_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.VALLA_CIERRE_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.VALLA_CIERRE_INDEX;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class VallaCierreCalculateIndiceEstado extends Calculation {

    private static final BigDecimal weigthA = new BigDecimal("0.4");
    private static final BigDecimal weigthB = new BigDecimal("0.4");
    private static final BigDecimal weigthC = new BigDecimal("0.2");

    public VallaCierreCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return VALLA_CIERRE_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { VALLA_CIERRE_A, VALLA_CIERRE_B, VALLA_CIERRE_C };
    }

    @Override
    protected String calculate() {

	BigDecimal value = new BigDecimal(0);

	value = value.add(operandValue(VALLA_CIERRE_A).multiply(weigthA));
	value = value.add(operandValue(VALLA_CIERRE_B).multiply(weigthB));
	value = value.add(operandValue(VALLA_CIERRE_C).multiply(weigthC));

	return formatter.format(value);
    }

}
