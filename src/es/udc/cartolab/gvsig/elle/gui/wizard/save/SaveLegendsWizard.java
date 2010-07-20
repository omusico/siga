package es.udc.cartolab.gvsig.elle.gui.wizard.save;

import java.util.ArrayList;
import java.util.List;

import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.VectorialDriver;
import com.iver.cit.gvsig.fmap.drivers.jdbc.postgis.PostGisDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.VectorialDBAdapter;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class SaveLegendsWizard extends SaveMapWizard {

	public final static int ACTIVES = 0;
	public final static int VISIBLES = 1;
	public final static int ALL = 2;

	private int layersOption;

	public SaveLegendsWizard(View view, int layersOption) {
		super(view);

		this.layersOption = layersOption;
		setLayersProperties();

	}

	@Override
	protected void addWizardComponents() {
		views.add(new SaveLegendsWizardComponent(properties));
	}

	private void setLayersProperties() {
		Object aux = properties.get(SaveMapWizardComponent.PROPERTY_VIEW);
		if (aux != null && aux instanceof View) {
			FLayers layers = ((View) aux).getMapControl().getMapContext().getLayers();
			List<LayerProperties> list = getList(layers);
			properties.put(SaveMapWizardComponent.PROPERTY_LAYERS_MAP, list);
		}
	}

	private List<LayerProperties> getList(FLayers layers) {
		List<LayerProperties> list = new ArrayList<LayerProperties>();

		for (int i=layers.getLayersCount()-1; i>=0; i--) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLayers) {
				list.addAll(getList((FLayers) layer));
			} else {
				if (layer instanceof FLyrVect) {
					VectorialDriver driver = ((FLyrVect) layer).getSource().getDriver();
					if (driver instanceof PostGisDriver) {
						//					ReadableVectorial rv = ((VectorialDBAdapter) ((FLyrVect) layer).getSource()).getOriginalAdapter();
						DBLayerDefinition layerDef = ((VectorialDBAdapter) ((FLyrVect) layer).getSource()).getLyrDef();

						String schema = layerDef.getSchema();
						String table = layerDef.getTableName();
						String layerName = layer.getName();

						LayerProperties lp = new LayerProperties(schema, table, layerName);
						lp.setVisible(layer.isVisible());

						switch (layersOption) {
						case ACTIVES :
							lp.setSave(layer.isActive());
							break;
						case VISIBLES :
							lp.setSave(layer.isVisible());
							break;
						case ALL:
						default:
							lp.setSave(true);
						}

						lp.setShownname(layerName);
						lp.setGroup(layer.getParentLayer().getName());
						lp.setMaxScale(layer.getMaxScale());
						lp.setMinScale(layer.getMinScale());
						lp.setPosition(layers.getLayersCount()-i);

						list.add(lp);
					}
				}
			}
		}

		return list;

	}

}
