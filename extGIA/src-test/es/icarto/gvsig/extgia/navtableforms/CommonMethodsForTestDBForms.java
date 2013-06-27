package es.icarto.gvsig.extgia.navtableforms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.ORMLiteAppDomain;
import es.icarto.gvsig.navtableforms.ormlite.XMLSAXParser;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.ValidationRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public abstract class CommonMethodsForTestDBForms {

    private ORMLiteAppDomain ado;
    private FormPanel form;
    private HashMap<String, JComponent> widgets;

    @BeforeClass
    public static void doSetupBeforeClass() {
	try {
	    initializegvSIGDrivers();
	    DBSession.createConnection("localhost", 5432, "audasa_test", null,
		    "postgres", "postgres");

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Before
    public void doSetup() {
	ORMLite ormLite = new ORMLite(getXmlFile());
	ado = ormLite.getAppDomain();
	form = new FormPanel(getAbeilleForm());
	widgets = AbeilleParser.getWidgetsFromContainer(form);
    }

    @Test
    public void test_xmlIsValid() throws SAXException {
	try {
	    new XMLSAXParser(getXmlFile());
	} catch (ParserConfigurationException e) {

	} catch (IOException e) {

	}
    }

    protected abstract String getAbeilleForm();

    // protected abstract ConnectionParameters getConnectionParameters();

    protected abstract String getXmlFile();

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

    // TODO: abeille-xml files must be in the classpath, new
    // FormPanel(abeillePath) does't work in other case. To handle this: Run
    // Configuration -> TestTaludesForm -> Classpath -> Advanced -> add
    // extGIA/forms
    // see
    // http://java.net/nonav/projects/abeille/lists/users/archive/2005-06/message/7
    @Test
    public void test_allWidgetsHaveName() {

	for (final JComponent widget : this.widgets.values()) {
	    assertNotNull(widget.getName());
	    assertTrue(widget.getName().trim().length() > 0);
	}
    }

    @Test
    public void test_domainValuesMatchComboBoxesNames() throws Exception {

	final HashMap<String, DomainValues> domainValues = ado
		.getDomainValues();

	for (final String domainValue : domainValues.keySet()) {
	    JComponent cb = this.widgets.get(domainValue);
	    if (!(cb instanceof JComboBox)) {
		fail(domainValue);
	    }
	}
	assertTrue(true);
    }

    @Test
    public void test_ComboBoxesNamesMatchDomainValues() {
	final HashMap<String, DomainValues> domainValues = ado
		.getDomainValues();

	for (JComponent cb : widgets.values()) {
	    if (cb instanceof JComboBox) {
		if (domainValues.get(cb.getName()) == null) {
		    fail(cb.getName());
		}
	    }
	}
	assertTrue(true);
    }

    @Test
    public void test_validationRulesMatchWidgetNames() throws Exception {

	final HashMap<String, Set<ValidationRule>> validationRules = new HashMap<String, Set<ValidationRule>>();
	for (String key : ado.getDomainValidators().keySet()) {
	    validationRules.put(key, ado.getDomainValidators().get(key)
		    .getRules());
	}
	for (final String validationRule : validationRules.keySet()) {
	    assertNotNull(validationRule, this.widgets.get(validationRule));
	}
    }

    @Test
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
		getSchema(), getTable());

	final Set<String> columnsSet = new HashSet<String>(
		Arrays.asList(columns));
	return columnsSet;
    }

    protected String getSchema() {
	return "audasa_extgia";
    }

    protected abstract String getTable();

}
