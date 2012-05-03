package es.icarto.gvsig.extgex.utils.retrievers;

import java.util.ArrayList;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class FincaSeccionRetriever {

    private String xmlFilePath;
    private ArrayList<String> values;
    private ArrayList<String> fksNumeroFinca;
    private ArrayList<String> fksSeccion;

    private String tramoKey;
    private String ucKey;
    private String ayuntamientoKey;
    private String subtramoKey;

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
    }

    public ArrayList<String> getValues(){
	return values;
    }

    private ArrayList<String> retrieveValues(){
	setFKNumeroFinca();
	ArrayList<String> values = new ArrayList<String>();
	DomainValues dvNumeroFinca = ORMLite.getAplicationDomainObject(xmlFilePath)
		.getDomainValuesForComponent("numero_finca");
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
	DomainValues dvSeccion = ORMLite.getAplicationDomainObject(xmlFilePath)
		.getDomainValuesForComponent("seccion");
	fksSeccion.clear();
	fksSeccion.add(tramoKey);
	fksSeccion.add(ucKey);
	fksSeccion.add(ayuntamientoKey);
	fksSeccion.add(subtramoKey);
	fksSeccion.add(finca);
	return dvSeccion.getValuesFilteredBy(fksSeccion);
    }

}
