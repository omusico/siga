package es.icarto.gvsig.extgia.forms.barrera_rigida;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.forms.utils.CalculateReconocimientoIndexValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class BarreraRigidaCalculateIndiceEstado extends CalculateReconocimientoIndexValue {

    public BarreraRigidaCalculateIndiceEstado(AbstractSubForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	float value = 0;

	String strA = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.BARRERA_RIGIDA_A)).getSelectedItem())
		.getKey();
	String strB = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.BARRERA_RIGIDA_B)).getSelectedItem()).getKey();
	String strC = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.BARRERA_RIGIDA_C)).getSelectedItem()).getKey();
	String strD = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.BARRERA_RIGIDA_D)).getSelectedItem()).getKey();

	value += Integer.parseInt(strA) * 0.3;
	value += Integer.parseInt(strB) * 0.2;
	value += Integer.parseInt(strC) * 0.25;
	value += Integer.parseInt(strD) * 0.25;

	String strValue = Float.toString(value);
	resultComponent.setText(strValue);
    }
}
