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

import java.util.ArrayList;

import javax.swing.tree.TreePath;

import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;

/**
 * Utility class with Event related methods. Used during user interaction
 * with TOC GUI
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class EventUtil {

    /**
     * Returns a list of layers whose visibility property
     * is affected by a change in the scale
     * 
     * @param root root layer
     * @param scalea from scale
     * @param scaleb to scale 
     * @return
     */
    public static ArrayList getLayersToRefreshDueToScaleLimits(FLayers root,
        double scalea, double scaleb) {

        // =========== discard rare cases ============
        if (scalea == scaleb) {
            return new ArrayList();
        }
        if ((scalea != 0) && (scaleb != 0)) {
            double f = scalea / scaleb;
            f = Math.abs(1d - f);
            if (f < 0.00001) {
                return new ArrayList();
            }
        }
        // ===========================================

        if (scaleLimitsResultChanges(root, scalea, scaleb)) {
            ArrayList aux = getAllLayers(root, false);
            return aux;
        }

        ArrayList resp = new ArrayList();

        int sz = root.getLayersCount();
        FLayer lyr = null;

        for (int i = 0; i < sz; i++) {
            lyr = root.getLayer(i);
            if (lyr instanceof FLayers) {
                if (scaleLimitsResultChanges(lyr, scalea, scaleb)) {
                    ArrayList aux = getAllLayers((FLayers) lyr, false);
                    resp.addAll(aux);
                } else {
                    ArrayList aux =
                        getLayersToRefreshDueToScaleLimits((FLayers) lyr,
                            scalea, scaleb);
                    resp.addAll(aux);
                }
            } else {
                if (scaleLimitsResultChanges(lyr, scalea, scaleb)) {
                    resp.add(lyr);
                }
            }
        }
        return resp;
    }

    /**
     * Returns list with all layers.
     * 
     * @param lyrs root layer
     * @param act_only whether or not only active layers must be considered
     * @return
     */
    public static ArrayList getAllLayers(FLayers lyrs, boolean act_only) {

        FLayer lyr = null;
        int sz = lyrs.getLayersCount();

        ArrayList list = new ArrayList();
        if (compliesActive(lyrs, act_only)) {
            list.add(lyrs);
        }

        for (int i = 0; i < sz; i++) {
            lyr = lyrs.getLayer(i);
            if (lyr instanceof FLayers) {
                ArrayList aux = getAllLayers((FLayers) lyr, act_only);
                list.addAll(aux);
            } else {
                if (compliesActive(lyr, act_only)) {
                    list.add(lyr);
                }
            }
        }

        return list;
    }

    private static boolean compliesActive(FLayer lyr, boolean act_only) {

        return (act_only && lyr.isActive()) || (!act_only);
    }

    private static boolean scaleLimitsResultChanges(FLayer lyr, double scalea,
        double scaleb) {

        boolean a = lyr.isWithinScale(scalea);
        boolean b = lyr.isWithinScale(scaleb);
        return (a && !b) || (b && !a);
    }

    /**
     * Gets path until input layer is reached
     * 
     * @param plyr target layer
     * @param root tree root
     * @return
     */
    public static TreePath getPath(FLayer plyr, Object root) {

        ArrayList list = new ArrayList();
        FLayer aux = plyr;
        while (aux != null) {
            list.add(aux);
            aux = aux.getParentLayer();
        }

        // check root
        int sz = list.size();
        if (list.get(sz - 1) != root) {
            // top parent and root not equal
            return null;
        }

        Object[] arr = new Object[sz];
        for (int i = 0; i < sz; i++) {
            arr[i] = list.get(sz - i - 1);
        }
        TreePath tp = new TreePath(arr);
        return tp;
    }

}
