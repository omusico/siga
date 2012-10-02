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

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.preferences.GEXPreferences;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.launcher.ILauncherForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormReversions extends AbstractForm implements ILauncherForm, TableModelListener {

    private static final String WIDGET_TABLAFINCASAFECTADAS = "tabla_fincas_afectadas";

    private static FormPanel form;
    private JTable fincasAfectadas;
    private JTextField numeroReversion;
    private FLyrVect layer = null;
    private JTextField idReversion;
    private IDReversionHandler idReversionHandler;
    private JComboBox tramo;

    private AddExpropiationsListener addExpropiationsListener;
    private DeleteExpropiationsListener deleteExpropiationsListener;
    private JButton addExpropiationsButton;
    private JButton deleteExpropiationsButton;

    private FormExpropiationsLauncher expropiationsLauncher;

    public FormReversions(FLyrVect layer) {
	super(layer);
	this.layer = layer;
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

    private void initWindow() {
	viewInfo.setHeight(650);
	viewInfo.setWidth(550);
	viewInfo.setTitle("Expediente de reversiones");
    }

    private void initListeners() {
	expropiationsLauncher = new FormExpropiationsLauncher(this);
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	HashMap<String, JComponent> widgets = getWidgetComponents();

	fincasAfectadas = (JTable) widgets.get(WIDGET_TABLAFINCASAFECTADAS);
	fincasAfectadas.addMouseListener(expropiationsLauncher);

	idReversion = (JTextField) widgets.get(DBNames.FIELD_IDREVERSION_REVERSIONES);
	tramo = (JComboBox) widgets.get(DBNames.FIELD_TRAMO_FINCAS);

	numeroReversion = (JTextField) widgets.get(DBNames.FIELD_NUMEROREVERSION_REVERSIONES);
	idReversionHandler = new IDReversionHandler();
	numeroReversion.addKeyListener(idReversionHandler);

	addExpropiationsListener = new AddExpropiationsListener();
	addExpropiationsButton = (JButton) form.getComponentByName("add_expropiations_button");
	addExpropiationsButton.addActionListener(addExpropiationsListener);

	deleteExpropiationsListener = new DeleteExpropiationsListener();
	deleteExpropiationsButton = (JButton) form.getComponentByName("delete_expropiations_button");
	deleteExpropiationsButton.addActionListener(deleteExpropiationsListener);

    }

    private class IDReversionHandler implements KeyListener{

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
		    new SubFormReversionsAddExpropiations(layer, fincasAfectadas, idReversion.getText());
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
	    String query = "SELECT id_finca, superficie, importe " +
		    "FROM audasa_expropiaciones.fincas_reversiones " +
		    "WHERE id_reversion = '" + getIDReversion() + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		reversionData[0] = ValueFactory.createValue(rs.getString(1));
		reversionData[1] = ValueFactory.createValue(rs.getDouble(2));
		reversionData[2] = ValueFactory.createValue(rs.getInt(3));
		tableModel.addRow(reversionData);
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
	String idFinca;
	String superficie;
	String importe;

	for (int i=0; i<fincasAfectadas.getRowCount(); i++) {
	    try {
		idFinca = fincasAfectadas.getModel().getValueAt(i, 0).toString();
		superficie = fincasAfectadas.getModel().getValueAt(i, 1).toString();
		importe = fincasAfectadas.getModel().getValueAt(i, 2).toString();
		query = "INSERT INTO audasa_expropiaciones.fincas_reversiones " +
			"VALUES ('" + idFinca + "', '" + getIDReversion() + "', '" +
			superficie + "', '" + importe + "');";
		statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
		statement.executeQuery();
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
