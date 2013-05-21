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

import java.sql.Timestamp;
import java.util.List;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;

/**
 * Parameter class for recommender queries.
 * @author fei
 * @version $Id: RecQueryParam.java,v 1.2 2009-07-21 11:26:44 folke Exp $
 */
public class RecQueryParam {
	private Long qid;
	/** ID for mapping posts to recommender queries */
	private int pid;
	/** content typ: 1 for bookmar, 2 for bibtex */
	private Integer contentType;
	private String userName;
	private Timestamp timeStamp;
	/** querie's timeout value */
	private int queryTimeout;
	private Post<? extends Resource> post;
	private List<RecommendedTag> tags;
	private List<RecommendedTag> preset;
	
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setQid(long qid) {
		this.qid = qid;
	}
	public long getQid() {
		return qid;
	}
	public void setPost(Post<? extends Resource> post) {
		this.post = post;
	}
	public Post<? extends Resource> getPost() {
		return post;
	}
	public void setTags(List<RecommendedTag> tags) {
		this.tags = tags;
	}
	public List<RecommendedTag> getTags() {
		return tags;
	}
	public void setPreset(List<RecommendedTag> preset) {
		this.preset = preset;
	}
	public List<RecommendedTag> getPreset() {
		return preset;
	}
	public void setContentType(Integer content_type) {
		this.contentType = content_type;
	}
	public Integer getContentType() {
		return contentType;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getPid() {
		return pid;
	}
	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}
	public int getQueryTimeout() {
		return queryTimeout;
	}
}
