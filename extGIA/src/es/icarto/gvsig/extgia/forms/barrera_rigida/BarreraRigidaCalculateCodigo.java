package es.icarto.gvsig.extgia.forms.barrera_rigida;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class BarreraRigidaCalculateCodigo extends CalculateComponentValue {

    public BarreraRigidaCalculateCodigo(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String tipoCode = "";
	String metodoCode = "";
	String perfilCode = "";

	if (validate) {
	    JComboBox tipo = (JComboBox) operatorComponents.get(DBFieldNames.TIPO);
	    String tipoValue = ((KeyValue) tipo.getSelectedItem()).getValue();
	    JComboBox metodo = (JComboBox) operatorComponents.get(DBFieldNames.METODO_CONSTRUCTIVO);
	    String metodoValue = ((KeyValue) metodo.getSelectedItem()).getValue();
	    JComboBox perfil = (JComboBox) operatorComponents.get(DBFieldNames.PERFIL);
	    String perfilValue = ((KeyValue) perfil.getSelectedItem()).getValue();

	    if (tipoValue.equals("Barrera Hormigón Simple")) {
		tipoCode = "BHS";
	    } else if (tipoValue.equals("Barrera Hormigón Doble")) {
		tipoCode = "BHD";
	    }

	    if (metodoValue.equals("Hormigonada \"in situ\"")) {
		metodoCode = "E";
	    } else if (metodoValue.equals("Prefabricada")) {
		metodoCode = "P";
	    } else if (metodoValue.equals("Hormigonada con molde")) {
		metodoCode = "X";
	    }

	    if (perfilValue.equals("New Jersey")) {
		perfilCode = "J";
	    } else if (perfilValue.equals("Perfil \"F\"")) {
		perfilCode = "F";
	    } else if (perfilValue.equals("Perfil \"Tric-Bloc\"")) {
		perfilCode = "T";
	    }

	}
	resultComponent.setText(tipoCode+metodoCode+perfilCode);
	form.getFormController().setValue(resultComponentName, tipoCode+metodoCode+perfilCode);
    }
}
