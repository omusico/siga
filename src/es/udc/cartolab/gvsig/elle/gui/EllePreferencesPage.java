/*
 * Copyright (c) 2010. Cartolab (Universidade da Coru�a)
 * 
 * This file is part of extELLE
 * 
 * extELLE is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * extELLE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with extELLE.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package es.udc.cartolab.gvsig.elle.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class EllePreferencesPage extends AbstractPreferencePage implements ActionListener {

	/* key names */
	public static final String DEFAULT_LEGEND_DIR_KEY_NAME = "LegendDir";
	public static final String DEFAULT_LEGEND_FILE_TYPE_KEY_NAME = "LegendFileType";

	/* default values */
	private static final String DEFAULT_LEGEND_DIR = Launcher.getAppHomeDir();
	public static final String DEFAULT_LEGEND_FILE_TYPE = "gvl";

	protected String id;
	private ImageIcon icon;
	private boolean panelStarted = false;
	private JTextField legendDirField;
	private JButton legendDirButton;
	private JComboBox legendTypeCBox;
	private String title = "ELLE";

	public EllePreferencesPage() {
		super();
		id = this.getClass().getName();
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("images/logo.png"));
		panelStarted = false;
	}

	@Override
	public void setChangesApplied() {
		setChanged(false);
	}

	@Override
	public void storeValues() throws StoreException {
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();

		storeDir(xml);
		storeType(xml);

	}

	private void storeDir(XMLEntity xml) throws StoreException {
		String legendDir = legendDirField.getText();
		File f = new File(legendDir);
		if (f.exists() && f.isDirectory() && f.canRead()) {
			xml.putProperty(DEFAULT_LEGEND_DIR_KEY_NAME,
					legendDir);
		} else {
			String message = String.format("%s no es un directorio v�lido", legendDir);
			throw new StoreException(message);
		}
	}

	private void storeType(XMLEntity xml) {
		String type = legendTypeCBox.getSelectedItem().toString();
		xml.putProperty(DEFAULT_LEGEND_FILE_TYPE_KEY_NAME, type);
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public ImageIcon getIcon() {
		return icon;
	}

	@Override
	public JPanel getPanel() {

		if (!panelStarted ) {
			panelStarted = true;

			FormPanel form = new FormPanel("forms/preferences.jfrm");
			form.setFocusTraversalPolicyProvider(true);

			JLabel legendLabel = form.getLabel("legendLabel");
			legendLabel.setText(PluginServices.getText(this, "legend_directory"));

			legendDirField = form.getTextField("legendField");
			legendDirButton = (JButton) form.getButton("legendButton");
			legendDirButton.addActionListener(this);

			JLabel legendTypeLabel = form.getLabel("legendTypeLabel");
			legendTypeLabel.setText(PluginServices.getText(this, "legend_type_pref"));

			legendTypeCBox = form.getComboBox("legendTypeCB");
			legendTypeCBox.addItem("gvl");
			legendTypeCBox.addItem("sld");

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

		legendDirField.setText(DEFAULT_LEGEND_DIR);

		setCB(DEFAULT_LEGEND_FILE_TYPE);

	}

	private void setCB(String text) {
		int itemCount = legendTypeCBox.getItemCount();
		for (int i=0; i<itemCount; i++) {
			if (text.equals(legendTypeCBox.getItemAt(i))) {
				legendTypeCBox.setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public void initializeValues() {

		if (!panelStarted) {
			getPanel();
		}

		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();

		// Default Projection
		String legendDir = null;
		if (xml.contains(DEFAULT_LEGEND_DIR_KEY_NAME)) {
			legendDir = xml.getStringProperty(DEFAULT_LEGEND_DIR_KEY_NAME);
		} else {
			legendDir = DEFAULT_LEGEND_DIR;
		}

		legendDirField.setText(legendDir);

		String type = DEFAULT_LEGEND_FILE_TYPE;
		if (xml.contains(DEFAULT_LEGEND_FILE_TYPE_KEY_NAME)) {
			type = xml.getStringProperty(DEFAULT_LEGEND_FILE_TYPE_KEY_NAME);
		}

		setCB(type.toLowerCase());

	}

	@Override
	public boolean isValueChanged() {
		return super.hasChanged();
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource()==legendDirButton) {
			File currentDirectory = new File(legendDirField.getText());
			JFileChooser chooser;
			if (!(currentDirectory.exists() &&
					currentDirectory.isDirectory() &&
					currentDirectory.canRead())) {
				currentDirectory = new File(DEFAULT_LEGEND_DIR);
			}
			chooser = new JFileChooser(currentDirectory);

			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(legendDirField);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				legendDirField.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

}
