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

package de.unikassel.puma.openaccess.classification.chain;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import de.unikassel.puma.openaccess.classification.Classification;
import de.unikassel.puma.openaccess.classification.ClassificationSource;
import de.unikassel.puma.openaccess.classification.ClassificationTextParser;

public class ClassificationTextChainElement implements ClassificationSource {

	private final ClassificationTextParser classificationParser;
	
	private ClassificationSource next = null;

	public ClassificationTextChainElement(ClassificationTextParser cParser) {
		this.classificationParser = cParser;
	}

	public void setNext(ClassificationSource next) {
		this.next = next;
	}
	
	public ClassificationSource getNext() {
		return this.next;
	}
	
	@Override
	public Classification getClassification(URL url) throws IOException {
		BufferedReader in =
			new BufferedReader(new FileReader(url.getPath()));
		
		classificationParser.parse(in);
		
		if(!present(classificationParser.getList())) {
			if(!present(next)) {
				return null;
			} else {
				return next.getClassification(url);
			}
		}
		
		return new Classification(classificationParser.getName(), classificationParser.getList(), classificationParser.getDelimiter());
	}
}
