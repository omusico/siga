package es.icarto.gvsig.extgia.forms.isletas;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.ForeignValue;
import es.icarto.gvsig.extgia.forms.VegetationCalculateMedicion;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.IValidatableForm;

public class IsletasTrabajosCalculateMedicion extends
VegetationCalculateMedicion {

    public IsletasTrabajosCalculateMedicion(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_ISLETA;
    }

    @Override
    protected String getLongitudForeignValue() {
	return null;
    }

    @Override
    protected ForeignValue getMedicionForeignValue() {
	String unidad =
		((JComboBox) form.getWidgets().get(DBFieldNames.UNIDAD)).getSelectedItem().toString();
	ForeignValue medicionElemento =
		new CalculateIsletasTrabajosMedicionElemento(getForeignKey()).getForeignValue();
	ForeignValue medicionUltimoTrabajo =
		new CalculateIsletasTrabajosMedicionUltimoTrabajo(getForeignKey(),unidad).getForeignValue();
	if (medicionUltimoTrabajo.getValue() != null) {
	    return medicionUltimoTrabajo;
	} else {
	    return medicionElemento;
	}
    }

}
