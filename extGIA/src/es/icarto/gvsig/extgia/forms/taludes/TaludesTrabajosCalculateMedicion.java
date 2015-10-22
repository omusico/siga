package es.icarto.gvsig.extgia.forms.taludes;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.ForeignValue;
import es.icarto.gvsig.extgia.forms.VegetationCalculateMedicion;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.IValidatableForm;

public class TaludesTrabajosCalculateMedicion extends
	VegetationCalculateMedicion {

    public TaludesTrabajosCalculateMedicion(IValidatableForm form) {
	super(form, DBFieldNames.ID_TALUD);
    }

    @Override
    protected ForeignValue getMedicionForeignValue() {
	String unidad = ((JComboBox) form.getWidgets().get(DBFieldNames.UNIDAD))
		.getSelectedItem().toString();
	ForeignValue medicionElemento = new CalculateDBForeignValue(
		getForeignKey(), DBFieldNames.MEDICION_ELEMENTO,
		DBFieldNames.SUP_TOTAL_ANALITICA,
		DBFieldNames.TALUDES_DBTABLENAME, DBFieldNames.ID_TALUD)
		.getForeignValue();

	ForeignValue medicionUltimoTrabajo = new CalculateDBForeignValueLastJob(
		unidad, getForeignKey(), DBFieldNames.MEDICION_ULTIMO_TRABAJO,
		DBFieldNames.TALUDES_TRABAJOS_DBTABLENAME,
		DBFieldNames.ID_TALUD).getForeignValue();
	if (medicionUltimoTrabajo.getValue() != null) {
	    return medicionUltimoTrabajo;
	} else {
	    return medicionElemento;
	}
    }

}
