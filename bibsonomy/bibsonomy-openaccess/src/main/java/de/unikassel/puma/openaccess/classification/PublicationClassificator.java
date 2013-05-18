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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.unikassel.puma.openaccess.classification.chain.ClassificationTextChainElement;
import de.unikassel.puma.openaccess.classification.chain.ClassificationXMLChainElement;
import de.unikassel.puma.openaccess.classification.chain.parser.ACMClassification;
import de.unikassel.puma.openaccess.classification.chain.parser.DDCClassification;
import de.unikassel.puma.openaccess.classification.chain.parser.JELClassification;

public class PublicationClassificator {
	
	private static final Log log = LogFactory.getLog(PublicationClassificator.class);
	
	private final String xmlPath;
	
	private HashMap<org.bibsonomy.model.Classification, Classification> classifications = new HashMap<org.bibsonomy.model.Classification, Classification>();
	
	private Classification getClassificationByName(String name) {
		for(org.bibsonomy.model.Classification c : classifications.keySet()) {
			if(c.getName().equals(name))
				return classifications.get(c);
		}
		
		return null;
	}
	
	public PublicationClassificator(String xmlPath) {
		this.xmlPath = xmlPath;
		initialise();
	}
	
	public final List<PublicationClassification> getChildren(String classification, String name) {
		Classification c = getClassificationByName(classification);
		
		if(present(c)) {
			return c.getChildren(name);
		} else {
			return new ArrayList<PublicationClassification>();
		}
	}
	
	public Set<org.bibsonomy.model.Classification> getAvailableClassifications() {
		return classifications.keySet();
	}
	
	public String getDescription(String classification, String name) {
		Classification c = getClassificationByName(classification);
		
		if(present(c)) {
			return c.getDescription(name);
		} else {
			return "";
		}	
	}
	
	private void initialise() {
		ArrayList<ClassificationSource> cceList = new ArrayList<ClassificationSource>();
		cceList.add(new ClassificationXMLChainElement(new JELClassification()));
		cceList.add(new ClassificationXMLChainElement(new ACMClassification()));
		cceList.add(new ClassificationTextChainElement(new DDCClassification()));
		
		
		File path = new File(xmlPath);
		
		if(path.isDirectory()) {
			
			File[] files = path.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File file) {
					if(file.isDirectory()) {
						return false;
					}
					
					if(!file.toString().endsWith(".properties")) {
						return true;
					}
					
					return false;
				}
			});
			
			for(File f : files) {
				try {
					Classification c = null;
					
					for(int i = 0; i < cceList.size() && !present(c); ++i) {
						c = cceList.get(i).getClassification(f.toURI().toURL());
					}
					
					if(!present(c)) {
						log.error("Unable to parse " +f.getName());
						continue;
					}
					
					log.info("Found Classification " +c.getClassName());
					
					//try to read values from .properties file
					try {
						Properties properties = new Properties();
						org.bibsonomy.model.Classification classification = new org.bibsonomy.model.Classification();
						
						properties.load(new FileReader(f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-4) +".properties"));

						classification.setName(properties.getProperty("name"));
						classification.setDesc(properties.getProperty("desc"));
						classification.setUrl(properties.getProperty("url"));
						
						classifications.put(classification, c);
						
					} catch (IOException e) {
						//no .properties file found, use the file name						
						org.bibsonomy.model.Classification classification = new org.bibsonomy.model.Classification();
						classification.setName(f.getName().substring(0,f.getName().length()-4));
						classifications.put(classification, c);
					}
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	public int getNumberOfClassifications() {
		return classifications.size();
	}

}
