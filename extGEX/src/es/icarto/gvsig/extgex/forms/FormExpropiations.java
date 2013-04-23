package es.icarto.gvsig.extgex.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

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

import org.apache.log4j.Logger;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IGeometry;
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
import es.icarto.gvsig.navtableforms.launcher.AlphanumericNavTableLauncher;
import es.icarto.gvsig.navtableforms.launcher.ILauncherForm;
import es.icarto.gvsig.navtableforms.launcher.LauncherParams;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.icarto.gvsig.navtableforms.validation.listeners.DependentComboboxesHandler;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormExpropiations extends AbstractForm implements ILauncherForm, TableModelListener {

    private static final String WIDGET_REVERSIONES = "tabla_reversiones_afectan";
    private static final String WIDGET_EXPROPIACIONES = "tabla_expropiaciones";
    private static final String WIDGET_PM = "tabla_pm_afectan";

    private FormPanel form;

    private JComboBox tramo;
    private JComboBox uc;
    private JComboBox ayuntamiento;
    private JComboBox subtramo;
    private JTextField finca;
    private JTextField numFinca;
    private JTextField seccion;
    private JTextField importe_pendiente_mejoras;
    private JTextField importe_pendiente_terrenos;
    private JTextField importe_pendiente_total_autocalculado;
    private JTextField importe_mutuo_acuerdo;
    private JTextField importe_anticipo;
    private JTextField importe_deposito_previo_pagado;
    private JTextField importe_deposito_previo_consignado;
    private JTextField importe_mutuo_acuerdo_parcial;
    private JTextField importe_pagos_varios;
    private JTextField importe_deposito_previo_levantado;
    private JTextField importe_pagado_total;
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

    private DependentComboboxesHandler ucDomainHandler;
    private DependentComboboxesHandler ayuntamientoDomainHandler;
    private DependentComboboxesHandler subtramoDomainHandler;
    private UpdateNroFincaHandler updateNroFincaHandler;

    private UpdateImportePendienteHandler updateImportePendienteHandler;
    private UpdateImportePagadoHandler updateImportePagadoHandler;

    private AlphanumericNavTableLauncher tableExpropiationsLauncher;
    private FormReversionsLauncher formReversionsLauncher;

    private FLyrVect layer = null;
    private IGeometry insertedGeom;

    private ArrayList<String> oldReversions;

    public FormExpropiations(FLyrVect layer, IGeometry insertedGeom) {
	super(layer);
	this.layer = layer;
	if (insertedGeom != null) {
	    this.insertedGeom = insertedGeom;
	}
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

    @Override
    protected void enableSaveButton(boolean bool) {
	if (!isChangedValues()) {
	    saveB.setEnabled(false);
	} else {
	    saveB.setEnabled(bool);
	}
    }

    private void initWindow() {
	viewInfo.setHeight(700);
	viewInfo.setWidth(690);
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

	updateImportePendienteHandler = new UpdateImportePendienteHandler();
	importe_pendiente_mejoras = (JTextField) widgets.get(DBNames.FINCAS_IMPORTE_PENDIENTE_MEJORAS);
	importe_pendiente_mejoras.addKeyListener(updateImportePendienteHandler);
	importe_pendiente_terrenos = (JTextField) widgets.get(DBNames.FINCAS_IMPORTE_PENDIENTE_TERRENOS);
	importe_pendiente_terrenos.addKeyListener(updateImportePendienteHandler);
	importe_pendiente_total_autocalculado = (JTextField) widgets.get(DBNames.FINCAS_IMPORTE_PENDIENTE_TOTAL_AUTOCALCULADO);

	updateImportePagadoHandler = new UpdateImportePagadoHandler();
	importe_mutuo_acuerdo = (JTextField) widgets.get(DBNames.FINCAS_MUTUO_ACUERDO);
	importe_mutuo_acuerdo.addKeyListener(updateImportePagadoHandler);
	importe_anticipo = (JTextField) widgets.get(DBNames.FINCAS_ANTICIPO);
	importe_anticipo.addKeyListener(updateImportePagadoHandler);
	importe_deposito_previo_pagado = (JTextField) widgets.get(DBNames.FINCAS_DEPOSITO_PREVIO_PAGADO);
	importe_deposito_previo_pagado.addKeyListener(updateImportePagadoHandler);
	importe_deposito_previo_consignado = (JTextField) widgets.get(DBNames.FINCAS_DEPOSITO_PREVIO_CONSIGNADO);
	importe_deposito_previo_consignado.addKeyListener(updateImportePagadoHandler);
	importe_mutuo_acuerdo_parcial = (JTextField) widgets.get(DBNames.FINCAS_MUTUO_ACUERDO_PARCIAL);
	importe_mutuo_acuerdo_parcial.addKeyListener(updateImportePagadoHandler);
	importe_pagos_varios = (JTextField) widgets.get(DBNames.FINCAS_PAGOS_VARIOS);
	importe_pagos_varios.addKeyListener(updateImportePagadoHandler);
	importe_deposito_previo_levantado = (JTextField) widgets.get(DBNames.FINCAS_DEPOSITO_PREVIO_LEVANTADO);
	importe_deposito_previo_levantado.addKeyListener(updateImportePagadoHandler);
	importe_pagado_total = (JTextField) widgets.get(DBNames.FINCAS_IMPORTE_PAGADO_TOTAL_AUTOCALCULADO);

	expropiaciones = (JTable) widgets.get(WIDGET_EXPROPIACIONES);
	reversiones = (JTable) widgets.get(WIDGET_REVERSIONES);
	pm = (JTable) widgets.get(WIDGET_PM);

	afectado_pm = (JComboBox) widgets.get(DBNames.EXPROPIATIONS_AFECTADO_PM);

	addReversionsListener = new AddReversionsListener();
	addReversionsButton = (JButton) form.getComponentByName(DBNames.EXPROPIATIONS_ADD_REVERSIONS_BUTTON);
	addReversionsButton.addActionListener(addReversionsListener);

	deleteReversionsListener = new DeleteReversionsListener();
	deleteReversionsButton = (JButton) form.getComponentByName(DBNames.EXPROPIATIONS_DELETE_REVERSIONS_BUTTON);
	deleteReversionsButton.addActionListener(deleteReversionsListener);

	addExpropiationListener = new AddExpropiationListener();
	addExpropiationButton = (JButton) form.getComponentByName(DBNames.EXPROPIATIONS_ADD_EXPROPIATION_BUTTON);
	addExpropiationButton.addActionListener(addExpropiationListener);

	deleteExpropiationListener = new DeleteExpropiationListener();
	deleteExpropiationButton = (JButton) form.getComponentByName(DBNames.EXPROPIATIONS_DELETE_EXPROPIATION_BUTTON);
	deleteExpropiationButton.addActionListener(deleteExpropiationListener);

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

	importe_pendiente_mejoras.removeKeyListener(updateImportePendienteHandler);
	importe_pendiente_terrenos.removeKeyListener(updateImportePendienteHandler);

	importe_mutuo_acuerdo.removeKeyListener(updateImportePagadoHandler);
	importe_anticipo.removeKeyListener(updateImportePagadoHandler);
	importe_deposito_previo_pagado.removeKeyListener(updateImportePagadoHandler);
	importe_deposito_previo_consignado.removeKeyListener(updateImportePagadoHandler);
	importe_mutuo_acuerdo_parcial.removeKeyListener(updateImportePagadoHandler);
	importe_pagos_varios.removeKeyListener(updateImportePagadoHandler);
	importe_deposito_previo_levantado.removeKeyListener(updateImportePagadoHandler);

	expropiaciones.removeMouseListener(tableExpropiationsLauncher);
	reversiones.removeMouseListener(formReversionsLauncher);

	addReversionsButton.removeActionListener(addReversionsListener);
	deleteReversionsButton.removeActionListener(deleteReversionsListener);

	addExpropiationButton.removeActionListener(addExpropiationListener);
	deleteExpropiationButton.removeActionListener(deleteExpropiationListener);
    }

    public class AddReversionsListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    SubFormExpropiationsAddReversions subForm = new SubFormExpropiationsAddReversions(layer, reversiones, getIDFinca(), insertedGeom);
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

    public class AddExpropiationListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    SubformExpropiationsAddExpropiation subForm = new SubformExpropiationsAddExpropiation(expropiaciones);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class DeleteExpropiationListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    int[] selectedRows = expropiaciones.getSelectedRows();
	    DefaultTableModel model = (DefaultTableModel) expropiaciones.getModel();
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

    public class UpdateImportePendienteHandler implements KeyListener {

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	    if (!isFillingValues()) {
		getFormController().setValue(DBNames.FINCAS_IMPORTE_PENDIENTE_TOTAL_AUTOCALCULADO, setImporteTotalPendiente());
	    }
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
    }

    public class UpdateImportePagadoHandler implements KeyListener {

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	    if (!isFillingValues()) {
		getFormController().setValue(DBNames.FINCAS_IMPORTE_PAGADO_TOTAL_AUTOCALCULADO, setImporteTotalPagado());
	    }

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
    }

    private String setImporteTotalPendiente() {
	if ((importe_pendiente_mejoras.getText().isEmpty()) && (importe_pendiente_terrenos.getText().isEmpty())) {
	    importe_pendiente_total_autocalculado.setText("");
	}else if (!importe_pendiente_mejoras.getText().isEmpty() && (importe_pendiente_terrenos.getText().isEmpty())) {
	    importe_pendiente_total_autocalculado.setText(importe_pendiente_mejoras.getText());
	}else if (importe_pendiente_mejoras.getText().isEmpty() && (!importe_pendiente_terrenos.getText().isEmpty())) {
	    importe_pendiente_total_autocalculado.setText(importe_pendiente_terrenos.getText());
	}else {
	    String importeMejorasText = importe_pendiente_mejoras.getText();
	    if (importeMejorasText.contains(",")) {
		importeMejorasText = importeMejorasText.replace(",", ".");
	    }
	    double importe_mejoras = Double.parseDouble(importeMejorasText);
	    String importeTerrenosText = importe_pendiente_terrenos.getText();
	    if (importeTerrenosText.contains(",")) {
		importeTerrenosText = importeTerrenosText.replace(",", ".");
	    }
	    double importe_terrenos = Double.parseDouble(importeTerrenosText);

	    double importe_total = importe_mejoras + importe_terrenos;

	    BigDecimal importeTotalAsBigDecimal = BigDecimal.valueOf(importe_total);
	    importe_pendiente_total_autocalculado.setText(String.valueOf(importeTotalAsBigDecimal));
	}
	return importe_pendiente_total_autocalculado.getText();
    }

    private String setImporteTotalPagado() {
	double importe_mutuo_acuerdo_double;
	double importe_anticipo_double;
	double importe_deposito_previo_pagado_double;
	double importe_deposito_previo_consignado_double;
	double importe_mutuo_acuerdo_parcial_double;
	double importe_pagos_varios_double;
	double importe_pagado_total_double;

	if (importe_mutuo_acuerdo.getText().isEmpty() &&
		importe_anticipo.getText().isEmpty() &&
		importe_deposito_previo_pagado.getText().isEmpty() &&
		importe_deposito_previo_consignado.getText().isEmpty() &&
		importe_mutuo_acuerdo_parcial.getText().isEmpty() &&
		importe_pagos_varios.getText().isEmpty()) {
	    importe_pagado_total.setText("");
	}else {
	    if (!importe_mutuo_acuerdo.getText().isEmpty()) {
		String importe = importe_mutuo_acuerdo.getText();
		if (importe.contains(",")) {
		    importe = importe.replace(",", ".");
		}
		importe_mutuo_acuerdo_double = Double.parseDouble(importe);
	    }else {
		importe_mutuo_acuerdo_double = 0.0;
	    }
	    if (!importe_anticipo.getText().isEmpty()) {
		String importe = importe_anticipo.getText();
		if (importe.contains(",")) {
		    importe = importe.replace(",", ".");
		}
		importe_anticipo_double = Double.parseDouble(importe);
	    }else {
		importe_anticipo_double = 0.0;
	    }
	    if (!importe_deposito_previo_pagado.getText().isEmpty()) {
		String importe = importe_deposito_previo_pagado.getText();
		if (importe.contains(",")) {
		    importe = importe.replace(",", ".");
		}
		importe_deposito_previo_pagado_double = Double.parseDouble(importe);
	    }else {
		importe_deposito_previo_pagado_double = 0.0;
	    }
	    if (!importe_deposito_previo_consignado.getText().isEmpty()) {
		String importe = importe_deposito_previo_consignado.getText();
		if (importe.contains(",")) {
		    importe = importe.replace(",", ".");
		}
		importe_deposito_previo_consignado_double = Double.parseDouble(importe);
	    }else {
		importe_deposito_previo_consignado_double = 0.0;
	    }
	    if (!importe_mutuo_acuerdo_parcial.getText().isEmpty()) {
		String importe = importe_mutuo_acuerdo_parcial.getText();
		if (importe.contains(",")) {
		    importe = importe.replace(",", ".");
		}
		importe_mutuo_acuerdo_parcial_double = Double.parseDouble(importe);
	    }else {
		importe_mutuo_acuerdo_parcial_double = 0.0;
	    }
	    if (!importe_pagos_varios.getText().isEmpty()) {
		String importe = importe_pagos_varios.getText();
		if (importe.contains(",")) {
		    importe = importe.replace(",", ".");
		}
		importe_pagos_varios_double = Double.parseDouble(importe);
	    }else {
		importe_pagos_varios_double = 0.0;
	    }
	    importe_pagado_total_double = importe_mutuo_acuerdo_double + importe_anticipo_double +
		    importe_deposito_previo_pagado_double + importe_deposito_previo_consignado_double +
		    importe_mutuo_acuerdo_parcial_double + importe_pagos_varios_double;
	    BigDecimal importeTotalAsBigDecimal = BigDecimal.valueOf(importe_pagado_total_double);
	    importe_pagado_total.setText(String.valueOf(importeTotalAsBigDecimal));
	}
	return importe_pagado_total.getText();
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
	    String query = "SELECT " +
		    DBNames.FIELD_SUPERFICIE_EXPROPIACIONES + ", " +
		    DBNames.FIELD_DESCRIPCION_CULTIVOS + " " +
		    "FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_EXPROPIACIONES + " a, " +
		    DBNames.SCHEMA_DATA + "." + DBNames.TABLE_CULTIVOS + " b " +
		    "WHERE a." + DBNames.FIELD_IDCULTIVO_EXPROPIACIONES + " = " +
		    "b." + DBNames.FIELD_ID_CULTIVO_CULTIVOS + " AND " +
		    DBNames.FIELD_ID_FINCA_EXPROPIACIONES + " = '" + getIDFinca() + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		if (rs.getObject(1) != null) {
		    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
		    Double doubleValue = rs.getDouble(1);
		    String doubleAsString = doubleFormat.format(doubleValue);
		    expropiationData[0] = ValueFactory.createValue(doubleAsString);
		}else {
		    expropiationData[0] = null;
		}
		if (rs.getObject(2) != null) {
		    expropiationData[1] = ValueFactory.createValue(rs.getString(2));
		}else {
		    expropiationData[1] = null;
		}
		tableModel.addRow(expropiationData);
		//Save current Fincas in order to remove them
		//from database when there is some change in the table model
		//oldExpropiations.add(rs.getString(1));
	    }
	    repaint();
	    tableModel.addTableModelListener(this);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private void updateReversionsTable() {
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
		    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
		    Double doubleValue = rs.getDouble(2);
		    String doubleAsString = doubleFormat.format(doubleValue);
		    reversionData[1] = ValueFactory.createValue(doubleAsString);
		}else {
		    reversionData[1] = null;
		}
		if (rs.getObject(3) != null) {
		    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
		    Double doubleValue = rs.getDouble(3);
		    String doubleAsString = doubleFormat.format(doubleValue);
		    reversionData[2] = ValueFactory.createValue(doubleAsString);
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
	    Value[] pmData = new Value[3];
	    PreparedStatement statement;
	    String query = "SELECT " +
		    DBNames.FIELD_NUMPM_FINCAS_PM + " " +
		    "FROM " + DBNames.PM_SCHEMA + "." + DBNames.TABLE_FINCAS_PM + " " +
		    "WHERE " + DBNames.FIELD_IDFINCA_FINCAS_PM + " = '" + getIDFinca() +
		    "';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		pmData[0] = ValueFactory.createValue(rs.getString(1));
		tableModel.addRow(pmData);
		afectado_pm.setSelectedIndex(1);
	    }
	    repaint();
	    //tableModel.addTableModelListener(this);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public boolean saveRecord() {
	saveReversionsTable();
	saveExpropiationsTable();
	return super.saveRecord();
    }

    private void saveReversionsTable() {
	PreparedStatement statement;
	String query = null;
	String idReversion = null;
	String superficie;
	String importe;

	// Check if ID reversion exists into reversions table
	for (int i=0; i<reversiones.getRowCount(); i++) {
	    idReversion = reversiones.getModel().getValueAt(i, 0).toString();
	    query = "SELECT " + DBNames.FIELD_IDREVERSION_REVERSIONES + " " +
		    "FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_REVERSIONES + " " +
		    "WHERE " + DBNames.FIELD_IDREVERSION_REVERSIONES + " = '" + idReversion + "';";
	    try {
		statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		if (!rs.next()) {
		    JOptionPane.showMessageDialog(this,
			    "EL ID de Reversión: " + idReversion + " no existe. Modifique los datos para poder guardar.",
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
		    if (superficie.contains(",")) {
			superficie = superficie.replace(",", ".");
		    }
		}else {
		    superficie = null;
		}
		if (reversiones.getModel().getValueAt(i, 2) != null) {
		    importe = reversiones.getModel().getValueAt(i, 2).toString();
		    if (importe.contains(",")) {
			importe = importe.replace(",", ".");
		    }
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
    }

    private void saveExpropiationsTable() {
	PreparedStatement statement;
	String query = null;
	String superficie;
	String cultivo;

	// First, we remove old Expropiations on this Finca
	try {
	    query = "DELETE FROM " +
		    DBNames.SCHEMA_DATA + "." + DBNames.TABLE_EXPROPIACIONES + " " +
		    "WHERE " + DBNames.FIELD_ID_FINCA_EXPROPIACIONES + " = '" +
		    getIDFinca() + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	// Now, we save into database current state of JTable
	for (int i=0; i<expropiaciones.getRowCount(); i++) {
	    try {
		if (expropiaciones.getModel().getValueAt(i, 0) != null) {
		    superficie = expropiaciones.getModel().getValueAt(i, 0).toString();
		    if (superficie.contains(",")) {
			superficie = superficie.replace(",", ".");
		    }
		}else {
		    superficie = null;
		}
		if (expropiaciones.getModel().getValueAt(i, 1) != null) {
		    cultivo = expropiaciones.getModel().getValueAt(i, 1).toString();
		}else {
		    cultivo = null;
		}
		query = "INSERT INTO " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_EXPROPIACIONES + " " +
			"VALUES ('" + getIDFinca() + "',";
		if (superficie != null) {
		    query = query + " '" + superficie + "',";
		}else {
		    query = query + " null,";
		}
		if (cultivo != null) {
		    String cultivoID = getIDCultivo(cultivo);
		    query = query + " '" + cultivoID + "');";
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

    }

    private String getIDCultivo(String cultivo) {
	String cultivoID = null;
	PreparedStatement statement;
	String query = "SELECT " + DBNames.FIELD_ID_CULTIVO_CULTIVOS + " " +
		"FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_CULTIVOS + " " +
		"WHERE " + DBNames.FIELD_DESCRIPCION_CULTIVOS + " = " +
		"'" + cultivo + "';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareCall(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.next();
	    cultivoID = rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return cultivoID;
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
