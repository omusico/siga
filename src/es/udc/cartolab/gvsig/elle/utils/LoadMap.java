package es.udc.cartolab.gvsig.elle.utils;

import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.List;

import org.cresques.cts.IProjection;

import com.hardcode.gdbms.driver.exceptions.InitializeDriverException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueWriter;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LoadMap {

	/*
	 * Propio de la EIEL:
	 *  * variables de cartografiaBase y nucleosLayer, y todo lo que conlleva
	 *    en la carga del mapa.
	 *  * Constantes.
	 *  * El parametro loadCartBase de la funcion loadMap.
	 */

	private static final String cartografiaBase = "Cartograf\u00eda base";
	private static final String nucleosLayer = "N\u00facleos";

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
		loadMap(view, mapName, proj, false);
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
			IProjection proj, boolean loadCartBase) throws Exception {

		/*
		 * EIEL hacks to remove: constants.
		 */

		DBSession dbs = DBSession.getCurrentSession();
		if (dbs != null) {
			String where = "WHERE mapa='" + mapName + "'";
			if (loadCartBase) {
				where = where.concat(" OR mapa='" + cartografiaBase + "'");
			}

			System.out.println(where);

			/////////////// MapControl
			String[][] layers = dbs.getTable("_map", dbs.getSchema(), where, new String[]{"posicion"}, false);

			Constants constants = Constants.getCurrentConstants();
			String whereClause = "";
			if (constants!=null) {
				whereClause = "WHERE ";
				List<String> municipios = constants.getMunicipios();
				for (int j=0; j<municipios.size()-1; j++) {
					whereClause = whereClause.concat("municipio ='" + municipios.get(j) +
					"' OR ");
				}
				whereClause = whereClause.concat("municipio ='" + municipios.get(municipios.size()-1) + "'");
			}

			FLayer nucLayer = null;
			FLayers group = null;
			String groupName = "default";
			for (int i=0; i<layers.length; i++) {
				String schema=null;
				if (layers[i][8].length()>1) {
					schema = layers[i][8];
				}

				boolean visible = true;
				if (!layers[i][4].equalsIgnoreCase("t")) {
					visible = false;
				}

				FLayer layer = getLayer(layers[i][1], layers[i][2], schema, whereClause, proj, visible);
				if (layers[i][7].length()>1) {
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
				if (layers[i][1].equals(nucleosLayer)) {
					nucLayer = layer;
				}
			}

			if (constants!=null && !constants.getNucCod().equals("")) {
				if (nucLayer!=null) {
					zoomToNucleo(nucLayer, constants.getMunCod(), constants.getEntCod(), constants.getNucCod());
				}
			}

			/////////////// MapOverview
			String[][] layersOV = dbs.getTable("_map_overview", dbs.getSchema(), where, new String[]{"posicion"}, false);

			constants = Constants.getCurrentConstants();

			for (int i=0; i<layersOV.length; i++) {
				String schema=null;
				if (layersOV[i][2].length()>1) {
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

	public static void zoomToNucleo (FLayer layer, String codMun, String codEnt, String codNuc) {
		Rectangle2D rectangle = null;

		if (layer instanceof AlphanumericData) {
			int pos = -1;

			SelectableDataSource recordset;
			try {
				recordset = ((FLyrVect) layer).getRecordset();


				int munIdx = recordset.getFieldIndexByName("mun");
				int entIdx = recordset.getFieldIndexByName("ent");
				int nucIdx = recordset.getFieldIndexByName("poblamiento");

				if (munIdx > -1 && entIdx > -1 && nucIdx > -1) {
					for (int i=0; i<recordset.getRowCount(); i++) {
						Value val = recordset.getFieldValue(i, munIdx);
						String cod = val.getStringValue(ValueWriter.internalValueWriter);
						cod = cod.replaceAll("'", "");
						if (cod.equals(codMun)) {
							val = recordset.getFieldValue(i, entIdx);
							cod = val.getStringValue(ValueWriter.internalValueWriter);
							cod = cod.replaceAll("'", "");
							if (cod.equals(codEnt)) {
								val = recordset.getFieldValue(i, nucIdx);
								cod = val.getStringValue(ValueWriter.internalValueWriter);
								cod = cod.replaceAll("'", "");
								if (cod.equals(codNuc)) {
									pos = i;
								}
							}
						}
					}
				}
			} catch (ReadDriverException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			if (pos > -1) {

				//TODO gvSIG comment: Esta comprobacion se hacia con Selectable
				try {
					IGeometry g;
					ReadableVectorial source = ((FLyrVect)layer).getSource();
					source.start();
					g = source.getShape(pos);
					source.stop();

					/* fix to avoid zoom problems when layer and view
					 * projections aren't the same. */
					g.reProject(layer.getProjection().getCT(layer.getMapContext().getProjection()));

					rectangle = g.getBounds2D();

					if (rectangle.getWidth() < 200){
						rectangle.setFrameFromCenter(rectangle.getCenterX(),
								rectangle.getCenterY(),
								rectangle.getCenterX()+100,
								rectangle.getCenterY()+100);
					}

					if (rectangle != null) {
						layer.getMapContext().getViewPort().setExtent(rectangle);
					}

				} catch (InitializeDriverException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ReadDriverException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
