package es.icarto.gvsig.extgex.forms;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.preferences.GEXPreferences;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelFactory;
import es.icarto.gvsig.navtableforms.launcher.ILauncherForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class FormReversions extends AbstractForm implements ILauncherForm {

    private static final String WIDGET_TABLAFINCASAFECTADAS = "tabla_fincas_afectadas";

    private static FormPanel form;
    private JTable fincasAfectadas;
    private JTextField numeroReversion;
    private FLyrVect layer = null;
    private JTextField idReversion;
    private IDReversionHandler idReversionHandler;
    private JComboBox tramo;

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
	viewInfo.setWidth(630);
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
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("ReversionsFileForm");
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    return new FormPanel("reversiones.xml");
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
	    fincasAfectadas.setModel(TableModelFactory.createFromTable(
		    DBNames.TABLE_FINCASREVERSIONES,
		    DBNames.FIELD_IDREVERSION_REVERSIONES, formatIDReversion(),
		    columnasFincas, columnasFincas));
	} catch (ReadDriverException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
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

}
