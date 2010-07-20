package es.udc.cartolab.gvsig.elle.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;

import org.gvsig.symbology.fmap.drivers.sld.FMapSLDDriver;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.gvl.FMapGVLDriver;
import com.iver.cit.gvsig.fmap.drivers.legend.IFMapLegendDriver;
import com.iver.cit.gvsig.fmap.drivers.legend.LegendDriverException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.rendering.ILegend;
import com.iver.cit.gvsig.fmap.rendering.IVectorLegend;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * This ELLE class can load legends (styles) on the layers. This styles are  'gvl' files placed on a folder defined by the user
 * on the config panel.
 * 
 * @author uve
 *
 */
public abstract class LoadLegend {

	private static String legendPath;
	private static HashMap<String, Class<? extends IFMapLegendDriver>> drivers = new HashMap<String, Class<? extends IFMapLegendDriver>>();

	static {
		drivers.put("gvl", FMapGVLDriver.class);
		drivers.put("sld", FMapSLDDriver.class);
	}

	public static void setLegendPath(String path) {
		File f = new File(path);
		if (f.exists() && f.isDirectory()) {
			legendPath = path;
		}
		if (!legendPath.endsWith(File.separator)) {
			legendPath = legendPath + File.separator;
		}
	}

	public static String getLegendPath(){
		return legendPath;
	}

	public static String getOverviewLegendPath(){
		return legendPath + "overview" + File.separator;
	}

	private static void setLegend(FLyrVect lyr, File legendFile){

		if (lyr == null) {
			System.out.println("[LoadLegend] La capa es null: " + lyr + " legend: " + legendFile);
			return;
		}

		//File styleFile = new File(getLegendPath() + legendFilename);
		if (legendFile.exists()){

			String ext = legendFile.getName().substring(legendFile.getName().lastIndexOf('.') +1);
			try {
				if (drivers.containsKey(ext.toLowerCase())) {
					IFMapLegendDriver driver = drivers.get(ext.toLowerCase()).newInstance();
					Hashtable<FLayer, ILegend> table = driver.read(lyr.getMapContext().getLayers(),lyr, legendFile);
					ILegend legend = table.get(lyr);
					if (legend != null && legend instanceof IVectorLegend) {
						lyr.setLegend((IVectorLegend)table.get(lyr));
						System.out.println("Cargado el style: "+ legendFile.getAbsolutePath());
					}
				} else {
					System.out.println("Tipo de leyenda no soportado");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			//				InputStreamReader reader;
			//				reader = new InputStreamReader(new FileInputStream(legendFile),"UTF-8");
			//				XmlTag tag = (XmlTag) XmlTag.unmarshal(reader);
			//				XMLEntity xml=new XMLEntity(tag);
			//				IVectorLegend legend = LegendFactory.createFromXML(xml);
			//				System.out.println("Legend: " + legend + " xml: " + xml.toString().length()  + "name: " + lyr.getName() + " layer: "+ lyr);
			//				lyr.setLegend(legend);
			//LoadLegend.setLegend((FLyrVect) lyr, styleFile.getAbsolutePath());
		} else {
			System.out.println("No existe el style: "+ legendFile.getAbsolutePath());
		}

	}

	public static void saveLegend(FLyrVect layer, File legendFile) throws LegendDriverException {
		String ext = legendFile.getName().substring(legendFile.getName().lastIndexOf('.') +1);
		if (drivers.containsKey(ext.toLowerCase())) {
			try {
				IFMapLegendDriver driver = drivers.get(ext.toLowerCase()).newInstance();
				driver.write(layer.getMapContext().getLayers(),layer, layer.getLegend(), legendFile, "");
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void setOverviewLegend(FLyrVect lyr, String legendFilename){

		if (legendFilename == null || !legendFilename.endsWith(".gvl")){
			legendFilename = lyr.getName().toLowerCase() + ".gvl";
		}

		File legendFile = new File(getOverviewLegendPath() + legendFilename);
		setLegend(lyr, legendFile);

	}

	public static void setLegend(FLyrVect lyr, String legendFilename) {
		setLegend(lyr, legendFilename, false);
	}

	public static void setLegend(FLyrVect lyr, String legendFilename, boolean absolutePath){

		if (!absolutePath) {
			legendFilename = getLegendPath() + legendFilename;
		}

		File legendFile = new File(legendFilename);
		setLegend(lyr, legendFile);

	}

	public static void setLegend(FLyrVect lyr){
		setLegend(lyr, (String)null);
	}

	public static void setOverviewLegend(FLyrVect lyr){
		setOverviewLegend(lyr, (String)null);
	}

	public static void loadAllStyles(View view){
		FLayers layers = view.getMapControl().getMapContext().getLayers();

		for (int i = 0; i < layers.getLayersCount(); i++){
			FLayer lyr = layers.getLayer(i);
			if (lyr instanceof FLayers){
				FLayers layers2 = (FLayers)lyr;
				for (int j = 0; j < layers2.getLayersCount(); j++){
					FLayer lyr2 = layers2.getLayer(j);
					if (lyr2 instanceof FLyrVect){
						setLegend((FLyrVect)lyr2);
					}
				}
			}
			if (lyr instanceof FLyrVect){
				setLegend((FLyrVect)lyr);
			}
		}

		layers = view.getMapOverview().getMapContext().getLayers();
		//TODO [NachoV] This FOR may be removed... because there are no groups on overview map
		for (int i = 0; i < layers.getLayersCount(); i++){
			FLayer lyr = layers.getLayer(i);
			if (lyr instanceof FLayers){
				FLayers layers2 = (FLayers)lyr;
				for (int j = 0; j < layers2.getLayersCount(); j++){
					FLayer lyr2 = layers2.getLayer(j);
					if (lyr2 instanceof FLyrVect){
						setOverviewLegend((FLyrVect)lyr2);
					}
				}
			}
			if (lyr instanceof FLyrVect){
				setOverviewLegend((FLyrVect)lyr);
			}
		}
	}

	public static void setLegend(String layerName) {

		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		FLyrVect layer = (FLyrVect) view.getModel().getMapContext().getLayers().getLayer(layerName);
		setLegend(layer);

	}
}