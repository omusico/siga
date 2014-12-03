package es.icarto.gvsig.audasacommons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.iver.andami.plugins.IExtension;
import com.iver.andami.plugins.config.generate.ActionTool;
import com.iver.andami.plugins.config.generate.Extension;
import com.iver.andami.plugins.config.generate.Extensions;
import com.iver.andami.plugins.config.generate.PluginConfig;
import com.iver.andami.plugins.config.generate.ToolBar;

import es.udc.cartolab.gvsig.elle.SigaLoadMapExtension;

public class TestExtensions {

    private static List<Class<? extends IExtension>> projectExtensions;
    private static PluginConfig config;

    static {
	projectExtensions = new ArrayList<Class<? extends IExtension>>();
	projectExtensions.add(OpenStreetViewExtension.class);
	projectExtensions.add(SIGAConfigExtension.class);
	projectExtensions.add(SigaLoadMapExtension.class);
    }

    class ICartoExtension {
	private final Extension expected;

	public ICartoExtension(Class<? extends IExtension> c) {
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
	config = ExtensionTestHelper.getConfig("config/config.xml");
    }

    @Test
    public void testConfigFile() throws FileNotFoundException {
	assertTrue(config.isValid());

	Extensions extensions = config.getExtensions();

	assertEquals(0, extensions.getSkinExtensionCount());
	assertEquals(projectExtensions.size(), extensions.getExtensionCount());

	Extension[] exts = extensions.getExtension();

	for (Class<? extends IExtension> c : projectExtensions) {
	    ICartoExtension icartoExt = new ICartoExtension(c);
	    icartoExt.check(exts);
	}
    }

    @Test
    public void test() {
	fail("Not yet implemented");
    }

}
