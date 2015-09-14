package es.icarto.gvsig.siga;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.MDIManager;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.project.documents.table.gui.Table;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.siga.OpenStreetViewExtension;

@RunWith(PowerMockRunner.class)
// http://stackoverflow.com/questions/11943703/trying-to-mock-static-system-class-with-powermock-gives-verifyerror
@PowerMockIgnore("javax.swing.*")
@PrepareForTest({ PluginServices.class, MDIManager.class })
public class TestOpenStreetViewExtension {

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
