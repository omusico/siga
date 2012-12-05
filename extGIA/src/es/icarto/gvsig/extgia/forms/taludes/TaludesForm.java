package es.icarto.gvsig.extgia.forms.taludes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
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
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.audasacommons.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.navtableforms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.navtableforms.utils.EnableComponentBasedOnCheckBox;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.validation.listeners.DependentComboboxesHandler;

@SuppressWarnings("serial")
public class TaludesForm extends AbstractFormWithLocationWidgets {

    public static final String ABEILLE_FILENAME = "forms/taludes.xml";
    private FormPanel form;
    JComboBox tipoTaludWidget;
    JTextField numeroTaludWidget;
    JTextField taludIDWidget;
    CalculateComponentValue taludid;

    JTable reconocimientoEstado;
    JTable trabajos;
    JButton addReconocimientoButton;
    JButton editReconocimientoButton;
    JButton deleteReconocimientoButton;
    JButton addTrabajoButton;
    JButton editTrabajoButton;
    JButton deleteTrabajoButton;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    private CalculateComponentValue inclinacionMedia;
    private EnableComponentBasedOnCheckBox cunetaPie;
    private EnableComponentBasedOnCheckBox cunetaCabeza;
    private JComboBox tipoViaPI;
    private JComboBox tipoViaPF;
    private DependentComboboxesHandler direccionPIDomainHandler;
    private DependentComboboxesHandler direccionPFDomainHandler;

    FilesLinkButton filesLinkButton;
    NavTableComponentsPrintButton ntPrintButton;

    public TaludesForm(FLyrVect layer) {
	super(layer);
	initWindow();
	initListeners();
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
    public FormPanel getFormBody() {
	if (this.form == null) {
	    InputStream stream = getClass().getClassLoader().getResourceAsStream(TaludesForm.ABEILLE_FILENAME);
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

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
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

	reconocimientoEstado = (JTable) widgets.get("taludes_reconocimiento_estado");
	trabajos = (JTable) widgets.get("taludes_trabajos");

	addReconocimientoButton = (JButton) form.getComponentByName("add_reconocimiento_button");
	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	editReconocimientoButton = (JButton) form.getComponentByName("edit_reconocimiento_button");
	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	addTrabajoButton = (JButton) form.getComponentByName("add_trabajo_button");
	addTrabajoListener = new AddTrabajoListener();
	addTrabajoButton.addActionListener(addTrabajoListener);
	editTrabajoButton = (JButton) form.getComponentByName("edit_trabajo_button");
	editTrabajoListener = new EditTrabajoListener();
	editTrabajoButton.addActionListener(editTrabajoListener);
	deleteReconocimientoButton = (JButton) form.getComponentByName("delete_reconocimiento_button");
	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton.addActionListener(deleteReconocimientoListener);
	deleteTrabajoButton = (JButton) form.getComponentByName("delete_trabajo_button");
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
	super.removeListeners();
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    TaludesReconocimientosSubForm subForm =
		    new TaludesReconocimientosSubForm(
			    "forms/taludes_reconocimiento_estado.xml",
			    "taludes_reconocimiento_estado",
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
			    "forms/taludes_trabajos.xml",
			    "taludes_trabajos",
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
				"forms/taludes_reconocimiento_estado.xml",
				"taludes_reconocimiento_estado",
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
				"forms/taludes_trabajos.xml",
				"taludes_trabajos",
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
	    deleteElement(reconocimientoEstado, "taludes_reconocimiento_estado", "n_inspeccion");
	}
    }

    public class DeleteTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(trabajos, "taludes_trabajos", "id_trabajo");
	}
    }

    private void deleteElement(JTable embebedTable, String dbTableName,
	    String pkField) {

	if (embebedTable.getSelectedRowCount() != 0) {
	    Object[] options = {"Eliminar", "Cancelar"};
	    int response = JOptionPane.showOptionDialog(null,
		    "Los datos seleccionados se eliminarán de forma permanente.",
		    "Eliminar",
		    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
		    null, // do not use a custom Icon
		    options, // the titles of buttons
		    options[0]); // default button title
	    if (response == JOptionPane.YES_OPTION) {
		int selectedRow = embebedTable.getSelectedRow();
		String pkValue = embebedTable.getValueAt(selectedRow, 0).toString();
		DefaultTableModel model = (DefaultTableModel) embebedTable.getModel();
		model.removeRow(selectedRow);
		SqlUtils.delete(DBFieldNames.GIA_SCHEMA, dbTableName, pkField, pkValue);
		repaint();
	    } else {
		// Nothing to do
	    }
	}else {
	    JOptionPane.showMessageDialog(null,
		    "Debe seleccionar una fila para editar los datos.",
		    "Ninguna fila seleccionada",
		    JOptionPane.INFORMATION_MESSAGE);
	}
    }

}
