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

package org.bibsonomy.webapp.config;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Bean for managing runtime configuration of the authorization process.
 * 
 * @author folke
 * @author dzo
 * @version $Id: AuthenticationFilterConfigurator.java,v 1.5 2011-03-16 13:26:30 nosebrain Exp $
 */
public class AuthenticationFilterConfigurator implements BeanPostProcessor {
	
	private static final String FILTERCHAIN_BEAN_NAME = "org.springframework.security.filterChainProxy";
	
	/**
	 * additional filters are added into the filter chain AFTER the {@link LogoutFilter}
	 * TODO: configure via spring 
	 */
	private static final Class<LogoutFilter> ENTRYPOINT_FILTER = LogoutFilter.class;

	/**
	 * RememberMe filters are added into the filter chain BEFORE the {@link AnonymousAuthenticationFilter} 
	 * TODO: configure via spring 
	 */
	private static final Class<AnonymousAuthenticationFilter> REMEMBERME_ENTRYPOINT_FILTER = AnonymousAuthenticationFilter.class;
	
	/**
	 * look for given filter in given list of filters and return its position if found,
	 * null otherwise
	 * 
	 * @param filterClass requested filter
	 * @param filterList list of filters
	 * @return filter's position in filter list if found, null otherwise
	 */
	private static int findFilter(final Class<? extends GenericFilterBean> filterClass, final List<Filter> filterList) {
		for (int i = 0; i < filterList.size(); i++ ) {
			final Filter filter = filterList.get(i);
			if (filterClass.isAssignableFrom(filter.getClass())) {
				return i;
			}
		}
		
		// not found
		return -1;
	}
	

	/** dertermines which authentication methods are used */
	private AuthConfig config;

	/**
	 * all known filters
	 */
	private Map<AuthMethod, Filter> authFilterMap = new HashMap<AuthMethod, Filter>();
	private Map<AuthMethod, Filter> authRememberMeFilterMap = new HashMap<AuthMethod, Filter>();
	private Map<AuthMethod, Filter> authPreFilterMap = new HashMap<AuthMethod, Filter>();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof FilterChainProxy && FILTERCHAIN_BEAN_NAME.equals(beanName)) {
			final FilterChainProxy proxy = (FilterChainProxy) bean;
			final Map<String, List<Filter>> filterChainMap = proxy.getFilterChainMap();
			
			for (final Entry<String, List<Filter>> list : filterChainMap.entrySet()) {
				final List<Filter> filterList = list.getValue();
				if (!filterList.isEmpty()) {
					// get all filters for each auth method
					final List<Filter> filters = new LinkedList<Filter>();
					final List<Filter> preFilters = new LinkedList<Filter>();
					final List<Filter> rememberMeFilters = new LinkedList<Filter>();
					for (final AuthMethod method : this.config.getAuthOrder()) {
						final Filter filter = this.authFilterMap.get(method);
						if (present(filter)) {
							filters.add(filter);
						}
							
						final Filter preFilter = this.authPreFilterMap.get(method);
						if (present(preFilter)) {
							preFilters.add(preFilter);
						}
							
						final Filter rememberMeFilter = this.authRememberMeFilterMap.get(method);
						if (present(rememberMeFilter)) {
							rememberMeFilters.add(rememberMeFilter);
						}
					}
					
					// TODO: preFilters

					// additional filters are added into the filter chain AFTER the {@link LogoutFilter}
					final int filterEntryPoint = findFilter(ENTRYPOINT_FILTER, filterList) + 1;
					filterList.addAll(filterEntryPoint, filters);
					
					// RememberMe filters are added into the filter chain BEFORE the {@link AnonymousAuthenticationFilter}
					final int rememberMeEntryPoint = findFilter(REMEMBERME_ENTRYPOINT_FILTER, filterList);
					filterList.addAll(rememberMeEntryPoint, rememberMeFilters);
				}
			}
			
			// set the new filter chain map
			proxy.setFilterChainMap(filterChainMap);
		}
		
		return bean;
	}
	
	/**
	 * @param config the config to set
	 */
	public void setConfig(AuthConfig config) {
		this.config = config;
	}
	
	/**
	 * @param authPreFilterMap the authPreFilterMap to set
	 */
	public void setAuthPreFilterMap(final Map<AuthMethod, Filter> authPreFilterMap) {
		this.authPreFilterMap = authPreFilterMap;
	}

	/**
	 * @param authFilterMap the authFilterMap to set
	 */
	public void setAuthFilterMap(final Map<AuthMethod, Filter> authFilterMap) {
		this.authFilterMap = authFilterMap;
	}
	
	/**
	 * @param authRememberMeFilterMap the authRememberMeFilterMap to set
	 */
	public void setAuthRememberMeFilterMap(final Map<AuthMethod, Filter> authRememberMeFilterMap) {
		this.authRememberMeFilterMap = authRememberMeFilterMap;
	}
}
