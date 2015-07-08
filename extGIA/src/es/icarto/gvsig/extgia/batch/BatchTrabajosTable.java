package es.icarto.gvsig.extgia.batch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class BatchTrabajosTable extends JPanel implements IWindow {

    private WindowInfo viewInfo;
    private JTable table;
    private final String dbTableName;
    private final String[] columnNames;
    private final String[] columnDbNames;
    private final Integer[] columnDbTypes;
    private final String[][] data;
    private final ORMLite batchOrmLite;

    private JButton cancelButton;
    private JButton saveButton;

    private JComboBox unidadComboBox;

    public BatchTrabajosTable(ORMLite batchOrmLite, String dbTableName, String[][] data,
	    final String[] columnNames, final String[] columnDbNames, final Integer[] columnsDbTypes) {
	super();
	this.dbTableName = dbTableName;
	this.columnNames = columnNames;
	this.columnDbNames = columnDbNames;
	this.columnDbTypes = columnsDbTypes;
	this.batchOrmLite = batchOrmLite;
	this.data = data;
	initTable();
    }

    private void initTable() {
	this.setLayout(new BorderLayout());
	table = new JTable(new BatchTrabajosTableModel());
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	JScrollPane scrollPane = new JScrollPane(table);

	cancelButton = new JButton("Cancelar");
	cancelButton.addActionListener(new ButtonsHandler());
	saveButton = new JButton("Guardar");
	saveButton.addActionListener(new ButtonsHandler());

	JPanel buttonPane = new JPanel();
	buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	buttonPane.add(Box.createHorizontalGlue());
	buttonPane.add(cancelButton);
	buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	buttonPane.add(saveButton);

	this.add(scrollPane, BorderLayout.CENTER);
	this.add(buttonPane, BorderLayout.SOUTH);

	setUnidadCellEditorAsComboBox();
	setFechaCellEditorAsJDateChooser();
	updateMedicion();
    }

    @Override
    public WindowInfo getWindowInfo() {
	if (viewInfo==null) {
	    viewInfo = new WindowInfo(WindowInfo.ICONIFIABLE |
		    WindowInfo.MAXIMIZABLE | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    viewInfo.setTitle(PluginServices.getText(this, "Añadir Trabajos"));
	    viewInfo.setWidth(640);
	    viewInfo.setHeight(480);
	}
	return viewInfo;
    }

    @Override
    public Object getWindowProfile() {
	return WindowInfo.EDITOR_PROFILE;
    }

    private void closeWindow() {
	PluginServices.getMDIManager().closeWindow(this);
    }

    public class ButtonsHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    if (e.getSource() == saveButton) {
		save();
	    }

	    if (e.getSource() == cancelButton) {
		closeWindow();
	    }
	}

	private void save() {
	    try {
		for (int i = 0; i < data.length; i++) {
		    Value[] values = new Value[data[i].length];
		    for (int j = 0; j < data[i].length; j++) {
			if (data[i][j] == null) {
			    values[j] = ValueFactory.createNullValue();
			} else if (!data[i][j].isEmpty()) {
			    values[j] = createValueByType(data[i][j], columnDbTypes[j]);
			}else {
			    values[j] = ValueFactory.createNullValue();
			}
		    }
		    DBSession.getCurrentSession().insertRow(DBFieldNames.GIA_SCHEMA,
			    dbTableName, columnDbNames, values);
		}
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    } catch (ParseException e2) {
		e2.printStackTrace();
	    }
	    JOptionPane.showMessageDialog(
		    null,
		    PluginServices.getText(this, "addedInfo_msg_I")
		    + data.length
		    + " "
		    + PluginServices.getText(this,
			    "addedInfo_msg_II"));
	    closeWindow();
	}
    }

    private Value createValueByType(String value,int type) throws ParseException {
	if (type == Types.NUMERIC || type == Types.DOUBLE) {
	    if (value.contains(",")) {
		value = value.replace(",", ".");
	    }
	}
	return ValueFactory.createValueByType(value, type);
    }

    public HashMap<String, String> getTableDataRow(int row, int columns) {
	HashMap<String, String> rowValues = new HashMap<String, String>();
	for (int i = 0; i < columns; i++) {
	    rowValues.put(columnNames[i], table.getValueAt(row, i).toString());
	}
	return rowValues;
    }

    public void setUnidadCellEditorAsComboBox() {
	DomainValues unidadValues = batchOrmLite.getAppDomain().getDomainValuesForComponent(
		DBFieldNames.UNIDAD);
	unidadComboBox = new JComboBox();
	unidadComboBox.addActionListener(new BatchUnidadListener());
	for (KeyValue value : unidadValues.getValues()) {
	    unidadComboBox.addItem(value.toString());
	}
	Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
	while (columns.hasMoreElements()) {
	    TableColumn unidadColumn = columns.nextElement();
	    if (unidadColumn.getHeaderValue().toString().equalsIgnoreCase(DBFieldNames.UNIDAD)) {
		unidadColumn.setCellEditor(new DefaultCellEditor(unidadComboBox));
		break;
	    }
	}
    }

    private void updateMedicion() {
	int unidadColumn = table.getColumn("Unidad").getModelIndex();
	int medicionColumn = table.getColumn("Medición").getModelIndex();
	int medicionElementoColumn = table.getColumn("Medición elemento").getModelIndex();
	int medicionLastJobColumn = table.getColumn("Medición último trabajo").getModelIndex();

	String medicionValue = null;

	for (int i = 0; i < data.length; i++) {
	    if (table.getValueAt(i, medicionLastJobColumn) != null) {
		medicionValue = data[i][medicionLastJobColumn];
	    }else if (table.getValueAt(i, medicionElementoColumn) != null) {
		medicionValue = data[i][medicionElementoColumn];
	    }
	    table.setValueAt(medicionValue.toString(), i, medicionColumn);

	    if (data[i][unidadColumn].toString().equals("Herbicida")) {
		updateMedicionHerbicida(i, medicionColumn);
	    }
	    table.repaint();
	}
    }

    private void updateMedicionHerbicida(int row, int column) {
	int longitudColumn = table.getColumn("Longitud").getModelIndex();
	int anchoColumn = table.getColumn("Ancho").getModelIndex();

	Object longitudValue = table.getValueAt(row, longitudColumn);
	Object anchoValue = table.getValueAt(row, anchoColumn);
	if (!longitudValue.toString().isEmpty() && !anchoValue.toString().isEmpty()) {
	    Double medicionHerbicida = Double.parseDouble(longitudValue.toString()) *
		    Double.parseDouble(anchoValue.toString().replace(",", "."));
	    table.setValueAt(medicionHerbicida.toString(), row, column);
	}
    }

    private class BatchUnidadListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    int medicionColumn = table.getColumn("Medición").getModelIndex();
	    int medicionLastJobColumn = table.getColumn("Medición último trabajo").getModelIndex();

	    JComboBox unidadComboBox = (JComboBox)e.getSource();
	    int row = table.getSelectedRow();
	    if (unidadComboBox.getSelectedItem().toString().equals("Herbicida")) {
		updateMedicionHerbicida(row, medicionColumn);
		if (row != -1) {
		    table.setValueAt(getMedicionLastJobValueByUnit(row), row, medicionLastJobColumn);
		    //table.setValueAt(null, row, medicionColumn);
		}
		//table.repaint();
	    } else {
		updateMedicion();
		if (row != -1 && getMedicionLastJobValueByUnit(row) != null) {
		    table.setValueAt(getMedicionLastJobValueByUnit(row), row, medicionLastJobColumn);
		    table.setValueAt(getMedicionLastJobValueByUnit(row), row, medicionColumn);
		}
		//table.repaint();
	    }
	    table.repaint();
	}

	//TODO: This method is copied from CalculateDBForeignValueLastJob class
	private String getMedicionLastJobValueByUnit(int row) {
	    try {
		Connection connection = DBSession.getCurrentSession().getJavaConnection();
		PreparedStatement statement = connection.prepareStatement(getSQLSentence(row));
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
		    if (rs.getString(1) != null) {
			if (rs.getString(1).contains(".")) {
			    return rs.getString(1).replace(".", ",");
			} else {
			    return rs.getString(1);
			}
		    }
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    return null;
	}


	//TODO: This method is copied from CalculateDBForeignValueLastJob class
	protected String getSQLSentence(int row) {
	    if (unidadComboBox.getSelectedItem().toString().equalsIgnoreCase(" ") || !getLastJobByUnit(row)) {
		return "SELECT " + DBFieldNames.MEDICION + " FROM " + DBFieldNames.GIA_SCHEMA + "."
			+ dbTableName + " WHERE " + columnDbNames[0] + " = " + "'" + data[row][0]
				+ "'" +" AND " + DBFieldNames.FECHA + " IN (SELECT max(" + DBFieldNames.FECHA + ") FROM "
				+ DBFieldNames.GIA_SCHEMA + "." + dbTableName + " WHERE " + columnDbNames[0] + " = "
				+ "'" + data[row][0] + "')";
	    } else {
		return "SELECT " + DBFieldNames.MEDICION + " FROM " + DBFieldNames.GIA_SCHEMA + "."
			+ dbTableName + " WHERE " + columnDbNames[0] + " = " + "'" + data[row][0]
				+ "'" +" AND " + DBFieldNames.FECHA + " IN (SELECT max(" + DBFieldNames.FECHA + ") FROM "
				+ DBFieldNames.GIA_SCHEMA + "." + dbTableName + " WHERE " + columnDbNames[0] + " = "
				+ "'" + data[row][0] + "' AND unidad = '" + unidadComboBox.getSelectedItem().toString() + "')";
	    }
	}

	//TODO: This method is copied from CalculateDBForeignValueLastJob class
	protected Boolean getLastJobByUnit(int row) {
	    if (!unidadComboBox.getSelectedItem().toString().equalsIgnoreCase(" ")) {
		try {
		    Connection connection = DBSession.getCurrentSession().getJavaConnection();
		    PreparedStatement statement;
		    String query = "SELECT " + DBFieldNames.MEDICION + " FROM " + DBFieldNames.GIA_SCHEMA + "."
			    + dbTableName + " WHERE " + columnDbNames[0] + " = " + "'" + data[row][0]
				    + "'" +" AND " + DBFieldNames.UNIDAD + " = '" + unidadComboBox.getSelectedItem().toString() + "'";
		    statement = connection.prepareStatement(query);
		    ResultSet rs = statement.executeQuery();
		    return rs.next();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	    return null;
	}

    }

    private void setFechaCellEditorAsJDateChooser() {
	Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
	while (columns.hasMoreElements()) {
	    TableColumn unidadColumn = columns.nextElement();
	    if (unidadColumn.getHeaderValue().toString().equalsIgnoreCase(DBFieldNames.FECHA)) {
		unidadColumn.setCellEditor(new DateCellEditor());
		break;
	    }
	}
    }

    private boolean isColumnEditable(TableColumn column) {
	for (int i = 0; i < DBFieldNames.trabajosVegetacionTableEditableCells.length; i++) {
	    if (DBFieldNames.trabajosVegetacionTableEditableCells[i].equals(column.getHeaderValue().
		    toString())) {
		return true;
	    }
	    if (unidadComboBox.getSelectedItem().toString().equals("Herbicida")) {
		if (column.getHeaderValue().toString().equals("Longitud") ||
			column.getHeaderValue().toString().equals("Ancho")) {
		    return true;
		}
	    }
	}
	return false;
    }

    public class DateCellEditor extends AbstractCellEditor implements TableCellEditor {
	JDateChooser dateChooser = new JDateChooser();
	SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();

	@Override
	public Component getTableCellEditorComponent (JTable table, Object value,
		boolean isSelected, int row, int column) {
	    dateChooser.setDateFormatString(dateFormat.toPattern());
	    dateChooser.getDateEditor().setEnabled(false);
	    dateChooser.getDateEditor().getUiComponent().setBackground(new Color(255, 255, 255));
	    dateChooser.getDateEditor().getUiComponent().setFont(new Font("Arial", Font.PLAIN, 11));
	    dateChooser.getDateEditor().getUiComponent().setToolTipText(null);
	    if (!value.toString().isEmpty()) {
		try {
		    Date date = dateFormat.parse(value.toString());
		    dateChooser.setDate(date);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
	    }
	    return dateChooser;
	}

	@Override
	public Object getCellEditorValue () {
	    return dateFormat.format(dateChooser.getDate());
	}
    }

    class BatchTrabajosTableModel extends AbstractTableModel {

	@Override
	public int getColumnCount() {
	    return columnNames.length;
	}

	@Override
	public int getRowCount() {
	    return data.length;
	}

	@Override
	public String getColumnName(int col) {
	    return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
	    return data[row][col];
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
	    data[row][col] = (String) value;
	    fireTableCellUpdated(row, col);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
	    TableColumn column = table.getColumnModel().getColumn(columnIndex);
	    return isColumnEditable(column);
	}
    }
}
