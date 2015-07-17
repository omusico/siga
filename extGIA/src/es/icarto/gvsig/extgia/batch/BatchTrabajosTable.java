package es.icarto.gvsig.extgia.batch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.icarto.gvsig.navtableforms.utils.TOCTableManager;
import es.udc.cartolab.gvsig.navtable.dataacces.TableController;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

@SuppressWarnings("serial")
public class BatchTrabajosTable extends JPanel implements IWindow {

    private WindowInfo viewInfo;
    private JTable table;
    private final String dbTableName;
    private final String[] columnNames;
    private final String[] columnDbNames;
    private final Integer[] columnDbTypes;
    private final String[][] data;
    private final ORMLite ormLite;

    private JButton cancelButton;
    private JButton saveButton;

    private BatchTrabajosCalculation foo;
    private final BaseTableHandler trabajosTableHandler;

    private static final Logger logger = Logger
	    .getLogger(BatchTrabajosTable.class);

    public BatchTrabajosTable(ORMLite ormLite, String dbTableName,
	    String[][] data, final String[] columnNames,
	    final String[] columnDbNames, final Integer[] columnsDbTypes,
	    BaseTableHandler trabajosTableHandler) {
	super();
	this.dbTableName = dbTableName;
	this.columnNames = columnNames;
	this.columnDbNames = columnDbNames;
	this.columnDbTypes = columnsDbTypes;
	this.ormLite = ormLite;
	this.data = data;
	this.trabajosTableHandler = trabajosTableHandler;
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

	foo = new BatchTrabajosCalculation(this, dbTableName, columnDbNames[0]);
	table.setDefaultRenderer(Object.class, new ColorColumnRenderer(foo));

	foo.updateAllRows();
	autoFit();

	table.getModel().addTableModelListener(foo);
    }

    private void autoFit() {
	int avaliable = table.getColumnModel().getTotalColumnWidth();

	int[] maxLengths = ((BatchTrabajosTableModel) table.getModel())
		.getMaxLengths();
	double needed = 0.0;
	for (int i = 0; i < table.getColumnCount(); i++) {
	    int m = (table.getColumnName(i).length() > maxLengths[i]) ? table
		    .getColumnName(i).length() : maxLengths[i];
	    needed += m;
	}

	for (int i = 0; i < table.getModel().getColumnCount(); i++) {
	    double preferredWidth = avaliable * (maxLengths[i] / needed);

	    preferredWidth = 150;
	    table.getColumnModel().getColumn(i)
	    .setPreferredWidth((int) preferredWidth);
	}
    }

    @Override
    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.ICONIFIABLE
		    | WindowInfo.MAXIMIZABLE | WindowInfo.RESIZABLE
		    | WindowInfo.PALETTE);
	    viewInfo.setTitle(PluginServices.getText(this, "Añadir Trabajos"));
	    viewInfo.setWidth((int) table.getPreferredSize().getWidth() + 10);
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

    private void showWarning(String msg) {
	JOptionPane.showMessageDialog(
		(Component) PluginServices.getMainFrame(), msg, "Aviso",
		JOptionPane.WARNING_MESSAGE);
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
	    stopCellEdition();
	    if (!foo.validate()) {
		showWarning("Hay valores incorrectos en la tabla");
		saveButton.setEnabled(false);
		return;
	    }

	    try {
		TOCTableManager tm = new TOCTableManager();
		IEditableSource source = tm.getTableModelByName(dbTableName);
		for (int i = 0; i < data.length; i++) {
		    HashMap<String, String> newValues = new HashMap<String, String>();
		    for (int j = 0; j < data[i].length; j++) {
			String v = (data[i][j] == null) ? "" : data[i][j];
			newValues.put(columnDbNames[j], v);
		    }

		    // workaround. Si la unidad no es herbicida ponemos a null a
		    // la hora de guardar
		    // longitud y ancho
		    String unidad = newValues.get("unidad");
		    if ((unidad != null)
			    && (!unidad.equalsIgnoreCase("herbicida"))) {
			newValues.remove("ancho");
			newValues.remove("longitud");
		    }

		    TableController tableController = new TableController(
			    source);
		    tableController.create(newValues);
		}
		if (trabajosTableHandler != null) {
		    BaseTableModel m = (BaseTableModel) trabajosTableHandler
			    .getJTable().getModel();
		    m.dataChanged();
		    // workaround. next line should work if createTableModel in
		    // GIATableHandler doesnt ofuscate it
		    // trabajosTableHandler.getModel().dataChanged();
		}

	    } catch (Exception e) {
		logger.error(e.getStackTrace(), e);
		showWarning("Problema desconocido guardando la capa. Los datos no serán salvados");
		return;
	    }
	    JOptionPane.showMessageDialog(
		    null,
		    PluginServices.getText(this, "addedInfo_msg_I")
			    + data.length + " "
			    + PluginServices.getText(this, "addedInfo_msg_II"));

	    closeWindow();
	}
    }

    private void stopCellEdition() {
	if (table.isEditing()) {
	    if (table.getCellEditor() != null) {
		table.getCellEditor().stopCellEditing();
	    }
	}
    }

    public HashMap<String, String> getTableDataRow(int row, int columns) {
	HashMap<String, String> rowValues = new HashMap<String, String>();
	for (int i = 0; i < columns; i++) {
	    rowValues.put(columnNames[i], table.getValueAt(row, i).toString());
	}
	return rowValues;
    }

    private void setUnidadCellEditorAsComboBox() {
	DomainValues unidadValues = ormLite.getAppDomain()
		.getDomainValuesForComponent(DBFieldNames.UNIDAD);
	JComboBox unidadComboBox = new JComboBox();
	for (KeyValue value : unidadValues.getValues()) {
	    unidadComboBox.addItem(value.toString());
	}
	Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
	while (columns.hasMoreElements()) {
	    TableColumn unidadColumn = columns.nextElement();
	    if (unidadColumn.getHeaderValue().toString()
		    .equalsIgnoreCase(DBFieldNames.UNIDAD)) {
		unidadColumn
			.setCellEditor(new DefaultCellEditor(unidadComboBox));
		break;
	    }
	}
    }

    private void setFechaCellEditorAsJDateChooser() {
	Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
	while (columns.hasMoreElements()) {
	    TableColumn unidadColumn = columns.nextElement();
	    if (unidadColumn.getHeaderValue().toString()
		    .equalsIgnoreCase(DBFieldNames.FECHA)) {
		unidadColumn.setCellEditor(new DateCellEditor());
		break;
	    }
	}
    }

    public class DateCellEditor extends AbstractCellEditor implements
	    TableCellEditor {
	JDateChooser dateChooser = new JDateChooser();
	SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();

	@Override
	public Component getTableCellEditorComponent(JTable table,
		Object value, boolean isSelected, int row, int column) {
	    dateChooser.setDateFormatString(dateFormat.toPattern());
	    dateChooser.getDateEditor().setEnabled(false);
	    dateChooser.getDateEditor().getUiComponent()
		    .setBackground(new Color(255, 255, 255));
	    dateChooser.getDateEditor().getUiComponent()
		    .setFont(new Font("Arial", Font.PLAIN, 11));
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
	public Object getCellEditorValue() {
	    final Date date = dateChooser.getDate();
	    if (date != null) {
		return dateFormat.format(date);		
	    }
	    return "";
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
	    if ((data[row][col] == null) && (value != null)) {
		data[row][col] = (String) value;
		fireTableCellUpdated(row, col);
	    } else if ((data[row][col] == null) && (value == null)) {
		return;
	    } else if ((data[row][col] != null)
		    && (!data[row][col].equals(value))) {
		data[row][col] = (String) value;
		fireTableCellUpdated(row, col);
	    }
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
	    // TODO
	    TableColumn column = table.getColumnModel().getColumn(columnIndex);

	    for (int i = 0; i < DBFieldNames.trabajosVegetacionTableEditableCells.length; i++) {
		if (DBFieldNames.trabajosVegetacionTableEditableCells[i]
			.equals(column.getHeaderValue().toString())) {
		    return true;
		}
	    }
	    int unidadColumnIndex = table.getColumn("Unidad").getModelIndex();
	    Object foo = getValueAt(rowIndex, unidadColumnIndex);
	    if ((foo != null) && (foo.toString().equals("Herbicida"))) {
		if (column.getHeaderValue().toString().equals("Longitud")
			|| column.getHeaderValue().toString().equals("Ancho")) {
		    return true;
		}
	    }
	    return false;
	}

	public int[] getMaxLengths() {
	    int[] maxLengths = new int[getColumnCount()];
	    Arrays.fill(maxLengths, 50);

	    for (int i = 0; i < getRowCount(); i++) {
		for (int j = 0; j < getColumnCount(); j++) {
		    final Object o = getValueAt(i, j);
		    int l = (o == null) ? 0 : o.toString().length();
		    if (l > maxLengths[j]) {
			maxLengths[j] = l;
		    }
		}
	    }
	    return maxLengths;
	}
    }

    public JButton getSaveButton() {
	return saveButton;
    }

    public String[] getColumnDbNames() {
	return columnDbNames;
    }

    public ORMLite getOrmLite() {
	return ormLite;
    }

    public JTable getTable() {
	return table;
    }
}
