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
package com.iver.cit.gvsig.project.documents.view.toc.gui;

import org.gvsig.fmap.swing.toc.TOCFactory;

/**
 * Utility class to create items in the TOC implementation combo box
 * (gvsig settings)
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class TocImplementationComboItem {

    /**
     * the instance represented by this combo item
     */
    private TOCFactory tocFactory = null;

    /**
     * 
     * @param tofa the TOCFactory represented by this combo item
     */
    public TocImplementationComboItem(TOCFactory tofa) {
        tocFactory = tofa;
    }

    @Override
    public String toString() {
        return tocFactory.getName();
    }

    public TOCFactory getTOCFactory() {
        return tocFactory;
    }

}
