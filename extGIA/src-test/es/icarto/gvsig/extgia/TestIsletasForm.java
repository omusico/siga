package es.icarto.gvsig.extgia;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.Ignore;
import org.junit.Test;

import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.isletas.IsletasForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;
import es.icarto.gvsig.navtableforms.utils.LayerController;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TestIsletasForm extends CommonMethodsForTestDBForms {

	@Override
    protected String getAbeilleForm() {
	return IsletasForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getSchema() {
	return "audasa_extgia";
    }

    @Override
    protected String getTable() {
	return "isletas";
    }
    
    @Override
	protected String getXmlFile() {
		return "rules/isletas.xml";
	}
    
    @Ignore
    @Test
    public void test_writeValues() throws Exception {
	HashMap<String, String> validValues = getValidValues();
	FLyrVect layer = (FLyrVect) DBSession.getCurrentSession().getLayer(
		"layerNameInTocNoMathers", getTable(), getSchema(), "",
		CRSFactory.getCRS("EPSG:23030"));

	IsletasForm dialog = new IsletasForm(layer);
	dialog.init();
	LayerController formController = dialog.getFormController();
	HashMap<String, JComponent> myWidgets = dialog.getWidgetComponents();

	for (JComponent c : myWidgets.values()) {
	    String name = c.getName();
	    String value = validValues.get(name);
	    if (value == null) {
		System.out.println("un widget al que no he asignado valor: "
			+ name);
		continue;
	    }
	    if (c instanceof JFormattedTextField) {
		((JFormattedTextField) c).setText(value);
		formController.setValue(name, value);
	    } else if (c instanceof JTextField) {
		((JTextField) c).setText(value);
		formController.setValue(name, value);
	    } else if (c instanceof JComboBox) {
		// ((JComboBox) c).setSelectedItem(value);
		// formController.setValue(name, value);
	    } else if (c instanceof JCheckBox) {
		boolean selected = value.equals("true");
		((JCheckBox) c).setSelected(selected);
		formController.setValue(name, value);
	    } else if (c instanceof JTextArea) {
		((JTextArea) c).setText(value);
		formController.setValue(name, value);
	    }
	}

	dialog.setChangedValues();
	dialog.saveRecord();
	dialog.refreshGUI();

	for (String name : validValues.keySet()) {
	    assertEquals(name, validValues.get(name), dialog
		    .getFormController().getValueInLayer(name));
	}

    }

    // test_comboboxWidgetHasADomainValue
    // test_comboboxWidgetsWithOutDomain
    // test_widgetWithOutValidationRule

    // test lanzar los valid values directamente contra la base de datos
    private HashMap<String, String> getValidValues() {
	HashMap<String, String> validValues = new HashMap<String, String>();
	validValues.put("id_isleta", "D-584N");
	validValues.put("numero_isleta", "584");
	validValues.put("area_mantenimiento", "1");
	validValues.put("base_contratista", "2");
	validValues.put("tramo", "1");
	validValues.put("tipo_via", "1");
	validValues.put("nombre", "Test");
	validValues.put("pk_inicial", "1");
	validValues.put("ramal", "Entrada");
	validValues.put("direccion", "0");
	validValues.put("pk_final", "2");
	validValues.put("sentido", "Creciente");
	validValues.put("margen", "Derecho");
	validValues.put("tipo_isleta", "Nariz");
	validValues.put("superficie_bajo_bionda", "21");
	validValues.put("posibilidad_empleo_vehiculos", "No");
	validValues.put("fecha_actualizacion", "01/01/2012");
	validValues.put("observaciones", "Muchísimas observaciones");

	return validValues;
    }

    
}
