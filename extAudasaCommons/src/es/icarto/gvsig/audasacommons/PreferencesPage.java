package es.icarto.gvsig.audasacommons;

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

@SuppressWarnings("serial")
public class PreferencesPage extends AbstractPreferencePage implements
	ActionListener {

    private static final String PLUGIN_NAME = "es.icarto.gvsig.audasacommons";

    // fileslink
    private static final String DEFAULT_FILES_DIR_KEY_NAME = "FilesDir";

    private static final String DEFAULT_FILES_DIR = Launcher.getAppHomeDir();

    private String id;
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

    @Override
    public String getID() {
	return id;
    }

    @Override
    public ImageIcon getIcon() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JPanel getPanel() {
	if (!panelStarted) {
	    panelStarted = true;

	    FormPanel form = new FormPanel("forms/preferences.xml");
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

    @Override
    public String getTitle() {
	return title;
    }

    @Override
    public void initializeDefaults() {
	filesDirField.setText(DEFAULT_FILES_DIR);

    }

    @Override
    public void initializeValues() {
	if (!panelStarted) {
	    getPanel();
	}

	PluginServices ps = PluginServices.getPluginServices(this);
	XMLEntity xml = ps.getPersistentXML();

	// Default Projection
	String filesDir = null;
	if (xml.contains(DEFAULT_FILES_DIR_KEY_NAME)) {
	    filesDir = xml.getStringProperty(DEFAULT_FILES_DIR_KEY_NAME);
	} else {
	    filesDir = DEFAULT_FILES_DIR;
	}

	filesDirField.setText(filesDir);

    }

    @Override
    public boolean isValueChanged() {
	return super.hasChanged();
    }

    @Override
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
	XMLEntity xml = PluginServices.getPluginServices(PLUGIN_NAME)
		.getPersistentXML();
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
	    xml.putProperty(DEFAULT_FILES_DIR_KEY_NAME, baseDirectory);
	} else {
	    String message = String.format("%s no es un directorio v�lido",
		    baseDirectory);
	    throw new StoreException(message);
	}
    }

}