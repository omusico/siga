package es.icarto.gvsig.extgia.forms.isletas;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class IsletaCalculateIDValue extends CalculateComponentValue {

    public IsletaCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName,
		operatorComponentsNames);
    }

    /**
     * (primera letra "Tipo de Isleta")&-&("Número de Isleta")&(Primera letra de
     * "Base de contratista") ; EJ: I-584N
     * 
     * @param validate
     *            . True if the operatorComponents validate their checks
     * 
     */
    @Override
    public void setValue(boolean validate) {

	// TODO: Aplicar el formato adecuado a los valores base

	JTextField numeroIsletaWidget = (JTextField) operatorComponents
		.get(DBFieldNames.NUMERO_ISLETA);
	JComboBox baseContratistaWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.BASE_CONTRATISTA);

	if (numeroIsletaWidget.getText().isEmpty()) {
	    validate = false;
	}

	String isletaID = "";
	if (validate) {

	    isletaID = String.format("%s-%03d%s", "I", Integer
		    .valueOf(numeroIsletaWidget.getText()),
		    ((KeyValue) baseContratistaWidget.getSelectedItem())
		    .getValue().substring(0, 1));
	}
	resultComponent.setText(isletaID);
	form.getFormController().setValue(resultComponentName, isletaID);

    }
}
