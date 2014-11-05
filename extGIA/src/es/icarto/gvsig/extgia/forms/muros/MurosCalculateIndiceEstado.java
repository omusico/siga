package es.icarto.gvsig.extgia.forms.muros;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_D;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_E;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_F;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_G;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_H;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_I;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_INDEX;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MUROS_J;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class MurosCalculateIndiceEstado extends Calculation {

    private static final BigDecimal weigthA = new BigDecimal("0.2");
    private static final BigDecimal weigthB = new BigDecimal("0.2");
    private static final BigDecimal weigthC = new BigDecimal("0.05");
    private static final BigDecimal weigthD = new BigDecimal("0.35");
    private static final BigDecimal weigthE = new BigDecimal("0.08");
    private static final BigDecimal weigthF = new BigDecimal("0.07");
    private static final BigDecimal weigthG = new BigDecimal("0.04");
    private static final BigDecimal weigthH = new BigDecimal("0.04");
    private static final BigDecimal weigthI = new BigDecimal("0.10");
    private static final BigDecimal weigthJ = new BigDecimal("0.07");

    public MurosCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return MUROS_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { MUROS_A, MUROS_B, MUROS_C, MUROS_D, MUROS_E,
		MUROS_F, MUROS_G, MUROS_H, MUROS_I, MUROS_J };
    }

    @Override
    protected String calculate() {

	BigDecimal value = new BigDecimal(0);

	if (operandValue(MUROS_A).compareTo(operandValue(MUROS_B)) > 0) {
	    value = value.add(operandValue(MUROS_A).multiply(weigthA));
	} else {
	    value = value.add(operandValue(MUROS_B).multiply(weigthB));
	}
	value = value.add(operandValue(MUROS_C).multiply(weigthC));
	value = value.add(operandValue(MUROS_D).multiply(weigthD));
	value = value.add(operandValue(MUROS_E).multiply(weigthE));
	value = value.add(operandValue(MUROS_F).multiply(weigthF));
	value = value.add(operandValue(MUROS_G).multiply(weigthG));
	value = value.add(operandValue(MUROS_H).multiply(weigthH));
	value = value.add(operandValue(MUROS_I).multiply(weigthI));
	value = value.add(operandValue(MUROS_J).multiply(weigthJ));

	return formatter.format(value);
    }

}
