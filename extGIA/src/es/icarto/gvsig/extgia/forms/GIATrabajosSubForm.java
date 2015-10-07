package es.icarto.gvsig.extgia.forms;

import java.util.ArrayList;

import javax.swing.JTextField;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;

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
	getWindowInfo().setTitle("Añadir Trabajo");
    }

    @Override
    public void actionUpdateRecord(long position) {
	this.setEditing(true);
	super.actionUpdateRecord(position);
	fillForeignValues();
	getWindowInfo().setTitle("Editar Trabajo");
    }

    private void fillForeignValues() {
	for (ForeignValue fv : getForeignValues()) {
	    JTextField component = (JTextField) getFormPanel()
		    .getComponentByName(fv.getComponent());
	    String v = (fv.getValue() != null) ? fv.getValue() : "";
	    component.setText(v);
	    getFormController().setValue(fv.getComponent(), v);
	}
    }

    private void fillMedicionValue() {
	for (ForeignValue fv : getForeignValues()) {
	    JTextField medicionComponent = (JTextField) getFormPanel()
		    .getComponentByName(DBFieldNames.MEDICION);
	    if (fv.getComponent().equalsIgnoreCase(DBFieldNames.MEDICION_ULTIMO_TRABAJO)
		    && fv.getValue() != null) {
		medicionComponent.setText(fv.getValue());
		getFormController().setValue(medicionComponent.getName(), fv.getValue());
		break;
	    }
	    if (fv.getComponent().equalsIgnoreCase(DBFieldNames.MEDICION_ELEMENTO)
		    && fv.getValue() != null) {
		medicionComponent.setText(fv.getValue());
		getFormController().setValue(medicionComponent.getName(), fv.getValue());
	    }
	}
    }

    protected abstract ArrayList<ForeignValue> getForeignValues();

    protected void setEditing(boolean editing) {
	this.editing = editing;
    }

    protected boolean isEditing() {
	return editing;
    }
}
