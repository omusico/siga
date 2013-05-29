package es.icarto.gvsig.navtableforms;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.widgetsdependency.DependencyReader;
import es.icarto.gvsig.navtableforms.ormlite.widgetsdependency.EnabledComponentBasedOnWidget;

public class DependencyHandler {

    private ORMLite ormlite;
    private HashMap<String, JComponent> widgets;
    private IValidatableForm form;

    public DependencyHandler(ORMLite ormlite,
	    HashMap<String, JComponent> widgetsVector, IValidatableForm form) {
	this.ormlite = ormlite;
	this.widgets = widgetsVector;
	this.form = form;
    }

    public void setListeners() {
	for (JComponent comp : widgets.values()) {
	    if (ormlite.getAppDomain().getDependencyValuesForComponent(
		    comp.getName()) != null) {
		DependencyReader values = ormlite.getAppDomain()
			.getDependencyValuesForComponent(comp.getName());
		EnabledComponentBasedOnWidget componentBasedOnWidget = new EnabledComponentBasedOnWidget(
			widgets.get(values.getComponent()), comp,
			values.getValue(), form);
		componentBasedOnWidget.setRemoveDependentValues(true);
		componentBasedOnWidget.setListeners();
	    }
	    if (ormlite.getAppDomain().isNonEditableComponent(comp.getName()) != null
		    && ormlite.getAppDomain().isNonEditableComponent(
			    comp.getName())) {
		widgets.get(comp.getName()).setEnabled(false);
	    }
	}
    }

    public void removeListeners() {
	for (JComponent comp : widgets.values()) {
	    if (ormlite.getAppDomain().getDependencyValuesForComponent(
		    comp.getName()) != null) {
		DependencyReader values = ormlite.getAppDomain()
			.getDependencyValuesForComponent(comp.getName());
		EnabledComponentBasedOnWidget componentBasedOnWidget = new EnabledComponentBasedOnWidget(
			widgets.get(values.getComponent()), comp,
			values.getValue(), form);
		componentBasedOnWidget.removeListeners();
	    }
	}
    }

    public void fillValues() {
	for (JComponent comp : widgets.values()) {
	    if (ormlite.getAppDomain().getDependencyValuesForComponent(
		    comp.getName()) != null) {
		DependencyReader values = ormlite.getAppDomain()
			.getDependencyValuesForComponent(comp.getName());
		EnabledComponentBasedOnWidget componentBasedOnWidget = new EnabledComponentBasedOnWidget(
			widgets.get(values.getComponent()), comp,
			values.getValue(), form);
		componentBasedOnWidget.fillValues();
	    }
	}
    }

}