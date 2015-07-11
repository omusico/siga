package es.icarto.gvsig.extgia.consultas;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;
import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.commons.gui.AbstractIWindow;
import es.icarto.gvsig.navtableforms.FillHandler;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

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
	for (JComponent c : getWidgets().values()) {
	    if (c instanceof JDateChooser) {
		SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
		((JDateChooser) c).setDateFormatString(dateFormat.toPattern());
		((JDateChooser) c).getDateEditor().setEnabled(false);
	    }
	}
	mockController = new MockController();
	fillHandler = new FillHandler(widgets, mockController,
		ormlite.getAppDomain());
	fillingValues = true;
	fillHandler.fillValues();
	fillingValues = false;

    }

    protected abstract String getBasicName();

    public FormPanel getFormPanel() {
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

    /**
     * Instead of create an implementation of ImageHandler that only sets a path (FixedImageHandler) this utiliy method
     * sets the image without doing anything more
     * @param imgComponent
     *            . Name of the abeille widget
     * @param absPath
     *            . Absolute path to the image or relative path from andami.jar
     */
    protected void addImageHandler(String imgComponent, String absPath) {
	ImageComponent image = (ImageComponent) formPanel
		.getComponentByName(imgComponent);
	ImageIcon icon = new ImageIcon(absPath);
	image.setIcon(icon);
    }

}
