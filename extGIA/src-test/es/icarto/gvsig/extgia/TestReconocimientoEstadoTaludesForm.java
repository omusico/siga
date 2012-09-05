package es.icarto.gvsig.extgia;

public class TestReconocimientoEstadoTaludesForm extends
	CommonMethodsForTestForms {

    @Override
    protected String getAbeilleForm() {
	return ReconocimientoEstadoTaludesForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getSchema() {
	return "audasa_extgia";
    }

    @Override
    protected String getTable() {
	return "taludes_reconocimiento_estado";
    }

}
