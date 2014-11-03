package es.icarto.gvsig.extgia.forms.lecho_frenado;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;
import es.icarto.gvsig.extgia.forms.utils.CalculateReconocimientoIndexValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class LechoFrenadoCalculateIndiceEstado extends
CalculateReconocimientoIndexValue {

    public LechoFrenadoCalculateIndiceEstado(BasicAbstractSubForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	float value = 0;

	String strA = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.LECHO_FRENADO_A)).getSelectedItem())
		.getKey();
	String strB = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.LECHO_FRENADO_B)).getSelectedItem()).getKey();
	String strC = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.LECHO_FRENADO_C)).getSelectedItem()).getKey();

	value += Integer.parseInt(strA) * 0.4;
	value += Integer.parseInt(strB) * 0.3;
	value += Integer.parseInt(strC) * 0.3;

	double valueFormatted = Math.rint(value*1000)/1000;

	String strValue = Double.toString(valueFormatted);
	resultComponent.setText(strValue);

    }

}
