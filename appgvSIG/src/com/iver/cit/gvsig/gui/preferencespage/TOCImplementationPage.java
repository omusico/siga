package com.iver.cit.gvsig.gui.preferencespage;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.gvsig.fmap.swing.toc.TOCFactory;
import org.gvsig.fmap.swing.toc.TOCLocator;
import org.gvsig.fmap.swing.toc.TOCManager;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.cit.gvsig.TOCImplementationExtension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.order.DefaultOrderManager;
import com.iver.cit.gvsig.fmap.layers.order.OrderManager;
import com.iver.cit.gvsig.project.documents.view.toc.gui.TocImplementationComboItem;
import com.iver.cit.gvsig.project.documents.view.toc.impl.DefaultToc;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

public class TOCImplementationPage
	extends AbstractPreferencePage implements ItemListener {
	
	private static Logger logger =
		Logger.getLogger(TOCImplementationPage.class.getName());
	
	private boolean initialized = false;
	private JComboBox tocCombo = null;
	protected boolean changed = false;

	public TOCImplementationPage() {
		super();
		setParentID(ViewPage.class.getName());
	}


	public void storeValues() throws StoreException {
		
		Object object = tocCombo.getSelectedItem();
		if (object instanceof TocImplementationComboItem) {
			TocImplementationComboItem sel = (TocImplementationComboItem) object;
			IExtension ext = PluginServices.getExtension(TOCImplementationExtension.class);
			TOCImplementationExtension toc_ext =
				(TOCImplementationExtension) ext;
			toc_ext.setPreferredTOCImplementation(sel.getTOCFactory());
		}
		changed = false;
	}

	public String getID() {
		return getClass().getName();
	}

	public ImageIcon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	public JPanel getPanel() {
		return this;
	}

	public String getTitle() {
		return PluginServices.getText(this, "TOC_style");
	}

	public void initializeDefaults() {
		// TODO Auto-generated method stub
		
	}

	public void initializeValues() {
		if (!initialized) {
			initialized = true;
			createUI();
		} else {
		    // reload combo
		    fillTocCombo(tocCombo);
		}
	}
	
	private void createUI() {
		
		JLabel label = new JLabel(PluginServices.getText(this, "TOC_to_use"));
		tocCombo = new JComboBox();
		
		fillTocCombo(tocCombo);
		
		tocCombo.addItemListener(this);
		this.addComponent(
				label,
				tocCombo,
				GridBagConstraints.NONE, new Insets(8, 8, 8, 8));		
	}


	private void fillTocCombo(JComboBox cmb) {
		
		try {
			
		    cmb.removeAllItems();
		    
			TOCManager tm = TOCLocator.getInstance().getTOCManager();
			
			TOCFactory sel_tf = tm.getDefaultTOCFactory();
			
			List<TOCFactory> lis = tm.getTOCFactories();
			TocImplementationComboItem item = null;
			
			int n = lis.size();
			TOCFactory tf = null;
			for (int i=0; i<n; i++) {
				tf = lis.get(i);
				item = new TocImplementationComboItem(tf);
				cmb.addItem(item);
				if (tf.getName().compareToIgnoreCase(sel_tf.getName()) == 0) {
					cmb.setSelectedItem(item);
				}
			}
		} catch (Exception ex) {
			logger.warn("Error getting toc implems: ", ex);
		}
	}


	public boolean isValueChanged() {
		return changed;
	}

	public void itemStateChanged(ItemEvent e) {
		changed = true;
	}

	public void setChangesApplied() { }

	

}
