package es.icarto.gvsig.extgia.forms.taludes;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.andami.Launcher;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.audasacommons.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extgia.forms.areas_servicio.AreasServicioReconocimientosSubForm;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.EnableComponentBasedOnCheckBox;
import es.icarto.gvsig.extgia.forms.utils.GIAAlphanumericTableHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class TaludesForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "taludes";

    JComboBox tipoTaludWidget;
    JTextField numeroTaludWidget;
    JTextField taludIDWidget;
    CalculateComponentValue taludid;

    private CalculateComponentValue inclinacionMedia;
    private EnableComponentBasedOnCheckBox cunetaPie;
    private EnableComponentBasedOnCheckBox cunetaCabeza;
    private JComboBox tipoViaPI;
    private JComboBox tipoViaPF;
    private DependentComboboxHandler direccionPIDomainHandler;
    private DependentComboboxHandler direccionPFDomainHandler;

    FilesLinkButton filesLinkButton;
    NavTableComponentsPrintButton ntPrintButton;

    boolean hasJustOpened = true;

    public TaludesForm(FLyrVect layer) {
	super(layer);

	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, DBFieldNames.trabajosColWidths,
		this));

	addTableHandler(new GIAAlphanumericTableHandler(
		getReconocimientosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.reconocimientosColNames,
		DBFieldNames.reconocimientosColAlias, null, this,
		AreasServicioReconocimientosSubForm.class));
    }

    private void addNewButtonsToActionsToolBar() {
	// URL reportPath = this.getClass().getClassLoader()
	// .getResource("reports/taludes.jasper");
	// String extensionPath =
	// reportPath.getPath().replace("reports/taludes.jasper", "");
	JPanel actionsToolBar = this.getActionsToolBar();

	filesLinkButton = new FilesLinkButton(this, new FilesLinkData() {

	    @Override
	    public String getRegisterField() {
		return DBFieldNames
			.getPrimaryKey(DBFieldNames.Elements.Taludes);
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
			+ DBFieldNames.Elements.Taludes;

		return baseDirectory;
	    }
	});

	if (hasJustOpened) {
	    actionsToolBar.add(filesLinkButton);
	    hasJustOpened = false;
	}

	// ntPrintButton = new NavTableComponentsPrintButton();
	// JButton printReportB = null;
	// if (!layer.isEditing()) {
	// printReportB = ntPrintButton.getPrintButton(this, extensionPath,
	// reportPath.getPath(),
	// DBFieldNames.TALUDES_TABLENAME, DBFieldNames.ID_TALUD,
	// taludIDWidget.getText());
	// printReportB.setName("printButton");
	// }
	//
	// if (printReportB != null) {
	// for (int i=0; i<this.getActionsToolBar().getComponents().length; i++)
	// {
	// if (getActionsToolBar().getComponents()[i].getName() != null) {
	// if
	// (getActionsToolBar().getComponents()[i].getName().equalsIgnoreCase("printButton"))
	// {
	// this.getActionsToolBar().remove(getActionsToolBar().getComponents()[i]);
	// actionsToolBar.add(printReportB);
	// break;
	// }
	// }
	// }
	// actionsToolBar.add(printReportB);
	// }
	//
	// if (printReportB != null) {
	// actionsToolBar.add(printReportB);
	// }
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	// if (!((JCheckBox)
	// getWidgetComponents().get("cuneta_cabeza")).isSelected()) {
	// cunetaCabeza.setRemoveDependentValues(true);
	// }
	cunetaCabeza.fillSpecificValues();

	cunetaPie.fillSpecificValues();
	direccionPIDomainHandler.updateComboBoxValues();
	direccionPFDomainHandler.updateComboBoxValues();

	addNewButtonsToActionsToolBar();

    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	taludIDWidget = (JTextField) widgets.get(DBFieldNames.ID_TALUD);

	taludid = new TaludesCalculateTaludIDValue(this, getWidgetComponents(),
		DBFieldNames.ID_TALUD, DBFieldNames.TIPO_TALUD,
		DBFieldNames.NUMERO_TALUD, DBFieldNames.BASE_CONTRATISTA);
	taludid.setListeners();

	inclinacionMedia = new TaludesCalculateInclinacionMediaValue(this,
		getWidgetComponents(), DBFieldNames.INCLINACION_MEDIA,
		DBFieldNames.SECTOR_INCLINACION);
	inclinacionMedia.setListeners();

	cunetaCabeza = new EnableComponentBasedOnCheckBox(
		(JCheckBox) getWidgetComponents().get("cuneta_cabeza"),
		getWidgetComponents().get("cuneta_cabeza_revestida"));
	// cunetaCabeza.setRemoveDependentValues(true);

	cunetaCabeza.setListeners();
	cunetaPie = new EnableComponentBasedOnCheckBox(
		(JCheckBox) getWidgetComponents().get("cuneta_pie"),
		getWidgetComponents().get("cuneta_pie_revestida"));
	// cunetaPie.setRemoveDependentValues(true);
	cunetaPie.setListeners();

	JComboBox direccionPI = (JComboBox) getWidgetComponents().get(
		"direccion_pi");
	tipoViaPI = (JComboBox) getWidgetComponents().get("tipo_via");
	direccionPIDomainHandler = new DependentComboboxHandler(this,
		tipoViaPI, direccionPI);
	tipoViaPI.addActionListener(direccionPIDomainHandler);

	JComboBox direccionPF = (JComboBox) getWidgetComponents().get(
		"direccion_pf");
	tipoViaPF = (JComboBox) getWidgetComponents().get("tipo_via_pf");
	direccionPFDomainHandler = new DependentComboboxHandler(this,
		tipoViaPF, direccionPF);
	tipoViaPF.addActionListener(direccionPFDomainHandler);

    }

    @Override
    protected void removeListeners() {
	taludid.removeListeners();
	inclinacionMedia.removeListeners();
	cunetaCabeza.removeListeners();
	cunetaPie.removeListeners();
	tipoViaPI.removeActionListener(direccionPIDomainHandler);
	tipoViaPF.removeActionListener(direccionPFDomainHandler);

	super.removeListeners();
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged().containsKey("id_talud")) {
	    if (taludIDWidget.getText() != "") {
		String query = "SELECT id_talud FROM audasa_extgia.taludes "
			+ " WHERE id_talud = '" + taludIDWidget.getText()
			+ "';";
		PreparedStatement statement = null;
		Connection connection = DBSession.getCurrentSession()
			.getJavaConnection();
		try {
		    statement = connection.prepareStatement(query);
		    statement.execute();
		    ResultSet rs = statement.getResultSet();
		    if (rs.next()) {
			JOptionPane.showMessageDialog(null,
				"El ID está en uso, por favor, escoja otro.",
				"ID en uso", JOptionPane.WARNING_MESSAGE);
			return true;
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return super.validationHasErrors();
    }

    @Override
    public JTable getReconocimientosJTable() {
	return null;
    }

    @Override
    public JTable getTrabajosJTable() {
	return null;
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Taludes.name();
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_talud";
    }

    @Override
    public String getElementIDValue() {
	return taludIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "taludes_imagenes";
    }

}
