/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package org.bibsonomy.webapp.command.admin;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.bibsonomy.recommender.tags.database.params.RecAdminOverview;
import org.bibsonomy.recommender.tags.multiplexer.MultiplexingTagRecommender;
import org.bibsonomy.webapp.command.BaseCommand;

/**
 * Command bean for admin page 
 * 
 * @author bsc
 * @version $Id: AdminRecommenderViewCommand.java,v 1.8 2010-07-20 14:33:43 bsc Exp $
 */
public class AdminRecommenderViewCommand extends BaseCommand {
	private MultiplexingTagRecommender mp;
	private List<RecAdminOverview> recOverview; 
	private String action;
	private String adminResponse;
	private Long queriesPerLatency;
	private List<Long> activeRecs;
	private List<Long> disabledRecs;
	private final Map<Integer, String> tabdescriptor;
	/**
	 * @author bsc
	 *
	 */
	public enum Tab{ STATUS, ACTIVATE, ADD }
	private Tab tab;
	private Map<Long, String> activeRecommenders;
	private Map<Long, String> disabledRecommenders;

	private long editSid;
	private List<String> deleteRecIds;
	private String newrecurl;
	
	
	/**
	 */
	public AdminRecommenderViewCommand(){
		this.queriesPerLatency = (long)1000;
		this.action = null;
		
		this.tabdescriptor = new TreeMap<Integer, String>();
		tabdescriptor.put(Tab.STATUS.ordinal(), "Active Recommenders");
		tabdescriptor.put(Tab.ACTIVATE.ordinal(), "Activate/deactivate");
		tabdescriptor.put(Tab.ADD.ordinal(), "Add/Remove");
		this.tab = Tab.STATUS;
	}
	
	/**
	 * @param activeRecommenders map {setting-id} -> {recommender-id}
	 */
	public void setActiveRecommenders(Map<Long, String> activeRecommenders){
		this.activeRecommenders = activeRecommenders;
	}
	
	/**
	 * @param disabledRecommenders map {setting-id} -> {recommender-id}
	 */
	public void setDisabledRecommenders(Map<Long, String> disabledRecommenders){
		this.disabledRecommenders = disabledRecommenders;
	}
	
	/**
	 * @return Entryset of currently activated recommenders 
	 */
	public Set<Entry<Long, String>> getActiveRecommenders(){
		if (activeRecommenders == null) return null;
		return activeRecommenders.entrySet();
	}
	
	/**
	 * @return Entryset of currently deactivated recommenders 
	 */
	public Set<Entry<Long, String>> getDisabledRecommenders(){
		if (disabledRecommenders == null) {
			return null;
		}
		return disabledRecommenders.entrySet();
	}
	
	/**
	 * @param t ordinal number of tab to be activated
	 */
	public void setTab(Integer t){
		if (t>=0 && t<Tab.values().length) {
		  this.tab = Tab.values()[t];
		}
	}
	/**
	 * @param t Tab to be activated
	 */
	public void setTab(Tab t){
		this.tab = t;
	}
	/**
	 * @return ordinal number of active tab
	 */
	public Integer getTab(){
		return this.tab.ordinal();
	}
	/**
	 * @return name/description of currently activated tab
	 */
	public String getTabDescription(){
		return tabdescriptor.get(this.tab.ordinal());
	}
	/**
	 * @param t tab to get description for
	 * @return Description of this tab
	 */
	public String getTabDescription(Tab t){
		return tabdescriptor.get(t.ordinal());
	}

	/**
	 * @return Entryset containing Tab-id and their descriptions
	 */
	public Set<Entry<Integer, String>> getTabs(){
		return tabdescriptor.entrySet();
	}
	
	/**
	 * @param recOverview List of recommmenders contained in multiplexer
	 */
	public void setRecOverview(List<RecAdminOverview> recOverview){
		this.recOverview = recOverview;
	}
	/**
	 * @return List of recommmenders contained in multiplexer
	 */
	public List<RecAdminOverview> getRecOverview(){
		return this.recOverview;
	}
	/**
	 * @param mp multiplexer
	 */
	public void setMultiplexingTagRecommender(MultiplexingTagRecommender mp){
		this.mp = mp;
	}
	/**
	 * @return multiplexer
	 */
	public MultiplexingTagRecommender getMultiplexingTagRecommender(){
		return this.mp;
	}
	/**
	 * @param action the action which will be executed by the controller and set to null again
	 */
	public void setAction(String action){
		this.action = action;
	}
	/**
	 * @return the action which will be executed by the controller and set to null again
	 */
	public String getAction(){
		return this.action;
	}
	/**
	 * @param queriesPerLatency number of values which will be fetched from the database to calculate average recommender-latencies
	 */
	public void setQueriesPerLatency(Long queriesPerLatency){
		//only accept positive values
		if(queriesPerLatency > 0) {
		   this.queriesPerLatency = queriesPerLatency;
		}
	}
	/**
	 * @return number of values which will be fetched from the database to calculate average recommender-latencies
	 */
	public Long getQueriesPerLatency(){
		return this.queriesPerLatency;
	}
	
	/**
	 * @param adminResponse response-message to the last action executed (e.g. failure, success etc.) set by the controller
	 */
	public void setAdminResponse(String adminResponse){
		this.adminResponse = adminResponse;
	}
	/**
	 * @return response-message to the last action executed (e.g. failure, success etc.) set by the controller
	 */
	public String getAdminResponse(){
		return this.adminResponse;
	}
	
	/**
	 * @param activeRecs updated list of active setting-ids.
	 * This property can be set in the view by administrators and will be managed and set back to null by the controller. 
	 */
	public void setActiveRecs(List<Long> activeRecs){
		this.activeRecs = activeRecs;
	}
	/**
	 * @return updated active recommenders
	 */
	public List<Long> getActiveRecs(){
		return this.activeRecs;
	}
	
	/**
	 * @param disabledRecs updated list of inactive setting-ids
	 */
	public void setDisabledRecs(List<Long> disabledRecs){
		this.disabledRecs = disabledRecs;
	}
	/**
	 * @return updated list of inactive setting-ids
	 */
	public List<Long> getDisabledRecs(){
		return this.disabledRecs;
	}
	

	/**
	 * @param editSid setting-id of recommender to be edited
	 */
	public void setEditSid(long editSid) {
		this.editSid = editSid;
	}
	/**
	 * @return setting-id of recommender to be edited
	 */
	public long getEditSid() {
		return this.editSid;
	}

	/**
	 * @return ids/urls of recommenders to be deleted
	 */
	public List<String> getDeleteRecIds() {
		return this.deleteRecIds;
	}

	/**
	 * @param deleteRecIds ids/urls of recommenders to be edited
	 */
	public void setDeleteRecIds(List<String> deleteRecIds) {
		this.deleteRecIds = deleteRecIds;
	}
	/**
	 * @param recurl url of new recommender to be added
	 */
	public void setNewrecurl(String recurl){
		this.newrecurl = recurl;
	}
	/**
	 * @return url of new recommender to be added
	 */
	public String getNewrecurl(){
		return this.newrecurl;
	}
	
	
}