package com.iver.cit.gvsig.fmap.rendering;

import com.hardcode.gdbms.engine.values.Value;

public interface IInterval {

	/**
	 * Devuelve "true" si el double que se pasa como par�metro esta dentro del
	 * intervalo.
	 *
	 * @param check dou�l��a comprobar.
	 *
	�* @return "true" si est� dentro del intervalo.
	 */
	public abstract boolean isInInterval(Value v);

	/**
	 * Devuelve un string con la informaci�n de inicio y final.
	 *
	 * @return String.
	 */
	public abstract String toString();

}