package es.icarto.gvsig.extgia.forms.barrera_rigida;

import java.util.ArrayList;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.ForeignValue;
import es.icarto.gvsig.extgia.forms.GIATrabajosSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BarreraRigidaTrabajosSubForm extends GIATrabajosSubForm {
    public BarreraRigidaTrabajosSubForm() {
	super("barrera_rigida_trabajos");
	addCalculation(new BarreraRigidaTrabajosCalculateMedicion(this));
    }

    @Override
    protected ArrayList<ForeignValue> getForeignValues() {
	JComboBox unidadCB = (JComboBox) getFormPanel().getComponentByName(
		DBFieldNames.UNIDAD);
	String unidad = unidadCB.getSelectedItem().toString();
	ArrayList<ForeignValue> foreignValues = new ArrayList<ForeignValue>();
	foreignValues.add(new CalculateDBForeignValueLastJob(unidad,
		getForeignKey(), DBFieldNames.MEDICION_ULTIMO_TRABAJO,
			DBFieldNames.BARRERA_RIGIDA_TRABAJOS_DBTABLENAME,
			DBFieldNames.ID_BARRERA_RIGIDA).getForeignValue());
	return foreignValues;
    }
}