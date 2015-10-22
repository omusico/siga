package es.icarto.gvsig.extgia.forms.isletas;

import java.util.ArrayList;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.ForeignValue;
import es.icarto.gvsig.extgia.forms.GIATrabajosSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class IsletasTrabajosSubForm extends GIATrabajosSubForm {
    public IsletasTrabajosSubForm() {
	super("isletas_trabajos");
	addCalculation(new IsletasTrabajosCalculateMedicion(this));
    }

    @Override
    protected ArrayList<ForeignValue> getForeignValues() {
	JComboBox unidadCB = (JComboBox) getFormPanel().getComponentByName(
		DBFieldNames.UNIDAD);
	String unidad = unidadCB.getSelectedItem().toString();
	ArrayList<ForeignValue> foreignValues = new ArrayList<ForeignValue>();
	foreignValues.add(new CalculateDBForeignValue(getForeignKey(),
		DBFieldNames.MEDICION_ELEMENTO,
		DBFieldNames.SUPERFICIE_BAJO_BIONDA,
		DBFieldNames.ISLETAS_DBTABLENAME, DBFieldNames.ID_ISLETA)
	.getForeignValue());
	foreignValues.add(new CalculateDBForeignValueLastJob(unidad,
		getForeignKey(), DBFieldNames.MEDICION_ULTIMO_TRABAJO,
		DBFieldNames.ISLETAS_TRABAJOS_DBTABLENAME,
		DBFieldNames.ID_ISLETA).getForeignValue());

	return foreignValues;
    }

}
