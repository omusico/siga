package es.icarto.gvsig.extgia;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class CalculateTaludIDValue extends CalculateComponentValue {

    public CalculateTaludIDValue(AbstractForm form, String resultComponentName,
	    String... operatorComponentsNames) {
	super(form, resultComponentName, operatorComponentsNames);
    }

    /**
     * (primera letra "Tipo de Talud")&-&("Número de Talud")&(Primera letra de
     * "Base decontratista") ; EJ: D-584N
     * 
     * @param validate
     *            . True if the operatorComponents validate their checks
     * 
     */
    @Override
    public void setValue(boolean validate) {

	// TODO: Aplicar el formato adecuado a los valores base

	JComboBox tipoTaludWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.TIPO_TALUD);
	JTextField numeroTaludWidget = (JTextField) operatorComponents
		.get(DBFieldNames.NUMERO_TALUD);
	JComboBox baseContratistaWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.BASE_CONTRATISTA);

	String taludID = "";
	if (validate) {

	    taludID = String.format("%s-%03d%s", ((KeyValue) tipoTaludWidget
		    .getSelectedItem()).getValue().substring(0, 1), Integer
		    .valueOf(numeroTaludWidget.getText()),
		    ((KeyValue) baseContratistaWidget.getSelectedItem())
			    .getValue().substring(0, 1));
	}
	resultComponent.setText(taludID);
	form.getFormController().setValue(resultComponentName, taludID);

    }
}
