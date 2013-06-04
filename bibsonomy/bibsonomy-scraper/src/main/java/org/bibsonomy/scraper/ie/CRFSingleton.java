/**
 *
 *  BibSonomy-Scraper - Web page scrapers returning BibTeX for BibSonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.scraper.ie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.naming.NamingException;

import edu.umass.cs.mallet.base.fst.CRF4;

/**
 * stores the Conditional Random Field for Information Extraction with mallet
 * @version $Id: CRFSingleton.java,v 1.6 2011-04-29 07:24:33 bibsonomy Exp $
 */
public class CRFSingleton {
		
	private static final String CRF_DAT = "crf.dat";
	private static CRF4 crf = null;
	
	public CRFSingleton() throws FileNotFoundException, IOException, NamingException, ClassNotFoundException {
		if(crf == null){
			// get path to crf file and read crf
			ObjectInputStream ois = new ObjectInputStream(this.getClass().getResourceAsStream(CRF_DAT));
			crf = (CRF4) ois.readObject();
			ois.close();
		}		
	}
		
	public CRF4 getCrf(){
		return crf;
	}

}


