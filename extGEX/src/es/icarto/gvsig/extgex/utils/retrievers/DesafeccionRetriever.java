package es.icarto.gvsig.extgex.utils.retrievers;

import java.util.HashMap;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.data.DataSourceFactory;
import com.hardcode.gdbms.engine.instruction.EvaluationException;
import com.hardcode.gdbms.engine.instruction.SemanticException;
import com.hardcode.gdbms.parser.ParseException;
import com.iver.cit.gvsig.fmap.edition.EditableAdapter;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class DesafeccionRetriever {

    private HashMap<String, String> values = null;

    public DesafeccionRetriever(String fincaID) {
	fillValues(fincaID);
    }

    private void fillValues(String idFinca) {
	values = new HashMap<String, String>();
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect desafecciones = toc
		.getLayerByName(DBNames.TABLE_DESAFECCIONES);
	if (desafecciones == null) {
	    values.put(DBNames.FIELD_FECHAACTA_DESAFECCIONES, "");
	    values.put(DBNames.FIELD_SUPERFICIE_DESAFECCIONES, "");
	    values.put(DBNames.FIELD_OCUPACION_DESAFECCIONES, "");
	} else {
	    SelectableDataSource sds = getFilteredRecordset(idFinca);
	    try {
		int indexOfOcupacion = sds
			.getFieldIndexByName(DBNames.FIELD_OCUPACION_DESAFECCIONES);
		int indexOfSuperficie = sds
			.getFieldIndexByName(DBNames.FIELD_SUPERFICIE_DESAFECCIONES);
		int indexOfFechaActa = sds
			.getFieldIndexByName(DBNames.FIELD_FECHAACTA_DESAFECCIONES);
		values.put(DBNames.FIELD_FECHAACTA_DESAFECCIONES, sds
			.getFieldValue(0, indexOfFechaActa).toString());
		values.put(DBNames.FIELD_SUPERFICIE_DESAFECCIONES, sds
			.getFieldValue(0, indexOfSuperficie).toString());
		values.put(DBNames.FIELD_OCUPACION_DESAFECCIONES, sds
			.getFieldValue(0, indexOfOcupacion).toString());
	    } catch (ReadDriverException e) {
		e.printStackTrace();
	    }
	}
    }

    private SelectableDataSource getFilteredRecordset(String idFinca) {
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect desafecciones = toc
		.getLayerByName(DBNames.TABLE_DESAFECCIONES);
	LocalizacionRetriever localizacionRetriever = new LocalizacionRetriever(
		idFinca);
	String tramo = localizacionRetriever.getKey(DBNames.FIELD_TRAMO_FINCAS);
	String uc = localizacionRetriever.getKey(DBNames.FIELD_UC_FINCAS);
	String ayuntamiento = localizacionRetriever
		.getKey(DBNames.FIELD_AYUNTAMIENTO_FINCAS);
	String parroquiaSubtramo = localizacionRetriever
		.getKey(DBNames.FIELD_PARROQUIASUBTRAMO_FINCAS);
	String numFinca = localizacionRetriever
		.getKey(DBNames.FIELD_NUMEROFINCA_FINCAS);
	String seccion = localizacionRetriever
		.getKey(DBNames.FIELD_SECCION_FINCAS);

	try {
	    DataSourceFactory dsf = desafecciones.getRecordset()
		    .getDataSourceFactory();
	    String sqlQuery = "select * from "
		    + desafecciones.getRecordset().getName() + " where "
		    + DBNames.FIELD_TRAMO_FINCAS + " = '" + tramo + "' and "
		    + DBNames.FIELD_UC_FINCAS + " = '" + uc + "' and "
		    + DBNames.FIELD_AYUNTAMIENTO_FINCAS + " = '" + ayuntamiento
		    + "' and " + DBNames.FIELD_PARROQUIASUBTRAMO_FINCAS
		    + " = '" + parroquiaSubtramo + "' and "
		    + DBNames.FIELD_NUMEROFINCA_FINCAS + " = '" + numFinca
		    + "' and " + DBNames.FIELD_SECCION_FINCAS + " = '"
		    + seccion + "';";
	    DataSource ds = dsf.executeSQL(sqlQuery, EditionEvent.ALPHANUMERIC);
	    ds.setDataSourceFactory(dsf);
	    SelectableDataSource sds = new SelectableDataSource(ds);
	    EditableAdapter ea = new EditableAdapter();
	    ea.setOriginalDataSource(sds);
	    return ea.getRecordset();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	} catch (DriverLoadException e) {
	    e.printStackTrace();
	    return null;
	} catch (ParseException e) {
	    e.printStackTrace();
	    return null;
	} catch (SemanticException e) {
	    e.printStackTrace();
	    return null;
	} catch (EvaluationException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public String getValue(String valueName) {
	return values.get(valueName);
    }
}
