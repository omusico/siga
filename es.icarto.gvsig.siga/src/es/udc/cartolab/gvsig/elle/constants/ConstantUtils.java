package es.udc.cartolab.gvsig.elle.constants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.elle.db.DBStructure;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ConstantUtils {

    // TODO. Modificar tabla _constants porque sólo se la toca desde aqui
    private static final Logger logger = Logger.getLogger(ConstantUtils.class);

    private static final String MUNICIPIO_CONSTANTS_TABLENAME = "audasa_extgia_dominios.municipio_constantes";
    private final static String CONSTANTS_TABLE_NAME = "_constants";
    private final static String CONSTANTS_AFFECTED_TABLE_NAME = "nombre_tabla";

    public static String[] getValuesFromConstantByQuery() {
	String query;
	final String userArea = getAreaByConnectedUser();
	if (userArea == null) {
	    return new String[0];
	}
	if (userArea.equalsIgnoreCase("ambas")) {
	    query = "SELECT tag FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		    + " ORDER BY orden;";
	} else if (userArea.equalsIgnoreCase("sur")) {
	    // Include Padron in councils list if area = Sur
	    query = "SELECT tag, orden FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		    + " WHERE area = " + "'" + userArea
		    + "' UNION SELECT tag, orden FROM "
		    + MUNICIPIO_CONSTANTS_TABLENAME + " WHERE id = '15065'"
		    + " ORDER BY orden;";
	} else {
	    query = "SELECT tag FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		    + " WHERE area = " + "'" + userArea + "' ORDER BY orden;";
	}
	PreparedStatement statement;
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    List<String> resultArray = new ArrayList<String>();
	    while (rs.next()) {
		String val = rs.getString(1);
		resultArray.add(val);
	    }
	    rs.close();

	    String[] result = new String[resultArray.size()];
	    for (int i = 0; i < resultArray.size(); i++) {
		result[i] = resultArray.get(i);
	    }

	    return result;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return new String[0];
    }

    public static String getAreaByConnectedUser() {
	String query = "SELECT area FROM " + "audasa_expedientes.usuarios"
		+ " WHERE name =" + "'"
		+ DBSession.getCurrentSession().getUserName() + "'" + ";";
	PreparedStatement statement;
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    if (!rs.next()) {
		return null;
	    }
	    rs.first();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static String getNombreMunicipioById(String id) {
	String query = "SELECT item FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		+ " WHERE id =" + "'" + id + "'" + ";";
	PreparedStatement statement;
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.first();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static Collection<String> getCouncilsByConnectedUser() {

	String areaByConnectedUser = ConstantUtils.getAreaByConnectedUser();
	if (areaByConnectedUser == null) {
	    return null;
	}
	try {
	    String[][] table = DBSession.getCurrentSession().getTable(
		    "municipio_constantes", "audasa_extgia_dominios",
		    new String[] { "id" },
		    " WHERE area = '" + areaByConnectedUser + "'", null, false);

	    Collection<String> councils = flat(table);
	    // If area=Sur included also Padron into councils list
	    if (areaByConnectedUser.equalsIgnoreCase("sur")) {
		councils.add("15065");
	    }
	    return councils;
	} catch (SQLException e) {
	    logger.error(e.getStackTrace(), e);
	}
	return Collections.emptyList();
    }

    public static String getIdByConstantTag(String constantTag) {
	String query = "SELECT id FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		+ " WHERE tag =" + "'" + constantTag + "'" + ";";
	PreparedStatement statement;
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.first();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private static <T> Collection<T> flat(T[][] array) {
	Collection<T> result = new ArrayList<T>();

	for (T[] e : array) {
	    Collections.addAll(result, e);
	}

	return result;
    }

    public static Collection<String> getTablesAffectedByConstant() {
	try {
	    String[][] table = DBSession.getCurrentSession().getTable(
		    CONSTANTS_TABLE_NAME, DBStructure.SCHEMA_NAME,
		    new String[] { CONSTANTS_AFFECTED_TABLE_NAME }, null, null,
		    false);
	    return flat(table);
	} catch (SQLException e) {
	    logger.error(e.getStackTrace(), e);
	}
	return Collections.emptyList();
    }

    public static String constantChecks(View view) {
	TOCLayerManager tocManager = new TOCLayerManager(view.getMapControl());
	List<FLyrVect> joinedLayers = tocManager.getJoinedLayers();
	List<FLyrVect> editingLayers = tocManager.getEditingLayers();

	String errorMsg = "";

	if (!joinedLayers.isEmpty()) {
	    errorMsg += "Deshaga las uniones o enlaces de las siguientes capas para poder continuar:\n\n";
	    for (FLyrVect l : joinedLayers) {
		errorMsg += " - " + l.getName() + "\n";
	    }
	    errorMsg += "\n\n";

	}

	if (!editingLayers.isEmpty()) {
	    errorMsg += "Cierre la edición de las siguientes capas para poder continuar:\n\n";
	    for (FLyrVect l : editingLayers) {
		errorMsg += " - " + l.getName() + "\n";
	    }

	}

	return errorMsg;
    }

}
