package es.icarto.gvsig.extgia.forms.taludes;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.navtableforms.utils.CalculateComponentValue;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class CalculateIndiceEstado extends CalculateComponentValue {

    public CalculateIndiceEstado(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName,
		operatorComponentsNames);
    }

    @Override
    public void setValue(boolean validate) {

	float value = 0;

	String strA = ((KeyValue) ((JComboBox) operatorComponents
		.get("existencia_deformaciones_o_grietas")).getSelectedItem())
		.getKey();
	String strB = ((KeyValue) ((JComboBox) operatorComponents
		.get("peligro_caida_materiales")).getSelectedItem()).getKey();
	String strC = ((KeyValue) ((JComboBox) operatorComponents
		.get("bajante_deteriorada")).getSelectedItem()).getKey();
	String strD = ((KeyValue) ((JComboBox) operatorComponents
		.get("elementos_proteccion_talud")).getSelectedItem()).getKey();

	value += Integer.parseInt(strA) * 0.3;
	value += Integer.parseInt(strB) * 0.25;
	value += Integer.parseInt(strC) * 0.2;
	value += Integer.parseInt(strD) * 0.25;

	String strValue = Float.toString(value);
	resultComponent.setText(strValue);
	form.getFormController().setValue(resultComponentName, strValue);
    }
}
