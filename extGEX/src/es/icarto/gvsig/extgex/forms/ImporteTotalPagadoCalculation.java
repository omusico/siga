package es.icarto.gvsig.extgex.forms;

import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_ANTICIPO;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_DEPOSITO_PREVIO_CONSIGNADO;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_DEPOSITO_PREVIO_CONSIGNADO_INDEMNIZACION;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_DEPOSITO_PREVIO_LEVANTADO;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_DEPOSITO_PREVIO_PAGADO;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_IMPORTE_PAGADO_TOTAL_AUTOCALCULADO;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_INDEMNIZACION_IMPORTE;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_JUSTIPRECIO_IMPORTE;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_LIMITE_ACUERDO_IMORTE;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_MUTUO_ACUERDO;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_MUTUO_ACUERDO_PARCIAL;
import static es.icarto.gvsig.extgex.preferences.DBNames.FINCAS_PAGOS_VARIOS;

import java.math.BigDecimal;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class ImporteTotalPagadoCalculation extends Calculation {

    public ImporteTotalPagadoCalculation(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return FINCAS_IMPORTE_PAGADO_TOTAL_AUTOCALCULADO;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { FINCAS_DEPOSITO_PREVIO_LEVANTADO,
		FINCAS_DEPOSITO_PREVIO_CONSIGNADO,
		FINCAS_DEPOSITO_PREVIO_CONSIGNADO_INDEMNIZACION,
		FINCAS_DEPOSITO_PREVIO_PAGADO, FINCAS_MUTUO_ACUERDO,
		FINCAS_ANTICIPO, FINCAS_MUTUO_ACUERDO_PARCIAL,
		FINCAS_LIMITE_ACUERDO_IMORTE, FINCAS_PAGOS_VARIOS,
		FINCAS_INDEMNIZACION_IMPORTE, FINCAS_JUSTIPRECIO_IMPORTE };
    }

    @Override
    protected String calculate() {

	BigDecimal total = new BigDecimal(0);

	total = total.subtract(operandValue(FINCAS_DEPOSITO_PREVIO_LEVANTADO));
	total = total.add(operandValue(FINCAS_DEPOSITO_PREVIO_CONSIGNADO));
	total = total
		.add(operandValue(FINCAS_DEPOSITO_PREVIO_CONSIGNADO_INDEMNIZACION));
	total = total.add(operandValue(FINCAS_DEPOSITO_PREVIO_PAGADO));
	total = total.add(operandValue(FINCAS_MUTUO_ACUERDO));
	total = total.add(operandValue(FINCAS_ANTICIPO));
	total = total.add(operandValue(FINCAS_MUTUO_ACUERDO_PARCIAL));
	total = total.add(operandValue(FINCAS_LIMITE_ACUERDO_IMORTE));
	total = total.add(operandValue(FINCAS_PAGOS_VARIOS));
	total = total.add(operandValue(FINCAS_INDEMNIZACION_IMPORTE));
	total = total.add(operandValue(FINCAS_JUSTIPRECIO_IMPORTE));

	return formatter.format(total);
    }
}
