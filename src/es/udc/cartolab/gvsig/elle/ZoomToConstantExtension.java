package es.udc.cartolab.gvsig.elle;

import java.sql.SQLException;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.utils.Constants;
import es.udc.cartolab.gvsig.elle.utils.LoadMap;

public class ZoomToConstantExtension extends Extension {

	public void execute(String actionCommand) {
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		try {
			if (LoadMap.isMapLoaded(view, "Cartograf\u00eda base")) {
				FLayer layer = view.getMapControl().getMapContext().getLayers().getLayer("N\u00facleos");
				if (layer!=null) {
					Constants c = Constants.getCurrentConstants();
					LoadMap.zoomToNucleo(layer, c.getMunCod(), c.getEntCod(), c.getNucCod());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		Constants c = Constants.getCurrentConstants();
		return c!=null;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return PluginServices.getMDIManager().getActiveWindow() instanceof View;
	}

}
