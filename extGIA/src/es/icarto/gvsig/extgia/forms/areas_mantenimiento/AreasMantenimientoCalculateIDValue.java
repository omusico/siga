package es.icarto.gvsig.extgia.forms.areas_mantenimiento;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class AreasMantenimientoCalculateIDValue extends CalculateComponentValue {

    public AreasMantenimientoCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
    }

    @Override
    public void setValue(boolean validate) {
	String areaMantenimientoID = String.valueOf(SqlUtils.
		getNextIdOfSequence("audasa_extgia.areas_mantenimiento_id_area_mantenimiento_seq"));
	resultComponent.setText(areaMantenimientoID);
	form.getFormController().setValue(resultComponentName, areaMantenimientoID);

    }

}
