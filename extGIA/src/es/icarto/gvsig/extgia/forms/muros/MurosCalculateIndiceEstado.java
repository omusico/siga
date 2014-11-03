package es.icarto.gvsig.extgia.forms.muros;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;
import es.icarto.gvsig.extgia.forms.utils.CalculateReconocimientoIndexValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class MurosCalculateIndiceEstado extends
CalculateReconocimientoIndexValue {

    public MurosCalculateIndiceEstado(BasicAbstractSubForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	float value = 0;

	String strA = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_A)).getSelectedItem())
		.getKey();
	String strB = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_B)).getSelectedItem()).getKey();
	String strC = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_C)).getSelectedItem()).getKey();
	String strD = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_D)).getSelectedItem()).getKey();
	String strE = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_E)).getSelectedItem()).getKey();
	String strF = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_F)).getSelectedItem()).getKey();
	String strG = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_G)).getSelectedItem()).getKey();
	String strH = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_H)).getSelectedItem()).getKey();
	String strI = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_I)).getSelectedItem()).getKey();
	String strJ = ((KeyValue) ((JComboBox) operatorComponents
		.get(DBFieldNames.MUROS_J)).getSelectedItem()).getKey();

	if (Integer.parseInt(strA) > Integer.parseInt(strB)) {
	    value += Integer.parseInt(strA) * 0.20;
	}else {
	    value += Integer.parseInt(strB) * 0.20;
	}
	value += Integer.parseInt(strC) * 0.05;
	value += Integer.parseInt(strD) * 0.35;
	value += Integer.parseInt(strE) * 0.08;
	value += Integer.parseInt(strF) * 0.07;
	value += Integer.parseInt(strG) * 0.04;
	value += Integer.parseInt(strH) * 0.04;
	value += Integer.parseInt(strI) * 0.10;
	value += Integer.parseInt(strJ) * 0.07;

	double valueFormatted = Math.rint(value*1000)/1000;

	String strValue = Double.toString(valueFormatted);
	resultComponent.setText(strValue);
    }

}
