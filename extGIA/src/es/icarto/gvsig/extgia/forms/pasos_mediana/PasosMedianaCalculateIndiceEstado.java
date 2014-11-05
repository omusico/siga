package es.icarto.gvsig.extgia.forms.pasos_mediana;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.PASOS_MEDIANA_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.PASOS_MEDIANA_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.PASOS_MEDIANA_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.PASOS_MEDIANA_D;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.PASOS_MEDIANA_INDEX;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class PasosMedianaCalculateIndiceEstado extends Calculation {

    private static final BigDecimal weigthA = new BigDecimal("0.2");
    private static final BigDecimal weigthB = new BigDecimal("0.35");
    private static final BigDecimal weigthC = new BigDecimal("0.1");
    private static final BigDecimal weigthD = new BigDecimal("0.35");

    public PasosMedianaCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return PASOS_MEDIANA_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { PASOS_MEDIANA_A, PASOS_MEDIANA_B,
		PASOS_MEDIANA_C, PASOS_MEDIANA_D };
    }

    @Override
    protected String calculate() {

	BigDecimal value = new BigDecimal(0);

	value = value.add(operandValue(PASOS_MEDIANA_A).multiply(weigthA));
	value = value.add(operandValue(PASOS_MEDIANA_B).multiply(weigthB));
	value = value.add(operandValue(PASOS_MEDIANA_C).multiply(weigthC));
	value = value.add(operandValue(PASOS_MEDIANA_D).multiply(weigthD));

	return formatter.format(value);
    }
}
