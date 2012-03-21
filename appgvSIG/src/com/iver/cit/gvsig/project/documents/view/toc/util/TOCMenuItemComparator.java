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
package com.iver.cit.gvsig.project.documents.view.toc.util;

import java.text.NumberFormat;
import java.util.Comparator;

import org.gvsig.fmap.swing.toc.action.TOCAction;

/**
 * Comparator used Used to decide TOC menu item order.
 * Orders by groud ID then ID inside group
 *  
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class TOCMenuItemComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        return this.compareHere((TOCAction) o1, (TOCAction) o2);
    }

    private int compareHere(TOCAction o1, TOCAction o2) {

        NumberFormat formater = NumberFormat.getInstance();
        formater.setMinimumIntegerDigits(3);

        Integer o1_o = (Integer) o1.getValue(TOCAction.ORDER);
        Integer o2_o = (Integer) o2.getValue(TOCAction.ORDER);
        Integer o1_go = (Integer) o1.getValue(TOCAction.GROUP_ORDER);
        Integer o2_go = (Integer) o2.getValue(TOCAction.GROUP_ORDER);

        String o1_g = (String) o1.getValue(TOCAction.GROUP);
        String o2_g = (String) o2.getValue(TOCAction.GROUP);

        String key1 =
            "" + formater.format(o1_go.intValue()) + o1_g
                + formater.format(o1_o);
        String key2 =
            "" + formater.format(o2_go.intValue()) + o2_g
                + formater.format(o2_o);
        return key1.compareTo(key2);
    }

}
