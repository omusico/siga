/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib��ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

package org.gvsig.gui.beans.controls.label;

import java.awt.event.ActionListener;

import javax.swing.JLabel;

import org.gvsig.gui.beans.controls.IControl;

/**
 * @author Cesar Martinez Izquierdo <cesar.martinez@iver.es>
 *
 */
public class Label extends JLabel implements IControl {
	private static final long serialVersionUID = 1L;


	/* (non-Javadoc)
	 * @see org.gvsig.gui.beans.controls.IControl#addActionListener(java.awt.event.ActionListener)
	 */
	public void addActionListener(ActionListener listener) {
		// Labels doesn't have any action, so we discard actionListeners
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gui.beans.controls.IControl#removeActionListener(java.awt.event.ActionListener)
	 */
	public void removeActionListener(ActionListener listener) {
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.gui.beans.controls.IControl#setValue(java.lang.Object)
	 */
	public Object setValue(Object value) {
		try {
			String text = (String) value;
			setText(text);
			return text;
		}
		catch (ClassCastException ex) {
			return null;
		}
	}

}
