package es.udc.cartolab.gvsig.elle;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.LoadAllLegendsDialog;

public class LoadAllLegendsExtension extends Extension {

	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
		LoadAllLegendsDialog dialog;
		try {
			dialog = new LoadAllLegendsDialog(
					(View)PluginServices.getMDIManager().getActiveWindow());
			PluginServices.getMDIManager().addCentredWindow(dialog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,
				    e.getMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		IWindow w = PluginServices.getMDIManager().getActiveWindow();
		if (w instanceof View) {
			FLayers layers = ((View) w).getMapControl().getMapContext().getLayers();
			return layers.getLayersCount() > 0;
		}
		return false;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		IWindow w = PluginServices.getMDIManager().getActiveWindow();
		return w instanceof View;
	}

}
