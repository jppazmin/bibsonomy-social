/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.database.params;

import java.util.Date;

import org.bibsonomy.common.enums.ConceptStatus;

/**
 * @author Jens Illig
 * @version $Id: TagRelationParam.java,v 1.3 2010-07-02 11:45:50 nosebrain Exp $
 */
public class TagRelationParam extends GenericParam {

	private Integer relationId;
	private String lowerTagName;
	private String upperTagName;
	private Date creationDate;
	private String ownerUserName;
	private ConceptStatus conceptStatus;

	/**
	 * @return creationDate
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * @param creationDate
	 */
	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return lowerTagName
	 */
	public String getLowerTagName() {
		return this.lowerTagName;
	}

	/**
	 * @param lowerTagName
	 */
	public void setLowerTagName(final String lowerTagName) {
		this.lowerTagName = lowerTagName;
	}

	/**
	 * @return ownerUserName
	 */
	public String getOwnerUserName() {
		return this.ownerUserName;
	}

	/**
	 * @param ownerUserName
	 */
	public void setOwnerUserName(final String ownerUserName) {
		this.ownerUserName = ownerUserName;
	}

	/**
	 * @return relationId
	 */
	public Integer getRelationId() {
		return this.relationId;
	}

	/**
	 * @param relationId
	 */
	public void setRelationId(final Integer relationId) {
		this.relationId = relationId;
	}

	/**
	 * @return upperTagName
	 */
	public String getUpperTagName() {
		return this.upperTagName;
	}

	/**
	 * @param upperTagName
	 */
	public void setUpperTagName(final String upperTagName) {
		this.upperTagName = upperTagName;
	}

	/**
	 * @return conceptStatus
	 */
	public ConceptStatus getConceptStatus() {
		return this.conceptStatus;
	}

	/**
	 * @param conceptStatus
	 */
	public void setConceptStatus(final ConceptStatus conceptStatus) {
		this.conceptStatus = conceptStatus;
	}
}