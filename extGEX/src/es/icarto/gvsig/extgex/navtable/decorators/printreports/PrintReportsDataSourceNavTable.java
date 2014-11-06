package es.icarto.gvsig.extgex.navtable.decorators.printreports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class PrintReportsDataSourceNavTable implements JRDataSource {

    private int colIndex = -1;
    private SelectableDataSource sds;
    private int currentPosition = -1;

    public PrintReportsDataSourceNavTable() {
    }

    public void prepareDataSource(String layerName, long currentPosition) {
	this.currentPosition = (int) currentPosition;
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(layerName);
	try {
	    sds = layer.getRecordset();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public boolean next() throws JRException {
	try {
	    colIndex++;
	    if (colIndex >= sds.getFieldCount()) {
		return false;
	    } else {
		return true;
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
	try {
	    Value valueField = sds.getFieldValue(currentPosition, colIndex);
	    String nameField = sds.getFieldName(colIndex);

	    Object value = null;
	    String fieldName = field.getName();

	    if ("nombre".equals(fieldName)) {
		value = nameField;
	    } else if ("valor".equals(fieldName)) {
		value = valueField.toString();
	    }
	    return value;

	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}

    }

}
