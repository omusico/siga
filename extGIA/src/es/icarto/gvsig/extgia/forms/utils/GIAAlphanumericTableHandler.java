package es.icarto.gvsig.extgia.forms.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;
import es.icarto.gvsig.navtableforms.gui.tables.menu.AlphanumericCompleteJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.utils.FormFactory;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

public class GIAAlphanumericTableHandler extends BaseTableHandler {

    private static final Logger logger = Logger
	    .getLogger(GIAAlphanumericTableHandler.class);

    private AbstractSubForm subform;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;

    private final int[] coldWidths;

    public GIAAlphanumericTableHandler(String tableName,
	    Map<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases, int[] colWidths,
	    AbstractForm form) {

	super(tableName, widgets, foreignKeyId, colNames, colAliases);
	this.coldWidths = colWidths;

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
	    Map<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases, int[] colWidths,
	    AbstractForm form, Class<? extends AbstractSubForm> subFormClass) {
	super(tableName, widgets, foreignKeyId, colNames, colAliases);

	FormFactory.checkAndLoadTableRegistered(tableName);
	this.coldWidths = colWidths;
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
	AlphanumericTableModel model = null;
	// if (sourceTableName.equals("senhalizacion_vertical_senhales")) {
	// TOCTableManager toc = new TOCTableManager();
	// IEditableSource orgModel = toc.getTableModelByName(sourceTableName);
	// int fieldIndex = orgModel.getRecordset().getFieldIndexByName(
	// destinationKey);
	// IRowFilter filter = new IRowFilterQuick(fieldIndex, originKeyValue);
	// model = new AlphanumericTableModel(orgModel, colNames, colAliases,
	// filter);
	// } else {
	model = TableModelFactory.createFromTableWithFilter(sourceTableName,
		destinationKey, originKeyValue, colNames, colAliases);
	// }

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
	if (coldWidths != null) {
	    TableColumnModel columnModel = jtable.getColumnModel();
	    for (int i = 0; i < coldWidths.length; i++) {
		columnModel.getColumn(i).setPreferredWidth(coldWidths[i]);
	    }
	}
	TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
		jtable.getModel());
	sorter.setSortsOnUpdates(true);
	for (int i = 0; i < colNames.length; i++) {
	    if (colNames[i].startsWith("fecha")) {
		sorter.setComparator(i, new Comparator<String>() {

		    @Override
		    public int compare(String o1, String o2) {
			Date d1 = DateFormatNT.convertStringToDate(o1);
			Date d2 = DateFormatNT.convertStringToDate(o2);
			if (d1 == null) {
			    return 1;
			}
			if (d2 == null) {
			    return -1;
			}
			return d1.compareTo(d2);
		    }
		});
		javax.swing.RowSorter.SortKey sk = new RowSorter.SortKey(i,
			SortOrder.ASCENDING);
		sorter.setSortKeys(Arrays
			.asList(new RowSorter.SortKey[] { sk }));
		break;
	    }
	}
	jtable.setRowSorter(sorter);
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
