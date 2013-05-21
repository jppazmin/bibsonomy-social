/**
 *
 *  BibSonomy-Recommender - Various methods to provide recommendations for BibSonomy
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

package org.bibsonomy.recommender.tags.database.params;

/**
 * Parameter class for storing recommender results in database
 * @author fei
 * @version $Id: SelectorTagParam.java,v 1.1 2009-03-26 14:55:06 folke Exp $
 */
public class SelectorTagParam {
	private Long qid;           /// query id
	private Long rid;           /// settings id
	private Long latency;
	private Double score;
	private Double confidence;
	private String tagName;
	
	public void setQid(long qid) {
		this.qid = qid;
	}
	public long getQid() {
		return qid;
	}
	public void setRid(long sid) {
		this.rid = sid;
	}
	public long getRid() {
		return rid;
	}
	public void setLatency(long latency) {
		this.latency = latency;
	}
	public long getLatency() {
		return latency;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getScore() {
		return score;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagName() {
		return tagName;
	}
}
