package es.icarto.gvsig.extgex.navtable.decorators.printreports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extgex.utils.managers.TableLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class PrintReportsDataSubreportReversions implements JRDataSource {

    private static final String JASPER_NUMREVERSION = "reversion_num_reversion";
    private static final String JASPER_SUPERFICIE = "reversion_superficie";
    private static final String JASPER_OCUPACION = "reversion_ocupacion";
    private static final String JASPER_FECHAACTA = "reversion_fecha_acta";
    private static final int NO_VALUE = -1;
    private int count = 0;
    private String idFinca = null;
    private ArrayList<Integer> indexOfReversionsAffected = null;
    private Table reversionsTable = null;
    private FLyrVect reversionsLayer = null;

    public PrintReportsDataSubreportReversions(String idFinca) {
	this.idFinca = idFinca;
	reversionsTable = getReversionsTable();
	reversionsLayer = getReversionsLayer();
	indexOfReversionsAffected = getReversions(this.idFinca);
    }

    private FLyrVect getReversionsLayer() {
	TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(DBNames.LAYER_REVERSIONES);
    }

    private Table getReversionsTable() {
	TableLayerManager tableManager = new TableLayerManager();
	return tableManager.getTableByName(DBNames.TABLE_FINCASREVERSIONES);
    }

    private ArrayList<Integer> getReversions(String idFinca) {
	indexOfReversionsAffected = new ArrayList<Integer>();
	ArrayList<String> reversionsAffected = new ArrayList<String>();
	try {
	    //	    SelectableDataSource sds = reversionsTable.getModel().getModelo()
	    //		    .getRecordset();
	    //	    SelectableDataSource sdsReversions = reversionsLayer.getRecordset();
	    //	    int indexOfIDFinca = sds.getFieldIndexByName(DBNames.FIELD_IDFINCA);
	    //	    int indexOfIDReversion = sds
	    //		    .getFieldIndexByName(DBNames.FIELD_IDREVERSION_REVERSIONES);
	    //	    for (int i = 0; i < sds.getRowCount(); i++) {
	    //		if (sds.getFieldValue(i, indexOfIDFinca).toString()
	    //			.equalsIgnoreCase(idFinca)) {
	    //		    reversionsAffected.add(sds.getFieldValue(i,
	    //			    indexOfIDReversion).toString());
	    //		}
	    //	    }
	    SelectableDataSource sdsReversions = reversionsLayer.getRecordset();
	    PreparedStatement statement;
	    String query = "SELECT id_reversion FROM audasa_expropiaciones.fincas_reversiones " +
		    "WHERE id_finca = '" + idFinca + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		reversionsAffected.add(rs.getString(1));
	    }

	    int indexOfIDReversion = sdsReversions.getFieldIndexByName(DBNames.FIELD_IDREVERSION_REVERSIONES);
	    for (int i = 0; i < sdsReversions.getRowCount(); i++) {
		if (reversionsAffected.contains(sdsReversions
			.getFieldValue(i, indexOfIDReversion).toString())) {
		    indexOfReversionsAffected.add(i);
		}
	    }
	    return indexOfReversionsAffected;
	} catch (ReadDriverException e) {
	    return new ArrayList<Integer>();
	} catch (SQLException e) {
	    e.printStackTrace();
	    return new ArrayList<Integer>();
	}
    }

    @Override
    public boolean next() throws JRException {
	count++;
	return (count <= indexOfReversionsAffected.size());
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
	try {
	    if (jrField.getName().equalsIgnoreCase(JASPER_NUMREVERSION)) {
		return reversionsLayer.getRecordset().getFieldValue(
			indexOfReversionsAffected.get(count - 1),
			getIndexOf(JASPER_NUMREVERSION));
	    } else if (jrField.getName().equalsIgnoreCase(JASPER_SUPERFICIE)) {
		return reversionsLayer.getRecordset().getFieldValue(
			indexOfReversionsAffected.get(count - 1),
			getIndexOf(JASPER_SUPERFICIE));
	    } else if (jrField.getName().equalsIgnoreCase(JASPER_OCUPACION)) {
		return reversionsLayer.getRecordset().getFieldValue(
			indexOfReversionsAffected.get(count - 1),
			getIndexOf(JASPER_OCUPACION));
	    } else if (jrField.getName().equals(JASPER_FECHAACTA)) {
		return reversionsLayer.getRecordset().getFieldValue(
			indexOfReversionsAffected.get(count - 1),
			getIndexOf(JASPER_FECHAACTA));
	    }
	    return null;
	} catch (ReadDriverException e) {
	    return null;
	}
    }

    private int getIndexOf(String valueName) {
	SelectableDataSource sds;
	try {
	    sds = reversionsLayer.getRecordset();
	    if (valueName.equals(JASPER_NUMREVERSION)) {
		return sds
			.getFieldIndexByName(DBNames.FIELD_IDREVERSION_REVERSIONES);
	    } else if (valueName.equals(JASPER_SUPERFICIE)) {
		return sds
			.getFieldIndexByName(DBNames.FIELD_SUPERFICIE_REVERSIONES);
	    } else if (valueName.equals(JASPER_OCUPACION)) {
		return sds
			.getFieldIndexByName(DBNames.FIELD_OCUPACION_REVERSIONES);
	    } else if (valueName.equals(JASPER_FECHAACTA)) {
		return sds
			.getFieldIndexByName(DBNames.FIELD_FECHAACTA_REVERSIONES);
	    } else {
		return NO_VALUE;
	    }
	} catch (ReadDriverException e) {
	    return NO_VALUE;
	}
    }

}
