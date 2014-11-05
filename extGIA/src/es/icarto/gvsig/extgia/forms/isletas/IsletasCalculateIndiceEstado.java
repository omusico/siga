package es.icarto.gvsig.extgia.forms.isletas;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class IsletasCalculateIndiceEstado extends Calculation {

    public IsletasCalculateIndiceEstado(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return DBFieldNames.ISLETAS_INDEX;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { DBFieldNames.ISLETAS_A };
    }

    @Override
    protected String calculate() {
	return formatter.format(operandValue(DBFieldNames.ISLETAS_A));
    }

}
