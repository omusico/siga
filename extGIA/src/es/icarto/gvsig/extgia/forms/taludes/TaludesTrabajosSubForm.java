package es.icarto.gvsig.extgia.forms.taludes;

import java.util.ArrayList;

import javax.swing.JComboBox;

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
	JComboBox unidadCB = (JComboBox) getFormPanel().getComponentByName(DBFieldNames.UNIDAD);
	String unidad = unidadCB.getSelectedItem().toString();
	ArrayList<ForeignValue> foreignValues = new ArrayList<ForeignValue>();
	foreignValues.add(new CalculateTaludesTrabajosMedicionElemento(getForeignKey()).getForeignValue());
	foreignValues.add(new CalculateTaludesTrabajosMedicionComplementaria(getForeignKey()).getForeignValue());
	foreignValues.add(new CalculateTaludesTrabajosMedicionUltimoTrabajo(getForeignKey(), unidad).getForeignValue());
	return foreignValues;
    }


}
