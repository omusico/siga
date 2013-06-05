package es.icarto.gvsig.extpm.forms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ormlite.ORMLiteAppDomain;
import es.icarto.gvsig.navtableforms.ormlite.XMLSAXParser;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.TestProperties;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TestPMForm {

    private ORMLiteAppDomain ado;
    private FormPanel formPanel;
    private HashMap<String, JComponent> widgets;
    IValidatableForm form = null;

    @BeforeClass
    public static void doSetupBeforeClass() {
	initgvSIGDrivers();
	try {
	    DBSession.createConnection("localhost",
		    5432, "audasa_test", null,
		    "postgres", "postgres");
	} catch (DBException e) {
	    e.printStackTrace();
	}
    }

    private static void initgvSIGDrivers() {

	final String fwAndamiDriverPath = TestProperties.driversPath;

	final File baseDriversPath = new File(fwAndamiDriverPath);
	if (!baseDriversPath.exists()) {
	    throw new RuntimeException("Can't find drivers path: "
		    + fwAndamiDriverPath);
	}

	LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
	if (LayerFactory.getDM().getDriverNames().length < 1) {
	    throw new RuntimeException("Can't find drivers in path: "
		    + fwAndamiDriverPath);
	}
    }

    @Before
    public void doSetup() {
	//ORMLite ormLite = new ORMLite(getMetadataFile());
	//ado = ormLite.getAppDomain();
	try {
	    InputStream file = new FileInputStream(getUIFile());
	    formPanel = new FormPanel(file);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	widgets = AbeilleParser.getWidgetsFromContainer(formPanel);
    }

    @Test
    public void testXMLIsValid() throws SAXException {
	boolean thrown = false;
	File file = new File(getMetadataFile());
	assertTrue("File not exists: " + getMetadataFile(), file.exists());
	try {
	    new XMLSAXParser(getMetadataFile());
	} catch (ParserConfigurationException e) {
	    thrown = true;
	} catch (SAXException e) {
	    thrown = true;
	} catch (IOException e) {
	    thrown = true;
	}
	assertFalse(thrown);
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
		"audasa_pm", "exp_pm");
	final Set<String> columnsSet = new HashSet<String>(
		Arrays.asList(columns));
	return columnsSet;
    }

    protected String getUIFile() {
	return "forms/pm.xml";
    }

    protected String getMetadataFile() {
	return "data/extpm.xml";
    }
}
