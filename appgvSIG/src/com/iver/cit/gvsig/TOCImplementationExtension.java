package com.iver.cit.gvsig;

import org.apache.log4j.Logger;
import org.gvsig.fmap.swing.impl.toc.DefaultTOCFactory;
import org.gvsig.fmap.swing.impl.toc.TOCDefaultImplLibrary;
import org.gvsig.fmap.swing.toc.TOCFactory;
import org.gvsig.fmap.swing.toc.TOCLibrary;
import org.gvsig.fmap.swing.toc.TOCLocator;
import org.gvsig.fmap.swing.toc.TOCManager;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.impl.DefaultDynObjectManager;
import org.gvsig.tools.library.Library;
import org.gvsig.tools.service.ServiceException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;
import com.iver.cit.gvsig.gui.preferencespage.TOCImplementationPage;
import com.iver.cit.gvsig.project.documents.view.toc.impl.DefaultToc;
import com.iver.utiles.XMLEntity;

public class TOCImplementationExtension extends Extension 
implements IPreferenceExtension {
	
	private static Logger logger = Logger.getLogger(TOCImplementationExtension.class.getName());
	private TOCImplementationPage page = null;
	
	private static boolean initDone = false;

	public IPreference getPreferencesPage() {
		if (page==null) {
			page = new TOCImplementationPage();
		}
		return page;
	}
	



	public void initialize() {
		init();
	}
	
	public void postInitialize() {
		tryToSetTOCFromPersistence();
	}

	private void tryToSetTOCFromPersistence() {

		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		if (xml!=null && xml.contains(DefaultToc.TOC_CURRENT_TOC_KEY)) {
			
			String pref_toc = xml.getStringProperty(DefaultToc.TOC_CURRENT_TOC_KEY);
			try {
				TOCLocator.getInstance().getTOCManager().setDefaultTOCFactory(pref_toc);
			} catch (Exception ex) {
				NotificationManager.addError("While getting TOC from persistence. ", ex);
			}
		}		
	}




	private void init() {
		
		if (initDone) {
			return;
		}
		
		Library lib = new ToolsLibrary();
		lib.initialize();
		lib.postInitialize();
		
		lib = new TOCLibrary();
		lib.initialize();
		lib.postInitialize();
		
		// =========================
		
		lib = new TOCDefaultImplLibrary();
		lib.initialize();
		lib.postInitialize();
		
		
		initDone = true;
	}

	
	public void setPreferredTOCImplementation(TOCFactory tf) {
		if (tf==null) {
			return;
		}
		
		try {
            TOCLocator.getInstance().getTOCManager().setDefaultTOCFactory(tf.getName());
        } catch (ServiceException e) {
            logger.error("Unable to set new default toc: " + tf.getName() + ". " + e.getMessage());
        }

		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		xml.putProperty(
				DefaultToc.TOC_CURRENT_TOC_KEY,
				tf.getName());
	}




	public boolean isEnabled() {
		return false;
	}

	public boolean isVisible() {
		return false;
	}
	

	public IPreference[] getPreferencesPages() {
		IPreference[] preferences=new IPreference[1];
		preferences[0]=getPreferencesPage();
		return preferences;
	}
	
	public void execute(String actionCommand) { }


}
