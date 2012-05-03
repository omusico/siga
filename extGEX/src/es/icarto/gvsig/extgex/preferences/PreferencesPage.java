package es.icarto.gvsig.extgex.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.utiles.XMLEntity;
import com.jeta.forms.components.panel.FormPanel;

public class PreferencesPage extends AbstractPreferencePage implements
	ActionListener {

    // ormlite
    public static final String XML_ORMLITE_RELATIVE_PATH = "data/extgex.xml";

    // fileslink
    public static final String DEFAULT_FILES_DIR_KEY_NAME = "FilesDir";

    public static final String DEFAULT_FILES_DIR = Launcher.getAppHomeDir();

    protected String id;
    private boolean panelStarted = false;
    private JTextField filesDirField;
    private JButton filesDirButton;
    private String title = "Audasa";


    static String baseDirectory = "";

    public PreferencesPage() {
	super();
	id = this.getClass().getName();
	panelStarted = false;
    }

    @Override
    public void setChangesApplied() {
	setChanged(false);
    }

    @Override
    public void storeValues() throws StoreException {
	String legendDir = filesDirField.getText();
	setBaseDirectory(legendDir);
    }

    public String getID() {
	return id;
    }

    public ImageIcon getIcon() {
	// TODO Auto-generated method stub
	return null;
    }

    public JPanel getPanel() {
	if (!panelStarted) {
	    panelStarted = true;

	    FormPanel form = new FormPanel("preferences.jfrm");
	    form.setFocusTraversalPolicyProvider(true);

	    JLabel legendLabel = form.getLabel("filesLabel");
	    legendLabel
		    .setText(PluginServices.getText(this, "files_directory"));

	    filesDirField = form.getTextField("filesField");
	    filesDirButton = (JButton) form.getButton("filesButton");
	    filesDirButton.addActionListener(this);

	    addComponent(form);

	}

	return this;
    }

    public String getTitle() {
	return title;
    }

    public void initializeDefaults() {
	filesDirField.setText(DEFAULT_FILES_DIR);

    }

    public void initializeValues() {
	if (!panelStarted) {
	    getPanel();
	}

	PluginServices ps = PluginServices.getPluginServices(this);
	XMLEntity xml = ps.getPersistentXML();

	// Default Projection
	String filesDir = null;
	if (xml.contains(PreferencesPage.DEFAULT_FILES_DIR_KEY_NAME)) {
	    filesDir = xml
		    .getStringProperty(PreferencesPage.DEFAULT_FILES_DIR_KEY_NAME);
	} else {
	    filesDir = DEFAULT_FILES_DIR;
	}

	filesDirField.setText(filesDir);

    }

    public boolean isValueChanged() {
	return super.hasChanged();
    }

    public void actionPerformed(ActionEvent event) {
	if (event.getSource() == filesDirButton) {
	    File currentDirectory = new File(filesDirField.getText());
	    JFileChooser chooser;
	    if (!(currentDirectory.exists() && currentDirectory.isDirectory() && currentDirectory
		    .canRead())) {
		currentDirectory = new File(DEFAULT_FILES_DIR);
	    }
	    chooser = new JFileChooser(currentDirectory);

	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int returnVal = chooser.showOpenDialog(filesDirField);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		filesDirField.setText(chooser.getSelectedFile()
			.getAbsolutePath());
	    }
	}

    }

    public boolean isConfigured() {
	return (getBaseDirectory() != null);
    }

    public static String getBaseDirectory() {
	XMLEntity xml = PluginServices.getPluginServices(
		"es.icarto.gvsig.extgex").getPersistentXML();
	if (xml.contains(DEFAULT_FILES_DIR_KEY_NAME)) {
	    baseDirectory = xml.getStringProperty(DEFAULT_FILES_DIR_KEY_NAME);
	}
	return baseDirectory;
    }


    public void setBaseDirectory(String baseDirectory) throws StoreException {
	PluginServices ps = PluginServices.getPluginServices(this);
	XMLEntity xml = ps.getPersistentXML();
	File f = new File(baseDirectory);
	if (f.exists() && f.isDirectory() && f.canRead()) {
	    xml.putProperty(PreferencesPage.DEFAULT_FILES_DIR_KEY_NAME,
		    baseDirectory);
	} else {
	    String message = String.format("%s no es un directorio válido",
		    baseDirectory);
	    throw new StoreException(message);
	}
    }

}