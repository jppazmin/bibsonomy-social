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

package org.bibsonomy.lucene.index.analyzer;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.TokenStream;
import org.bibsonomy.lucene.util.LuceneBase;
import org.bibsonomy.util.ValidationUtils;

/**
 * this field wrapps lucene's PerFieldAnalyzerWrapper for making it
 * configurable via spring
 * 
 * @author fei
 * @version $Id: SpringPerFieldAnalyzerWrapper.java,v 1.9 2010-07-16 12:11:59 nosebrain Exp $
 */
public class SpringPerFieldAnalyzerWrapper extends Analyzer {	
	/** map configuring the index */
	private Map<String,Map<String,Object>> propertyMap;

	/** map configuring the fieldwrapper */
	private Map<String, Object> fieldMap;
	
	/** default analyzer */
	private Analyzer defaultAnalyzer;
	
	/** full text search analyzer */
	private Analyzer fullTextSearchAnalyzer;

	/** we delegate to this analyzer */
	private PerFieldAnalyzerWrapper analyzer;
	
	/**
	 * default constructor
	 */
	public SpringPerFieldAnalyzerWrapper() {
	}
	
	/**
	 * initialize internal data structures
	 */
	private void init() {
		// initialize tokenizer if all necessary properties are set
		if( (this.defaultAnalyzer!=null) && (this.fieldMap!=null) ) {
			this.analyzer = new PerFieldAnalyzerWrapper(getDefaultAnalyzer());
			
			for( String fieldName : fieldMap.keySet() ) {
				analyzer.addAnalyzer(fieldName, (Analyzer)fieldMap.get(fieldName));
			}
		}
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return this.analyzer.tokenStream(fieldName, reader);
	}
	
	/**
	 * @param fieldMap the fieldMap to set
	 */
	public void setFieldMap(Map<String, Object> fieldMap) {
		this.fieldMap = fieldMap;
		init();
	}

	/**
	 * @return the fieldMap
	 */
	public Map<String, Object> getFieldMap() {
		return fieldMap;
	}

	/**
	 * @param defaultAnalyzer the defaultAnalyzer to set
	 */
	public void setDefaultAnalyzer(Analyzer defaultAnalyzer) {
		this.defaultAnalyzer = defaultAnalyzer;
		init();
	}

	/**
	 * @return defaultAnalyzer
	 */
	public Analyzer getDefaultAnalyzer() {
		return defaultAnalyzer;
	}

	/**
	 * @param propertyMap the propertyMap to set
	 */
	public void setPropertyMap(Map<String,Map<String,Object>> propertyMap) {
		this.propertyMap = propertyMap;
		
		// update the fieldmap
		this.fieldMap = new HashMap<String,Object>();
		
		// TODO: use value entrySet iterator
		for( String propertyName : propertyMap.keySet() ) {
			String fieldName       = (String)propertyMap.get(propertyName).get(LuceneBase.CFG_LUCENENAME);
			Analyzer fieldAnalyzer = (Analyzer)propertyMap.get(propertyName).get(LuceneBase.CFG_ANALYZER);
			if( ValidationUtils.present(fieldAnalyzer) )
				this.fieldMap.put(fieldName, fieldAnalyzer);
		}
		
		// set full text search analyzer
		if( this.fullTextSearchAnalyzer!=null )
			fieldMap.put(LuceneBase.FLD_MERGEDFIELDS, this.fullTextSearchAnalyzer);
	}

	/**
	 * @return the propertyMap
	 */
	public Map<String,Map<String,Object>> getPropertyMap() {
		return propertyMap;
	}

	/**
	 * @param fullTextSearchAnalyzer the fullTextSearchAnalyzer
	 */
	public void setFullTextSearchAnalyzer(Analyzer fullTextSearchAnalyzer) {
		this.fullTextSearchAnalyzer = fullTextSearchAnalyzer;
		// update fieldmap
		if( this.fieldMap!=null ) {
			fieldMap.put(LuceneBase.FLD_MERGEDFIELDS, this.fullTextSearchAnalyzer);
		}
	}
	
	/**
	 * 
	 * @return the fullTextSearchAnalyzer
	 */
	public Analyzer getFullTextSearchAnalyzer() {
		return fullTextSearchAnalyzer;
	}
	
}
