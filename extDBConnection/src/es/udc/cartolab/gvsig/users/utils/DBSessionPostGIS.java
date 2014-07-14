/*
 * Copyright (c) 2010. CartoLab, Universidad de A Coruña
 *
 * This file is part of extDBConnection
 *
 * extDBConnection is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * extDBConnection is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with extDBConnection.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.cartolab.gvsig.users.utils;

import com.iver.cit.gvsig.fmap.drivers.DBException;

/**
 * Created to avoid update DBConnection to upstream
 * 
 */
public class DBSessionPostGIS {

    public static DBSession createConnection(String server, int port,
	    String database, String schema, String username, String password)
	    throws DBException {
	return DBSession.createConnection(server, port, database, schema,
		username, password);
    }
}