/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ib��ez, 50
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

/* CVS MESSAGES:
 *
 * $Id: DotDensity.java 15647 2007-10-30 12:03:52Z jmvivo $
 * $Log$
 * Revision 1.11  2007-09-19 15:47:10  jaume
 * refactor name IVectorialLegend -> IVectorLegend and a method name as well
 *
 * Revision 1.10  2007/09/04 14:13:01  jaume
 * *** empty log message ***
 *
 * Revision 1.9  2007/05/21 10:38:27  jaume
 * *** empty log message ***
 *
 * Revision 1.8  2007/05/17 09:32:37  jaume
 * *** empty log message ***
 *
 * Revision 1.7  2007/05/10 09:46:45  jaume
 * Refactored legend interface names
 *
 * Revision 1.6  2007/05/08 15:45:31  jaume
 * *** empty log message ***
 *
 * Revision 1.5  2007/04/20 07:54:39  jaume
 * *** empty log message ***
 *
 * Revision 1.4  2007/04/20 07:24:56  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2007/04/17 06:53:46  bsanchez
 * - Corregido fallo de Double.MIN_VALUE por Double.NEGATIVE_INFINITY comentado por Victor Olaya.
 *
 * Revision 1.2  2007/03/09 11:25:00  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.4  2007/02/21 07:35:14  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.3  2007/02/12 15:14:41  jaume
 * refactored interval legend and added graduated symbol legend
 *
 * Revision 1.1.2.2  2007/02/09 11:00:03  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2007/01/26 13:49:03  jaume
 * *** empty log message ***
 *
 * Revision 1.4  2006/11/17 13:53:45  cesar
 * *** empty log message ***
 *
 * Revision 1.3  2006/11/17 12:50:36  jaume
 * tama�o de punto defecto 2
 *
 * Revision 1.2  2006/11/15 12:57:31  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/14 11:10:27  jaume
 * *** empty log message ***
 *
 *
 */
package org.gvsig.symbology.gui.layerproperties;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Types;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JIncrementalNumberField;
import org.gvsig.symbology.fmap.rendering.DotDensityLegend;
import org.gvsig.symbology.fmap.symbols.DotDensityFillSymbol;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.MultiLayerFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.SimpleFillSymbol;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.XMLException;
import com.iver.cit.gvsig.fmap.rendering.ILegend;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ILegendPanel;
import com.iver.cit.gvsig.project.documents.view.legend.gui.JSymbolPreviewButton;
import com.iver.cit.gvsig.project.documents.view.legend.gui.Quantities;

/**
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class DotDensity extends JPanel implements ILegendPanel {
	private static final int MAX_VALUE_COUNT = 300;
	private FLyrVect layer;
	private JPanel northPanel = null;
	private GridBagLayoutPanel densityButtonsPanel = null;
	private JPanel pnlDensities = null;
	private JComboBox cmbLegendField = null;
	private JRadioButton rdBtnHigh = null;
	private JRadioButton rdBtnMedium = null;
	private JRadioButton rdBtnLow = null;
	private JIncrementalNumberField numDotSize = null;

	private JIncrementalNumberField nmbrDotValue = null;
	private JLabel lblLabellingField = null;
	private DotDensityLegend legend;
	private String fieldName;
	private boolean initializing;
	private double max;
	private double maxDotSize = 0;
	private double b, a; // line function params where: y = bx + a
	private int valueCount;


	private NumberFormat nf = NumberFormat.getInstance();
	{
		nf.setMaximumFractionDigits(3);
	}
	private MyListener cmbAction = new MyListener();

	private class MyListener implements ItemListener, ActionListener {
		public void itemStateChanged(ItemEvent e) {
			if (!initializing)
				doIt();
		}

		public void actionPerformed(ActionEvent e) {
			if (!initializing)
				doIt();
		}

		private void doIt() {
			int index = cmbLegendField.getSelectedIndex();
			try {
				SelectableDataSource sds = layer.getRecordset();

				if (index != -1) {
					fieldName = (String) cmbLegendField.getSelectedItem();
				} else {
					fieldName = (String) cmbLegendField.getItemAt(0);
				}

				int fieldIndex = sds.getFieldIndexByName(fieldName);

				long rowCount = sds.getRowCount();
				valueCount = (rowCount > MAX_VALUE_COUNT) ? MAX_VALUE_COUNT : (int) rowCount;

				double maxValue = Double.MIN_VALUE;
				double minValue = Double.MAX_VALUE;
				for (int i = 0; i < valueCount; i++) {
					double value = ((NumericValue) sds.getFieldValue(i,fieldIndex)).doubleValue();
					if (value < minValue){
						minValue = value;
					}
					if (value > maxValue){
						maxValue = value;
					}
				}
				b = (maxValue - minValue)/(valueCount);
				a = minValue;

				buttonsListener.actionPerformed(null);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};
	private ActionListener buttonsListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int oldValue = getSldDensity().getValue();
			int newValue = 0;
			if (getRdBtnHigh().isSelected()) {
				newValue = 33;
			} else if (getRdBtnLow().isSelected()) {
				newValue = 66;
			} else if (getRdBtnMedium().isSelected()) {
				newValue = 50;
			}
			if (oldValue == newValue){
				sldListener.stateChanged(null);
			} else{
				getSldDensity().setValue(newValue);
			}
		}
	};
	private JPanel centerPanel = null;
	private JSlider sldDensity = null;

	private boolean dotValueChanging= false;

	private ChangeListener sldListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			if (dotValueChanging){
				return;
			}

			dotValueChanging = true;
			double d = sldValueToDotValue(getSldDensity().getValue());
			nmbrDotValue.setDouble(d);
			dotValueChanging = false;
		}

	};


	private ActionListener nmbrDotValueListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			if (dotValueChanging){
				return;
			}

			dotValueChanging = true;
			double dotValue = getNmbrDotValue().getDouble();
			if (dotValue < 0) dotValue = 0;
			int result = dotValueToSldValue(dotValue);
			getSldDensity().setValue(result);
			dotValueChanging = false;
		}




	};
	private ColorChooserPanel jcc;
	private ILegend oldLegend;
	private JSymbolPreviewButton btnOutline;
	private ColorChooserPanel jccBackground;

	public DotDensity() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(new java.awt.Dimension(492,278));
		this.add(getNorthPanel(), java.awt.BorderLayout.NORTH);
		this.add(getCenterPanel(), java.awt.BorderLayout.CENTER);
	}

	private double sldValueToDotValue(int value){
		int quantileIndex = (valueCount*value)/100;
		double d = (b*quantileIndex+a); // /50; // �por qu� el 50?
		return d;
	}

	private int dotValueToSldValue(double value){
		int quantileIndex = (int)Math.round((value-a)/b);
		int result = 100*quantileIndex/valueCount;
		return result;
	}

	public void setData(FLayer lyr, ILegend legend) {
		this.layer = (FLyrVect) lyr;
		try {
			this.oldLegend = legend.cloneLegend();
		} catch (XMLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//Para evitar los campos no pertenecientes a la fuente original de la capa.
			// SelectableDataSource sds = layer.getSource().getRecordset();
			// FJP: Otra vez con soporte del join
			SelectableDataSource sds = layer.getRecordset();
			initializing = true; // silents events to the combo box
			cmbLegendField.removeAllItems();
			String[] fNames = sds.getFieldNames();
			for (int i = 0; i < fNames.length; i++) {
				if (isNumericField(sds.getFieldType(i))) {
					cmbLegendField.addItem(fNames[i]);
				}
			}
			if (!(legend instanceof DotDensityLegend)) {
				legend = new DotDensityLegend();
				((DotDensityLegend) legend).setClassifyingFieldNames(
						new String[] {(String) cmbLegendField.getItemAt(0)});
				((DotDensityLegend) legend).setShapeType(layer.getShapeType());
			}

			DotDensityLegend theLegend = (DotDensityLegend) legend;

			initializing = false; // enables events to the combo box

			cmbLegendField.setSelectedItem(theLegend.getClassifyingFieldNames()[0]);
			try {
				getDotColorChooserPanel().setColor(theLegend.getDotColor());
			} catch (NullPointerException npEx) {
				getDotColorChooserPanel().setColor(Color.RED);
			}
			try {
				getBackgroundColorChooserPanel().setColor(theLegend.getBGColor());
			} catch (NullPointerException npEx) {
				getDotColorChooserPanel().setColor(Color.WHITE);
			}

			getBtnOutline().setSymbol(theLegend.getOutline());
			try {
				double dotValue = theLegend.getDotValue();
				if (dotValue <= 0)
					dotValue = sldValueToDotValue(50);//100;
				getNmbrDotValue().setDouble(dotValue);
				dotValueChanging = true;
				getSldDensity().setValue(dotValueToSldValue(dotValue));
				dotValueChanging = false;

			} catch (NullPointerException npEx) {
				getSldDensity().setValue(50);
			}
			try {
				double dotSize = theLegend.getDotSize();
				if (dotSize <= 0)
					dotSize = 2;
				getNumDotSize().setDouble(dotSize);
			} catch (NullPointerException npEx) {
				getNumDotSize().setDouble(3);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isNumericField(int fieldType) {
		switch (fieldType) {
		case Types.BIGINT:
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.REAL:
		case Types.SMALLINT:
		case Types.TINYINT:
			return true;
		default:
			return false;
		}

	}

	public ILegend getLegend() {
		try {
			int shapeType = layer.getShapeType();

			// shapeType should be always polygon
			if ((shapeType%FShape.Z) != FShape.POLYGON) {
				NotificationManager.addError(PluginServices.getText(this, "cannot_apply_to_a_non_polygon_layer"), new Exception());
			}

			// check if the field exists (this is probably dead code)
			SelectableDataSource sds;
			sds = layer.getRecordset();
			if (-1 == sds.getFieldIndexByName(fieldName))
				return null;

			// gather values
			double dotValue;
			double dotSize;
			try {
//				dotValue = Double.parseDouble(nmbrDotValue.getText());
				dotValue = nmbrDotValue.getDouble();
			} catch (Exception e) {
//				dotValue = nf.parse(nmbrDotValue.getText()).doubleValue();
				dotValue = nmbrDotValue.getDouble();
			}
			if (dotValue == 0)
				dotValue = 1;
			try {
//				dotSize = Double.parseDouble(numDotSize.getText());
				dotSize = numDotSize.getDouble();
			} catch (Exception e) {
//				dotSize = nf.parse(numDotSize.getText()).doubleValue();
				dotSize = numDotSize.getDouble();
			}

			if (max/dotValue > 50000) {
				int option = JOptionPane.showConfirmDialog(this,
						PluginServices.getText(this, "looks_like_too_low_value_for_this_field_may_cause_system_to_run_slow"),
						PluginServices.getText(this, "warning"),
						JOptionPane.OK_CANCEL_OPTION);
				if (option	== JOptionPane.CANCEL_OPTION)
					return oldLegend;
			}

			// create the density symbol with the values set above
			DotDensityFillSymbol densitySymbol = new DotDensityFillSymbol();
			densitySymbol.setDotSize(dotSize);
			densitySymbol.setDotColor(getDotColorChooserPanel().getColor());

			// create a simple-fill symbol over which the dot density will be drawn
			SimpleFillSymbol fillSymbol = new SimpleFillSymbol();
			fillSymbol.setFillColor(getBackgroundColorChooserPanel().getColor());
			fillSymbol.setOutline((ILineSymbol) getBtnOutline().getSymbol());

			// combine both the DotDensitySymbol and the SimpleFillSymbol in
			// MultiLayerSymbol so they will be paint as a unique ISymbol
			MultiLayerFillSymbol symbol = new MultiLayerFillSymbol();
			symbol.setDescription(
					"DotDensitySymbol" + PluginServices.getText(this, "in_layer") +
					": '"+layer.getName()+"'");
			symbol.addLayer(fillSymbol);
			symbol.addLayer(densitySymbol);

			legend = new DotDensityLegend();
			legend.addSymbol(ValueFactory.createValue("theSymbol"), symbol);
			legend.setDefaultSymbol(symbol);
			legend.setDotValue(dotValue);
			legend.setClassifyingFieldNames(new String[] {fieldName});
			legend.setBGColor(getBackgroundColorChooserPanel().getColor());
			legend.setDotColor(getDotColorChooserPanel().getColor());

		} catch (Exception e) {
			NotificationManager.addError(PluginServices.getText(this, "could_not_setup_legend")+".", e);
		}
		return legend;

	}

	/**
	 * This method initializes centerPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			lblLabellingField = new JLabel();
			lblLabellingField.setText(PluginServices.getText(this, "labeling_field")+".");
			northPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,15,0));
			northPanel.add(lblLabellingField, null);
			northPanel.add(getCmbLegendField(), null);

		}
		return northPanel;
	}

	private ColorChooserPanel getDotColorChooserPanel() {
		if (jcc == null) {
			jcc = new ColorChooserPanel() ;
			jcc.setAlpha(255);
		}
		return jcc;
	}

	/**
	 * This method initializes southPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getDensityButtonsPanel() {
		if (densityButtonsPanel == null) {
			densityButtonsPanel = new GridBagLayoutPanel();
			LayoutManager layout = new FlowLayout(FlowLayout.LEADING, 0,0);
			JPanel aux = new JPanel(layout);
			aux.add(getNumDotSize());
			densityButtonsPanel.addComponent(
					PluginServices.getText(this, "dot_size"), aux);
			aux = new JPanel(layout);
			aux.add(getNmbrDotValue());
			densityButtonsPanel.addComponent(
					PluginServices.getText(this,"dot_value"), aux);
			aux = new JPanel(layout);
			aux.add(getDotColorChooserPanel());
			densityButtonsPanel.addComponent(
					PluginServices.getText(this, "color"), aux);
			aux = new JPanel(layout);
			aux.add(getBackgroundColorChooserPanel());
			densityButtonsPanel.addComponent(
					PluginServices.getText(this, "background_color"), aux);
			aux = new JPanel(layout);
			aux.add(getBtnOutline());
			densityButtonsPanel.addComponent(
					PluginServices.getText(this, "outline"), aux);
		}
		return densityButtonsPanel;
	}

	private ColorChooserPanel getBackgroundColorChooserPanel() {
		if (jccBackground == null) {
			jccBackground = new ColorChooserPanel() ;
			jccBackground.setColor(Color.WHITE);
			jccBackground.setAlpha(255);
		}
		return jccBackground;
	}

	private JSymbolPreviewButton getBtnOutline() {
		if (btnOutline == null) {
			btnOutline = new JSymbolPreviewButton(FShape.LINE);
			btnOutline.setPreferredSize(new Dimension(100, 35));
		}
		return btnOutline;
	}

	/**
	 * This method initializes pnlDensities
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlDensities() {
		if (pnlDensities == null) {
			pnlDensities = new JPanel(new BorderLayout(5,0));
			pnlDensities.setBorder(BorderFactory.createTitledBorder(null,
					PluginServices.getText(this, "densities")));
			JPanel aux2 = new JPanel();
			JPanel aux;
			aux = new JPanel(new GridLayout(1,3));
			aux.add(new JLabel(PluginServices.getText(this, "low")));
			aux.add(new JLabel(PluginServices.getText(this, "medium")));
			aux.add(new JLabel(PluginServices.getText(this, "high")));

			aux2.add(aux);

			aux = new JPanel(new GridLayout(1,3));
			aux.add(getRdBtnLow());
			aux.add(getRdBtnMedium());
			aux.add(getRdBtnHigh());

			aux2.add(aux);
			aux2.setLayout(new BoxLayout(aux2, BoxLayout.Y_AXIS));

			pnlDensities.add(aux2, BorderLayout.NORTH);
			pnlDensities.add(getSldDensity(), BorderLayout.CENTER);
			pnlDensities.add(getDensityButtonsPanel(), BorderLayout.SOUTH);

			ButtonGroup group = new ButtonGroup();
			group.add(getRdBtnHigh());
			group.add(getRdBtnLow());
			group.add(getRdBtnMedium());
			getRdBtnMedium().setSelected(true);
		}
		return pnlDensities;
	}


	/**
	 * This method initializes cmbLegendField
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbLegendField() {
		if (cmbLegendField == null) {
			cmbLegendField = new JComboBox();
			cmbLegendField.addActionListener(cmbAction);
		}
		return cmbLegendField;
	}

	/**
	 * This method initializes rdBtnHigh
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdBtnHigh() {
		if (rdBtnHigh == null) {
			rdBtnHigh = new JRadioButton(PluginServices.getIconTheme().get("high-density"));
			rdBtnHigh.addActionListener(buttonsListener);
		}
		return rdBtnHigh;
	}

	/**
	 * This method initializes rdBtnMedium
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdBtnMedium() {
		if (rdBtnMedium == null) {
			rdBtnMedium = new JRadioButton(PluginServices.getIconTheme()
					.get("medium-density"));
			rdBtnMedium.addActionListener(buttonsListener);
		}
		return rdBtnMedium;
	}

	/**
	 * This method initializes rdBtnMax
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdBtnLow() {
		if (rdBtnLow == null) {
			rdBtnLow = new JRadioButton(PluginServices.getIconTheme().get("low-density"));
			rdBtnLow.addActionListener(buttonsListener);
		}
		return rdBtnLow;
	}

	/**
	 * This method initializes numDotSize
	 *
	 * @return de.ios.framework.swing.JNumberField
	 */
	private JIncrementalNumberField getNumDotSize() {
		if (numDotSize == null) {
			numDotSize = new JIncrementalNumberField(null, 4);
			numDotSize.setMinValue(0);
			numDotSize.setDouble(1);
		}
		return numDotSize;
	}

	/**
	 * This method initializes nmbrDotValue
	 *
	 * @return de.ios.framework.swing.JNumberField
	 */
	private JIncrementalNumberField getNmbrDotValue() {
		if (nmbrDotValue == null) {
			nmbrDotValue = new JIncrementalNumberField(null, 15);
			nmbrDotValue.setMinValue(0.01);
			nmbrDotValue.addActionListener(nmbrDotValueListener);

		}
		return nmbrDotValue;
	}

	/**
	 * This method initializes centerPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout(5, 5));
			centerPanel.add(getPnlDensities(), java.awt.BorderLayout.WEST);
		}
		return centerPanel;
	}

	/**
	 * This method initializes sldDensity
	 *
	 * @return javax.swing.JSlider
	 */
	private JSlider getSldDensity() {
		if (sldDensity == null) {
			sldDensity = new JSlider();
			sldDensity.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			sldDensity.addChangeListener(sldListener);
		}
		return sldDensity;
	}

	public String getDescription() {
		return PluginServices.getText(this,"Defines_a_dot_density_symbol_based_on_a_field_value") + ".";
	}

	public ImageIcon getIcon() {
		return new ImageIcon(this.getClass().getClassLoader().
				getResource("images/DotDensity.PNG"));
	}

	public Class getParentClass() {
		return Quantities.class;
	}

	public String getTitle() {
		return PluginServices.getText(this, "dot_density");
	}

	public JPanel getPanel() {
		return this;
	}

	public Class getLegendClass() {
		return DotDensityLegend.class;
	}

	public boolean isSuitableFor(FLayer layer) {
		if (layer instanceof FLyrVect) {
			try {
				FLyrVect lyr = (FLyrVect) layer;

				if ((lyr.getShapeType()%FShape.Z) != FShape.POLYGON)
					return false;

				SelectableDataSource sds;
				sds = lyr.getRecordset();
				String[] fNames = sds.getFieldNames();
				for (int i = 0; i < fNames.length; i++) {
					if (isNumericField(sds.getFieldType(i))) {
						return true;
					}
				}
			} catch (ReadDriverException e) {
				return false;
			}
		}
		return false;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
