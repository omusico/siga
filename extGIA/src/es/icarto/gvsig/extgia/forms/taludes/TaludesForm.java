package es.icarto.gvsig.extgia.forms.taludes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.audasacommons.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.EnableComponentBasedOnCheckBox;
import es.icarto.gvsig.extgia.forms.utils.LaunchGIAForms.Elements;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class TaludesForm extends AbstractFormWithLocationWidgets {

    public static String ABEILLE_FILENAME = "forms/taludes.xml";
    public static final String ABEILLE_RECONOCIMIENTOS_FILENAME = "forms/taludes_reconocimiento_estado.xml";
    public static final String ABEILLE_TRABAJOS_FILENAME = "forms/taludes_trabajos.xml";

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

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    boolean hasJustOpened = true;

    public TaludesForm(FLyrVect layer) {
	super(layer);
	initListeners();
    }

    @Override
    public String getFormBodyPath() {
	return ABEILLE_FILENAME;
    }

    private void addNewButtonsToActionsToolBar() {
	URL reportPath = this.getClass().getClassLoader()
		.getResource("reports/taludes.jasper");
	String extensionPath = reportPath.getPath().replace("reports/taludes.jasper", "");
	JPanel actionsToolBar = this.getActionsToolBar();

	filesLinkButton = new FilesLinkButton(this, new FilesLinkData() {

	    @Override
	    public String getRegisterField() {
		return DBFieldNames.getPrimaryKey(Elements.Taludes);
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
			+ Elements.Taludes;

		return baseDirectory;
	    }
	});

	if (hasJustOpened) {
	    actionsToolBar.add(filesLinkButton);
	    hasJustOpened = false;
	}

	ntPrintButton = new NavTableComponentsPrintButton();
	JButton printReportB = null;
	if (!layer.isEditing()) {
	    printReportB = ntPrintButton.getPrintButton(this, extensionPath, reportPath.getPath(),
		    DBFieldNames.TALUDES_TABLENAME, DBFieldNames.ID_TALUD, taludIDWidget.getText());
	    printReportB.setName("printButton");
	}

	if (printReportB != null) {
	    for (int i=0; i<this.getActionsToolBar().getComponents().length; i++) {
		if (getActionsToolBar().getComponents()[i].getName() != null) {
		    if (getActionsToolBar().getComponents()[i].getName().equalsIgnoreCase("printButton")) {
			this.getActionsToolBar().remove(getActionsToolBar().getComponents()[i]);
			actionsToolBar.add(printReportB);
			break;
		    }
		}
	    }
	    actionsToolBar.add(printReportB);
	}

	if (printReportB != null) {
	    actionsToolBar.add(printReportB);
	}
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/taludes_metadata.xml")
		.getPath();
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	//	if (!((JCheckBox) getWidgetComponents().get("cuneta_cabeza")).isSelected()) {
	//	    cunetaCabeza.setRemoveDependentValues(true);
	//	}
	cunetaCabeza.fillSpecificValues();

	cunetaPie.fillSpecificValues();
	direccionPIDomainHandler.updateComboBoxValues();
	direccionPFDomainHandler.updateComboBoxValues();

	addNewButtonsToActionsToolBar();

	// Embebed Tables
	int[] trabajoColumnsSize = {1, 1, 110, 70, 60};
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", "taludes_reconocimiento_estado",
		DBFieldNames.reconocimientoEstadoFields, null, "id_talud", taludIDWidget.getText(), "n_inspeccion");
	SqlUtils.createEmbebedTableFromDB(trabajos,
		"audasa_extgia", "taludes_trabajos",
		DBFieldNames.trabajoFields, trabajoColumnsSize, "id_talud", taludIDWidget.getText(), "id_trabajo");

	revalidate();
	repaint();
    }

    protected void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

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
	//cunetaCabeza.setRemoveDependentValues(true);

	cunetaCabeza.setListeners();
	cunetaPie = new EnableComponentBasedOnCheckBox(
		(JCheckBox) getWidgetComponents().get("cuneta_pie"),
		getWidgetComponents().get("cuneta_pie_revestida"));
	//cunetaPie.setRemoveDependentValues(true);
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

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	addTrabajoListener = new AddTrabajoListener();
	addTrabajoButton.addActionListener(addTrabajoListener);
	editTrabajoListener = new EditTrabajoListener();
	editTrabajoButton.addActionListener(editTrabajoListener);
	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton.addActionListener(deleteReconocimientoListener);
	deleteTrabajoListener = new DeleteTrabajoListener();
	deleteTrabajoButton.addActionListener(deleteTrabajoListener);

    }

    @Override
    protected void removeListeners() {
	taludid.removeListeners();
	inclinacionMedia.removeListeners();
	cunetaCabeza.removeListeners();
	cunetaPie.removeListeners();
	tipoViaPI.removeActionListener(direccionPIDomainHandler);
	tipoViaPF.removeActionListener(direccionPFDomainHandler);
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton.removeActionListener(editReconocimientoListener);
	addTrabajoButton.removeActionListener(addTrabajoListener);
	editTrabajoButton.removeActionListener(editTrabajoListener);
	deleteReconocimientoButton.removeActionListener(deleteReconocimientoListener);
	deleteTrabajoButton.removeActionListener(deleteTrabajoListener);
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

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    TaludesReconocimientosSubForm subForm =
		    new TaludesReconocimientosSubForm(
			    ABEILLE_RECONOCIMIENTOS_FILENAME,
			    getReconocimientosDBTableName(),
			    reconocimientoEstado,
			    "id_talud",
			    taludIDWidget.getText(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    TaludesTrabajosSubForm subForm = new TaludesTrabajosSubForm(
		    ABEILLE_TRABAJOS_FILENAME,
		    getTrabajosDBTableName(),
		    trabajos,
		    "id_talud",
		    taludIDWidget.getText(),
		    null,
		    null,
		    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		TaludesReconocimientosSubForm subForm =
			new TaludesReconocimientosSubForm(
				ABEILLE_RECONOCIMIENTOS_FILENAME,
				getReconocimientosDBTableName(),
				reconocimientoEstado,
				"id_talud",
				taludIDWidget.getText(),
				"n_inspeccion",
				reconocimientoEstado.getValueAt(row, 0).toString(),
				true);
		PluginServices.getMDIManager().addWindow(subForm);
	    }else {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }

    public class EditTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (trabajos.getSelectedRowCount() != 0) {
		int row = trabajos.getSelectedRow();
		TaludesTrabajosSubForm subForm = new TaludesTrabajosSubForm(
			ABEILLE_TRABAJOS_FILENAME,
			getTrabajosDBTableName(),
			trabajos,
			"id_talud",
			taludIDWidget.getText(),
			"id_trabajo",
			trabajos.getValueAt(row, 0).toString(),
			true);
		PluginServices.getMDIManager().addWindow(subForm);
	    }else {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }

    public class DeleteReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(reconocimientoEstado, getReconocimientosDBTableName(),
		    getReconocimientosIDField());
	}
    }

    public class DeleteTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(trabajos, getTrabajosDBTableName(), getTrabajosIDField());
	}
    }

    @Override
    public JTable getReconocimientosJTable() {
	return reconocimientoEstado;
    }

    @Override
    public JTable getTrabajosJTable() {
	return trabajos;
    }

    @Override
    public String getReconocimientosDBTableName() {
	return "taludes_reconocimiento_estado";
    }

    @Override
    public String getTrabajosDBTableName() {
	return "taludes_trabajos";
    }

    @Override
    public boolean isSpecialCase() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    protected String getBasicName() {
	return "Taludes";
    }

    @Override
    public String getElement() {
	return Elements.Taludes.name();
    }

    @Override
    public String getReconocimientosFormFileName() {
	return ABEILLE_RECONOCIMIENTOS_FILENAME;
    }

    @Override
    public String getTrabajosFormFileName() {
	return ABEILLE_TRABAJOS_FILENAME;
    }
}
