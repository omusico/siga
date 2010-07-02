package es.udc.cartolab.gvsig.elle.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.XMLEntity;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.elle.utils.LoadLegend;
import es.udc.cartolab.gvsig.elle.utils.LoadMap;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LoadMapWindow extends JPanel implements IWindow, ActionListener {

	private WindowInfo viewInfo;
	private DBSession dbs;
	protected CRSSelectPanel crsPanel = null;
	private JPanel listPanel, southPanel;
	protected JList mapList;
	private JTextArea layerTextArea;
	private String[][] layers;
	private JButton okButton, cancelButton;
	protected final View view;
	private JComboBox legendCB;
	private String legendDir;

	public WindowInfo getWindowInfo() {

		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
			viewInfo.setTitle(PluginServices.getText(this, "Load_map"));
			viewInfo.setWidth(525);
			viewInfo.setHeight(520);
		}
		return viewInfo;
	}

	public LoadMapWindow() {

		dbs = DBSession.getCurrentSession();
		view = (View) PluginServices.getMDIManager().getActiveWindow();

		XMLEntity xml = PluginServices.getPluginServices("es.udc.cartolab.gvsig.elle").getPersistentXML();
		if (xml.contains(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME)) {
			legendDir = xml.getStringProperty(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME);
		}

		JPanel mainPanel = new JPanel();

		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		mainPanel.setLayout(new BorderLayout());

		mainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, PluginServices.getText(this, "Choose_Map"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));


		mainPanel.add(getListPanel(), BorderLayout.CENTER);
		mainPanel.add(getCRSPanel(), BorderLayout.SOUTH);

		add(mainPanel, new GridBagConstraints(0, 1, 1, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(getSouthPanel(), new GridBagConstraints(0, 2, 1, 1, 10, 0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

	}

	private JPanel getCRSPanel() {
		if (crsPanel == null) {
			crsPanel = CRSSelectPanel.getPanel(AddLayerDialog.getLastProjection());
			crsPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (crsPanel.isOkPressed()) {
						AddLayerDialog.setLastProjection(crsPanel.getCurProj());
					}
				}
			});
		}
		return crsPanel;
	}

	protected JPanel getSouthPanel() {

		if (southPanel == null) {
			southPanel = new JPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.RIGHT);
			southPanel.setLayout(layout);
			okButton = new JButton(PluginServices.getText(this, "ok"));
			okButton.setEnabled(false);
			cancelButton = new JButton(PluginServices.getText(this, "cancel"));
			okButton.addActionListener(this);
			cancelButton.addActionListener(this);
			southPanel.add(okButton);
			southPanel.add(cancelButton);
		}
		return southPanel;
	}

	private JPanel getListPanel() {
		if (listPanel == null) {

			listPanel = new JPanel();

			try {

				FormPanel form = new FormPanel("forms/loadMap.jfrm");
				form.setFocusTraversalPolicyProvider(true);

				listPanel.add(form);

				dbs = DBSession.getCurrentSession();

				if (dbs.tableExists(dbs.getSchema(), "_map") && dbs.tableExists(dbs.getSchema(), "_map_overview")) {

					String[] groups = dbs.getDistinctValues("_map", "mapa");

					//layerList = form.getList("layerList");
					mapList = form.getList("mapList");
					mapList.setListData(groups);

					layerTextArea = (JTextArea) form.getComponentByName("layerTextArea");
					layerTextArea.setEditable(false);

					legendCB = form.getComboBox("legendCombo");

					JLabel mapLabel = form.getLabel("mapLabel");
					JLabel layerLabel = form.getLabel("layerLabel");
					JLabel legendLabel = form.getLabel("legendLabel");

					mapLabel.setText(PluginServices.getText(this, "map_load"));
					layerLabel.setText(PluginServices.getText(this, "layer_load"));
					legendLabel.setText(PluginServices.getText(this, "legend"));

					if (legendDir != null) {
						File f = new File(legendDir);
						File[] files = f.listFiles();
						for (int i=0; i<files.length; i++) {
							if (files[i].isDirectory() && !files[i].isHidden()) {
								legendCB.addItem(files[i].getName());
							}
						}
					}

					mapList.addListSelectionListener(new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent arg0) {
							// TODO Auto-generated method stub
							int[] selected = mapList.getSelectedIndices();
							changeOkButtonState();
							if (selected.length == 1) {
								String selectedValue = (String) mapList.getSelectedValues()[0];
								String where = String.format("WHERE mapa = '%s'", selectedValue);
								try {
									layers = dbs.getTable("_map", dbs.getSchema(), where, new String[]{"posicion"}, true);
									String layerText = "";
									for (int i=0; i<layers.length; i++) {
										layerText = layerText + layers[i][1] + "\n";
									}

									layerTextArea.setText(layerText);

								} catch (SQLException e) {
									// TODO Auto-generated catch block
									JOptionPane.showMessageDialog(null,
											"Error SQL: " + e.getMessage(),
											"SQL Exception",
											JOptionPane.ERROR_MESSAGE);
								}

							} else {
								layerTextArea.setText("");
							}

						}

					});
				} else {
					listPanel = new JPanel();
					JLabel label = new JLabel(PluginServices.getText(this, "no_map_table_on_schema"));
					listPanel.add(label);
				}


			} catch (SQLException e) {
				//exception
				e.printStackTrace();
			}
		}

		return listPanel;
	}

	private void changeOkButtonState() {
		if (mapList.getSelectedIndices().length != 1) {
			okButton.setEnabled(false);
		} else {
			okButton.setEnabled(true);
		}
	}

	protected void callLoadMap() throws Exception {
		LoadMap.loadMap(view, mapList.getSelectedValue().toString(), crsPanel.getCurProj());
	}

	protected void loadMap() {
		PluginServices.getMDIManager().setWaitCursor();
		dbs = DBSession.getCurrentSession();
		try {
			callLoadMap();

			/* styles */
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
		} catch (Exception e) {
			String message = PluginServices.getText(this, "error_loading_layers");
			PluginServices.getMDIManager().restoreCursor();
			JOptionPane.showMessageDialog(this,
					String.format(message, e.getMessage()),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		PluginServices.getMDIManager().restoreCursor();
	}


	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() == cancelButton) {
			PluginServices.getMDIManager().closeWindow(this);
		}
		if (event.getSource() == okButton) {
			loadMap();
			PluginServices.getMDIManager().closeWindow(this);
		}
	}

	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return null;
	}

}
