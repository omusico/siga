/*
Copyright (C) 2013-2014  Cartolab. (Universade da Coruña)
Copyright (C) 2014 iCarto

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.icarto.gvsig.commons.gui;

import javax.swing.table.DefaultTableModel;

/**
 * OnlyOneColumnEditable is used to made just one column of the JTables editable
 * and the rest non editable.
 * 
 * @author Francisco Puga <fpuga@cartolab.es>
 * 
 */
@SuppressWarnings("serial")
public class OnlyOneColumnEditable extends DefaultTableModel {

    private final int editableColumn;

    public OnlyOneColumnEditable(int editableColumn) {
	super();
	this.editableColumn = editableColumn;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
	return col == editableColumn;
    }
}
