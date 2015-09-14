package es.icarto.gvsig.extgex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.iver.andami.plugins.config.generate.Extension;
import com.iver.andami.plugins.config.generate.Extensions;
import com.iver.andami.plugins.config.generate.PluginConfig;

import es.icarto.gvsig.siga.ExtensionTestHelper;

public class TestFormExpropiationExtension {

    @Test
    /**
     * AddFincaAction uses the toolbar name set in config.xml to add a button to it. This test ensures that the name of the toolbar is not changed
     */
    public void testToolbarName() throws FileNotFoundException {
	PluginConfig config = ExtensionTestHelper
		.getConfig("config/config.xml");
	assertTrue(config.isValid());

	Extensions extensions = config.getExtensions();
	for (int i = 0; i < extensions.getExtensionCount(); i++) {
	    Extension extension = extensions.getExtension(i);
	    if (extension.getClassName().equals(
		    FormExpropiationsExtension.class.getName())) {
		assertEquals(FormExpropiationsExtension.TOOLBAR_NAME, extension
			.getToolBar(0).getName());
		return;
	    }
	}
	fail("No existe una toolbar con ese nombre");
    }

}
