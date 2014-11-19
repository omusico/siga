package es.icarto.gvsig.extgex.forms.expropiations;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class SubformExpropiationsAddExpropiation extends JPanel implements
	IWindow, ActionListener {

    private final FormPanel form;

    private JTextField superficie;
    private JComboBox cultivo;
    private JButton addExpropiationButton;

    private final JTable expropiationsTable;

    protected WindowInfo viewInfo = null;
    private final String title = "Añadir Expropiación";
    private final int width = 300;
    private final int height = 80;

    private Color defaultbg;
    private static final Color invalidBg = new Color(249, 112, 140);

    public SubformExpropiationsAddExpropiation(JTable expropiationsTable) {
	InputStream stream = getClass().getClassLoader().getResourceAsStream(
		"forms/add_expropiaciones.xml");
	FormPanel result = null;
	try {
	    result = new FormPanel(stream);
	    this.add(result);
	} catch (FormException e) {
	    e.printStackTrace();
	}
	this.form = result;
	this.expropiationsTable = expropiationsTable;
	initWidgets();
    }

    private void initWidgets() {

	ImageComponent image = (ImageComponent) form
		.getComponentByName("image");
	ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	addExpropiationButton = (JButton) form
		.getComponentByName(DBNames.SUBFORMEXPROPIATION_ADD_EXPROPIATION_BUTTON);
	addExpropiationButton.addActionListener(this);

	superficie = (JTextField) form
		.getComponentByName(DBNames.SUBFORMEXPROPIATION_SUPERFICIE);
	defaultbg = superficie.getBackground();
	cultivo = (JComboBox) form
		.getComponentByName(DBNames.SUBFORMEXPROPIATION_CULTIVO);
	for (String tipo_cultivo : getTiposCultivo()) {
	    cultivo.addItem(tipo_cultivo);
	}
    }

    private ArrayList<String> getTiposCultivo() {
	ArrayList<String> tiposCultivo = new ArrayList<String>();
	PreparedStatement statement;
	String query = "SELECT " + DBNames.FIELD_DESCRIPCION_CULTIVOS + " "
		+ "FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_CULTIVOS
		+ ";";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareCall(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		tiposCultivo.add(rs.getString(1));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return tiposCultivo;
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

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == addExpropiationButton) {
	    if (notValidate()) {
		superficie.setBackground(invalidBg);
		return;
	    }
	    superficie.setBackground(defaultbg);
	    Value[] expropiationData = getExpropiationData();
	    DefaultTableModel tableModel = (DefaultTableModel) expropiationsTable
		    .getModel();
	    tableModel.addRow(expropiationData);
	}
    }

    private boolean notValidate() {
	final String superficieText = superficie.getText();
	if (!superficieText.isEmpty()) {
	    try {
		Integer.parseInt(superficieText);
	    } catch (NumberFormatException nfe) {
		return true;
	    }
	}
	return false;
    }

    private Value[] getExpropiationData() {
	Value[] expropiationData = new Value[2];
	if (!superficie.getText().isEmpty()) {
	    expropiationData[0] = ValueFactory
		    .createValue(superficie.getText());
	}
	if (!cultivo.getSelectedItem().toString().isEmpty()) {
	    expropiationData[1] = ValueFactory.createValue((cultivo
		    .getSelectedItem().toString()));
	}
	return expropiationData;
    }

}
