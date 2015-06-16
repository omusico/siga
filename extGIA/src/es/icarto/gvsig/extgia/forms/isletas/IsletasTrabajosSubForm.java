package es.icarto.gvsig.extgia.forms.isletas;

import java.util.ArrayList;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.forms.utils.ForeignValue;
import es.icarto.gvsig.extgia.forms.utils.GIATrabajosSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class IsletasTrabajosSubForm extends GIATrabajosSubForm {
    public IsletasTrabajosSubForm() {
	super("isletas_trabajos");
	addCalculation(new IsletasTrabajosCalculateMedicion(this));
    }

    @Override
    protected ArrayList<ForeignValue> getForeignValues() {
	JComboBox unidadCB = (JComboBox) getFormPanel().getComponentByName(DBFieldNames.UNIDAD);
	String unidad = unidadCB.getSelectedItem().toString();
	ArrayList<ForeignValue> foreignValues = new ArrayList<ForeignValue>();
	foreignValues.add(new CalculateIsletasTrabajosMedicionElemento(getForeignKey()).getForeignValue());
	foreignValues.add(new CalculateIsletasTrabajosMedicionUltimoTrabajo(getForeignKey(), unidad).getForeignValue());
	return foreignValues;
    }

}
