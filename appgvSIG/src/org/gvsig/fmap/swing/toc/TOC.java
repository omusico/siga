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

import javax.swing.JComponent;

import org.gvsig.fmap.swing.toc.event.ActiveLayerChangeEventListener;
import org.gvsig.fmap.swing.toc.event.LayerActionEventListener;
import org.gvsig.fmap.swing.toc.event.LegendActionEventListener;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.service.Service;

import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.toc.impl.DefaultToc;

/**
 * This interface must be implemented by future TOC implementations.
 * This is used following the Tools  scheme (services, managers, etc)
 * 
 * @see DefaultToc
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public interface TOC extends Service {

    /**
     * Gets the parameters that were used to instantiate this TOC. Normally,
     * the @see MapContext
     * 
     * @return a dynobject containing the parameters
     */
    public DynObject getParameters();

    /**
     * @return the TOCFactory that created this TOC.
     */
    public TOCFactory getFactory();

    /**
     * 
     * @return the Swing component that represents this TOC (for example a JTree
     * or a JPanel with a JTree in it) 
     */
    public JComponent getComponent();

    /**
     * 
     * @return the MapContext on which this TOC is necessarily based
     */
    public MapContext getMapContext();

    /**
     * Expands fully the graphic representation of the TOC (for example, expands all
     * the nodes to show all layer details) 
     */
    public void showAll();

    /**
     * Hides as much as possible the graphic representation of the TOC (for example, collapses all
     * the nodes to hide all layer details except the list of layers itself) 
     */
    public void hideAll();

    /**
     * Expands all graphic representation of layer legends
     */
    public void showAllLegends();

    /**
     * Collapses all graphic representation of layer legends
     */
    public void hideAllLegends();

    /**
     * Makes the name and representation of a certain layer visible in the TOC.
     * For example, the TOC might be a JTree inside a scroll pane. This method would
     * scroll down the scroll pane to let the user see the layer is listed
     * 
     * @param lyr the layer whose name and details must be made visible
     * @param detailed whether the layer details must be shown or not
     * 
     */
    public void show(FLayer lyr, boolean detailed);

    /**
     * Makes layer legend details visible
     * 
     * @param lyr layer whose legend details must be shown
     */
    public void showLegend(FLayer lyr);

    /**
     * Makes layer legend details not visible
     * 
     * @param lyr layer whose legend details must be hidden
     */
    public void hideLegend(FLayer lyr);

    /**
     * Add a layer to the set of selected layers
     * @param lyr layer which must be selected (not exclusively)
     */
    public void selectLayer(FLayer lyr);

    /**
     * Removes a layer from the set of selected layers
     * @param lyr layer which must be unselected
     */
    public void unselectLayer(FLayer lyr);

    /**
     * Clears current layer selection
     */
    public void clearSelection();

    /**
     * 
     * @return array of selected layers
     */
    public FLayer[] getSelectedLayers();

    /**
     * Sets a layer as active (exclusively: the number of active layers must be 0 or 1)
     * @param lyr layer to be set as active or null to have 0 layers active
     */
    public void setActiveLayer(FLayer lyr);

    /**
     * 
     * @return the currently active layer or null if no layer is active
     */
    public FLayer getActiveLayer();

    /**
     * Invokes a certain action on a certain layer
     * @param lyr the layer on which the action will be performed
     * @param action the action to be performed 
     */
    public void invokeAction(FLayer lyr, String action);

    /**
     * Adds a legend action listener. These listeners will be notified when
     * the user shows interest in a legend symbol (typically double click)
     * @param lel new legend action listener
     */
    public void addLegendActionListener(LegendActionEventListener lel);

    /**
     * Removes a legend action listener
     * @param lel legend action listener to be removed
     */
    public void removeLegendActionListener(LegendActionEventListener lel);

    /**
     * Adds a layer action listener. These listeners will be notified when
     * the user shows interest in a layer (typically double click)
     * @param lel new layer action listener
     */
    public void addLayerActionListener(LayerActionEventListener lel);

    /**
     * Removes a layer action listener
     * @param lel layer action listener to be removed
     */
    public void removeLayerActionListener(LayerActionEventListener lel);

    /**
     * Adds a active layer change listener. These listeners will be notified when
     * a new layer is set as active
     * @param alcel new active layer change listener
     */
    public void addActiveLayerChangeListener(
        ActiveLayerChangeEventListener alcel);

    /**
     * Removes a active layer change listener
     * @param alcel active layer change listener to be removed
     */
    public void removeActiveLayerChangeListener(
        ActiveLayerChangeEventListener alcel);

}
