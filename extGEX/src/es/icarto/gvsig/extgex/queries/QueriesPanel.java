package es.icarto.gvsig.extgex.queries;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.commons.gui.AbstractIWindow;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class QueriesPanel extends AbstractIWindow implements
	TableModelListener, ActionListener {

    private static final String DEFAULT_FILTER = "--TODOS--";

    private FormPanel formBody;

    private final String ID_RUNQUERIES = "runQueriesButton";
    private JButton runQueriesB;

    private final String ID_QUERIESTABLE = "queriesTable";
    private JTable queriesTable;

    private final String ID_TRAMOCB = "tramo";
    private JComboBox tramo;

    private final String ID_UCCB = "uc";
    private JComboBox uc;

    private final String ID_AYUNTAMIENTOCB = "ayuntamiento";
    private JComboBox ayuntamiento;

    private final String ID_PARROQUIA_SUBTRAMOCB = "parroquia_subtramo";
    private JComboBox parroquia_subtramo;

    private static final Logger logger = Logger.getLogger(QueriesPanel.class);

    DBSession dbs;
    String[][] tramos;

    // Filters
    String tramoSelected = DEFAULT_FILTER;
    String ucSelected = DEFAULT_FILTER;
    String ayuntamientoSelected = DEFAULT_FILTER;
    String parroquiaSelected = null;

    public QueriesPanel() {
	super();
	setWindowTitle("Consultas");
	setWindowInfoProperties(WindowInfo.MODALDIALOG);

	try {
	    formBody = new FormPanel(getClass().getClassLoader()
		    .getResourceAsStream("consultas.xml"));
	    this.add(formBody);
	} catch (FormException e) {
	    logger.error(e.getStackTrace(), e);
	}

	dbs = DBSession.getCurrentSession();
	initWidgets();
	initListeners();
    }

    public void initWidgets() {
	ImageComponent image = (ImageComponent) formBody
		.getComponentByName("image");
	ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);
	runQueriesB = (JButton) formBody.getComponentByName(ID_RUNQUERIES);
	runQueriesB.addActionListener(this);
	initFilterWidgets();
	initQueriesTable();
	fillQueriesTable();
    }

    private void initFilterWidgets() {
	tramo = (JComboBox) formBody.getComponentByName(ID_TRAMOCB);
	tramo.addItem(new String(DEFAULT_FILTER));
	uc = (JComboBox) formBody.getComponentByName(ID_UCCB);
	uc.addItem(new String(DEFAULT_FILTER));
	ayuntamiento = (JComboBox) formBody
		.getComponentByName(ID_AYUNTAMIENTOCB);
	ayuntamiento.addItem(new String(DEFAULT_FILTER));
	parroquia_subtramo = (JComboBox) formBody
		.getComponentByName(ID_PARROQUIA_SUBTRAMOCB);
	parroquia_subtramo.setEnabled(false);

	try {
	    String[] order = new String[1];
	    order[0] = DBNames.FIELD_IDTRAMO;
	    String[][] tramos = dbs.getTable(DBNames.TABLE_TRAMOS,
		    DBNames.SCHEMA_DATA, order, false);
	    for (int i = 0; i < tramos.length; i++) {
		tramo.addItem(tramos[i][1]);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private void initQueriesTable() {
	queriesTable = (JTable) formBody.getComponentByName(ID_QUERIESTABLE);

	DefaultTableModel model = new QueriesTableModel();
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

    private void initListeners() {
	tramo.addActionListener(new TramoListener());
	uc.addActionListener(new UCListener());
	ayuntamiento.addActionListener(new AyuntamientoListener());
	parroquia_subtramo.addActionListener(new ParroquiaListener());
    }

    private final class TramoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    tramoSelected = (String) tramo.getSelectedItem();
	    updateUCDependingOnTramo();
	}
    }

    private final class UCListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    ucSelected = (String) uc.getSelectedItem();
	    if (ucSelected != null) {
		updateAyuntamientoDependingOnUC();
	    }
	}
    }

    private final class AyuntamientoListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    ayuntamientoSelected = (String) ayuntamiento.getSelectedItem();
	    if (ayuntamientoSelected != null) {
		updateParroquiaDependingOnAyuntamientoAndUC();
	    }
	}
    }

    private final class ParroquiaListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (!parroquia_subtramo.isEnabled()) {
		parroquiaSelected = null;
	    } else {
		parroquiaSelected = (String) parroquia_subtramo
			.getSelectedItem();
	    }
	}
    }

    private String getTramoId() throws SQLException {
	String whereSQL = DBNames.FIELD_NOMBRETRAMO_TRAMOS + " = " + "'"
		+ tramoSelected + "'";
	String tramoId = getIDFromCB(DBNames.FIELD_IDTRAMO,
		DBNames.TABLE_TRAMOS, whereSQL);
	return tramoId;
    }

    private String getUcId() throws SQLException {
	String whereSQL = DBNames.FIELD_NOMBREUC_UC + " = " + "'" + ucSelected
		+ "'";
	String uc = getIDFromCB(DBNames.FIELD_IDUC, DBNames.TABLE_UC, whereSQL);
	return uc;
    }

    private String getAyuntamientoId() throws SQLException {
	String whereSQL = DBNames.FIELD_IDUC + " = " + "'" + getUcId() + "'"
		+ " and " + DBNames.FIELD_NOMBREAYUNTAMIENTO_AYUNTAMIENTO
		+ " = " + "'" + ayuntamientoSelected + "'";
	String ayuntamiento = getIDFromCB(DBNames.FIELD_IDAYUNTAMIENTO,
		DBNames.TABLE_AYUNTAMIENTOS, whereSQL);
	return ayuntamiento;
    }

    private String getIDFromCB(String fieldID, String tablename,
	    String whereClause) throws SQLException {
	Connection con = dbs.getJavaConnection();
	String query = "SELECT " + fieldID + " FROM " + DBNames.SCHEMA_DATA
		+ "." + tablename + " WHERE " + whereClause;
	Statement st = con.createStatement();
	ResultSet resultSet = st.executeQuery(query);
	resultSet.first();
	return resultSet.getString(1);
    }

    private void updateUCDependingOnTramo() {

	if (tramoSelected.compareToIgnoreCase(DEFAULT_FILTER) == 0) {
	    uc.removeAllItems();
	    uc.addItem(new String(DEFAULT_FILTER));
	} else {
	    try {
		String[] order = new String[1];
		order[0] = DBNames.FIELD_IDUC;
		String tramoSelectedId = getTramoId();
		String whereClause = DBNames.FIELD_IDTRAMO + " = " + "'"
			+ tramoSelectedId + "'";
		String[][] ucs = dbs.getTable(DBNames.TABLE_UC,
			DBNames.SCHEMA_DATA, whereClause, order, false);
		uc.removeAllItems();
		uc.addItem(new String(DEFAULT_FILTER));
		for (int i = 0; i < ucs.length; i++) {
		    uc.addItem(ucs[i][2]);
		}
	    } catch (SQLException e) {
		logger.error(e.getMessage(), e);
	    }
	}
    }

    private void updateAyuntamientoDependingOnUC() {

	if (ucSelected.compareToIgnoreCase(DEFAULT_FILTER) == 0) {
	    ayuntamiento.removeAllItems();
	    ayuntamiento.addItem(new String(DEFAULT_FILTER));
	} else {
	    try {
		String[] order = new String[1];
		order[0] = DBNames.FIELD_IDAYUNTAMIENTO;
		String ucSelectedId = getUcId();
		String whereClause = DBNames.FIELD_IDUC + " = " + "'"
			+ ucSelectedId + "'";
		String[][] ayuntamientos = dbs.getTable(
			DBNames.TABLE_AYUNTAMIENTOS, DBNames.SCHEMA_DATA,
			whereClause, order, false);
		ayuntamiento.removeAllItems();
		ayuntamiento.addItem(new String(DEFAULT_FILTER));
		for (int i = 0; i < ayuntamientos.length; i++) {
		    ayuntamiento.addItem(ayuntamientos[i][2]);
		}
	    } catch (SQLException e) {
		logger.error(e.getMessage(), e);
	    }
	}
    }

    private void updateParroquiaDependingOnAyuntamientoAndUC() {

	if (ayuntamientoSelected.compareToIgnoreCase(DEFAULT_FILTER) == 0) {
	    parroquia_subtramo.removeAllItems();
	    parroquia_subtramo.setEnabled(false);
	} else {
	    try {
		String[] order = new String[1];
		order[0] = DBNames.FIELD_IDPARROQUIA;
		String ucSelectedId = getUcId();
		String ayuntamientoSelectedId = getAyuntamientoId();
		String whereClause = DBNames.FIELD_IDAYUNTAMIENTO + " = " + "'"
			+ ayuntamientoSelectedId + "'" + " and "
			+ DBNames.FIELD_IDUC + " = " + "'" + ucSelectedId + "'";
		String[][] parroquias = dbs.getTable(
			DBNames.TABLE_PARROQUIASSUBTRAMOS, DBNames.SCHEMA_DATA,
			whereClause, order, false);
		if (parroquias.length <= 0) {
		    parroquia_subtramo.setEnabled(false);
		} else {
		    parroquia_subtramo.setEnabled(true);
		    parroquia_subtramo.removeAllItems();
		    parroquia_subtramo.addItem(new String(DEFAULT_FILTER));
		    for (int i = 0; i < parroquias.length; i++) {
			parroquia_subtramo.addItem(parroquias[i][3]);
		    }
		}
	    } catch (SQLException e) {
		logger.error(e.getMessage(), e);
	    }
	}
    }

    private void fillQueriesTable() {
	DefaultTableModel model = (DefaultTableModel) queriesTable.getModel();
	model.setRowCount(0);

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
    public void tableChanged(TableModelEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == runQueriesB) {
	    executeValidations();
	    return;
	}
    }

    private void executeValidations() {
	QueriesTask qt = new QueriesTask();
	ProgressBarDialog progressBarDialog = new ProgressBarDialog(qt);
	progressBarDialog.open();

    }

    private class QueriesTask extends SwingWorker<String, Void> {

	private final boolean sqlError = false;
	private final String error = "";
	private ArrayList<ResultTableModel> resultsMap;

	@Override
	protected String doInBackground() throws Exception {
	    DBSession dbs = DBSession.getCurrentSession();

	    DefaultTableModel model = (DefaultTableModel) queriesTable
		    .getModel();

	    setProgress(0);

	    resultsMap = new ArrayList<ResultTableModel>();
	    Connection con = null;
	    try {

		int i = queriesTable.getSelectedRow();

		String queryCode = (String) model.getValueAt(i, 0);

		String[] queryContents = doQuery(queryCode);

		String queryDescription = queryContents[1];
		String queryTitle = queryContents[2];
		String querySubtitle = queryContents[3];

		setProgress(50);

		con = dbs.getJavaConnection();
		PreparedStatement statement = con
			.prepareStatement(queryContents[0]);
		statement.execute();
		ResultSet rs = statement.getResultSet();

		setProgress(75);

		ResultTableModel result = new ResultTableModel(queryCode,
			queryDescription, queryTitle, querySubtitle,
			getFilters());
		resultSetToTable(result, rs);
		resultsMap.add(result);

	    } catch (Exception ex) {
		logger.error(ex.getMessage());
		con.close();
	    }
	    setProgress(99);
	    String html = showResultsAsHTML(resultsMap);
	    return html;
	}

	private String[] getFilters() {
	    String[] filters = new String[4];
	    filters[0] = tramoSelected;
	    filters[1] = ucSelected;
	    filters[2] = ayuntamientoSelected;
	    filters[3] = parroquiaSelected;
	    return filters;
	}

	private String getWhereClause(boolean hasWhere) throws SQLException {
	    String whereC;
	    if (!hasWhere) {
		whereC = "WHERE";
	    } else {
		whereC = " AND ";
	    }
	    if (tramoSelected.compareToIgnoreCase(DEFAULT_FILTER) != 0) {
		whereC = whereC + " " + DBNames.FIELD_TRAMO_FINCAS + " = "
			+ "'" + getTramoId() + "'";
	    }
	    if (ucSelected.compareToIgnoreCase(DEFAULT_FILTER) != 0) {
		whereC = whereC + " AND " + DBNames.FIELD_UC_FINCAS + " = "
			+ "'" + getUcId() + "'";
	    }
	    if (ayuntamientoSelected.compareToIgnoreCase(DEFAULT_FILTER) != 0) {
		whereC = whereC + " AND " + DBNames.FIELD_AYUNTAMIENTO_FINCAS
			+ " = " + "'" + getAyuntamientoId() + "'";
	    }
	    if (whereC.equalsIgnoreCase("WHERE")) {
		whereC = ""; // has no combobox selected
	    }
	    if (whereC.equalsIgnoreCase(" AND ")) {
		whereC = "AND 1=1";
	    }
	    return whereC;
	}

	private String[] doQuery(String queryCode) throws Exception {
	    DBSession dbs = DBSession.getCurrentSession();

	    String whereClause = DBNames.FIELD_CODIGO_QUERIES + " = '"
		    + queryCode + "'";
	    String[][] tableContent = dbs.getTable(DBNames.TABLE_QUERIES,
		    DBNames.SCHEMA_QUERIES, whereClause);

	    String[] contents = new String[4];
	    String query;
	    query = tableContent[0][1];
	    boolean hasWhere = false;
	    if (tableContent[0][5].compareToIgnoreCase("SI") == 0) {
		hasWhere = true;
	    }
	    // query
	    contents[0] = query.replaceAll("\\[\\[WHERE\\]\\]",
		    getWhereClause(hasWhere));
	    // description
	    contents[1] = tableContent[0][2];
	    // title
	    contents[2] = tableContent[0][3];
	    // subtitle
	    contents[3] = tableContent[0][4];
	    return contents;
	}

	@Override
	public void done() {
	    if (!isCancelled() && !sqlError) {
		try {
		    String str = get();
		    QueriesResultPanel resultPanel;
		    // if (councilCB.getSelectedIndex() > 0) {
		    // resultPanel = new EIELValidationResultPanel(councilCB
		    // .getSelectedItem().toString());
		    // } else {
		    resultPanel = new QueriesResultPanel();
		    // }
		    resultPanel.open();
		    resultPanel.setResult(str);
		    resultPanel.setResultMap(resultsMap);
		    resultPanel.setFilters(getFilters());
		    PluginServices.getMDIManager().restoreCursor();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		} catch (ExecutionException e) {
		    e.printStackTrace();
		}
	    } else if (sqlError) {
		String message = error + "\n"
			+ PluginServices.getText(this, "checkSchema");
		JOptionPane.showMessageDialog(null, message,
			PluginServices.getText(this, "validationError"),
			JOptionPane.ERROR_MESSAGE);
	    }
	}

	private String showResultsAsHTML(ArrayList<ResultTableModel> resultMap) {
	    StringBuffer sf = new StringBuffer();

	    for (ResultTableModel result : resultMap) {
		sf.append("<h3 style=\"color: blue\">" + result.getCode()
			+ "  -  " + result.getDescription() + "</h3>");

		sf.append("<p>" + result.getQueryTables() + "</p>");
		sf.append(result.getHTML());
	    }
	    sf.append("</h2>");
	    sf.append("<hr>");

	    return sf.toString();
	}

	private void resultSetToTable(ResultTableModel result, ResultSet rs)
		throws SQLException {
	    // TODO: don't create empty ResultTableModel
	    ResultSetMetaData metaData = rs.getMetaData();
	    int numColumns = metaData.getColumnCount();

	    for (int i = 0; i < numColumns; i++) {
		result.addColumn(metaData.getColumnLabel(i + 1));
	    }

	    // Getting values of the rows that have failed
	    // int oldErrors = errorsFound;

	    while (rs.next()) {
		// errorsFound++;
		Object rowData[] = new Object[numColumns];
		for (int i = 0; i < numColumns; i++) {
		    rowData[i] = rs.getObject(i + 1);
		}
		result.addRow(rowData);
	    }

	}

    }// QueriesTask Class

}
