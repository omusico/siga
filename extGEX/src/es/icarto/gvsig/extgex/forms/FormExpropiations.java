package es.icarto.gvsig.extgex.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.preferences.PreferencesPage;
import es.icarto.gvsig.extgex.utils.retrievers.LocalizadorFormatter;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelFactory;
import es.icarto.gvsig.navtableforms.launcher.AlphanumericNavTableLauncher;
import es.icarto.gvsig.navtableforms.launcher.ILauncherForm;
import es.icarto.gvsig.navtableforms.launcher.LauncherParams;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.icarto.gvsig.navtableforms.validation.listeners.DependentComboboxesHandler;

public class FormExpropiations extends AbstractForm implements ILauncherForm {

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
    //    private JTextField impTerrenosPdte;
    //    private JTextField impMejorasPdte;
    //    private JTextField impTotalPdte;
    private JTable expropiaciones;
    private JTable reversiones;

    private DependentComboboxesHandler ucDomainHandler;
    private DependentComboboxesHandler ayuntamientoDomainHandler;
    private DependentComboboxesHandler subtramoDomainHandler;
    private UpdateNroFincaHandler updateNroFincaHandler;
    //    private UpdateImpTotalPdteHandler updateImpTotalPdteHandler;

    private AlphanumericNavTableLauncher tableExpropiationsLauncher;
    private FormReversionsLauncher formReversionsLauncher;
    private FLyrVect layer = null;

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
	viewInfo.setWidth(670);
	viewInfo.setTitle("Expediente de expropiaciones");
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("ExpropiationsFileForm");
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    return new FormPanel("expropiaciones.xml");
	}
	return form;
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	// RETRIEVE WIDGETS
	HashMap<String, JComponent> widgets = getWidgetComponents();

	tramo = (JComboBox) widgets.get(DBNames.FIELD_TRAMO_FINCAS);
	uc = (JComboBox) widgets.get(DBNames.FIELD_UC_FINCAS);
	ayuntamiento = (JComboBox) widgets.get(DBNames.FIELD_AYUNTAMIENTO_FINCAS);
	subtramo = (JComboBox) widgets.get(DBNames.FIELD_PARROQUIASUBTRAMO_FINCAS);

	numFinca = (JTextField) widgets.get(DBNames.FIELD_NUMEROFINCA_FINCAS);
	seccion = (JTextField) widgets.get(DBNames.FIELD_SECCION_FINCAS);

	finca = (JTextField) widgets.get(DBNames.FIELD_IDFINCA);
	finca.setEnabled(false);

	//	impTerrenosPdte = (JTextField) widgets.get(DBNames.FIELD_IMPORTEPDTETERRENOS);
	//	impMejorasPdte = (JTextField) widgets.get(DBNames.FIELD_IMPORTEPDTEMEJORAS);
	//	impTotalPdte = (JTextField) widgets.get(DBNames.FIELD_IMPORTEPDTETOTAL);
	//	impTotalPdte.setEnabled(false);

	expropiaciones = (JTable) widgets.get(WIDGET_EXPROPIACIONES);
	reversiones = (JTable) widgets.get(WIDGET_REVERSIONES);

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

	//	updateImpTotalPdteHandler = new UpdateImpTotalPdteHandler();
	//	impTerrenosPdte.addKeyListener(updateImpTotalPdteHandler);
	//	impMejorasPdte.addKeyListener(updateImpTotalPdteHandler);

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

	//	impTerrenosPdte.removeKeyListener(updateImpTotalPdteHandler);
	//	impMejorasPdte.removeKeyListener(updateImpTotalPdteHandler);

	expropiaciones.removeMouseListener(tableExpropiationsLauncher);
	reversiones.removeMouseListener(formReversionsLauncher);
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

    //    private void setImpTotalPdte() {
    //	Double impTotal = getDoubleImpTerrenosPdte()
    //		+ getDoubleImpMejorasPdte();
    //	impTotalPdte.setText(impTotal.toString());
    //	super.setWidgetValues(DBNames.FIELD_IMPORTEPDTETOTAL, impTotal.toString());
    //    }

    public String getIDFinca() {
	return finca.getText();
    }

    //    private Double getDoubleImpTerrenosPdte() {
    //	String impTerrenosValue = impTerrenosPdte.getText();
    //	try {
    //	    return Double.parseDouble(impTerrenosValue);
    //	} catch (NumberFormatException nfe) {
    //	    return 0.0;
    //	}
    //    }

    //    private Double getDoubleImpMejorasPdte() {
    //	String impMejorasValue = impMejorasPdte.getText();
    //	try {
    //	    return Double.parseDouble(impMejorasValue);
    //	} catch (NumberFormatException nfe) {
    //	    return 0.0;
    //	}
    //    }

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

    //    public class UpdateImpTotalPdteHandler implements KeyListener {
    //
    //	@Override
    //	public void keyTyped(KeyEvent e) {
    //	}
    //
    //	@Override
    //	public void keyPressed(KeyEvent e) {
    //	}
    //
    //	@Override
    //	public void keyReleased(KeyEvent e) {
    //	    if (!isFillingValues()) {
    //		setImpTotalPdte();
    //	    }
    //	}
    //
    //    }

    @Override
    protected void fillSpecificValues() {
	ucDomainHandler.updateComboBoxValues();
	ayuntamientoDomainHandler.updateComboBoxValues();
	subtramoDomainHandler.updateComboBoxValues();
	updateJTables();
    }

    private void updateJTables() {
	ArrayList<String> columnasCultivos = new ArrayList<String>();
	columnasCultivos.add(DBNames.FIELD_SUPERFICIE_EXPROPIACIONES);
	columnasCultivos.add(DBNames.FIELD_IDCULTIVO_EXPROPIACIONES);
	try {
	    expropiaciones.setModel(TableModelFactory.createFromTable(
		    DBNames.TABLE_EXPROPIACIONES,
		    DBNames.FIELD_IDFINCA, finca.getText(),
		    columnasCultivos, columnasCultivos));
	} catch (ReadDriverException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	ArrayList<String> columnasReversiones = new ArrayList<String>();
	columnasReversiones.add(DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES);
	columnasReversiones.add(DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES);
	columnasReversiones.add(DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES);
	try {
	    reversiones.setModel(TableModelFactory.createFromTable(
		    DBNames.TABLE_FINCASREVERSIONES,
		    DBNames.FIELD_IDFINCA, finca.getText(),
		    columnasReversiones, columnasReversiones));
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
	return PluginServices.getPluginServices("es.icarto.gvsig.extgex")
	.getClassLoader()
	.getResource(PreferencesPage.XML_ORMLITE_RELATIVE_PATH).getPath();
    }

}
