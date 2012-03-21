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
package org.gvsig.fmap.swing.toc;

import org.gvsig.tools.locator.AbstractLocator;

/**
 * Locator for TOC management following the org.gvsig.tools scheme of locators managers, etc
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class TOCLocator extends AbstractLocator {

    private static TOCLocator tocLocator = null;
    public static final String NAME = "TOC Locator";

    private TOCLocator() {
    }

    /**
     * 
     * @return gets singleton-style instance of locator
     */
    public static TOCLocator getInstance() {
        if (tocLocator == null) {
            tocLocator = new TOCLocator();
        }
        return tocLocator;
    }

    /**
     * Registers new TOC manager as the default TOC manager
     * 
     * @param clazz class of the  new TOC manager
     */
    public static void registerDefaultTOCManager(Class clazz) {
        getInstance().registerDefault("gvsig.toc.default.manager",
            "gvSIG TOC manager", clazz);
    }

    public String getLocatorName() {
        return NAME;
    }

    /**
     * 
     * @return current TOC manager
     */
    public TOCManager getTOCManager() {
        return (TOCManager) getInstance().get("gvsig.toc.default.manager");
    }

}
