package es.icarto.gvsig.extgex.utils.retrievers;

import java.util.ArrayList;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class FincaSeccionRetriever {

    private final String xmlFilePath;
    private final ArrayList<String> values;
    private final ArrayList<String> fksNumeroFinca;
    private final ArrayList<String> fksSeccion;

    private final String tramoKey;
    private final String ucKey;
    private final String ayuntamientoKey;
    private final String subtramoKey;

    private final ORMLite orm;

    public FincaSeccionRetriever(String xmlFilePath,
	    String tramoKey,
	    String ucKey,
	    String ayuntamientoKey,
	    String subtramoKey) {

	this.xmlFilePath = xmlFilePath;

	fksSeccion = new ArrayList<String>();
	fksNumeroFinca = new ArrayList<String>();
	this.tramoKey = tramoKey;
	this.ucKey = ucKey;
	this.ayuntamientoKey = ayuntamientoKey;
	this.subtramoKey = subtramoKey;

	this.values = retrieveValues();
	orm = new ORMLite(xmlFilePath);
    }

    public ArrayList<String> getValues(){
	return values;
    }

    private ArrayList<String> retrieveValues(){
	setFKNumeroFinca();
	ArrayList<String> values = new ArrayList<String>();
	DomainValues dvNumeroFinca = orm.getAppDomain().getDomainValuesForComponent("numero_finca");
	ArrayList<KeyValue> numerosFinca = dvNumeroFinca.getValuesFilteredBy(fksNumeroFinca);
	for(KeyValue numeroFinca : numerosFinca) {
	    ArrayList<KeyValue> seccionValues = getSeccionValues(numeroFinca.getKey());
	    for(KeyValue seccion : seccionValues) {
		if(!values.contains(numeroFinca.getKey() + "-" + seccion.getKey())) {
		    values.add(numeroFinca.getKey() + "-" + seccion.getKey());
		}
	    }
	}
	return values;
    }

    private void setFKNumeroFinca() {
	fksNumeroFinca.clear();
	fksNumeroFinca.add(tramoKey);
	fksNumeroFinca.add(ucKey);
	fksNumeroFinca.add(ayuntamientoKey);
	fksNumeroFinca.add(subtramoKey);
    }

    private ArrayList<KeyValue> getSeccionValues(String finca){
	DomainValues dvSeccion = orm.getAppDomain().getDomainValuesForComponent("seccion");
	fksSeccion.clear();
	fksSeccion.add(tramoKey);
	fksSeccion.add(ucKey);
	fksSeccion.add(ayuntamientoKey);
	fksSeccion.add(subtramoKey);
	fksSeccion.add(finca);
	return dvSeccion.getValuesFilteredBy(fksSeccion);
    }

}
