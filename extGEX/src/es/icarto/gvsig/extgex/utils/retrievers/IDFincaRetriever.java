package es.icarto.gvsig.extgex.utils.retrievers;

import javax.swing.JComboBox;

import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class IDFincaRetriever {

    private String idFinca;
    private JComboBox tramo;
    private JComboBox uc;
    private JComboBox ayuntamiento;
    private JComboBox subtramo;
    private JComboBox finca_seccion;

    public IDFincaRetriever(JComboBox tramo, 
	    JComboBox uc, 
	    JComboBox ayuntamiento,
	    JComboBox parroquia_subtramo,
	    JComboBox finca_seccion) {

	this.tramo = tramo;
	this.uc = uc;
	this.ayuntamiento = ayuntamiento;
	this.subtramo = parroquia_subtramo;
	this.finca_seccion = finca_seccion;
	idFinca = calculateIDFinca();
    }

    private String calculateIDFinca() {
	String idFinca = LocalizadorFormatter.getTramo(((KeyValue) tramo.getSelectedItem()).getKey())
		+ LocalizadorFormatter.getUC(((KeyValue) uc.getSelectedItem()).getKey()) 
		+ LocalizadorFormatter.getAyuntamiento(((KeyValue) ayuntamiento.getSelectedItem()).getKey());
	if(subtramo.getSelectedItem() instanceof KeyValue) {
	    idFinca = idFinca + LocalizadorFormatter.getSubtramo(
		    ((KeyValue) subtramo.getSelectedItem()).getKey());
	} else {
	    idFinca = idFinca + LocalizadorFormatter.getSubtramo("");
	}
	idFinca = idFinca + getStringNroFincaFormatted()
		+ getStringSeccionFormatted();
	return idFinca;
    }

    private String getStringNroFincaFormatted() {
	String fincaSeccionValue = (String) finca_seccion.getSelectedItem();
	String fincaValue = fincaSeccionValue.split("-")[0];
	return LocalizadorFormatter.getNroFinca(fincaValue);
    }

    private String getStringSeccionFormatted() {
	String fincaSeccionValue = (String) finca_seccion.getSelectedItem();
	return fincaSeccionValue.split("-")[1];
    }

    public String getIDFinca() {
	return idFinca;
    }

}
