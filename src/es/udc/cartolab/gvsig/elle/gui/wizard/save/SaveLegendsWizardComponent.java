package es.udc.cartolab.gvsig.elle.gui.wizard.save;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.legend.LegendDriverException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.XMLEntity;

import es.udc.cartolab.gvsig.elle.gui.EllePreferencesPage;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.utils.LoadLegend;
import es.udc.cartolab.gvsig.elle.utils.LoadMap;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SaveLegendsWizardComponent extends WizardComponent {

	public final static String PROPERTY_SAVE_OVERVIEW = "save_overview";


	private String[] types = {"gvl", "sld"};

	private JRadioButton noLegendRB, databaseRB, fileRB;
	private JPanel dbPanel;
	private JPanel filePanel;
	private JTextField dbStyles, fileStyles;
	private JTable table;
	private JCheckBox overviewCHB;
	private JComboBox overviewCB;

	private List<FLayer> layers;

	private String legendDir = null;

	public SaveLegendsWizardComponent(Map<String, Object> properties) {
		super(properties);

		XMLEntity xml = PluginServices.getPluginServices("es.udc.cartolab.gvsig.elle").getPersistentXML();
		if (xml.contains(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME)) {
			legendDir = xml.getStringProperty(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME);
		}

		setLayout(new MigLayout("",
				"10[grow]",
		"[grow][]"));



		dbPanel = getDBPanel();
		filePanel = getFilePanel();

		setTable();
		add(new JScrollPane(table), "shrink, growx, growy, wrap");

		//options
		add(getOptionsPanel(), "shrink, growx, growy");
	}


	private JPanel getOptionsPanel() {

		JPanel panelOptions = new JPanel();
		panelOptions.setLayout(new MigLayout("",
				"[grow]80",
		"[][]15[][]15[][]"));


		panelOptions.add(getOverviewPanel(), "wrap");

		noLegendRB = new JRadioButton("No usar leyendas");
		noLegendRB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dbSetEnabled(false);
				fileSetEnabled(false);
			}

		});
		panelOptions.add(noLegendRB, "wrap");

		databaseRB = new JRadioButton("Cargar leyendas desde la base de datos");
		databaseRB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dbSetEnabled(true);
				fileSetEnabled(false);
			}

		});
		panelOptions.add(databaseRB, "shrink, growx, growy, wrap");
		panelOptions.add(dbPanel, "shrink, growx, growy, wrap");

		fileRB = new JRadioButton("Cargar desde disco duro");
		fileRB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dbSetEnabled(false);
				fileSetEnabled(true);
			}

		});
		panelOptions.add(fileRB, "wrap");
		panelOptions.add(filePanel, "shrink, growx, growy, wrap");

		ButtonGroup group = new ButtonGroup();
		group.add(noLegendRB);
		group.add(databaseRB);
		group.add(fileRB);

		noLegendRB.setSelected(true);
		dbSetEnabled(false);
		fileSetEnabled(false);

		return panelOptions;
	}

	private JPanel getOverviewPanel() {
		JPanel overviewPanel = new JPanel();
		overviewPanel.setLayout(new MigLayout("",
				"[grow][][right]",
		"[]"));

		overviewCHB = new JCheckBox("Guardar las leyendas del localizador");
		overviewCHB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				overviewCB.setEnabled(overviewCHB.isSelected());
			}

		});
		overviewPanel.add(overviewCHB);
		overviewPanel.add(new JLabel("Formato"));

		overviewCB = new JComboBox();
		for (String type : types) {
			overviewCB.addItem(type);
		}

		overviewPanel.add(overviewCB, "shrink, wrap");

		return overviewPanel;

	}

	private void setTable() {
		String[] header = {"",
				PluginServices.getText(this, "name"),
				//				PluginServices.getText(this, "group"),
		"type"};
		DefaultTableModel model = new LegendTableModel();
		for (String h : header) {
			model.addColumn(h);
		}

		table = new JTable();
		table.setModel(model);

		TableColumn col = table.getColumnModel().getColumn(2);

		JComboBox cbox = new JComboBox();
		for (String type : types) {
			cbox.addItem(type);
		}
		col.setCellEditor(new DefaultCellEditor(cbox));

		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.getColumnModel().getColumn(2).setMaxWidth(60);

	}


	private void dbSetEnabled(boolean enabled) {
		dbStyles.setEnabled(enabled);
	}

	private void fileSetEnabled(boolean enabled) {
		fileStyles.setEnabled(enabled);
	}

	private JPanel getDBPanel() {

		JPanel panel = new JPanel();
		MigLayout layout = new MigLayout("inset 0, align center",
				"10[][grow, right]10",
		"5[grow]5");
		panel.setLayout(layout);

		JLabel label = new JLabel("Seleccione conjunto de leyendas");
		dbStyles = new JTextField("", 20);

		panel.add(label);
		panel.add(dbStyles, "shrink, right, wrap");

		return panel;
	}

	private JPanel getFilePanel() {

		JPanel panel = new JPanel();
		MigLayout layout = new MigLayout("inset 0, align center",
				"10[][grow, right]10",
		"5[grow]5");
		panel.setLayout(layout);

		fileStyles = new JTextField("", 20);
		if (legendDir != null) {
			panel.add(new JLabel(PluginServices.getText(this, "legend")));
			panel.add(fileStyles, "shrink, right, wrap");
		} else {
			panel.add(new JLabel("<html>El directorio de leyendas no está configurado.<br> Por favor, selecciónelo en el panel de configuraciónd de gvSIG.</html>"), "span 2 wrap");
		}

		return panel;

	}

	@Override
	public boolean canFinish() {
		return true;
	}

	@Override
	public boolean canNext() {
		return true;
	}

	@Override
	public void finish() throws WizardException {

		if (!noLegendRB.isSelected()) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			if ((fileRB.isSelected() && !fileStyles.equals("")) || (databaseRB.isSelected() && !dbStyles.equals(""))) {
				boolean cont = true;
				boolean useNotGvl = false;
				for (int i = 0; i<model.getRowCount(); i++) {
					String type = model.getValueAt(i, 2).toString();
					if (!type.toLowerCase().equals("gvl")) {
						useNotGvl = true;
						break;
					}
				}
				if (overviewCHB.isSelected() && !overviewCB.getSelectedItem().toString().toLowerCase().equals("gvl")) {
					useNotGvl = true;
				}
				if (useNotGvl) {
					Object[] options = {PluginServices.getText(this, "ok"),
							PluginServices.getText(this, "cancel")};
					int n = JOptionPane.showOptionDialog(this,
							String.format("El formato de alguna de las leyendas no es propio de gvSIG y por tanto no es totalmente compatible, por lo que se pueden perder" +
							" algunas características, ¿desea continuar?"),
							null,
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							options,
							options[1]);
					if (n!=0) {
						cont = false;
					}
				}
				if (cont) {
					try {
						String dirName = fileStyles.getText();
						File dir = null;
						if (fileRB.isSelected()) {
							dir = getDir(dirName);
							if (dir == null) {
								cont = false;
							}
						} else {
							if (LoadMap.legendExists(dbStyles.getText())) {
								Object[] options = {PluginServices.getText(this, "ok"),
										PluginServices.getText(this, "cancel")};
								String message = PluginServices.getText(this, "overwrite_legend_question");
								int n = JOptionPane.showOptionDialog(this,
										String.format(message, dbStyles.getText()),
										PluginServices.getText(this, "overwrite_legend"),
										JOptionPane.YES_NO_CANCEL_OPTION,
										JOptionPane.WARNING_MESSAGE,
										null,
										options,
										options[1]);
								if (n!=0) {
									cont = false;
								} else {
									LoadMap.deleteLegends(dbStyles.getText());
								}
							}
						}
						if (cont) {
							saveLegends(model, dir);
							saveOverviewLegends(dir);
						} else {
							throw new WizardException("", false, false);
						}
					} catch (WizardException e) {
						throw e;
					} catch (Exception e) {
						throw new WizardException(e);
					}
				} else {
					throw new WizardException("", false, false);
				}
			}
		}

	}

	private void saveLegends(DefaultTableModel model, File dir) throws LegendDriverException, IOException, SQLException {
		for (int i=0; i<model.getRowCount(); i++) {
			if ((Boolean) model.getValueAt(i, 0)) {
				FLayer layer = layers.get(i);
				if (layer instanceof FLyrVect) {
					String type = model.getValueAt(i, 2).toString();
					if (fileRB.isSelected()) {
						saveFileLegend(dir, (FLyrVect) layer, type);
					} else {
						saveDBLegend(dbStyles.getText(), (FLyrVect) layer, type, "_map_style");
					}
				}
			}
		}
	}

	private void saveOverviewLegends(File dir) throws LegendDriverException, WizardException, IOException, SQLException {
		Object aux = properties.get(SaveMapWizardComponent.PROPERTY_VIEW);
		if (aux != null && aux instanceof View) {
			FLayers ovLayers = ((View) aux).getMapOverview().getMapContext().getLayers();
			if (fileRB.isSelected()) {
				File overviewDir = new File(dir.getAbsolutePath() + File.separator + "overview");
				if (!overviewDir.exists()) {
					overviewDir.mkdir();
				}
				if (!overviewDir.isDirectory()) {
					String msg = PluginServices.getText(this, "legend_overview_error");
					throw new WizardException(String.format(msg, overviewDir.getAbsolutePath()));
				}
			}
			for (int i=0; i<ovLayers.getLayersCount(); i++) {
				if (ovLayers.getLayer(i) instanceof FLyrVect) {
					if (fileRB.isSelected()) {
						File overviewDir = new File(dir.getAbsolutePath() + File.separator + "overview");
						saveFileLegend(overviewDir, (FLyrVect) ovLayers.getLayer(i), overviewCB.getSelectedItem().toString().toLowerCase());
					} else {
						saveDBLegend(dbStyles.getText(), (FLyrVect) ovLayers.getLayer(i), overviewCB.getSelectedItem().toString().toLowerCase(), "_map_overview_style");
					}
				}
			}
		}
	}

	private void saveDBLegend(String legendName, FLyrVect layer, String type, String table) throws IOException, LegendDriverException, SQLException {
		File legendFile = File.createTempFile("style", "." + type);
		LoadLegend.saveLegend(layer, legendFile);
		BufferedReader reader = new BufferedReader(new FileReader(legendFile.getAbsolutePath()));
		StringBuffer buffer = new StringBuffer();
		String line = reader.readLine();
		while (line != null) {
			buffer.append(line).append("\n");
			line = reader.readLine();
		}
		String xml = buffer.toString();
		String[] row = {
				layer.getName(),
				legendName,
				type,
				xml
		};
		DBSession dbs = DBSession.getCurrentSession();
		dbs.insertRow(dbs.getSchema(), table, row);
	}

	private void saveFileLegend(File dir, FLyrVect layer, String type) throws LegendDriverException {
		if (dir!=null) {
			String path = dir.getAbsolutePath();
			if (!path.endsWith(File.separator)) {
				path = path + File.separator;
			}
			File legendFile = new File(path + layer.getName() + "." + type);
			LoadLegend.saveLegend(layer, legendFile);
		}
	}


	@Override
	public String getWizardComponentName() {
		return "save_legends";
	}

	@Override
	public void setProperties() {
		// Nothing to do

	}

	@Override
	public void showComponent() throws WizardException {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		Object aux = properties.get(SaveMapWizardComponent.PROPERTY_LAYERS_MAP);
		layers = getLayers();
		if (aux != null && aux instanceof List<?>) {
			List<LayerProperties> list = (List<LayerProperties>) aux;
			for (LayerProperties lp : list) {
				Object[] row = new Object[3];
				row[0] = lp.save();
				row[1] = lp.getShownname();
				row[2] = "gvl";
				model.addRow(row);
			}
		}

		//checkbox
		aux = properties.get(PROPERTY_SAVE_OVERVIEW);
		if (aux != null && aux instanceof Boolean) {
			overviewCHB.setSelected((Boolean) aux);
		}
		overviewCB.setEnabled(overviewCHB.isSelected());
	}

	@SuppressWarnings("unchecked")
	private List<FLayer> getLayers() throws WizardException {
		Object aux = properties.get(SaveMapWizardComponent.PROPERTY_VIEW);
		if (aux == null || !(aux instanceof View)) {
			throw new WizardException("property");
		}
		View view = (View) aux;
		aux = properties.get(SaveMapWizardComponent.PROPERTY_LAYERS_MAP);
		if (aux == null || !(aux instanceof List<?>)) {
			throw new WizardException("property");
		}
		List<LayerProperties> layers = (List<LayerProperties>) aux;
		List<FLayer> layerList = new ArrayList<FLayer>();
		for (LayerProperties lp : layers) {
			String layerName = lp.getLayername();
			layerList.add(getLayer(view.getMapControl().getMapContext().getLayers(), layerName));
		}
		return layerList;
	}

	//checks if it's possible to save and then saves
	private File getDir(String dir) throws WizardException {
		PluginServices ps = PluginServices.getPluginServices("es.udc.cartolab.gvsig.elle");
		XMLEntity xml = ps.getPersistentXML();
		if (xml.contains(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME)) {
			String path = xml.getStringProperty(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME);
			if (path.endsWith(File.separator)) {
				path = path + dir + File.separator;
			} else {
				path = path + File.separator + dir + File.separator;
			}
			File f = new File(path);
			if (!f.exists()) {
				if (!f.mkdir()) {
					String message = PluginServices.getText(this, "legend_write_dir_error");
					throw new WizardException(String.format(message, path));
				} else {
					return f;
				}
			} else {
				if (!f.isDirectory()) {
					throw new WizardException("legend_exist_file_error", false);
				}
				//overwrite?
				Object[] options = {PluginServices.getText(this, "ok"),
						PluginServices.getText(this, "cancel")};
				String message = PluginServices.getText(this, "overwrite_legend_question");
				int n = JOptionPane.showOptionDialog(this,
						String.format(message, dir),
						PluginServices.getText(this, "overwrite_legend"),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null,
						options,
						options[1]);
				if (n==0) {
					return f;
				} else {
					return null;
				}
			}

		} else {
			throw new WizardException("no configurado");
		}
	}

	//there's no chekcing here
	private void saveDir(File f, String type) throws WizardException {
		String path = f.getAbsolutePath();
		if (f.canWrite()) {
			if (layers!=null) {
				for (FLayer layer : layers) {
					if (layer instanceof FLyrVect) {
						String legendFilePath = path + layer.getName().toLowerCase() + "." + type;
						File legendFile = new File(legendFilePath);
						try {
							LoadLegend.saveLegend((FLyrVect) layer, legendFile);
						} catch (LegendDriverException e) {
							throw new WizardException(e);
						}
					}

				}
			}
			/* overview */
			//			if (overviewLayers != null) {
			//				File overviewDir = new File(f.getAbsolutePath() + File.separator + "overview");
			//				if (!overviewDir.exists()) {
			//					overviewDir.mkdir();
			//				}
			//				if (overviewDir.isDirectory()) {
			//					for (int i=0; i<overviewLayers.length; i++) {
			//						String legendFilePath = overviewDir.getAbsolutePath() + File.separator + overviewLayers[i].getName().toLowerCase() + ".gvl";
			//						File legendFile = new File(legendFilePath);
			//						writeLegend(overviewLayers[i], legendFile);
			//					}
			//				} else {
			//					String msg = PluginServices.getText(this, "legend_overview_error");
			//					throw new WizardException(String.format(msg, overviewDir.getAbsolutePath()));
			//				}
			//			}
		} else {
			//error no se puede escribir
			String message = PluginServices.getText(this, "legend_write_dir_error");
			throw new WizardException(String.format(message, path));
		}
	}

	private FLayer getLayer(FLayers layers, String layerName) {
		for (int i=layers.getLayersCount()-1; i>=0; i--) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLayers) {
				return getLayer((FLayers) layer, layerName);
			} else {
				if (layerName.equals(layer.getName())) {
					return layer;
				}
			}
		}
		return null;
	}

	private class LegendTableModel extends DefaultTableModel {

		@Override
		public Class<?> getColumnClass(int index) {
			if (index == 0) {
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
