package es.icarto.gvsig.extgia.forms.utils;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;
import es.icarto.gvsig.navtableforms.gui.tables.menu.AlphanumericCompleteJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

public class GIAAlphanumericTableHandler extends BaseTableHandler {

    private static final Logger logger = Logger
	    .getLogger(GIAAlphanumericTableHandler.class);

    private AbstractSubForm subform;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;

    public GIAAlphanumericTableHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases, AbstractForm form) {

	super(tableName, widgets, foreignKeyId, colNames, colAliases);

	FormFactory.checkAndLoadTableRegistered(tableName);
	subform = new GIASubForm(tableName);

	addButton = (JButton) form.getFormBody().getComponentByName(
		tableName + "_add_button");
	editButton = (JButton) form.getFormBody().getComponentByName(
		tableName + "_edit_button");
	deleteButton = (JButton) form.getFormBody().getComponentByName(
		tableName + "_delete_button");
    }

    public GIAAlphanumericTableHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases, AbstractForm form,
	    Class<? extends AbstractSubForm> subFormClass) {
	super(tableName, widgets, foreignKeyId, colNames, colAliases);

	FormFactory.checkAndLoadTableRegistered(tableName);

	addButton = (JButton) form.getFormBody().getComponentByName(
		tableName + "_add_button");
	editButton = (JButton) form.getFormBody().getComponentByName(
		tableName + "_edit_button");
	deleteButton = (JButton) form.getFormBody().getComponentByName(
		tableName + "_delete_button");

	try {
	    subform = subFormClass.newInstance();
	} catch (InstantiationException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (IllegalAccessException e) {
	    logger.error(e.getStackTrace(), e);
	}
    }

    // TODO
    @Override
    protected void createTableModel() throws ReadDriverException {
	AlphanumericTableModel model = TableModelFactory
		.createFromTableWithFilter(sourceTableName, destinationKey,
			originKeyValue, colNames, colAliases);
	jtable.setModel(model);
	if (subform != null) {
	    subform.setModel(model);
	}
	// jtable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    }

    private void autoFit() {

	// TODO never less that the header text
	int avaliable = jtable.getColumnModel().getTotalColumnWidth();

	int[] maxLengths = model.getMaxLengths();
	double needed = 0.0;
	for (int l : maxLengths) {
	    needed += l;
	}

	for (int i = 0; i < model.getColumnCount(); i++) {
	    double preferredWidth = avaliable * (maxLengths[i] / needed);

	    jtable.getColumnModel().getColumn(i)
		    .setPreferredWidth((int) preferredWidth);
	}
    }

    // TODO
    @Override
    public void fillValues(String foreignKeyValue) {
	super.fillValues(foreignKeyValue);
	Map<String, String> foreignKey = new HashMap<String, String>(1);
	foreignKey.put(destinationKey, originKeyValue);
	if (subform != null) {
	    subform.setForeingKey(foreignKey);
	}
    }

    // TODO
    @Override
    protected void createTableListener() {
	listener = new AlphanumericCompleteJTableContextualMenu(subform);
    }

    @Override
    public AlphanumericCompleteJTableContextualMenu getListener() {
	return (AlphanumericCompleteJTableContextualMenu) listener;
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
