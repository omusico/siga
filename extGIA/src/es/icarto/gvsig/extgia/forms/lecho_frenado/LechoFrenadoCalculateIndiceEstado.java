package es.icarto.gvsig.extgia.forms.lecho_frenado;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.LECHO_FRENADO_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.LECHO_FRENADO_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.LECHO_FRENADO_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.LECHO_FRENADO_INDEX;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class LechoFrenadoCalculateIndiceEstado extends Calculation {

    private static final BigDecimal weigthA = new BigDecimal("0.4");
    private static final BigDecimal weigthB = new BigDecimal("0.3");
    private static final BigDecimal weigthC = new BigDecimal("0.3");

    public LechoFrenadoCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return LECHO_FRENADO_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { LECHO_FRENADO_A, LECHO_FRENADO_B, LECHO_FRENADO_C };
    }

    @Override
    protected String calculate() {

	BigDecimal value = new BigDecimal(0);

	value = value.add(operandValue(LECHO_FRENADO_A).multiply(weigthA));
	value = value.add(operandValue(LECHO_FRENADO_B).multiply(weigthB));
	value = value.add(operandValue(LECHO_FRENADO_C).multiply(weigthC));

	// TODO: double valueFormatted = Math.rint(value*1000)/1000;

	return formatter.format(value);
    }

}
