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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * this class wraps a single application context for the lucene modul
 * 
 * @author fei
 * @version $Id: LuceneSpringContextWrapper.java,v 1.3 2010-10-13 11:31:53 nosebrain Exp $
 */
public class LuceneSpringContextWrapper {
	private static final String CONFIG_LOCATION = "LuceneContext.xml";
	
	/** bean factory */
	private static final BeanFactory beanFactory = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
	
	/**
	 * @return the beanfactory for the bibsonomy modul
	 */
	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}
	
	/**
	 * this method just loads the class to init the static fields
	 */
	public static void init() {
		// just load the class
	}
}
