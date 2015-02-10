package com.hardcode.gdbms.engine.values;

import java.sql.Types;

public class BlankValue extends AbstractValue {

    @Override
    public int doHashCode() {
	return 0;
    }

    @Override
    public String getStringValue(ValueWriter writer) {
	return "BLANK";
    }

    @Override
    public int getSQLType() {
	return Types.OTHER;
    }

    @Override
    public int getWidth() {
	return 0;
    }
    
    public String toString() {
	return "BLANK";
    }

}
