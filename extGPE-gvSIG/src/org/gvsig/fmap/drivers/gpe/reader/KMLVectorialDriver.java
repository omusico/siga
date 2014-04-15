package org.gvsig.fmap.drivers.gpe.reader;

import java.util.ArrayList;

import org.gvsig.gpe.kml.parser.GPEKml2_2_Parser;
import org.gvsig.gpe.kml.parser.GPEKmz2_2_Parser;

import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.drivers.WithDefaultLegend;
import com.iver.cit.gvsig.fmap.rendering.ILegend;
import com.iver.cit.gvsig.fmap.rendering.LegendFactory;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.ILabelingStrategy;


/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ibáñez, 50
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
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class KMLVectorialDriver extends GPEVectorialDriver implements WithDefaultLegend {
	public static final String DRIVERNAME = "gvSIG KML Memory Driver";
	private ILegend legend;
	private ILabelingStrategy labelingStrategy;
	
	public void setDefaultLegend(ILegend legend) {
		this.legend = legend;
	}
	
	public void setDefaultLabelingStrategy(ILabelingStrategy labelStrategy) {
		this.labelingStrategy = labelStrategy;
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see com.hardcode.driverManager.Driver#getName()
	 */
	public String getName() {
		return DRIVERNAME;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.drivers.gpe.reader.IGPEDriver#getGPEParser()
	 */
	public ArrayList getGPEParsers() {
		ArrayList parsers = new ArrayList();
//		parsers.add(GPEKml2_2_Parser.class);
		parsers.add(GPEKmz2_2_Parser.class);
		return parsers;
	}

	public ILegend getDefaultLegend() {
		if (legend == null) {
			legend = LegendFactory.createSingleSymbolLegend(FShape.MULTI);
		}
		return legend;
	}

	public ILabelingStrategy getDefaultLabelingStrategy() {
		return labelingStrategy;
	}
}
