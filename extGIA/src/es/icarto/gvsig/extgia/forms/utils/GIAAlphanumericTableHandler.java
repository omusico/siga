package es.icarto.gvsig.extgia.forms.utils;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;
import es.icarto.gvsig.navtableforms.gui.tables.menu.AlphanumericCompleteJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

public class GIAAlphanumericTableHandler extends BaseTableHandler {

    private static final Logger logger = Logger
	    .getLogger(GIAAlphanumericTableHandler.class);

    protected AbstractSubForm form;

    public GIAAlphanumericTableHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases,
	    Class<? extends AbstractSubForm> subFormClass) {
	super(tableName, widgets, foreignKeyId, colNames, colAliases);

	// TODO
	GIAFormFactory.registerFormFactory();
	FormFactory.checkAndLoadTableRegistered(tableName);

	try {
	    form = subFormClass.newInstance();
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
	if (form != null) {
	    form.setModel(model);
	}
    }

    // TODO
    @Override
    public void fillValues(String foreignKeyValue) {
	super.fillValues(foreignKeyValue);
	Map<String, String> foreignKey = new HashMap<String, String>(1);
	foreignKey.put(destinationKey, originKeyValue);
	if (form != null) {
	    form.setForeingKey(foreignKey);
	}
    }

    // TODO
    @Override
    protected void createTableListener() {
	listener = new AlphanumericCompleteJTableContextualMenu(form);
    }

    @Override
    public AlphanumericCompleteJTableContextualMenu getListener() {
	return (AlphanumericCompleteJTableContextualMenu) listener;
    }

}
