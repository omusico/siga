package org.gvsig.gpe.kml.warnings;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import org.gvsig.exceptions.BaseException;

/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 * $Id: VersionNotFoundWarning.java 38 2007-04-12 10:21:52Z jorpiell $
 * $Log$
 * Revision 1.1  2007/04/12 10:21:52  jorpiell
 * Add the writers
 *
 * Revision 1.1  2007/03/07 08:19:10  jorpiell
 * Pasadas las clases de KML de libGPE-GML a libGPE-KML
 *
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * Añadidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2007/02/12 13:49:18  jorpiell
 * A�adido el driver de KML
 *
 *
 */
/**
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 */
public class VersionNotFoundWarning extends BaseException{
	private static final long serialVersionUID = -7929656757664427123L;
	private File m_File = null;
	
	public VersionNotFoundWarning() {		
		init();		
	}
	
	public VersionNotFoundWarning(Throwable exception) {
		init();
		initCause(exception);
	}
	
	private void init() {
		messageKey = "warning_kml_versionNotFound";
		formatString = "The lm� version number is not specified";
		code = serialVersionUID;		
	}

	protected Map values() {
		return new Hashtable();
	}
}
