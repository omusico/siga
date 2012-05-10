package org.gvsig.mapsheets.print.series.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.mapsheets.print.audasa.AudasaPreferences;
import org.gvsig.mapsheets.print.audasa.ICancelPanel;
import org.gvsig.mapsheets.print.audasa.VariablesTemplatePanel;
import org.gvsig.mapsheets.print.series.fmap.MapSheetGrid;
import org.gvsig.mapsheets.print.series.gui.utils.LayerComboItem;
import org.gvsig.mapsheets.print.series.gui.utils.MeasureUnitComboItem;
import org.gvsig.mapsheets.print.series.gui.utils.NumericDocument;
import org.gvsig.mapsheets.print.series.utils.MapSheetsUtils;

import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.GenericFileFilter;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.xml.XMLEncodingUtils;
import com.iver.utiles.xmlEntity.generate.XmlTag;

/**
 * Dialog to create a new map sheet grid, by choosing
 * creation method, overlapping, etc.
 * 
 * 
 * @author jldominguez
 *
 */
public class MapSheetsSettingsPanel extends JPanel implements IWindow, ActionListener, KeyListener {

	private static Logger logger = Logger.getLogger(MapSheetsSettingsPanel.class.getName());
	
	public static final int WIDTH = 645;
	public static final int HEIGHT = 445 - 175;

	private static final long serialVersionUID = 1L;
	private JPanel areaPanel = null;
	
	// private JPanel layoutPanel = null;
	private JPanel scalePanel = null;
	// private JPanel metaBoxPanel = null;

	private JPanel templatesPanel;
	private JRadioButton templateA3Dimensiones;
	private JRadioButton templateA3Consultas;
	private JRadioButton templateA4Consultas;
	private JRadioButton templateA3DimensionesWithLoc;
	private JRadioButton templateA3ConsultasWithLoc;
	private JRadioButton templateA4ConsultasWithLoc;

	private JRadioButton coverView = null;
	private JRadioButton basedOnFeatures = null;
	private JComboBox vecLayerToUse = null;

	private JCheckBox selectedOnly = null;
	
	private JSpinner overlapSpinner = null;
	
	private JLabel oneFixedLabel = null;
	private JLabel scaleLabel = null;
	private JTextField scaleText = null;
	// numberField = new JFormattedTextField(NumberFormat.getInstance());

	private JButton acceptButton = null;
	private JButton cancelButton = null;
	private JLabel overlapLabel = null;
	
//	private JLabel measureUnitLabel = null;
//	private JComboBox measureUnitCombo = null;
//	private JLabel widthLabel = null;
//	private JTextField widthText = null;
//	private JLabel heightLabel = null;
//	private JTextField heightText = null;
	
	private View view = null;

	/**
	 * This is the default constructor
	 */
	public MapSheetsSettingsPanel(View v) {
		super();
		view = v;
		// mps = new MapSheetsSettings(v);
		// ViewPort vp = v.getMapControl().getViewPort().cloneViewPort();
		// vp.setImageSize(v.getMapControl().getViewPort().getImageSize());
		// vp.setExtent(v.getMapControl().getViewPort().getAdjustedExtent());
		// mps.setOriginalViewPort(vp);
		initialize();
	}
	
	public View getView() {
		return view;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(null);
		this.add(getAreaPanel(), null);
		// this.add(getLayoutPanel(), null);
		this.add(getScalePanel(), null);
		this.add(getTemplatesPanel(), null);
		// this.add(getBaseLayerPanel(), null);
		// this.add(getMetaboxPanel(), null);
		this.add(getAcceptButton(), null);
		this.add(getCancelButton(), null);
		
		this.loadLayersCombo(getVectLayerToUseCB(), true);
		// this.loadLayersCombo(getBackLayerCB(), false);
		
		if (getVectLayerToUseCB().getItemCount() == 0) {
			// getCoverViewRB().setSelected(true);
			getBasedOnFeaturesRB().setEnabled(false);
			getVectLayerToUseCB().setEnabled(false);
			getSelectedOnlyCB().setEnabled(false);
		} else {
			getBasedOnFeaturesRB().setEnabled(true);
			getVectLayerToUseCB().setEnabled(getBasedOnFeaturesRB().isSelected());
			getSelectedOnlyCB().setEnabled(getBasedOnFeaturesRB().isSelected());
		}
	}
	


	/**
	 * This method initializes areaPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAreaPanel() {
		if (areaPanel == null) {
			areaPanel = new JPanel();
			areaPanel.setLayout(null);
			areaPanel.setBounds(new Rectangle(5, 5, 631, 67));
			String bor_txt = PluginServices.getText(this, "Area_selection");
			areaPanel.setBorder(BorderFactory.createTitledBorder(null, bor_txt, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			
			areaPanel.add(getCoverViewRB(), null);
			areaPanel.add(getBasedOnFeaturesRB(), null);
			areaPanel.add(getVectLayerToUseCB(), null);
			areaPanel.add(getSelectedOnlyCB(), null);
			
			ArrayList rbb = new ArrayList();
			rbb.add(getCoverViewRB());
			rbb.add(getBasedOnFeaturesRB());
			MapSheetsUtils.joinRadioButtons(rbb);
		}
		return areaPanel;
	}

	



	/**
	 * This method initializes scalePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getScalePanel() {
		if (scalePanel == null) {
			scalePanel = new JPanel();
			scalePanel.setLayout(null);
			scalePanel.setBounds(new Rectangle(5, 135+13+40-112, 631, 68));//+120-15));
			
			String bor_txt = PluginServices.getText(this, "Choose_scale_or_grid_division");
			scalePanel.setBorder(BorderFactory.createTitledBorder(null, bor_txt, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			
			scalePanel.add(getScaleLabel(), null);
			scalePanel.add(getOneLabel(), null);
			scalePanel.add(getScaleField(), null);
			
			// scalePanel.add(getStandardScaleCB(), null);
			
			scalePanel.add(getOverlapLabel(), null);
			scalePanel.add(getOverlapSpinner(), null);
			
//			scalePanel.add(getMeasureUnitLabel(), null);
//			scalePanel.add(getMeasureUnitCombo(), null);
//			scalePanel.add(getWidthLabel(), null);
//			scalePanel.add(getWidthText(), null);
//			scalePanel.add(getHeightLabel(), null);
//			scalePanel.add(getHeightText(), null);
			
		}
		return scalePanel;
	}
	
	private JPanel getTemplatesPanel() {
	    if(templatesPanel == null) {
		templatesPanel = new JPanel();
		templatesPanel.setLayout(null);
		templatesPanel.setBounds(new Rectangle(5, 135+13+40-112+68, 631, 21*3+30));
		templatesPanel.setBorder(BorderFactory.createTitledBorder(null, 
			PluginServices.getText(this, "Template_type"),
			TitledBorder.DEFAULT_JUSTIFICATION, 
			TitledBorder.DEFAULT_POSITION, 
			new Font("Dialog", Font.BOLD, 12), 
			new Color(51, 51, 51)));
		
		templateA3Dimensiones = new JRadioButton(AudasaPreferences.A3_DIMENSIONES);
		templateA3Dimensiones.setBounds(new Rectangle(15, 21, 300-15-15, 21));
		templateA3Dimensiones.setSelected(true);
		templateA3Dimensiones.addActionListener(this);
		templateA3Consultas = new JRadioButton(AudasaPreferences.A3_CONSULTAS);
		templateA3Consultas.setBounds(new Rectangle(15, 21+21, 300-15-15, 21));
		templateA3Consultas.setSelected(false);
		templateA3Consultas.addActionListener(this);
		templateA4Consultas = new JRadioButton(AudasaPreferences.A4_CONSULTAS);
		templateA4Consultas.setBounds(new Rectangle(15, 21+21+21, 300-15-15, 21));
		templateA4Consultas.setSelected(false);
		templateA4Consultas.addActionListener(this);
		templateA3DimensionesWithLoc = new JRadioButton(AudasaPreferences.A3_DIMENSIONES_LOCALIZADOR);
		templateA3DimensionesWithLoc.setBounds(new Rectangle(15+275, 21, 315-15-15, 21));
		templateA3DimensionesWithLoc.setSelected(false);
		templateA3DimensionesWithLoc.addActionListener(this);
		templateA3ConsultasWithLoc = new JRadioButton(AudasaPreferences.A3_CONSULTAS_LOCALIZADOR);
		templateA3ConsultasWithLoc.setBounds(new Rectangle(15+275, 21+21, 315-15-15, 21));
		templateA3ConsultasWithLoc.setSelected(false);
		templateA3ConsultasWithLoc.addActionListener(this);
		templateA4ConsultasWithLoc = new JRadioButton(AudasaPreferences.A4_CONSULTAS_LOCALIZADOR);
		templateA4ConsultasWithLoc.setBounds(new Rectangle(15+275, 21+21+21, 315-15-15, 21));
		templateA4ConsultasWithLoc.setSelected(false);
		templateA4ConsultasWithLoc.addActionListener(this);

		templatesPanel.add(templateA3Dimensiones, null);
		templatesPanel.add(templateA3Consultas, null);
		templatesPanel.add(templateA4Consultas, null);
		templatesPanel.add(templateA3DimensionesWithLoc, null);
		templatesPanel.add(templateA3ConsultasWithLoc, null);
		templatesPanel.add(templateA4ConsultasWithLoc, null);

		ArrayList<JRadioButton> group = new ArrayList<JRadioButton>();
		group.add(templateA3Dimensiones);
		group.add(templateA3Consultas);
		group.add(templateA4Consultas);
		group.add(templateA3DimensionesWithLoc);
		group.add(templateA3ConsultasWithLoc);
		group.add(templateA4ConsultasWithLoc);
		MapSheetsUtils.joinRadioButtons(group);
	    }
	    return templatesPanel;
	}

//	private JTextField getHeightText() {
//		if (heightText == null) {
//			heightText = new JTextField();
//			heightText.setDocument(new NumericDocument());
//			heightText.setBounds(170,28+120-21,130,21);
//		}
//		return heightText;
//	}

//	private JLabel getHeightLabel() {
//		if (heightLabel == null) {
//			heightLabel = new JLabel();
//			heightLabel.setBounds(25,28+120-21,140,21);
//			heightLabel.setText(PluginServices.getText(this, "Height_"));
//		}
//		return heightLabel;
//		
//	}

//	private JTextField getWidthText() {
//		if (widthText == null) {
//			widthText = new JTextField();
//			widthText.setDocument(new NumericDocument());
//			widthText.setBounds(170,28+40+40-14,130,21);
//		}
//		return widthText;
//	}

//	private JLabel getWidthLabel() {
//		if (widthLabel == null) {
//			widthLabel = new JLabel();
//			widthLabel.setBounds(25,28+40+40-14,140,21);
//			widthLabel.setText(PluginServices.getText(this, "Width"));
//		}
//		return widthLabel;
//	}

	
//	private JComboBox getMeasureUnitCombo() {
//		if (measureUnitCombo == null) {
//			measureUnitCombo = new JComboBox();
//			measureUnitCombo.addItem(MeasureUnitComboItem.MEASURE_UNIT_CM);
//			measureUnitCombo.addItem(MeasureUnitComboItem.MEASURE_UNIT_MM);
//			measureUnitCombo.setBounds(170,28+40-7,130,21);
//			// measureUnitLabel.setBounds(25,28+40,140,21);
//			// ngle(25, 28, 100, 21));
//			// le(170, 28, 130, 21));
//		}
//		return measureUnitCombo;
//	}
	

//	private JLabel getMeasureUnitLabel() {
//		if (measureUnitLabel == null) {
//			measureUnitLabel = new JLabel();
//			measureUnitLabel.setBounds(25,28+40-7,140,21);
//			// ngle(25, 28, 100, 21));
//			// le(170, 28, 130, 21));
//			measureUnitLabel.setText(PluginServices.getText(this, "Measure_unit"));
//		}
//		return measureUnitLabel;
//	}


	private JLabel getScaleLabel() {
		if (scaleLabel == null) {
			scaleLabel = new JLabel();
			scaleLabel.setBounds(new Rectangle(25, 28, 100, 21));
			scaleLabel.setText(PluginServices.getText(this, "Scale"));
		}
		return scaleLabel;
		
	}

	private JLabel getOneLabel() {
		if (oneFixedLabel == null) {
			oneFixedLabel = new JLabel();
			oneFixedLabel.setBounds(new Rectangle(135, 28, 35, 21));
			oneFixedLabel.setText("1 :");
		}
		return oneFixedLabel;
		
	}
	
	private JTextField getScaleField() {
		if (scaleText == null) {
			// scaleText = new JFormattedTextField(NumberFormat.getInstance());
			scaleText = new JTextField();
			scaleText.setDocument(new NumericDocument());
			
			// scaleText.getDocument().
			// scaleText = new JTextField();
			scaleText.setBounds(new Rectangle(170, 28, 130, 21));
			scaleText.setText("1000");
		}
		return scaleText;
		
	}

	/**
	 * This method initializes userRectRB	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCoverViewRB() {
		if (coverView == null) {
			coverView = new JRadioButton();
			coverView.setText(PluginServices.getText(this, "Cover_view"));
			coverView.setBounds(new Rectangle(20, 27, 150, 21));
			coverView.addActionListener(this);
		}
		return coverView;
	}

	/**
	 * This method initializes viewRectRB	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getBasedOnFeaturesRB() {
		if (basedOnFeatures == null) {
			basedOnFeatures = new JRadioButton();
			basedOnFeatures.setText(PluginServices.getText(this, "Based_on_features"));
			basedOnFeatures.setBounds(new Rectangle(180, 27, 140, 21));
			basedOnFeatures.addActionListener(this);
		}
		return basedOnFeatures;
	}

	/**
	 * This method initializes horSpinner	
	 * 	
	 * @return javax.swing.JSpinner	
	 */
	private JSpinner getOverlapSpinner() {
		if (overlapSpinner == null) {
			overlapSpinner = new JSpinner();
			JSpinner.NumberEditor editor = (JSpinner.NumberEditor) overlapSpinner.getEditor();
			editor.getTextField().setEditable(false);
			editor.getModel().setValue(0);
			editor.getModel().setMinimum(0);
			editor.getModel().setMaximum(50);
			overlapSpinner.setBounds(new Rectangle(430+75, 28, 45, 21));
		}
		return overlapSpinner;
	}



	/**
	 * This method initializes acceptButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAcceptButton() {
		if (acceptButton == null) {
			acceptButton = new JButton(PluginServices.getText(null, "Aceptar"));
			acceptButton.setBounds(new Rectangle(205, 440-175, 111, 26));
			acceptButton.setMnemonic(KeyEvent.VK_A);
			acceptButton.addActionListener(this);
		}
		return acceptButton;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton(PluginServices.getText(null, "Cancel"));
			cancelButton.setBounds(new Rectangle(331, 440-175, 111, 26));
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.addActionListener(this);
		}
		return cancelButton;
	}

	private WindowInfo winfo = null;

	private File layoutFile = null;

	private boolean hasCancelled = true;

	private String selectedTemplate = AudasaPreferences.A3_DIMENSIONES;


	public WindowInfo getWindowInfo() {
		
		if (winfo == null) {
			winfo = new WindowInfo(WindowInfo.MODALDIALOG);
			winfo.setTitle(AudasaPreferences.TITULO_VENTANA);
			winfo.setHeight(HEIGHT);
			winfo.setWidth(WIDTH);
		}
		return winfo;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
	
	private JLabel getOverlapLabel() {
		if (overlapLabel == null) {
			overlapLabel = new JLabel();
			overlapLabel.setText(PluginServices.getText(this, "Overlap_or_clearance"));
			overlapLabel.setBounds(new Rectangle(270+75, 28, 150, 21));
		}
		return overlapLabel;
	}

	public void actionPerformed(ActionEvent e) {
		
		Object src = e.getSource();
		if (src == this.getAcceptButton()) {
			hasCancelled = false;
			try {
			    	double grid_width;
			    	double grid_height;
			    	if(selectedTemplate.equals(AudasaPreferences.A4_CONSULTAS) || 
			    		selectedTemplate.equals(AudasaPreferences.A4_CONSULTAS_LOCALIZADOR)) {
			    	    grid_width = AudasaPreferences.VIEW_WIDTH_A4;
			    	    grid_height = AudasaPreferences.VIEW_HEIGHT_A4;
			    	} else { // any A3 template
			    	    grid_width = AudasaPreferences.VIEW_WIDTH_A3;
			    	    grid_height = AudasaPreferences.VIEW_HEIGHT_A3;
			    	}
				//Long.parseLong(this.getWidthText().getText());
				//Long.parseLong(this.getHeightText().getText());
				double w = 1.0 * grid_width;
				double h = 1.0 * grid_height; 
//				MeasureUnitComboItem muci = (MeasureUnitComboItem) getMeasureUnitCombo().getSelectedItem();
				MeasureUnitComboItem muci = MeasureUnitComboItem.MEASURE_UNIT_CM;
				w = 100 * muci.getMetersPerUnit() * w;
				h = 100 * muci.getMetersPerUnit() * h;
				
				Rectangle2D use_map_r = new Rectangle2D.Double(0,0,w,h);
				long s = Long.parseLong(this.getScaleField().getText());
				int opc = Integer.parseInt(this.getOverlapSpinner().getValue().toString());
				
				FLyrVect lyrv = null;
				LayerComboItem lci = (LayerComboItem) getVectLayerToUseCB().getSelectedItem();
				if (lci != null) {
					lyrv = (FLyrVect) lci.getLayer();
				}
				
				ArrayList[] igs_codes = MapSheetsUtils.createFrames(
						this.coverView.isSelected(),
						this.selectedOnly.isSelected(),
						use_map_r,
						getView().getMapControl().getViewPort(),
						s, opc, getView().getProjection(), lyrv);

				ArrayList igs = igs_codes[0];
				ArrayList cods = igs_codes[1];
				HashMap atts_hm = null;
				
				MapSheetGrid newgrid = MapSheetGrid.createMapSheetGrid(
						MapSheetGrid.createNewName(),
						getView().getProjection(),
						MapSheetGrid.createDefaultLyrDesc());

				
				int sz = igs.size();
				for (int i=0; i<sz; i++) {
					atts_hm = new HashMap();
					
					atts_hm.put(MapSheetGrid.ATT_NAME_CODE,
							ValueFactory.createValue((String) cods.get(i)));
					atts_hm.put(MapSheetGrid.ATT_NAME_ROT_RAD,
							ValueFactory.createValue(new Double(0)));
					atts_hm.put(MapSheetGrid.ATT_NAME_OVERLAP,
							ValueFactory.createValue(new Double(opc)));
					atts_hm.put(MapSheetGrid.ATT_NAME_SCALE,
							ValueFactory.createValue(new Double(s)));
					atts_hm.put(MapSheetGrid.ATT_NAME_DIMX_CM,
							ValueFactory.createValue(new Double(w)));
					atts_hm.put(MapSheetGrid.ATT_NAME_DIMY_CM,
							ValueFactory.createValue(new Double(h)));
					
					newgrid.addSheet(
							(IGeometry) igs.get(i),
							atts_hm);
				}
				
				//before adding the new layer, delete all MapSheetGrids in TOC
				FLayers layersInTOC = getView().getMapControl().getMapContext().getLayers();
				for (int i=0; i<layersInTOC.getLayersCount(); i++) {
				    if (layersInTOC.getLayer(i) instanceof MapSheetGrid) {
					getView().getMapControl().getMapContext().getLayers().removeLayer(layersInTOC.getLayer(i));
				    }
				}
				getView().getMapControl().getMapContext().getLayers().addLayer(newgrid);
				MapSheetsUtils.setOnlyActive(
						newgrid,
						getView().getMapControl().getMapContext().getLayers());
				
				// getView().getMapControl().getMapContext().
				PluginServices.getMDIManager().closeWindow(this);

			} catch (Exception exc) {
				JOptionPane.showMessageDialog(
						this,
						exc.getMessage(),
						PluginServices.getText(this, "Error"),
						JOptionPane.ERROR_MESSAGE);
				// NotificationManager.addError("While creating maps: ", exc);
			}
			return;
		}
		
		if (src == this.getCancelButton()) {
		    	hasCancelled = true;
			PluginServices.getMDIManager().closeWindow(this);
			return;
		}

		
		if (src == getCoverViewRB() || src == getBasedOnFeaturesRB()) {
			getVectLayerToUseCB().setEnabled(getBasedOnFeaturesRB().isSelected());
			getSelectedOnlyCB().setEnabled(getBasedOnFeaturesRB().isSelected());
		}

		if((src == templateA3Dimensiones) || 
			(src == templateA3Consultas) || 
			(src == templateA4Consultas) ||
			(src == templateA3DimensionesWithLoc) ||
			(src == templateA3ConsultasWithLoc) ||
			(src == templateA4ConsultasWithLoc)) {

		    selectedTemplate = ((JRadioButton) src).getText();
		}
		
	}

	public boolean hasCancelled() {
	    return hasCancelled;
	}

	public Layout getMapLayout() {
	    return getLayoutFromFile(AudasaPreferences.getSelectedFile(selectedTemplate));
	}

	public Layout getLayoutFromFile(File layoutFile) {
	    Project project = ((ProjectExtension) PluginServices.getExtension(ProjectExtension.class)).getProject();
	    Layout layout=null;
	    
	    if (!(layoutFile.getPath().endsWith(".gvt") || layoutFile.getPath().endsWith(".GVT"))){
		layoutFile = new File(layoutFile.getPath()+".gvt");
	    }
	    try {
		File xmlFile = new File(layoutFile.getAbsolutePath());
		FileInputStream is = new FileInputStream(xmlFile);
		Reader reader = XMLEncodingUtils.getReader(is);

		XmlTag tag = (XmlTag) XmlTag.unmarshal(reader);
		try {
		    XMLEntity xml=new XMLEntity(tag);
		    if (xml.contains("followHeaderEncoding")) {
			layout = Layout.createLayout(xml,project);
		    }
		    else {
			reader = new FileReader(xmlFile);
			tag = (XmlTag) XmlTag.unmarshal(reader);
			xml=new XMLEntity(tag);
			layout = Layout.createLayout(xml,project);
		    }

		    return layout;
		} catch (OpenException e) {
		    e.showError();
		    return null;
		}
		//fPanelLegendManager.setRenderer(LegendFactory.createFromXML(new XMLEntity(tag)));
	    } catch (FileNotFoundException e) {
		NotificationManager.addError(PluginServices.getText(this, "Al_leer_la_leyenda"), e);
		return null;
	    } catch (MarshalException e) {
		NotificationManager.addError(PluginServices.getText(this, "Al_leer_la_leyenda"), e);
		return null;
	    } catch (ValidationException e) {
		NotificationManager.addError(PluginServices.getText(this, "Al_leer_la_leyenda"), e);
		return null;
	    }
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public JComboBox getVectLayerToUseCB() {
		if (vecLayerToUse == null) {
			vecLayerToUse = new JComboBox();
			vecLayerToUse.setBounds(new Rectangle(330, 27, 150, 21));
			vecLayerToUse.setEnabled(false);
			// 
		}
		return vecLayerToUse;
	}
	
	
	private void loadLayersCombo(JComboBox cmbo,
			boolean vector_only) {
		
		cmbo.removeAllItems();
		
		IProjectView pv = getView().getModel();
		FLayers lyrs = pv.getMapContext().getLayers();
		int[] types = { FShape.POLYGON, FShape.LINE };
		ArrayList list = null;
		
		try {
			list = MapSheetsUtils.getLayers(lyrs, types, vector_only);
		} catch (Exception ex) {
			NotificationManager.addError("While getting vect layers.", ex);
		}

		LayerComboItem item = null;
		for (int i=0; i<list.size(); i++) {
			
			try {
				item = new LayerComboItem((FLayer) list.get(i));
				cmbo.addItem(item);
			} catch (Exception ex) {
				NotificationManager.addError("While adding layer to vector layer list.", ex);
			}
		}
		
		
	}
	
	public JCheckBox getSelectedOnlyCB() {
		if (selectedOnly == null) {
			selectedOnly = new JCheckBox();
			selectedOnly.setText(PluginServices.getText(this, "Selected_only"));
			selectedOnly.setBounds(new Rectangle(490, 27, 135, 21));
			selectedOnly.setEnabled(false);
		}
		return selectedOnly;
	}

	public String getSelectedTemplate() {
	    return selectedTemplate;
	}



}  //  @jve:decl-index=0:visual-constraint="0,-2"