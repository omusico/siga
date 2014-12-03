package es.icarto.gvsig.extgia.forms.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.batch.AddReconocimientosBatchListener;
import es.icarto.gvsig.extgia.batch.AddTrabajosBatchListener;
import es.icarto.gvsig.extgia.forms.images.AddImageListener;
import es.icarto.gvsig.extgia.forms.images.DeleteImageListener;
import es.icarto.gvsig.extgia.forms.images.ShowImageAction;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.BasicAbstractForm;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;

@SuppressWarnings("serial")
public abstract class AbstractFormWithLocationWidgets extends BasicAbstractForm {

    private static final Logger logger = Logger
	    .getLogger(AbstractFormWithLocationWidgets.class);

    protected static final String AREA_MANTENIMIENTO = "area_mantenimiento";
    protected static final String BASE_CONTRATISTA = "base_contratista";
    protected static final String TRAMO = "tramo";
    private static final String TIPO_VIA = "tipo_via";
    private static final String TIPO_VIA_PF = "tipo_via_pf";
    private static final String NOMBRE_VIA = "nombre_via";
    private static final String NOMBRE_VIA_PF = "nombre_via_pf";
    private static final String PK = "pk";
    private static final String PK_INICIAL = "pk_inicial";
    private static final String PK_FINAL = "pk_final";
    private static final String SENTIDO = "sentido";

    protected FilesLinkButton filesLinkButton;

    private JComboBox sentidoWidget;

    protected JButton addTrabajosBatchButton;
    protected JButton addReconocimientosBatchButton;
    protected JButton saveRecordsBatchButton;

    protected ImageComponent imageComponent;
    protected JButton addImageButton;
    protected JButton deleteImageButton;

    AddTrabajosBatchListener addTrabajosBatchListener;
    AddReconocimientosBatchListener addReconocimientosBatchListener;
    SaveRecordsBatchListener saveRecordsBatchListener;

    protected AddImageListener addImageListener;
    protected DeleteImageListener deleteImageListener;

    public AbstractFormWithLocationWidgets(FLyrVect layer) {
	super(layer);
	addChained(BASE_CONTRATISTA, AREA_MANTENIMIENTO);
	addChained(TRAMO, BASE_CONTRATISTA);
	final Map<String, JComponent> widgets = getWidgets();
	if (widgets.get(TIPO_VIA) != null) {
	    addChained(TIPO_VIA, BASE_CONTRATISTA, TRAMO);
	}
	if (widgets.get(TIPO_VIA_PF) != null) {
	    addChained(TIPO_VIA_PF, BASE_CONTRATISTA, TRAMO);
	}
	if (widgets.get(NOMBRE_VIA) != null) {
	    addChained(NOMBRE_VIA, BASE_CONTRATISTA, TRAMO, TIPO_VIA);
	}
	if (widgets.get(NOMBRE_VIA_PF) != null) {
	    addChained(NOMBRE_VIA_PF, BASE_CONTRATISTA, TRAMO, TIPO_VIA_PF);
	}
	if (widgets.get(PK) != null) {
	    addChained(PK, TIPO_VIA, NOMBRE_VIA);
	}
	if (widgets.get(PK_INICIAL) != null) {
	    addChained(PK_INICIAL, TIPO_VIA, NOMBRE_VIA);
	}
	if (widgets.get(PK_FINAL) != null) {
	    if (widgets.get(NOMBRE_VIA_PF) != null) {
		addChained(PK_FINAL, TIPO_VIA_PF, NOMBRE_VIA_PF);
	    } else {
		addChained(PK_FINAL, TIPO_VIA, NOMBRE_VIA);
	    }
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	imageComponent = (ImageComponent) formBody
		.getComponentByName("element_image");
	addImageButton = (JButton) formBody
		.getComponentByName("add_image_button");
	deleteImageButton = (JButton) formBody
		.getComponentByName("delete_image_button");

	if (addImageListener == null) {
	    addImageListener = new AddImageListener(imageComponent,
		    addImageButton, getImagesDBTableName(), getElementID());
	    addImageButton.addActionListener(addImageListener);
	}

	if (deleteImageListener == null) {
	    deleteImageListener = new DeleteImageListener(imageComponent,
		    addImageButton, getImagesDBTableName(), getElementID());
	    deleteImageButton.addActionListener(deleteImageListener);
	}

	if (hasSentido()) {
	    sentidoWidget = (JComboBox) getWidgets().get(SENTIDO);
	}

	if (SqlUtils.elementHasType(dataName, "inspecciones")) {
	    if (addReconocimientosBatchButton == null) {
		addReconocimientosBatchButton = new JButton();
		java.net.URL imgURL = getClass().getResource(
			"/batch_reconocimiento.png");
		ImageIcon batchReconocimientoIcon = new ImageIcon(imgURL);
		addReconocimientosBatchButton.setIcon(batchReconocimientoIcon);
		addReconocimientosBatchButton.setToolTipText(PluginServices
			.getText(this, "addBatchReconocimientos_tooltip"));
		getActionsToolBar().add(addReconocimientosBatchButton);
	    }
	}

	if (SqlUtils.elementHasType(dataName, "trabajos")) {
	    if (addTrabajosBatchButton == null) {
		addTrabajosBatchButton = new JButton();
		java.net.URL imgURL = getClass().getResource(
			"/batch_trabajo.png");
		ImageIcon trabajosBatchIcon = new ImageIcon(imgURL);
		addTrabajosBatchButton.setIcon(trabajosBatchIcon);
		addTrabajosBatchButton.setToolTipText(PluginServices.getText(
			this, "addBatchTrabajos_tooltip"));
		getActionsToolBar().add(addTrabajosBatchButton);
	    }
	}

	if (addTrabajosBatchListener == null && addTrabajosBatchButton != null) {
	    addTrabajosBatchListener = new AddTrabajosBatchListener(
		    getElement(), getTrabajosFormFileName(),
		    getTrabajosDBTableName());
	    addTrabajosBatchButton.addActionListener(addTrabajosBatchListener);
	}

	if (addReconocimientosBatchListener == null
		&& addReconocimientosBatchButton != null) {
	    addReconocimientosBatchListener = new AddReconocimientosBatchListener(
		    getElement(), getReconocimientosFormFileName(),
		    getReconocimientosDBTableName());
	    addReconocimientosBatchButton
		    .addActionListener(addReconocimientosBatchListener);
	}

	if (saveRecordsBatchButton == null) {
	    saveRecordsBatchButton = new JButton();
	    java.net.URL imgURL = getClass().getResource("/saveSelected.png");
	    ImageIcon saveBatchIcon = new ImageIcon(imgURL);
	    saveRecordsBatchButton.setIcon(saveBatchIcon);
	    saveRecordsBatchButton.setToolTipText(PluginServices.getText(this,
		    "saveRecordsBatch_tooltip"));
	    getActionsToolBar().add(saveRecordsBatchButton);
	}

	if (saveRecordsBatchListener == null && saveRecordsBatchButton != null) {
	    saveRecordsBatchListener = new SaveRecordsBatchListener();
	    saveRecordsBatchButton.addActionListener(saveRecordsBatchListener);
	}

    }

    public class SaveRecordsBatchListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (isSaveable()) {
		setSavingValues(true);
		try {
		    if (layer.getRecordset().getSelection().isEmpty()) {
			JOptionPane.showMessageDialog(null, PluginServices
				.getText(this, "unselectedElements_msg"),
				PluginServices.getText(this, "warning"),
				JOptionPane.WARNING_MESSAGE);
			return;
		    }

		    if (layerController.getValuesChanged().containsKey(
			    getElementID())) {
			JOptionPane
				.showMessageDialog(
					null,
					"La actualización masiva no funciona en campos que forman parte del ID.",
					"Cambios en el ID",
					JOptionPane.WARNING_MESSAGE);
			layerController.clearAll();
			setChangedValues(false);
			setSavingValues(false);
			return;
		    }

		    Object[] options = {
			    PluginServices.getText(this, "optionPane_yes"),
			    PluginServices.getText(this, "optionPane_no") };
		    int m = JOptionPane.showOptionDialog(
			    null,
			    PluginServices.getText(this, "updateInfo_msg_I")
				    + layer.getRecordset().getSelection()
					    .cardinality()
				    + " "
				    + PluginServices.getText(this,
					    "updateInfo_msg_II"), null,
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.INFORMATION_MESSAGE, null, options,
			    options[1]);
		    if (m == JOptionPane.OK_OPTION) {
			saveRecords();
			JOptionPane.showMessageDialog(
				null,
				PluginServices.getText(this,
					"updatedInfo_msg_I")
					+ layer.getRecordset().getSelection()
						.cardinality()
					+ " "
					+ PluginServices.getText(this,
						"updatedInfo_msg_II"));
			setChangedValues(false);
			setSavingValues(false);
		    }
		} catch (ReadDriverException ex) {
		    layerController.clearAll();
		    setChangedValues(false);
		    setSavingValues(false);
		    logger.error(ex.getStackTrace(), ex);
		} catch (StopWriterVisitorException e1) {
		    layerController.clearAll();
		    setChangedValues(false);
		    setSavingValues(false);
		    logger.error(e1.getStackTrace(), e1);
		}
	    }
	}

	private void saveRecords() throws ReadDriverException,
		StopWriterVisitorException {
	    int[] indexesofValuesChanged = layerController
		    .getIndexesOfValuesChanged();
	    String[] valuesChanged = layerController.getValuesChanged()
		    .values().toArray(new String[0]);

	    ToggleEditing te = new ToggleEditing();
	    boolean wasEditing = layer.isEditing();
	    if (!wasEditing) {
		te.startEditing(layer);
	    }
	    for (int i = 0; i < layer.getRecordset().getRowCount(); i++) {
		if (layer.getRecordset().isSelected(i)) {
		    te.modifyValues(layer, i, indexesofValuesChanged,
			    valuesChanged);
		}
	    }
	    if (!wasEditing) {
		te.stopEditing(layer, false);
	    }
	    layerController.read(getPosition());
	}

    }

    @Override
    protected void removeListeners() {

	if (addTrabajosBatchButton != null) {
	    addTrabajosBatchButton
		    .removeActionListener(addTrabajosBatchListener);
	}
	if (addReconocimientosBatchButton != null) {
	    addReconocimientosBatchButton
		    .removeActionListener(addReconocimientosBatchListener);
	}

	saveRecordsBatchButton.removeActionListener(saveRecordsBatchListener);

	addImageButton.removeActionListener(addImageListener);
	deleteImageButton.removeActionListener(deleteImageListener);

	super.removeListeners();
    }

    protected void addNewButtonsToActionsToolBar(
	    final DBFieldNames.Elements element) {
	JPanel actionsToolBar = this.getActionsToolBar();

	filesLinkButton = new FilesLinkButton(this, new FilesLinkData() {

	    @Override
	    public String getRegisterField() {
		return DBFieldNames.getPrimaryKey(element);
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
			+ element;

		return baseDirectory;
	    }
	});
	actionsToolBar.add(filesLinkButton);
    }

    @Override
    public boolean saveRecord() throws StopWriterVisitorException {

	JComboBox nombreViaWidget = (JComboBox) getWidgets().get(NOMBRE_VIA);
	if (nombreViaWidget != null) {
	    if (nombreViaWidget.getSelectedItem().toString().isEmpty()) {
		layerController.setValue(nombreViaWidget.getName(), "0");
	    }
	}

	// taludes, valla cierre, muros, lineas suministro
	JComboBox nombreViaPFWidget = (JComboBox) getWidgets().get(
		NOMBRE_VIA_PF);
	if (nombreViaPFWidget != null) {
	    if (nombreViaPFWidget.getSelectedItem().toString().isEmpty()) {
		layerController.setValue(nombreViaPFWidget.getName(), "0");
	    }
	}

	if (hasSentido()) {
	    if (sentidoWidget.getSelectedItem().toString().isEmpty()) {
		layerController.setValue(sentidoWidget.getName(), "0");
	    }
	}
	return super.saveRecord();
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();
	if (addImageListener != null) {
	    addImageListener.setPkValue(getElementIDValue());
	}

	if (deleteImageListener != null) {
	    deleteImageListener.setPkValue(getElementIDValue());
	}

	// Element image
	new ShowImageAction(imageComponent, addImageButton,
		getImagesDBTableName(), getElementID(), getElementIDValue());
    }

    @Override
    protected String getSchema() {
	return DBFieldNames.GIA_SCHEMA;
    }

    protected String getReconocimientosFormFileName() {
	return "forms/" + getBasicName() + "_reconocimientos.jfrm";
    }

    protected String getTrabajosFormFileName() {
	return "forms/" + getBasicName() + "_trabajos.jfrm";
    }

    public String getReconocimientosDBTableName() {
	return getBasicName() + "_reconocimientos";
    }

    public String getTrabajosDBTableName() {
	return getBasicName() + "_trabajos";
    }

    public String getRamalesDBTableName() {
	return getBasicName() + "_ramales";
    }

    @Override
    protected String getPrimaryKeyValue() {
	return getFormController().getValue(getElementID());
    }

    public abstract String getElement();

    public abstract String getElementID();

    public abstract String getElementIDValue();

    public abstract JTable getReconocimientosJTable();

    public abstract JTable getTrabajosJTable();

    public abstract String getImagesDBTableName();

    public String getReconocimientosIDField() {
	return "n_inspeccion";
    }

    public String getTrabajosIDField() {
	return "id_trabajo";
    }

    @Override
    protected abstract String getBasicName();

    protected abstract boolean hasSentido();
}
