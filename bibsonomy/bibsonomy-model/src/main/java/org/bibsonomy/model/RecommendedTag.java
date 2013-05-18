/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model;

import org.bibsonomy.common.exceptions.InvalidModelException;

/**
 * Adds scores and confidence to {@link Tag}.
 * 
 * @author rja
 * @version $Id: RecommendedTag.java,v 1.10 2011-04-29 06:45:05 bibsonomy Exp $
 */
public class RecommendedTag extends Tag {
	private static final long serialVersionUID = -1872430526599241544L;
	
	private double score;
	private double confidence;
	
	/**
	 * for bean-compatibility
	 */
	public RecommendedTag() {
	}
	
	/**
	 * sets name, score and confidence; validates tag name
	 * 
	 * @param name
	 * @param score
	 * @param confidence
	 */
	public RecommendedTag(final String name, final double score, final double confidence) {
		super(name);
		ensureValidTagName(name); // check validity of given name
		this.score = score;
		this.confidence = confidence;
	}
	
	/** Overriding {@link Tag#setName(String)} to check the validity of the
	 * given name for recommended tags.
	 * 
	 * @see org.bibsonomy.model.Tag#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		super.setName(name);
		ensureValidTagName(name); // check validity of given name
	}
	
	/**
	 * @return the score
	 */
	public double getScore() {
		return this.score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * @return the confidence
	 */
	public double getConfidence() {
		return this.confidence;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	/**
	 * Checks the validity of a given tag name. Currently, recommended tags
	 * must not contain whitespace.
	 * 
	 * This method should be called by all methods which set/change the tag name, 
	 * e.g., constructors and setters.
	 * 
	 * @param name - the tag name to be checked.
	 * @exception InvalidModelException - if the given tag name is not a valid name for a tag.
	 */
	private void ensureValidTagName(final String name) {
		if (name != null && name.matches(".*\\s.*")) 
			throw new InvalidModelException("recomended tags must not contain whitespace characters");
		/*
		 * XXX:
		 * originally, NULL was also rejected as tag name. However, the empty 
		 * constructor sets the name to NULL, so this did not work ...
		 */
	}
	
	
	/**
	 * Compares two recommended tags. Their equality is checked based on 
	 * their names - if they are equal (ignoring case!), the tags are 
	 * equal. Also, if {@link Tag#equals(Object)} is <code>true</code>,
	 * the tags are regarded equal.
	 * 
	 * @see org.bibsonomy.model.Tag#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object tag) {
		/*
		 * if tag is null or not a RecommendedTag, return false
		 */
	    if (!(tag instanceof RecommendedTag)) {
	    	return false;
	    }    
	    
	    /*
	     * cast
	     */
	    final RecommendedTag recTag = (RecommendedTag)tag;
	    
	    /*
	     * accept the super classes 'equals' method 
	     */
	    if (super.equals(recTag)) return true;
	    
	    /*
	     * ignore case
	     */
		return this.getName().equalsIgnoreCase((recTag).getName());
	}
	
	/** Fits to {@link #equals(Object)} ignoring case of tags.
	 * 
	 * @see org.bibsonomy.model.Tag#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getName().toLowerCase().hashCode();
	}
	
	@Override
	public String toString() {
		return super.toString() + " (score=" + score + ", confidence=" + confidence + ")";
	}
}
