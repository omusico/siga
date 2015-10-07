package es.icarto.gvsig.extgia.forms.barrera_rigida;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class BarreraRigidaCalculateIDValue extends CalculateComponentValue {

    public BarreraRigidaCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    /**
     * ("BR")&-&("Número de Barrera")&(Primera letra de
     * "Base de contratista") ; EJ: BR-001N
     * 
     * @param validate
     *            . True if the operatorComponents validate their checks
     * 
     */
    @Override
    public void setValue(boolean validate) {
	JTextField numeroBarreraWidget = (JTextField) operatorComponents
		.get(DBFieldNames.NUMERO_BARRERA_RIGIDA);
	JComboBox baseContratistaWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.BASE_CONTRATISTA);

	if (numeroBarreraWidget.getText().isEmpty()) {
	    validate = false;
	}

	String barreraRigidaID = "";
	if (validate) {

	    barreraRigidaID = String.format("%s-%03d%s", "BR", Integer
		    .valueOf(numeroBarreraWidget.getText()),
		    ((KeyValue) baseContratistaWidget.getSelectedItem())
		    .getValue().substring(0, 1));
	}
	resultComponent.setText(barreraRigidaID);
	form.getFormController().setValue(resultComponentName, barreraRigidaID);

    }

}
