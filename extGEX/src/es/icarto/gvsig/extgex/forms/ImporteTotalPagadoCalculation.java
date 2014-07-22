package es.icarto.gvsig.extgex.forms;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;

public class ImporteTotalPagadoCalculation extends Calculation {

    public ImporteTotalPagadoCalculation(IValidatableForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName,
		operatorComponentsNames);
    }

    @Override
    public void setValue(boolean validate) {
	if (!validate) {
	    return;
	}

	String value = "";
	if (!allEmpty()) {
	    value = getImporteTotalPagado();
	}
	resultComponent.setText(value);
	form.getFormController().setValue(resultComponentName, value);
    }

    private String getImporteTotalPagado() {

	BigDecimal total = new BigDecimal(0);

	total = total
		.subtract(getDoubleValue(DBNames.FINCAS_DEPOSITO_PREVIO_LEVANTADO));
	total = total
		.add(getDoubleValue(DBNames.FINCAS_DEPOSITO_PREVIO_CONSIGNADO));
	total = total
		.add(getDoubleValue(DBNames.FINCAS_DEPOSITO_PREVIO_CONSIGNADO_INDEMNIZACION));
	total = total
		.add(getDoubleValue(DBNames.FINCAS_DEPOSITO_PREVIO_PAGADO));
	total = total.add(getDoubleValue(DBNames.FINCAS_MUTUO_ACUERDO));
	total = total.add(getDoubleValue(DBNames.FINCAS_ANTICIPO));
	total = total.add(getDoubleValue(DBNames.FINCAS_MUTUO_ACUERDO_PARCIAL));
	total = total.add(getDoubleValue(DBNames.FINCAS_LIMITE_ACUERDO_IMORTE));
	total = total.add(getDoubleValue(DBNames.FINCAS_PAGOS_VARIOS));
	total = total.add(getDoubleValue(DBNames.FINCAS_INDEMNIZACION_IMPORTE));
	total = total.add(getDoubleValue(DBNames.FINCAS_JUSTIPRECIO_IMPORTE));

	NumberFormat format = DoubleFormatNT.getBigDecimalFormat();
	return format.format(total);
    }
}
