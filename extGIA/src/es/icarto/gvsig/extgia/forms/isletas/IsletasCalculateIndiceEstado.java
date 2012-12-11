package es.icarto.gvsig.extgia.forms.isletas;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.forms.utils.CalculateReconocimientoIndexValue;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class IsletasCalculateIndiceEstado extends CalculateReconocimientoIndexValue {

    public IsletasCalculateIndiceEstado(AbstractSubForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {

	String strA = ((KeyValue) ((JComboBox) operatorComponents
		.get("estado_siega")).getSelectedItem())
		.getKey();

	resultComponent.setText(strA);
    }

}
