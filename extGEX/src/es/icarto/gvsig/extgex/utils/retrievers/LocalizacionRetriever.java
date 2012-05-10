package es.icarto.gvsig.extgex.utils.retrievers;

import java.util.ArrayList;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.preferences.PreferencesPage;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class LocalizacionRetriever {

    private KeyValue tramo;
    private KeyValue uc;
    private KeyValue ayuntamiento;
    private KeyValue parroquia_subtramo;
    private KeyValue nro_finca;
    private KeyValue seccion;

    public LocalizacionRetriever(String idFinca) {
	decipher(idFinca);
    }

    private void decipher(String idFinca) {
	tramo = new KeyValue();
	uc = new KeyValue();
	ayuntamiento = new KeyValue();
	parroquia_subtramo = new KeyValue();
	nro_finca = new KeyValue();
	seccion = new KeyValue();

	/*
	 * id_finca = tramo [0:2] + uc [2:5] + ayuntamiento [5:6] +
	 * parroquia_subtramo [6:7] + nro_finca [7:11] + seccion [11:]
	 */
	tramo.setKey(idFinca.substring(0, 2)); // 2 chars
	uc.setKey(idFinca.substring(2, 5)); // 3 chars
	ayuntamiento.setKey(idFinca.substring(5, 6)); // 1 chars
	parroquia_subtramo.setKey(idFinca.substring(6, 7)); // 1 chars
	nro_finca.setKey(idFinca.substring(7, 11)); // 4 chars
	seccion.setKey(idFinca.substring(11)); // the rest (commonly 2 chars)

	// set values
	tramo.setValue(getTramoValue());
	uc.setValue(getUCValue());
	ayuntamiento.setValue(getAyuntamientoValue());
	parroquia_subtramo.setValue(getParroquiaSubtramoValue());
	// key and value are the same in this case
	nro_finca.setValue(idFinca.substring(7, 11));
	seccion.setValue(idFinca.substring(11));
    }

    private String getTramoValue() {
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent(DBNames.FIELD_TRAMO);
	ArrayList<KeyValue> kvs = dv.getValues();
	String value = null;
	for (KeyValue kv : kvs) {
	    if (kv.getKey().equalsIgnoreCase(tramo.getKey())) {
		value = kv.getValue();
	    }
	}
	return value;
    }

    private String getUCValue() {
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent(DBNames.FIELD_UC);
	ArrayList<String> foreignKeys = new ArrayList<String>();
	foreignKeys.add(tramo.getKey());
	ArrayList<KeyValue> kvs = dv.getValuesFilteredBy(foreignKeys);
	String value = null;
	for (KeyValue kv : kvs) {
	    if (kv.getKey().equalsIgnoreCase(uc.getKey())) {
		value = kv.getValue();
	    }
	}
	return value;
    }

    private String getAyuntamientoValue() {
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent(DBNames.FIELD_AYUNTAMIENTO);
	ArrayList<String> foreignKeys = new ArrayList<String>();
	foreignKeys.add(uc.getKey());
	ArrayList<KeyValue> kvs = dv.getValuesFilteredBy(foreignKeys);
	String value = null;
	for (KeyValue kv : kvs) {
	    if (kv.getKey().equalsIgnoreCase(ayuntamiento.getKey())) {
		value = kv.getValue();
	    }
	}
	return value;
    }

    private String getParroquiaSubtramoValue() {
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent(DBNames.FIELD_PARROQUIASUBTRAMO);
	ArrayList<String> foreignKeys = new ArrayList<String>();
	foreignKeys.add(uc.getKey());
	foreignKeys.add(ayuntamiento.getKey());
	ArrayList<KeyValue> kvs = dv.getValuesFilteredBy(foreignKeys);
	String value = null;
	for (KeyValue kv : kvs) {
	    if (kv.getKey().equalsIgnoreCase(parroquia_subtramo.getKey())) {
		value = kv.getValue();
	    }
	}
	return value;
    }

    public String getValue(String component) {
	if (component.equalsIgnoreCase(DBNames.FIELD_TRAMO)) {
	    return tramo.getValue();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_UC)) {
	    return uc.getValue();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_AYUNTAMIENTO)) {
	    return ayuntamiento.getValue();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_PARROQUIASUBTRAMO)) {
	    return parroquia_subtramo.getValue();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_NUMFINCA)) {
	    return nro_finca.getValue();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_SECCION)) {
	    return seccion.getValue();
	} else {
	    return null;
	}
    }

    public String getKey(String component) {
	if (component.equalsIgnoreCase(DBNames.FIELD_TRAMO)) {
	    return tramo.getKey();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_UC)) {
	    return uc.getKey();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_AYUNTAMIENTO)) {
	    return ayuntamiento.getKey();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_PARROQUIASUBTRAMO)) {
	    return parroquia_subtramo.getKey();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_NUMFINCA)) {
	    return nro_finca.getKey();
	} else if (component.equalsIgnoreCase(DBNames.FIELD_SECCION)) {
	    return seccion.getKey();
	} else {
	    return null;
	}
    }

    private String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.extgex")
		.getClassLoader()
		.getResource(PreferencesPage.XML_ORMLITE_RELATIVE_PATH)
		.getPath();
    }
}