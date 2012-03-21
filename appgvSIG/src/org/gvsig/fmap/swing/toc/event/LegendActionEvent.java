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
package org.gvsig.fmap.swing.toc.event;

import org.gvsig.fmap.swing.toc.TOC;

import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.rendering.ILegend;

/**
 * Event class to notify that user has shown interest in one of the
 * sample symbols of a legend
 * (typically user double clicked on it)
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class LegendActionEvent extends TOCEvent {

    private ILegend legend = null;
    private ISymbol symbol = null;

    /**
     * 
     * @param t TOC where action was performed
     * @param sym symbol on which user performed action
     * @param leg legend that contains that symbol
     */
    public LegendActionEvent(TOC t, ISymbol sym, ILegend leg) {
        super(t);
        symbol = sym;
        legend = leg;
    }

    /**
     * 
     * @return the affected legend
     */
    public ILegend getLegend() {
        return legend;
    }

    /**
     * 
     * @return the symbol on which user performed an action
     */
    public ISymbol getSymbol() {
        return symbol;
    }

}
