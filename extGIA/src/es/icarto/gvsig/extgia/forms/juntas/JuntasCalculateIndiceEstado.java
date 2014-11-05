package es.icarto.gvsig.extgia.forms.juntas;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.JUNTAS_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.JUNTAS_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.JUNTAS_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.JUNTAS_D;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.JUNTAS_E;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.JUNTAS_INDEX;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class JuntasCalculateIndiceEstado extends Calculation {
    private static final BigDecimal weigth = new BigDecimal("0.2");

    public JuntasCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return JUNTAS_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { JUNTAS_A, JUNTAS_B, JUNTAS_C, JUNTAS_D, JUNTAS_E };
    }

    @Override
    protected String calculate() {

	BigDecimal value = new BigDecimal(0);

	value = value.add(operandValue(JUNTAS_A).multiply(weigth));
	value = value.add(operandValue(JUNTAS_B).multiply(weigth));
	value = value.add(operandValue(JUNTAS_C).multiply(weigth));
	value = value.add(operandValue(JUNTAS_D).multiply(weigth));
	value = value.add(operandValue(JUNTAS_E).multiply(weigth));

	return formatter.format(value);
    }

}
