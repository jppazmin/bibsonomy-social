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

package org.bibsonomy.recommender.tags.simple.termprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jil
 * @version $Id: StopWordRemover.java,v 1.4 2010-07-14 11:38:29 nosebrain Exp $
 */
public class StopWordRemover implements TermProcessor {
	private static final Log log = LogFactory.getLog(StopWordRemover.class);
	
	private static final String stopWordFile = "multilangST.txt";
	
	private static StopWordRemover instance;
	
	/**
	 * @return the {@link StopWordRemover} instance
	 */
	public static StopWordRemover getInstance() {
		if (instance == null) {
			instance = new StopWordRemover();
		}
		return instance;
	}
	
	private final Collection<String> stopWords;
	
	private StopWordRemover() {
		this.stopWords = new HashSet<String>();
		try {
			InputStream is = getClass().getResourceAsStream(stopWordFile);
			if (is == null) {
				throw new IOException("is == null");
			}
			final BufferedReader r = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String sw = null;
			while ((sw = r.readLine()) != null) {
				this.stopWords.add(sw);
			}
			
			r.close();
		} catch (IOException e) {
			log.fatal("Stopwordfile could not be loaded");
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String process(String term) {
		if (!stopWords.contains(term)) {
			log.debug("not removed word '" + term + "' with length " + term.length());
			return term;
		}
		
		log.debug("removed stopword '" + term + "' with length " + term.length());
		return null;
	}

}
