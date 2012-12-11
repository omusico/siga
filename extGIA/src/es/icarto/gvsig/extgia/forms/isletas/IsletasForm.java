package es.icarto.gvsig.extgia.forms.isletas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

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
import es.icarto.gvsig.extgia.forms.taludes.TaludesReconocimientosSubForm;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.validation.listeners.DependentComboboxesHandler;

@SuppressWarnings("serial")
public class IsletasForm extends AbstractFormWithLocationWidgets {

    public static final String ABEILLE_FILENAME = "forms/isletas.xml";
    public static final String ABEILLE_RECONOCIMIENTOS_FILENAME = "forms/isletas_reconocimiento_estado.xml";
    public static final String ABEILLE_TRABAJOS_FILENAME = "forms/isletas_trabajos.xml";

    JComboBox tipoIsletaWidget;
    JTextField numeroIsletaWidget;
    JComboBox baseContratistaWidget;
    JTextField isletaIDWidget;
    CalculateComponentValue isletaid;
    private JComboBox tipoVia;
    private DependentComboboxesHandler direccionDomainHandler;

    FilesLinkButton filesLinkButton;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    public IsletasForm(FLyrVect layer) {
	super(layer);
	initWindow();
	initListeners();
    }

    private void addNewButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();

	filesLinkButton = new FilesLinkButton(this, new FilesLinkData() {

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
    }

    @Override
    protected void initWindow() {
	super.initWindow();
	this.viewInfo.setTitle("Isletas");
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

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	int[] trabajoColumnsSize = {1, 1, 120, 60, 60};
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", "isletas_reconocimiento_estado",
		DBFieldNames.reconocimientoEstadoFields, null, "id_isleta", isletaIDWidget.getText());
	SqlUtils.createEmbebedTableFromDB(trabajos,
		"audasa_extgia", "isletas_trabajos",
		DBFieldNames.trabajoFields, trabajoColumnsSize, "id_isleta", isletaIDWidget.getText());
    }

    protected void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

	isletaIDWidget = (JTextField) widgets.get(DBFieldNames.ID_ISLETA);

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
	isletaid.removeListeners();
	tipoVia.removeActionListener(direccionDomainHandler);
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
			    "id_isleta",
			    isletaIDWidget.getText(),
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
			    "id_isleta",
			    isletaIDWidget.getText(),
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
				"id_isleta",
				isletaIDWidget.getText(),
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
				"id_isleta",
				isletaIDWidget.getText(),
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
    public String getFormBodyPath() {
	return ABEILLE_FILENAME;
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
    public String getReconocimientosDBTableName() {
	return "isletas_reconocimiento_estado";
    }

    @Override
    public String getTrabajosDBTableName() {
	return "isletas_trabajos";
    }


}
