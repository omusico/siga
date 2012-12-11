package es.icarto.gvsig.extgia.forms.enlaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

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
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;

@SuppressWarnings("serial")
public class EnlacesForm extends AbstractFormWithLocationWidgets {

    public static String ABEILLE_FILENAME = "forms/enlaces.xml";
    public static final String ABEILLE_RECONOCIMIENTOS_FILENAME = "forms/enlaces_reconocimiento_estado.xml";

    JTextField enlaceIDWidget;
    CalculateComponentValue enlaceid;

    FilesLinkButton filesLinkButton;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    public EnlacesForm(FLyrVect layer) {
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
	this.viewInfo.setTitle("Enlaces");
    }

    @Override
    protected void fillSpecificValues() {
	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	DBFieldNames.setReconocimientoEstadoFields(DBFieldNames.enlacesReconocimientoEstadoFields);
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null, "id_enlace", enlaceIDWidget.getText());

    }

    protected void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

	enlaceIDWidget = (JTextField) widgets.get(DBFieldNames.ID_ENLACE);

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton.addActionListener(deleteReconocimientoListener);

    }

    @Override
    protected void removeListeners() {
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton.removeActionListener(deleteReconocimientoListener);
	super.removeListeners();

	DBFieldNames.setReconocimientoEstadoFields(DBFieldNames.genericReconocimientoEstadoFields);
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    EnlacesReconocimientosSubForm subForm =
		    new EnlacesReconocimientosSubForm(
			    ABEILLE_RECONOCIMIENTOS_FILENAME,
			    getReconocimientosDBTableName(),
			    reconocimientoEstado,
			    "id_enlace",
			    enlaceIDWidget.getText(),
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
		EnlacesReconocimientosSubForm subForm =
			new EnlacesReconocimientosSubForm(
				ABEILLE_RECONOCIMIENTOS_FILENAME,
				getReconocimientosDBTableName(),
				reconocimientoEstado,
				"id_enlace",
				enlaceIDWidget.getText(),
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

    public class DeleteReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(reconocimientoEstado, getReconocimientosDBTableName(),
		    getReconocimientosIDField());
	}
    }

    @Override
    public String getFormBodyPath() {
	return ABEILLE_FILENAME;
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger(this.getClass().getName());
    }

    @Override
    public String getXMLPath() {
	return Preferences.getPreferences().getXMLFilePath();
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
	return "enlaces_reconocimiento_estado";
    }

    @Override
    public String getTrabajosDBTableName() {
	// TODO Auto-generated method stub
	return null;
    }


}
