package es.icarto.gvsig.extgia.consultas;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

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

    private static final String CSV_SEPARATOR = "\t";

    public static String ABEILLE_FILENAME = "forms/consultas_inventario.jfrm";

    private final Connection connection = DBSession.getCurrentSession().getJavaConnection();
    private final Locale loc = new Locale("es");

    private static final int TRABAJOS = 0;
    private static final int RECONOCIMIENTOS = 1;
    private static final int TRABAJOS_FIRME = 2;
    private static final int RECONOCIMIENTOS_FIRME = 3;

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
    private JRadioButton pdfRadioButton;
    private JRadioButton csvRadioButton;
    private JButton launchButton;

    private UpdateBaseContratistaListener updateBaseContratistaListener;
    private UpdateTramoListener updateTramoListener;

    private boolean isReportOfSeveralElements = false;

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

	pdfRadioButton = (JRadioButton) form.getComponentByName("pdf");
	pdfRadioButton.setSelected(true);

	csvRadioButton = (JRadioButton) form.getComponentByName("csv");

	ButtonGroup group = new ButtonGroup();
	group.add(pdfRadioButton);
	group.add(csvRadioButton);

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

	    Date fechaInicial = fechaInicio.getDate();
	    Date fechaFinal = fechaFin.getDate();

	    String[] filters = getFilters(fechaInicial, fechaFinal);

	    if (!isCheckingOK()) {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar al menos Elemento y Tipo");
		return;
	    }

	    if (elemento.getSelectedItem().toString().equals("-TODOS-")) {
		isReportOfSeveralElements = true;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showSaveDialog(this);
		File outputPath = fileChooser.getSelectedFile();
		if (outputPath != null) {
		    if (pdfRadioButton.isSelected()) {
			for (String[] element: getElements(tipoConsulta.getSelectedItem().toString())) {
			    generateReportFile(element,
				    outputPath + File.separator + element[0] + ".pdf",
				    fechaInicial, fechaFinal, filters);
			}
		    }else {
			for (String[] element: getElements(tipoConsulta.getSelectedItem().toString())) {
			    generateReportFile(element,
				    outputPath + File.separator + element[0] + ".csv",
				    fechaInicial, fechaFinal, filters);
			}
		    }
		    Object[] reportGeneratedOptions = { "Abrir directorio", "Cerrar" };
		    int m = JOptionPane.showOptionDialog(
			    null,
			    "Ficheros generados con �xito en: \n" + "\""
				    + outputPath.getAbsolutePath() + "\"", null,
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.INFORMATION_MESSAGE, null,
				    reportGeneratedOptions, reportGeneratedOptions[1]);

		    if (m == JOptionPane.OK_OPTION) {
			Desktop d = Desktop.getDesktop();
			try {
			    d.open(outputPath);
			} catch (IOException e1) {
			    e1.printStackTrace();
			}
		    }
		}
	    }else {
		String[] element = new String[2];
		element[0] = ((KeyValue) elemento.getSelectedItem()).getKey();
		element[1] =	((KeyValue) elemento.getSelectedItem()).getValue();
		SaveFileDialog sfd;
		if (elementHasType(element[0], tipoConsulta.getSelectedItem().toString())) {
		    if (pdfRadioButton.isSelected()) {
			sfd = new SaveFileDialog("PDF Files", "pdf");
		    }else {
			sfd = new SaveFileDialog("CSV Files", "csv");
		    }
		    File outputFile = sfd.showDialog();
		    if (outputFile != null) {
			generateReportFile(element,
				outputFile.getAbsolutePath(),
				fechaInicial, fechaFinal, filters);
		    }
		}else {
		    JOptionPane.showMessageDialog(null,
			    "No se puede realizar esta consulta para el elemento seleccionado");
		}
	    }
	}
    }

    private void generateReportFile(String[] element, String outputFile, Date fechaInicial,
	    Date fechaFinal, String[] filters) {

	int tipo = -1;
	String elementId = getElementId(element[0]);
	String fields = "";

	if (tipoConsulta.getSelectedItem().toString().equals("Trabajos")) {
	    if (elemento.getSelectedItem().toString().equals("Firme")) {
		fields = getFirmeTrabajosFieldNames(elementId);
		tipo = TRABAJOS_FIRME;
	    }else {
		fields = getTrabajosFieldNames(elementId);
		tipo = TRABAJOS;
	    }
	}else if(tipoConsulta.getSelectedItem().toString().equals("Inspecciones")) {
	    if (elemento.getSelectedItem().toString().equals("Firme")) {
		fields = getFirmeReconocimientosFieldNames(elementId);
		tipo = RECONOCIMIENTOS_FIRME;
	    }else {
		fields = getReconocimientosFieldNames(elementId);
		tipo = RECONOCIMIENTOS;
	    }
	}

	String query = getReportQuery(tipo, fechaInicial, fechaFinal, element[0],
		elementId, fields);

	if (pdfRadioButton.isSelected()) {
	    createPdfReport(tipo, outputFile, element, filters, query);
	}else {
	    createCsvReport(outputFile, query);
	}
    }

    private void createPdfReport(int tipo, String outputFile, String[] element, String[] filters, String query) {
	if (outputFile != null) {

	    PreparedStatement statement;
	    try {
		statement = connection.prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();

		if (!queryHasResults(rs)) {
		    return;
		}

		if (tipo == TRABAJOS) {
		    new TrabajosReport(
			    element[1],
			    outputFile, rs, filters);
		}else if (tipo == TRABAJOS_FIRME) {
		    new FirmeTrabajosReport(
			    element[1],
			    outputFile, rs, filters);
		}else if (tipo == RECONOCIMIENTOS_FIRME) {
		    new FirmeReconocimientosReport(
			    element[1],
			    outputFile, rs, filters);
		}else {
		    new ReconocimientosReport(
			    element[1],
			    outputFile, rs, filters);
		}

		if (!isReportOfSeveralElements) {
		    Object[] reportGeneratedOptions = { "Ver listado", "Cerrar" };
		    int m = JOptionPane.showOptionDialog(
			    null,
			    "Listado generado con �xito en: \n" + "\""
				    + outputFile + "\"", null,
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.INFORMATION_MESSAGE, null,
				    reportGeneratedOptions, reportGeneratedOptions[1]);

		    if (m == JOptionPane.OK_OPTION) {
			Desktop d = Desktop.getDesktop();
			try {
			    d.open(new File(outputFile));
			} catch (IOException e1) {
			    e1.printStackTrace();
			}
		    }
		}
	    } catch (SQLException e1) {
		e1.printStackTrace();
		return;
	    }
	}
    }

    private void createCsvReport(String outputFile, String query) {
	PreparedStatement statement;

	if (outputFile != null) {
	    try {
		statement = connection.prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		ResultSetMetaData rsMetaData = rs.getMetaData();

		if (!queryHasResults(rs)) {
		    return;
		}

		FileWriter writer = new FileWriter(outputFile);

		for (int i=0; i<rsMetaData.getColumnCount(); i++) {
		    writer.append(rsMetaData.getColumnName(i+1));
		    writer.append(CSV_SEPARATOR);
		}
		writer.append("\n");

		while (rs.next()) {
		    for (int i=0; i<rsMetaData.getColumnCount(); i++) {
			writer.append(rs.getString(i+1));
			writer.append(CSV_SEPARATOR);
		    }
		    writer.append("\n");
		}

		writer.flush();
		writer.close();

	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }

	    if (!isReportOfSeveralElements) {
		JOptionPane.showMessageDialog(null,
			"Archivo generado con �xito en: \n" +
				outputFile);
	    }
	}
    }

    private boolean queryHasResults(ResultSet rs) throws SQLException {
	if (!rs.next()) {
	    if (!isReportOfSeveralElements) {
		JOptionPane.showMessageDialog(null,
			"La consulta produjo 0 resultados");
		return false;
	    }else {
		return false;
	    }
	}
	return true;
    }

    private boolean isCheckingOK() {
	if (!elemento.getSelectedItem().toString().equals(" ") &&
		!tipoConsulta.getSelectedItem().toString().equals(" ")) {
	    return true;
	}else {
	    return false;
	}
    }

    private boolean elementHasType(String element, String tipoConsulta) {
	boolean type = false;
	PreparedStatement statement;
	String query = "SELECT " + tipoConsulta + " FROM audasa_extgia_dominios.elemento " +
		"WHERE id = '" + element + "';";
	try {
	    statement = connection.prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		type = rs.getBoolean(1);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return type;
    }

    private ArrayList<String[]> getElements(String tipoConsulta) {
	ArrayList<String[]> elements = new ArrayList<String[]>();
	PreparedStatement statement;
	String query = "SELECT id, item FROM audasa_extgia_dominios.elemento " +
		"WHERE id <> 'todos' AND id <> ' ' AND " + tipoConsulta + " = " + "true;";
	try {
	    statement = connection.prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		String[] element = new String[2];
		element[0] = rs.getString(1);
		element[1] = rs.getString(2);
		elements.add(element);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return elements;
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

	if (tipo == TRABAJOS || tipo == TRABAJOS_FIRME) {
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

    private String getFirmeTrabajosFieldNames(String elementId) {
	return elementId + ", fecha, pk_inicial, pk_final, sentido, " +
		"descripcion, fecha_certificado";
    }

    private String getReconocimientosFieldNames(String elementId) {
	return elementId + ", nombre_revisor, fecha_inspeccion, indice_estado, observaciones";
    }

    private String getFirmeReconocimientosFieldNames(String elementId) {
	return elementId + ", tipo_inspeccion, nombre_revisor, aparato_medicion," +
		"fecha_inspeccion, observaciones";
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