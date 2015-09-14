package es.icarto.gvsig.siga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.iver.andami.plugins.config.generate.PluginConfig;
import com.iver.utiles.xml.XMLEncodingUtils;

public class ExtensionTestHelper {

    private ExtensionTestHelper() {
	throw new AssertionError("only static methods");
    }

    public static PluginConfig getConfig(String path)
	    throws FileNotFoundException {
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
	try {
	    return (PluginConfig) PluginConfig.unmarshal(xml);
	} catch (MarshalException e) {
	    throw new RuntimeException("Invalid xml");
	} catch (ValidationException e) {
	    throw new RuntimeException("Invalid xml");
	}
    }

}
