/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 * Revision 1.5  2007-08-14 11:10:20  jvidal
 * javadoc updated
 *
 * Revision 1.4  2007/08/07 11:21:42  jvidal
 * javadoc
 *
 * Revision 1.3  2007/05/08 15:44:07  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2007/04/04 16:01:14  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/09 11:25:00  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.4  2007/02/21 07:35:14  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.3  2007/02/08 15:43:04  jaume
 * some bug fixes in the editor and removed unnecessary imports
 *
 * Revision 1.1.2.2  2007/01/30 18:10:10  jaume
 * start commiting labeling stuff
 *
 * Revision 1.1.2.1  2007/01/26 13:49:03  jaume
 * *** empty log message ***
 *
 *
 */
package com.iver.cit.gvsig.gui.styling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Vector;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.core.SymbologyFactory;
import com.iver.cit.gvsig.fmap.core.styles.IStyle;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.xmlEntity.generate.XmlTag;

/**
 * Implements a list to select styles.This list
 * has the property that allows the user to stablish a filter to accept or reject
 * elements for it from a directory which is also specified when the StyleSelectorModel
 * is created.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class StyleSelectorListModel extends SymbolSelectorListModel {

	public static final String STYLE_FILE_EXTENSION = ".style";

	/**
	 * <p>
	 * Creates a new instance of the model for the list in the Style Selector window
	 * where the styles are stored in the <b>dir</b> (root directory) param.<br>
	 * </p>
	 * <p>The <b>currentElement</b> defines which element is pre-selected.<br></p>
	 * <p>The <b>filter</b> is a user defined filter used to know which elements in
	 * the folder are accepted or rejected for this list and it is used to avoid
	 * mixing marker styles for polygons for example.<br></p>
	 * <p><b>fileExtension</b> param defines the extension of the file to be parsed. This
	 * is like that to enable inheritance of this class to other file selector such
	 * as StyleSelector.
	 *
	 * @param dir, the root dir where styles are located.
	 * @param currentElemet, the element to be pre-selected.
	 * @param filter, the filter used to show or hide some elements.
	 * @param fileExtension, file extension used for the files to be parsed.
	 */
	public StyleSelectorListModel(File dir, SelectorFilter filter, String fileExtension) {
		super(dir, filter, fileExtension);
		// TODO Auto-generated constructor stub
	}

	public Vector getObjects() {
		if (elements == null) {
			elements = new Vector();

			File[] ff = dir.listFiles(ffilter);
			for (int i = 0; i < ff.length; i++) {

				XMLEntity xml;
				try {
					xml = new XMLEntity((XmlTag) XmlTag.unmarshal(new FileReader(ff[i])));
					IStyle sty = SymbologyFactory.createStyleFromXML(xml, ff[i].getName());
					if (sfilter.accepts(sty))
						add(sty);
				} catch (MarshalException e) {
					NotificationManager.
					addWarning("Error in file ["+ff[i].getAbsolutePath()+"]. " +
							"File corrupted! Skiping it...", e);
				} catch (ValidationException e) {
					NotificationManager.
					addWarning("Error validating style file ["+ff[i].getAbsolutePath()+"].", e);
				} catch (FileNotFoundException e) {
					// unreachable code, but anyway...
					NotificationManager.
					addWarning("File not found: "+ ff[i].getAbsolutePath(), e);
				}

			}
		}
		return elements;
	}


	public void add(Object o) {
		TreeSet map = new TreeSet(new Comparator() {

			public int compare(Object o1, Object o2) {

				IStyle sym1 = (IStyle) o1;
				IStyle sym2 = (IStyle) o2;
				if (sym1.getDescription() == null && sym2.getDescription() != null) return -1;
				if (sym1.getDescription() != null && sym2.getDescription() == null) return 1;
				if (sym1.getDescription() == null && sym2.getDescription() == null) return 1;

				int result = sym1.getDescription().compareTo(sym2.getDescription());
				return (result!=0) ? result: 1; /* this will allow adding symbols with
				the same value for description than
				a previous one. */
			}

		});

		map.addAll(elements);
		map.add(o);
		elements = new Vector(map);

	}
}
