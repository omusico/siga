package es.icarto.gvsig.extgex.utils.retrievers;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueWriter;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class PositionRetriever {

    private FLyrVect layer;
    private String fieldName;
    private String fieldValue;
    private int position;

    public PositionRetriever(FLyrVect layer, String fieldName, String fieldValue) {
	this.layer = layer;
	this.fieldName = fieldName;
	this.fieldValue = fieldValue;
	position = calculatePosition();
    }

    public int getPosition() {
	return position;
    }

    private int calculatePosition() {
	try {
	    SelectableDataSource sds = layer.getRecordset();
	    int index = sds.getFieldIndexByName(fieldName);
	    for (int i=0; i<sds.getRowCount(); i++) {
		Value value = sds.getFieldValue(i, index);
		String strValue = 
			value.getStringValue(ValueWriter.internalValueWriter);
		strValue = strValue.replace("'", "").trim();
		if(strValue.equals(fieldValue)) {
		    return i;
		}
	    }
	    return AbstractNavTable.EMPTY_REGISTER;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return AbstractNavTable.EMPTY_REGISTER;
	}
    }

}
