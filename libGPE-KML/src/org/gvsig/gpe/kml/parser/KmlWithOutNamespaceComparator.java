/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */
package org.gvsig.gpe.kml.parser;

import javax.xml.namespace.QName;

import org.gvsig.gpe.xml.utils.AbstractQNameComparator;

/**
 * @author gvSIG Team
 * @version $Id$
 * 
 */
public class KmlWithOutNamespaceComparator extends AbstractQNameComparator {

	public boolean equals(QName qname1, QName qname2) {
		if (qname1 != null) {
			if (qname2 == null) {
				return false;
			}
			return qname1.getLocalPart().equals(qname2.getLocalPart());
		}
		return false;
	}
}
