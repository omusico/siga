package es.icarto.gvsig.extgia.forms.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.batch.AddReconocimientosBatchListener;
import es.icarto.gvsig.extgia.batch.AddTrabajosBatchListener;
import es.icarto.gvsig.extgia.forms.images.AddImageListener;
import es.icarto.gvsig.extgia.forms.images.DeleteImageListener;
import es.icarto.gvsig.extgia.forms.images.ShowImageAction;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;

@SuppressWarnings("serial")
public abstract class AbstractFormWithLocationWidgets extends AbstractForm {

    protected static final String AREA_MANTENIMIENTO = "area_mantenimiento";
    protected static final String BASE_CONTRATISTA = "base_contratista";
    protected static final String TRAMO = "tramo";
    private static final String TIPO_VIA = "tipo_via";
    private static final String TIPO_VIA_PF = "tipo_via_pf";
    private static final String NOMBRE_VIA = "nombre_via";
    private static final String NOMBRE_VIA_PF = "nombre_via_pf";
    private static final String SENTIDO = "sentido";

    protected FilesLinkButton filesLinkButton;

    protected JComboBox areaMantenimientoWidget;
    protected JComboBox baseContratistaWidget;
    protected JComboBox tramoWidget;
    private JComboBox tipoViaWidget;
    private JComboBox nombreViaWidget;
    private JComboBox tipoViaPFWidget;
    private JComboBox nombreViaPFWidget;
    private JComboBox sentidoWidget;

    private UpdateTipoViaListener updateTipoViaListener;
    private UpdateNombreViaListener updateNombreViaListener;
    private UpdateNombreViaPFListener updateNombreViaPFListener;

    protected JTable reconocimientoEstado;
    protected JTable trabajos;

    protected JButton addReconocimientoButton;
    protected JButton editReconocimientoButton;
    protected JButton deleteReconocimientoButton;
    protected JButton addTrabajoButton;
    protected JButton editTrabajoButton;
    protected JButton deleteTrabajoButton;
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

    protected DependentComboboxHandler baseContratistaDomainHandler;
    protected DependentComboboxHandler tramoDomainHandler;

    public AbstractFormWithLocationWidgets(FLyrVect layer) {
	super(layer);
    }

    @Override
    public WindowInfo getWindowInfo() {
	if (windowInfo == null) {
	    super.getWindowInfo();
	    windowInfo.setTitle(getBasicName());
	}
	return windowInfo;
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	if (!isSpecialCase()) {
	    ImageComponent image = (ImageComponent) formBody
		    .getComponentByName("image");
	    ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
	    image.setIcon(icon);

	    imageComponent = (ImageComponent) formBody
		    .getComponentByName("element_image");
	    addImageButton = (JButton) formBody
		    .getComponentByName("add_image_button");
	    deleteImageButton = (JButton) formBody
		    .getComponentByName("delete_image_button");

	    Map<String, JComponent> widgets = getWidgets();

	    areaMantenimientoWidget = (JComboBox) widgets
		    .get(AREA_MANTENIMIENTO);
	    baseContratistaWidget = (JComboBox) widgets.get(BASE_CONTRATISTA);
	    tramoWidget = (JComboBox) widgets.get(TRAMO);
	    tipoViaWidget = (JComboBox) widgets.get(TIPO_VIA);
	    nombreViaWidget = (JComboBox) widgets.get(NOMBRE_VIA);

	    baseContratistaDomainHandler = new DependentComboboxHandler(this,
		    areaMantenimientoWidget, baseContratistaWidget);
	    areaMantenimientoWidget
		    .addActionListener(baseContratistaDomainHandler);
	    tramoDomainHandler = new DependentComboboxHandler(this,
		    baseContratistaWidget, tramoWidget);

	    updateTipoViaListener = new UpdateTipoViaListener();
	    updateNombreViaListener = new UpdateNombreViaListener();

	    baseContratistaWidget.addActionListener(tramoDomainHandler);
	    tramoWidget.addActionListener(updateTipoViaListener);
	    tipoViaWidget.addActionListener(updateNombreViaListener);

	    if (elementHasIPandFP()) {
		tipoViaPFWidget = (JComboBox) widgets.get(TIPO_VIA_PF);
		updateNombreViaPFListener = new UpdateNombreViaPFListener();
		tipoViaPFWidget.addActionListener(updateNombreViaPFListener);
		nombreViaPFWidget = (JComboBox) widgets.get(NOMBRE_VIA_PF);
	    }

	    if (hasSentido()) {
		sentidoWidget = (JComboBox) widgets.get(SENTIDO);
	    }

	    reconocimientoEstado = (JTable) widgets
		    .get("reconocimiento_estado");
	    trabajos = (JTable) widgets.get("trabajos");
	    addReconocimientoButton = (JButton) formBody
		    .getComponentByName("add_reconocimiento_button");
	    editReconocimientoButton = (JButton) formBody
		    .getComponentByName("edit_reconocimiento_button");
	    addTrabajoButton = (JButton) formBody
		    .getComponentByName("add_trabajo_button");
	    editTrabajoButton = (JButton) formBody
		    .getComponentByName("edit_trabajo_button");
	    deleteReconocimientoButton = (JButton) formBody
		    .getComponentByName("delete_reconocimiento_button");
	    deleteTrabajoButton = (JButton) formBody
		    .getComponentByName("delete_trabajo_button");

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

    protected void selectBaseContratistaOption() {
	String baseContratista = this.getFormController().getValue(
		BASE_CONTRATISTA);
	for (int i = 0; i < baseContratistaWidget.getItemCount(); i++) {
	    if (baseContratista
		    .equalsIgnoreCase(((KeyValue) baseContratistaWidget
			    .getItemAt(i)).getKey())) {
		baseContratistaWidget.setSelectedIndex(i);
	    }
	}
    }

    protected void selectTramoOption() {
	String tramo = this.getFormController().getValue(TRAMO);
	for (int i = 0; i < tramoWidget.getItemCount(); i++) {
	    if (tramo.equalsIgnoreCase(((KeyValue) tramoWidget.getItemAt(i))
		    .getKey())) {
		tramoWidget.setSelectedIndex(i);
	    }
	}
    }

    private void updateTipoViaCombo() {
	String id_tramo = ((KeyValue) tramoWidget.getSelectedItem()).getKey();
	String id_bc = ((KeyValue) baseContratistaWidget.getSelectedItem())
		.getKey();
	String getTipoViaQuery = "SELECT id, item  FROM audasa_extgia_dominios.tipo_via"
		+ " WHERE id_tramo = "
		+ id_tramo
		+ " AND id_bc = "
		+ id_bc
		+ ";";
	tipoViaWidget.removeAllItems();
	tipoViaWidget.addItem(new KeyValue("", ""));
	if (!id_tramo.isEmpty() && !id_bc.isEmpty()) {
	    for (KeyValue value : SqlUtils
		    .getKeyValueListFromSql(getTipoViaQuery)) {
		tipoViaWidget.addItem(value);
	    }
	    if (elementHasIPandFP()) {
		tipoViaPFWidget.removeAllItems();
		tipoViaPFWidget.addItem(new KeyValue("", ""));
		for (KeyValue value : SqlUtils
			.getKeyValueListFromSql(getTipoViaQuery)) {
		    tipoViaPFWidget.addItem(value);
		}
	    }
	}
    }

    private void selectTipoViaOption() {
	String tipoVia = this.getFormController().getValue(TIPO_VIA);
	for (int i = 0; i < tipoViaWidget.getItemCount(); i++) {
	    if (tipoVia
		    .equalsIgnoreCase(((KeyValue) tipoViaWidget.getItemAt(i))
			    .getKey())) {
		tipoViaWidget.setSelectedIndex(i);
	    }
	}
	if (elementHasIPandFP()) {
	    String tipoViaPF = this.getFormController().getValue(TIPO_VIA_PF);
	    for (int i = 0; i < tipoViaPFWidget.getItemCount(); i++) {
		if (tipoViaPF.equalsIgnoreCase(((KeyValue) tipoViaPFWidget
			.getItemAt(i)).getKey())) {
		    tipoViaPFWidget.setSelectedIndex(i);
		}
	    }
	}
    }

    private void updateNombreViaCombo() {
	String id_tv = ((KeyValue) tipoViaWidget.getSelectedItem()).getKey();
	String id_tramo = ((KeyValue) tramoWidget.getSelectedItem()).getKey();
	String id_bc = ((KeyValue) baseContratistaWidget.getSelectedItem())
		.getKey();
	String getNombreViaQuery = "SELECT id, item FROM audasa_extgia_dominios.nombre_via"
		+ " WHERE id_tv = "
		+ id_tv
		+ " AND id_tramo = "
		+ id_tramo
		+ " AND id_bc = " + id_bc + ";";
	nombreViaWidget.removeAllItems();
	nombreViaWidget.addItem(new KeyValue("", ""));
	if (!id_tv.isEmpty() && !id_tramo.isEmpty() && !id_bc.isEmpty()) {
	    for (KeyValue value : SqlUtils
		    .getKeyValueListFromSql(getNombreViaQuery)) {
		nombreViaWidget.addItem(value);
	    }
	}
    }

    private void selectNombreViaOption() {
	String nombreVia = this.getFormController().getValue(NOMBRE_VIA);
	for (int i = 0; i < nombreViaWidget.getItemCount(); i++) {
	    if (nombreVia.equalsIgnoreCase(((KeyValue) nombreViaWidget
		    .getItemAt(i)).getKey())) {
		nombreViaWidget.setSelectedIndex(i);
	    }
	}
    }

    private void updateNombreViaPFCombo() {
	String id_tv = ((KeyValue) tipoViaPFWidget.getSelectedItem()).getKey();
	String id_tramo = ((KeyValue) tramoWidget.getSelectedItem()).getKey();
	String id_bc = ((KeyValue) baseContratistaWidget.getSelectedItem())
		.getKey();
	String getNombreViaQuery = "SELECT id, item FROM audasa_extgia_dominios.nombre_via"
		+ " WHERE id_tv = "
		+ id_tv
		+ " AND id_tramo = "
		+ id_tramo
		+ " AND id_bc = " + id_bc + ";";
	nombreViaPFWidget.removeAllItems();
	nombreViaPFWidget.addItem(new KeyValue("", ""));
	if (!id_tv.isEmpty() && !id_tramo.isEmpty() && !id_bc.isEmpty()) {
	    for (KeyValue value : SqlUtils
		    .getKeyValueListFromSql(getNombreViaQuery)) {
		nombreViaPFWidget.addItem(value);
	    }
	}
    }

    private void selectNombreViaPFOption() {
	String nombreViaPF = this.getFormController().getValue(NOMBRE_VIA_PF);
	for (int i = 0; i < nombreViaPFWidget.getItemCount(); i++) {
	    if (nombreViaPF.equalsIgnoreCase(((KeyValue) nombreViaPFWidget
		    .getItemAt(i)).getKey())) {
		nombreViaPFWidget.setSelectedIndex(i);
	    }
	}
    }

    public class UpdateTipoViaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tramoWidget.getItemCount() != 0) {
		updateTipoViaCombo();
	    }
	}
    }

    public class UpdateNombreViaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tipoViaWidget.getItemCount() != 0) {
		updateNombreViaCombo();
	    }
	}
    }

    public class UpdateNombreViaPFListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tipoViaPFWidget.getItemCount() != 0) {
		updateNombreViaPFCombo();
	    }
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
	areaMantenimientoWidget
		.removeActionListener(baseContratistaDomainHandler);
	baseContratistaWidget.removeActionListener(tramoDomainHandler);
	tramoWidget.removeActionListener(updateTipoViaListener);
	tipoViaWidget.removeActionListener(updateNombreViaListener);

	if (elementHasIPandFP()) {
	    tipoViaPFWidget.removeActionListener(updateNombreViaPFListener);
	}

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

    public JComboBox getAreaMantenimientoWidget() {
	return areaMantenimientoWidget;
    }

    public JComboBox getBaseContratistaWidget() {
	return baseContratistaWidget;
    }

    public JComboBox getTramoWidget() {
	return tramoWidget;
    }

    public JComboBox getTipoViaWidget() {
	return tipoViaWidget;
    }

    public JComboBox getNombreViaWidget() {
	return nombreViaWidget;
    }

    private boolean elementHasIPandFP() {
	if (layer.getName().equalsIgnoreCase("taludes")
		|| (layer.getName().equalsIgnoreCase("valla_cierre"))
		|| (layer.getName().equalsIgnoreCase("muros"))
		|| (layer.getName().equalsIgnoreCase("lineas_suministro"))) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public FormPanel getFormBody() {
	if (formBody == null) {
	    InputStream stream = getClass().getClassLoader()
		    .getResourceAsStream(getFormBodyPath());
	    try {
		formBody = new FormPanel(stream);
	    } catch (FormException e) {
		e.printStackTrace();
	    }
	}
	return formBody;
    }

    protected void deleteElement(JTable embebedTable, String dbTableName,
	    String pkField) {

	if (embebedTable.getSelectedRowCount() != 0) {
	    Object[] options = { "Eliminar", "Cancelar" };
	    int response = JOptionPane
		    .showOptionDialog(
			    null,
			    "Los datos seleccionados se eliminarán de forma permanente.",
			    "Eliminar", JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE, null, // do not use a
							       // custom Icon
			    options, // the titles of buttons
			    options[0]); // default button title
	    if (response == JOptionPane.YES_OPTION) {
		int selectedRow = embebedTable.getSelectedRow();
		String pkValue = embebedTable.getValueAt(selectedRow, 0)
			.toString();
		DefaultTableModel model = (DefaultTableModel) embebedTable
			.getModel();
		model.removeRow(selectedRow);
		SqlUtils.delete(DBFieldNames.GIA_SCHEMA, dbTableName, pkField,
			pkValue);
		repaint();
	    } else {
		// Nothing to do
	    }
	} else {
	    JOptionPane.showMessageDialog(null,
		    "Debe seleccionar una fila para editar los datos.",
		    "Ninguna fila seleccionada",
		    JOptionPane.INFORMATION_MESSAGE);
	}
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
	if (nombreViaWidget != null) {
	    if (nombreViaWidget.getSelectedItem().toString().isEmpty()) {
		layerController.setValue(nombreViaWidget.getName(), "0");
	    }
	}
	if (elementHasIPandFP()) {
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
	baseContratistaDomainHandler.updateComboBoxValues();
	tramoDomainHandler.updateComboBoxValues();
	updateTipoViaCombo();
	selectTipoViaOption();
	updateNombreViaCombo();
	selectNombreViaOption();
	if (elementHasIPandFP()) {
	    updateNombreViaPFCombo();
	    selectNombreViaPFOption();
	}

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

    public abstract String getElement();

    public abstract String getElementID();

    public abstract String getElementIDValue();

    public abstract String getFormBodyPath();

    public abstract JTable getReconocimientosJTable();

    public abstract JTable getTrabajosJTable();

    public abstract String getReconocimientosDBTableName();

    public abstract String getTrabajosDBTableName();

    public abstract String getImagesDBTableName();

    public abstract String getReconocimientosFormFileName();

    public abstract String getTrabajosFormFileName();

    public String getReconocimientosIDField() {
	return "n_inspeccion";
    }

    public String getTrabajosIDField() {
	return "id_trabajo";
    }

    public abstract boolean isSpecialCase();

    protected abstract String getBasicName();

    protected abstract boolean hasSentido();
}
