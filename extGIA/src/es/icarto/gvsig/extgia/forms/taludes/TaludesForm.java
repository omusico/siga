package es.icarto.gvsig.extgia.forms.taludes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;
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
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.audasacommons.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.EnableComponentBasedOnCheckBox;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.validation.listeners.DependentComboboxesHandler;

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
    private DependentComboboxesHandler direccionPIDomainHandler;
    private DependentComboboxesHandler direccionPFDomainHandler;

    FilesLinkButton filesLinkButton;
    NavTableComponentsPrintButton ntPrintButton;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    public TaludesForm(FLyrVect layer) {
	super(layer);
	initWindow();
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
		return ORMLite.getDataBaseObject(getXMLPath()).getTable("taludes").getPrimaryKey()[0];
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
			+ "taludes";

		return baseDirectory;
	    }
	});
	actionsToolBar.add(filesLinkButton);
	ntPrintButton = new NavTableComponentsPrintButton();
	JButton printReportB = null;
	if (!layer.isEditing()) {
	    printReportB = ntPrintButton.getPrintButton(this, extensionPath, reportPath.getPath(),
		    DBFieldNames.TALUDES_TABLENAME, DBFieldNames.ID_TALUD, taludIDWidget.getText());
	}
	if (printReportB != null) {
	    actionsToolBar.add(printReportB);
	}
    }

    @Override
    protected void initWindow() {
	super.initWindow();
	this.viewInfo.setTitle("Taludes");
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
	cunetaCabeza.fillSpecificValues();
	cunetaPie.fillSpecificValues();
	direccionPIDomainHandler.updateComboBoxValues();
	direccionPFDomainHandler.updateComboBoxValues();

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	int[] trabajoColumnsSize = {1, 1, 120, 60, 60};
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", "taludes_reconocimiento_estado",
		DBFieldNames.reconocimientoEstadoFields, null, "id_talud", taludIDWidget.getText());
	SqlUtils.createEmbebedTableFromDB(trabajos,
		"audasa_extgia", "taludes_trabajos",
		DBFieldNames.trabajoFields, trabajoColumnsSize, "id_talud", taludIDWidget.getText());
    }

    protected void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

	taludIDWidget = (JTextField) widgets.get(DBFieldNames.ID_TALUD);

	ImageComponent image = (ImageComponent) super.getFormBody().getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	taludid = new CalculateTaludIDValue(this, getWidgetComponents(),
		DBFieldNames.ID_TALUD, DBFieldNames.TIPO_TALUD,
		DBFieldNames.NUMERO_TALUD, DBFieldNames.BASE_CONTRATISTA);
	taludid.setListeners();

	inclinacionMedia = new CalculateInclinacionMediaValue(this,
		getWidgetComponents(), DBFieldNames.INCLINACION_MEDIA,
		DBFieldNames.SECTOR_INCLINACION);
	inclinacionMedia.setListeners();

	cunetaCabeza = new EnableComponentBasedOnCheckBox(
		(JCheckBox) getWidgetComponents().get("cuneta_cabeza"),
		getWidgetComponents().get("cuneta_cabeza_revestida"));
	cunetaCabeza.setRemoveDependentValues(true);
	cunetaCabeza.setListeners();
	cunetaPie = new EnableComponentBasedOnCheckBox(
		(JCheckBox) getWidgetComponents().get("cuneta_pie"),
		getWidgetComponents().get("cuneta_pie_revestida"));
	cunetaPie.setRemoveDependentValues(true);
	cunetaPie.setListeners();

	JComboBox direccionPI = (JComboBox) getWidgetComponents().get(
		"direccion_pi");
	tipoViaPI = (JComboBox) getWidgetComponents().get("tipo_via");
	direccionPIDomainHandler = new DependentComboboxesHandler(this,
		tipoViaPI, direccionPI);
	tipoViaPI.addActionListener(direccionPIDomainHandler);

	JComboBox direccionPF = (JComboBox) getWidgetComponents().get(
		"direccion_pf");
	tipoViaPF = (JComboBox) getWidgetComponents().get("tipo_via_pf");
	direccionPFDomainHandler = new DependentComboboxesHandler(this,
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
	    TaludesReconocimientosSubForm subForm =
		    new TaludesReconocimientosSubForm(
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
		TaludesReconocimientosSubForm subForm =
			new TaludesReconocimientosSubForm(
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
}
