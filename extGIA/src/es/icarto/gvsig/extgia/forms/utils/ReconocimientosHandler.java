package es.icarto.gvsig.extgia.forms.utils;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class ReconocimientosHandler extends GIAAlphanumericTableHandler {

    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;

    public ReconocimientosHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases, AbstractForm form) {
	super(tableName, widgets, foreignKeyId, colNames, colAliases,
		new BasicAbstractSubForm(tableName));

	addButton = (JButton) form.getFormBody().getComponentByName(
		"add_reconocimiento_button");
	editButton = (JButton) form.getFormBody().getComponentByName(
		"edit_reconocimiento_button");
	deleteButton = (JButton) form.getFormBody().getComponentByName(
		"delete_reconocimiento_button");
    }

    @Override
    public void reload() {
	super.reload();
	addButton.setAction(getListener().getCreateAction());
	editButton.setAction(getListener().getUpdateAction());
	deleteButton.setAction(getListener().getDeleteAction());
    }

    @Override
    public void removeListeners() {
	addButton.setAction(null);
	editButton.setAction(null);
	deleteButton.setAction(null);
	super.removeListeners();
    }
}
