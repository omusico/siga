package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.XMLEntity;

import es.udc.cartolab.gvsig.elle.gui.EllePreferencesPage;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.utils.LoadLegend;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LoadLegendWizardComponent extends WizardComponent {

	private JRadioButton noLegendRB, databaseRB, fileRB;
	private JPanel dbPanel;
	private JPanel filePanel;
	private JComboBox dbStyles, fileStyles;

	private String legendDir = null;

	public LoadLegendWizardComponent(Map<String, Object> properties) {
		super(properties);

		XMLEntity xml = PluginServices.getPluginServices("es.udc.cartolab.gvsig.elle").getPersistentXML();
		if (xml.contains(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME)) {
			legendDir = xml.getStringProperty(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME);
		}

		setLayout(new MigLayout("inset 0, align center",
				"20[grow]",
		"[]15[][]15[][]"));

		dbPanel = getDBPanel();
		filePanel = getFilePanel();

		noLegendRB = new JRadioButton("No usar leyendas");
		noLegendRB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dbSetEnabled(false);
				fileSetEnabled(false);
			}

		});
		add(noLegendRB, "wrap");

		databaseRB = new JRadioButton("Cargar leyendas desde la base de datos");
		databaseRB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dbSetEnabled(true);
				fileSetEnabled(false);
			}

		});
		add(databaseRB, "wrap");
		add(dbPanel, "wrap");

		fileRB = new JRadioButton("Cargar desde disco duro");
		fileRB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dbSetEnabled(false);
				fileSetEnabled(true);
			}

		});
		add(fileRB, "wrap");
		add(filePanel, "wrap");

		ButtonGroup group = new ButtonGroup();
		group.add(noLegendRB);
		group.add(databaseRB);
		group.add(fileRB);

		noLegendRB.setSelected(true);
		dbSetEnabled(false);
		fileSetEnabled(false);


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
				"10[grow][]10",
		"5[grow]5");
		panel.setLayout(layout);

		JLabel label = new JLabel("Seleccione conjunto de leyendas");
		dbStyles = new JComboBox();

		panel.add(label);
		panel.add(dbStyles, "wrap");

		return panel;
	}

	private JPanel getFilePanel() {

		JPanel panel = new JPanel();
		MigLayout layout = new MigLayout("inset 0, align center",
				"10[grow][]10",
		"5[grow]5");
		panel.setLayout(layout);

		fileStyles = new JComboBox();
		if (legendDir != null) {
			File f = new File(legendDir);
			File[] files = f.listFiles();
			for (int i=0; i<files.length; i++) {
				if (files[i].isDirectory() && !files[i].isHidden()) {
					fileStyles.addItem(files[i].getName());
				}
			}
			panel.add(new JLabel(PluginServices.getText(this, "legend")));
			panel.add(fileStyles, "wrap");
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
	public String getWizardComponentName() {
		return "legend_wizard_component";
	}

	@Override
	public void showComponent() {
		dbStyles.removeAllItems();

		DBSession dbs = DBSession.getCurrentSession();
		try {
			String[] legends = dbs.getDistinctValues("_map_style", dbs.getSchema(), "nombre_estilo", true, false);
			for (String legend : legends) {
				dbStyles.addItem(legend);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadDBLegend(FLyrVect layer) throws SQLException, IOException {
		DBSession dbs = DBSession.getCurrentSession();
		String layerName = layer.getName();
		String styleName = dbStyles.getSelectedItem().toString();
		String[][] style = dbs.getTable("_map_style", "where nombre_capa='" + layerName + "' and nombre_estilo='" + styleName + "'");
		if (style.length == 1) {
			String type = style[0][2];
			String def = style[0][3];

			File tmpLegend = File.createTempFile("style", layerName + "." + type);
			FileWriter writer = new FileWriter(tmpLegend);
			writer.write(def);
			writer.close();
			LoadLegend.setLegend(layer, tmpLegend.getAbsolutePath(), true);

		}
	}

	private void loadFileLegend(FLyrVect layer) {
		String stylePath;
		if (legendDir.endsWith(File.separator)) {
			stylePath = legendDir + fileStyles.getSelectedItem().toString();
		} else {
			stylePath = legendDir + File.separator + fileStyles.getSelectedItem().toString();
		}
		LoadLegend.setLegendPath(stylePath);
		LoadLegend.setLegend(layer);
	}

	@Override
	public void finish() throws WizardException {
		Object aux = properties.get(LoadMapWizardComponent.PROPERTY_VEW);
		if (aux!=null && aux instanceof View) {
			View view = (View) aux;

			if ((databaseRB.isSelected() && dbStyles.getSelectedItem()!=null) || (fileRB.isSelected() && fileStyles.getSelectedItem()!=null)) {
				FLayers layers = view.getMapControl().getMapContext().getLayers();
				try {
					loadLegends(layers);
				} catch (Exception e) {
					throw new WizardException(e);
				}
			}
		} else {
			throw new WizardException("Couldn't retrieve the view");
		}
	}

	private void loadLegends(FLayers layers) throws SQLException, IOException {
		for (int i=0; i<layers.getLayersCount(); i++) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLyrVect) {
				if (databaseRB.isSelected()) {
					loadDBLegend((FLyrVect) layer);
				} else {
					loadFileLegend((FLyrVect) layer);
				}
			} else if (layer instanceof FLayers) {
				loadLegends((FLayers) layer);
			}
		}
	}

	@Override
	public void setProperties() {
		// Nothing to do
	}



}
