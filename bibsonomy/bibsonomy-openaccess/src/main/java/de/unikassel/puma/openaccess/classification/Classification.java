/**
 *
 *  BibSonomy-OpenAccess - Check Open Access Policies for Publications
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

package de.unikassel.puma.openaccess.classification;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Classification {
	
	private final String delimiter;
	
	private final String className;
	
	private final  LinkedHashMap<String , ClassificationObject> classifications;
	
	public Classification(String className, LinkedHashMap<String , ClassificationObject> classifications, String delimiter) {
		this.delimiter = delimiter;
		this.className = className;
		this.classifications = classifications;
	}
	
	public String getClassName() {
		return className;
	}
	
	private String getNextToken(String actualToken) {
		if(present(delimiter)) {
			int delimiter = actualToken.indexOf('.') +1;
			
			if(delimiter != 0) {
				return actualToken.substring(0, delimiter);
			} else {
				return actualToken;
			}
		} else {
			return actualToken.charAt(0) +"";
		}
	}
	
	private String getRestToken(String token) {
		if(present(delimiter)) {
			int delimiter = token.indexOf('.') +1;
			
			if(delimiter != 0) {
				return token.substring(delimiter, token.length());
			} else {
				return "";
			}
		} else {
			return token.substring(1);
		}
	}
	
	public final List<PublicationClassification> getChildren(String name) {
		ArrayList<PublicationClassification> erg = new ArrayList<PublicationClassification>();
		
		String actual, tempName = name;
		
		LinkedHashMap<String , ClassificationObject> children = classifications;
		ClassificationObject actualObject = null;
		
		while(!tempName.isEmpty()) {			
			actual = getNextToken(tempName);
			tempName = getRestToken(tempName);
			actualObject = children.get(actual);
			
			children = actualObject.getChildren();
		}

		Set<String> keys = children.keySet();
		for(String s : keys) {
			PublicationClassification co = new PublicationClassification(s, getDescription(name +s));
			erg.add(co);
		}
		return erg;
	}
	
	public String getDescription(String name) {
		String actual = getNextToken(name);
		name = getRestToken(name);
		
		LinkedHashMap<String , ClassificationObject> children = classifications;
		ClassificationObject actualObject = null;
		
		while(!children.isEmpty()) {
			if(!actual.isEmpty()) {
				actualObject = children.get(actual);
			} else {
				
				if(present(actualObject)) {
					return actualObject.getDescription();
				} else {
					return "";
				}
			}
			
			if(!name.isEmpty()) {
				actual = getNextToken(name);
				name = getRestToken(name);
			} else {
				actual = "";
			}
			
			if(present(actualObject))
				children = actualObject.getChildren();
		}
		if(present(actualObject)) {
			return actualObject.getDescription();
		} else {
			return "";
		}
	}

	
	
	

}
