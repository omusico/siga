package es.icarto.gvsig.extgia.consultas;

import java.util.HashMap;

import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;

import es.udc.cartolab.gvsig.navtable.dataacces.IController;

public class MockController implements IController {

    @Override
    public long create(HashMap<String, String> newValues) throws Exception {
	return 0;
    }

    @Override
    public void read(long position) throws ReadDriverException {
    }

    @Override
    public void update(long position) throws ReadDriverException,
	    StopWriterVisitorException {
    }

    @Override
    public void delete(long position) throws StopWriterVisitorException,
	    InitializeWriterException, StartWriterVisitorException,
	    ReadDriverException {
    }

    @Override
    public void clearAll() {
    }

    @Override
    public int getIndex(String fieldName) {
	return 0;
    }

    @Override
    public int[] getIndexesOfValuesChanged() {
	return null;
    }

    @Override
    public String getValue(String fieldName) {
	return null;
    }

    @Override
    public String getValueInLayer(String fieldName) {
	return null;
    }

    @Override
    public HashMap<String, String> getValues() {
	return null;
    }

    @Override
    public HashMap<String, String> getValuesOriginal() {
	return null;
    }

    @Override
    public HashMap<String, String> getValuesChanged() {
	return null;
    }

    @Override
    public void setValue(String fieldName, String value) {
    }

    @Override
    public int getType(String fieldName) {
	return 0;
    }

    @Override
    public long getRowCount() throws ReadDriverException {
	return 0;
    }

}
