package es.icarto.gvsig.extgia.forms.taludes;

import java.util.ArrayList;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.CalculateDBForeignValue;
import es.icarto.gvsig.extgia.forms.CalculateDBForeignValueLastJob;
import es.icarto.gvsig.extgia.forms.ForeignValue;
import es.icarto.gvsig.extgia.forms.GIATrabajosSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class TaludesTrabajosSubForm extends GIATrabajosSubForm {
    public TaludesTrabajosSubForm() {
	super("taludes_trabajos");
	addCalculation(new TaludesTrabajosCalculateMedicion(this));
    }

    @Override
    protected ArrayList<ForeignValue> getForeignValues() {
	JComboBox unidadCB = (JComboBox) getFormPanel().getComponentByName(
		DBFieldNames.UNIDAD);
	String unidad = unidadCB.getSelectedItem().toString();
	ArrayList<ForeignValue> foreignValues = new ArrayList<ForeignValue>();
	foreignValues.add(new CalculateDBForeignValue(getForeignKey(),
		DBFieldNames.MEDICION_ELEMENTO,
		DBFieldNames.SUP_TOTAL_ANALITICA,
		DBFieldNames.TALUDES_DBTABLENAME, DBFieldNames.ID_TALUD)
	.getForeignValue());

	foreignValues.add(new CalculateDBForeignValue(getForeignKey(),
		DBFieldNames.MEDICION_COMPLEMENTARIA,
		DBFieldNames.SUP_COMPLEMENTARIA,
		DBFieldNames.TALUDES_DBTABLENAME, DBFieldNames.ID_TALUD)
		.getForeignValue());

	foreignValues.add(new CalculateDBForeignValueLastJob(unidad,
		getForeignKey(), DBFieldNames.MEDICION_ULTIMO_TRABAJO,
			DBFieldNames.TALUDES_TRABAJOS_DBTABLENAME,
			DBFieldNames.ID_TALUD).getForeignValue());

	return foreignValues;
    }

}
