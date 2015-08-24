package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.elle.db.DBStructure;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ConstantUtils {

    private static final String MUNICIPIO_CONSTANTS_TABLENAME = "audasa_extgia_dominios.municipio_constantes";
    private final static String CONSTANTS_TABLE_NAME = "_constants";
    private final static String CONSTANTS_CONSTANT_FIELD_NAME = "constante";
    private final static String CONSTANTS_FILTER_FIELD_NAME = "campo_filtro";
    private final static String CONSTANTS_AFFECTED_TABLE_NAME = "nombre_tabla";

    public static String[] getValuesFromConstantByQuery(String constant) {
	String query;
	final String userArea = getAreaByConnectedUser();
	if (userArea == null) {
	    return null;
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
	return null;
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

    public static ArrayList<String> getCouncilsByConnectedUser() {
	ArrayList<String> councils = new ArrayList<String>();
	String areaByConnectedUser = ConstantUtils.getAreaByConnectedUser();
	if (areaByConnectedUser == null) {
	    return null;
	}
	String query = "SELECT id FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		+ " WHERE area = '" + areaByConnectedUser + "';";
	PreparedStatement statement;
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		councils.add(rs.getString(1));
	    }
	    // If area=Sur included also Padron into councils list
	    if (areaByConnectedUser.equalsIgnoreCase("sur")) {
		councils.add("15065");
	    }
	    return councils;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static String getWhereWithAllCouncilsOfArea(String selectedConstant) {
	String where = "";
	for (int i = 0; i < getCouncilsByConnectedUser().size(); i++) {
	    if (i != getCouncilsByConnectedUser().size() - 1) {
		where = where + getValueOfFieldByConstant(selectedConstant)
			+ " = " + "'" + getCouncilsByConnectedUser().get(i)
			+ "'" + " OR ";
	    } else {
		where = where + getValueOfFieldByConstant(selectedConstant)
			+ " = " + "'" + getCouncilsByConnectedUser().get(i)
			+ "')";
	    }
	}
	return where;
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

    public static String getValueOfFieldByConstant(String constant) {
	String query = "SELECT " + CONSTANTS_FILTER_FIELD_NAME + " FROM "
		+ DBStructure.getSchema() + "." + CONSTANTS_TABLE_NAME
		+ " WHERE " + CONSTANTS_CONSTANT_FIELD_NAME + " = " + "'"
		+ constant + "'" + ";";
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

    public static String[] getTablesAffectedByConstant(String constant) {
	String query = "SELECT nombre_tabla FROM " + DBStructure.getSchema()
		+ "." + CONSTANTS_TABLE_NAME + " WHERE "
		+ CONSTANTS_CONSTANT_FIELD_NAME + " = " + "'" + constant + "'"
		+ ";";
	PreparedStatement statement;
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    List<String> resultArray = new ArrayList<String>();
	    while (rs.next()) {
		String val = rs.getString(CONSTANTS_AFFECTED_TABLE_NAME);
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
	return null;
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
