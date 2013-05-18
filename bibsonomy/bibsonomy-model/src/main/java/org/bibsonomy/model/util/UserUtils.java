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

package org.bibsonomy.model.util;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.User;
import org.bibsonomy.util.HashUtils;

/**
 * @author Dominik Benz
 * @author Miranda Grahl
 * @version $Id: UserUtils.java,v 1.32 2011-04-29 06:45:01 bibsonomy Exp $
 */
public class UserUtils {


	/** Checks, if the given user is the special DBLP user 
	 * (which has some special rights).
	 *  
	 * @param user
	 * @return <code>true</code>, if <code>user</code> is the DBLP user.
	 */
	public static boolean isDBLPUser(final User user) {
		return isDBLPUser(user.getName());
	}


	/** Checks, if the given user name is the special DBLP user 
	 * (which has some special rights).
	 *  
	 * @param userName - the name of the user in question.
	 * @return <code>true</code>, if <code>user</code> is the DBLP user.
	 */
	public static boolean isDBLPUser(final String userName) {
		return "dblp".equalsIgnoreCase(userName);
	}

	/**
	 * Generates an Api key with a MD5 message digest from a random number.
	 * 
	 * @return String Api key
	 */
	public static String generateApiKey() {
		return HashUtils.getMD5Hash(generateRandom());
	}

	private static byte[] generateRandom() {
		final byte[] randomBytes = new byte[32];
		try {
			new Random().nextBytes(randomBytes);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return randomBytes;
	}


	/**
	 * Helper function to set a user's groups by a list of group IDs
	 * 
	 * @param user
	 * @param groupIDs
	 */
	public static void setGroupsByGroupIDs(final User user, final List<Integer> groupIDs) {
		for (final int groupID : groupIDs) {
			user.addGroup(new Group(groupID));
		}
	}
	
	/**
	 * Generates the ActivationCode for a specific user.
	 * 
	 * @param user
	 * @return activationCode
	 */
	public static String generateActivationCode(final User user) {
		String prepareStatement = user.getName() + user.getIPAddress() + user.getApiKey() + new String(generateRandom());
		return HashUtils.getMD5Hash(prepareStatement.getBytes());
	}

	/**
	 * Helper function to get a list of group IDs from the user's list of groups
	 * 
	 * @param user
	 * @return list of groupIDs extracted from the given user's list of groups
	 */
	public static List<Integer> getListOfGroupIDs(final User user) {
		final List<Integer> groupIDs = new ArrayList<Integer>();
		final List<Group> groups = getListOfGroups(user);
		for (final Group group : groups) {
			groupIDs.add(group.getGroupId());
		}
		return groupIDs;
	}

	/**
	 * Helper function to get a list of groups from the user's list of groups
	 * 
	 * @param user
	 * @return list of groups extracted from the given user's list of groups
	 */
	public static List<Group> getListOfGroups(final User user) {
		final List<Group> groups = new ArrayList<Group>();
		/*
		 * every user may see public posts
		 */ 
		groups.add(new Group(GroupID.PUBLIC));
		if (user == null) {
			return groups;
		}
		groups.addAll(user.getGroups());
		return groups;
	}	

	/**
	 * Check whether the user is a group by comparing his name with the names
	 * of all groups he belongs to. If a group exists with the user's name, the
	 * user is a group.
	 * 
	 * @param user
	 * @return boolean 
	 */
	public static boolean userIsGroup(final User user) {
		if (user == null) return false;

		final String userName = user.getName();
		final List<Group> groups = user.getGroups();

		/*
		 * iterate over groups and check whether the user name equals a group name
		 */
		if (groups != null) {
			for (final Group group: groups) {
				if (userName.equalsIgnoreCase(group.getName())) {
					return true;
				}
			}
		}

		return false;
	}
	
	
	/** Update a user:
	 * In the existingUser all fields, that are set in updatedUser will be overwritten
	 * Warning: UserSettings are not Updated!
	 * @param existingUser = the user before the update
	 * @param updatedUser = the user with updated fields
	 * 
	 */
	public static void updateUser(final User existingUser, final User updatedUser) {
		// FIXME if existingUser should copy all properties from the one bean to the
		// other we might want to come up with a more generic version of existingUser
		// code block - so if we add a field to the User bean we don't have to
		// remember adding it here
		// The problem with that idea is, that NOT ALL properties are updated (e.g. name, registrationDate)
		existingUser.setEmail(		!present(updatedUser.getEmail()) 	? existingUser.getEmail() 	: updatedUser.getEmail());
		existingUser.setPassword(	!present(updatedUser.getPassword()) 	? existingUser.getPassword() 	: updatedUser.getPassword());		
		existingUser.setRealname(	!present(updatedUser.getRealname()) 	? existingUser.getRealname() 	: updatedUser.getRealname());		
		existingUser.setHomepage(	!present(updatedUser.getHomepage()) 	? existingUser.getHomepage() 	: updatedUser.getHomepage());
		existingUser.setApiKey(		!present(updatedUser.getApiKey()) 	? existingUser.getApiKey()	: updatedUser.getApiKey());		
		existingUser.setBirthday(	!present(updatedUser.getBirthday()) 	? existingUser.getBirthday() 	: updatedUser.getBirthday());
		existingUser.setGender(		!present(updatedUser.getGender()) 	? existingUser.getGender() 	: updatedUser.getGender());
		existingUser.setHobbies(	!present(updatedUser.getHobbies()) 	? existingUser.getHobbies() 	: updatedUser.getHobbies());
		existingUser.setInterests(	!present(updatedUser.getInterests()) 	? existingUser.getInterests() 	: updatedUser.getInterests());
		existingUser.setIPAddress(	!present(updatedUser.getIPAddress()) 	? existingUser.getIPAddress() 	: updatedUser.getIPAddress());
		existingUser.setOpenURL(	!present(updatedUser.getOpenURL()) 	? existingUser.getOpenURL() 	: updatedUser.getOpenURL());
		existingUser.setPlace(		!present(updatedUser.getPlace()) 	? existingUser.getPlace() 	: updatedUser.getPlace());
		existingUser.setProfession(	!present(updatedUser.getProfession()) 	? existingUser.getProfession()  : updatedUser.getProfession());
		existingUser.setInstitution(	!present(updatedUser.getInstitution()) 	? existingUser.getInstitution() : updatedUser.getInstitution());
		
		existingUser.setOpenID(		!present(updatedUser.getOpenID())       ? existingUser.getOpenID()      : updatedUser.getOpenID());
		existingUser.setLdapId(		!present(updatedUser.getLdapId())       ? existingUser.getLdapId()      : updatedUser.getLdapId());
		
		existingUser.setSpammer(	!present(updatedUser.getSpammer()) 	? existingUser.getSpammer() 	: updatedUser.getSpammer());
		existingUser.setRole(		!present(updatedUser.getRole()) 	? existingUser.getRole()		: updatedUser.getRole());

		existingUser.setUpdatedBy(	!present(updatedUser.getUpdatedBy()) 	? existingUser.getUpdatedBy() 	: updatedUser.getUpdatedBy());
		existingUser.setUpdatedAt(	!present(updatedUser.getUpdatedAt()) 	? existingUser.getUpdatedAt() 	: updatedUser.getUpdatedAt());

		existingUser.setReminderPassword(			!present(updatedUser.getReminderPassword()) 			? existingUser.getReminderPassword() : updatedUser.getReminderPassword());
		existingUser.setReminderPasswordRequestDate(!present(updatedUser.getReminderPasswordRequestDate()) 	? existingUser.getReminderPasswordRequestDate() : updatedUser.getReminderPasswordRequestDate());

	}

}