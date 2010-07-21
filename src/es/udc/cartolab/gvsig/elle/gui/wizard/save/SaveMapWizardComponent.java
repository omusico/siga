package es.udc.cartolab.gvsig.elle.gui.wizard.save;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.ConnectionJDBC;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.VectorialDriver;
import com.iver.cit.gvsig.fmap.drivers.jdbc.postgis.PostGisDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.VectorialDBAdapter;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.jtable.JTable;

import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.utils.LoadMap;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SaveMapWizardComponent extends WizardComponent implements ActionListener {

	public final static String PROPERTY_LAYERS_MAP = "table_layers";

	private JButton upButton;
	private JButton downButton;
	private JTextField mapNameField;
	private JCheckBox overviewChb;
	private JTable mapTable;


	private List<LayerProperties> mapLayers;

	private View view;

	public SaveMapWizardComponent(Map<String, Object> properties) {
		super(properties);

		mapLayers = new ArrayList<LayerProperties>();

		//layout
		MigLayout layout = new MigLayout("inset 0, align center",
				"10[grow]10",
		"10[grow]");

		setLayout(layout);

		add(getMainPanel(), "shrink, growx, growy, wrap");


	}

	private JPanel getMainPanel() {
		JPanel panel = new JPanel();
		MigLayout layout = new MigLayout("inset 0, align center",
				"[grow][grow]",
		"[grow][]");
		panel.setLayout(layout);


		//map, up & down buttons
		setMapTable();
		JPanel tablePanel = new JPanel();
		MigLayout tableLayout = new MigLayout("inset 0, align center", "[grow][]", "[grow]");
		tablePanel.setLayout(tableLayout);
		tablePanel.add(new JScrollPane(mapTable), "growx, growy");
		tablePanel.add(getUpDownPanel(), "shrink, align right, wrap");


		//map overview
		overviewChb = new JCheckBox(PluginServices.getText(this, "save_overview"));

		//map name
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel(PluginServices.getText(this, "map_name")));
		mapNameField = new JTextField("", 20);

		mapNameField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				callStateChanged();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});
		namePanel.add(mapNameField);

		//add to panel
		panel.add(tablePanel, "span 2 1, grow, wrap");
		panel.add(overviewChb, "grow 0, align left");
		panel.add(namePanel, "grow 0, align right, wrap");

		return panel;
	}

	private JPanel getUpDownPanel() {

		JPanel upDownPanel = new JPanel();
		MigLayout layout = new MigLayout("inset 0, align center",
				"[]",
		"[grow]");

		upDownPanel.setLayout(layout);
		java.net.URL imgURL = getClass().getResource("/go-up.png");
		upButton = new JButton(new ImageIcon(imgURL));
		upButton.addActionListener(this);

		imgURL = getClass().getResource("/go-down.png");
		downButton = new JButton(new ImageIcon(imgURL));
		downButton.addActionListener(this);

		upDownPanel.add(upButton, "shrink, wrap");
		upDownPanel.add(downButton, "shrink");

		return upDownPanel;
	}

	private void setMapTable() {
		String[] header = {"",
				PluginServices.getText(this, "layer"),
				PluginServices.getText(this, "name"),
				PluginServices.getText(this, "group"),
				PluginServices.getText(this, "visible"),
				PluginServices.getText(this, "max_scale"),
				PluginServices.getText(this, "min_scale")};
		DefaultTableModel model = new MapTableModel();
		for (String columnName : header) {
			model.addColumn(columnName);
		}
		model.setRowCount(0);
		mapTable =  new JTable();
		mapTable.setModel(model);

		mapTable.getColumnModel().getColumn(0).setMaxWidth(30);
		mapTable.getColumnModel().getColumn(1).setMinWidth(120);
		mapTable.getColumnModel().getColumn(2).setMinWidth(120);
		mapTable.getColumnModel().getColumn(3).setMinWidth(100);
		mapTable.getColumnModel().getColumn(4).setMinWidth(40);
		mapTable.getColumnModel().getColumn(5).setMinWidth(60);
		mapTable.getColumnModel().getColumn(6).setMinWidth(60);

	}

	@Override
	public boolean canFinish() {
		return canNext();
	}

	@Override
	public boolean canNext() {
		String mapname = mapNameField.getText();
		return mapname != null && !mapname.equals("");
	}

	@Override
	public String getWizardComponentName() {
		return "save_map";
	}


	private List<String> parse() {

		if (mapTable.isEditing()) {
			if (mapTable.getCellEditor() != null) {
				mapTable.getCellEditor().stopCellEditing();
			}
		}

		String shownNameError = PluginServices.getText(this, "error_empty_layer_name");
		String parseError = PluginServices.getText(this, "error_numeric_scale");
		String minGreaterError = PluginServices.getText(this, "error_min_greater_than_max");
		String repeatedLayerNameError = PluginServices.getText(this, "error_repeated_layer_name");

		List<String> errors = new ArrayList<String>();
		int position = 1;
		MapTableModel model = (MapTableModel) mapTable.getModel();
		List<String> layerNames = new ArrayList<String>();

		for (int i=0; i<model.getRowCount(); i++) {

			boolean save = (Boolean) model.getValueAt(i, 0);

			Object aux = model.getValueAt(i, 2);
			String shownName = null;
			if (aux!=null) {
				shownName = aux.toString();
				if (shownName.equals("")) {
					if (!errors.contains(shownNameError)) {
						errors.add(shownNameError);
					}
				}
			} else {
				if (!errors.contains(shownNameError)) {
					errors.add(shownNameError);
				}
			}
			if (layerNames.contains(shownName)) {
				errors.add(repeatedLayerNameError);
			}
			aux = model.getValueAt(i, 3);
			String group = null;
			if (aux!=null) {
				if (!aux.toString().equals("")) {
					group = aux.toString();
				}
			}
			boolean visible = (Boolean) model.getValueAt(i, 4);
			Double maxScale = null, minScale = null;
			try {
				aux = model.getValueAt(i, 5);
				if (aux!=null) {
					String str = aux.toString();
					if (!str.equals("")) {
						maxScale = NumberFormat.getInstance().parse(str).doubleValue();
					}
				}
			} catch (ParseException e) {
				if (!errors.contains(parseError)) {
					errors.add(parseError);
				}
			}
			try {
				aux = model.getValueAt(i, 6);
				if (aux != null) {
					String str = aux.toString();
					if (!str.equals("")) {
						minScale = NumberFormat.getInstance().parse(str).doubleValue();
					}
				}
			} catch (ParseException e) {
				if (!errors.contains(parseError)) {
					errors.add(parseError);
				}
			}
			if (minScale!=null && maxScale!=null && minScale > maxScale) {
				if (!errors.contains(minGreaterError)) {
					errors.add(minGreaterError);
				}
			}

			LayerProperties lp = mapLayers.get(i);
			lp.setShownname(shownName);
			lp.setPosition(position);
			lp.setVisible(visible);
			if (maxScale != null) {
				lp.setMaxScale(maxScale);
			}
			if (minScale != null) {
				lp.setMinScale(minScale);
			}
			lp.setGroup(group);
			lp.setSave(save);

			position++;

		}

		return errors;
	}

	@Override
	public void setProperties() throws WizardException {
		List<String> errors = parse();
		if (errors.size()>0) {
			String msg = PluginServices.getText(this, "errors_list");
			for (String error : errors) {
				msg = msg + "\n" + error;
			}
			throw new WizardException(msg);
		}
		properties.put(PROPERTY_LAYERS_MAP, mapLayers);
		properties.put(SaveLegendsWizardComponent.PROPERTY_SAVE_OVERVIEW, overviewChb.isSelected());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void showComponent() throws WizardException {
		DefaultTableModel model = (DefaultTableModel) mapTable.getModel();
		model.setRowCount(0);
		Object aux = properties.get(PROPERTY_LAYERS_MAP);
		if (aux != null && aux instanceof List<?>) {
			List<LayerProperties> layers = (List<LayerProperties>) aux;
			for (LayerProperties lp : layers) {
				double maxScale = lp.getMaxScale();
				String maxScaleStr = "";
				if (maxScale >= 0) {
					maxScaleStr = NumberFormat.getInstance().format(maxScale);
				}
				double minScale = lp.getMinScale();
				String minScaleStr = "";
				if (minScale >= 0) {
					minScaleStr = NumberFormat.getInstance().format(minScale);
				}
				Object[] row = {lp.save(),
						lp.getLayername(),
						lp.getShownname(),
						lp.getGroup(),
						lp.visible(),
						maxScaleStr,
						minScaleStr	};
				model.addRow(row);
			}
		} else {
			aux = properties.get(SaveMapWizard.PROPERTY_VIEW);
			if (aux != null && aux instanceof View) {
				view = (View) aux;
				fillWithViewLayers(mapTable, view.getMapControl().getMapContext().getLayers());
			} else {
				throw new WizardException("Couldn't retrieve the view");
			}
		}
	}

	private void fillWithViewLayers(JTable table, FLayers layers) {

		for (int i=layers.getLayersCount()-1; i>=0; i--) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLayers) {
				fillWithViewLayers(table, (FLayers) layer);
			} else if (layer instanceof FLyrVect) {
				VectorialDriver driver = ((FLyrVect) layer).getSource().getDriver();
				if (driver instanceof PostGisDriver) {
					DBLayerDefinition layerDef = ((VectorialDBAdapter) ((FLyrVect) layer).getSource()).getLyrDef();

					DBSession dbc = DBSession.getCurrentSession();
					try {
						String user = ((ConnectionJDBC)((PostGisDriver) driver).getConnection()).getConnection().getMetaData().getUserName();

						if (user != null && user.equals(dbc.getUserName())) {
							//layer data to fill the table
							String name = layer.getName();
							String group = layer.getParentLayer().getName();
							double maxScale = layer.getMaxScale();
							String maxScaleStr = "";
							if (maxScale >= 0) {
								maxScaleStr = NumberFormat.getInstance().format(maxScale);
							}
							double minScale = layer.getMinScale();
							String minScaleStr = "";
							if (minScale >= 0) {
								minScaleStr = NumberFormat.getInstance().format(minScale);
							}

							boolean visible = layer.isVisible();
							LayerProperties lp = new LayerProperties(layerDef.getSchema(), layerDef.getTableName(), layer.getName());

							mapLayers.add(lp);

							Object[] row = {true, name, name, group, visible, maxScaleStr, minScaleStr};
							((DefaultTableModel) table.getModel()).addRow(row);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}


	@Override
	public void finish() throws WizardException {

		//check existence of tables _map and _map_overview
		DBSession dbs = DBSession.getCurrentSession();
		try {
			boolean tableMapExists = dbs.tableExists(dbs.getSchema(), "_map");
			boolean tableMapOvExists = dbs.tableExists(dbs.getSchema(), "_map_overview");
			//TODO legends tables

			if (!tableMapExists || !tableMapOvExists) {

				boolean canCreate = dbs.getDBUser().canCreateTable(dbs.getSchema());
				if (!canCreate) {
					//[jestevez] I think this code is never reached due to the limitations of SaveMapExtension
					throw new WizardException(PluginServices.getText(this, "table_map_contact_admin"));
				} else {
					String message = String.format(PluginServices.getText(this, "tables_will_be_created"), dbs.getSchema());
					int answer = JOptionPane.showConfirmDialog(
							this,
							message,
							"",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null);
					if (answer == 0) {
						LoadMap.createMapTables();
						JOptionPane.showMessageDialog(
								this,
								PluginServices.getText(this, "tables_created_correctly"),
								"",
								JOptionPane.INFORMATION_MESSAGE);
						tableMapExists = true;
					}
				}
			}


			if (tableMapExists) {

				String mapName = mapNameField.getText();

				boolean mapExists = LoadMap.mapExists(mapName);
				if (mapExists) {
					String question = PluginServices.getText(this, "overwrite_map_question");
					question = String.format(question, mapName);
					int answer = JOptionPane.showOptionDialog(
							this,
							question,
							PluginServices.getText(this, "overwrite_map"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							null,
							null);

					if (answer!=0) {
						throw new WizardException("", false, false);
					} else {
						LoadMap.deleteMap(mapName);
					}
				}


				PluginServices.getMDIManager().setWaitCursor();


				String[] errors = saveMap(mapName);
				PluginServices.getMDIManager().restoreCursor();
				if (errors.length>0) {
					String msg = PluginServices.getText(this, "errors_list");
					for (String error : errors) {
						msg = msg + "\n" + error;
					}
					throw new WizardException(msg);
				}
			}

		} catch (SQLException e1) {
			PluginServices.getMDIManager().restoreCursor();
			throw new WizardException(PluginServices.getText(this, "error_saving_map"), e1);
		}
	}

	private String[] saveMap(String mapName) throws SQLException {

		String mapoverviewError = PluginServices.getText(this, "error_overview");

		List<Object[]> rows = new ArrayList<Object[]>();

		List<String> errors = parse();

		if (errors.size()>0) {
			return errors.toArray(new String[0]);
		} else {
			for (LayerProperties lp : mapLayers) {
				Double maxScale = null, minScale = null;
				if (lp.getMaxScale() > -1) {
					maxScale = lp.getMaxScale();
				}
				if (lp.getMinScale() > -1) {
					minScale = lp.getMinScale();
				}
				if (lp.save()) {
					Object[] row = {lp.getShownname(),
							lp.getTablename(),
							lp.getPosition(),
							lp.visible(),
							maxScale,
							minScale,
							lp.getGroup(),
							lp.getSchema()};
					rows.add(row);
				}
			}

			if (overviewChb.isSelected()) {
				try {
					saveOverview(mapName);
				} catch (SQLException e) {
					return new String[]{mapoverviewError};
				}
			}
			LoadMap.saveMap(rows.toArray(new Object[0][0]), mapName);
			return new String[0];
		}

	}

	private void saveOverview(String mapName) throws SQLException {
		FLayers layers = view.getMapOverview().getMapContext().getLayers();
		List<Object[]> rows = new ArrayList<Object[]>();
		List<String> knownTables = new ArrayList<String>();
		for (int i=layers.getLayersCount()-1; i>=0; i--) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLyrVect) {
				VectorialDriver driver = ((FLyrVect) layer).getSource().getDriver();
				if (driver instanceof PostGisDriver) {

					DBLayerDefinition layerDef = ((VectorialDBAdapter) ((FLyrVect) layer).getSource()).getLyrDef();

					DBSession dbc = DBSession.getCurrentSession();
					String user;
					try {
						user = ((ConnectionJDBC)((PostGisDriver) driver).getConnection()).getConnection().getMetaData().getUserName();

						if (user != null && user.equals(dbc.getUserName())) {
							if (!knownTables.contains(layerDef.getComposedTableName())) {
								String tablename = layerDef.getTableName();
								String schema = layerDef.getSchema();
								String[] row = {tablename, schema};
								rows.add(row);
								knownTables.add(layerDef.getComposedTableName());
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		LoadMap.saveMapOverview(rows.toArray(new Object[0][0]), mapName);

	}

	private void moveRowsDown() {
		int[] selectedRows = mapTable.getSelectedRows();
		DefaultTableModel model = (DefaultTableModel) mapTable.getModel();
		ListSelectionModel selectionModel = mapTable.getSelectionModel();
		mapTable.clearSelection();
		int beginPos = 0;
		int endPos = 0;
		for(int i=0; i<selectedRows.length; i++) {
			//determine the beginning and ending of the selected rows group
			beginPos = selectedRows[i];
			endPos = selectedRows[i];
			for (int j=i+1; j<selectedRows.length; j++) {
				if (selectedRows[j]-endPos == 1) {
					endPos++;
				} else {
					break;
				}
				i = j;
			}
			if (mapTable.getRowCount()>endPos+1) {
				//reorder the table
				model.moveRow(beginPos, endPos, beginPos+1);
				selectionModel.addSelectionInterval(beginPos+1, endPos+1);
				//reorder lists - move last unselected value to next position of the last selected one
				LayerProperties elementToMove = mapLayers.get(beginPos+1);
				mapLayers.remove(beginPos+1);
				mapLayers.add(endPos, elementToMove);
			} else {
				//the selection group is at the top of the table, don't move anything
				selectionModel.addSelectionInterval(beginPos, endPos);
			}
		}
	}

	private void moveRowsUp() {
		int[] selectedRows = mapTable.getSelectedRows();
		DefaultTableModel model = (DefaultTableModel) mapTable.getModel();
		ListSelectionModel selectionModel = mapTable.getSelectionModel();
		mapTable.clearSelection();
		int beginPos = 0;
		int endPos = 0;
		for(int i=0; i<selectedRows.length; i++) {
			//determine the beginning and ending of the selected rows group
			beginPos = selectedRows[i];
			endPos = selectedRows[i];
			for (int j=i+1; j<selectedRows.length; j++) {
				if (selectedRows[j]-endPos == 1) {
					endPos++;
				} else {
					break;
				}
				i = j;
			}
			if (beginPos-1>=0) {
				//reorder the table
				model.moveRow(beginPos, endPos, beginPos-1);
				selectionModel.addSelectionInterval(beginPos-1, endPos-1);
				//reorder lists - move last unselected value to next position of the last selected one
				LayerProperties elementToMove = mapLayers.get(beginPos-1);
				mapLayers.remove(beginPos-1);
				mapLayers.add(endPos, elementToMove);
			} else {
				//the selection group is at the top of the table, don't move anything
				selectionModel.addSelectionInterval(beginPos, endPos);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == upButton) {
			moveRowsUp();
		}
		if (e.getSource() == downButton) {
			moveRowsDown();
		}
	}

	private class MapTableModel extends DefaultTableModel {

		@Override
		public Class<?> getColumnClass(int index) {
			if (index == 0 || index == 4) {
				return Boolean.class;
			} else {
				return super.getColumnClass(index);
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			if (column == 1) {
				return false;
			}
			return true;
		}

	}
}
