package es.icarto.gvsig.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.users.utils.IFormatter;

public class SIGAFormatter implements IFormatter {
    private final NumberFormat numberFormat = DoubleFormatNT
	    .getDisplayingFormat();

    @Override
    public String toString(Object o) {
	if (o == null) {
	    return "";
	}
	if ((o instanceof Double) || (o instanceof Float)
		|| (o instanceof BigDecimal)) {
	    return numberFormat.format(o);
	}
	return o.toString();
    }

}
