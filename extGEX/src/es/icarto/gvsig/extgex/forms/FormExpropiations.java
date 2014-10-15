package es.icarto.gvsig.extgex.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.commons.utils.Field;
import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.retrievers.LocalizadorFormatter;
import es.icarto.gvsig.navtableforms.BasicAbstractForm;
import es.icarto.gvsig.navtableforms.gui.CustomTableModel;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class FormExpropiations extends BasicAbstractForm implements
	TableModelListener {

    public static final String TABLENAME = "exp_finca";
    public static final Object TOCNAME = "Fincas";

    private static final String WIDGET_REVERSIONES = "tabla_reversiones_afectan";
    private static final String WIDGET_EXPROPIACIONES = "tabla_expropiaciones";
    private static final String WIDGET_PM = "tabla_pm_afectan";

    private JComboBox tramo;
    private JComboBox uc;
    private JComboBox ayuntamiento;
    private JComboBox subtramo;
    private JTextField finca;
    private JTextField numFinca;
    private JTextField seccion;

    private JTable expropiaciones;
    private JTable reversiones;
    private JTable pm;

    private JComboBox afectado_pm;

    private AddReversionsListener addReversionsListener;
    private DeleteReversionsListener deleteReversionsListener;
    private JButton addReversionsButton;
    private JButton deleteReversionsButton;

    private AddExpropiationListener addExpropiationListener;
    private DeleteExpropiationListener deleteExpropiationListener;
    private JButton addExpropiationButton;
    private JButton deleteExpropiationButton;

    private DependentComboboxHandler ucDomainHandler;
    private DependentComboboxHandler ayuntamientoDomainHandler;
    private DependentComboboxHandler subtramoDomainHandler;
    private UpdateNroFincaHandler updateNroFincaHandler;

    private FormReversionsLauncher formReversionsLauncher;

    private IGeometry insertedGeom;

    private ArrayList<String> oldReversions;

    public FormExpropiations(FLyrVect layer, IGeometry insertedGeom) {
	super(layer);
	if (insertedGeom != null) {
	    this.insertedGeom = insertedGeom;
	}
	addButtonsToActionsToolBar();

	addCalculation(new ImporteTotalPagadoCalculation(this));
	addCalculation(new ImportePendienteTotalCalculation(this));
    }

    private void addButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();
	NavTableComponentsFactory ntFactory = new NavTableComponentsFactory();
	JButton filesLinkB = ntFactory.getFilesLinkButton(layer, this);
	JButton printReportB = ntFactory.getPrintButton(layer, this);
	if ((filesLinkB != null) && (printReportB != null)) {
	    actionsToolBar.add(filesLinkB);
	    actionsToolBar.add(printReportB);
	}
	actionsToolBar.add(new JButton(new AddFincaAction(layer, this)));
    }

    @Override
    protected void enableSaveButton(boolean bool) {
	if (!isChangedValues()) {
	    saveB.setEnabled(false);
	} else {
	    saveB.setEnabled(bool);
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	// RETRIEVE WIDGETS
	Map<String, JComponent> widgets = getWidgets();

	ImageComponent image = (ImageComponent) formBody
		.getComponentByName("image");
	ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	tramo = (JComboBox) widgets.get(DBNames.FIELD_TRAMO_FINCAS);
	uc = (JComboBox) widgets.get(DBNames.FIELD_UC_FINCAS);
	ayuntamiento = (JComboBox) widgets
		.get(DBNames.FIELD_AYUNTAMIENTO_FINCAS);
	subtramo = (JComboBox) widgets
		.get(DBNames.FIELD_PARROQUIASUBTRAMO_FINCAS);

	numFinca = (JTextField) widgets.get(DBNames.FIELD_NUMEROFINCA_FINCAS);
	seccion = (JTextField) widgets.get(DBNames.FIELD_SECCION_FINCAS);

	finca = (JTextField) widgets.get(DBNames.FIELD_IDFINCA);
	// finca.setEnabled(false);

	expropiaciones = (JTable) widgets.get(WIDGET_EXPROPIACIONES);
	reversiones = (JTable) widgets.get(WIDGET_REVERSIONES);
	pm = (JTable) widgets.get(WIDGET_PM);

	afectado_pm = (JComboBox) widgets
		.get(DBNames.EXPROPIATIONS_AFECTADO_PM);

	addReversionsListener = new AddReversionsListener();
	addReversionsButton = (JButton) formBody
		.getComponentByName(DBNames.EXPROPIATIONS_ADD_REVERSIONS_BUTTON);
	addReversionsButton.addActionListener(addReversionsListener);

	deleteReversionsListener = new DeleteReversionsListener();
	deleteReversionsButton = (JButton) formBody
		.getComponentByName(DBNames.EXPROPIATIONS_DELETE_REVERSIONS_BUTTON);
	deleteReversionsButton.addActionListener(deleteReversionsListener);

	addExpropiationListener = new AddExpropiationListener();
	addExpropiationButton = (JButton) formBody
		.getComponentByName(DBNames.EXPROPIATIONS_ADD_EXPROPIATION_BUTTON);
	addExpropiationButton.addActionListener(addExpropiationListener);

	deleteExpropiationListener = new DeleteExpropiationListener();
	deleteExpropiationButton = (JButton) formBody
		.getComponentByName(DBNames.EXPROPIATIONS_DELETE_EXPROPIATION_BUTTON);
	deleteExpropiationButton.addActionListener(deleteExpropiationListener);

	// BIND LISTENERS TO WIDGETS
	ucDomainHandler = new DependentComboboxHandler(this, tramo, uc);
	tramo.addActionListener(ucDomainHandler);

	ayuntamientoDomainHandler = new DependentComboboxHandler(this, uc,
		ayuntamiento);
	uc.addActionListener(ayuntamientoDomainHandler);

	ArrayList<JComponent> parentComponents = new ArrayList<JComponent>();
	parentComponents.add(uc);
	parentComponents.add(ayuntamiento);
	subtramoDomainHandler = new DependentComboboxHandler(this,
		parentComponents, subtramo);
	ayuntamiento.addActionListener(subtramoDomainHandler);

	updateNroFincaHandler = new UpdateNroFincaHandler();
	subtramo.addActionListener(updateNroFincaHandler);
	numFinca.addKeyListener(updateNroFincaHandler);
	seccion.addKeyListener(updateNroFincaHandler);

	// BIND LAUNCHERS TO WIDGETS
	// LauncherParams expropiationsParams = new LauncherParams(this,
	// DBNames.TABLE_EXPROPIACIONES,
	// "Cultivos",
	// "Abrir cultivos");
	// tableExpropiationsLauncher = new AlphanumericNavTableLauncher(
	// this, expropiationsParams);
	formReversionsLauncher = new FormReversionsLauncher(this);
	// expropiaciones.addMouseListener(tableExpropiationsLauncher);
	reversiones.addMouseListener(formReversionsLauncher);

    }

    @Override
    protected void removeListeners() {
	super.removeListeners();

	tramo.removeActionListener(ucDomainHandler);
	uc.removeActionListener(ayuntamientoDomainHandler);
	ayuntamiento.removeActionListener(subtramoDomainHandler);
	subtramo.removeActionListener(updateNroFincaHandler);
	numFinca.removeKeyListener(updateNroFincaHandler);
	seccion.removeKeyListener(updateNroFincaHandler);

	// expropiaciones.removeMouseListener(tableExpropiationsLauncher);
	reversiones.removeMouseListener(formReversionsLauncher);

	addReversionsButton.removeActionListener(addReversionsListener);
	deleteReversionsButton.removeActionListener(deleteReversionsListener);

	addExpropiationButton.removeActionListener(addExpropiationListener);
	deleteExpropiationButton
		.removeActionListener(deleteExpropiationListener);
    }

    public class AddReversionsListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    SubFormExpropiationsAddReversions subForm = new SubFormExpropiationsAddReversions(
		    layer, reversiones, getIDFinca(), insertedGeom);
	    if (!subForm.getReversionsFromFinca().isEmpty()) {
		PluginServices.getMDIManager().addWindow(subForm);
	    } else {
		JOptionPane.showMessageDialog(null,
			PluginServices.getText(this, "cannotAddReversion"));
	    }
	}

    }

    public class DeleteReversionsListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    int[] selectedRows = reversiones.getSelectedRows();
	    DefaultTableModel model = (DefaultTableModel) reversiones
		    .getModel();
	    for (int i = 0; i < selectedRows.length; i++) {
		int rowIndex = selectedRows[i];
		model.removeRow(rowIndex);
		repaint();
	    }
	}
    }

    public class AddExpropiationListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    SubformExpropiationsAddExpropiation subForm = new SubformExpropiationsAddExpropiation(
		    expropiaciones);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class DeleteExpropiationListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    int[] selectedRows = expropiaciones.getSelectedRows();
	    DefaultTableModel model = (DefaultTableModel) expropiaciones
		    .getModel();
	    for (int i = 0; i < selectedRows.length; i++) {
		int rowIndex = selectedRows[i];
		model.removeRow(rowIndex);
		repaint();
	    }
	}
    }

    private void setIDFinca() {
	if ((tramo.getSelectedItem() instanceof KeyValue)
		&& (uc.getSelectedItem() instanceof KeyValue)
		&& (ayuntamiento.getSelectedItem() instanceof KeyValue)
		&& (subtramo.getSelectedItem() instanceof KeyValue)) {
	    // will update id_finca only when comboboxes have proper values
	    String id_finca = LocalizadorFormatter.getTramo(((KeyValue) tramo
		    .getSelectedItem()).getKey())
		    + LocalizadorFormatter.getUC(((KeyValue) uc
			    .getSelectedItem()).getKey())
		    + LocalizadorFormatter
			    .getAyuntamiento(((KeyValue) ayuntamiento
				    .getSelectedItem()).getKey())
		    + LocalizadorFormatter.getSubtramo(((KeyValue) subtramo
			    .getSelectedItem()).getKey())
		    + getStringNroFincaFormatted()
		    + getStringSeccionFormatted();
	    finca.setText(id_finca);
	    getFormController().setValue(DBNames.FIELD_IDFINCA, id_finca);
	}
    }

    public String getIDFinca() {
	return finca.getText();
    }

    private String getStringNroFincaFormatted() {
	HashMap<String, String> values = getFormController().getValuesChanged();
	try {
	    String formatted = LocalizadorFormatter.getNroFinca(values
		    .get(DBNames.FIELD_NUMEROFINCA_FINCAS));
	    numFinca.setText(formatted);
	    getFormController().setValue(DBNames.FIELD_NUMEROFINCA_FINCAS,
		    formatted);
	    return formatted;
	} catch (NumberFormatException nfe) {
	    numFinca.setText(LocalizadorFormatter.FINCA_DEFAULT_VALUE);
	    getFormController().setValue(DBNames.FIELD_NUMEROFINCA_FINCAS,
		    LocalizadorFormatter.FINCA_DEFAULT_VALUE);
	    return LocalizadorFormatter.FINCA_DEFAULT_VALUE;
	}
    }

    private String getStringSeccionFormatted() {
	HashMap<String, String> values = getFormController().getValuesChanged();
	String formatted = values.get(DBNames.FIELD_SECCION_FINCAS);
	seccion.setText(formatted);
	getFormController().setValue(DBNames.FIELD_SECCION_FINCAS, formatted);
	return formatted;
    }

    public class UpdateNroFincaHandler implements KeyListener, ActionListener {

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	    if (!isFillingValues()) {
		setIDFinca();
	    }
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues()) {
		setIDFinca();
	    }
	}
    }

    @Override
    protected void fillSpecificValues() {
	ucDomainHandler.updateComboBoxValues();
	ayuntamientoDomainHandler.updateComboBoxValues();
	subtramoDomainHandler.updateComboBoxValues();
	updateJTables();
    }

    private void updateJTables() {
	oldReversions = new ArrayList<String>();

	updateExpropiationsTable();
	updateReversionsTable();
	updatePMTable();

    }

    private void updateExpropiationsTable() {
	ArrayList<String> columnasCultivos = new ArrayList<String>();
	columnasCultivos.add(DBNames.FIELD_SUPERFICIE_EXPROPIACIONES);
	columnasCultivos.add(DBNames.FIELD_IDCULTIVO_EXPROPIACIONES);

	try {
	    DefaultTableModel tableModel;
	    tableModel = new DefaultTableModel();
	    for (String columnName : columnasCultivos) {
		tableModel.addColumn(columnName);
	    }
	    expropiaciones.setModel(tableModel);
	    Value[] expropiationData = new Value[3];
	    PreparedStatement statement;
	    String query = "SELECT " + DBNames.FIELD_SUPERFICIE_EXPROPIACIONES
		    + ", " + DBNames.FIELD_DESCRIPCION_CULTIVOS + " " + "FROM "
		    + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_EXPROPIACIONES
		    + " a, " + DBNames.SCHEMA_DATA + "."
		    + DBNames.TABLE_CULTIVOS + " b " + "WHERE a."
		    + DBNames.FIELD_IDCULTIVO_EXPROPIACIONES + " = " + "b."
		    + DBNames.FIELD_ID_CULTIVO_CULTIVOS + " AND "
		    + DBNames.FIELD_ID_FINCA_EXPROPIACIONES + " = '"
		    + getIDFinca() + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		if (rs.getObject(1) != null) {
		    NumberFormat doubleFormat = DoubleFormatNT
			    .getDisplayingFormat();
		    Double doubleValue = rs.getDouble(1);
		    String doubleAsString = doubleFormat.format(doubleValue);
		    expropiationData[0] = ValueFactory
			    .createValue(doubleAsString);
		} else {
		    expropiationData[0] = null;
		}
		if (rs.getObject(2) != null) {
		    expropiationData[1] = ValueFactory.createValue(rs
			    .getString(2));
		} else {
		    expropiationData[1] = null;
		}
		tableModel.addRow(expropiationData);
		// Save current Fincas in order to remove them
		// from database when there is some change in the table model
		// oldExpropiations.add(rs.getString(1));
	    }
	    repaint();
	    tableModel.addTableModelListener(this);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private void updateReversionsTable() {
	DefaultTableModel tableModel = setTableHeader();
	try {
	    reversiones.setModel(tableModel);
	    Value[] reversionData = new Value[tableModel.getColumnCount()];
	    PreparedStatement statement;
	    String query = "SELECT "
		    + DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES + ", "
		    + DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES + ", "
		    + DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES_EUROS + ", "
		    + DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES_PTAS + ", "
		    + DBNames.FIELD_FECHA_FINCAS_REVERSIONES + " " + "FROM "
		    + DBNames.SCHEMA_DATA + "."
		    + DBNames.TABLE_FINCASREVERSIONES + " " + "WHERE "
		    + DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES + " = '"
		    + getIDFinca() + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		reversionData[0] = ValueFactory.createValue(rs.getString(1));
		if (rs.getObject(2) != null) {
		    reversionData[1] = ValueFactory
			    .createValue(getDoubleFormatted(rs.getDouble(2)));
		} else {
		    reversionData[1] = ValueFactory.createNullValue();
		}
		if (rs.getObject(3) != null) {
		    reversionData[2] = ValueFactory
			    .createValue(getDoubleFormatted(rs.getDouble(3)));
		} else {
		    reversionData[2] = ValueFactory.createNullValue();
		}
		if (rs.getObject(4) != null) {
		    reversionData[3] = ValueFactory.createValue(rs.getInt(4));
		} else {
		    reversionData[3] = ValueFactory.createNullValue();
		}
		if (rs.getObject(5) != null) {
		    reversionData[4] = ValueFactory
			    .createValue(getDateFormatted(rs.getDate(5)));
		} else {
		    reversionData[4] = ValueFactory.createNullValue();
		}
		tableModel.addRow(reversionData);
		// Save current Fincas in order to remove them
		// from database when there is some change in the table model
		oldReversions.add(rs.getString(1));
	    }
	    repaint();
	    tableModel.addTableModelListener(this);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private DefaultTableModel setTableHeader() {
	CustomTableModel tableModel = new CustomTableModel();
	List<Field> columnasReversiones = new ArrayList<Field>();
	columnasReversiones.add(new Field("exp_id", "<html>Reversión</html>"));
	columnasReversiones.add(new Field("superficie",
		"<html>Superficie (m<sup>2</sup>)</html>"));
	columnasReversiones.add(new Field("importe_euros",
		"<html>Importe (&euro;)</html>"));
	columnasReversiones.add(new Field("importe_ptas",
		"<html>Importe (Ptas)</html>"));
	columnasReversiones.add(new Field("fecha_acta", "<html>Fecha</html>"));

	for (Field columnName : columnasReversiones) {
	    tableModel.addColumn(columnName);
	}

	return tableModel;
    }

    private String getDateFormatted(Date date) {
	SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
	return dateFormat.format(date);
    }

    private String getDoubleFormatted(Double doubleValue) {
	NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
	return doubleFormat.format(doubleValue);
    }

    public void updatePMTable() {
	ArrayList<String> columnasPM = new ArrayList<String>();
	columnasPM.add(DBNames.FIELD_NUMPM_FINCAS_PM);

	try {
	    DefaultTableModel tableModel;
	    tableModel = new DefaultTableModel();
	    for (String columnName : columnasPM) {
		tableModel.addColumn(columnName);
	    }
	    pm.setModel(tableModel);
	    pm.setEnabled(false);
	    Value[] pmData = new Value[3];
	    PreparedStatement statement;
	    String query = "SELECT " + DBNames.FIELD_NUMPM_FINCAS_PM + " "
		    + "FROM " + DBNames.PM_SCHEMA + "."
		    + DBNames.TABLE_FINCAS_PM + " " + "WHERE "
		    + DBNames.FIELD_IDFINCA_FINCAS_PM + " = '" + getIDFinca()
		    + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    if (rs.next()) {
		if (afectado_pm.getItemCount() > 1) {
		    afectado_pm.setSelectedIndex(1);
		}
	    }
	    rs.beforeFirst();
	    while (rs.next()) {
		pmData[0] = ValueFactory.createValue(rs.getString(1));
		tableModel.addRow(pmData);
	    }
	    repaint();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public boolean saveRecord() throws StopWriterVisitorException {
	saveReversionsTable();
	saveExpropiationsTable();
	return super.saveRecord();
    }

    private void saveReversionsTable() {
	PreparedStatement statement;
	String query = null;
	String idReversion = null;
	String superficie;
	String importeEuros;
	String importePtas = null;
	String fechaActa = null;

	// Check if ID reversion exists into reversions table
	for (int i = 0; i < reversiones.getRowCount(); i++) {
	    idReversion = reversiones.getModel().getValueAt(i, 0).toString();
	    query = "SELECT " + DBNames.FIELD_IDREVERSION_REVERSIONES + " "
		    + "FROM " + DBNames.SCHEMA_DATA + "."
		    + DBNames.TABLE_REVERSIONES + " " + "WHERE "
		    + DBNames.FIELD_IDREVERSION_REVERSIONES + " = '"
		    + idReversion + "';";
	    try {
		statement = DBSession.getCurrentSession().getJavaConnection()
			.prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		if (!rs.next()) {
		    JOptionPane
			    .showMessageDialog(
				    this,
				    "EL ID de Reversión: "
					    + idReversion
					    + " no existe. Modifique los datos para poder guardar.",
				    "Error en los datos",
				    JOptionPane.ERROR_MESSAGE);
		    return;
		}
	    } catch (SQLException e) {
		e.getMessage();
	    }
	}

	// we remove old Reversions on this Finca
	for (String ReversionID : oldReversions) {
	    try {
		query = "DELETE FROM " + DBNames.SCHEMA_DATA + "."
			+ DBNames.TABLE_FINCASREVERSIONES + " " + "WHERE "
			+ DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES
			+ " = '" + getIDFinca() + "' AND "
			+ DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES + " = '"
			+ ReversionID + "';";
		statement = DBSession.getCurrentSession().getJavaConnection()
			.prepareStatement(query);
		statement.execute();
	    } catch (SQLException e) {
		e.printStackTrace();
		continue;
	    }
	}

	// Now, we save into database current state of JTable
	for (int i = 0; i < reversiones.getRowCount(); i++) {
	    try {
		idReversion = reversiones.getModel().getValueAt(i, 0)
			.toString();
		if (reversiones.getModel().getValueAt(i, 1) != null) {
		    superficie = reversiones.getModel().getValueAt(i, 1)
			    .toString();
		    if (superficie.contains(",")) {
			superficie = superficie.replace(",", ".");
		    }
		} else {
		    superficie = null;
		}
		if (reversiones.getModel().getValueAt(i, 2) != null) {
		    importeEuros = reversiones.getModel().getValueAt(i, 2)
			    .toString();
		    if (importeEuros.contains(",")) {
			importeEuros = importeEuros.replace(",", ".");
		    }
		} else {
		    importeEuros = null;
		}

		if (importeEuros != null) {
		    try {
			double parseDouble = Double.parseDouble(importeEuros) * 166.386;
			importePtas = Long.toString(Math.round(parseDouble));
		    } catch (NumberFormatException e) {
			importeEuros = null;
			importePtas = null;
		    }

		}

		if (reversiones.getModel().getValueAt(i, 4) != null) {
		    fechaActa = reversiones.getModel().getValueAt(i, 4)
			    .toString();
		    try {
			DateFormatNT.getDateFormat().parse(fechaActa);
		    } catch (ParseException e) {
			fechaActa = null;
		    }
		}

		query = "INSERT INTO "
			+ DBNames.SCHEMA_DATA
			+ "."
			+ DBNames.TABLE_FINCASREVERSIONES
			+ " (id_finca, id_reversion, superficie, importe_euros, importe_ptas, fecha_acta) "
			+ "VALUES ('" + getIDFinca() + "', '" + idReversion
			+ "',";
		if (superficie != null) {
		    query = query + " '" + superficie + "',";
		} else {
		    query = query + " null,";
		}
		if (importeEuros != null) {
		    query = query + " '" + importeEuros + "',";
		} else {
		    query = query + " null );";
		}
		if (importePtas != null) {
		    query = query + " '" + importePtas + "',";
		} else {
		    query = query + " null );";
		}
		if (fechaActa != null) {
		    query = query + " '" + fechaActa + "');";
		} else {
		    query = query + " null );";
		}

		statement = DBSession.getCurrentSession().getJavaConnection()
			.prepareStatement(query);
		statement.execute();
	    } catch (SQLException e) {
		e.printStackTrace();
		continue;
	    }
	}
    }

    private void saveExpropiationsTable() {
	PreparedStatement statement;
	String query = null;
	String superficie;
	String cultivo;

	// First, we remove old Expropiations on this Finca
	try {
	    query = "DELETE FROM " + DBNames.SCHEMA_DATA + "."
		    + DBNames.TABLE_EXPROPIACIONES + " " + "WHERE "
		    + DBNames.FIELD_ID_FINCA_EXPROPIACIONES + " = '"
		    + getIDFinca() + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
	    statement.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	// Now, we save into database current state of JTable
	for (int i = 0; i < expropiaciones.getRowCount(); i++) {
	    try {
		if (expropiaciones.getModel().getValueAt(i, 0) != null) {
		    superficie = expropiaciones.getModel().getValueAt(i, 0)
			    .toString();
		    if (superficie.contains(",")) {
			superficie = superficie.replace(",", ".");
		    }
		} else {
		    superficie = null;
		}
		if (expropiaciones.getModel().getValueAt(i, 1) != null) {
		    cultivo = expropiaciones.getModel().getValueAt(i, 1)
			    .toString();
		} else {
		    cultivo = null;
		}
		query = "INSERT INTO " + DBNames.SCHEMA_DATA + "."
			+ DBNames.TABLE_EXPROPIACIONES + " " + "VALUES ('"
			+ getIDFinca() + "',";
		if (superficie != null) {
		    query = query + " '" + superficie + "',";
		} else {
		    query = query + " null,";
		}
		if (cultivo != null) {
		    String cultivoID = getIDCultivo(cultivo);

		    query = query + " '" + cultivoID + "');";
		} else {
		    query = query + " null );";
		}
		statement = DBSession.getCurrentSession().getJavaConnection()
			.prepareStatement(query);
		statement.execute();
	    } catch (SQLException e) {
		e.printStackTrace();
		continue;
	    }
	}

    }

    private String getIDCultivo(String cultivo) {
	String cultivoID = null;
	PreparedStatement statement;
	String query = "SELECT " + DBNames.FIELD_ID_CULTIVO_CULTIVOS + " "
		+ "FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_CULTIVOS
		+ " " + "WHERE " + DBNames.FIELD_DESCRIPCION_CULTIVOS + " = "
		+ "'" + cultivo + "';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareCall(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.next();
	    cultivoID = rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return cultivoID;
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
	updateJTables();
    }

    public String getSQLQuery(String queryID) {
	if (queryID.equalsIgnoreCase("EXPROPIACIONES")) {
	    return "select * from " + "'" + DBNames.TABLE_EXPROPIACIONES + "'"
		    + "where " + DBNames.FIELD_IDFINCA + " = " + "'"
		    + getIDFinca() + "'" + ";";
	}
	return null;
    }

    @Override
    public String getBasicName() {
	return TABLENAME;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
	super.setChangedValues(true);
	super.saveB.setEnabled(true);
    }

}
