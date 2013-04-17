package es.icarto.gvsig.extgia.forms.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorComponent;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;

@SuppressWarnings("serial")
public abstract class AbstractSubForm extends JPanel implements IWindow,
IWindowListener {

    private final FormPanel form;
    private final HashMap<String, Integer> types;
    private HashMap<String, Value> values;
    private final String dbTableName;
    private final JTable embebedTable;
    private final String idElementField;
    private final String idElementValue;
    private final String idField;
    private final String idValue;
    private final boolean edit;
    private boolean fillingValues = false;

    protected WindowInfo viewInfo = null;
    private String title;
    private final int width = 430;
    private final int height = 440;

    private HashMap<String, JComponent> widgetsVector;

    private final ORMLite ormLite;
    private final ValidatorForm formValidator;
    private final TextFieldsValidationNotifier textFieldsNotifier;
    private final ComboBoxValidationNotifier comboBoxNotifier;
    private JButton addButton;

    public AbstractSubForm(String formFile,
	    String dbTableName,
	    JTable embebedTable,
	    String idElementField,
	    String idElementValue,
	    String idField,
	    String idValue,
	    boolean edit) {
	InputStream stream = getClass().getClassLoader().getResourceAsStream(formFile);
	FormPanel result = null;
	try {
	    result = new FormPanel(stream);
	    this.add(result);
	} catch (FormException e) {
	    e.printStackTrace();
	}
	this.form = result;
	this.dbTableName = dbTableName;
	this.embebedTable = embebedTable;
	this.idElementField = idElementField;
	this.idElementValue = idElementValue;
	this.idField = idField;
	this.idValue = idValue;
	this.edit = edit;
	ormLite = new ORMLite(getXMLPath());
	formValidator = new ValidatorForm();
	textFieldsNotifier = new TextFieldsValidationNotifier();
	comboBoxNotifier = new ComboBoxValidationNotifier();
	this.types = SqlUtils.getDataTypesFromDbTable(DBFieldNames.GIA_SCHEMA, dbTableName);
	initWidgets();
	setValidation();
	if (edit) {
	    this.values = SqlUtils.getValuesFilteredByPk(DBFieldNames.GIA_SCHEMA,
		    dbTableName, idField, getPKSelectedValue());
	}
	fillValues(edit);
    }

    private void setValidation() {
	for (JComponent c : widgetsVector.values()) {
	    setValidationListener(c);
	    ValidatorComponent cv = ValidatorComponentFactory.createValidator(
		    c, ormLite);
	    if (cv != null) {
		formValidator.addComponentValidator(cv);
	    }
	}
    }

    private void setValidationListener(JComponent c) {
	if(c instanceof JTextField) {
	    ((JTextField) c).addKeyListener(textFieldsNotifier);
	} else if (c instanceof JComboBox) {
	    ((JComboBox) c).addActionListener(comboBoxNotifier);
	}
    }

    private void removeValidationListeners() {
	for (JComponent c : widgetsVector.values()) {
	    if (c instanceof JTextField) {
		((JTextField) c).removeKeyListener(textFieldsNotifier);
	    } else if (c instanceof JComboBox) {
		((JComboBox) c).removeActionListener(comboBoxNotifier);
	    }
	}
    }

    public abstract String getXMLPath();

    protected void initWidgets() {
	widgetsVector = AbeilleParser.getWidgetsFromContainer(form);
	widgetsVector.size();

	JTextField idWidget = (JTextField) widgetsVector.get(idElementField);
	idWidget.setText(idElementValue);

	addButton = (JButton) form.getComponentByName("add_subform_button");
	AddDataToJTableListener addDataToJTableListener = new AddDataToJTableListener();
	addButton.addActionListener(addDataToJTableListener);

	if (edit) {
	    addButton.setText("Actualizar");
	}

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);
    }

    private final class ComboBoxValidationNotifier implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    formValidator.validate();
	    addButton.setEnabled(!formValidator.hasValidationErrors());
	}

    }

    private final class TextFieldsValidationNotifier implements KeyListener {
	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	    formValidator.validate();
	    addButton.setEnabled(!formValidator.hasValidationErrors());
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}
    }

    public class AddDataToJTableListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!edit) {
		SqlUtils.insert(DBFieldNames.GIA_SCHEMA, dbTableName, getFormData());
	    }else {
		SqlUtils.update(DBFieldNames.GIA_SCHEMA, dbTableName, getFormData(),
			idField, idValue);
	    }
	    closeWindow();
	    String[] fields = null;
	    if (embebedTable.getName().equalsIgnoreCase("reconocimiento_estado")) {
		fields = DBFieldNames.reconocimientoEstadoFields;
	    }else if (embebedTable.getName().equalsIgnoreCase("reconocimiento_estado_firme")) {
		fields = DBFieldNames.firmeReconocimientoEstadoFields;
	    }else if (embebedTable.getName().equalsIgnoreCase("trabajos_firme")) {
		fields = DBFieldNames.firmeTrabajoFields;
	    }else if (embebedTable.getName().equalsIgnoreCase("tabla_carreteras")) {
		fields = DBFieldNames.carreteras_enlazadas;
	    }else if (embebedTable.getName().equalsIgnoreCase("tabla_senhales")) {
		fields = DBFieldNames.senhales;
	    }else if (embebedTable.getName().equalsIgnoreCase("tabla_ramales")) {
		fields = DBFieldNames.ramales;
	    }else {
		fields = DBFieldNames.trabajoFields;
	    }
	    SqlUtils.reloadEmbebedTable(embebedTable, fields, DBFieldNames.GIA_SCHEMA,
		    dbTableName, idElementField, idElementValue);
	    repaint();
	}

    }

    private HashMap<String, Value> getFormData() {
	HashMap<String, Value> formData = new HashMap<String, Value>();
	for (JComponent comp : widgetsVector.values()) {
	    if (comp instanceof JComboBox) {
		if (((JComboBox) comp).getSelectedItem() != null) {
		    if (((JComboBox) comp).getSelectedItem() instanceof KeyValue) {
			try {
			    String value = ((KeyValue) ((JComboBox) comp).getSelectedItem()).getKey();
			    Integer.parseInt(value);
			    formData.put(comp.getName(),
				    ValueFactory.createValueByType(value, 4)
				    );
			} catch (ParseException e) {
			    e.printStackTrace();
			} catch (NumberFormatException e) {
			    formData.put(comp.getName(),
				    (ValueFactory.createValue(((JComboBox) comp).getSelectedItem().toString())));
			}
		    }else {
			formData.put(comp.getName(),
				(ValueFactory.createValue(((JComboBox) comp).getSelectedItem().toString())));
		    }
		}
	    } else if (comp instanceof JTextField) {
		if (!((JTextField) comp).getText().isEmpty()) {
		    String text = ((JTextField) comp).getText();
		    if (text.contains(",")) {
			text = text.replace(',', '.');
		    }
		    formData.put(comp.getName(),
			    ValueFactory.createValue((text)));
		}
	    } else if (comp instanceof JTextArea) {
		if (!((JTextArea) comp).getText().isEmpty()) {
		    formData.put(comp.getName(),
			    (ValueFactory.createValue(((JTextArea) comp).getText().toString())));
		}
	    }
	}
	return formData;
    }

    protected void fillValues(boolean edit) {
	fillingValues = true;
	for (JComponent comp : widgetsVector.values()) {
	    if (!edit) {
		if (comp instanceof JComboBox) {
		    fillJComboBox((JComboBox) comp, edit);
		}
	    }else {
		if (comp instanceof JTextField) {
		    fillJTextField((JTextField) comp);
		} else if (comp instanceof JTextArea) {
		    fillJTextArea((JTextArea) comp);
		} else if (comp instanceof JComboBox) {
		    fillJComboBox((JComboBox) comp, edit);
		}
	    }
	}
	fillingValues = false;
    }

    protected void fillJTextField(JTextField field) {
	String colName = field.getName();
	Value fieldValue = values.get(colName);
	field.setText(fieldValue.toString());
    }

    protected void fillJTextArea(JTextArea textArea) {
	String colName = textArea.getName();
	Value fieldValue = values.get(colName);
	textArea.setText(fieldValue.toString());
    }

    protected void fillJComboBox(JComboBox combobox, boolean edit) {
	String colName = combobox.getName();
	DomainValues dv = ormLite.getAppDomain().getDomainValuesForComponent(
		colName);
	if (dv != null) {
	    addDomainValuesToComboBox(combobox, dv.getValues());
	    if (edit) {
		Value fieldValue = values.get(colName);
		setDomainValueSelected(combobox, fieldValue.toString());
	    }
	}
    }

    protected void addDomainValuesToComboBox(JComboBox cb,
	    ArrayList<KeyValue> keyValueList) {

	if (cb.getItemCount() > 0) {
	    cb.removeAllItems();
	}
	for (KeyValue kv : keyValueList) {
	    cb.addItem(kv);
	}
    }

    protected void setDomainValueSelected(JComboBox combobox, String fieldValue) {
	// the value in this case here is the key in the key-value pair
	// value = alias to be shown
	// key = value to save in the database
	if(fieldValue != null) {
	    for (int j = 0; j < combobox.getItemCount(); j++) {
		String value = ((KeyValue) combobox.getItemAt(j)).getKey();
		if (value.compareTo(fieldValue.trim()) == 0) {
		    combobox.setSelectedIndex(j);
		    break;
		}
	    }
	}
	combobox.setEnabled(true);
	if (combobox.getSelectedIndex() == -1) {
	    combobox.addItem(new KeyValue("", "", ""));
	    combobox.setSelectedIndex(0);
	    combobox.setEnabled(false);
	}
    }

    private String getPKSelectedValue() {
	String pkValue = null;
	int selectedRow = -1;
	if (embebedTable.getSelectedRowCount() == 0) {
	}else {
	    selectedRow = embebedTable.getSelectedRow();
	}
	if (selectedRow != -1) {
	    pkValue = embebedTable.getValueAt(selectedRow, 0).toString();
	}
	return pkValue;
    }

    public boolean isFillingValues() {
	return fillingValues;
    }

    public HashMap<String, JComponent> getWidgetsVector() {
	return widgetsVector;
    }

    public void closeWindow() {
	PluginServices.getMDIManager().closeWindow(this);
    }

    @Override
    public WindowInfo getWindowInfo() {
	viewInfo = new WindowInfo(WindowInfo.MODALDIALOG);
	viewInfo.setTitle(title);
	viewInfo.setWidth(width);
	viewInfo.setHeight(height);
	return viewInfo;
    }

    @Override
    public Object getWindowProfile() {
	return null;
    }

    public void windowClosed() {
	removeValidationListeners();
    }

    public void windowActivated() {
	// Nothing to do
    }
}
