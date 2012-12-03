package es.icarto.gvsig.extgia.forms.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;

@SuppressWarnings("serial")
public abstract class AbstractSubForm extends JPanel implements IWindow {

    private final FormPanel form;
    private final HashMap<String, Integer> types;
    private HashMap<String, Value> values;
    private final JTable embebedTable;

    protected WindowInfo viewInfo = null;
    private String title;
    private final int width = 430;
    private final int height = 440;

    private HashMap<String, JComponent> widgetsVector;

    public AbstractSubForm(String formFile,
	    String dbTableName,
	    JTable embebedTable,
	    String idField,
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
	this.embebedTable = embebedTable;
	this.types = SqlUtils.getDataTypesFromDbTable(DBFieldNames.GIA_SCHEMA, dbTableName);
	initWidgets();
	if (edit) {
	    if (getPKSelectedValue() != null) {
		this.values = SqlUtils.getValuesFilteredByPk(DBFieldNames.GIA_SCHEMA,
			dbTableName, idField, getPKSelectedValue());
		fillValues();
	    }else {
		JOptionPane.showMessageDialog(this,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }

    public abstract String getXMLPath();

    protected void initWidgets() {
	widgetsVector = AbeilleParser.getWidgetsFromContainer(form);
	widgetsVector.size();

	JButton addButton = (JButton) form.getComponentByName("add_subform_button");
	AddDataToJTableListener addDataToJTableListener = new AddDataToJTableListener();
	addButton.addActionListener(addDataToJTableListener);

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);
    }

    public class AddDataToJTableListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    DefaultTableModel tableModel = (DefaultTableModel) embebedTable.getModel();
	    for (int i=0; i<getFormData().size(); i++) {
		System.out.println("VALUE: " + getFormData().get(i));
	    }
	    //	    tableModel.addRow(getFormData());
	}

    }

    private Vector<String> getFormData() {
	Vector<String> formData = new Vector<String>();
	for (JComponent comp : widgetsVector.values()) {
	    if (comp instanceof JComboBox) {
		if (((JComboBox) comp).getSelectedItem() != null) {
		    formData.add(((JComboBox) comp).getSelectedItem().toString());
		}else {
		    formData.add("");
		}
	    } else if (comp instanceof JTextField) {
		if (!((JTextField) comp).getText().isEmpty()) {
		    formData.add(((JTextField) comp).getText());
		}else {
		    formData.add("");
		}
	    } else if (comp instanceof JTextArea) {
		if (!((JTextArea) comp).getText().isEmpty()) {
		    formData.add(((JTextArea) comp).getText());
		}else {
		    formData.add("");
		}
	    }
	}
	return formData;
    }

    protected void fillValues() {
	for (JComponent comp : widgetsVector.values()) {
	    if (comp instanceof JTextField) {
		fillJTextField((JTextField) comp);
	    } else if (comp instanceof JTextArea) {
		fillJTextArea((JTextArea) comp);
	    } else if (comp instanceof JComboBox) {
		fillJComboBox((JComboBox) comp);
	    }
	}
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

    protected void fillJComboBox(JComboBox combobox) {
	String colName = combobox.getName();
	Value fieldValue = values.get(colName);
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent(colName);
	if (dv != null) {
	    addDomainValuesToComboBox(combobox, dv.getValues());
	    setDomainValueSelected(combobox, fieldValue.toString());
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
	// TODO Auto-generated method stub
	return null;
    }

}
