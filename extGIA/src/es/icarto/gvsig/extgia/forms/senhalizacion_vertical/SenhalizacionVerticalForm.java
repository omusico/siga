package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.net.URL;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.audasacommons.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.GIAAlphanumericTableHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;

@SuppressWarnings("serial")
public class SenhalizacionVerticalForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "senhalizacion_vertical";

    JTextField elementoSenhalizacionIDWidget;
    CalculateComponentValue elementoSenhalizacionid;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    private JButton printReportB;

    private URL reportPath;

    private String extensionPath;

    private PrintSVReportObserver printListener;

    public static String[] senhalesColNames = { "id_senhal_vertical",
	"tipo_senhal", "codigo_senhal", "leyenda", "fecha_instalacion",
	"fecha_reposicion"};

    public static String[] senhalesColAlias = { "ID Señal", "Tipo Señal",
	"Código Señal", "Leyenda", "Instalación", "Reposición"};

    public SenhalizacionVerticalForm(FLyrVect layer) {
	super(layer);

	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosVegetacionColNames,
		DBFieldNames.trabajosVegetacionColAlias, DBFieldNames.trabajosColWidths,
		this));

	addTableHandler(new GIAAlphanumericTableHandler(
		getReconocimientosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.reconocimientosColNames,
		DBFieldNames.reconocimientosColAlias, null, this,
		SenhalizacionVerticalReconocimientosSubForm.class));

	addTableHandler(new GIAAlphanumericTableHandler(
		"senhalizacion_vertical_senhales", getWidgetComponents(),
		getElementID(), senhalesColNames, senhalesColAlias, new int[] {
			20, 45, 45, 180, 40, 40 }, this));
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (elementoSenhalizacionIDWidget.getText().isEmpty()) {
	    elementoSenhalizacionid = new SenhalizacionVerticalCalculateIDValue(
		    this, getWidgetComponents(),
		    DBFieldNames.ID_ELEMENTO_SENHALIZACION,
		    DBFieldNames.ID_ELEMENTO_SENHALIZACION);
	    elementoSenhalizacionid.setValue(true);
	}

	addNewButtonsToActionsToolBar();
    }

    private void addNewButtonsToActionsToolBar() {
	if (filesLinkButton == null) {
	    super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Senhalizacion_Vertical);
	}

	if (printReportB == null) {
	    addPrintButton();
	}
	printReportB.removeActionListener(printListener);

	printListener = new PrintSVReportObserver(this, extensionPath,
		reportPath.getPath(), getElementID(), getPrimaryKeyValue());
	printReportB.addActionListener(printListener);
    }

    public ImageIcon getPrintIcon() {
	java.net.URL imgURL = this.getClass().getClassLoader()
		.getResource("images/print-report.png");
	ImageIcon icon = new ImageIcon(imgURL);
	return icon;
    }

    private void addPrintButton() {
	reportPath = this.getClass().getClassLoader()
		.getResource("reports/senhalizacion_vertical.jasper");
	extensionPath = reportPath.getPath().replace(
		"reports/senhalizacion_vertical.jasper", "");
	JPanel actionsToolBar = this.getActionsToolBar();

	NavTableComponentsPrintButton ntPrintButton = new NavTableComponentsPrintButton();

	printListener = new PrintSVReportObserver(this, extensionPath,
		reportPath.getPath(), getElementID(), getPrimaryKeyValue());
	printReportB = ntPrintButton.createButton(
		PluginServices.getText(this, "printReportsToolTip"),
		getPrintIcon(), printListener);

	printReportB.setName("printButton");
	actionsToolBar.add(printReportB);
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	elementoSenhalizacionIDWidget = (JTextField) widgets
		.get(DBFieldNames.ID_ELEMENTO_SENHALIZACION);

	JComboBox direccion = (JComboBox) widgets.get("direccion");
	tipoVia = (JComboBox) getWidgets().get("tipo_via");
	direccionDomainHandler = new DependentComboboxHandler(this, tipoVia,
		direccion);
	tipoVia.addActionListener(direccionDomainHandler);
    }

    @Override
    protected void removeListeners() {
	tipoVia.removeActionListener(direccionDomainHandler);

	super.removeListeners();
    }

    @Override
    public JTable getReconocimientosJTable() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JTable getTrabajosJTable() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Senhalizacion_Vertical.name();
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_elemento_senhalizacion";
    }

    @Override
    public String getElementIDValue() {
	return elementoSenhalizacionIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "senhalizacion_vertical_imagenes";
    }

}
