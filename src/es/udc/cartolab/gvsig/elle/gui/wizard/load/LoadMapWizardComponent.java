package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.utils.LoadMap;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LoadMapWizardComponent extends WizardComponent implements ActionListener {

	protected CRSSelectPanel crsPanel = null;
	protected JList mapList;
	private DBSession dbs;
	private JPanel listPanel;
	private JTextArea layerTextArea;
	private String[][] layers;

	public final static String PROPERTY_VEW = "view";

	public LoadMapWizardComponent(Map<String, Object> properties) {
		super(properties);

		dbs = DBSession.getCurrentSession();

		setLayout(new BorderLayout());

		add(getListPanel(), BorderLayout.CENTER);
		add(getCRSPanel(), BorderLayout.SOUTH);
	}

	@Override
	public boolean canFinish() {
		return canNext();
	}

	@Override
	public boolean canNext() {
		if (mapList!=null) {
			return mapList.getSelectedIndices().length == 1;
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		callStateChanged();
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

					JLabel mapLabel = form.getLabel("mapLabel");
					JLabel layerLabel = form.getLabel("layerLabel");

					mapLabel.setText(PluginServices.getText(this, "map_load"));
					layerLabel.setText(PluginServices.getText(this, "layer_load"));

					mapList.addListSelectionListener(new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent arg0) {
							// TODO Auto-generated method stub
							int[] selected = mapList.getSelectedIndices();
							callStateChanged();

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

	@Override
	public String getWizardComponentName() {
		return "load_map_wizard_component";
	}

	public String getMapName() {
		return mapList.getSelectedValue().toString();
	}

	@Override
	public void showComponent() {
	}

	@Override
	public void finish() throws WizardException {
		Object aux = properties.get(PROPERTY_VEW);
		if (aux!=null && aux instanceof View) {
			View view = (View) aux;
			try {
				LoadMap.loadMap(view, mapList.getSelectedValue().toString(), crsPanel.getCurProj());
			} catch (Exception e) {
				throw new WizardException(e);
			}
		} else {
			throw new WizardException("Couldn't retrieve the view");
		}
	}

	@Override
	public void setProperties() {
		// Nothing to do
	}
}
