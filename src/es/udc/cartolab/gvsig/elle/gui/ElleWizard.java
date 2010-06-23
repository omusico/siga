package es.udc.cartolab.gvsig.elle.gui;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cresques.cts.IProjection;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ElleWizard extends WizardPanel {

	private JPanel listPanel = null;
	private JList layerList = null;
	private JList groupList = null;
	private CRSSelectPanel crsPanel = null;
	private DBSession dbs;
	private String[][] layers;
	private JComboBox scopeCB;

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public FLayer getLayer() {
		// TODO Auto-generated method stub
		DBSession dbs = DBSession.getCurrentSession();
		FLayer layer = null;
		if (dbs != null) {
			PluginServices.getMDIManager().setWaitCursor();
			//load layer
			IProjection proj = crsPanel.getCurProj();
			int pos = layerList.getSelectedIndex();

			String layerName = layers[pos][1];
			String tableName = layers[pos][2];

			String schema = null;
			if (layers[pos].length > 8) {
				if (layers[pos][8].length()>1) {
					schema = layers[pos][8];
				}
			}

			String whereClause = "";
			//			Constants constants = Constants.getCurrentConstants();
			//			if (constants!=null) {
			//				switch (scopeCB.getSelectedIndex()) {
			//				case 1 : //adjacents
			//					whereClause = "WHERE ";
			//					List<String> municipios = constants.getMunicipios();
			//					for (int j=0; j<municipios.size()-1; j++) {
			//						whereClause = whereClause.concat("mun ='" + municipios.get(j) +
			//						"' OR ");
			//					}
			//					whereClause = whereClause.concat("mun ='" + municipios.get(municipios.size()-1) + "'");
			//					break;
			//				case 2 : //selected council
			//					whereClause = "WHERE mun='" + constants.getMunCod() + "'";
			//					break;
			//				default:
			//					break;
			//				}
			//			}

			try {
				if (schema!=null) {
					layer = dbs.getLayer(layerName, tableName, schema, whereClause, proj);
				} else {
					layer = dbs.getLayer(layerName, tableName, whereClause, proj);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this,
						"SQLException: " + e.getMessage(),
						"SQL Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this,
						"SQLException: " + e.getMessage(),
						"DB Error",
						JOptionPane.ERROR_MESSAGE);
			}
			PluginServices.getMDIManager().restoreCursor();
		} else {
			//Show no connection error
			JOptionPane.showMessageDialog(this,
					PluginServices.getText(this, "notConnectedError"),
					PluginServices.getText(this, "notConnected"),
					JOptionPane.ERROR_MESSAGE);
		}

		return layer;
	}

	@Override
	public void initWizard() {

		dbs = DBSession.getCurrentSession();
		setTabName("EIEL");

		setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		if (dbs != null) {
			// TODO Auto-generated method stub

			mainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "Choose_Layer"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));


			mainPanel.add(getListPanel(), BorderLayout.CENTER);
			mainPanel.add(getCRSPanel(), BorderLayout.SOUTH);

		} else {
			JLabel label = new JLabel(PluginServices.getText(this, "notConnectedError"));
			mainPanel.add(label, BorderLayout.NORTH);
		}
		add(mainPanel, BorderLayout.CENTER);

	}

	private JPanel getListPanel() {
		if (listPanel == null) {

			listPanel = new JPanel();

			try {

				FormPanel form = new FormPanel("forms/loadEIELLayer.jfrm");
				form.setFocusTraversalPolicyProvider(true);

				listPanel.add(form);

				dbs = DBSession.getCurrentSession();

				String[] groups = dbs.getDistinctValues("_map", "mapa");

				layerList = form.getList("layerList");
				groupList = form.getList("groupList");
				groupList.setListData(groups);

				JLabel scopeLabel = form.getLabel("scopeLabel");
				scopeLabel.setText(PluginServices.getText(this, "scope"));

				scopeCB = form.getComboBox("scopeCB");
				String op1 = PluginServices.getText(this, "all_prov");
				scopeCB.addItem(op1);
				//				Constants constants = Constants.getCurrentConstants();
				//				if (constants!= null) {
				//					String op2 = PluginServices.getText(this, "adjacent_councils");
				//					String op3 = PluginServices.getText(this, "selected_council");
				//					scopeCB.addItem(op2);
				//					scopeCB.addItem(op3);
				//				}

				groupList.addListSelectionListener(new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent arg0) {
						// TODO Auto-generated method stub
						int[] selected = groupList.getSelectedIndices();
						if (selected.length == 1) {
							String where = String.format("where mapa ='%s'", groupList.getSelectedValues()[0]);
							try {
								layers = dbs.getTable("_map", where);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null,
										"Error SQL: " + e.getMessage(),
										"SQL Exception",
										JOptionPane.ERROR_MESSAGE);
							}
							if (layers != null) {
								String[] layerNames = new String[layers.length];
								for (int i=0; i<layers.length; i++) {
									layerNames[i] = layers[i][1];
								}
								layerList.setListData(layerNames);
							}
						} else {
							layerList.setListData(new Object[0]);
						}
						callStateChanged(false);
					}

				});

				layerList.addListSelectionListener(new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent arg0) {
						// TODO Auto-generated method stub
						int[] selected = groupList.getSelectedIndices();
						if (selected.length > 0) {
							callStateChanged(true);
						} else {
							callStateChanged(false);
						}
					}

				});

			} catch (SQLException e) {
				listPanel = new JPanel();
				JLabel label = new JLabel("SQL Exception: " + e.getMessage());
				listPanel.add(label);
			}

		}

		return listPanel;

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

}
