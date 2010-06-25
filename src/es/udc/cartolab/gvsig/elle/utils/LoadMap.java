package es.udc.cartolab.gvsig.elle.utils;

import java.sql.SQLException;

import org.cresques.cts.IProjection;

import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LoadMap {

	private LoadMap() {

	}

	public static FLayer getLayer(String layerName, String tableName,
			String schema, String whereClause, IProjection proj,
			boolean visible) throws SQLException, DBException {
		DBSession dbs = DBSession.getCurrentSession();
		FLayer layer = null;

		if (dbs != null) {
			if (schema!=null) {
				layer = dbs.getLayer(layerName, tableName, schema, whereClause, proj);
			} else {
				layer = dbs.getLayer(layerName, tableName, whereClause, proj);
			}
			layer.setVisible(visible);
		}
		return layer;
	}

	public static void loadMap(View view, String mapName,
			IProjection proj) throws Exception {
		loadMap(view, mapName, proj, null);
	}

	/**
	 * Get layers querying on '_map' table to the MapView.
	 * Get layers querying on '_map_overview' table to the MapOverView.
	 * 
	 * _MAP SCHEMA:
	 * 0.- mapa character varying(255) NOT NULL,
	 * 1.- nombre_capa character varying(255) NOT NULL,
	 * 2.- nombre_tabla character varying(255),
	 * 3.- posicion integer NOT NULL DEFAULT 0,
	 * 4.- visible boolean,
	 * 5.- max_escala character varying(50),
	 * 6.- min_escala character varying(50),
	 * 7.- grupo character varying,
	 * 8.- "schema" character varying,
	 * 9.- localizador boolean
	 * 
	 * 
	 * 
	 * @param view
	 * @param mapName
	 * @param proj
	 * @param loadCartBase
	 * @throws Exception
	 */
	public static void loadMap(View view, String mapName,
			IProjection proj, String whereClause) throws Exception {

		if (whereClause == null) {
			whereClause = "";
		}

		DBSession dbs = DBSession.getCurrentSession();
		if (dbs != null) {
			String where = "WHERE mapa='" + mapName + "'";

			System.out.println(where);

			/////////////// MapControl
			String[][] layers = dbs.getTable("_map", dbs.getSchema(), where, new String[]{"posicion"}, false);

			FLayers group = null;
			String groupName = "default";
			for (int i=0; i<layers.length; i++) {
				String schema=null;
				if (layers[i][8].length()>0) {
					schema = layers[i][8];
				}

				boolean visible = true;
				if (!layers[i][4].equalsIgnoreCase("t")) {
					visible = false;
				}

				FLayer layer = getLayer(layers[i][1], layers[i][2], schema, whereClause, proj, visible);
				if (layers[i][7].length()>0) {
					if (layers[i][7].equals(groupName)) {
						group.addLayer(layer);
					} else {
						group = new FLayers();
						group.setName(layers[i][7].toUpperCase());
						group.setMapContext(view.getMapControl().getMapContext());
						groupName = layers[i][7];
						group.addLayer(layer);
						view.getMapControl().getMapContext().getLayers().addLayer(group);
					}
				} else {
					view.getMapControl().getMapContext().getLayers().addLayer(layer);
				}
				//				//Add to MapOverview (Localizator) the layer
				//				if (layers[i][9].length()>0 && layers[i][9].equalsIgnoreCase("t")) {
				//					view.getMapOverview().getMapContext().getLayers().addLayer(layer.cloneLayer());
				//				}

			}

			/////////////// MapOverview
			String[][] layersOV = dbs.getTable("_map_overview", dbs.getSchema(), where, new String[]{"posicion"}, false);

			//			constants = Constants.getCurrentConstants();

			for (int i=0; i<layersOV.length; i++) {
				String schema=null;
				if (layersOV[i][2].length()>0) {
					schema = layersOV[i][2];
				}

				FLayer layer = getLayer(layersOV[i][1], layersOV[i][1], schema, whereClause, proj, true);
				view.getMapOverview().getMapContext().getLayers().addLayer(layer.cloneLayer());

			}

		}

	}

	private static boolean isLayer(FLayers layers, String layerName) {

		for (int i=0; i<layers.getLayersCount(); i++) {
			boolean found = false;
			if (layers.getLayer(i) instanceof FLayers) {
				found = isLayer((FLayers)layers.getLayer(i), layerName);
			} else {
				if (layers.getLayer(i).getName().equals(layerName)) {
					found = true;
				}
			}
			if (found) {
				return true;
			}
		}
		return false;
	}


	/**
	 * This function makes a DB call, so it shouldn't be used in functions that are
	 * executed a lot of times like isVisible or isEnabled in a gvSIG extension.
	 * @param view
	 * @param mapName
	 * @return
	 * @throws SQLException
	 */
	public static boolean isMapLoaded(View view, String mapName) throws SQLException {

		DBSession dbs = DBSession.getCurrentSession();
		String where = "WHERE mapa='" + mapName + "'";
		String[][] layersOnMap = dbs.getTable("_map", where);
		FLayers layersOnView = view.getMapControl().getMapContext().getLayers();
		boolean result = true;
		for (int i=0; i<layersOnMap.length; i++) {
			result = result && isLayer(layersOnView, layersOnMap[i][1]);
			if (!result) {
				break;
			}
		}
		return result;

	}

}
