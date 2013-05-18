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

package org.bibsonomy.database.managers;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.managers.chain.concept.ConceptChain;
import org.bibsonomy.database.params.LoggingParam;
import org.bibsonomy.database.params.TagRelationParam;
import org.bibsonomy.database.plugin.DatabasePluginRegistry;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.util.ExceptionUtils;

/**
 * Used to deal with tag concepts in the database.
 * 
 * @author Jens Illig
 * @version $Id: TagRelationDatabaseManager.java,v 1.27 2011-06-07 13:35:05 rja Exp $
 */
public class TagRelationDatabaseManager extends AbstractDatabaseManager {

	private static final Log log = LogFactory.getLog(TagRelationDatabaseManager.class);

	private final static TagRelationDatabaseManager singleton = new TagRelationDatabaseManager();
	private static final ConceptChain chain = new ConceptChain();

	/**
	 * Defines relations for tags or concepts.
	 */
	private static enum Relation {
		/** Super tag */
		SUPER,
		/** Sub tag */
		SUB
	}
	
	private final GeneralDatabaseManager generalDb;
	private final DatabasePluginRegistry plugins;

	private TagRelationDatabaseManager() {
		this.generalDb = GeneralDatabaseManager.getInstance();
		this.plugins = DatabasePluginRegistry.getInstance();
	}

	/**
	 * @return a singleton instance of the TagRelationDatabaseManager
	 */
	public static TagRelationDatabaseManager getInstance() {
		return singleton;
	}

	/**
	 * Performs the chain
	 * 
	 * @param param
	 * @param session
	 * @return list of concepts
	 */
	public List<Tag> getConcepts(final TagRelationParam param, DBSession session) {
		return chain.getFirstElement().perform(param, session);
	}
	
	/**
	 * Inserts a relation.
	 * 
	 * @param tag
	 * @param userName
	 * @param session
	 */
	public void insertRelations(final Tag tag, final String userName, final DBSession session) {
		this.addRel(tag.getName(), tag.getSuperTags(), userName, Relation.SUPER, session);
		this.addRel(tag.getName(), tag.getSubTags(), userName, Relation.SUB, session);
	}

	private void addRel(final String centerTagName, final List<Tag> relatedTags, final String userName, final Relation rel, final DBSession session) {
		if ((relatedTags == null) || (relatedTags.size() == 0)) {
			return;
		}
		final TagRelationParam trp = new TagRelationParam();
		trp.setOwnerUserName(userName);
		trp.setCreationDate(new Date());
		if (rel == Relation.SUPER) {
			trp.setLowerTagName(centerTagName);
		} else if (rel == Relation.SUB) {
			trp.setUpperTagName(centerTagName);
		} else {
			ExceptionUtils.logErrorAndThrowRuntimeException(log, null, "unknown " + Relation.class.getName() + " '" + rel.toString() + "'");
		}
		for (final Tag tag : relatedTags) {
			if (rel == Relation.SUPER) {
				trp.setUpperTagName(tag.getName());
			} else if (rel == Relation.SUB) {
				trp.setLowerTagName(tag.getName());
			}
			if (!isRelationPresent(trp, session)) {
				this.insertIfNotPresent(trp, session);
			}
		}
	}

	private void insertIfNotPresent(final TagRelationParam trp, final DBSession session) {
		session.beginTransaction();
		try {
			this.insert("insertTagRelationIfNotPresent", trp, session);
			this.generalDb.updateIds(ConstantID.IDS_TAGREL_ID, session);
		} catch (final Exception ex) {
			// TODO: improve me...
			log.warn(ex.getMessage(), ex);
		} finally {
			session.commitTransaction();
			session.endTransaction();
		}
	}

	/**
	 * Deletes a whole concept
	 * 
	 * @param upperTagName -
	 *            the concept name
	 * @param userName -
	 *            the owner of the concept
	 * @param session -
	 *            the session
	 */
	public void deleteConcept(final String upperTagName, final String userName, final DBSession session) {
		this.plugins.onConceptDelete(upperTagName, userName, session);
		final TagRelationParam trp = new TagRelationParam();
		trp.setOwnerUserName(userName);
		trp.setUpperTagName(upperTagName);
		this.delete("deleteConcept", trp, session);
	}

	/**
	 * Deletes a single relation
	 * 
	 * @param upperTagName -
	 *            supertag name
	 * @param lowerTagName -
	 *            subtag name
	 * @param userName -
	 *            the owners name
	 * @param session -
	 *            the session
	 */
	public void deleteRelation(final String upperTagName, final String lowerTagName, final String userName, final DBSession session) {
		this.plugins.onTagRelationDelete(upperTagName, lowerTagName, userName, session);
		final TagRelationParam trp = new TagRelationParam();
		trp.setOwnerUserName(userName);
		trp.setLowerTagName(lowerTagName);
		trp.setUpperTagName(upperTagName);
		this.delete("deleteTagRelation", trp, session);
	}


	/**
	 * @param param
	 * @param session
	 * @return the concepts list of a user
	 */
	public List<Tag> getAllConceptsForUser(final TagRelationParam param, final DBSession session) {
		return this.queryForList("getAllConceptsForUser", param, Tag.class, session);
	}

	/**
	 * Retrieve concepts for users as well as groups
	 * 
	 * @param conceptName
	 *            the conceptname
	 * @param userName
	 *            the users or groups name
	 * @param session
	 * @return concepts for given user or group
	 */
	public Tag getConceptForUser(final String conceptName, final String userName, final DBSession session) {
		final TagRelationParam trp = new TagRelationParam();
		trp.setOwnerUserName(userName);
		trp.setUpperTagName(conceptName);
		return this.queryForObject("getConceptForUser", trp, Tag.class, session);
	}

	/**
	 * Returns the picked concepts for the given user.
	 * 
	 * @param userName
	 * @param session
	 * @return picked concepts for the given user
	 */
	@SuppressWarnings("unchecked")
	public List<Tag> getPickedConceptsForUser(final String userName, final DBSession session) {
		return this.queryForList("getPickedConceptsForUser", userName, session);
	}

	/**
	 * @param session -
	 *            the db session
	 * @return all concepts (50 most popular)
	 */
	public List<Tag> getAllConcepts(final DBSession session) {
		return this.queryForList("getAllConcepts", null, Tag.class, session);
	}

	/**
	 * @param conceptName
	 * @param session -
	 *            the db session
	 * @return a global concept by name
	 */
	public Tag getGlobalConceptByName(final String conceptName, final DBSession session) {
		return this.queryForObject("getGlobalConceptByName", conceptName, Tag.class, session);
	}

	/**
	 * Checks if the given relation already exists for the user
	 * 
	 * @param param -
	 *            TagRelationParam
	 * @param session -
	 *            the session
	 * @return <code>true</code> if relation already exists else
	 *         <code>false</code>
	 */
	public boolean isRelationPresent(final TagRelationParam param, final DBSession session) {
		final String relationID = queryForObject("getRelationID", param, String.class, session);
		if (relationID == null) return false;
		return true;
	}
	
	/**
	 * Sets the concept with the given uppertag to picked
	 * 
	 * @param concept 
	 * @param ownerUserName 
	 * @param session
	 */
	public void pickConcept(final Tag concept, final String ownerUserName, final DBSession session){
		TagRelationParam param = new TagRelationParam();
		param.setUpperTagName(concept.getName());
		param.setOwnerUserName(ownerUserName);
		
		this.update("pickConcept", param, session);
	}
	
	/**
	 * Sets the concept with the given uppertag to unpicked
	 * @param concept 
	 * @param ownerUserName 
	 * @param session
	 */
	public void unpickConcept(final Tag concept, final String ownerUserName, final DBSession session){
		TagRelationParam param = new TagRelationParam();
		param.setUpperTagName(concept.getName());
		param.setOwnerUserName(ownerUserName);
		
		this.update("unpickConcept", param, session);
	}
	
	/**
	 * Sets all concepts to unpicked
	 * 
	 * @param ownerUserName
	 * @param session
	 */
	public void unpickAllConcepts(final String ownerUserName, final DBSession session){
		this.update("unpickAllConcepts", ownerUserName, session);
	}
	
	/**
	 * Sets all concepts to picked
	 * 
	 * @param ownerUserName
	 * @param session
	 */
	public void pickAllConcepts(final String ownerUserName, final DBSession session){
		this.update("pickAllConcepts", ownerUserName, session);
	}

	/**
	 * TODO: improve documentation
	 * 
	 * @param user
	 * @param tagToReplace
	 * @param replacementTag
	 * @param session
	 */
	public void updateTagRelations(final User user, final Tag tagToReplace, final Tag replacementTag, final DBSession session) {
		if(!present(tagToReplace.getName()) || !present(replacementTag.getName())) {
			log.error("tried to replace tag without name in TagRelationDatabase.updateTagRelations()");
			throw new ValidationException("tried to replace tag without valid name");
		}
		
		LoggingParam<String> param = new LoggingParam<String>();
		param.setOldId(tagToReplace.getName());
		param.setNewId(replacementTag.getName());
		param.setUser(user);
		
		this.update("updateUpperTagRelations", param, session);
		this.update("updateLowerTagRelations", param, session);
		this.update("deleteEqualConcepts", param, session);
		this.update("deleteOldConcepts", param, session);
		
	}
}