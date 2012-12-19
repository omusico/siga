package es.icarto.gvsig.extgia.forms.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.iver.andami.Launcher;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.forms.utils.LaunchGIAForms.Elements;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

@SuppressWarnings("serial")
public abstract class AbstractFormWithLocationWidgets extends AbstractForm {

    private static final String AREA_MANTENIMIENTO = "area_mantenimiento";
    private static final String BASE_CONTRATISTA = "base_contratista";
    private static final String TRAMO = "tramo";
    private static final String TIPO_VIA = "tipo_via";
    private static final String TIPO_VIA_PF = "tipo_via_pf";
    private static final String NOMBRE_VIA = "nombre_via";
    private static final String NOMBRE_VIA_PF = "nombre_via_pf";

    private FormPanel form;
    protected FilesLinkButton filesLinkButton;

    private JComboBox areaMantenimientoWidget;
    private JComboBox baseContratistaWidget;
    private JComboBox tramoWidget;
    private JComboBox tipoViaWidget;
    private JComboBox nombreViaWidget;

    private JComboBox tipoViaPFWidget;
    private JComboBox nombreViaPFWidget;

    private UpdateBaseContratistaListener updateBaseContratistaListener;
    private UpdateTramoListener updateTramoListener;
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

    public AbstractFormWithLocationWidgets (FLyrVect layer) {
	super(layer);
	initWidgets();
	setListeners();
    }

    protected void initWindow() {
	this.viewInfo.setHeight(700);
	this.viewInfo.setWidth(690);
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	HashMap<String, JComponent> widgets = getWidgetComponents();

	areaMantenimientoWidget = (JComboBox) widgets.get(AREA_MANTENIMIENTO);
	baseContratistaWidget = (JComboBox) widgets.get(BASE_CONTRATISTA);
	tramoWidget = (JComboBox) widgets.get(TRAMO);
	tipoViaWidget = (JComboBox) widgets.get(TIPO_VIA);
	nombreViaWidget = (JComboBox) widgets.get(NOMBRE_VIA);

	updateBaseContratistaListener = new UpdateBaseContratistaListener();
	updateTramoListener = new UpdateTramoListener();
	updateTipoViaListener = new UpdateTipoViaListener();
	updateNombreViaListener = new UpdateNombreViaListener();

	areaMantenimientoWidget.addActionListener(updateBaseContratistaListener);
	baseContratistaWidget.addActionListener(updateTramoListener);
	tramoWidget.addActionListener(updateTipoViaListener);
	tipoViaWidget.addActionListener(updateNombreViaListener);

	if (elementHasIPandFP()) {
	    tipoViaPFWidget = (JComboBox) widgets.get(TIPO_VIA_PF);
	    updateNombreViaPFListener = new UpdateNombreViaPFListener();
	    tipoViaPFWidget.addActionListener(updateNombreViaPFListener);
	    nombreViaPFWidget = (JComboBox) widgets.get(NOMBRE_VIA_PF);
	}

	reconocimientoEstado = (JTable) widgets.get("reconocimiento_estado");
	trabajos = (JTable) widgets.get("trabajos");
	addReconocimientoButton = (JButton) form.getComponentByName("add_reconocimiento_button");
	editReconocimientoButton = (JButton) form.getComponentByName("edit_reconocimiento_button");
	addTrabajoButton = (JButton) form.getComponentByName("add_trabajo_button");
	editTrabajoButton = (JButton) form.getComponentByName("edit_trabajo_button");
	deleteReconocimientoButton = (JButton) form.getComponentByName("delete_reconocimiento_button");
	deleteTrabajoButton = (JButton) form.getComponentByName("delete_trabajo_button");

    }

    private void updateBaseContratistaCombo() {
	String id = ((KeyValue)areaMantenimientoWidget.getSelectedItem()).getKey();
	String getBaseContratistaQuery =
		"SELECT id, item FROM audasa_extgia_dominios.base_contratista" +
			" WHERE id_am = " + id + ";";
	baseContratistaWidget.removeAllItems();
	baseContratistaWidget.addItem(new KeyValue("", ""));
	if (!id.isEmpty()) {
	    for (KeyValue value: SqlUtils.getKeyValueListFromSql(getBaseContratistaQuery)) {
		baseContratistaWidget.addItem(value);
	    }
	}
    }

    private void selectBaseContratistaOption() {
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

    private void updateTramoCombo() {
	String id = ((KeyValue) baseContratistaWidget.getSelectedItem())
		.getKey();
	String getTramoQuery = "SELECT id, item FROM audasa_extgia_dominios.tramo"
		+ " WHERE id_bc = " + id + ";";
	tramoWidget.removeAllItems();
	tramoWidget.addItem(new KeyValue("", ""));
	if (!id.isEmpty()) {
	    for (KeyValue value : SqlUtils
		    .getKeyValueListFromSql(getTramoQuery)) {
		tramoWidget.addItem(value);
	    }
	}
    }

    private void selectTramoOption() {
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

    public class UpdateBaseContratistaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues()) {
		updateBaseContratistaCombo();
	    }
	}
    }

    public class UpdateTramoListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && baseContratistaWidget.getItemCount() != 0) {
		updateTramoCombo();
	    }
	}
    }

    public class UpdateTipoViaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tramoWidget.getItemCount()!=0) {
		updateTipoViaCombo();
	    }
	}
    }

    public class UpdateNombreViaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tipoViaWidget.getItemCount()!=0) {
		updateNombreViaCombo();
	    }
	}
    }

    public class UpdateNombreViaPFListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tipoViaPFWidget.getItemCount()!=0) {
		updateNombreViaPFCombo();
	    }
	}
    }

    @Override
    protected void removeListeners() {
	areaMantenimientoWidget.removeActionListener(updateBaseContratistaListener);
	baseContratistaWidget.removeActionListener(updateTramoListener);
	tramoWidget.removeActionListener(updateTipoViaListener);
	tipoViaWidget.removeActionListener(updateNombreViaListener);

	if (elementHasIPandFP()) {
	    tipoViaPFWidget.removeActionListener(updateNombreViaPFListener);
	}

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
	if (layer.getName().equalsIgnoreCase("taludes")) {
	    return true;
	}else {
	    return false;
	}
    }

    @Override
    public FormPanel getFormBody() {
	if (this.form == null) {
	    InputStream stream = getClass().getClassLoader().getResourceAsStream(getFormBodyPath());
	    try {
		this.form = new FormPanel(stream);
	    } catch (FormException e) {
		e.printStackTrace();
	    }
	}
	return this.form;
    }

    protected void deleteElement(JTable embebedTable, String dbTableName,
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

    protected void addNewButtonsToActionsToolBar(final Elements element) {
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
    protected void fillSpecificValues() {
	updateBaseContratistaCombo();
	selectBaseContratistaOption();
	updateTramoCombo();
	selectTramoOption();
	updateTipoViaCombo();
	selectTipoViaOption();
	updateNombreViaCombo();
	selectNombreViaOption();
	if (elementHasIPandFP()) {
	    updateNombreViaPFCombo();
	    selectNombreViaPFOption();
	}
    }

    public abstract String getFormBodyPath();

    @Override
    public abstract Logger getLoggerName();

    @Override
    public abstract String getXMLPath();

    public abstract JTable getReconocimientosJTable();

    public abstract JTable getTrabajosJTable();

    public abstract String getReconocimientosDBTableName();

    public abstract String getTrabajosDBTableName();

    public String getReconocimientosIDField() {
	return "n_inspeccion";
    }

    public String getTrabajosIDField() {
	return "id_trabajo";
    }
}
