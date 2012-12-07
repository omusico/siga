package es.icarto.gvsig.extgia.forms.isletas;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class CalculateIsletaIDValue extends CalculateComponentValue {

    public CalculateIsletaIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName,
		operatorComponentsNames);
    }

    /**
     * (primera letra "Tipo de Isleta")&-&("Número de Isleta")&(Primera letra de
     * "Base de contratista") ; EJ: D-584N
     * 
     * @param validate
     *            . True if the operatorComponents validate their checks
     * 
     */
    @Override
    public void setValue(boolean validate) {

	// TODO: Aplicar el formato adecuado a los valores base

	JComboBox tipoIsletaWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.TIPO_ISLETA);
	JTextField numeroIsletaWidget = (JTextField) operatorComponents
		.get(DBFieldNames.NUMERO_ISLETA);
	JComboBox baseContratistaWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.BASE_CONTRATISTA);

	String isletaID = "";
	if (validate) {

	    isletaID = String.format("%s-%03d%s", ((KeyValue) tipoIsletaWidget
		    .getSelectedItem()).getValue().substring(0, 1), Integer
		    .valueOf(numeroIsletaWidget.getText()),
		    ((KeyValue) baseContratistaWidget.getSelectedItem())
			    .getValue().substring(0, 1));
	}
	resultComponent.setText(isletaID);
	form.getFormController().setValue(resultComponentName, isletaID);

    }
}
