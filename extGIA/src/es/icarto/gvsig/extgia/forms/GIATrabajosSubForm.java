package es.icarto.gvsig.extgia.forms;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MEDICION;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MEDICION_ELEMENTO;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MEDICION_ULTIMO_TRABAJO;

import java.util.ArrayList;

@SuppressWarnings("serial")
public abstract class GIATrabajosSubForm extends GIASubForm {

    boolean editing = false;

    public GIATrabajosSubForm(String basicName) {
	super(basicName);
    }

    @Override
    public void actionCreateRecord() {
	this.setEditing(false);
	super.actionCreateRecord();
	fillForeignValues();
	fillMedicionValue();
	getWindowInfo().setTitle("A�adir Trabajo");
    }

    @Override
    public void actionUpdateRecord(long position) {
	this.setEditing(true);
	super.actionUpdateRecord(position);
	fillForeignValues();
	getWindowInfo().setTitle("Editar Trabajo");
    }

    protected void fillForeignValues() {
	for (ForeignValue fv : getForeignValues()) {
	    String v = (fv.getValue() != null) ? fv.getValue() : "";
	    final String compName = fv.getComponent();
	    getFormPanel().getTextField(compName).setText(v);
	    getFormController().setValue(compName, v);
	}
    }

    protected void fillMedicionValue() {
	String medicionValue = "";

	for (ForeignValue fv : getForeignValues()) {
	    if (fv.getComponent().equals(MEDICION_ULTIMO_TRABAJO)
		    && fv.getValue() != null) {
		medicionValue = fv.getValue();
		break;
	    }
	    if (fv.getComponent().equals(MEDICION_ELEMENTO)
		    && fv.getValue() != null) {
		medicionValue = fv.getValue();
		medicionValue = fv.getValue();
	    }
	}
	getFormPanel().getTextField(MEDICION).setText(medicionValue);
	getFormController().setValue(MEDICION, medicionValue);
    }

    protected abstract ArrayList<ForeignValue> getForeignValues();

    protected void setEditing(boolean editing) {
	this.editing = editing;
    }

    protected boolean isEditing() {
	return editing;
    }
}
