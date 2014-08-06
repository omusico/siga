package es.icarto.gvsig.extgex.queries;

import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.commons.gui.OnlyOneColumnEditable;
import es.icarto.gvsig.commons.queries.QueriesWidget;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class QueriesWidgetTable implements QueriesWidget {

    private static final Logger logger = Logger
	    .getLogger(QueriesWidgetTable.class);
    private final JTable queriesTable;

    public QueriesWidgetTable(FormPanel formBody, String name) {
	queriesTable = (JTable) formBody.getComponentByName(name);
	initQueriesTable();
	fillQueriesTable();
    }

    @Override
    public String getQueryId() {
	DefaultTableModel model = (DefaultTableModel) queriesTable.getModel();
	int i = queriesTable.getSelectedRow();
	return (String) model.getValueAt(i, 0);
    }

    private void initQueriesTable() {
	DefaultTableModel model = new OnlyOneColumnEditable(0);
	queriesTable.setModel(model);
	String[] columnNames = { "Código", "Descripción" };

	model.setRowCount(0);
	queriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	queriesTable.setRowSelectionAllowed(true);
	queriesTable.setColumnSelectionAllowed(false);

	TableColumn column01 = new TableColumn();
	model.addColumn(column01);

	TableColumn column02 = new TableColumn();
	model.addColumn(column02);

	DefaultTableCellRenderer columnCentered = new DefaultTableCellRenderer();
	columnCentered.setHorizontalAlignment(SwingConstants.CENTER);
	queriesTable.getColumnModel().getColumn(0)
		.setCellRenderer(columnCentered);

	queriesTable.getColumnModel().getColumn(0)
		.setHeaderValue(columnNames[0]);
	queriesTable.getColumnModel().getColumn(0).setMinWidth(100);
	queriesTable.getColumnModel().getColumn(0).setMaxWidth(110);
	queriesTable.getColumnModel().getColumn(1)
		.setHeaderValue(columnNames[1]);
	queriesTable.getColumnModel().getColumn(1).setMaxWidth(500);
    }

    private void fillQueriesTable() {
	DefaultTableModel model = (DefaultTableModel) queriesTable.getModel();
	model.setRowCount(0);
	DBSession dbs = DBSession.getCurrentSession();

	try {
	    String[] orderBy = new String[1];
	    orderBy[0] = DBNames.FIELD_CODIGO_QUERIES;
	    String[][] tableContent = dbs.getTable(DBNames.TABLE_QUERIES,
		    DBNames.SCHEMA_QUERIES, orderBy, false);

	    for (int i = 0; i < tableContent.length; i++) {
		Object[] row = new Object[5];
		// Table Schema: 0-codigo, 1-consulta(SQL), 2-descripcion
		row[0] = tableContent[i][DBNames.INDEX_CODIGO_QUERIES];
		row[1] = tableContent[i][DBNames.INDEX_DESCRIPCION_QUERIES];
		model.addRow(row);
		model.fireTableRowsInserted(0, model.getRowCount() - 1);
	    }
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    @Override
    public boolean isQueryIdSelected(String id) {
	throw new AssertionError("Not implemented");
    }
}
