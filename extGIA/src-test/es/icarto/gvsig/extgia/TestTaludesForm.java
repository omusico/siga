package es.icarto.gvsig.extgia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainValues;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.navtableforms.utils.LayerController;
import es.icarto.gvsig.navtableforms.validation.rules.ValidationRule;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TestTaludesForm {

    private static FormPanel form;
    private HashMap<String, JComponent> widgets;

    @BeforeClass
    public static void doSetupBeforeClass() throws Exception {
	form = new FormPanel(TaludesForm.ABEILLE_FILENAME);
	initializegvSIGDrivers();
	DBSession.createConnection("localhost", 5434, "audasa_test",
		"audasa_extgia_dominios", "postgres", "postgres");
    }

    private static void initializegvSIGDrivers() throws Exception {
	final String fwAndamiDriverPath = "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";
	final File baseDriversPath = new File(fwAndamiDriverPath);
	if (!baseDriversPath.exists()) {
	    throw new Exception("Can't find drivers path: "
		    + fwAndamiDriverPath);
	}

	LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
	if (LayerFactory.getDM().getDriverNames().length < 1) {
	    throw new Exception("Can't find drivers in path: "
		    + fwAndamiDriverPath);
	}
    }

    @Before
    public void doSetup() {
	this.widgets = AbeilleParser.getWidgetsFromContainer(form);
    }

    // TODO: abeille-xml files must be in the classpath, new
    // FormPanel(abeillePath) does't work in other case. To handle this: Run
    // Configuration -> TestTaludesForm -> Classpath -> Advanced -> add
    // extGIA/forms
    // see
    // http://java.net/nonav/projects/abeille/lists/users/archive/2005-06/message/7
    @Test
    @Ignore
    public void test_allWidgetsHaveName() {

	for (final JComponent widget : this.widgets.values()) {
	    assertNotNull(widget.getName());
	    assertTrue(widget.getName().trim().length() > 0);
	}
    }

    @Test
    @Ignore
    public void test_domainValuesMatchWidgetNames() throws Exception {

	final HashMap<String, DomainValues> domainValues = ORMLite
		.getAplicationDomainObject("data/audasa.xml").getDomainValues();

	for (final String domainValue : domainValues.keySet()) {
	    assertNotNull(domainValue, this.widgets.get(domainValue));
	}
    }

    @Test
    @Ignore
    public void test_validationRulesMatchWidgetNames() throws Exception {

	final HashMap<String, Set<ValidationRule>> validationRules = ORMLite
		.getAplicationDomainObject("data/audasa.xml")
		.getValidationRules();

	for (final String validationRule : validationRules.keySet()) {
	    assertNotNull(validationRule, this.widgets.get(validationRule));
	}
    }

    @Test
    @Ignore
    public void test_widgetsWithOutDatabaseField() throws SQLException {
	final Set<String> columnsSet = getColums();

	for (final JComponent widget : this.widgets.values()) {

	    if (!(widget instanceof JTable)) {
		assertTrue(widget.getName(),
			columnsSet.contains(widget.getName()));
	    }
	}
    }

    private Set<String> getColums() throws SQLException {
	final String[] columns = DBSession.getCurrentSession().getColumns(
		"audasa_extgia", "taludes");

	final Set<String> columnsSet = new HashSet<String>(
		Arrays.asList(columns));
	return columnsSet;
    }

    @Test
    public void test_writeValues() throws Exception {
	HashMap<String, String> validValues = getValidValues();
	FLyrVect layer = (FLyrVect) DBSession.getCurrentSession().getLayer(
		"taludes", "taludes", "audasa_extgia", "",
		CRSFactory.getCRS("EPSG:23030"));

	TaludesForm dialog = new TaludesForm(layer);
	dialog.init();
	LayerController formController = dialog.getFormController();
	HashMap<String, JComponent> myWidgets = dialog.getWidgetComponents();

	for (JComponent c : myWidgets.values()) {
	    String name = c.getName();
	    String value = validValues.get(name);
	    if (value == null) {
		System.out.println("un widget al que no he asignado valor: "
			+ name);
		continue;
	    }
	    if (c instanceof JFormattedTextField) {
		((JFormattedTextField) c).setText(value);
		formController.setValue(name, value);
	    } else if (c instanceof JTextField) {
		((JTextField) c).setText(value);
		formController.setValue(name, value);
	    } else if (c instanceof JComboBox) {
		// ((JComboBox) c).setSelectedItem(value);
		// formController.setValue(name, value);
	    } else if (c instanceof JCheckBox) {
		boolean selected = value.equals("true");
		((JCheckBox) c).setSelected(selected);
		formController.setValue(name, value);
	    } else if (c instanceof JTextArea) {
		((JTextArea) c).setText(value);
		formController.setValue(name, value);
	    }
	}

	dialog.setChangedValues();
	dialog.saveRecord();
	dialog.refreshGUI();

	for (String name : validValues.keySet()) {
	    assertEquals(name, validValues.get(name), dialog
		    .getFormController().getValueInLayer(name));
	}

    }

    // test_comboboxWidgetHasADomainValue
    // test_comboboxWidgetsWithOutDomain
    // test_widgetWithOutValidationRule

    private HashMap<String, String> getValidValues() {
	HashMap<String, String> validValues = new HashMap<String, String>();
	validValues.put("id_talud", "N-583D");
	validValues.put("numero_talud", "583");
	// validValues.put("tramo", "0");
	// validValues.put("base_contratista", "Sur");
	// validValues.put("tipo_via_pi", "0");
	validValues.put("nombre_pi", "0");
	validValues.put("pk_inicial_pi", "5");
	validValues.put("ramal_pi", "0");
	// validValues.put("tipo_via_pf", "0");
	validValues.put("nombre_pf", "0");
	validValues.put("pk_final_pf", "0");
	validValues.put("ramal_pf", "0");
	validValues.put("sentido", "0");
	validValues.put("margen", "0");
	// validValues.put("tipo_talud", "0");
	validValues.put("roca", "false");
	validValues.put("arboles", "true");
	validValues.put("gunita", "false");
	validValues.put("escollera", "false");
	validValues.put("gaviones", "false");
	validValues.put("malla", "false");
	validValues.put("observaciones", "0");
	validValues.put("arcen", "false");
	validValues.put("barrera_seguridad", "true");
	validValues.put("cuneta_pie", "false");
	validValues.put("cuneta_pie_revestida", "false");
	validValues.put("cuneta_cabeza", "false");
	validValues.put("cuneta_cabeza_revestida", "false");
	validValues.put("berma", "true");
	validValues.put("longitud", "0");
	validValues.put("sector", "0");
	validValues.put("inclinacion_media", "0");
	validValues.put("altura_max_talud", "0");
	validValues.put("sup_total_analitica", "0");
	validValues.put("sup_mecanizada_analitica", "0");
	validValues.put("sup_manual_analitica", "0");
	validValues.put("sup_restada_analitica", "0");
	validValues.put("sup_total_campo", "8");
	validValues.put("sup_mecanizada_real", "0");
	validValues.put("sup_restada_campo", "0");
	validValues.put("sup_manual_real", "0");
	validValues.put("sup_complementaria", "0");
	validValues.put("concepto", "0");
	validValues.put("sup_desbrozada_y_complementaria", "0");
	return validValues;
    }
}
