package org.bibsonomy.opensocial.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MockPersonSpi implements PersonService {
	private static final UserId JOHN = new UserId(UserId.Type.userId,
			"john.doe");

	private static final UserId JANE = new UserId(UserId.Type.userId,
			"jane.doe");

	private static final UserId[] FRIENDS = { JOHN, JANE };
	HttpServletRequest req;

	public Future<Person> getPerson(UserId userId, Set<String> fields,
			SecurityToken token) throws ProtocolException {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		Person person = new PersonImpl();
		person.setId(userId.getUserId());
		person.setEthnicity("youDontKnow");
		person.setAboutMe("Test");
		person.setDisplayName(userId.getUserId());
		return ImmediateFuture.newInstance(person);
	}

	public Future<RestfulCollection<Person>> getPeople(Set<UserId> userIds,
			GroupId groupId, CollectionOptions collectionOptions,
			Set<String> fields, SecurityToken token) throws ProtocolException {
		try {
			List<Person> people = new ArrayList<Person>();
			switch (groupId.getType().ordinal()) {
			case 1:
				for (UserId userId : userIds) {
					Person person = new PersonImpl();
					person.setId(userId.getUserId());
					person.setDisplayName(userId.getUserId());
					person.setEthnicity("youDontKnow");
					person.setAboutMe("Test");
					people.add(person);
				}
				break;
			case 2:
				for (UserId userId : FRIENDS) {
					Person person = new PersonImpl();
					person.setId(userId.getUserId());
					person.setDisplayName(userId.getUserId());
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
}