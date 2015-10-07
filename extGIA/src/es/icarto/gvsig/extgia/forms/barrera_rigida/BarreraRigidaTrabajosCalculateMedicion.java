package es.icarto.gvsig.extgia.forms.barrera_rigida;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.ForeignValue;
import es.icarto.gvsig.extgia.forms.VegetationCalculateMedicion;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.IValidatableForm;

public class BarreraRigidaTrabajosCalculateMedicion extends
VegetationCalculateMedicion {

    public BarreraRigidaTrabajosCalculateMedicion(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_BARRERA_RIGIDA;
    }

    @Override
    protected String getLongitudForeignValue() {
	return 	(new CalculateBarreraRigidaTrabajosLongitud(getForeignKey()).getForeignValue()).getValue();
    }

    @Override
    protected ForeignValue getMedicionForeignValue() {
	String unidad =
		((JComboBox) form.getWidgets().get(DBFieldNames.UNIDAD)).getSelectedItem().toString();
	ForeignValue medicionUltimoTrabajo =
		new CalculateBarreraRigidaTrabajosMedicionUltimoTrabajo(getForeignKey(), unidad).getForeignValue();
	if (medicionUltimoTrabajo != null) {
	    return medicionUltimoTrabajo;
	}
	return null;
    }

}
