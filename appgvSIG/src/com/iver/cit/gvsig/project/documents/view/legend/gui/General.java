/*
 * Created on 31-ene-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.project.documents.view.legend.gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JBlank;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.DriverIOException;
import com.iver.cit.gvsig.fmap.edition.VectorialEditableAdapter;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.VectorialAdapter;
import com.iver.cit.gvsig.fmap.layers.VectorialDBAdapter;
import com.iver.cit.gvsig.fmap.layers.VectorialFileAdapter;
import com.iver.cit.gvsig.project.documents.view.legend.CreateSpatialIndexMonitorableTask;
import com.iver.utiles.swing.threads.IMonitorableTask;



/**
 * This class implements an useful and intuitive graphic interface to change some
 * properties of the layer. This class extends AbstractThemeManager. The properties
 * that allow modified are the name of the layer, the scale, if the user want to use
 * Spatial index or not and the HyperLink. Also shows a scroll with a little resume
 * with the properties of the layer.
 * @author jmorell
 *
 */
public class General extends AbstractThemeManagerPage {

	private static final long serialVersionUID = 1L;
    private FLayer layer;
    private NumberFormat nf = NumberFormat.getInstance();
	private JPanel pnlLayerName = null;
	private GridBagLayoutPanel pnlScale = null;
	private JPanel pnlProperties = null;
	private JLabel lblLayerName = null;
	private JTextField txtLayerName = null;
	private JTextField txtMaxScale = null;
	private JTextArea propertiesTextArea = null;
	private JRadioButton rdBtnShowAlways = null;
	private JRadioButton rdBtnDoNotShow = null;
	private JTextField txtMinScale = null;
    private JCheckBox jCheckBoxSpatialIndex = null;
    private JScrollPane scrlProperties;


	public General() {
		super();
		initialize();
	}

	private  void initialize() {
		this.setLayout(new BorderLayout());
		GridBagLayoutPanel aux = new GridBagLayoutPanel();
		aux.addComponent(getPnlLayerName());
		aux.addComponent(new JBlank(10, 10));
		aux.addComponent(getJCheckBoxSpatialIndex());
		aux.addComponent("", getPnlScale());
		aux.addComponent("", getPnlProperties());
		aux.setPreferredSize(getPreferredSize());
		this.add(aux, BorderLayout.CENTER);
		this.add(new JBlank(5, 10), BorderLayout.WEST);
		this.add(new JBlank(5, 10), BorderLayout.EAST);
	}


	/**
	 * Sets the necessary properties in the panel. This properties are
	 * extracted from the layer. With this properties fills the TextFields,
	 * ComboBoxes and the rest of GUI components.
	 * @param FLayer layer,
	 */
	public void setModel(FLayer layer) {
		this.layer = layer;

        if (layer instanceof FLyrVect) {
            FLyrVect lyrVect = (FLyrVect) layer;


            if(lyrVect.getISpatialIndex() == null) {
                getJCheckBoxSpatialIndex().setSelected(false);
            } else {
                getJCheckBoxSpatialIndex().setSelected(true);
            }
        }
		if (layer.getMinScale() != -1) {
		    getTxtMaxScale().setText(nf.format(layer.getMinScale()));
		}
		if (layer.getMaxScale() != -1) {
		    getTxtMinScale().setText(nf.format(layer.getMaxScale()));
		}
		if (layer.getMinScale() == -1 && layer.getMaxScale() == -1) {
			getRdBtnShowAlways().setSelected(true);
			txtMaxScale.setEnabled(false);
			txtMinScale.setEnabled(false);

		} else {
			getRdBtnDoNotShowWhen().setSelected(true);
			txtMaxScale.setEnabled(true);
			txtMinScale.setEnabled(true);
		}
		txtLayerName.setText(layer.getName());
		showLayerInfo();
    }


	private JPanel getPnlLayerName() {
		if (pnlLayerName == null) {
			lblLayerName = new JLabel();
			pnlLayerName = new JPanel();
			lblLayerName.setText(PluginServices.getText(this,"Nombre") + ":");
			lblLayerName.setComponentOrientation(ComponentOrientation.UNKNOWN);
			pnlLayerName.setComponentOrientation(ComponentOrientation.UNKNOWN);
			pnlLayerName.add(lblLayerName, null);
			pnlLayerName.add(getTxtLayerName(), null);
		}
		return pnlLayerName;
	}

	private GridBagLayoutPanel getPnlScale() {
		if (pnlScale == null) {
			pnlScale = new GridBagLayoutPanel();
			pnlScale.setBorder(BorderFactory.createTitledBorder(
    				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					PluginServices.getText(this, "rango_de_escalas"),
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null)
				);
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(getRdBtnShowAlways());
			buttonGroup.add(getRdBtnDoNotShowWhen());
			pnlScale.addComponent(getRdBtnShowAlways());
			pnlScale.addComponent(getRdBtnDoNotShowWhen());
			JPanel aux;


			aux = new JPanel(new FlowLayout(FlowLayout.LEFT));
			aux.add(getTxtMaxScale());
			aux.add(new JLabel("(" + PluginServices.getText(this,"escala_maxima") + ")"));

			GridBagLayoutPanel aux2;
			aux2 = new GridBagLayoutPanel();
			aux2.addComponent(PluginServices.getText(
					this,"este_por_encima_de")+" 1:",
					aux);
			aux = new JPanel(new FlowLayout(FlowLayout.LEFT));
			aux.add(getTxtMinScale());
			aux.add(new JLabel("(" + PluginServices.getText(this,"escala_minima") + ")"));

			aux2.addComponent(PluginServices.getText(
					this,"este_por_debajo_de_")+" 1:",
					aux);

			pnlScale.addComponent(new JBlank(20, 1), aux2);

			pnlScale.addComponent(new JBlank(20, 1), aux2);

		}
		return pnlScale;
	}

    /**
     * This method initializes pnlProperties, the jpanel which contains the
     * ScrollPane with the properties.
     */
	private JPanel getPnlProperties() {
		if (pnlProperties == null) {
			pnlProperties = new JPanel(new GridBagLayout());
			pnlProperties.setBorder(BorderFactory.createTitledBorder(
    				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					PluginServices.getText(this, "propiedades"),
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null)
				);
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 5, 5);
	    constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			pnlProperties.add(getScrlProperties(), constraints);
		}
		return pnlProperties;
	}

	private JTextField getTxtLayerName() {
		if (txtLayerName == null) {
			txtLayerName = new JTextField(25);
			txtLayerName.setEditable(true);
		}
		return txtLayerName;
	}

	private JTextField getTxtMaxScale() {
		if (txtMaxScale == null) {
			txtMaxScale = new JTextField(15);
			txtMaxScale.setEnabled(false);
		}
		return txtMaxScale;
	}

    /**
     * This method initializes propertiesTextArea, where are display the
     * properties of the layer
     */
	private JTextArea getPropertiesTextArea() {
		if (propertiesTextArea == null) {
			propertiesTextArea = new JTextArea();
			propertiesTextArea.setEditable(false);
			propertiesTextArea.setBackground(SystemColor.control);

		}
		return propertiesTextArea;
	}

	private JScrollPane getScrlProperties() {
		if (scrlProperties == null) {
			scrlProperties = new JScrollPane();
			scrlProperties.setViewportView(getPropertiesTextArea());
			scrlProperties.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			scrlProperties.setPreferredSize(new Dimension(350, 180));
		}
		return scrlProperties;
	}

	private JRadioButton getRdBtnShowAlways() {
		if (rdBtnShowAlways == null) {
			rdBtnShowAlways = new JRadioButton();
			rdBtnShowAlways.setText(PluginServices.getText(this,"Mostrar_siempre"));
			rdBtnShowAlways.setSelected(true);
			rdBtnShowAlways.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtMaxScale.setEnabled(false);
					txtMinScale.setEnabled(false);
				}
			});
		}
		return rdBtnShowAlways;
	}

	private JRadioButton getRdBtnDoNotShowWhen() {
		if (rdBtnDoNotShow == null) {
			rdBtnDoNotShow = new JRadioButton();
			rdBtnDoNotShow.setText(PluginServices.getText(this,"No_mostrar_la_capa_cuando_la_escala"));
			rdBtnDoNotShow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtMaxScale.setEnabled(true);
					txtMinScale.setEnabled(true);
				}
			});
		}
		return rdBtnDoNotShow;
	}

	private JTextField getTxtMinScale() {
		if (txtMinScale == null) {
			txtMinScale = new JTextField(15);
			txtMinScale.setEnabled(false);
		}
		return txtMinScale;
	}

	private String getLayerName(){
		return txtLayerName.getText().toString();
	}


    private JCheckBox getJCheckBoxSpatialIndex() {
    	if (jCheckBoxSpatialIndex == null) {
    		jCheckBoxSpatialIndex = new JCheckBox();
    		jCheckBoxSpatialIndex.setBounds(2, 33, 242, 23);
    		jCheckBoxSpatialIndex.setText(PluginServices.getText(this,"Usar_indice_espacial"));
    	}
    	return jCheckBoxSpatialIndex;
    }


    private boolean isSpatialIndexSelected() {
        return getJCheckBoxSpatialIndex().isSelected();
    }

    /**
     * Add the information of the layer to the textArea
     */
    private void showLayerInfo() {
	try {
	    String info = ((FLyrDefault) layer).getInfoString();
	    if (info == null) {
		Rectangle2D layerExtentOnView = layer.getFullExtent();
		IProjection viewProj = layer.getMapContext().getProjection();
		info = getLayerInfoExtent(layerExtentOnView, viewProj,
			"view_projection");

		// show layer native projection
		if (!layer.getProjection().getAbrev()
			.equals(viewProj.getAbrev())) {
		    IProjection layerNativeProj = layer.getProjection();
		    ICoordTrans ct = viewProj.getCT(layerNativeProj);
		    Rectangle2D layerNativeExtent = ct
			    .convert(layerExtentOnView);
		    info += getLayerInfoExtent(layerNativeExtent,
			    layerNativeProj, "layer_native_projection");
		}
		if (layer instanceof FLyrVect) {
		    info += getLayerVectorialInfo();
		} else {
		    info = info
			    + PluginServices.getText(this, "Origen_de_datos")
			    + ": " + layer.getName();
		}
	    }
	    getPropertiesTextArea().setText(info);
	} catch (ReadDriverException e) {
	    NotificationManager.addError(e.getMessage(), e);
	}
    }

    /**
     * Returns true or false if the scale is selected
     */
	private boolean isScaleActive() {
		return getRdBtnDoNotShowWhen().isSelected();
	}

    /**
     * Instantiates a ITask (@see com.iver.utiles.swing.threads.ITask) to create
     * a spatial index for the selected layer in background. This task also
     * allow to monitor process evolution, and to cancel this process.
     *
     * @throws DriverException
     * @throws DriverIOException
     */
    private IMonitorableTask getCreateSpatialIndexTask() throws ReadDriverException {
    	// FIXME REVISAR ESTO (Quizas lanzar TaskException)
    	return new CreateSpatialIndexMonitorableTask((FLyrVect)layer);
    }

	public void acceptAction() {
	// does nothing
	}

	public void cancelAction() {
		// does nothing
	}

	    /**
     * When we press the apply button, sets the new properties of the layer that
     * the user modified using the UI components
     */
	public void applyAction() {
		if (isScaleActive()) {
			try	{
				layer.setMinScale((nf.parse(getTxtMaxScale().getText())).doubleValue());
			} catch (ParseException ex)	{
			    if (getTxtMaxScale().getText().compareTo("") == 0) {
				layer.setMinScale(-1);
			    } else {
				System.err.print(ex.getLocalizedMessage());
			    }
			}

			try	{
			    layer.setMaxScale((nf.parse(getTxtMinScale().getText())).doubleValue());
			} catch (ParseException ex)	{
			    if (getTxtMinScale().getText().compareTo("") == 0) {
				layer.setMaxScale(-1);
			    } else {
				System.err.print(ex.getLocalizedMessage());
			    }
			}

		} else {
	        layer.setMinScale(-1);
	        layer.setMaxScale(-1);
		}

		if (!getLayerName().equals(layer.getName())){
			layer.setName(getLayerName());
		}

        if (layer instanceof FLyrVect){
            FLyrVect lyrVect = (FLyrVect) layer;
            if (isSpatialIndexSelected()) {
            	if(lyrVect.getISpatialIndex() == null) {
                	//AZABALA
                	try {
						PluginServices.
							cancelableBackgroundExecution(getCreateSpatialIndexTask());
					} catch (ReadDriverException e) {
						NotificationManager.addError(e.getMessage(), e);
					}
                }
            }
            //AZABALA
            /*
             * If we unselect the spatial index checkbox...Must we delete
             * spatial index file, or we only have to put Spatial Index
             * reference to null?. I have followed the second choice
             */
            else{
                lyrVect.deleteSpatialIndex();
            }


        }

	}


	public String getName() {
		return PluginServices.getText(this,"General");
	}

    private String getLayerVectorialInfo() throws ReadDriverException {
	final FLyrVect lv = (FLyrVect) layer;
	ReadableVectorial rv = lv.getSource();
	if (rv instanceof VectorialEditableAdapter) {
	    rv = ((VectorialEditableAdapter) lv.getSource())
		    .getOriginalAdapter();
	}

	String info = PluginServices.getText(this, "Origen_de_datos") + ": ";

	if (rv instanceof VectorialAdapter) {
	    info += "\n" + rv.getDriver().getName() + "\n";

	    if (rv instanceof VectorialFileAdapter) {
		info += PluginServices.getText(this, "fichero") + ": "
			+ ((VectorialFileAdapter) rv).getFile();
	    } else if (rv instanceof VectorialDBAdapter) {
		DBLayerDefinition dbdef = ((VectorialDBAdapter) rv).getLyrDef();
		try {
		    info += PluginServices.getText(this, "url") + ": "
			    + dbdef.getConnection().getURL() + "\n";
		} catch (Exception e) {
		    e.printStackTrace();
		}

		info += PluginServices.getText(this, "Tabla") + ": "
			+ dbdef.getComposedTableName() + "\n";

		info += PluginServices.getText(this, "sql_restriction") + ": "
			+ ((VectorialDBAdapter) rv).getWhereClause() + "\n";
	    }
	}

	info += "\n" + PluginServices.getText(this, "type") + ": "
		+ lv.getTypeStringVectorLayer() + "\n";
	return info;
    }

    private String getLayerInfoExtent(Rectangle2D extent, IProjection proj,
	    String extentName) {
	String info;
	info = PluginServices.getText(this, "Extent") + " " + proj.getAbrev()
		+ " (" + PluginServices.getText(this, extentName) + "):\n\t"
		+ PluginServices.getText(this, "Superior") + ":\t"
		+ extent.getMaxY() + "\n\t"
		+ PluginServices.getText(this, "Inferior") + ":\t"
		+ extent.getMinY() + "\n\t"
		+ PluginServices.getText(this, "Izquierda") + ":\t"
		+ extent.getMinX() + "\n\t"
		+ PluginServices.getText(this, "Derecha") + ":\t"
		+ extent.getMaxX() + "\n";
	return info;
    }
}