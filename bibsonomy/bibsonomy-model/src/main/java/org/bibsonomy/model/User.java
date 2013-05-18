/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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


package org.bibsonomy.model;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bibsonomy.common.enums.Role;
import org.bibsonomy.util.UrlUtils;

/**
 * This class defines a user. An unknown user has an empty (<code>null</code>) name.
 * 
 * @version $Id: User.java,v 1.57 2011-07-25 11:44:06 folke Exp $
 */
public class User implements Serializable {
	/*
	 * XXX: When adding new fields make sure to integrate them into the updateUser method
	 * {@link UserUtils#updateUser}
	 */

	/**
	 * For persistency (Serializable)
	 */
	private static final long serialVersionUID = -4494680395320981307L;

	/**
	 * The (nick-)name of this user. Is <code>null</code> if the user is not logged in (unknown). 
	 */
	private String name;
	/**
	 * This user's password
	 */
	private String password;
	/**
	 * The Api Key for this user
	 */
	private String apiKey;
	/**
	 * Which role the user has in the system (e.g. admin, ...)
	 */
	private Role role;

	
	/* ****************************** profile ****************************** */ 
	
	/**
	 * The (real-)name of this user.
	 */
	private String realname;
	/**
	 * This user's email address.
	 */
	private String email;
	/**
	 * Ths {@link URL} to this user's homepage.
	 */
	private URL homepage;
	/**
	 * birthday
	 */
	private Date birthday;
	/**
	 * Gender
	 */
	private String gender;
	/**
	 * Profession
	 */
	private String profession;
	/**
	 * Institution (company, etc.)
	 */
	private String institution;
	/**
	 * Interests
	 */
	private String interests;
	/**
	 * Hobbies
	 */
	private String hobbies;
	/**
	 * Location of this user
	 */
	private String place;
	/**
	 * OpenURL url
	 */
	private String openURL;

	/* ****************************** system properties ****************************** */
	/**
	 * The user belongs to these groups.
	 */
	private List<Group> groups;
	/**
	 * Holds the friends of this user
	 */
	private List<User> friends;
	/**
	 * Those are the posts of this user.
	 */
	private List<Post<? extends Resource>> posts;
	/**
	 * List of tags which were assigned to this user via a tagged relationship
	 */
	private List<Tag> tags;
	/**
	 * the settings of this user
	 */
	private UserSettings settings;
	/**
	 * Basket of this user where he can pick some entries
	 */
	private Basket basket;
	/**
	 * Inbox of this user where he gets Posts sent by other users
	 */
	private Inbox inbox;


	/* ****************************** classification ****************************** */ 

	/**
	 * Indicates if this user is a spammer.
	 */
	private Boolean spammer;
	/**
	 * who updated state of the user
	 */
	private String updatedBy;
	/**
	 * date of update
	 */
	private Date updatedAt; 
	/**
	 * flag if the classifier should take this user
	 * into account for classification
	 */
	private Integer toClassify;
	/**
	 * The classification algortihm the user was classified with
	 */
	private String algorithm;
	/**
	 * The spammer prediction of the classifier
	 */
	private Integer prediction;
	/**
	 * The confidence of the classifier
	 */
	private Double confidence;
	/** 
	 * The mode of the classifier (day or night)
	 */ 
	private String mode;
	
	
	/* ****************************** account management ****************************** */
	
	/**
	 * The Activation Code
	 */
	private String activationCode;
	/**
	 * The {@link Date} when this user registered to bibsonomy.
	 */
	private Date registrationDate;
	/**
	 * IP Address
	 */
	private String IPAddress;
	/**
	 * OpenID url for authentication
	 */
	private String openID;
	/**
	 * LDAP userId for authentication
	 */
	private String ldapId;
	/**
	 * The temporary password the user can request when asking for a password reminder.
	 */
	private String reminderPassword;
	/**
	 * The time at which the user requested a password reminder.
	 */
	private Date reminderPasswordRequestDate;
	
	/**
	 * Constructor
	 */
	public User() {
		this.basket = new Basket();
		this.inbox = new Inbox();
		this.settings = new UserSettings();
		this.role = Role.NOBODY; // TODO: check, if this has any bad implications!
	}

	/**
	 * Constructor
	 * 
	 * @param name the name of the user
	 */
	public User(final String name) {
		this();
		this.setName(name); 
	}

	/**
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return homepage
	 */
	public URL getHomepage() {
		return this.homepage;
	}

	/**
	 * @param homepage
	 */
	public void setHomepage(URL homepage) {
		this.homepage = homepage;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return this.name;	
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name == null ? null : name.toLowerCase();
	}

	/**
	 * @return realname
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * @param realname
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/**
	 * @return registrationDate
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return groups
	 */
	public List<Group> getGroups() {
		if (this.groups == null) {
			this.groups = new LinkedList<Group>();
		}
		return this.groups;
	}

	/**
	 * @param groups
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	/**
	 * Convenience method to add a group.
	 * 
	 * @param group
	 */
	public void addGroup(final Group group) {
		// call getGroups to initialize this.groups
		this.getGroups();
		this.groups.add(group);
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return apiKey
	 */
	public String getApiKey() {
		return this.apiKey;
	}

	/**
	 * @param apiKey
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return settings
	 */
	public UserSettings getSettings() {
		return this.settings;
	}

	/**
	 * @param settings
	 */
	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}

	/**
	 * @return IPAddress
	 */
	public String getIPAddress() {
		return this.IPAddress;
	}

	/**
	 * @param IPAddress
	 */
	public void setIPAddress(String IPAddress) {
		this.IPAddress = IPAddress;
	}

	/**
	 * @return birthday
	 */
	public Date getBirthday() {
		return this.birthday;
	}

	/**
	 * @param birthday
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return gender
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * @param gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return profession
	 */
	public String getProfession() {
		return this.profession;
	}

	/**
	 * @param profession
	 */
	public void setProfession(String profession) {
		this.profession = profession;
	}

	/**
	 * @return interests
	 */
	public String getInterests() {
		return this.interests;
	}

	/**
	 * @param interests
	 */
	public void setInterests(String interests) {
		this.interests = interests;
	}

	/**
	 * @return hobbies
	 */
	public String getHobbies() {
		return this.hobbies;
	}

	/**
	 * @param hobbies
	 */
	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}

	/**
	 * @return place
	 */
	public String getPlace() {
		return this.place;
	}

	/**
	 * @param place
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * The spammer property can have three states:
	 * <dl>
	 * <dt><code>null</code></dd>
	 * <dd>
	 *  The spam status hasn't been set in this object, i.e., we don't
	 *  know it and don't want to change it.  
	 *  <br/>
	 *  It can never be <code>null</code> for users coming from the 
	 *  DBLogic, since in the DB the property is either 
	 *  <code>true</code> or <code>false</code>.
	 * </dd>
	 * <dt><code>true</code></dt>
	 * <dd>This user is a spammer, for sure.</dd>
	 * <dt><code>false</code></dt>
	 * <dd>This user not a spammer or not yet.</dd>
	 * <dd></dd>
	 * </dl>
	 * @return spammer
	 */
	public Boolean getSpammer() {
		return this.spammer;
	}
	
	/**
	 * @return <code>true</code> if and only if spammer == true.
	 */
	public boolean isSpammer() {
		return this.spammer == null ? false : this.spammer;
	}

	/**
	 * @param spammer
	 */
	public void setSpammer(Boolean spammer) {
		this.spammer = spammer;
	}

	/**
	 * @return openURL
	 */
	public String getOpenURL() {
		return this.openURL;
	}

	/**
	 * @param openURL
	 */
	public void setOpenURL(String openURL) {
		this.openURL = openURL;
	}
	
	/**
	 * @return openID
	 */
	public String getOpenID() {
		return this.openID;
	}

	/**
	 * @param openID
	 */
	public void setOpenID(String openID) {
		this.openID = UrlUtils.normalizeURL(openID);
	}

	/**
	 * @param ldapId
	 */
	public void setLdapId(String ldapId) {
		this.ldapId = ldapId;
	}

	/**
	 * @return ldap user id
	 */
	public String getLdapId() {
		return ldapId;
	}

	/**
	 * @return basket
	 */
	public Basket getBasket() {
		return this.basket;
	}

	/**
	 * @param basket
	 */
	public void setBasket(Basket basket) {
		this.basket = basket;
	}
	
	/**
	 * @return inbox
	 */
	public Inbox getInbox() {
		return this.inbox;
	}

	/**
	 * @param inbox
	 */
	public void setInbox(Inbox inbox) {
		this.inbox = inbox;
	}
	
	/** 
	 * @return activationCode
	 */
	public String getActivationCode() {
		return this.activationCode;
	}

	/**
	 * @param activationCode
	 */
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
	/**
	 * @return a List of friends
	 */
	public List<User> getFriends() {
		if (this.friends == null) {
			this.friends = new LinkedList<User>();
		}
		return this.friends;
	}
	
	/**
	 * TODO: unused?
	 * @return a List with names of user's friends
	 */
	public List<String> getFriendsAsString() {
		if (this.friends == null) {
			this.friends = new LinkedList<User>();
		}
		final List<String> friendsAsString = new LinkedList<String>();
		for (final User friend : friends) {
			friendsAsString.add(friend.getName());
		}
		return friendsAsString;
	}

	/**
	 * @param friend
	 */
	public void addFriend(final User friend) {
		// call getFriends to initialize this.friends
		this.getFriends();
		this.friends.add(friend);
	}

	/**
	 * 
	 * @param friends
	 */
	public void addFriends(final List<User> friends) {
		// call getFriends to initialize this.friends
		this.getFriends();
		this.friends.addAll(friends);
	}
	
	
	/**
	 * @return The role of the user.
	 */
	public Role getRole() {
		return this.role;
	}

	/**
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return Classification algorithm the user was classified with
	 */
	public String getAlgorithm() {
		return this.algorithm;
	}

	/**
	 * @param algorithm classification algorithm
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @return prediction of classifier
	 */
	public Integer getPrediction() {
		return this.prediction;
	}

	/**
	 * @param prediction Prediction
	 */
	public void setPrediction(Integer prediction) {
		this.prediction = prediction;
	}
	
	/**
	 * @return confidence of classifier
	 */
	public Double getConfidence() {
		return this.confidence;
	}
	
	/**
	 * @param confidence Confidence
	 */
	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}

	/**
	 * @return if user is considered for classification
	 */
	public Integer getToClassify() {
		return this.toClassify;
	}

	/**
	 * @param toClassify if user should be classified
	 */
	public void setToClassify(Integer toClassify) {
		this.toClassify = toClassify;
	}

	/**
	 * @return person who updates user dataset
	 */
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	/**
	 * @param updatedBy person who updates user dataset
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return Date of update
	 */
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	/**
	 * @param updatetAt date of update
	 */
	public void setUpdatedAt(Date updatetAt) {
		this.updatedAt = updatetAt;
	}

	/**
	 * @return mode
	 */
	public String getMode() {
		return this.mode;
	}

	/**
	 * @param mode
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return The temporary password the user can request when asking for a password reminder.
	 * @see #getReminderPasswordRequestDate()
	 */
	public String getReminderPassword() {
		return this.reminderPassword;
	}

	/** Set the temporary password the user can request when asking for a password reminder.
	 * @param reminderPassword
	 * @see #getReminderPasswordRequestDate()
	 */
	public void setReminderPassword(String reminderPassword) {
		this.reminderPassword = reminderPassword;
	}

	/**
	 * @return The time at which the user requested a password reminder.
	 * @see #getReminderPassword()
	 */
	public Date getReminderPasswordRequestDate() {
		return this.reminderPasswordRequestDate;
	}

	/** Set the time at which the user requested a password reminder.
	 * @param reminderPasswordRequestDate
	 * @see #setReminderPassword(String)
	 */
	public void setReminderPasswordRequestDate(Date reminderPasswordRequestDate) {
		this.reminderPasswordRequestDate = reminderPasswordRequestDate;
	}
	
	/**
	 * Two users are equal, if their name is equal. Users with <code>null</code>
	 * names are not equal.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		return obj != null && obj instanceof User && this.name != null && this.name.equals(((User) obj).name);
	}
	
	@Override
	public int hashCode() {
		if (this.name != null) return this.name.hashCode();
		return super.hashCode();
	}

	/**
	 * @return institution
	 */
	public String getInstitution() {
		return this.institution;
	}

	/**
	 * @param institution
	 */
	public void setInstitution(String institution) {
		this.institution = institution;
	}

	/**
	 * @return posts
	 */
	public List<Post<? extends Resource>> getPosts() {
		if (this.posts == null) {
			this.posts = new LinkedList<Post<? extends Resource>>();
		}
		return this.posts;
	}

	/**
	 * @param posts
	 */
	public void setPosts(List<Post<? extends Resource>> posts) {
		this.posts = posts;
	}

	/**
	 * @param tags List of tags which were assigned to this user via a tagged relationship
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * @return List of tags which were assigned to this user via a tagged relationship 
	 */
	public List<Tag> getTags() {
		return tags;
	}

	@Override
	public String toString() {
		return name;
	}
	
}