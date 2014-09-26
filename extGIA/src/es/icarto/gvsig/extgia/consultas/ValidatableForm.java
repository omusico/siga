package es.icarto.gvsig.extgia.consultas;

import java.io.InputStream;
import java.util.Map;

import javax.swing.JComponent;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.commons.gui.AbstractIWindow;
import es.icarto.gvsig.navtableforms.FillHandler;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;

@SuppressWarnings("serial")
public abstract class ValidatableForm extends AbstractIWindow implements
	IValidatableForm {

    private final ORMLite ormlite;
    private final FillHandler fillHandler;
    private final Map<String, JComponent> widgets;
    protected FormPanel formPanel;
    private final IController mockController;
    private boolean fillingValues;

    public ValidatableForm() {
	ormlite = new ORMLite(getClass().getClassLoader()
		.getResource("rules/" + getBasicName() + ".xml").getPath());
	formPanel = getFormPanel();
	this.add(formPanel);
	widgets = AbeilleParser.getWidgetsFromContainer(formPanel);
	mockController = new MockController();
	fillHandler = new FillHandler(widgets, mockController,
		ormlite.getAppDomain());
	fillingValues = true;
	fillHandler.fillValues();
	fillingValues = false;

    }

    protected abstract String getBasicName();

    private FormPanel getFormPanel() {
	if (formPanel == null) {
	    InputStream stream = getClass().getClassLoader()
		    .getResourceAsStream("/forms/" + getBasicName() + ".jfrm");
	    try {
		formPanel = new FormPanel(stream);
	    } catch (FormException e) {
		e.printStackTrace();
	    }
	}
	return formPanel;
    }

    @Override
    public boolean isFillingValues() {
	return fillingValues;
    }

    @Override
    public IController getFormController() {
	return mockController;
    }

    @Override
    public void setChangedValues() {
    }

    @Override
    public FillHandler getFillHandler() {
	return fillHandler;
    }

    @Override
    public void validateForm() {
    }

    @Override
    public ValidatorForm getValidatorForm() {
	return null;
    }

    @Override
    public Map<String, JComponent> getWidgets() {
	return widgets;
    }

}
