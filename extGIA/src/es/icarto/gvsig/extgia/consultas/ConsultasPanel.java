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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;
import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.audasacommons.forms.reports.SaveFileDialog;
import es.icarto.gvsig.extgia.consultas.agregados.CSVTrabajosAgregadosIsletasReport;
import es.icarto.gvsig.extgia.consultas.agregados.CSVTrabajosAgregadosTaludesReport;
import es.icarto.gvsig.extgia.consultas.agregados.TrabajosAgregadosIsletasReport;
import es.icarto.gvsig.extgia.consultas.agregados.TrabajosAgregadosTaludesReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.CSVCaracteristicasQueries;
import es.icarto.gvsig.extgia.consultas.caracteristicas.PDFCaracteristicasQueries;
import es.icarto.gvsig.extgia.consultas.firme.FirmeReconocimientosReport;
import es.icarto.gvsig.extgia.consultas.firme.FirmeTrabajosReport;
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

    private static final int TRABAJOS = 0;
    private static final int RECONOCIMIENTOS = 1;
    private static final int TRABAJOS_FIRME = 2;
    private static final int RECONOCIMIENTOS_FIRME = 3;
    private static final int CARACTERISTICAS = 4;
    private static final int TRABAJOS_AGREGADOS = 5;

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

    private TipoConsultaListener tipoConsultaListener;
    private UpdateBaseContratistaListener updateBaseContratistaListener;
    private UpdateTramoListener updateTramoListener;

    private boolean isReportOfSeveralElements = false;

    private ConsultasFilters consultasFilters;

    public ConsultasPanel() {
	InputStream stream = getClass().getClassLoader().
		getResourceAsStream(ABEILLE_FILENAME);
	FormPanel result = null;

	try {
	    result = new FormPanel(stream);
	} catch (FormException e) {
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
	KeyValue blankValue = new KeyValue(" ", " ");
	elemento.addItem(blankValue);
	KeyValue allItemsValue = new KeyValue("todos", "-TODOS-");
	elemento.addItem(allItemsValue);
	setComboBoxValues(elemento);
	tipoConsulta = (JComboBox) widgetsVector.get("tipo_consulta");
	setComboBoxValues(tipoConsulta);
	areaMantenimiento = (JComboBox) widgetsVector.get("area_mantenimiento");
	setComboBoxValues(areaMantenimiento);
	baseContratista = (JComboBox) widgetsVector.get("base_contratista");
	setComboBoxValues(baseContratista);
	tramo = (JComboBox) widgetsVector.get("tramo");
	setComboBoxValues(tramo);

	tipoConsultaListener = new TipoConsultaListener();
	updateBaseContratistaListener = new UpdateBaseContratistaListener();
	updateTramoListener = new UpdateTramoListener();

	tipoConsulta.addActionListener(tipoConsultaListener);
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

	    consultasFilters = new ConsultasFilters(
		    getFilterAreaValue(),
		    getFilterBaseContratistaValue(),
		    getFilterTramoValue(),
		    fechaInicio.getDate(),
		    fechaFin.getDate());

	    if (!isCheckingOK()) {
		JOptionPane.showMessageDialog(null,
			PluginServices.getText(this, "elementAndTypeUnselected_msg"));
		return;
	    }

	    if (!isCheckingTrabajosAgregadosOK()) {
		JOptionPane.showMessageDialog(null,
			PluginServices.getText(this, "unavailableQuery_msg"));
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
				    consultasFilters.getFechaInicio(), consultasFilters.getFechaFin(),
				    consultasFilters);
			}
		    }else {
			for (String[] element: getElements(tipoConsulta.getSelectedItem().toString())) {
			    generateReportFile(element,
				    outputPath + File.separator + element[0] + ".csv",
				    consultasFilters.getFechaInicio(), consultasFilters.getFechaFin(),
				    consultasFilters);
			}
		    }
		    Object[] reportGeneratedOptions = {PluginServices.getText(this, "reportGeneratedOptions_open"),
			    PluginServices.getText(this, "reportGeneratedOptions_close")};
		    int m = JOptionPane.showOptionDialog(
			    null,
			    PluginServices.getText(this, "filesGenerated_msg") + "\""
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
		if (SqlUtils.elementHasType(element[0], tipoConsulta.getSelectedItem().toString())) {
		    if (pdfRadioButton.isSelected()) {
			sfd = new SaveFileDialog(PluginServices.getText(this, "pdfFiles"), "pdf");
		    }else {
			sfd = new SaveFileDialog(PluginServices.getText(this, "csvFiles"), "csv");
		    }
		    File outputFile = sfd.showDialog();
		    if (outputFile != null) {
			generateReportFile(element,
				outputFile.getAbsolutePath(),
				consultasFilters.getFechaInicio(),
				consultasFilters.getFechaFin(),
				consultasFilters);
		    }
		}else {
		    JOptionPane.showMessageDialog(null,
			    PluginServices.getText(this, "unavailableQuery_msg"));
		}
	    }
	}
    }

    private boolean isCheckingTrabajosAgregadosOK() {
	if (tipoConsulta.getSelectedItem().toString().equals("Trabajos Agrupados")) {
	    if (elemento.getSelectedItem().toString().equals("Taludes") ||
		    elemento.getSelectedItem().toString().equals("Isletas")) {
		return true;
	    }else {
		return false;
	    }
	}else {
	    return true;
	}
    }

    private void generateReportFile(String[] element, String outputFile, Date fechaInicial,
	    Date fechaFinal, ConsultasFilters filters) {

	int tipo = -1;
	String elementId = ConsultasFieldNames.getElementId(element[0]);
	String fields = "";

	if (tipoConsulta.getSelectedItem().toString().equals("Trabajos")) {
	    if (elemento.getSelectedItem().toString().equals("Firme") || element[1].equals("Firme")) {
		fields = ConsultasFieldNames.getFirmeTrabajosFieldNames(elementId);
		tipo = TRABAJOS_FIRME;
	    }else {
		fields = ConsultasFieldNames.getTrabajosFieldNames(elementId);
		tipo = TRABAJOS;
	    }
	}else if(tipoConsulta.getSelectedItem().toString().equals("Inspecciones")) {
	    if (elemento.getSelectedItem().toString().equals("Firme") || element[1].equals("Firme")) {
		fields = ConsultasFieldNames.getFirmeReconocimientosFieldNames(elementId);
		tipo = RECONOCIMIENTOS_FIRME;
	    }else if (!ConsultasFieldNames.hasIndiceFieldOnReconocimientos(element[0])) {
		fields = ConsultasFieldNames.getReconocimientosFieldNamesWithoutIndice(elementId);
		tipo = RECONOCIMIENTOS;
	    }else {
		fields = ConsultasFieldNames.getReconocimientosFieldNames(elementId);
		tipo = RECONOCIMIENTOS;
	    }
	}else if (tipoConsulta.getSelectedItem().toString().equals("Características")) {
	    fields = ConsultasFieldNames.getPDFCaracteristicasFieldNames(element[0]);
	    tipo = CARACTERISTICAS;
	}else if (tipoConsulta.getSelectedItem().toString().equals("Trabajos Agrupados")) {
	    tipo = TRABAJOS_AGREGADOS;
	}

	String query = getReportQuery(tipo, fechaInicial, fechaFinal, element[0],
		elementId, fields);

	if (pdfRadioButton.isSelected()) {
	    if (tipo == TRABAJOS_AGREGADOS) {
		createPdfReportAgregados(outputFile, element, filters);
	    }else {
		createPdfReport(tipo, outputFile, element, filters, query);
	    }
	}else {
	    if (tipo == TRABAJOS_AGREGADOS) {
		createCsvReportAgregados(outputFile, element, filters);
	    }else {
		createCsvReport(outputFile, query, consultasFilters);
	    }
	}
    }

    private void createPdfReportAgregados(String outputFile, String[] element,
	    ConsultasFilters filters) {
	if (element[0].equals("Taludes")) {
	    new TrabajosAgregadosTaludesReport(element, outputFile, null, filters);
	}else if (element[0].equals("Isletas")){
	    new TrabajosAgregadosIsletasReport(element, outputFile, null, filters);
	}
	showOpenSingleReportDialog(outputFile);
    }

    private void createCsvReportAgregados(String outputFile, String[] element, ConsultasFilters filters) {
	if (element[0].equals("Taludes")) {
	    new CSVTrabajosAgregadosTaludesReport(outputFile, consultasFilters);
	}else if (element[0].equals("Isletas")) {
	    new CSVTrabajosAgregadosIsletasReport(outputFile, consultasFilters);
	}
	JOptionPane.showMessageDialog(null,
		PluginServices.getText(this, "csvReportGenerated_msg") +
		outputFile);
    }

    private void createPdfReport(int tipo, String outputFile, String[] element,
	    ConsultasFilters filters, String query) {
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
		}else if (tipo == CARACTERISTICAS) {
		    ConsultasFieldNames.createCaracteristicasReport(element, outputFile, rs, filters);
		}else {
		    new ReconocimientosReport(
			    element[1],
			    outputFile, rs, filters);
		}

		if (!isReportOfSeveralElements) {
		    showOpenSingleReportDialog(outputFile);
		}
	    } catch (SQLException e1) {
		e1.printStackTrace();
		return;
	    }
	}
    }

    private void showOpenSingleReportDialog(String outputFile) {
	Object[] reportGeneratedOptions = {PluginServices.getText(this,
		"singleReportGeneratedOptions_open"), PluginServices.getText(this,
			"singleReportGeneratedOptions_close")};
	int m = JOptionPane.showOptionDialog(
		null,
		PluginServices.getText(this, "reportGenerated_msg") + "\""
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

    private void createCsvReport(String outputFile, String query, ConsultasFilters filters) {
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

		new CSVReport(outputFile, rsMetaData, rs, filters);

	    } catch (SQLException e) {
		e.printStackTrace();
	    }

	    if (!isReportOfSeveralElements) {
		JOptionPane.showMessageDialog(null,
			PluginServices.getText(this, "csvReportGenerated_msg") +
			outputFile);
	    }
	}
    }

    private boolean queryHasResults(ResultSet rs) throws SQLException {
	if (!rs.next()) {
	    if (!isReportOfSeveralElements) {
		JOptionPane.showMessageDialog(null,
			PluginServices.getText(this, "queryWithoutResults_msg"));
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

    private ArrayList<String[]> getElements(String tipoConsulta) {
	ArrayList<String[]> elements = new ArrayList<String[]>();
	PreparedStatement statement;
	String query;
	if (tipoConsulta.equals("Características")) {
	    query = "SELECT id, item FROM audasa_extgia_dominios.elemento " +
		    "WHERE id_fieldname <> ' '";
	}else {
	    query = "SELECT id, item FROM audasa_extgia_dominios.elemento " +
		    "WHERE id <> 'todos' AND id <> ' ' AND " + tipoConsulta + " = " + "true;";
	}
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
	String query;

	if (tipo == TRABAJOS_AGREGADOS) {
	    query = "SELECT 1=1";
	    return query;
	}
	if (tipo == CARACTERISTICAS) {
	    if (csvRadioButton.isSelected()) {
		query = CSVCaracteristicasQueries.getCSVCaracteristicasQuery(element, consultasFilters);
	    }else {
		query = PDFCaracteristicasQueries.getPDFCaracteristicasQuery(element, consultasFilters);
	    }
	}else {
	    query = "SELECT " + fields + " FROM " +
		    DBFieldNames.GIA_SCHEMA + "." +
		    element + "_" + ((KeyValue) tipoConsulta.getSelectedItem()).getKey();
	}

	if (!consultasFilters.getWhereClauseByLocationWidgets(false).isEmpty()) {
	    if (tipo == CARACTERISTICAS) {
		//		query = query + " WHERE " + elementId + " IN (SELECT " + elementId +
		//			" FROM " + DBFieldNames.GIA_SCHEMA + "." + element +
		//			consultasFilters.getWhereClauseByLocationWidgets(false) + ");";
	    }else {
		query = query + " WHERE " + elementId + " IN (SELECT " + elementId +
			" FROM " + DBFieldNames.GIA_SCHEMA + "." + element +
			consultasFilters.getWhereClauseByLocationWidgets(false);
	    }
	}

	if (tipo == CARACTERISTICAS) {
	    return query;
	}else if (tipo == TRABAJOS || tipo == TRABAJOS_FIRME) {
	    query = query + consultasFilters.getWhereClauseByDates("fecha_certificado");
	}else {
	    query = query + consultasFilters.getWhereClauseByDates("fecha_inspeccion");
	}
	return query;
    }

    private KeyValue getFilterAreaValue() {
	KeyValue areaValue = null;
	if (!areaMantenimiento.getSelectedItem().toString().equals(" ")) {
	    areaValue = ((KeyValue) areaMantenimiento.getSelectedItem());
	}
	return areaValue;
    }

    private KeyValue getFilterBaseContratistaValue() {
	KeyValue baseValue = null;
	if (!baseContratista.getSelectedItem().toString().equals(" ")) {
	    baseValue = ((KeyValue) baseContratista.getSelectedItem());
	}
	return baseValue;
    }

    private KeyValue getFilterTramoValue() {
	KeyValue tramoValue = null;
	if (!tramo.getSelectedItem().toString().equals(" ")) {
	    tramoValue = ((KeyValue) tramo.getSelectedItem());
	}
	return tramoValue;
    }

    public class TipoConsultaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (tipoConsulta.getSelectedItem().toString().equals("Características")) {
		fechaInicio.setEnabled(false);
		fechaFin.setEnabled(false);
	    }else {
		fechaInicio.setEnabled(true);
		fechaFin.setEnabled(true);
	    }
	}
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
