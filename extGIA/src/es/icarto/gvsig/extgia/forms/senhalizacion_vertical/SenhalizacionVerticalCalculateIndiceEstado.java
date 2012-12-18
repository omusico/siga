package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.forms.utils.CalculateReconocimientoIndexValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class SenhalizacionVerticalCalculateIndiceEstado extends CalculateReconocimientoIndexValue {

    public SenhalizacionVerticalCalculateIndiceEstado(AbstractSubForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	float value = 0;

	String strA = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.SENHALIZACION_VERTICAL_A)).getSelectedItem())
		.getKey();
	String strB = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.SENHALIZACION_VERTICAL_B)).getSelectedItem()).getKey();
	String strC = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.SENHALIZACION_VERTICAL_C)).getSelectedItem()).getKey();
	String strD = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.SENHALIZACION_VERTICAL_D)).getSelectedItem()).getKey();
	String strE = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.SENHALIZACION_VERTICAL_E)).getSelectedItem()).getKey();

	value += Integer.parseInt(strA) * 0.2;
	value += Integer.parseInt(strB) * 0.1;
	value += Integer.parseInt(strC) * 0.1;
	value += Integer.parseInt(strD) * 0.35;
	value += Integer.parseInt(strE) * 0.25;

	String strValue = Float.toString(value);
	resultComponent.setText(strValue);

    }

}
