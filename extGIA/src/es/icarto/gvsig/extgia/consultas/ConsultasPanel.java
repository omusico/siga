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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class ConsultasPanel extends JPanel implements IWindow, ActionListener {

    public static String ABEILLE_FILENAME = "forms/consultas_inventario.jfrm";

    private final Connection connection = DBSession.getCurrentSession().getJavaConnection();
    private final Locale loc = new Locale("es");

    private static final int TRABAJOS = 0;
    private static final int RECONOCIMIENTOS = 1;

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

    private UpdateBaseContratistaListener updateBaseContratistaListener;
    private UpdateTramoListener updateTramoListener;

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

	Calendar calendar = Calendar.getInstance();
	// Setting name of JTextFieldDateEditors since NTForms gets an error if it is null
	fechaInicio = (JDateChooser) result.getComponentByName("fecha_inicio");
	fechaInicio.getDateEditor().getUiComponent().setName("fecha_inicio_TF");
	fechaFin = (JDateChooser) result.getComponentByName("fecha_fin");
	fechaFin.getDateEditor().getUiComponent().setName("fecha_fin_TF");
	fechaInicio.setDate(calendar.getTime());
	fechaFin.setDate(calendar.getTime());

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

	updateBaseContratistaListener = new UpdateBaseContratistaListener();
	updateTramoListener = new UpdateTramoListener();

	areaMantenimiento.addActionListener(updateBaseContratistaListener);
	baseContratista.addActionListener(updateTramoListener);

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
	    int tipo = -1;

	    Date fechaInicial = fechaInicio.getDate();
	    Date fechaFinal = fechaFin.getDate();

	    String[] filters = getFilters(fechaInicial, fechaFinal);

	    String element = ((KeyValue) elemento.getSelectedItem()).getKey();
	    String elementId = getElementId(element);

	    String fields = "";
	    if (tipoConsulta.getSelectedItem().toString().equals("Trabajos")) {
		fields = getTrabajosFieldNames(elementId);
		tipo = TRABAJOS;
	    }else if(tipoConsulta.getSelectedItem().toString().equals("Inspecciones")) {
		fields = getReconocimientosFieldNames(elementId);
		tipo = RECONOCIMIENTOS;
	    }

	    String query = getReportQuery(tipo, fechaInicial, fechaFinal, element,
		    elementId, fields);

	    if (!isCheckingOK()) {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar al menos Elemento y Tipo");
		return;
	    }

	    createPdfReport(tipo, filters, query);
	}
    }

    private void createPdfReport(int tipo, String[] filters, String query) {
	SaveFileDialog sfd = new SaveFileDialog("PDF files", "pdf");
	File outputFile = sfd.showDialog();

	PreparedStatement statement;
	try {
	    statement = connection.prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    if (tipo == TRABAJOS) {
		TrabajosReport report = new TrabajosReport(
			((KeyValue) elemento.getSelectedItem()).getValue(),
			outputFile.getAbsolutePath(), rs, filters);
	    }else {
		ReconocimientosReport report = new ReconocimientosReport(
			((KeyValue) elemento.getSelectedItem()).getValue(),
			outputFile.getAbsolutePath(), rs, filters);
	    }

	    Object[] reportGeneratedOptions = { "Ver listado", "Cerrar" };
	    int m = JOptionPane.showOptionDialog(
		    null,
		    "Listado generado con éxito en: \n" + "\""
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

    private boolean isCheckingOK() {
	if (!elemento.getSelectedItem().toString().equals(" ") &&
		!tipoConsulta.getSelectedItem().toString().equals(" ")) {
	    return true;
	}else {
	    return false;
	}
    }

    private String getReportQuery(int tipo, Date fechaInicial, Date fechaFinal,
	    String element, String elementId, String fields) {
	String query = "SELECT " + fields + " FROM " +
		DBFieldNames.GIA_SCHEMA + "." +
		element + "_" + ((KeyValue) tipoConsulta.getSelectedItem()).getKey();

	if (!getWhereClauseByLocationWidgets().isEmpty()) {
	    query = query + " WHERE " + elementId + " IN (SELECT " + elementId +
		    " FROM " + DBFieldNames.GIA_SCHEMA + "." + element +
		    getWhereClauseByLocationWidgets();
	}

	if (tipo == TRABAJOS) {
	    query = query + getWhereClauseByDates("fecha_certificado", fechaInicial, fechaFinal);
	}else {
	    query = query + getWhereClauseByDates("fecha_inspeccion", fechaInicial, fechaFinal);
	}
	return query;
    }

    private String[] getFilters(Date fechaInicial, Date fechaFinal) {
	// 0.AM - 1.BC - 2.Tramo - 3.FechaInicio - 4.FechaFin
	String[] filters = new String[5];
	if (!areaMantenimiento.getSelectedItem().toString().equals(" ")) {
	    filters[0] = areaMantenimiento.getSelectedItem().toString();
	}else {
	    filters[0] = "-";
	}
	if (!baseContratista.getSelectedItem().toString().equals(" ")) {
	    filters[1] = baseContratista.getSelectedItem().toString();
	}else {
	    filters[1] = "-";
	}
	if (!tramo.getSelectedItem().toString().equals(" ")) {
	    filters[2] = tramo.getSelectedItem().toString();
	}else {
	    filters[2] = "-";
	}
	DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, loc);
	filters[3] = dateFormat.format(fechaInicial);
	filters[4] = dateFormat.format(fechaFinal);
	return filters;
    }

    private String getElementId(String element) {
	PreparedStatement statement;
	String query = "SELECT id_fieldname FROM audasa_extgia_dominios.elemento " +
		"WHERE id = '" + element + "';";
	try {
	    statement = connection.prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.next();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private String getTrabajosFieldNames(String elementId) {
	return elementId + ", fecha, unidad, medicion_contratista, medicion_audasa, " +
		"observaciones, fecha_certificado";
    }

    private String getReconocimientosFieldNames(String elementId) {
	return elementId + ", nombre_revisor, fecha_inspeccion, indice_estado, observaciones";
    }

    private String getWhereClauseByLocationWidgets() {
	String query = "";
	if (!areaMantenimiento.getSelectedItem().toString().equals(" ")) {
	    query = " WHERE area_mantenimiento =  '" +
		    ((KeyValue) areaMantenimiento.getSelectedItem()).getKey() + "'";
	}
	if (!baseContratista.getSelectedItem().toString().equals(" ")) {
	    query = query + " AND base_contratista = '" +
		    ((KeyValue) baseContratista.getSelectedItem()).getKey() + "'";
	}
	if (!tramo.getSelectedItem().toString().equals(" ")) {
	    query = query + " AND tramo = '" +
		    ((KeyValue) tramo.getSelectedItem()).getKey() + "'";
	}
	return query;
    }

    private String getWhereClauseByDates(String fechaField, Date fechaInicial, Date fechaFinal) {
	String query = "";
	if (!getWhereClauseByLocationWidgets().isEmpty()) {
	    query = " ) AND " + fechaField + " BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "'";
	}else {
	    query = " WHERE " + fechaField + " BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "'";
	}
	return query;
    }

    public class UpdateBaseContratistaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    String id = ((KeyValue)areaMantenimiento.getSelectedItem()).getKey();
	    String getBaseContratistaQuery =
		    "SELECT id, item FROM audasa_extgia_dominios.base_contratista" +
			    " WHERE id_am = " + id + ";";
	    baseContratista.removeAllItems();
	    baseContratista.addItem(new KeyValue(" ", " "));
	    if (!id.isEmpty()) {
		for (KeyValue value: SqlUtils.getKeyValueListFromSql(getBaseContratistaQuery)) {
		    baseContratista.addItem(value);
		}
	    }
	}
    }

    public class UpdateTramoListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (baseContratista.getSelectedItem()!=null &&
		    !baseContratista.getSelectedItem().toString().equals(" ")) {
		String id = ((KeyValue) baseContratista.getSelectedItem())
			.getKey();
		String getTramoQuery = "SELECT id, item FROM audasa_extgia_dominios.tramo"
			+ " WHERE id_bc = " + id + ";";
		tramo.removeAllItems();
		tramo.addItem(new KeyValue(" ", " "));
		if (!id.isEmpty()) {
		    for (KeyValue value : SqlUtils
			    .getKeyValueListFromSql(getTramoQuery)) {
			tramo.addItem(value);
		    }
		}
	    }
	}
    }
}
