package es.icarto.gvsig.extgia.forms.areas_servicio;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class AreasServicioCalculateIDValue extends CalculateComponentValue {

    public AreasServicioCalculateIDValue(AbstractForm form,
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
	String sentidoValue = "";

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
	JComboBox sentidoWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.SENTIDO);
	if (sentidoWidget.getItemCount()!=0) {
	    sentidoValue = ((KeyValue) sentidoWidget.
		    getSelectedItem()).getKey();
	}


	String areaServicioID = "";
	if (validate) {

	    areaServicioID = areaMantenimientoValue + baseContratistaValue + tramoValue + tipoViaValue +
		    municipioValue + sentidoValue;
	}
	resultComponent.setText(areaServicioID);
	form.getFormController().setValue(resultComponentName, areaServicioID);
    }

}

