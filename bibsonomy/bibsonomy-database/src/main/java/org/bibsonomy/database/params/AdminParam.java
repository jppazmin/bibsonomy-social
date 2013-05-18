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

import java.net.InetAddress;
import java.util.Date;

import org.bibsonomy.common.enums.InetAddressStatus;

/**
 * Holds parameters for admin specific things (e.g. blocking an IP, marking a
 * spammer).
 * 
 * @author Robert Jaeschke
 * @author Stefan Stützer
 * @version $Id: AdminParam.java,v 1.14 2010-04-09 09:04:39 nosebrain Exp $
 */
public class AdminParam {

	/** An inetAddress whose status should be get/set/deleted. */
	private InetAddress inetAddress = null;

	/** Status of the corresponding inetAddress */
	private InetAddressStatus inetAddressStatus;

	/** The name of the flagged user */
	private String userName;

	/** The spammer status of the user */
	private Boolean spammer;

	/** flag if user should by classified any longer */
	private Integer toClassify;

	/** The prediction of the classifier */
	private Integer prediction;
	
	/** The confidence of the classifier */
	private Double confidence;

	/** The classifier algorithm */
	private String algorithm;

	/** The mode of the classiefier (day or night) */
	private String mode;

	/** The group id of the posts before flagging */
	private int oldGroupId;

	/** The group id after flagging */
	private int newGroupId;

	/** The time of the update */
	private Date updatedAt;

	/** The username of the admin who executes the update */
	private String updatedBy;

	/** key for classifier settings */
	private String key;

	/** Corresponding value for classifier settings */
	private String value;

	/** Interval in hours for retrieve latest classifications */
	private int interval;
	
	/** Page limit for table entries */
	private int limit;
	
	/** Integer range for creating groups */
	private int groupRange; 
	
	/** String for describing the table in which to update a group Id **/
	private String groupIdTable;

	/**
	 * @return the inetAddress
	 */
	public InetAddress getInetAddress() {
		return this.inetAddress;
	}

	/**
	 * @param inetAddress the inetAddress to set
	 */
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	/**
	 * @return the inetAddressStatus
	 */
	public InetAddressStatus getInetAddressStatus() {
		return this.inetAddressStatus;
	}

	/**
	 * @param inetAddressStatus the inetAddressStatus to set
	 */
	public void setInetAddressStatus(InetAddressStatus inetAddressStatus) {
		this.inetAddressStatus = inetAddressStatus;
	}

	/**
	 * @return the spammer status
	 */
	public boolean isSpammer() {
		return this.spammer == null ? false : this.spammer;
	}

	/**
	 * @param spammer the spammer status to set
	 */
	public void setSpammer(Boolean spammer) {
		this.spammer = spammer;
	}

	/**
	 * @return the toClassify
	 */
	public Integer getToClassify() {
		return this.toClassify;
	}

	/**
	 * @param toClassify the toClassify to set
	 */
	public void setToClassify(Integer toClassify) {
		this.toClassify = toClassify;
	}

	/**
	 * @return the prediction
	 */
	public Integer getPrediction() {
		return this.prediction;
	}

	/**
	 * @param prediction the prediction to set
	 */
	public void setPrediction(Integer prediction) {
		this.prediction = prediction;
	}

	/**
	 * @return the confidence
	 */
	public Double getConfidence() {
		return this.confidence;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}

	/**
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return this.algorithm;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return this.mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the oldGroupId
	 */
	public int getOldGroupId() {
		return this.oldGroupId;
	}

	/**
	 * @param oldGroupId the oldGroupId to set
	 */
	public void setOldGroupId(int oldGroupId) {
		this.oldGroupId = oldGroupId;
	}

	/**
	 * @return the newGroupId
	 */
	public int getNewGroupId() {
		return this.newGroupId;
	}

	/**
	 * @param newGroupId the newGroupId to set
	 */
	public void setNewGroupId(int newGroupId) {
		this.newGroupId = newGroupId;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the interval
	 */
	public int getInterval() {
		return this.interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * @return the groupRange
	 */
	public int getGroupRange() {
		return this.groupRange;
	}

	/**
	 * @param groupRange the groupRange to set
	 */
	public void setGroupRange(int groupRange) {
		this.groupRange = groupRange;
	}

	/**
	 * @return the groupIdTable
	 */
	public String getGroupIdTable() {
		return this.groupIdTable;
	}

	/**
	 * @param groupIdTable the groupIdTable to set
	 */
	public void setGroupIdTable(String groupIdTable) {
		this.groupIdTable = groupIdTable;
	}
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return this.limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
}