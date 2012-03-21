package com.iver.cit.gvsig.fmap.operations.arcview;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.data.persistence.Memento;
import com.hardcode.gdbms.engine.data.persistence.MementoException;
import com.hardcode.gdbms.engine.data.persistence.OperationLayerMemento;
import com.hardcode.gdbms.engine.strategies.OperationDataSource;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class ArcJoinDataSource extends OperationDataSource {
	private DataSource source;
	private DataSource linked;
	private int[] relation;
	private int linkFieldindex;
	public static String prefix = "j_";
	private boolean bIsOpen = false;

	/**
	 * DOCUMENT ME!
	 *
	 * @param result
	 * @param source
	 * @param linked DOCUMENT ME!
	 * @param linkFieldindex DOCUMENT ME!
	 */
	public ArcJoinDataSource(int[] result, DataSource source,
		DataSource linked, int linkFieldindex) {
		this.relation = result;
		this.source = source;
		this.linked = linked;
		this.linkFieldindex = linkFieldindex;
	}

	/**
	 * @throws ReadDriverException
	 * @see com.hardcode.gdbms.engine.data.DataSource#start()
	 */
	public void start() throws ReadDriverException {
		// Fjp: Tenemos un problema cuando hacemos join con tablas de base de datos, ya que no podemos
		// estar abriendo y cerrando continuamente el datasource (muy lento, se cuelga gvSIG).
		// Lo correcto sería quitar de una vez el AUTOMATIC_DATASOURCE y dejarlo en MANUAL, pero mientras
		// tanto habrá que parchear esto, y tener las tablas de un join abiertas.
		// Creo que esto funciona porque al pedir valores ya hace cada tabla su start.
		// TODO: Comprobar que al hacer el stop no estamos otra vez con el mismo problema. Lo ideal sería
		// que no se cerrara y abriera el recordset cada vez que llamamos a getFieldValue, o getRecordCount, etc.
		if (bIsOpen == false) {
			source.start();
			linked.start();
			bIsOpen = true;
		}
	}

	/**
	 * @see com.hardcode.gdbms.engine.data.DataSource#stop()
	 */
	public void stop() throws ReadDriverException {
//		source.stop();
//		linked.stop();
	}

	/**
	 * @see com.hardcode.gdbms.engine.data.FieldNameAccess#getFieldIndexByName(java.lang.String)
	 */
	public int getFieldIndexByName(String fieldName) throws ReadDriverException {
        for (int i = 0; i < getFieldCount(); i++) {
            if (getFieldName(i).equals(fieldName)){
                return i;
            }
        }

        return -1;
    }

	/**
	 * @see com.hardcode.gdbms.engine.data.ReadDriver#getFieldValue(long, int)
	 */
	public Value getFieldValue(long rowIndex, int fieldId)
		throws ReadDriverException {
		if (fieldId < source.getFieldCount()) {
			return source.getFieldValue(rowIndex, fieldId);
		}
		fieldId = fieldId - source.getFieldCount();

		if (fieldId >= linkFieldindex) {
			fieldId++;
		}

		int index = relation[(int) rowIndex];

		if (index == -1) {
			return ValueFactory.createNullValue();
		}

		return linked.getFieldValue(index, fieldId);
	}

	/**
	 * @see com.hardcode.gdbms.engine.data.ReadDriver#getFieldCount()
	 */
	public int getFieldCount() throws ReadDriverException {
		return (source.getFieldCount() + linked.getFieldCount()) - 1;
	}

	/**
	 * @see com.hardcode.gdbms.engine.data.ReadDriver#getFieldName(int)
	 */
	public String getFieldName(int fieldId) throws ReadDriverException {
		if (fieldId < source.getFieldCount()) {
			return source.getFieldName(fieldId);
		}
		fieldId = fieldId - source.getFieldCount();

		if (fieldId >= linkFieldindex) {
			fieldId++;
		}

		return prefix + linked.getFieldName(fieldId);
	}

	/**
	 * @see com.hardcode.gdbms.engine.data.ReadDriver#getRowCount()
	 */
	public long getRowCount() throws ReadDriverException {
		return source.getRowCount();
	}

	/**
	 * @see com.hardcode.gdbms.engine.data.ReadDriver#getFieldType(int)
	 */
	public int getFieldType(int i) throws ReadDriverException {
		if (i < source.getFieldCount()) {
			return source.getFieldType(i);
		}
		i = i - source.getFieldCount();

		if (i >= linkFieldindex) {
			i++;
		}

		return linked.getFieldType(i);
	}

	/**
	 * @see com.hardcode.gdbms.engine.data.DataSource#getMemento()
	 */
	public Memento getMemento() throws MementoException {
		return new OperationLayerMemento(getName(),
				new Memento[]{source.getMemento(), linked.getMemento()}, getSQL());
	}

	public int getFieldWidth(int i) throws ReadDriverException {
		if (i < source.getFieldCount()) {
			return source.getFieldWidth(i);
		}
		i = i - source.getFieldCount();

		if (i >= linkFieldindex) {
			i++;
		}

		return linked.getFieldWidth(i);
	}

    public boolean isVirtualField(int fieldId) throws ReadDriverException  {
		if (fieldId < source.getFieldCount()) {
			return source.isVirtualField(fieldId);
		}
		fieldId = fieldId - source.getFieldCount();

		if (fieldId >= linkFieldindex) {
			fieldId++;
		}

		return linked.isVirtualField(fieldId);
    }
}
