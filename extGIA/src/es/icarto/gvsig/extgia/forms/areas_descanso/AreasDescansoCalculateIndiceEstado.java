package es.icarto.gvsig.extgia.forms.areas_descanso;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.forms.utils.CalculateReconocimientoIndexValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class AreasDescansoCalculateIndiceEstado extends CalculateReconocimientoIndexValue {

    public AreasDescansoCalculateIndiceEstado(AbstractSubForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	float value = 0;

	String strA = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.AREA_SERVICIO_A)).getSelectedItem())
		.getKey();
	String strB = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.AREA_SERVICIO_B)).getSelectedItem()).getKey();
	String strC = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.AREA_SERVICIO_C)).getSelectedItem()).getKey();

	value += Integer.parseInt(strA) * 0.33;
	value += Integer.parseInt(strB) * 0.33;
	value += Integer.parseInt(strC) * 0.33;

	String strValue = Float.toString(value);
	resultComponent.setText(strValue);

    }

}
