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

package org.bibsonomy.lucene.search;

import static org.apache.lucene.util.Version.LUCENE_24;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_INDEX_ID_DELIMITER;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_LUCENE_FIELD_SPECIFIER;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_LUCENE_INDEX_PREFIX;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_TAG_CLOUD_LIMIT;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_DATE;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_GROUP;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_INTERHASH;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_MERGEDFIELDS;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_PRIVATEFIELDS;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_TAS;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_TITLE;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_USER;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_YEAR;
import static org.bibsonomy.lucene.util.LuceneBase.PARAM_RELEVANCE;
import static org.bibsonomy.lucene.util.LuceneBase.getEnableTagClouds;
import static org.bibsonomy.lucene.util.LuceneBase.getIndexBasePath;
import static org.bibsonomy.lucene.util.LuceneBase.getTagCloudLimit;
import static org.bibsonomy.util.ValidationUtils.present;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DuplicateFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.lucene.database.LuceneDBInterface;
import org.bibsonomy.lucene.param.QuerySortContainer;
import org.bibsonomy.lucene.search.collector.TagCountCollector;
import org.bibsonomy.lucene.util.LuceneBase;
import org.bibsonomy.lucene.util.LuceneResourceConverter;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.ResultList;
import org.bibsonomy.model.Tag;
import org.bibsonomy.services.searcher.ResourceSearch;

/**
 * abstract parent class for lucene search
 * 
 * @author fei
 * @version $Id: LuceneResourceSearch.java,v 1.48 2011-07-26 07:59:29 folke Exp $
 *
 * @param <R> resource type
 */
public abstract class LuceneResourceSearch<R extends Resource> implements ResourceSearch<R> {
	private static final Log log = LogFactory.getLog(LuceneResourceSearch.class);
	
	/**
	 *  read/write lock, allowing multiple searcher or exclusive an index update
	 *	TODO: we should use an implementation, which prefers writers for obtaining the lock 
	 */
	private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
	
	/** write lock, used for blocking  index searcher */
	private final Lock w = lock.writeLock();
	
	/** read lock, used for blocking the index update */
	private final Lock r = lock.readLock();
	
	/** logic interface for retrieving data from bibsonomy */
	private LuceneDBInterface<R> dbLogic;

	/** path to the managed resource index */
	protected String luceneIndexPath;
	
	/** global reference to the lucene searcher */
	protected IndexSearcher searcher; 

	/** default field analyzer */
	private Analyzer analyzer; 
	
	/** flag indicating whether the index was loaded successfully */
	private boolean isReady = false;
	
	/** default junction of search terms */
	private Operator defaultSearchTermJunctor = null;
	
	/** post model converter */
	private LuceneResourceConverter<R> resourceConverter;
	
	/** id for identifying redundant resource indices */
	// TODO: please use an object representing the index
	private int indexId;
	
	/**
	 * constructor
	 */
	public LuceneResourceSearch() {
		this.defaultSearchTermJunctor = Operator.AND;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.services.searcher.ResourceSearch#getPosts(java.lang.String, java.lang.String, java.lang.String, java.util.Collection, java.lang.String, java.lang.String, java.lang.String, java.util.Collection, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public ResultList<Post<R>> getPosts(final String userName, final String requestedUserName, final String requestedGroupName, final Collection<String> allowedGroups, final String searchTerms, final String titleSearchTerms, final String authorSearchTerms, final Collection<String> tagIndex, final String year, final String firstYear, final String lastYear, final int limit, final int offset) {
		// build query
		final QuerySortContainer query = this.buildQuery(userName, requestedUserName, requestedGroupName, allowedGroups, searchTerms, titleSearchTerms, authorSearchTerms, tagIndex, year, firstYear, lastYear);
		// perform search query
		return this.doSearch(query, limit, offset);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.services.searcher.ResourceSearch#getTags(java.lang.String, java.lang.String, java.lang.String, java.util.Collection, java.lang.String, java.lang.String, java.lang.String, java.util.Collection, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public List<Tag> getTags(final String userName, final String requestedUserName, final String requestedGroupName, final Collection<String> allowedGroups, final String searchTerms, final String titleSearchTerms, final String authorSearchTerms, final Collection<String> tagIndex, final String year, final String firstYear, final String lastYear, final int limit, final int offset) {
		// build query
		final QuerySortContainer qf = this.buildQuery(userName, requestedUserName, requestedGroupName, allowedGroups, searchTerms, titleSearchTerms, authorSearchTerms, tagIndex, year, firstYear, lastYear);
		// limit number of posts to consider for building the tag cloud
		qf.setLimit(getTagCloudLimit());
		// query index
		return doTagSearch(qf);
	}

	/**
	 * @return an empty collection of managed post objects
	 */
	protected ResultList<Post<R>> createEmptyResultList() {
		return new ResultList<Post<R>>();
	}	
	
	/**
	 * initialize internal data structures
	 */
	private void init() {
		LuceneBase.initRuntimeConfiguration();
		this.luceneIndexPath = getIndexBasePath() + CFG_LUCENE_INDEX_PREFIX + getResourceName();
	}
	
	/**
	 * reload the index -- has to be called after each index change 
	 * 
	 * @param indexId
	 */
	public void reloadIndex(final int indexId) {
		this.setIndexId(indexId);
		/*
		 * open new index searcher
		 */
		IndexSearcher newSearcher = null;
		init();
		try {
			// load and hold index on physical hard disk
			log.debug("Opening index " + indexId);
			final String indexPath = luceneIndexPath + CFG_INDEX_ID_DELIMITER + indexId;
			newSearcher = new IndexSearcher(FSDirectory.open(new File(indexPath)));
		} catch (final Exception e) {
			log.error("Error reloading index, disabling searcher ("+e.getMessage()+") - this should be the case while building a new index");
		}
 		
		/*
		 * switch searcher
		 */
		IndexSearcher oldSearcher = null;
		w.lock();
		try {
			if (newSearcher == null) {
				disableIndex();
			} else {
				oldSearcher = this.searcher;
				this.searcher = newSearcher;
				enableIndex();
			}
		} finally {
			w.unlock();
		}
		
		/*
		 * close old searcher
		 */
		try {
			if (oldSearcher != null) 
				oldSearcher.close();
		} catch (final IOException e) {
			log.debug("Error closing searcher.", e);
		}

	}
	
	/**
	 * perform given query, respecting read/write locks
	 * 
	 * @param limit
	 * @param offset
	 * @param query
	 * @return
	 */
	private ResultList<Post<R>> doSearch(final QuerySortContainer query, final int limit, final int offset) {
		if (!isEnabled()) {
			return this.createEmptyResultList();
		}
		
		r.lock();
		try {
			return this.searchLucene(query, limit, offset);
		} finally {
			r.unlock();
		}
	}

	/**
	 * query index for documents and create result list of post models 
	 */
	protected ResultList<Post<R>> searchLucene(final QuerySortContainer qf, final int limit, final int offset) {
		// initialize data structures
		final ResultList<Post<R>> postList = createEmptyResultList();
		
		final Query query = qf.getQuery();
		final Sort sort  = qf.getSort();
		log.debug("Querystring:  "+ query.toString() + " sorted by: "+ sort);

		try {
			//----------------------------------------------------------------
			// query the index
			//----------------------------------------------------------------
			// remove duplicate entries (with respect to the interhash)
			// DuplicateFilter df = new DuplicateFilter(FLD_INTERHASH, DuplicateFilter.KM_USE_FIRST_OCCURRENCE, DuplicateFilter.PM_FAST_INVALIDATION);
			
			// perform index search
			long starttimeQuery = System.currentTimeMillis();
			final TopDocs topDocs = searcher.search(query, null, offset + limit, sort);
			
			// determine number of posts to display
			final int hitslimit = (((offset+limit) < topDocs.totalHits) ? (offset + limit) : topDocs.totalHits);
			log.debug("offset / limit / hitslimit / hits.length():  " + offset + " / " + limit + " / " + hitslimit + " / " + topDocs.totalHits);
			log.debug("Query time: " + (System.currentTimeMillis() - starttimeQuery) + "ms");
			

			postList.setTotalCount(topDocs.totalHits);

			/*
			 * extract posts
			 */
			for (int i = offset; i < hitslimit; i++) {
				// get document from index
				final Document doc  = searcher.doc(topDocs.scoreDocs[i].doc);
				// convert document to bibsonomy post model
				final Post<R> post = this.resourceConverter.writePost(doc); 
				
				// set post frequency
				starttimeQuery = System.currentTimeMillis();
				int postFreq = 1;
				if (doc.get(FLD_INTERHASH) != null) {
					postFreq = this.searcher.docFreq(new Term(FLD_INTERHASH, doc.get(FLD_INTERHASH)));
				}
				log.debug("PostFreq query time: " + (System.currentTimeMillis() - starttimeQuery) + "ms");
				post.getResource().setCount(postFreq);
				
				postList.add(post);
			}

		} catch (final IOException e) {
			log.debug("LuceneBibTex: IOException: " + e.getMessage());
		}
		
		return postList;
	}	


	/**
	 * get tag assignments of top n relevant documents
	 * 
	 * @param qf
	 * @return
	 */
	private List<Tag> doTagSearch(final QuerySortContainer qf) {
		if (!isEnabled() && !getEnableTagClouds()) {
		    return new LinkedList<Tag>();
		}
		
		
		final Map<Tag, Integer> tagCounter = new HashMap<Tag, Integer>();
		
		r.lock();
		try {
			// gather tags used by the author's posts
			log.debug("Starting tag collection");
			final TopDocs topDocs = searcher.search(qf.getQuery(), null, qf.getLimit(), qf.getSort());
			log.debug("Done collecting tags");
			//----------------------------------------------------------------
			// extract tags from top n documents
			//----------------------------------------------------------------
			final int hitsLimit = ((qf.getLimit() < topDocs.totalHits) ? (qf.getLimit()) : topDocs.totalHits);
			for (int i = 0; i < hitsLimit; i++) {
				// get document from index
				final Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
				// convert document to bibsonomy post model
				final Post<R> post = this.resourceConverter.writePost(doc); 

				// set tag count
				if (present(post.getTags())) {
					for (final Tag tag : post.getTags()) {
						Integer oldCnt = tagCounter.get(tag);
						if (!present(oldCnt)) {
							oldCnt = 1;
						} else {
							oldCnt += 1;
						}
							
						tagCounter.put(tag, oldCnt);
					}
				}						
			}
		} catch (final IOException e) {
			log.error("Error building full text tag cloud for query " + qf.getQuery().toString());
		} finally {
			r.unlock();
		}
		
		final List<Tag> tags = new LinkedList<Tag>();
		// extract all tags
		for (final Map.Entry<Tag, Integer> entry : tagCounter.entrySet()) {
			final Tag tag = entry.getKey();
			tag.setUsercount(entry.getValue());
			tag.setGlobalcount(entry.getValue()); // FIXME: we set user==global count
			tags.add(tag);
		}
		log.debug("Done calculating tag statistics");
		
		// all done.
		return tags;	
	}
	
	/**
	 * check whether index is ready for searching
	 */
	private boolean isEnabled() {
		return this.isReady;
	}

	/** 
	 * disable search  
	 */
	private void disableIndex() {
		this.isReady = false;
	}

	/** 
	 * enable search  
	 */
	private void enableIndex() {
		this.isReady = true;
	}

	/**
	 * parse given search term for allowing lucene's search syntax
	 * 
	 * @param searchTerms a lucene search query
	 * @return the parsed query term
	 */
	protected Query buildFulltextSearchQuery(final String searchTerms) {
		return this.parseSearchQuery(FLD_MERGEDFIELDS, searchTerms);
	}

	/**
	 * parse given search term for allowing lucene's search syntax on the title field
	 * 
	 * @param searchTerms a lucene search query
	 * @return the parsed query term
	 */
	protected Query buildTitleSearchQuery(final String searchTerms) {
		return this.parseSearchQuery(FLD_TITLE, searchTerms);
	}
	
	/**
	 * build query to search for posts who's private notes field matches to the given search terms
	 * @param userName
	 * @return the private notes query for the user
	 */
	protected Query buildPrivateNotesQuery(final String userName, final String searchTerms) {
		final BooleanQuery privateSearchQuery = new BooleanQuery();
		
		if (present(userName)) {
			final Query privateSearchTermQuery = parseSearchQuery(FLD_PRIVATEFIELDS, searchTerms);
			privateSearchQuery.add(privateSearchTermQuery, Occur.MUST);
			privateSearchQuery.add(new TermQuery(new Term(FLD_USER, userName)), Occur.MUST);
		}
		
		return privateSearchQuery;
	}

	/**
	 * restrict result list to posts with given tag assignments
	 * 
	 * @param tagIndex list of tags 
	 * @return search query for restricting posts to given tag assignments
	 */
	protected Query buildTagSearchQuery(Collection<String> tagIndex) {
		//--------------------------------------------------------------------
		// prepare input parameters
		//--------------------------------------------------------------------
		final List<String> tags = new LinkedList<String>();
		if (present(tagIndex)) {
			for (final String tag : tagIndex) {
				try {
					tags.add(parseToken(FLD_TAS, tag));
				} catch (final IOException e) {
					log.error("Error parsing input tag " + tag + " ("+e.getMessage()+")");
					tags.add(tag);
				}
			}
			tagIndex = tags;
		}
		
		//--------------------------------------------------------------------
		// restrict to given tags
		//--------------------------------------------------------------------
		final BooleanQuery tagQuery = new BooleanQuery();
		if (present(tags)) {
			for (final String tagItem : tags) {
				tagQuery.add(new TermQuery(new Term(FLD_TAS, tagItem)), Occur.MUST);
			}
		}
		
		// all done
		return tagQuery;
	}
	
	/**
	 * restrict result list to posts owned by one of the given group members 
	 * @param requestedGroupName
	 * 
	 * @return the group search query
	 */
	protected BooleanQuery buildGroupSearchQuery(final String requestedGroupName) {
		// get given group's members
		final Collection<String> groupMembers = this.dbLogic.getGroupMembersByGroupName(requestedGroupName);
		
		//--------------------------------------------------------------------
		// restrict to group members
		//--------------------------------------------------------------------
		final BooleanQuery groupMemberQuery = new BooleanQuery();
		if (present(requestedGroupName) && present(groupMembers)) {
			for (final String member : groupMembers) {
				final Query memberQuery = new TermQuery(new Term(FLD_USER, member));
				groupMemberQuery.add(memberQuery, Occur.SHOULD);
			}
		}
		return groupMemberQuery;
	}
	
	/**
	 * restrict given query to posts belonging to a given time range
	 * 
	 * @param mainQuery
	 * @param year
	 * @param firstYear
	 * @param lastYear
	 * @return time range query
	 */
	protected Query makeTimeRangeQuery(final BooleanQuery mainQuery, String year, String firstYear, String lastYear) {
		/*
		 * exact year query
		 */
		boolean includeLowerBound = false;
		boolean includeUpperBound = false;

		if (present(year)) {
			year = year.replaceAll("\\D", "");
			mainQuery.add( new TermQuery(new Term(FLD_YEAR, year)), Occur.MUST );
		} else {
			/*
			 * range query
			 */
			// firstYear != null
			if (present(firstYear)) {
				firstYear = firstYear.replaceAll("\\D", "");
				includeLowerBound = true;
			}
			// lastYear != null
			if (present(lastYear)) {
				lastYear = lastYear.replaceAll("\\D", "");
				includeUpperBound = true; 
			}
		}
		
		if (includeLowerBound || includeUpperBound) {
			// if upper or lower bound is given, then use filter
			final Filter rangeFilter = new TermRangeFilter(FLD_YEAR , firstYear, lastYear, includeLowerBound, includeUpperBound);
			return new FilteredQuery(mainQuery, rangeFilter);
		}
		
		return mainQuery;
	}
	
	/**
	 * restrict result to posts which are visible to the user
	 * 
	 * @param userName the logged in user's name
	 * @param allowedGroups list of groups of which the logged in user is a member
	 * @return a query term which restricts the result to posts, which are visible to the user
	 */
	protected Query buildAccessModeQuery(final String userName, final Collection<String> allowedGroups) {
		//--------------------------------------------------------------------
		// get missing information from bibsonomy's database
		//--------------------------------------------------------------------
		final Collection<String> friends = this.dbLogic.getFriendsForUser(userName);
		
		final BooleanQuery accessModeQuery  = new BooleanQuery();
		final BooleanQuery privatePostQuery = new BooleanQuery();

		//--------------------------------------------------------------------
		// allowed groups
		//--------------------------------------------------------------------
		for (final String groupName : allowedGroups) {
			final Query groupQuery = new TermQuery(new Term(FLD_GROUP, groupName));
			accessModeQuery.add(groupQuery, Occur.SHOULD);
		}
		
		//--------------------------------------------------------------------
		// private post query
		//--------------------------------------------------------------------
		if (present(userName)) {
			final BooleanQuery privatePostGroups = new BooleanQuery();
			privatePostGroups.add(new TermQuery(new Term(FLD_GROUP, GroupID.PRIVATE.name().toLowerCase())), Occur.SHOULD);
			privatePostGroups.add(new TermQuery(new Term(FLD_GROUP, GroupID.FRIENDS.name().toLowerCase())), Occur.SHOULD);
			privatePostQuery.add(privatePostGroups, Occur.MUST);
			privatePostQuery.add(new TermQuery(new Term(FLD_USER, userName)), Occur.MUST);
			accessModeQuery.add(privatePostQuery, Occur.SHOULD);
		}

		if (present(friends)) {
			final BooleanQuery friendPostQuery= new BooleanQuery();
			friendPostQuery.add(new TermQuery(new Term(FLD_GROUP, GroupID.FRIENDS.name().toLowerCase())), Occur.MUST);

			final BooleanQuery friendPostAllowanceQuery= new BooleanQuery();
			// the post owner's friend may read the post
			for( final String friend : friends ) {
				friendPostAllowanceQuery.add(new TermQuery(new Term(FLD_USER, friend)), Occur.SHOULD);
			}

			friendPostQuery.add(friendPostAllowanceQuery, Occur.MUST);
			accessModeQuery.add(friendPostQuery, Occur.SHOULD);
		}		
		
		// all done
		return accessModeQuery; 
	}
	
	/**
	 * build the overall lucene search query term
	 * @param userName
	 * @param requestedUserName restrict the resulting posts to those which are owned by this user name
	 * @param requestedGroupName restrict the resulting posts to those which are owned this group
	 * @param searchTerms
	 * @return overall lucene search query
	 */
	protected QuerySortContainer buildQuery(final String userName, final String requestedUserName, final String requestedGroupName,  final Collection<String> allowedGroups, final String searchTerms, final String titleSearchTerms, final String authorSearchTerms, final Collection<String> tagIndex, final String year, final String firstYear, final String lastYear) {		
		//--------------------------------------------------------------------
		// build the query
		//--------------------------------------------------------------------
		// the resulting main query
		final BooleanQuery mainQuery = new BooleanQuery();
		final BooleanQuery searchQuery = this.buildSearchQuery(userName, searchTerms, titleSearchTerms, authorSearchTerms, tagIndex);
		
		// restrict result to given group
		if (present(requestedGroupName)) {
			final BooleanQuery groupQuery = this.buildGroupSearchQuery(requestedGroupName);
			if (groupQuery.getClauses().length >= 1) {
				mainQuery.add(groupQuery, Occur.MUST);
			}
		}
		
		// restricting access to posts visible to the user
		final Query accessModeQuery = buildAccessModeQuery(userName, allowedGroups);
		
		//--------------------------------------------------------------------
		// post owned by user
		//--------------------------------------------------------------------
		if (present(requestedUserName)) {
			mainQuery.add(new TermQuery(new Term(FLD_USER, requestedUserName)), Occur.MUST);
		}

		//--------------------------------------------------------------------
		// post owned by group
		//--------------------------------------------------------------------
		// TODO: remove code below??!
//		if ( false && present(requestedGroupName) ) {
//			mainQuery.add(new TermQuery(new Term(FLD_GROUP, requestedGroupName)), Occur.MUST);
//		}
		
		//--------------------------------------------------------------------
		// build final query
		//--------------------------------------------------------------------

		// combine query terms
		mainQuery.add(searchQuery, Occur.MUST);
		mainQuery.add(accessModeQuery, Occur.MUST);

		// set ordering
		final Sort sort = new Sort(new SortField(FLD_DATE,SortField.LONG,true));
		
		// all done
		log.debug("[Full text] Search query: " + mainQuery.toString());
		
		final QuerySortContainer qf = new QuerySortContainer();
		qf.setQuery(makeTimeRangeQuery(mainQuery, year, firstYear, lastYear));
		qf.setSort(sort);
		
		// set up collector
		TagCountCollector collector;
		try {
			collector = new TagCountCollector(null, CFG_TAG_CLOUD_LIMIT, qf.getSort());
		} catch (final IOException e) {
			log.error("Error building tag cloud collector");
			collector = null;
		}
		qf.setTagCountCollector(collector);
		
		return qf;
	}

	/**
	 * 
	 * @param userName
	 * @param searchTerms
	 * @param titleSearchTerms
	 * @param authorSearchTerms TODO: unused
	 * @param tagIndex
	 * @return a search query for the searh terms
	 */
	protected BooleanQuery buildSearchQuery(final String userName, final String searchTerms, final String titleSearchTerms, final String authorSearchTerms, final Collection<String> tagIndex) {
		final BooleanQuery searchQuery = new BooleanQuery();
		
		// search full text 
		if( present(searchTerms) ) {
			final Query fulltextQuery = this.buildFulltextSearchQuery(searchTerms);
			searchQuery.add(fulltextQuery, Occur.SHOULD);
		}
		
		// search private nodes
		if( present(userName) && present(searchTerms) ) {
			final Query privateNotesQuery = this.buildPrivateNotesQuery(userName, searchTerms);
			searchQuery.add(privateNotesQuery, Occur.SHOULD);
		}

		// search title 
		if( present(titleSearchTerms) ) {
			final Query titleQuery = this.buildTitleSearchQuery(titleSearchTerms);
			searchQuery.add(titleQuery, Occur.MUST);
		}
		
		// search tag assignments
		if( present(tagIndex) ) {
			final Query tagQuery = this.buildTagSearchQuery(tagIndex);
			searchQuery.add(tagQuery, Occur.MUST);
		}
		
		return searchQuery;
	}
	
	/**
	 * construct lucene query filter for searching posts matching a given title 
	 * 
	 * (title:searchTerms) 
	 *   [AND user_name:requestedUsername]
	 *    AND ( 
	 *          group:allowedGroup_1 OR ... OR group:allowedGroup_n 
	 *          OR (group:private AND user:userName)
	 *        )  
	 *        
	 * FIXME: merge buildFulltextQuery and buildGroupSearchQuery
	 * FIXME: shouldn't this query also respect 'visible for friends'?
	 * 
	 * @param group group name from which posts should be searched
	 * @param searchTerms search query
	 * @param requestedUserName user name from whom posts should be searched
	 * @param userName login user name
	 * @param allowedGroups groups which the login user is member of
	 * 
	 * @return title query
	 */
	protected QuerySortContainer buildTitleQuery(final String group, final String searchTerms, final String requestedUserName, final String userName, final Set<String> allowedGroups) {
		// FIXME: configure this
		//	String orderBy = "relevance"; 
		final String orderBy = FLD_DATE; 
		
		final BooleanQuery mainQuery       = new BooleanQuery();
		final BooleanQuery searchQuery     = new BooleanQuery();
		final BooleanQuery accessModeQuery = new BooleanQuery();
		final BooleanQuery privatePostQuery= new BooleanQuery();
		
		//--------------------------------------------------------------------
		// search terms
		//--------------------------------------------------------------------
		final Query searchTermQuery = parseSearchQuery(FLD_TITLE, searchTerms);
		searchQuery.add(searchTermQuery, Occur.SHOULD);

		//--------------------------------------------------------------------
		// allowed groups
		//--------------------------------------------------------------------
		for (final String groupName : allowedGroups) {
			final Query groupQuery = new TermQuery(new Term(FLD_GROUP, groupName));
			accessModeQuery.add(groupQuery, Occur.SHOULD);
		}
		
		//--------------------------------------------------------------------
		// private post query
		//--------------------------------------------------------------------
		if (present(userName)) {
			final BooleanQuery privatePostGroups = new BooleanQuery();
			privatePostGroups.add(new TermQuery(new Term(FLD_GROUP, GroupID.PRIVATE.name().toLowerCase())), Occur.SHOULD);
			privatePostGroups.add(new TermQuery(new Term(FLD_GROUP, GroupID.FRIENDS.name().toLowerCase())), Occur.SHOULD);
			privatePostQuery.add(privatePostGroups, Occur.MUST);
			privatePostQuery.add(new TermQuery(new Term(FLD_USER, userName)), Occur.MUST);
			accessModeQuery.add(privatePostQuery, Occur.SHOULD);
		}

		//--------------------------------------------------------------------
		// post owned by user
		//--------------------------------------------------------------------
		if (present(requestedUserName)) {
			mainQuery.add(new TermQuery(new Term(FLD_USER, requestedUserName)), Occur.MUST);
		}
		
		//--------------------------------------------------------------------
		// post owned by group
		//--------------------------------------------------------------------
		if ( present(group) ) {
			mainQuery.add( new TermQuery(new Term(FLD_GROUP, group)), Occur.MUST );
		}
		
		//--------------------------------------------------------------------
		// build final query
		//--------------------------------------------------------------------
		mainQuery.add(searchQuery, Occur.MUST);
		if (!(present(userName) && userName.equals(requestedUserName))) {
			mainQuery.add(accessModeQuery, Occur.MUST);
		}
		
		log.debug("[Full text] Search query: " + mainQuery.toString());

		//--------------------------------------------------------------------
		// set ordering
		//--------------------------------------------------------------------
		Sort sort = null;
		if (PARAM_RELEVANCE.equals(orderBy)) {
			sort = new Sort(SortField.FIELD_SCORE,new SortField(FLD_DATE, SortField.LONG,true));
		} else { 
			// orderBy=="date"
			// FIXME: why does the default operator depend on the ordering
			// myParser.setDefaultOperator(QueryParser.Operator.AND);
			sort = new Sort(new SortField(FLD_DATE,SortField.LONG,true));
		}
		
		// at last build the container
		final QuerySortContainer qf = new QuerySortContainer();
		qf.setQuery(mainQuery);
		qf.setSort(sort);
		
		return qf;
	}

	/** 
	 * analyzes given input parameter
	 * 
	 * @param fieldName the name of the field
	 * @param param the value of the field
	 * @return the analyzed string
	 * @throws IOException
	 */
	protected String parseToken(final String fieldName, final String param) throws IOException {
	    if (present(param)) {
		// use lucene's new token stream api (see org.apache.lucene.analysis' javadoc at package level)
		final TokenStream ts = this.getAnalyzer().tokenStream(fieldName, new StringReader(param));
		final TermAttribute termAtt = ts.addAttribute(TermAttribute.class);
		ts.reset();

		// analyze the parameter - that is: concatenate its normalized tokens
		final StringBuilder analyzedString = new StringBuilder();
		while (ts.incrementToken()) {
            		analyzedString.append(" ").append(termAtt.term());
		}

		return analyzedString.toString().trim();
	    }
		
	    return "";
	}
	
	/**
	 * build full text query for given query string
	 * 
	 * @param fieldName
	 * @param searchTerms
	 * @return the search query
	 */
	protected Query parseSearchQuery(final String fieldName, String searchTerms) {
		// parse search terms for handling phrase search
		final QueryParser searchTermParser = new QueryParser(LUCENE_24, fieldName, getAnalyzer());
		// FIXME: configure default operator via spring
		searchTermParser.setDefaultOperator(getDefaultSearchTermJunctor());
		Query searchTermQuery = null;
		try {
			// disallow field specification in search query
			searchTerms = searchTerms.replace(CFG_LUCENE_FIELD_SPECIFIER, "\\"+CFG_LUCENE_FIELD_SPECIFIER);
			searchTermQuery = searchTermParser.parse(searchTerms);
		} catch (final ParseException e) {
			searchTermQuery = new TermQuery(new Term(fieldName, searchTerms) );
		}
		
		return searchTermQuery;
	}	
	
	/**
	 * @return get managed resource name
	 */
	protected abstract String getResourceName();

	/**
	 * @return the dbLogic
	 */
	public LuceneDBInterface<R> getDbLogic() {
		return dbLogic;
	}

	/**
	 * @param dbLogic the dbLogic to set
	 */
	public void setDbLogic(final LuceneDBInterface<R> dbLogic) {
		this.dbLogic = dbLogic;
	}

	/**
	 * @return the analyzer
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer the analyzer to set
	 */
	public void setAnalyzer(final Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * @return the defaultSearchTermJunctor
	 */
	public Operator getDefaultSearchTermJunctor() {
		return defaultSearchTermJunctor;
	}

	/**
	 * @param defaultSearchTermJunctor the defaultSearchTermJunctor to set
	 */
	public void setDefaultSearchTermJunctor(final Operator defaultSearchTermJunctor) {
		this.defaultSearchTermJunctor = defaultSearchTermJunctor;
	}

	/**
	 * @return the resourceConverter
	 */
	public LuceneResourceConverter<R> getResourceConverter() {
		return resourceConverter;
	}

	/**
	 * @param resourceConverter the resourceConverter to set
	 */
	public void setResourceConverter(final LuceneResourceConverter<R> resourceConverter) {
		this.resourceConverter = resourceConverter;
	}

	/**
	 * @return the indexId
	 */
	public int getIndexId() {
		return indexId;
	}

	/**
	 * @param indexId the indexId to set
	 */
	public void setIndexId(final int indexId) {
		this.indexId = indexId;
	}
}
