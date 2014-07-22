package es.icarto.gvsig.extgex.forms;

import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_IMPORTE_PENDIENTE_MEJORAS;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_IMPORTE_PENDIENTE_TERRENOS;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_IMPORTE_PENDIENTE_TOTAL_AUTOCALCULADO;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class ImportePendienteTotalCalculation extends Calculation {

    public ImportePendienteTotalCalculation(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return FINCAS_IMPORTE_PENDIENTE_TOTAL_AUTOCALCULADO;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { FINCAS_IMPORTE_PENDIENTE_MEJORAS,
		FINCAS_IMPORTE_PENDIENTE_TERRENOS };
    }

    @Override
    protected String calculate() {
	BigDecimal total = new BigDecimal(0);
	total = total.add(operandValue(FINCAS_IMPORTE_PENDIENTE_MEJORAS));
	total = total.add(operandValue(FINCAS_IMPORTE_PENDIENTE_TERRENOS));

	return formatter.format(total);
    }

}
