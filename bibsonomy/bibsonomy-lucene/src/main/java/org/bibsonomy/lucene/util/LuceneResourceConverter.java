/**
 *
 *  BibSonomy-Lucene - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.lucene.util;

import static org.bibsonomy.lucene.util.LuceneBase.CFG_FLDINDEX;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_FLDSTORE;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_FULLTEXT_FLAG;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_LIST_DELIMITER;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_LUCENENAME;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_PRIVATE_FLAG;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_TYPEHANDLER;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_MERGEDFIELDS;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_PRIVATEFIELDS;
import static org.bibsonomy.util.ValidationUtils.present;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.bibsonomy.lucene.param.LucenePost;
import org.bibsonomy.lucene.param.typehandler.LuceneTypeHandler;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.util.tex.TexDecode;

/**
 * class for converting bibsonomy post model objects to lucene documents
 * 
 * @author fei
 * @version $Id: LuceneResourceConverter.java,v 1.7 2010-07-16 12:12:00 nosebrain Exp $
 * 
 * @param <R> the resource to convert
 */
public abstract class LuceneResourceConverter<R extends Resource> {
	private static final Log log = LogFactory.getLog(LuceneResourceConverter.class);

	/** property map for configuring and mapping post model properties to lucene fields and vice versa */
	private Map<String,Map<String, Object>> postPropertyMap;
	
	/**
	 * read property values from given lucene document and creates post model
	 * 
	 * @param doc
	 * @return the post representation of the lucene document
	 */
	public Post<R> writePost(final Document doc) {
		// initialize 
		final Post<R> post = this.createEmptyPost();

		// cycle though all properties and store the corresponding
		// values in the content hash map
		for (String propertyName : postPropertyMap.keySet()) {
			// get lucene index properties
			String fieldName   = (String)postPropertyMap.get(propertyName).get(CFG_LUCENENAME);
			String propertyStr = doc.get(fieldName); 
			
			if( !present(propertyStr) )
				continue;
			
			@SuppressWarnings("unchecked")
			final LuceneTypeHandler<Object> typeHandler = (LuceneTypeHandler<Object>)postPropertyMap.get(propertyName).get(CFG_TYPEHANDLER);

			Object propertyValue = null;
			if( typeHandler!=null ) {
				propertyValue = typeHandler.setValue(propertyStr);
			} else
				propertyValue = propertyStr;

			try {
				PropertyUtils.setNestedProperty(post, propertyName, propertyValue);
			} catch (Exception e) {
				log.error("Error setting property " + propertyName + " to " + propertyValue.toString(), e);
			}
		}
		
		// all done.
		return post;
	}
	
	/**
	 * read property values from given object as defined in given propertyMap
	 * 
	 * @param post
	 * @return the lucene document representation of the post
	 */
	public Document readPost(final Post<R> post) {
		final Document retVal = new Document();
		// FIXME: default values should be configured via spring
		Index fldDefaultIndex = Field.Index.NOT_ANALYZED;
		Store fldDefaultStore = Field.Store.YES;
		Index fldIndex = fldDefaultIndex;
		Store fldStore = fldDefaultStore;

		// all fields are concatenated for full text search
		String mergedField = "";
		
		// all private fields are concatenated for full text search
		String privateField= "";
		
		//--------------------------------------------------------------------
		// cycle though all properties and store the corresponding
		// values in the content hash map
		//--------------------------------------------------------------------
		for( String propertyName : postPropertyMap.keySet() ) {
			//----------------------------------------------------------------
			// retrieve property value from post object
			//----------------------------------------------------------------
			// extract property value from object
			Object property = null;
			String propertyValue = "";
			try {
				// get property from post object
				property = PropertyUtils.getProperty(post, propertyName);
				// only handle non-null values
				if( property!=null ) {
					@SuppressWarnings("unchecked")
					LuceneTypeHandler<Object> typeHandler  = (LuceneTypeHandler<Object>)postPropertyMap.get(propertyName).get(CFG_TYPEHANDLER);
					
					// get property value
					propertyValue = extractPropertyValue(postPropertyMap, typeHandler, propertyName, property);
					
					// get lucene index configuration
					final Index index = (Index) postPropertyMap.get(propertyName).get(CFG_FLDINDEX);
					if( index != null) {
						fldIndex = index;
					} else {
						fldIndex = fldDefaultIndex;
					}
					if( postPropertyMap.get(propertyName).get(CFG_FLDSTORE) != null) {
						fldStore = (Store) postPropertyMap.get(propertyName).get(CFG_FLDSTORE);
					} else {
						fldStore = fldDefaultStore;
					}
				}
			} catch (Exception e) {
				log.error("Error reading property '"+propertyName+"' from post object.", e);
			}
			//----------------------------------------------------------------
			// add property to the lucene document
			//----------------------------------------------------------------
			String luceneName = (String)postPropertyMap.get(propertyName).get(CFG_LUCENENAME);
			// FIXME: configure default value field wise via spring
			String defaultValue = "";
			if( (propertyValue!=null) && (luceneName!=null) && (!"".equals(propertyValue.trim())) ) {
				// add field to the lucene document
				retVal.add( new Field(luceneName, propertyValue, fldStore, fldIndex));
				// add term to full text search field, if configured accordingly 
				if( present(postPropertyMap.get(propertyName).get(CFG_FULLTEXT_FLAG)) &&
				    (Boolean)postPropertyMap.get(propertyName).get(CFG_FULLTEXT_FLAG) ) {
					mergedField += CFG_LIST_DELIMITER + propertyValue;
				}
				// add term to private full text search field, if configured accordingly 
				if( present(postPropertyMap.get(propertyName).get(CFG_PRIVATE_FLAG)) &&
				    (Boolean)postPropertyMap.get(propertyName).get(CFG_PRIVATE_FLAG) ) {
					privateField += CFG_LIST_DELIMITER + propertyValue;
				}
			} else {
				// add empty field
				retVal.add( new Field(luceneName, defaultValue, fldStore, fldIndex));
			}
		}
		
		// store merged field
		retVal.add(new Field(FLD_MERGEDFIELDS, decodeTeX(mergedField), Field.Store.NO, Field.Index.ANALYZED));

		// store private field
		retVal.add(new Field(FLD_PRIVATEFIELDS, decodeTeX(privateField), Field.Store.YES, Field.Index.ANALYZED));
		
		// all done.
		return retVal;
	}

	/**
	 * extracts property value from given object
	 * 
	 * @param bibTexPropertyMap TODO: unused
	 * @param propertyName TODO: unused
	 * @param item
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private String extractPropertyValue(Map<String, Map<String, Object>> bibTexPropertyMap, LuceneTypeHandler<Object> typeHandler, String propertyName, Object item) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String itemValue = null;
		// get the string value for the given object
		if( typeHandler != null ) {
			// if a type handler is set, use the type handler for rendering the string
			itemValue = typeHandler.getValue(item);
		} else {
			// if no type handler is set use Object.toString()
			itemValue = item.toString();
		}
		return itemValue;
	}
	
	/**
	 * decode bibtex characters to corresponding utf8 characters
	 * 
	 * @param input
	 * @return
	 */
	private String decodeTeX(String input) {
		String mergedFieldUTF8 = null; 
		try {
			mergedFieldUTF8 = TexDecode.decode(input);
		} catch (IllegalStateException e) {
			mergedFieldUTF8 = input;
			log.debug("Error decoding TeX-string '"+input+"'");
		}
		return mergedFieldUTF8;
	}
	
	private Post<R> createEmptyPost() {
		final R resource = this.createNewResource();
		final User user = new User();
		final Post<R> post = new LucenePost<R>();
		post.setResource(resource);
		post.setUser(user);
		post.getResource().recalculateHashes();
		return post;
	}
	
	protected abstract R createNewResource();

	/**
	 * @param postPropertyMap the postPropertyMap to set
	 */
	public void setPostPropertyMap(Map<String,Map<String,Object>> postPropertyMap) {
		this.postPropertyMap = postPropertyMap;
	}

	/**
	 * @return the postPropertyMap
	 */
	public Map<String,Map<String,Object>> getPostPropertyMap() {
		return this.postPropertyMap;
	}
	
}
