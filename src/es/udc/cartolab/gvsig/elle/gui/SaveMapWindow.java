package es.udc.cartolab.gvsig.elle.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
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

import es.udc.cartolab.gvsig.elle.utils.LoadMap;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class SaveMapWindow extends JPanel implements IWindow, ActionListener {

	private final int width = 750;
	private final int height = 500;

	private View view = null;
	private WindowInfo viewInfo;
	private JTable mapTable;

	private List<String> schemas;
	private List<String> tableNames;

	private JButton okButton, cancelButton;
	private JButton upButton;
	private JButton downButton;
	private JTextField mapNameField;
	private JCheckBox overviewChb;

	private static Logger logger = Logger.getLogger(SaveMapWindow.class);

	public SaveMapWindow(View view) {
		this.view = view;
		schemas = new ArrayList<String>();
		tableNames = new ArrayList<String>();

		//layout
		MigLayout layout = new MigLayout("inset 0, align center",
				"10[grow]10",
		"10[grow][]");

		setLayout(layout);

		add(getMainPanel(), "shrink, growx, growy, wrap");
		add(getSouthPanel(), "shrink, align right");


	}

	private JPanel getSouthPanel() {

		okButton = new JButton(PluginServices.getText(this, "ok"));
		okButton.addActionListener(this);
		cancelButton = new JButton(PluginServices.getText(this, "cancel"));
		cancelButton.addActionListener(this);

		JPanel southPanel = new JPanel();
		southPanel.add(okButton);
		southPanel.add(cancelButton);

		return southPanel;
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
		fillWithViewLayers(mapTable, view.getMapControl().getMapContext().getLayers());


		//map overview
		overviewChb = new JCheckBox(PluginServices.getText(this, "save_overview"));
		//		optionsPanel.add(overviewChb, "growy, align left");

		//map name
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel(PluginServices.getText(this, "map_name")));
		mapNameField = new JTextField("", 20);
		namePanel.add(mapNameField);
		//		optionsPanel.add(namePanel, "shrink, align right, wrap");

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
	public WindowInfo getWindowInfo() {
		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
			viewInfo.setTitle(PluginServices.getText(this, "save_map"));
			viewInfo.setWidth(width);
			viewInfo.setHeight(height);
		}
		return viewInfo;
	}

	@Override
	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	private void fillWithViewLayers(JTable table, FLayers layers) {

		for (int i=layers.getLayersCount()-1; i>=0; i--) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLayers) {
				fillWithViewLayers(table, (FLayers) layer);
			} else if (layer instanceof FLyrVect) {
				VectorialDriver driver = ((FLyrVect) layer).getSource().getDriver();
				if (driver instanceof PostGisDriver) {
					//					ReadableVectorial rv = ((VectorialDBAdapter) ((FLyrVect) layer).getSource()).getOriginalAdapter();
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
							schemas.add(layerDef.getSchema());
							tableNames.add(layerDef.getTableName());
							boolean visible = layer.isVisible();
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
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == cancelButton) {
			closeWindow();
		}
		if (e.getSource() == okButton) {
			//check map name
			saveMap();
		}
		if (e.getSource() == upButton) {
			moveRowsUp();
		}
		if (e.getSource() == downButton) {
			moveRowsDown();
		}
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
				String elementToMove = schemas.get(beginPos+1);
				schemas.remove(beginPos+1);
				schemas.add(endPos, elementToMove);
				elementToMove = tableNames.get(beginPos+1);
				tableNames.remove(beginPos+1);
				tableNames.add(endPos, elementToMove);
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
				String elementToMove = schemas.get(beginPos-1);
				schemas.remove(beginPos-1);
				schemas.add(endPos, elementToMove);
				elementToMove = tableNames.get(beginPos-1);
				tableNames.remove(beginPos-1);
				tableNames.add(endPos, elementToMove);
			} else {
				//the selection group is at the top of the table, don't move anything
				selectionModel.addSelectionInterval(beginPos, endPos);
			}
		}
	}

	private void saveMap() {

		//check existence of tables _map and _map_overview
		DBSession dbs = DBSession.getCurrentSession();
		try {
			boolean existsMap = dbs.tableExists(dbs.getSchema(), "_map");
			boolean existsMapOverview = dbs.tableExists(dbs.getSchema(), "_map_overview");

			if (!existsMap || !existsMapOverview) {

				boolean canCreate = dbs.getDBUser().canCreateTable(dbs.getSchema());
				if (!canCreate) {
					//JOptionPane...
					//					errors.add("No existen las tablas para guardar los mapas y no tiene permisos de usuario para crearlas, )
				} else {
					//JOptionPane...
					LoadMap.createMapTables();
					//JOptionPane...
					existsMap = true;
				}
			}


			if (existsMap) {
				String mapName = mapNameField.getText();
				if (!mapName.equals("")) {


					boolean mapExists = LoadMap.mapExists(mapName);
					boolean save = true;
					boolean close = true;
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
							save = false;
						}
						if (answer==1) {
							close = false;
						}
					}
					if (save) {

						PluginServices.getMDIManager().setWaitCursor();

						if (mapTable.isEditing()) {
							if (mapTable.getCellEditor() != null) {
								mapTable.getCellEditor().stopCellEditing();
							}
						}
						String[] errors = saveMap(mapName);
						PluginServices.getMDIManager().restoreCursor();
						if (errors.length>0) {
							String msg = PluginServices.getText(this, "errors_list");
							for (String error : errors) {
								msg = msg + "\n" + error;
							}
							JOptionPane.showMessageDialog(
									this,
									msg,
									"",
									JOptionPane.ERROR_MESSAGE);
							close = false;
						} else {
							JOptionPane.showMessageDialog(
									this,
									PluginServices.getText(this, "map_saved_correctly"),
									"",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
					if (close) {
						closeWindow();
					}
				} else {
					JOptionPane.showMessageDialog(
							this,
							PluginServices.getText(this, "error_empty_map_name"),
							"",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException e1) {
			PluginServices.getMDIManager().restoreCursor();
			JOptionPane.showMessageDialog(
					this,
					PluginServices.getText(this, "error_saving_map"),
					"",
					JOptionPane.ERROR_MESSAGE);
			logger.error(e1.getMessage(), e1);
		}
	}

	private String[] saveMap(String mapName) throws SQLException {

		List<String> errors = new ArrayList<String>();

		int position = 1;
		MapTableModel model = (MapTableModel) mapTable.getModel();
		List<Object[]> rows = new ArrayList<Object[]>();

		String shownNameError = PluginServices.getText(this, "error_empty_layer_name");
		String parseError = PluginServices.getText(this, "error_numeric_scale");
		String minGreaterError = PluginServices.getText(this, "error_min_greater_than_max");
		String mapoverviewError = PluginServices.getText(this, "error_overview");
		String repeatedLayerNameError = PluginServices.getText(this, "error_repeated_layer_name");
		List<String> layerNames = new ArrayList<String>();
		for (int i=0; i<model.getRowCount(); i++) {

			if ((Boolean) model.getValueAt(i, 0)) {
				String schema = schemas.get(i);
				String table = tableNames.get(i);
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

				Object[] row = {shownName, table, position, visible, maxScale, minScale, group, schema};
				rows.add(row);
				position++;

			}
		}
		if (errors.size()>0) {
			return errors.toArray(new String[0]);
		} else {
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

	@SuppressWarnings("deprecation")
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


	private void closeWindow() {
		PluginServices.getMDIManager().closeWindow(this);
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
