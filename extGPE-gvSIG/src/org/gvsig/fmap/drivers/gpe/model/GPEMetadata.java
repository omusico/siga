/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2011 Software Colaborativo (www.scolab.es)   development
 */

package org.gvsig.fmap.drivers.gpe.model;

import java.util.ArrayList;

public class GPEMetadata {
	private GPEMetadata parentData = null;
	private ArrayList dataList = new ArrayList();
	private String tagType = null;
	private String tagData = null;

	/**
	 * @return the tagType
	 */
	public String getTagType() {
		return this.tagType;
	}

	/**
	 * @param name
	 *            the tagType to set
	 */
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	/**
	 * @param name
	 *            the tag data to set
	 */
	public void setTagData(String tagData) {
		this.tagData = tagData;
	}

	/**
	 * @param name
	 *            the tag data to set
	 */
	public String getTagData() {
		return tagData;
	}

	/**
	 * @return the data list
	 */
	public ArrayList getDataList() {
		return dataList;
	}

	/**
	 * @return the metadata at position i
	 * @param i
	 *            Element position
	 */
	public GPEMetadata getElementAt(int i) {
		return (GPEMetadata) dataList.get(i);
	}

	/**
	 * @return the parent metadata
	 */
	public GPEMetadata getParentData() {
		return parentData;
	}

	/**
	 * @param parent
	 *            metadata the parentElement to set
	 */
	public void setParentData(Object parentData) {
		if (parentData != null) {
			this.parentData = (GPEMetadata) parentData;
			((GPEMetadata) parentData).addChildData(this);
		}
	}

	/**
	 * @param adds
	 *            a child metadata
	 */
	public void addChildData(GPEMetadata subData) {
		getDataList().add(subData);
	}

}
