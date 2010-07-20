package es.udc.cartolab.gvsig.elle;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.save.SaveLegendsWizard;

public class SaveAllLegendsExtension extends Extension {

	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
		//		boolean saveOverView = false;
		int option = -1;
		//		FLayer[] layers = null;
		//		FLayers allLayers =  ((View) PluginServices.getMDIManager().getActiveWindow()).
		//		getMapControl().getMapContext().getLayers();
		if (actionCommand.equals("ACTIVES")) {
			option = SaveLegendsWizard.ACTIVES;
			//			layers = allLayers.getActives();
		}
		if (actionCommand.equals("VISIBLES")) {
			option = SaveLegendsWizard.VISIBLES;
			//			layers = allLayers.getVisibles();
		}
		if (actionCommand.equals("ALL")) {
			option = SaveLegendsWizard.ALL;
			//			saveOverView = true;
			//			layers = new FLayer[allLayers.getLayersCount()];
			//			for (int i=0; i<allLayers.getLayersCount(); i++) {
			//				layers[i] = allLayers.getLayer(i);
			//			}
		}
		//		if (actionCommand.equals("MAPOVERVIEW")) {
		//			saveOverView = true;
		//		}
		//		if ((layers != null && layers.length>0) || saveOverView) {
		SaveLegendsWizard w = new SaveLegendsWizard((View) PluginServices.getMDIManager().getActiveWindow(), option);
		w.open();
		//			SaveAllLegendsDialog dialog;
		//			if (saveOverView) {
		//				FLayers allOVlayers = ((View) PluginServices.getMDIManager().getActiveWindow()).getMapOverview().getMapContext().getLayers();
		//				FLayer[] overviewLayers = new FLayer[allOVlayers.getLayersCount()];
		//				for (int i=0; i<allOVlayers.getLayersCount(); i++) {
		//					overviewLayers[i] = allOVlayers.getLayer(i);
		//				}
		//				dialog = new SaveAllLegendsDialog(layers, overviewLayers);
		//			} else {
		//			dialog = new SaveAllLegendsDialog(layers);
		//			}
		//			PluginServices.getMDIManager().addCentredWindow(dialog);


		//		} else {
		//			//lanzar error?
		//		}
	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof View) {
			if (((View) window).getMapControl().getMapContext().getLayers().getLayersCount()>0) {
				return true;
			}
		}
		return false;
	}


}
