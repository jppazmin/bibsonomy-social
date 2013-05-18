/**
 *
 *  BibSonomy-Web-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.util.spring.security;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import org.bibsonomy.common.enums.Role;
import org.bibsonomy.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Wrapper around our user object to make it available as UserDetails.
 * 
 * @author dzo
 * @version $Id: UserAdapter.java,v 1.1 2011-03-04 16:42:45 folke Exp $
 */
public class UserAdapter implements UserDetails {
	private static final long serialVersionUID = -3926600488722547211L;
	
	private final User user;

	/**
	 * @param user the user to adapt
	 */
	public UserAdapter(final User user) {
		this.user = user;
	}
	
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		final Collection<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
		authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
		if (Role.ADMIN.equals(this.user.getRole())) {
			authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
		}
		
		return Collections.unmodifiableCollection(authorities);
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !Role.DELETED.equals(this.user.getRole()) ;
	}
	
	@Override
	public String toString() {
		return user == null ? "" : user.toString(); 
	}
}
