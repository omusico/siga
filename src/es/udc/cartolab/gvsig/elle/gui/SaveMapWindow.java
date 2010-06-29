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

public class SaveMapWindow extends JPanel implements IWindow, ActionListener {

	private final int widthView = 750;
	private final int widthNoView = 500;
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

	private static Logger logger = Logger.getLogger(SaveMapWindow.class);

	public SaveMapWindow() {
		this(null);
	}

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
		if (view!=null) {
			MigLayout layout = new MigLayout("inset 0, align center",
					"[grow][]",
			"[grow][]");
			panel.setLayout(layout);
		} else {
			MigLayout layout = new MigLayout("inset 0, align center",
					"[][grow][]",
			"[grow][]");
			panel.setLayout(layout);
			//add left table and moveleft/moveright buttons
		}

		setMapTable();

		panel.add(new JScrollPane(mapTable), "shrink, growx, growy");
		panel.add(getUpDownPanel(), "shrink, wrap");


		//map name
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("Nombre del mapa:"));
		mapNameField = new JTextField("", 20);
		namePanel.add(mapNameField);

		panel.add(namePanel, "growy, align right, wrap");

		fillWithViewLayers(mapTable, view.getMapControl().getMapContext().getLayers());
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
		String[] header = {"", "Capa", "Nombre", "Grupo", "Visible", "Escala máxima", "Escala mínima"};
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
			int width;
			if (view != null) {
				width = widthView;
			} else {
				width = widthNoView;
			}
			viewInfo = new WindowInfo(WindowInfo.MODALDIALOG);
			viewInfo.setTitle(PluginServices.getText(this, "Load_map"));
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
		String mapName = mapNameField.getText();
		if (!mapName.equals("")) {
			try {

				if (mapTable.isEditing()) {
					if (mapTable.getCellEditor() != null) {
						mapTable.getCellEditor().stopCellEditing();
					}
				}

				boolean mapExists = LoadMap.mapExists(mapName);
				boolean save = true;
				boolean close = true;
				if (mapExists) {

					int answer = JOptionPane.showOptionDialog(
							this,
							"El mapa ya existe, ¿desea continuar?",
							"",
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
					String[] errors = saveMap(mapExists, mapName);
					if (errors.length>0) {
						String msg = "Se han producido los siguientes errores:";
						for (String error : errors) {
							msg = msg + "\n" + error;
						}
						JOptionPane.showMessageDialog(
								this,
								msg,
								"",
								JOptionPane.ERROR_MESSAGE);
						close = false;
					}
				}
				if (close) {
					closeWindow();
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(
						this,
						"Error al guardar el mapa",
						"",
						JOptionPane.ERROR_MESSAGE);
				logger.error(e1.getMessage(), e1);
			}
		} else {
			JOptionPane.showMessageDialog(
					this,
					"El nombre del mapa no puede estar vacío.",
					"",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String[] saveMap(boolean registerExists, String mapName) throws SQLException {

		int position = 1;
		MapTableModel model = (MapTableModel) mapTable.getModel();
		List<Object[]> rows = new ArrayList<Object[]>();
		List<String> errors = new ArrayList<String>();
		String shownNameError = "El nombre de la capa no puede estar vacío.";
		String parseError = "Las escalas deben ser numéricas.";
		String minGreaterError = "La escala mínima no puede ser superior a la máxima";
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
			LoadMap.saveMap(rows.toArray(new Object[0][0]), mapName);
			return new String[0];
		}

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
