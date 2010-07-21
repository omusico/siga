package es.udc.cartolab.gvsig.elle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

				double maxScale = -1;
				try {
					maxScale = Double.parseDouble(layers[i][5]);
				} catch (NumberFormatException e) {
					//do nothing
				}

				double minScale = -1;
				try {
					minScale = Double.parseDouble(layers[i][6]);
				} catch (NumberFormatException e) {
					//do nothing
				}

				FLayer layer = getLayer(layers[i][1], layers[i][2], schema, whereClause, proj, visible);
				if (maxScale >= minScale) {
					if (maxScale > -1) {
						layer.setMaxScale(maxScale);
					}
					if (minScale > -1) {
						layer.setMinScale(minScale);
					}
				}
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

	public static boolean isLayer(FLayers layers, String layerName) {

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

	public static void removeMap(View view, String mapName) throws SQLException {

		FLayers layersOnView = view.getMapControl().getMapContext().getLayers();
		removeMap(layersOnView, mapName, 0);
	}

	private static boolean removeMap(FLayers layers, String mapName, int depth) throws SQLException {
		DBSession dbs = DBSession.getCurrentSession();
		String where = "WHERE mapa='" + mapName + "'";
		String[][] layersOnMap = dbs.getTable("_map", where);

		ArrayList<Integer> removeIndexes = new ArrayList<Integer>();
		for (int i=0; i<layers.getLayersCount(); i++) {

			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLayers) {
				boolean removedAll = removeMap((FLayers)layer, mapName, depth++);
				if (removedAll) {
					//						layers.removeLayer(layer);
					removeIndexes.add(i);
				}
			}
			for (int j=0; j<layersOnMap.length; j++) {
				if (depth>0) {
					if (layers.getName()!=null && !layersOnMap[j][7].equals("")) {
						if (layers.getName().equalsIgnoreCase(layersOnMap[j][7]) && layer.getName().equals(layersOnMap[j][1])) {
							//								layers.removeLayer(layer);
							removeIndexes.add(i);
							break;
						}
					}
				} else {
					if (layer.getName().equals(layersOnMap[j][1])) {
						//							layers.removeLayer(layer);
						removeIndexes.add(i);
						break;
					}
				}
			}

		}
		//remove all indexes backwards to avoid losing positions
		for (int i=removeIndexes.size()-1; i>=0; i--) {
			layers.removeLayer(i);
		}
		return layers.getLayersCount()==0;
	}

	public static boolean mapExists(String mapName) throws SQLException {

		DBSession dbs = DBSession.getCurrentSession();
		String[] maps = dbs.getDistinctValues("_map", "mapa");
		boolean found = false;
		for (int i=0; i<maps.length; i++) {
			if (mapName.equals(maps[i])) {
				found = true;
				break;
			}
		}
		return found;

	}

	public static boolean legendExists(String legendName) throws SQLException {

		DBSession dbs = DBSession.getCurrentSession();
		String[] legends = dbs.getDistinctValues("_map_style", "nombre_estilo");
		boolean found = false;
		for (int i=0; i<legends.length; i++) {
			if (legendName.equals(legends[i])) {
				found = true;
				break;
			}
		}
		return found;

	}

	/**
	 * Saves the map. If the maps already exists, it'll be overwritten.
	 * @param rows
	 * @param mapName
	 * @throws SQLException
	 */
	public static void saveMap(Object[][] rows, String mapName) throws SQLException {

		String auxMapName = "__aux__" + Double.toString(Math.random()*100000).trim();
		DBSession dbs = DBSession.getCurrentSession();
		for (Object[] row : rows) {
			if (row.length == 8 || row.length == 9) {

				Object[] rowToSave = new Object[10];
				rowToSave[0] = auxMapName;
				for (int i=0; i<row.length; i++) {
					rowToSave[i+1] = row[i];
				}
				rowToSave[9] = null;

				try {
					dbs.insertRow(dbs.getSchema(), "_map", rowToSave);
				} catch (SQLException e) {
					// undo insertions
					try {
						dbs = DBSession.reconnect();
						dbs.deleteRows(dbs.getSchema(), "_map", "where mapa='" + auxMapName + "'");
						throw new SQLException(e);
					} catch (DBException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		//remove previous entries and rename aux table
		dbs.deleteRows(dbs.getSchema(), "_map", "where mapa='" + mapName + "'");
		dbs.updateRows(dbs.getSchema(), "_map", new String[]{"mapa"}, new String[]{mapName}, "where mapa='" + auxMapName + "'");
	}

	public static void saveMapOverview(Object[][] rows, String mapName) throws SQLException {

		String auxMapname = "__aux__" + Double.toString(Math.random()*100000).trim();
		DBSession dbs = DBSession.getCurrentSession();
		for (int j=0; j<rows.length; j++) {
			if (rows[j].length == 2 || rows[j].length == 3) {
				Object[] rowToSave = new Object[4];
				rowToSave[0] = auxMapname;
				rowToSave[3] = j+1;
				for (int i=0; i<rows[j].length; i++) {
					rowToSave[i+1] = rows[j][i];
				}

				try {
					dbs.insertRow(dbs.getSchema(), "_map_overview", rowToSave);
				} catch (SQLException e) {
					//undo insertions
					try {
						dbs = DBSession.reconnect();
						dbs.deleteRows(dbs.getSchema(), "_map_overview", "where mapa='" + auxMapname + "'");
						throw new SQLException(e);
					} catch (DBException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		//remove previous entries and rename aux table
		dbs.deleteRows(dbs.getSchema(), "_map_overview", "where mapa='" + mapName + "'");
		dbs.updateRows(dbs.getSchema(), "_map_overview", new String[]{"mapa"}, new String[]{mapName}, "where mapa='" + auxMapname + "'");

	}

	public static void createMapTables() throws SQLException {

		DBSession dbs = DBSession.getCurrentSession();

		String sqlCreateMap = "CREATE TABLE " + dbs.getSchema() +"._map "
		+ "("
		+ "   mapa character varying(255) NOT NULL,"
		+ "   nombre_capa character varying(255) NOT NULL,"
		+ "   nombre_tabla character varying(255),"
		+ "   posicion integer NOT NULL DEFAULT 0,"
		+ "   visible boolean,"
		+ "   max_escala character varying(50),"
		+ "   min_escala character varying(50),"
		+ "   grupo character varying,"
		+ "   \"schema\" character varying,"
		+ "   localizador boolean,"
		+ "   CONSTRAINT \"primary key\" PRIMARY KEY (mapa, nombre_capa)"
		+ ")"
		+ "WITH ("
		+ "   OIDS=FALSE"
		+ ")";

		String sqlCreateMapOverview =  "CREATE TABLE " + dbs.getSchema() + "._map_overview"
		+ "("
		+ "  mapa character varying NOT NULL,"
		+ "  nombre_capa character varying NOT NULL,"
		+ "  \"schema\" character varying,"
		+ "  posicion integer,"
		+ "  CONSTRAINT _map_overview_pkey PRIMARY KEY (mapa, nombre_capa)"
		+ ")"
		+ "WITH ("
		+ "  OIDS=FALSE"
		+ ")";


		String sqlCreateMapStyle =  "CREATE TABLE " + dbs.getSchema() + "._map_style"
		+ "("
		+ "  nombre_capa character varying NOT NULL,"
		+ "  nombre_estilo character varying NOT NULL,"
		+ "  type character varying(3),"
		+ "  definicion xml,"
		+ "  CONSTRAINT _map_style_pkey PRIMARY KEY (nombre_capa, nombre_estilo)"
		+ ")"
		+ "WITH ("
		+ "  OIDS=FALSE"
		+ ")";

		String sqlGrant = "GRANT SELECT ON TABLE " + dbs.getSchema() + ".%s TO public";

		Connection con = dbs.getJavaConnection();
		Statement stat = con.createStatement();

		if (!dbs.tableExists(dbs.getSchema(), "_map")) {
			stat.execute(sqlCreateMap);
			stat.execute(String.format(sqlGrant, "_map"));
		}

		if (!dbs.tableExists(dbs.getSchema(), "_map_overview")) {
			stat.execute(sqlCreateMapOverview);
			stat.execute(String.format(sqlGrant, "_map_overview"));
		}

		if (!dbs.tableExists(dbs.getSchema(), "_map_style")) {
			stat.execute(sqlCreateMapStyle);
			stat.execute(String.format(sqlGrant, "_map_style"));
		}

		con.commit();
	}

	public static void deleteMap(String mapName) throws SQLException {
		DBSession dbs = DBSession.getCurrentSession();
		String removeMap = "DELETE FROM " + dbs.getSchema() + "._map WHERE mapa=?";
		String removeMapOverview = "DELETE FROM " + dbs.getSchema() + "._map_overview WHERE mapa=?";

		PreparedStatement ps = dbs.getJavaConnection().prepareStatement(removeMap);
		ps.setString(1, mapName);
		ps.executeUpdate();
		ps.close();

		ps = dbs.getJavaConnection().prepareStatement(removeMapOverview);
		ps.setString(1, mapName);
		ps.executeUpdate();
		ps.close();

		dbs.getJavaConnection().commit();
	}

	public static void deleteLegends(String legendsName) throws SQLException {
		DBSession dbs = DBSession.getCurrentSession();
		String removeMap = "DELETE FROM " + dbs.getSchema() + "._map_style WHERE nombre_estilo=?";
		String removeMapOverview = "DELETE FROM " + dbs.getSchema() + "._map_overview_style WHERE nombre_estilo=?";

		PreparedStatement ps = dbs.getJavaConnection().prepareStatement(removeMap);
		ps.setString(1, legendsName);
		ps.executeUpdate();
		ps.close();

		ps = dbs.getJavaConnection().prepareStatement(removeMapOverview);
		ps.setString(1, legendsName);
		ps.executeUpdate();
		ps.close();

		dbs.getJavaConnection().commit();
	}
}

