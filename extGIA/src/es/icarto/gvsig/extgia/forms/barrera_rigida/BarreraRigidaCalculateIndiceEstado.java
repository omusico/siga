package es.icarto.gvsig.extgia.forms.barrera_rigida;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.BARRERA_RIGIDA_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.BARRERA_RIGIDA_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.BARRERA_RIGIDA_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.BARRERA_RIGIDA_D;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.BARRERA_RIGIDA_INDEX;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class BarreraRigidaCalculateIndiceEstado extends Calculation {

    private static final BigDecimal weigthA = new BigDecimal("0.3");
    private static final BigDecimal weigthB = new BigDecimal("0.2");
    private static final BigDecimal weigthC = new BigDecimal("0.25");
    private static final BigDecimal weigthD = new BigDecimal("0.25");

    public BarreraRigidaCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return BARRERA_RIGIDA_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { BARRERA_RIGIDA_A, BARRERA_RIGIDA_B,
		BARRERA_RIGIDA_C, BARRERA_RIGIDA_D };
    }

    @Override
    protected String calculate() {

	BigDecimal value = new BigDecimal(0);

	value = value.add(operandValue(BARRERA_RIGIDA_A).multiply(weigthA));
	value = value.add(operandValue(BARRERA_RIGIDA_B).multiply(weigthB));
	value = value.add(operandValue(BARRERA_RIGIDA_C).multiply(weigthC));
	value = value.add(operandValue(BARRERA_RIGIDA_D).multiply(weigthD));

	return formatter.format(value);
    }

}
