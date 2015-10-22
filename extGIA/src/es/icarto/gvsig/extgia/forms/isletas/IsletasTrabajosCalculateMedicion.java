package es.icarto.gvsig.extgia.forms.isletas;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
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
    protected ForeignValue getMedicionForeignValue() {
	String unidad = ((JComboBox) form.getWidgets().get(DBFieldNames.UNIDAD))
		.getSelectedItem().toString();

	ForeignValue medicionElemento = new CalculateDBForeignValue(
		getForeignKey(), DBFieldNames.MEDICION_ELEMENTO,
		DBFieldNames.SUPERFICIE_BAJO_BIONDA,
		DBFieldNames.ISLETAS_DBTABLENAME, DBFieldNames.ID_ISLETA)
	.getForeignValue();

	ForeignValue medicionUltimoTrabajo = new CalculateDBForeignValueLastJob(
		unidad, getForeignKey(), DBFieldNames.MEDICION_ULTIMO_TRABAJO,
		DBFieldNames.ISLETAS_TRABAJOS_DBTABLENAME,
		DBFieldNames.ID_ISLETA).getForeignValue();
	if (medicionUltimoTrabajo.getValue() != null) {
	    return medicionUltimoTrabajo;
	} else {
	    return medicionElemento;
	}
    }

}
