package es.icarto.gvsig.extgex.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.preferences.GEXPreferences;
import es.icarto.gvsig.extgex.utils.retrievers.LocalizadorFormatter;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelFactory;
import es.icarto.gvsig.navtableforms.launcher.AlphanumericNavTableLauncher;
import es.icarto.gvsig.navtableforms.launcher.ILauncherForm;
import es.icarto.gvsig.navtableforms.launcher.LauncherParams;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.icarto.gvsig.navtableforms.validation.listeners.DependentComboboxesHandler;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormExpropiations extends AbstractForm implements ILauncherForm, TableModelListener {

    private static final String WIDGET_REVERSIONES = "tabla_reversiones_afectan";
    private static final String WIDGET_EXPROPIACIONES = "tabla_expropiaciones";

    private FormPanel form;

    private JComboBox tramo;
    private JComboBox uc;
    private JComboBox ayuntamiento;
    private JComboBox subtramo;
    private JTextField finca;
    private JTextField numFinca;
    private JTextField seccion;
    private JTable expropiaciones;
    private JTable reversiones;

    private AddReversionsListener addReversionsListener;
    private DeleteReversionsListener deleteReversionsListener;
    private JButton addReversionsButton;
    private JButton deleteReversionsButton;

    private DependentComboboxesHandler ucDomainHandler;
    private DependentComboboxesHandler ayuntamientoDomainHandler;
    private DependentComboboxesHandler subtramoDomainHandler;
    private UpdateNroFincaHandler updateNroFincaHandler;

    private AlphanumericNavTableLauncher tableExpropiationsLauncher;
    private FormReversionsLauncher formReversionsLauncher;
    private FLyrVect layer = null;

    private ArrayList<String> oldReversions;

    public FormExpropiations(FLyrVect layer) {
	super(layer);
	this.layer = layer;
	initWindow();
	addButtonsToActionsToolBar();
    }

    private void addButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();
	NavTableComponentsFactory ntFactory = new NavTableComponentsFactory();
	JButton filesLinkB = ntFactory.getFilesLinkButton(layer,
		this);
	JButton printReportB = ntFactory.getPrintButton(layer,
		this);
	if ((filesLinkB != null) && (printReportB != null)) {
	    actionsToolBar.add(filesLinkB);
	    actionsToolBar.add(printReportB);
	}
    }

    private void initWindow() {
	viewInfo.setHeight(650);
	viewInfo.setWidth(750);
	viewInfo.setTitle("Expediente de expropiaciones");
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("ExpropiationsFileForm");
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    InputStream stream = getClass().getClassLoader().getResourceAsStream("expropiaciones.xml");
	    FormPanel result = null;
	    try {
		result = new FormPanel(stream);
	    } catch (FormException e) {
		e.printStackTrace();
	    }
	    form = result;
	}
	return form;
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	// RETRIEVE WIDGETS
	HashMap<String, JComponent> widgets = getWidgetComponents();

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	tramo = (JComboBox) widgets.get(DBNames.FIELD_TRAMO_FINCAS);
	uc = (JComboBox) widgets.get(DBNames.FIELD_UC_FINCAS);
	ayuntamiento = (JComboBox) widgets.get(DBNames.FIELD_AYUNTAMIENTO_FINCAS);
	subtramo = (JComboBox) widgets.get(DBNames.FIELD_PARROQUIASUBTRAMO_FINCAS);

	numFinca = (JTextField) widgets.get(DBNames.FIELD_NUMEROFINCA_FINCAS);
	seccion = (JTextField) widgets.get(DBNames.FIELD_SECCION_FINCAS);

	finca = (JTextField) widgets.get(DBNames.FIELD_IDFINCA);
	//	finca.setEnabled(false);

	expropiaciones = (JTable) widgets.get(WIDGET_EXPROPIACIONES);
	reversiones = (JTable) widgets.get(WIDGET_REVERSIONES);

	addReversionsListener = new AddReversionsListener();
	addReversionsButton = (JButton) form.getComponentByName(DBNames.EXPROPIATIONS_ADD_REVERSIONS_BUTTON);
	addReversionsButton.addActionListener(addReversionsListener);

	deleteReversionsListener = new DeleteReversionsListener();
	deleteReversionsButton = (JButton) form.getComponentByName(DBNames.EXPROPIATIONS_DELETE_REVERSIONS_BUTTON);
	deleteReversionsButton.addActionListener(deleteReversionsListener);

	// BIND LISTENERS TO WIDGETS
	ucDomainHandler = new DependentComboboxesHandler(this, tramo, uc);
	tramo.addActionListener(ucDomainHandler);

	ayuntamientoDomainHandler = new DependentComboboxesHandler(this, uc, ayuntamiento);
	uc.addActionListener(ayuntamientoDomainHandler);

	ArrayList<JComboBox> parentComponents = new ArrayList<JComboBox>();
	parentComponents.add(uc);
	parentComponents.add(ayuntamiento);
	subtramoDomainHandler = new DependentComboboxesHandler(this, parentComponents, subtramo);
	ayuntamiento.addActionListener(subtramoDomainHandler);

	updateNroFincaHandler = new UpdateNroFincaHandler();
	subtramo.addActionListener(updateNroFincaHandler);
	numFinca.addKeyListener(updateNroFincaHandler);
	seccion.addKeyListener(updateNroFincaHandler);

	// BIND LAUNCHERS TO WIDGETS
	LauncherParams expropiationsParams = new LauncherParams(this,
		DBNames.TABLE_EXPROPIACIONES,
		"Cultivos",
		"Abrir cultivos");
	tableExpropiationsLauncher = new AlphanumericNavTableLauncher(
		this, expropiationsParams);
	formReversionsLauncher = new FormReversionsLauncher(this);
	expropiaciones.addMouseListener(tableExpropiationsLauncher);
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

	expropiaciones.removeMouseListener(tableExpropiationsLauncher);
	reversiones.removeMouseListener(formReversionsLauncher);

	addReversionsButton.removeActionListener(addReversionsListener);
	deleteReversionsButton.removeActionListener(deleteReversionsListener);
    }

    public class AddReversionsListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    SubFormExpropiationsAddReversions subForm = new SubFormExpropiationsAddReversions(layer, reversiones, getIDFinca());
	    PluginServices.getMDIManager().addWindow(subForm);
	}

    }

    public class DeleteReversionsListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    int[] selectedRows = reversiones.getSelectedRows();
	    DefaultTableModel model = (DefaultTableModel) reversiones.getModel();
	    for (int i=0; i<selectedRows.length; i++) {
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
	    String id_finca = LocalizadorFormatter.getTramo(((KeyValue) tramo.getSelectedItem()).getKey())
		    + LocalizadorFormatter.getUC(((KeyValue) uc.getSelectedItem()).getKey())
		    + LocalizadorFormatter.getAyuntamiento(((KeyValue) ayuntamiento.getSelectedItem()).getKey())
		    + LocalizadorFormatter.getSubtramo(((KeyValue) subtramo.getSelectedItem()).getKey())
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
	    String formatted = LocalizadorFormatter.getNroFinca(
		    values.get(DBNames.FIELD_NUMEROFINCA_FINCAS));
	    numFinca.setText(formatted);
	    getFormController().setValue(DBNames.FIELD_NUMEROFINCA_FINCAS, formatted);
	    return formatted;
	} catch (NumberFormatException nfe) {
	    numFinca.setText(LocalizadorFormatter.FINCA_DEFAULT_VALUE);
	    getFormController().setValue(DBNames.FIELD_NUMEROFINCA_FINCAS, LocalizadorFormatter.FINCA_DEFAULT_VALUE);
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

	ArrayList<String> columnasCultivos = new ArrayList<String>();
	columnasCultivos.add(DBNames.FIELD_SUPERFICIE_EXPROPIACIONES);
	columnasCultivos.add(DBNames.FIELD_IDCULTIVO_EXPROPIACIONES);
	try {
	    expropiaciones.setModel(TableModelFactory.createFromTable(
		    DBNames.TABLE_EXPROPIACIONES,
		    DBNames.FIELD_IDFINCA, finca.getText(),
		    columnasCultivos, columnasCultivos));
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}

	ArrayList<String> columnasReversiones = new ArrayList<String>();
	columnasReversiones.add(DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES);
	columnasReversiones.add(DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES);
	columnasReversiones.add(DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES);

	try {
	    DefaultTableModel tableModel;
	    tableModel = new DefaultTableModel();
	    for (String columnName : columnasReversiones) {
		tableModel.addColumn(columnName);
	    }
	    reversiones.setModel(tableModel);
	    Value[] reversionData = new Value[3];
	    PreparedStatement statement;
	    String query = "SELECT " +
		    DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES + ", " +
		    DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES + ", " +
		    DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES + " " +
		    "FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCASREVERSIONES + " " +
		    "WHERE " + DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES + " = '" + getIDFinca() +
		    "';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		reversionData[0] = ValueFactory.createValue(rs.getString(1));
		if (rs.getObject(2) != null) {
		    reversionData[1] = ValueFactory.createValue(rs.getDouble(2));
		}else {
		    reversionData[1] = null;
		}
		if (rs.getObject(3) != null) {
		    reversionData[2] = ValueFactory.createValue(rs.getInt(3));
		}else {
		    reversionData[2] = null;
		}
		tableModel.addRow(reversionData);
		//Save current Fincas in order to remove them
		//from database when there is some change in the table model
		oldReversions.add(rs.getString(1));
	    }
	    repaint();
	    tableModel.addTableModelListener(this);
	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    @Override
    public boolean saveRecord() {
	PreparedStatement statement;
	String query = null;
	String idReversion;
	String superficie;
	String importe;

	// First, we remove old Reversions on this Finca
	for (String ReversionID : oldReversions) {
	    try {
		query = "DELETE FROM " +
			DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCASREVERSIONES + " " +
			"WHERE " + DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES + " = '" +
			getIDFinca() + "' AND " + DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES +
			" = '" + ReversionID + "';";
		statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
		statement.execute();
	    } catch (SQLException e) {
		e.printStackTrace();
		continue;
	    }
	}

	// Now, we save into database current state of JTable
	for (int i=0; i<reversiones.getRowCount(); i++) {
	    try {
		idReversion = reversiones.getModel().getValueAt(i, 0).toString();
		if (reversiones.getModel().getValueAt(i, 1) != null) {
		    superficie = reversiones.getModel().getValueAt(i, 1).toString();
		}else {
		    superficie = null;
		}
		if (reversiones.getModel().getValueAt(i, 2) != null) {
		    importe = reversiones.getModel().getValueAt(i, 2).toString();
		}else {
		    importe = null;
		}
		query = "INSERT INTO " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCASREVERSIONES + " " +
			"VALUES ('" + getIDFinca() + "', '" + idReversion + "',";
		if (superficie != null) {
		    query = query + " '" + superficie + "',";
		}else {
		    query = query + " null,";
		}
		if (importe != null) {
		    query = query + " '" + importe + "');";
		}else {
		    query = query + " null );";
		}
		statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
		statement.execute();
	    } catch (SQLException e) {
		e.printStackTrace();
		continue;
	    }
	}
	return super.saveRecord();
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
	updateJTables();
    }

    @Override
    public String getSQLQuery(String queryID) {
	if (queryID.equalsIgnoreCase("EXPROPIACIONES")) {
	    return "select * from " + "'"
		    + DBNames.TABLE_EXPROPIACIONES + "'"
		    + "where " + DBNames.FIELD_IDFINCA + " = " + "'" + getIDFinca() + "'" + ";";
	}
	return null;
    }

    protected String getAliasInXML() {
	return "exp_finca";
    }

    @Override
    public String getXMLPath() {
	return GEXPreferences.getPreferences().getXMLFilePath();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
	super.setChangedValues(true);
	super.saveB.setEnabled(true);
    }

}
