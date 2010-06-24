package es.udc.cartolab.gvsig.elle.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.exolab.castor.xml.Marshaller;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.layerOperations.ClassifiableVectorial;
import com.iver.cit.gvsig.fmap.rendering.IVectorLegend;
import com.iver.utiles.XMLEntity;
import com.jeta.forms.components.panel.FormPanel;


public class SaveAllLegendsDialog extends JPanel implements IWindow, ActionListener {

	FLayer[] layers;
	FLayer[] overviewLayers = null;
	private JPanel centerPanel = null;
	private JPanel southPanel = null;
	private JTextField legendsField;
	private JButton okButton;
	private JButton cancelButton;
	private WindowInfo viewInfo = null;

	public WindowInfo getWindowInfo() {

		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
			viewInfo.setTitle(PluginServices.getText(this, "save_legends"));
			viewInfo.setWidth(425);
			viewInfo.setHeight(75);
		}
		return viewInfo;
	}

	private void init() {

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

	public SaveAllLegendsDialog(FLayer[] layers) {
		this.layers = layers;
		init();
	}

	public SaveAllLegendsDialog(FLayer[] layers, FLayer[] overviewLayers) {
		this.layers = layers;
		this.overviewLayers = overviewLayers;
		init();
	}

	protected JPanel getCenterPanel() {

		if (centerPanel == null) {
			centerPanel = new JPanel();
			FormPanel form = new FormPanel("forms/saveLegends.jfrm");
			form.setFocusTraversalPolicyProvider(true);
			centerPanel.add(form);

			JLabel legendsLabel = form.getLabel("legendsLabel");
			legendsLabel.setText(PluginServices.getText(this, "legends_group_name"));

			legendsField = form.getTextField("legendsField");
			legendsField.addActionListener(this);

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
		if ((event.getSource() == okButton) || (event.getSource()==legendsField)) {
			PluginServices ps = PluginServices.getPluginServices("es.udc.cartolab.gvsig.elle");
			XMLEntity xml = ps.getPersistentXML();
			if (xml.contains(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME)) {
				String path = xml.getStringProperty(EllePreferencesPage.DEFAULT_LEGEND_DIR_KEY_NAME);
				if (path.endsWith(File.separator)) {
					path = path + legendsField.getText() + File.separator;
				} else {
					path = path + File.separator + legendsField.getText() + File.separator;
				}
				File f = new File(path);
				boolean cont = true;
				boolean error = false;
				if (!f.exists()) {
					cont = f.mkdir();
					error = !cont;
				} else {
					if (f.isDirectory()) {
						//overwrite?
						Object[] options = {PluginServices.getText(this, "ok"),
								PluginServices.getText(this, "cancel")};
						String message = PluginServices.getText(this, "overwrite_legend_question");
						int n = JOptionPane.showOptionDialog(this,
								String.format(message, legendsField.getText()),
								PluginServices.getText(this, "overwrite_legend"),
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.WARNING_MESSAGE,
								null,
								options,
								options[1]);
						if (n!=0) {
							cont = false;
						}
					} else {
						//error no es directorio
						JOptionPane.showMessageDialog(this,
								PluginServices.getText(this, "legend_exist_file_error"),
								PluginServices.getText(this, "saving_legend_error"),
								JOptionPane.ERROR_MESSAGE);
						cont = false;
					}
				}
				if (cont) {
					if (f.canWrite()) {
						if (layers!=null) {
							for (int i=0; i<layers.length; i++) {
								if (layers[i] instanceof FLayers) {
									FLayers lyrs = (FLayers) layers[i];
									for (int j=0; j<lyrs.getLayersCount(); j++) {
										String legendFilePath = path + lyrs.getLayer(j).getName().toLowerCase() + ".gvl";
										File legendFile = new File(legendFilePath);
										writeLegend(lyrs.getLayer(j), legendFile);
									}
								} else {
									String legendFilePath = path + layers[i].getName().toLowerCase() + ".gvl";
									File legendFile = new File(legendFilePath);
									writeLegend(layers[i], legendFile);
								}
							}
						}
						/* overview */
						if (overviewLayers != null) {
							File overviewDir = new File(f.getAbsolutePath() + File.separator + "overview");
							if (!overviewDir.exists()) {
								overviewDir.mkdir();
							}
							if (overviewDir.isDirectory()) {
								for (int i=0; i<overviewLayers.length; i++) {
									String legendFilePath = overviewDir.getAbsolutePath() + File.separator + overviewLayers[i].getName().toLowerCase() + ".gvl";
									File legendFile = new File(legendFilePath);
									writeLegend(overviewLayers[i], legendFile);
								}
							} else {
								String msg = PluginServices.getText(this, "legend_overview_error");
								JOptionPane.showMessageDialog(this,
										String.format(msg, overviewDir.getAbsolutePath()),
										PluginServices.getText(this, "saving_legend_error"),
										JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						//error no se puede escribir
						String message = PluginServices.getText(this, "legend_write_dir_error");
						JOptionPane.showMessageDialog(this,
								String.format(message, path),
								PluginServices.getText(this, "saving_legend_error"),
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					if (error) {
						//error al crear directorio
						String message = PluginServices.getText(this, "legend_create_dir_error");
						JOptionPane.showMessageDialog(this,
								String.format(message, path),
								PluginServices.getText(this, "saving_legend_error"),
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			PluginServices.getMDIManager().closeWindow(this);
		}
		if (event.getSource() == cancelButton) {
			PluginServices.getMDIManager().closeWindow(this);
		}
	}


	private void writeLegend(FLayer layer, File file) {
		if (layer instanceof ClassifiableVectorial) {
			ClassifiableVectorial aux = (ClassifiableVectorial) layer;
			try {
				IVectorLegend renderer = (IVectorLegend) aux.getLegend().cloneLegend();
				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
				OutputStreamWriter writer = new OutputStreamWriter(fos, ProjectExtension.PROJECTENCODING);
				Marshaller m = new Marshaller(writer);
				m.setEncoding(ProjectExtension.PROJECTENCODING);
				XMLEntity xml = renderer.getXMLEntity();
				xml.putProperty("followHeaderEncoding", true);
				m.marshal(xml.getXmlTag());
			} catch (Exception e) {
				NotificationManager.addError(PluginServices.getText(this, "Error_guardando_la_leyenda"), e);
			}
		}
	}

	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return null;
	}
}
