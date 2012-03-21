/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 *
 */
package com.iver.cit.gvsig.project.documents;

/**
 * Interface que deben de cumplir una acción aplicable dede un Menu contextual o
 * popup. <br>
 * Las acciones se deben registrar en el punto de extensión adecuado * <br>
 * <br>
 * Por lo general extender de la clase AbstractDocumentAction
 * 
 * 
 * 
 * @author Jose Manuel Vivó (Chema)
 */

public interface IContextMenuAction {

    /**
     * Dice si la acción es visible segun los documentos seleccionados <br>
     * 
     * @param item
     *            elemento sobre el que se ha pulsado
     * @param selectedItems
     *            elementos seleccionados en el momento de pulsar
     * 
     */
    public boolean isVisible(Object item, Object[] selectedItems);

    /**
     * Dice si la acción esta habilitada segun los documentos seleccionados <br>
     * 
     * @param item
     *            elemento sobre el que se ha pulsado
     * @param selectedItems
     *            elementos seleccionados en el momento de pulsar
     * 
     */
    public boolean isEnabled(Object item, Object[] selectedItems);

    /**
     * Ejecuta la acción sobre los documentos seleccionados <br>
     * 
     * @param item
     *            elemento sobre el que se ha pulsado
     * @param selectedItems
     *            elementos seleccionados en el momento de pulsar
     * 
     */
    public void execute(Object item, Object[] selectedItems);

    /**
     * Nombre del grupo al que pertenece la accion
     */
    public String getGroup();

    /**
     * Orden del grupo al que pertenece la acción
     */
    public int getGroupOrder();

    /**
     * Orden del elemento dentro del grupo
     */
    public int getOrder();

    /**
     * Texto del elemento
     */
    public String getText();

    /**
     * Descripción mas detallada de la acción (se utilizará como Tooltip)
     */
    public String getDescription();

}
