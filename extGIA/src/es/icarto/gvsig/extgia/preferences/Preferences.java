package es.icarto.gvsig.extgia.preferences;

import java.io.File;

public class Preferences {

    private static final String EXTENSION_PATH = "gvSIG" + File.separator
	    + "extensiones" + File.separator + "es.icarto.gvsig.extgia"
	    + File.separator;

    // private static final String EXTENSION_PATH = PluginServices
    // .getPluginServices(Preferences.class).getPluginDirectory()
    // .getAbsolutePath();

    public static final String XMLDATAFILE_PATH = EXTENSION_PATH + "audasa.xml";

}
