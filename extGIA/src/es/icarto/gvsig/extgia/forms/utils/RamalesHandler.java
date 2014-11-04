package es.icarto.gvsig.extgia.forms.utils;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class RamalesHandler extends GIAAlphanumericTableHandler {

    private final JButton addRamalButton;
    private final JButton editRamalButton;
    private final JButton deleteRamalButton;

    public RamalesHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases, AbstractForm form) {
	super(tableName, widgets, foreignKeyId, colNames, colAliases,
		new BasicAbstractSubForm(tableName));

	addRamalButton = (JButton) form.getFormBody().getComponentByName(
		"add_ramal_button");
	editRamalButton = (JButton) form.getFormBody().getComponentByName(
		"edit_ramal_button");
	deleteRamalButton = (JButton) form.getFormBody().getComponentByName(
		"delete_ramal_button");
    }

    @Override
    public void reload() {
	super.reload();
	addRamalButton.setAction(getListener().getCreateAction());
	editRamalButton.setAction(getListener().getUpdateAction());
	deleteRamalButton.setAction(getListener().getDeleteAction());
    }

    @Override
    public void removeListeners() {
	addRamalButton.setAction(null);
	editRamalButton.setAction(null);
	deleteRamalButton.setAction(null);
	super.removeListeners();
    }
}
