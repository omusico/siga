package es.icarto.gvsig.extgia.forms.utils;

import javax.swing.JComponent;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorComponent;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorDomain;

public class ValidatorComponentFactory {

    public static ValidatorComponent createValidator(JComponent c,
	    ORMLite ormLite) {
	ValidatorDomain dv = ormLite.getAppDomain()
		.getDomainValidatorForComponent(c.getName());
	if (dv == null) {
	    return null;
	}
	return new ValidatorComponent(c, dv);
    }

}
