package es.icarto.gvsig.extgia.forms.barrera_rigida;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.LONGITUD;

import java.util.ArrayList;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.ForeignValue;
import es.icarto.gvsig.extgia.forms.GIATrabajosSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Elements;

@SuppressWarnings("serial")
public class BarreraRigidaTrabajosSubForm extends GIATrabajosSubForm {

    private final Elements br = Elements.Barrera_Rigida;

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
		DBFieldNames.BARRERA_RIGIDA_TRABAJOS_DBTABLENAME, br.pk)
		.getForeignValue());

	foreignValues.add(new CalculateDBForeignValue(getForeignKey(),
		LONGITUD, DBFieldNames.BARRERA_RIGIDA_LONGITUD,
		DBFieldNames.BARRERA_RIGIDA_DBTABLENAME, br.pk)
	.getForeignValue());

	return foreignValues;
    }
}