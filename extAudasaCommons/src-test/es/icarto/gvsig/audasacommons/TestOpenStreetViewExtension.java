package es.icarto.gvsig.audasacommons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import javax.swing.ImageIcon;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.iver.andami.PluginServices;
import com.iver.andami.iconthemes.IIconTheme;
import com.iver.andami.iconthemes.IconThemeDir;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.plugins.config.generate.ActionTool;
import com.iver.andami.plugins.config.generate.Extension;
import com.iver.andami.plugins.config.generate.Extensions;
import com.iver.andami.plugins.config.generate.PluginConfig;
import com.iver.andami.plugins.config.generate.ToolBar;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.MDIManager;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.project.documents.table.gui.Table;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.xml.XMLEncodingUtils;

@RunWith(PowerMockRunner.class)
// http://stackoverflow.com/questions/11943703/trying-to-mock-static-system-class-with-powermock-gives-verifyerror
@PowerMockIgnore("javax.swing.*")
@PrepareForTest({ PluginServices.class, MDIManager.class })
public class TestOpenStreetViewExtension {

    class MyExtension {
	private final Extension expected;

	public MyExtension(Class<? extends IExtension> c) {
	    expected = new Extension();

	    // iCarto extensions should have the suffix "Extension"
	    expected.setClassName(c.getName());

	    // active="true" is mandatory for extensions
	    expected.setActive(true);

	    // iCarto extensions should not set a description
	    expected.setDescription(null);
	}

	private void iCartoRules(Extension actual) {
	    assertTrue(actual.isValid());
	    assertEquals(expected.hasActive(), actual.hasActive());
	    assertEquals(expected.getActive(), actual.getActive());
	    assertEquals(expected.getDescription(), actual.getDescription());
	    assertEquals(expected.getClassName(), actual.getClassName());
	    assertTrue(actual.getClassName().endsWith("Extension"));

	    if (actual.getToolBarCount() > 0) {
		assertEquals(1, actual.getToolBarCount());
		ToolBar toolBar = actual.getToolBar(0);
		// name is a mandatory field in toolbars
		assertEquals("SIGA_Tools", toolBar.getName()); // TODO
		assertTrue(toolBar.hasPosition()); // TODO
		assertEquals(75, toolBar.getPosition()); // TODO
		assertEquals(1, toolBar.getActionToolCount()); // TODO
		ActionTool actionTool = toolBar.getActionTool(0);
		// icon is a mandatory field in action-tool
		assertEquals("open-street-view", actionTool.getIcon()); // TODO
		assertTrue(actionTool.hasPosition()); // TODO
		assertEquals(6, actionTool.getPosition()); // TODO
	    }
	}

	public void check(Extension[] exts) {
	    Extension actual = null;
	    for (Extension e : exts) {
		if (e.getClassName().equals(expected.getClassName())) {
		    actual = e;
		    break;
		}
	    }
	    assertNotNull("extension not found", actual);
	    iCartoRules(actual);
	}

    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConfigFile() throws MarshalException,
	    FileNotFoundException, ValidationException {
	PluginConfig config = getConfig("config/config.xml");
	assertTrue(config.isValid());

	Extensions extensions = config.getExtensions();

	assertEquals(0, extensions.getSkinExtensionCount());
	assertEquals(3, extensions.getExtensionCount());

	Extension[] exts = extensions.getExtension();

	MyExtension myExtension = new MyExtension(OpenStreetViewExtension.class);
	myExtension.check(exts);

    }

    @Test
    public void alwaysVisible() {
	OpenStreetViewExtension ext = new OpenStreetViewExtension();
	assertTrue(ext.isVisible());
    }

    @Test
    public void viewEnabled() {
	OpenStreetViewExtension ext = new OpenStreetViewExtension();

	MDIManager mock = PowerMockito.mock(MDIManager.class);

	PowerMockito.mockStatic(PluginServices.class);
	PowerMockito.when(PluginServices.getMDIManager()).thenReturn(mock);

	IWindow view = new View();
	Mockito.when(mock.getActiveWindow()).thenReturn(view);
	assertTrue(ext.isEnabled());

	Mockito.when(mock.getActiveWindow()).thenReturn(null);
	assertFalse(ext.isEnabled());

	IWindow table = new Table();
	Mockito.when(mock.getActiveWindow()).thenReturn(table);
	assertFalse(ext.isEnabled());
    }

    @Test
    public void iconRegistered() {
	IIconTheme iconTheme = new IconThemeDir(PluginServices
		.getIconThemeManager().getDefault());
	PluginServices.getIconThemeManager().setCurrent(iconTheme);
	OpenStreetViewExtension ext = new OpenStreetViewExtension();
	ext.initialize();
	ImageIcon icon = PluginServices.getIconTheme().get("open-street-view");
	assertNotNull(icon);
    }

    private PluginConfig getConfig(String path) throws FileNotFoundException,
	    MarshalException, ValidationException {
	File xmlFile = new File(path);

	FileInputStream is = new FileInputStream(xmlFile);
	Reader xml = XMLEncodingUtils.getReader(is);
	if (xml == null) {
	    // the encoding was not correctly dPoint2D p = event.getPoint();
	    xml = new FileReader(xmlFile);
	} else {
	    // use a buffered reader to improve performance
	    xml = new BufferedReader(xml);
	}
	return (PluginConfig) PluginConfig.unmarshal(xml);
    }

    @Test
    public void test() {
	OpenStreetViewExtension ext = new OpenStreetViewExtension();

	ext.execute(null);

	// http://stackoverflow.com/a/542965/930271
	String baseUri = "http://maps.google.com/maps?q=&layer=c&cbll=%s,%s&layer=c";
	String lat = "42.2353679";
	String lng = "-8.7147829";
	String uri = String.format(baseUri, lat, lng);

	// Desktop.getDesktop().browse(new URI(uri));

	// select st_astext(st_transform(the_geom, 4326)) from
	// audasa_extgia.senhalizacion_vertical where gid=1719;
	// http://maps.google.com/maps?q=&layer=c&cbll=43.3329570226972,-8.39921571634638&layer=c

    }

    @Test
    public void foo() {

	IProjection crs4326 = CRSFactory.getCRS("EPSG:4326");
	IProjection crs23029 = CRSFactory.getCRS("EPSG:23029");

	ICoordTrans t4326To23029 = crs4326.getCT(crs23029);

	// t4326To23029.getInverted()
	ICoordTrans t23029To4326 = crs23029.getCT(crs4326);

	// ICoordTrans t4326To23029To4326 = crs4326.getCT(crs23029);

	Double lng = 548807.869562099;
	Double lat = 4797947.96388597;
	Point2D p = new Point2D.Double(lng, lat);

	Point2D pTo4326 = t23029To4326.convert(p, null);
	// [-8.40088124879342, 43.32963146548493]
	System.out.println(pTo4326);

	// En lat 43º 1 min = 1851.55m
    }

    // if (ct!=null) ig.reProject(ct);
}
