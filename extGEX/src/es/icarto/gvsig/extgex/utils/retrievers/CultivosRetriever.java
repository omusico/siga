package es.icarto.gvsig.extgex.utils.retrievers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.data.DataSourceFactory;
import com.hardcode.gdbms.engine.instruction.EvaluationException;
import com.hardcode.gdbms.engine.instruction.SemanticException;
import com.hardcode.gdbms.parser.ParseException;
import com.iver.cit.gvsig.fmap.edition.EditableAdapter;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TableLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class CultivosRetriever {

    private String idFinca = "";
    private final HashSet<Integer> cultivosID = new HashSet<Integer>();

    public CultivosRetriever(String idFinca) {
	this.idFinca = idFinca;
	setCultivosForFinca();
    }

    public String hasCultivo(int idCultivo) {
	return (cultivosID.contains(idCultivo)) ? "Sí" : "-";
    }

    private void setCultivosForFinca() {
	try {
	    PreparedStatement statement;
	    String query = "SELECT distinct(id_cultivo) from audasa_expropiaciones.expropiaciones where id_finca = '" +idFinca +"';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		cultivosID.add(rs.getInt(1));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	//	try {
	//	    SelectableDataSource sds = getFilteredRecordset();
	//	    int idCultivoIndex = getCultivoIDIndex(sds);
	//	    for (int i = 0; i < sds.getRowCount(); i++) {
	//		cultivosID
	//			.add(((IntValue) sds.getFieldValue(i, idCultivoIndex))
	//				.getValue());
	//	    }
	//	} catch (DriverLoadException e) {
	//	    e.printStackTrace();
	//	} catch (ReadDriverException e) {
	//	    e.printStackTrace();
	//	} catch (ParseException e) {
	//	    e.printStackTrace();
	//	} catch (SemanticException e) {
	//	    e.printStackTrace();
	//	} catch (EvaluationException e) {
	//	    e.printStackTrace();
	//	}
    }

    private int getCultivoIDIndex(SelectableDataSource sds) {
	int idCultivoIndex = -1;
	try {
	    for (int i = 0; i < sds.getFieldCount(); i++) {
		if (sds.getFieldName(i).equalsIgnoreCase(
			DBNames.FIELD_IDCULTIVO)) {
		    idCultivoIndex = i;
		    break;
		}
	    }
	    return idCultivoIndex;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return idCultivoIndex;
	}
    }

    private SelectableDataSource getFilteredRecordset()
	    throws ReadDriverException, ParseException, DriverLoadException,
	    SemanticException, EvaluationException {
	TableLayerManager tableManager = new TableLayerManager();
	Table expropiaciones = tableManager
		.getTableByName(DBNames.TABLE_EXPROPIACIONES);
	IEditableSource source = expropiaciones.getModel().getModelo();
	DataSourceFactory dsf = source.getRecordset().getDataSourceFactory();
	String sqlQuery = "select * from "
		+ expropiaciones.getModel().getName() + " where "
		+ DBNames.FIELD_IDFINCA + " = '" + idFinca + "';";
	DataSource ds = dsf.executeSQL(sqlQuery, EditionEvent.ALPHANUMERIC);
	ds.setDataSourceFactory(dsf);
	SelectableDataSource sds = new SelectableDataSource(ds);
	EditableAdapter ea = new EditableAdapter();
	ea.setOriginalDataSource(sds);
	return ea.getRecordset();
    }

}
