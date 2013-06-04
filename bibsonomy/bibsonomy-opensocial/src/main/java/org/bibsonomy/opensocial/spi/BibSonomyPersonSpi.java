package org.bibsonomy.opensocial.spi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.core.model.PersonImpl;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.database.ShindigLogicInterfaceFactory;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;

public class BibSonomyPersonSpi implements PersonService {
	private ShindigLogicInterfaceFactory dbLogicFactory;

	public Future<Person> getPerson(UserId userId, Set<String> fields,
			SecurityToken token) throws ProtocolException {
		LogicInterface dbLogic = this.dbLogicFactory.getLogicAccess(token);
		Person person = makePerson(getBibSonomyUser(userId, dbLogic, token));
		return ImmediateFuture.newInstance(person);
	}

	public Future<RestfulCollection<Person>> getPeople(Set<UserId> userIds,
			GroupId groupId, CollectionOptions collectionOptions,
			Set<String> fields, SecurityToken token) throws ProtocolException {
		LogicInterface dbLogic = this.dbLogicFactory.getLogicAccess(token);
		try {
			List<Person> people = new ArrayList();
			switch (groupId.getType().ordinal()) {
			case 1:
				for (UserId userId : userIds) {
					User dbUser = getBibSonomyUser(userId, dbLogic, token);
					Person person = makePerson(dbUser);
					people.add(person);
				}
				break;
			case 2:
				List<User> friends = getBibSonomyFriends(userIds, groupId,
						dbLogic, token);
				for (User user : friends) {
					Person person = makePerson(user);
					people.add(person);
				}
				break;
			case 3:
				throw new ProtocolException(501, "Not yet implemented");
			case 4:
				throw new ProtocolException(501, "Not yet implemented");
			case 5:
				throw new ProtocolException(501, "Not yet implemented");
			default:
				throw new ProtocolException(400, "Group ID not recognized");
			}
			return ImmediateFuture.newInstance(new RestfulCollection<Person>(
					people, 0, people.size()));
		} catch (Exception e) {
			throw new ProtocolException(500, "Exception occurred", e);
		}
	}

	private User getBibSonomyUser(UserId userId, LogicInterface dbLogic,
			SecurityToken token) {
		String userName = getUserName(userId, token);

		User dbUser = dbLogic.getUserDetails(userName);

		return dbUser;
	}

	private List<User> getBibSonomyFriends(Set<UserId> userIds,
			GroupId groupId, LogicInterface dbLogic, SecurityToken token) {
		List<User> friends = new LinkedList();
		for (UserId user : userIds) {
			String userName = getUserName(user, token);
			List<User> dbFriends = dbLogic.getUserRelationship(userName,
					UserRelation.OF_FRIEND, null);
			friends.addAll(dbFriends);
		}

		List retVal = new LinkedList();
		for (User user : friends) {
			User dbUser = dbLogic.getUserDetails(user.getName());
			retVal.add(dbUser);
		}

		return retVal;
	}

	private Person makePerson(User dbUser) {
		Person person = new PersonImpl();
		person.setId(dbUser.getName());
		person.setThumbnailUrl("http://www.bibsonomy.org/picture/user/"
				+ dbUser.getName());
		if (dbUser.getRealname() != null)
			person.setDisplayName(dbUser.getRealname());
		else {
			person.setDisplayName(dbUser.getName());
		}
		return person;
	}

	private String getUserName(UserId userId, SecurityToken token) {
		String userName;
		switch (userId.getType().ordinal()) {
		case 1:
			userName = token.getViewerId();
			break;
		case 2:
			userName = userId.getUserId();
			break;
		case 3:
			userName = token.getViewerId();
			break;
		case 4:
		default:
			throw new ProtocolException(501, "UserType '"
					+ userId.getType().name() + "' not yet implemented");
		}

		return userName;
	}

	public void setDbLogicFactory(ShindigLogicInterfaceFactory dbLogicFactory) {
		this.dbLogicFactory = dbLogicFactory;
	}

	public ShindigLogicInterfaceFactory getDbLogicFactory() {
		return this.dbLogicFactory;
	}
}