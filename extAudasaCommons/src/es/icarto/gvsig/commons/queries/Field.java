package es.icarto.gvsig.commons.queries;

import java.text.Collator;
import java.util.ArrayList;

public class Field implements Comparable<Field> {

    private String column;
    private String description;
    private ArrayList<String> foreignKeys = new ArrayList<String>();

    public Field() {
    }

    public Field(String key, String value, String fk) {
	this.column = key;
	this.description = value;
	this.foreignKeys.add(fk);
    }

    public Field(String key, String value, ArrayList<String> fk) {
	this.column = key;
	this.description = value;
	this.foreignKeys = fk;
    }

    public Field(String key, String value) {
	this.column = key;
	this.description = value;
    }

    public String getKey() {
	return this.column;
    }

    public void setKey(String key) {
	this.column = key;
    }

    public void addForeignKey(String d) {
	this.foreignKeys.add(d);
    }

    public ArrayList<String> getForeignKeys() {
	return this.foreignKeys;
    }

    public String getValue() {
	return this.description;
    }

    public void setValue(String value) {
	this.description = value;
    }

    @Override
    public String toString() {
	return this.description;
    }

    /**
     * Returns true if obj is a KeyValue object with the same key and value
     * fields, or if obj is a string equals to the value field
     */
    @Override
    public boolean equals(Object obj) {
	if (obj instanceof String) {
	    return getValue().equals(obj);
	} else if (obj instanceof Field) {
	    Field kvObj = (Field) obj;
	    return getValue().equals(kvObj.getValue())
		    && getKey().equals(kvObj.getKey());
	}
	return false;
    }

    @Override
    public int compareTo(Field o) {
	Collator usCollator = Collator.getInstance();
	usCollator.setStrength(Collator.IDENTICAL);
	return usCollator.compare(description, o.description);
    }

}