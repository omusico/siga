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
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.launcher.ILauncherForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormReversions extends AbstractForm implements ILauncherForm, TableModelListener {

    private static final String WIDGET_TABLAFINCASAFECTADAS = "tabla_fincas_afectadas";

    private FLyrVect layer = null;
    private IGeometry insertedGeom;

    private static FormPanel form;
    private JTable fincasAfectadas;
    private JTextField numeroReversion;
    private JTextField idReversion;
    private IDReversionHandler idReversionHandler;
    private JComboBox tramo;
    private JComboBox ayuntamiento;
    private String tramoSelected;

    private TramosListener tramosListener;

    private AddExpropiationsListener addExpropiationsListener;
    private DeleteExpropiationsListener deleteExpropiationsListener;
    private JButton addExpropiationsButton;
    private JButton deleteExpropiationsButton;

    private FormExpropiationsLauncher expropiationsLauncher;

    private ArrayList<String> oldFincasAfectadas;

    public FormReversions(FLyrVect layer, IGeometry insertedGeom) {
	super(layer);
	this.layer = layer;
	if (insertedGeom != null) {
	    this.insertedGeom = insertedGeom;
	}
	initWindow();
	initListeners();
	addNewButtonsToActionsToolBar();
    }

    private void addNewButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();
	NavTableComponentsFactory ntFactory = new NavTableComponentsFactory();
	JButton filesLinkB = ntFactory.getFilesLinkButton(layer,
		this);
	if (filesLinkB != null) {
	    actionsToolBar.add(filesLinkB);
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
	viewInfo.setHeight(650);
	viewInfo.setWidth(600);
	viewInfo.setTitle("Expediente de reversiones");
    }

    private void initListeners() {
	expropiationsLauncher = new FormExpropiationsLauncher(this);

    }

    @Override
    protected void setListeners() {
	super.setListeners();

	HashMap<String, JComponent> widgets = getWidgetComponents();

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	fincasAfectadas = (JTable) widgets.get(WIDGET_TABLAFINCASAFECTADAS);
	fincasAfectadas.addMouseListener(expropiationsLauncher);

	idReversion = (JTextField) widgets.get(DBNames.FIELD_IDREVERSION_REVERSIONES);
	tramosListener = new TramosListener();
	tramo = (JComboBox) widgets.get(DBNames.FIELD_TRAMO_FINCAS);
	tramo.addActionListener(tramosListener);
	ayuntamiento = (JComboBox) widgets.get(DBNames.FIELD_AYUNTAMIENTO_REVERSIONES);

	numeroReversion = (JTextField) widgets.get(DBNames.FIELD_NUMEROREVERSION_REVERSIONES);
	idReversionHandler = new IDReversionHandler();
	numeroReversion.addKeyListener(idReversionHandler);
	tramo.addActionListener(idReversionHandler);

	addExpropiationsListener = new AddExpropiationsListener();
	addExpropiationsButton = (JButton) form.getComponentByName(DBNames.REVERSIONS_ADD_EXPROPIATIONS_BUTTON);
	addExpropiationsButton.addActionListener(addExpropiationsListener);

	deleteExpropiationsListener = new DeleteExpropiationsListener();
	deleteExpropiationsButton = (JButton) form.getComponentByName(DBNames.REVERSIONS_DELETE_EXPROPIATIONS_BUTTON);
	deleteExpropiationsButton.addActionListener(deleteExpropiationsListener);

    }

    public class TramosListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    updateAyuntamientoDependingOnTramo();
	}
    }

    private void updateAyuntamientoDependingOnTramo() {
	if (!isFillingValues()) {
	    ayuntamiento.removeAllItems();
	    ayuntamiento.addItem(" ");
	    String query = null;
	    PreparedStatement statement;
	    try {
		query = "SELECT distinct(" + DBNames.FIELD_NOMBREAYUNTAMIENTO_AYUNTAMIENTO + ") " +
			"FROM " + DBNames.EXPROPIATIONS_SCHEMA + "." + DBNames.TABLE_TRAMOS + " t, " +
			DBNames.EXPROPIATIONS_SCHEMA + "." + DBNames.TABLE_AYUNTAMIENTOS + " a, " +
			DBNames.EXPROPIATIONS_SCHEMA + "." + DBNames.TABLE_UC + " u " +
			"WHERE t." + DBNames.FIELD_IDTRAMO_TRAMOS + "= u." + DBNames.FIELD_IDTRAMO_UC +
			" AND u." + DBNames.FIELD_IDUC_UC + "= a." +DBNames.FIELD_IDUC_AYUNTAMIENTO +
			" AND " + DBNames.FIELD_NOMBRETRAMO_TRAMOS + " = '" + tramo.getSelectedItem() + "';";
		statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		while (rs.next()) {
		    ayuntamiento.addItem(rs.getString(1));
		}
		rs.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    private class IDReversionHandler implements KeyListener, ActionListener{

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
	    if(!isFillingValues()) {
		setIDReversion();
	    }
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if(!isFillingValues()) {
		setIDReversion();
	    }
	}
    }

    private void setIDReversion() {
	idReversion.setText(formatIDReversion());
	getFormController().setValue(DBNames.FIELD_IDREVERSION_REVERSIONES,
		idReversion.getText());
    }

    private String getIDReversion() {
	return idReversion.getText();
    }

    private String formatIDReversion() {
	String num_rev;
	try {
	    num_rev = String.format("%1$04d", Integer.parseInt(numeroReversion.getText()));
	} catch (NumberFormatException nfe) {
	    num_rev = "0000";
	    System.out.print(nfe.getMessage());
	}
	numeroReversion.setText(num_rev);
	getFormController().setValue(DBNames.FIELD_NUMEROREVERSION_REVERSIONES,
		numeroReversion.getText());
	return ((KeyValue) tramo.getSelectedItem()).getKey()+
		numeroReversion.getText();
    }

    @Override
    protected void removeListeners() {
	super.removeListeners();
	fincasAfectadas.removeMouseListener(expropiationsLauncher);

	addExpropiationsButton.removeActionListener(addExpropiationsListener);
	deleteExpropiationsButton.removeActionListener(deleteExpropiationsListener);
    }

    public class AddExpropiationsListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    SubFormReversionsAddExpropiations subForm =
		    new SubFormReversionsAddExpropiations(layer, fincasAfectadas, idReversion.getText(), insertedGeom);
	    PluginServices.getMDIManager().addWindow(subForm);
	}

    }

    public class DeleteExpropiationsListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    int[] selectedRows = fincasAfectadas.getSelectedRows();
	    DefaultTableModel model = (DefaultTableModel) fincasAfectadas.getModel();

	    for (int i=0; i<selectedRows.length; i++) {
		int rowIndex = selectedRows[i];
		model.removeRow(rowIndex);
		repaint();
	    }
	}
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("ReversionsFileForm");
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    InputStream stream = getClass().getClassLoader().getResourceAsStream("reversiones.xml");
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
    protected void fillSpecificValues() {
	updateJTableFincasAfectadas();
    }

    private void updateJTableFincasAfectadas() {
	oldFincasAfectadas = new ArrayList<String>();

	ArrayList<String> columnasFincas = new ArrayList<String>();
	columnasFincas.add(DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES);
	columnasFincas.add(DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES);
	columnasFincas.add(DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES);

	try {
	    DefaultTableModel tableModel;
	    tableModel = new DefaultTableModel();
	    for (String columnName : columnasFincas) {
		tableModel.addColumn(columnName);
	    }
	    fincasAfectadas.setModel(tableModel);
	    Value[] reversionData = new Value[3];
	    PreparedStatement statement;
	    String query = "SELECT " +
		    DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES + ", " +
		    DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES + ", " +
		    DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES + " " +
		    "FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCASREVERSIONES + " " +
		    "WHERE " + DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES + " = '" + getIDReversion() + "';";
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
		oldFincasAfectadas.add(rs.getString(1));
	    }
	    repaint();
	    tableModel.addTableModelListener(this);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public boolean saveRecord() {
	saveFincasAfectadasTable();
	return super.saveRecord();
    }

    private void saveFincasAfectadasTable() {
	PreparedStatement statement;
	String query = null;
	String idFinca;
	String superficie;
	String importe;

	// Check if ID expropiation exists into expropiations table
	for (int i=0; i<fincasAfectadas.getRowCount(); i++) {
	    idFinca = fincasAfectadas.getModel().getValueAt(i, 0).toString();
	    query = "SELECT " + DBNames.FIELD_ID_FINCA_EXPROPIACIONES + " " +
		    "FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_EXPROPIACIONES + " " +
		    "WHERE " + DBNames.FIELD_ID_FINCA_EXPROPIACIONES + " = '" + idFinca + "';";
	    try {
		statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		if (!rs.next()) {
		    JOptionPane.showMessageDialog(this,
			    "EL ID de Finca: " + idFinca + " no existe. Modifique los datos para poder guardar.",
			    "Error en los datos",
			    JOptionPane.ERROR_MESSAGE);
		    return;
		}
	    } catch (SQLException e) {
		e.getMessage();
	    }
	}

	// First, we remove old Fincas on this reversions
	for (String FincaID : oldFincasAfectadas) {
	    try {
		query = "DELETE FROM " +
			DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCASREVERSIONES + " " +
			"WHERE " + DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES + " = '"
			+ FincaID + "' AND " + DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES +
			" = '" + getIDReversion() + "';";
		statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
		statement.execute();
	    } catch (SQLException e) {
		e.printStackTrace();
		continue;
	    }
	}

	// Now, we save into database current state of JTable
	for (int i=0; i<fincasAfectadas.getRowCount(); i++) {
	    idFinca = fincasAfectadas.getModel().getValueAt(i, 0).toString();
	    if (fincasAfectadas.getModel().getValueAt(i, 1) != null) {
		superficie = fincasAfectadas.getModel().getValueAt(i, 1).toString();
	    }else {
		superficie = null;
	    }
	    if (fincasAfectadas.getModel().getValueAt(i, 2) != null) {
		importe = fincasAfectadas.getModel().getValueAt(i, 2).toString();
	    }else {
		importe = null;
	    }
	    try {
		query = "INSERT INTO " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCASREVERSIONES + " " +
			"VALUES ('" + idFinca + "', '" + getIDReversion() + "',";
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
	updateJTableFincasAfectadas();
    }

    @Override
    public String getSQLQuery(String queryID) {
	return null;
    }

    @Override
    public String getXMLPath() {
	return GEXPreferences.getPreferences().getXMLFilePath();
    }

    @Override
    public void tableChanged(TableModelEvent arg0) {
	super.setChangedValues(true);
	super.saveB.setEnabled(true);
    }

}
