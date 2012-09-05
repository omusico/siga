package es.icarto.gvsig.extgia;

import es.icarto.gvsig.navtableforms.validation.FormValidator;
import es.icarto.gvsig.navtableforms.view.AlphanumericFormView;

public class AlphanumericFormValidator extends FormValidator {

    @Deprecated
    public AlphanumericFormValidator() {
	super();
    }

    // TODO: cual es la mejor práctica. úna instancia del listener por
    // componente o una instancia para todos los componentes
    public AlphanumericFormValidator(AlphanumericFormView formView) {

    }

}
