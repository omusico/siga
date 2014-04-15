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



/**
 * @author gvSIG Team
 * @version $Id$
 *
 */
public class GPEKml2_2_Parser extends GPEKml2_1_Parser{
    
    public GPEKml2_2_Parser() {
        super();     
        qNameComparator = new Kml2_2_QNameComparator();
    }

    /*
     * (non-Javadoc)
     * @see org.gvsig.gpe.parser.GPEParser#getDescription()
     */
    public String getDescription() {
        return "This parser parses KML 2.2";
    }

    /*
     * (non-Javadoc)
     * @see org.gvsig.gpe.parser.GPEParser#getName()
     */
    public String getName() {
        return "KML v2.2";
    }

    /*
     * (non-Javadoc)
     * @see org.gvsig.gpe.parser.GPEParser#getFormat()
     */
    public String getFormat() {
        return "text/xml; subtype=kml/2.2";     
    }
}
