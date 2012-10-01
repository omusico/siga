package es.icarto.gvsig.extgia.forms.isletas;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.andami.Launcher;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.audasacommons.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extgia.navtableforms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.navtableforms.utils.EnableComponentBasedOnCheckBox;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.validation.listeners.DependentComboboxesHandler;

@SuppressWarnings("serial")
public class IsletasForm extends AbstractForm {

    public static final String ABEILLE_FILENAME = "forms/isletas.xml";
    private FormPanel form;
    JComboBox tipoIsletaWidget;
    JTextField numeroIsletaWidget;
    JComboBox baseContratistaWidget;
    JTextField isletaIDWidget;
    CalculateComponentValue isletaid;
    private JComboBox tipoVia;
    private DependentComboboxesHandler direccionDomainHandler;

    public IsletasForm(FLyrVect layer) {
	super(layer);
	initWindow();

	//addNewButtonsToActionsToolBar();
    }

    private void addNewButtonsToActionsToolBar() {
	URL reportPath = this.getClass().getClassLoader()
		.getResource("reports/isletas.jasper");
	String extensionPath = reportPath.getPath().replace("reports/isletas.jasper", "");
	JPanel actionsToolBar = this.getActionsToolBar();
	
FilesLinkButton filesLinkButton = new FilesLinkButton(this, new FilesLinkData() {
		
		@Override
		public String getRegisterField() {
			return ORMLite.getDataBaseObject(getXMLPath()).getTable("isletas").getPrimaryKey()[0];
		}
		
		@Override
		public String getBaseDirectory() {
			String baseDirectory = null;
			try {
			    baseDirectory = PreferencesPage.getBaseDirectory();
			} catch (Exception e) {
			}

			if (baseDirectory == null || baseDirectory.isEmpty()) {
			    baseDirectory = Launcher.getAppHomeDir();
			}

			baseDirectory = baseDirectory + File.separator + "FILES"
				+ File.separator + "inventario" + File.separator
				+ "isletas";
		
			return baseDirectory;
		}
	});
	actionsToolBar.add(filesLinkButton);
	NavTableComponentsPrintButton ntPrintButton = new NavTableComponentsPrintButton();
	JButton printReportB = ntPrintButton.getPrintButton(this, extensionPath,
		reportPath.getPath());
	if (printReportB != null) {
	    actionsToolBar.add(printReportB);
	}
    }

    private void initWindow() {
	this.viewInfo.setHeight(570);
	this.viewInfo.setWidth(732);
	this.viewInfo.setTitle("Isletas");
    }

    @Override
    public FormPanel getFormBody() {
	if (this.form == null) {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(IsletasForm.ABEILLE_FILENAME);
	    try {
			this.form = new FormPanel(stream);
		} catch (FormException e) {
			e.printStackTrace();
		}
	}
	return this.form;
    }

    @Override
    public String getXMLPath() {
	return Preferences.getPreferences().getXMLFilePath();
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void fillSpecificValues() {
	direccionDomainHandler.updateComboBoxValues();
	
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	isletaid = new CalculateIsletaIDValue(this, getWidgetComponents(),
		DBFieldNames.ID_ISLETA, DBFieldNames.TIPO_ISLETA,
		DBFieldNames.NUMERO_ISLETA, DBFieldNames.BASE_CONTRATISTA);
	isletaid.setListeners();

	JComboBox direccion = (JComboBox) getWidgetComponents().get(
		"direccion");
	tipoVia = (JComboBox) getWidgetComponents().get("tipo_via");
	direccionDomainHandler = new DependentComboboxesHandler(this,
		tipoVia, direccion);
	tipoVia.addActionListener(direccionDomainHandler);

    }

    @Override
    protected void removeListeners() {
	isletaid.removeListeners();
	tipoVia.removeActionListener(direccionDomainHandler);
	super.removeListeners();
    }


}
