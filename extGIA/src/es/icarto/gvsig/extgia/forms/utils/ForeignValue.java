package es.icarto.gvsig.extgia.forms.utils;

public class ForeignValue {
    
    String component;
    String value;
    
    public ForeignValue(String component, String value) {
	this.component = component;
	this.value = value;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
