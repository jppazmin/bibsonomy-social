/**
 *
 *  BibSonomy-Logging - Logs clicks from users of the BibSonomy webapp.
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

package org.bibsonomy.logging;

/**
 * @author sst
 * @version $Id: Log.java,v 1.6 2010-07-14 12:21:59 nosebrain Exp $
 */
public class Log {

	/** unique id */
	private int id;

	/** date of insertion of logdata */
	private String logdate;

	/** path in dom, backwards */
	private String dompath;

	/** path in dom, backwards, with classes*/
	private String dompath2;

	/** type of logging info, where has user clicked? bookmark area, ... */
	private String type;

	/** page url */
	private String pageurl;

	/** anchor content */
	private String acontent;

	/** number of posts of anchor content*/
	private String anumberofposts;

	/** anchor hyper-reference */
	private String ahref;

	/** http-header: user agent - browseridentification */
	private String useragent;

	/** cookie: username */
	private String username;

	/** cookie: session id*/
	private String sessionid;

	/** http-header: hostname */
	private String host;

	/** complete http-header */
	private String completeheader;

	/** http-header: X-Forwarded-For */
	private String xforwardedfor;

	/** windowsize of client browser*/
	private String windowsize;

	/** mouse position in documennt */
	private String mousedocumentpos;

	/** mouse position in client brwoser window */
	private String mouseclientpos;

	/** position of clicked link in its list */
	private String listpos;
 
	/** clicked link is users own bookmark (or bibtex) */
	private String abmown;

	/** referrer - where does user come from  */
	private String referer;
	
	@Override
	public String toString() {
		return	"id: "+ this.id +
				"\nlogdate: "+ this.logdate +
				"\ndompath: "+ this.dompath +
				"\ndompath2: "+ this.dompath2 +
				"\ntype: "+ this.type +
				"\npageurl: "+ this.pageurl + 
				"\nacontent: "+ this.acontent +
				"\nanumberofposts: "+ this.anumberofposts +
				"\nahref: "+ this.ahref + 
				"\nuseragent: "+ this.useragent + 
				"\nusername: " +this.username + 
				"\nsessionid: " +this.sessionid +  
				"\nhost: " +this.host + 
				"\ncompleteheader: " +this.completeheader + 
				"\nxforwardedfor: " +this.xforwardedfor + 
				"\nwindowsize: " +this.windowsize + 
				"\nmousedocumentpos: " +this.mousedocumentpos + 
				"\nmouseclientpos: " +this.mouseclientpos + 
				"\nlistpos: " +this.listpos + 
				"\nabmown: " +this.abmown + 
				"\nreferer: " +this.referer;		
	}	

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the logdate
	 */
	public String getLogdate() {
		return logdate;
	}

	/**
	 * @param logdate the logdate to set
	 */
	public void setLogdate(String logdate) {
		this.logdate = logdate;
	}

	/**
	 * @return the dompath
	 */
	public String getDompath() {
		return dompath;
	}

	/**
	 * @param dompath the dompath to set
	 */
	public void setDompath(String dompath) {
		this.dompath = dompath;
	}

	/**
	 * @return the pageurl
	 */
	public String getPageurl() {
		return pageurl;
	}

	/**
	 * @param pageurl the pageurl to set
	 */
	public void setPageurl(String pageurl) {
		this.pageurl = pageurl;
	}

	/**
	 * @return the acontent
	 */
	public String getAcontent() {
		return acontent;
	}

	/**
	 * @param acontent the acontent to set
	 */
	public void setAcontent(String acontent) {
		this.acontent = acontent;
	}

	/**
	 * @return the anumberofposts
	 */
	public String getAnumberofposts() {
		return anumberofposts;
	}

	/**
	 * @param anumberofposts the anumberofposts to set
	 */
	public void setAnumberofposts(String anumberofposts) {
		try {
			Integer.parseInt(anumberofposts);
			this.anumberofposts = anumberofposts;
		} catch(NumberFormatException e) {
			this.anumberofposts = "0";
		}	
	}

	/**
	 * @return the ahref
	 */
	public String getAhref() {
		return ahref;
	}

	/**
	 * @param ahref the ahref to set
	 */
	public void setAhref(String ahref) {
		this.ahref = ahref;
	}

	/**
	 * @return the useragent
	 */
	public String getUseragent() {
		return useragent;
	}

	/**
	 * @param useragent the useragent to set
	 */
	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the sessionid
	 */
	public String getSessionid() {
		return sessionid;
	}

	/**
	 * @param sessionid the sessionid to set
	 */
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the completeheader
	 */
	public String getCompleteheader() {
		return completeheader;
	}
	
	/**
	 * @param completeheader the completeheader to set
	 */
	public void setCompleteheader(String completeheader) {
		this.completeheader = completeheader;
	}
	
	/**
	 * @return the xforwardedfor
	 */
	public String getXforwardedfor() {
		return xforwardedfor;
	}

	/**
	 * @param xforwardedfor the xforwardedfor to set
	 */
	public void setXforwardedfor(String xforwardedfor) {
		this.xforwardedfor = xforwardedfor;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the dompath2
	 */
	public String getDompath2() {
		return dompath2;
	}

	/**
	 * @param dompath2 the dompath2 to set
	 */
	public void setDompath2(String dompath2) {
		this.dompath2 = dompath2;
	}

	/**
	 * @return the windowsize
	 */
	public String getWindowsize() {
		return windowsize;
	}

	/**
	 * @param windowsize the windowsize to set
	 */
	public void setWindowsize(String windowsize) {
		this.windowsize = windowsize;
	}

	/**
	 * @return the mousedocumentpos
	 */
	public String getMousedocumentpos() {
		return mousedocumentpos;
	}

	/**
	 * @param mousedocumentpos the mousedocumentpos to set
	 */
	public void setMousedocumentpos(String mousedocumentpos) {
		this.mousedocumentpos = mousedocumentpos;
	}

	/**
	 * @return the mouseclientpos
	 */
	public String getMouseclientpos() {
		return mouseclientpos;
	}

	/**
	 * @param mouseclientpos the mouseclientpos to set
	 */
	public void setMouseclientpos(String mouseclientpos) {
		this.mouseclientpos = mouseclientpos;
	}

	/**
	 * @return the listpos
	 */
	public String getListpos() {
		return listpos;
	}

	/**
	 * @param listpos the listpos to set
	 */
	public void setListpos(String listpos) {
		this.listpos = listpos;
	}

	/**
	 * @return the abmown
	 */
	public String getAbmown() {
		return abmown;
	}

	/**
	 * @param abmown the abmown to set
	 */
	public void setAbmown(String abmown) {
		this.abmown = abmown;
	}

	/**
	 * @return the referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}
}
