package es.icarto.gvsig.extgia.consultas;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;
import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.audasacommons.forms.reports.SaveFileDialog;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class ConsultasPanel extends JPanel implements IWindow, ActionListener {

    public static String ABEILLE_FILENAME = "forms/consultas_inventario.jfrm";

    private final FormPanel form;
    private final ORMLite ormLite;

    protected WindowInfo viewInfo = null;
    private String title;
    private final int width = 430;
    private final int height = 330;

    private JComboBox elemento;
    private JComboBox tipoConsulta;
    private JComboBox areaMantenimiento;
    private JComboBox baseContratista;
    private JComboBox tramo;
    private final JDateChooser fechaInicio;
    private final JDateChooser fechaFin;
    private JButton launchButton;

    public ConsultasPanel() {
	InputStream stream = getClass().getClassLoader().
		getResourceAsStream(ABEILLE_FILENAME);
	FormPanel result = null;

	try {
	    result = new FormPanel(stream);
	} catch (FormException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	this.add(result);

	// Setting name of JTextFieldDateEditors since NTForms gets an error if it is null
	fechaInicio = (JDateChooser) result.getComponentByName("fecha_inicio");
	fechaInicio.getDateEditor().getUiComponent().setName("fecha_inicio_TF");
	fechaFin = (JDateChooser) result.getComponentByName("fecha_fin");
	fechaFin.getDateEditor().getUiComponent().setName("fecha_fin_TF");

	this.form = result;
	ormLite = new ORMLite(getClass().getClassLoader()
		.getResource("rules/consultas_metadata.xml")
		.getPath());
	initWidgets();
    }

    private void initWidgets() {
	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	HashMap<String, JComponent> widgetsVector = AbeilleParser.getWidgetsFromContainer(form);

	elemento = (JComboBox) widgetsVector.get("elemento");
	setComboBoxValues(elemento);
	tipoConsulta = (JComboBox) widgetsVector.get("tipo_consulta");
	setComboBoxValues(tipoConsulta);
	areaMantenimiento = (JComboBox) widgetsVector.get("area_mantenimiento");
	setComboBoxValues(areaMantenimiento);
	baseContratista = (JComboBox) widgetsVector.get("base_contratista");
	setComboBoxValues(baseContratista);
	tramo = (JComboBox) widgetsVector.get("tramo");
	setComboBoxValues(tramo);

	launchButton = (JButton) form.getComponentByName("launch_button");
	launchButton.addActionListener(this);
    }

    private void setComboBoxValues(JComboBox cb) {
	String comboBoxName = cb.getName();
	DomainValues dv = ormLite.getAppDomain().getDomainValuesForComponent(comboBoxName);

	for (KeyValue kv : dv.getValues()) {
	    cb.addItem(kv);
	}
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
	if (e.getSource() == launchButton) {
	    SaveFileDialog sfd = new SaveFileDialog("PDF files", "pdf");
	    File outputFile = sfd.showDialog();

	    // 0.AM - 1.BC - 2.Tramo - 3.Mes certificado - 4.Anho certificado
	    String[] filters = new String[5];
	    filters[0] = "-";
	    filters[1] = "-";
	    filters[2] = "-";
	    filters[3] = "-";
	    filters[4] = "-";

	    String fields = "id_area_servicio, fecha, unidad, medicion_contratista, medicion_audasa, " +
		    "observaciones, fecha_certificado";

	    String query = "SELECT " + fields + " FROM " +
		    DBFieldNames.GIA_SCHEMA + "." +
		    ((KeyValue) elemento.getSelectedItem()).getKey() + "_" +
		    ((KeyValue) tipoConsulta.getSelectedItem()).getKey();

	    Date fechaInicial = fechaInicio.getDate();
	    Date fechaFinal = fechaFin.getDate();

	    query = query + " WHERE fecha_certificado BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "'";

	    PreparedStatement statement;
	    Connection connection = DBSession.getCurrentSession().getJavaConnection();
	    try {
		statement = connection.prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();

		TrabajosReport report = new TrabajosReport(
			((KeyValue) elemento.getSelectedItem()).getValue(),
			outputFile.getAbsolutePath(), rs, filters);

		Object[] reportGeneratedOptions = { "Ver informe", "Cerrar" };
		int m = JOptionPane.showOptionDialog(
			null,
			"Informe generado con éxito en: \n" + "\""
				+ outputFile.getAbsolutePath() + "\"", null,
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null,
				reportGeneratedOptions, reportGeneratedOptions[1]);

		if (m == JOptionPane.OK_OPTION) {
		    Desktop d = Desktop.getDesktop();
		    try {
			d.open(outputFile);
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		}
	    } catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }

	}

    }

}
