/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.params;


/**
 * @author philipp
 * @version $Id: RepositoryParam.java,v 1.2 2011-06-16 13:55:00 nosebrain Exp $
 */
public class RepositoryParam {    
    
    private String intraHash;
    
    private String interHash;
    
    private String userName;
    
    private String repositoryName;
        
    /**
     * @return the repositoryName
     */
    public String getRepositoryName() {
        return this.repositoryName;
    }

    /**
     * @param repositoryName the repositoryName to set
     */
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    /**
     * @return the intraHash
     */
    public String getIntraHash() {
        return this.intraHash;
    }

    /**
     * @param intraHash the intraHash to set
     */
    public void setIntraHash(String intraHash) {
        this.intraHash = intraHash;
    }

    /**
     * @return the interHash
     */
    public String getInterHash() {
        return this.interHash;
    }

    /**
     * @param interHash the interHash to set
     */
    public void setInterHash(String interHash) {
        this.interHash = interHash;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
