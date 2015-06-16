package es.icarto.gvsig.extgia.forms.taludes;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.utils.ForeignValue;
import es.icarto.gvsig.extgia.forms.utils.VegetationCalculateMedicion;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.IValidatableForm;

public class TaludesTrabajosCalculateMedicion extends
VegetationCalculateMedicion {

    public TaludesTrabajosCalculateMedicion(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String getIDField() {
	return DBFieldNames.ID_TALUD;
    }

    @Override
    protected String getLongitudForeignValue() {
	return 	(new CalculateTaludesTrabajosLongitud(getForeignKey()).getForeignValue()).getValue();
    }

    @Override
    protected ForeignValue getMedicionForeignValue() {
	String unidad =
		((JComboBox) form.getWidgets().get(DBFieldNames.UNIDAD)).getSelectedItem().toString();
	ForeignValue medicionElemento =
		new CalculateTaludesTrabajosMedicionElemento(getForeignKey()).getForeignValue();
	ForeignValue medicionUltimoTrabajo =
		new CalculateTaludesTrabajosMedicionUltimoTrabajo(getForeignKey(),unidad).getForeignValue();
	if (medicionUltimoTrabajo.getValue() != null) {
	    return medicionUltimoTrabajo;
	} else {
	    return medicionElemento;
	}
    }

}
