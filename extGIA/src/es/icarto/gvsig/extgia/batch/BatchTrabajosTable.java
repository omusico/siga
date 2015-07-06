package es.icarto.gvsig.extgia.batch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	JComboBox unidadComboBox = new JComboBox();
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
	    if (DBFieldNames.trabajosVegetacionTableEditableCells[i].equals(column.getHeaderValue().toString())) {
		return true;
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
