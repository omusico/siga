package es.icarto.gvsig.extgia.forms.enlaces;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class EnlacesCalculateIDValue extends CalculateComponentValue {

    public EnlacesCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String baseContratistaValue = "";
	String tramoValue = "";
	String tipoViaValue = "";
	String municipioValue = "";

	JComboBox areaMantenimientoWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.AREA_MANTENIMIENTO);
	String areaMantenimientoValue = ((KeyValue) areaMantenimientoWidget.
		getSelectedItem()).getKey();
	JComboBox baseContratistaWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.BASE_CONTRATISTA);
	if (baseContratistaWidget.getItemCount()!=0) {
	    baseContratistaValue = ((KeyValue) baseContratistaWidget.
		    getSelectedItem()).getKey();
	}
	JComboBox tramoWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.TRAMO);
	if (tramoWidget.getItemCount()!=0) {
	    tramoValue = ((KeyValue) tramoWidget.
		    getSelectedItem()).getKey();
	}
	JComboBox tipoViaWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.TIPO_VIA);
	if (tipoViaWidget.getItemCount()!=0) {
	    tipoViaValue = ((KeyValue) tipoViaWidget.
		    getSelectedItem()).getKey();
	}
	JComboBox municipioWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.MUNICIPIO);
	if (municipioWidget.getItemCount()!=0) {
	    municipioValue = ((KeyValue) municipioWidget.
		    getSelectedItem()).getKey();
	}
	JTextField pkWidget = (JTextField) operatorComponents
		.get(DBFieldNames.PK);
	String pkValue = pkWidget.getText();
	String[] pkValues = pkValue.split("\\.");
	String pkValueFormated = "";
	if (pkValues[1].length() == 1) {
	    pkValueFormated = String.format("%03d", Integer.valueOf(pkValues[0])) +
		    pkValues[1] + "00";
	}else if (pkValues[1].length() == 2) {
	    pkValueFormated = String.format("%03d", Integer.valueOf(pkValues[0])) +
		    pkValues[1] + "0";
	}else {
	    pkValueFormated = String.format("%03d", Integer.valueOf(pkValues[0])) +
		    pkValues[1];
	}

	if (pkWidget.getText().isEmpty()) {
	    validate = false;
	}

	String enlaceID = "";
	if (validate) {

	    enlaceID = areaMantenimientoValue + baseContratistaValue + tramoValue + tipoViaValue +
		    municipioValue + pkValueFormated;
	}
	resultComponent.setText(enlaceID);
	form.getFormController().setValue(resultComponentName, enlaceID);
    }

}
