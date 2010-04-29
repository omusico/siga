package es.udc.cartolab.gvsig.elle.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.XMLEntity;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.elle.utils.LoadLegend;
import es.udc.cartolab.gvsig.users.preferences.EielPage;

public class LoadAllLegendsDialog extends JPanel implements IWindow, ActionListener {

	private JPanel centerPanel = null;
	private JPanel southPanel = null;
	private JComboBox legendCB;
	private JButton okButton;
	private JButton cancelButton;
	private WindowInfo viewInfo = null;
	private final View view;
	private String legendDir;

	public WindowInfo getWindowInfo() {

		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
			viewInfo.setTitle(PluginServices.getText(this, "load_legends"));
			viewInfo.setWidth(425);
			viewInfo.setHeight(75);
		}
		return viewInfo;
	}

	public LoadAllLegendsDialog(View view) throws Exception {
		this.view = view;
		XMLEntity xml = PluginServices.getPluginServices("es.udc.cartolab.gvsig.users").getPersistentXML();
		if (xml.contains(EielPage.DEFAULT_LEGEND_DIR_KEY_NAME)) {
			legendDir = xml.getStringProperty(EielPage.DEFAULT_LEGEND_DIR_KEY_NAME);
		}
		init();
	}

	private void init() throws Exception {

		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		add(getCenterPanel(), new GridBagConstraints(0, 0, 1, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(getSouthPanel(), new GridBagConstraints(0, 1, 1, 1, 10, 0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		//enables tabbing navigation
		setFocusCycleRoot(true);
	}

	protected JPanel getCenterPanel() throws Exception {

		if (centerPanel == null) {
			centerPanel = new JPanel();
			FormPanel form = new FormPanel("forms/loadLegends.jfrm");
			form.setFocusTraversalPolicyProvider(true);
			centerPanel.add(form);

			JLabel legendsLabel = form.getLabel("legendsLabel");
			legendsLabel.setText(PluginServices.getText(this, "legends_group_name"));

			legendCB = form.getComboBox("legendCB");
			if (legendDir != null) {
				File f = new File(legendDir);
				File[] files = f.listFiles();
				if (files.length > 0) {
					for (int i=0; i<files.length; i++) {
						if (files[i].isDirectory() && !files[i].isHidden()) {
							legendCB.addItem(files[i].getName());
						}
					}
				} else {
					throw new Exception(PluginServices.getText(this, "theres_no_legends"));
				}
			}

		}
		return centerPanel;
	}

	protected JPanel getSouthPanel() {

		if (southPanel == null) {
			southPanel = new JPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.RIGHT);
			southPanel.setLayout(layout);
			okButton = new JButton(PluginServices.getText(this, "ok"));
			cancelButton = new JButton(PluginServices.getText(this, "cancel"));
			okButton.addActionListener(this);
			cancelButton.addActionListener(this);
			southPanel.add(okButton);
			southPanel.add(cancelButton);
		}
		return southPanel;
	}

	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() == okButton) {
			String selectedItem = "";
			if (legendCB.getSelectedItem()!=null) {
				selectedItem = legendCB.getSelectedItem().toString();
			}
			if ((legendDir!=null) && !selectedItem.equals("")) {
				String stylePath;
				if (legendDir.endsWith(File.separator)) {
					stylePath = legendDir + selectedItem;
				} else {
					stylePath = legendDir + File.separator + selectedItem;
				}
				LoadLegend.setLegendPath(stylePath);
				LoadLegend.loadAllStyles(view);
			}
			PluginServices.getMDIManager().closeWindow(this);
		}
		if (event.getSource() == cancelButton) {
			PluginServices.getMDIManager().closeWindow(this);
		}
	}

	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return null;
	}

}
