package es.icarto.gvsig.extgia.forms.areas_descanso;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_DESCANSO_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_DESCANSO_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_DESCANSO_C;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_DESCANSO_INDEX;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class AreasDescansoCalculateIndiceEstado extends Calculation {

    private static final BigDecimal weigth = new BigDecimal("0.33");

    public AreasDescansoCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return AREA_DESCANSO_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { AREA_DESCANSO_A, AREA_DESCANSO_B, AREA_DESCANSO_C };
    }

    @Override
    protected String calculate() {
	BigDecimal value = new BigDecimal(0);

	value = value.add(operandValue(AREA_DESCANSO_A).multiply(weigth));
	value = value.add(operandValue(AREA_DESCANSO_B).multiply(weigth));
	value = value.add(operandValue(AREA_DESCANSO_C).multiply(weigth));

	return formatter.format(value);
    }

}
