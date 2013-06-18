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

package filters;


///**
// * This Filter reads user information/settings from DB or Cookies and makes it
// * available for following filters/servlets/JSPs.
// * 
// */
public class InitUserFilter { // implements Filter {
//	private final static Log log = LogFactory.getLog(InitUserFilter.class);
//	
//	/*
//	 * All X.509 users get the same password in the database, since it is never
//	 * used for authentication.
//	 */
//	@Deprecated
//	private static final String X509_GENERIC_PASSWORD = "*";
//	
//	/**
//	 * The filter configuration object we are associated with. If this value is
//	 * null, this filter instance is not currently configured.
//	 */
//	protected FilterConfig filterConfig = null;
//	
//	/**
//	 * TODO: improve documentation
//	 */
//	@Deprecated
//	public static final String OPENID_COOKIE_NAME = "_openIDUser";
//	
//	/**
//	 * TODO: improve documentation
//	 */
//	@Deprecated
//	public static final String REQ_ATTRIB_USER = "user";
//	
//	private static final String REQ_ATTRIB_LANGUAGE = SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME;
//	private static final String PROJECT_DEFAULT_LANGUAGE = "project.defaultLocale";
//	
	/**
	 * TODO: improve documentation
	 */
	@Deprecated
	public static final String REQ_ATTRIB_LOGIN_USER = "loginUser";
	
	/**
	 * TODO: improve documentation
	 */
	@Deprecated
	public static final String REQ_ATTRIB_LOGIN_USER_PASSWORD = "loginUserPassword";
	
//	/**
//	 * Enables X.509 authentication.
//	 */
//	public static boolean useX509forAuth = false;
//
//	/*
//	 * (non-Javadoc)
//	 * @see javax.servlet.Filter#destroy()
//	 */
//	@Override
//	public void destroy() {
//		this.filterConfig = null;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
//	 */
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//		this.filterConfig = filterConfig;
//		TODO: must be Configurable in spring security
//		/*
//		 * if true, we use X.509 certificates instead of passwords in DB for
//		 * authentication
//		 */
//		useX509forAuth = Boolean.parseBoolean(filterConfig.getInitParameter("useX509forAuth"));
//	}
//
//	/**
//	 * This method does the main work
//	 * 
//	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
//	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
//	 */
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		final String requPath = httpServletRequest.getServletPath();
//		/*
//		 * ignore resource files (CSS, JPEG/PNG, JavaScript) ...
//		 */
//		if (requPath.startsWith(STATIC_RESOURCES) || requPath.startsWith(API)) {
//			
//			//workaround that prevent the the 404 error to break because of no loginUser is set
//			//if user requests a site that doesn't exists
//			request.setAttribute(REQ_ATTRIB_LOGIN_USER, new User());
//			
//			chain.doFilter(request, response);
//			return;
//		}
//
//		/*
//		 * try to authenticate with the given username & password; if
//		 * successful, get user details
//		 */
//
//		final DBLogicUserInterfaceFactory dbLogicFactory = new DBLogicUserInterfaceFactory();
//		final IbatisDBSessionFactory ibatisDBSessionFactory = new IbatisDBSessionFactory();
//		dbLogicFactory.setDbSessionFactory(ibatisDBSessionFactory);
//		User loginUser = null;
//
//		final String userCookie = getCookie(httpServletRequest, USER_COOKIE_NAME);
//		final String openIDCookie = getCookie(httpServletRequest, OPENID_COOKIE_NAME);
//
//		// check user authentication
//		try {
//			/*
//			 * X.509 comes first, such that cookies can't override it ... (and
//			 * we can use blanco passwords in the database)
//			 */
//			if (useX509forAuth) {
//				/*
//				 * special handling for SAP X.509 certificates
//				 */
//				try {
//					log.debug("no user cookie found, trying X.509");
//					/*
//					 * get user name from client certificate
//					 */
//					final String uname = getUserName(httpServletRequest);
//					/*
//					 */
//					try {
//						/*
//						 * We use a fixed ("*") empty password - since auth is 
//						 * done using certificates. Since X.509 auth comes first,
//						 * nobody can use a Cookie to be another user.
//						 */
//						final LogicInterface logic = dbLogicFactory.getLogicAccess(uname, X509_GENERIC_PASSWORD);
//						loginUser = logic.getUserDetails(uname);
//					} catch (AccessDeniedException e) {
//						loginUser = createX509User(ibatisDBSessionFactory, uname);
//					}
//
//					/*
//					 * this should not be neccessary, if we got the user from
//					 * the database ...
//					 */
//					if (loginUser == null) {
//						loginUser = new User();
//						loginUser.setName(uname);
//					}
//				} catch (final Exception e) {
//					log.debug("Certificate authentication failed.", e);
//				}
//			} else if (userCookie != null) {
//				log.debug("found user cookie");
//				/*
//				 * user has Cookie set: try to authenticate
//				 */
//				final String userCookieParts[] = userCookie.split("%20");
//				
//				if (userCookieParts.length >= 2) {
//					final String userName = UrlUtils.safeURIDecode(userCookieParts[0]);
//					/*
//					 * all two parts of cookie available
//					 */
//					final String userPass = userCookieParts[1];
//					/*
//					 * check if user is listed in table ldapUser
//					 * if so check if it is required to authenticate again against ldap-server
//					 * if not use standard login method
//					 */
//
//					LogicInterface logic = null;
//
//					logic = dbLogicFactory.getLogicAccess(userName, userPass);
//					loginUser = logic.getUserDetails(userName);
//
//						
//					/* 
//					 * check, if it is an ldap user and if it has to re-auth agains ldap server. if so, do it.
//					 */
//					// if user database authentication was successful
//					// check if user is listed in ldapUser table
//					if (null != loginUser.getLdapId())
//					{
//					
//						// get date of last authentication against ldap server
//						Date userLastAccess = loginUser.getLastLdapUpdate();
//						
//						// TODO: get timeToReAuth from tomcat's environment, so a user can adjust it without editing code  
//						int timeToReAuth =  18 * 60 * 60; // seconds
//						Date dateNow = new Date();
//						// timeDiff is in seconds
//						long timeDiff = (dateNow.getTime() - userLastAccess.getTime())/1000;						
//						
//						log.debug("last access of user "+userName+" was on "+userLastAccess.toString()+ " ("+(timeDiff/3600)+" hours ago = "+ " ("+(timeDiff/60)+" minutes ago = "+timeDiff+" seconds ago)");
//					
//						/*
//						 *  check lastAccess - re-auth required?
//						 *  if time of last access is too far away, re-authenticate against ldap server to check
//						 *  whether password is same or user exists anymore
//						 */
//						
//						if ( timeDiff > timeToReAuth ) {
//							// re-auth
//							log.debug("last access time is up - ldap re-auth required -> throw reauthrequiredException");
//							
//							/*
//							 * check credentials against ldap server
//							 * if login is not correct redirect to login page
//							 * if it is correct use standard login method 
//							 */
//							Ldap ldap = new Ldap();
//							LdapUserinfo ldapUserinfo = new LdapUserinfo();
//							log.debug("loginUser = " + loginUser.getName());
//							log.debug("Trying to re-auth user " + userName + " via LDAP (uid="+loginUser.getLdapId()+")");
//					        ldapUserinfo = ldap.checkauth(loginUser.getLdapId(), userPass);
//					        
//							if (null == ldapUserinfo)
//							{
//								/*
//								 * user credentials do not match --> show error message
//								 * and go to login page
//								 */
//								log.debug("ra-auth of user " + userName + " failed.");
//								loginUser = null;
//							} else {
//			
//								log.debug("ra-auth of user " + userName + " succeeded.");
//			
//								loginUser = logic.getUserDetails(userName);
//		
//								// if ldap credentials are ok, update lastAccessTimestamp
//								if (null != logic.updateUser(loginUser, UserUpdateOperation.UPDATE_LDAP_TIMESTAMP)) {
//									log.debug("update LDAP-Timestamp of user " + userName + " successful");
//								} else {
//									log.error("error on updating LDAP-Timestamp of user " + userName + "!");
//								}
//							}
//							
//						}
//					}
//				} else {
//					/*
//					 * something is wrong with the cookie: log!
//					 */
//					String ip_address = ((HttpServletRequest) request).getHeader("x-forwarded-for");
//					log.warn("Someone manipulated the user cookie (IP: " + ip_address + ") : " + userCookie);
//				}
//			} else if (openIDCookie != null) {
//				log.debug("found OpenID cookie");
//
//				/*
//				 * user has OpenID cookie set
//				 */
//				final String openIdCookieParts[] = openIDCookie.split("%20");
//				if (openIdCookieParts.length >= 3) {
//					final String userName = UrlUtils.safeURIDecode(openIdCookieParts[0]);
//					final String openID = UrlUtils.safeURIDecode(openIdCookieParts[1]);
//					final String password = openIdCookieParts[2];
//
//					final HttpSession session = httpServletRequest.getSession(true);
//
//					final RequestLogic requestLogic = new RequestLogic(httpServletRequest);
//
//					final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//					final ResponseLogic responseLogic = new ResponseLogic(httpServletResponse);
//					final CookieLogic cookieLogic = new CookieLogic(requestLogic, responseLogic);
//
//					/*
//					 * check if cookie is still valid
//					 */
//					String openIDSession = (String) session.getAttribute(OpenID.OPENID_SESSION_ATTRIBUTE);
//
//					if (openIDSession != null) {
//						/*
//						 * valid OpenID session
//						 */
//						LogicInterface logic = dbLogicFactory.getLogicAccess(userName, password);
//						loginUser = logic.getUserDetails(userName);
//					} else {
//						/*
//						 * OpenID session expired --> reauthenticate at OpenID
//						 * provider
//						 */
//						StringBuffer referer = httpServletRequest.getRequestURL();
//						String queryString = requestLogic.getParametersAsQueryString();
//
//						if (queryString != null && queryString.length() > 1) referer.append(queryString);
//
//						String returnTo = InitialConfigListener.getProjectHome() + "login?referer=" + URLEncoder.encode(referer.toString(), "UTF-8");
//
//						/*
//						 * delete old openid cookie
//						 */
//						cookieLogic.deleteOpenIDCookie();
//
//						/*
//						 * redirect user to openID provider
//						 */
//						String url;
//						try {
//							url = openIDLogic.authOpenIdRequest(requestLogic, openID, InitialConfigListener.getProjectHome(), returnTo.toString(), false);
//							httpServletResponse.sendRedirect(url);
//						} catch (OpenIDException ex) {
//							log.error("OpenID provider url not valid");
//							ex.printStackTrace();
//						}
//						return;
//					}
//				} else {
//					/*
//					 * something is wrong with the cookie: log!
//					 */
//					String ip_address = ((HttpServletRequest) request).getHeader("x-forwarded-for");
//					log.warn("Someone manipulated the openid cookie (IP: " + ip_address + ") : " + openIDCookie);
//				}
//			} else {
//				final String[] auth = decodeAuthHeader(httpServletRequest);
//				if (auth != null && auth.length == 2) {
//					/*
//					 * HTTP BASIC AUTHENTICATION
//					 */
//					log.debug("Authentication for user '" + auth[0] + "' using HTTP basic auth.");
//
//					/*
//					 * FIXME: The password is expected to be already MD5-encoded
//					 * (as in the cookie). This is typically not the case (i.e.,
//					 * user enters password in browser), but we decided to do it
//					 * this way because we implemented this mechanism
//					 * exclusively for integration of publication lists into
//					 * CMS. There cookie handling is often difficult and one
//					 * does not want the cleartext password to be written into
//					 * the CMS (at least in our (eecs) scenario).
//					 */
//
//					// try to authenticate user
//					final LogicInterface logic = dbLogicFactory.getLogicAccess(auth[0], auth[1]);
//					loginUser = logic.getUserDetails(auth[0]);
//				}
//			}
//		} catch (AccessDeniedException valEx) {
//			log.debug("Login failed.", valEx);
//		}
//
//		if (loginUser == null) {
//			log.debug("user not found in DB or user has no cookie set");
//			/*
//			 * user is not in DB/authenticated properly: get/set values from
//			 * Cookie
//			 */
//			loginUser = new User();
//		}
//
//		/*
//		 * put bean into request for following Servlets/JSPs
//		 */
//		httpServletRequest.setAttribute(REQ_ATTRIB_LOGIN_USER, loginUser);
//
//		/*
//		 * For backwards compatibility, we add the user
//		 * as request attribute (used by old servlets and JSPs).
//		 */
//		httpServletRequest.setAttribute(REQ_ATTRIB_USER, loginUser);
//		
//		// add default language to request if no language is set
//		final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//		final HttpSession session = httpServletRequest.getSession();
//		final String langUser = FilterUtils.getUser().getSettings().getDefaultLanguage();
//		/*
//		if (session.getAttribute(REQ_ATTRIB_LANGUAGE) == null) {
//			if(langUser == null) {
//				session.setAttribute(REQ_ATTRIB_LANGUAGE, new Locale((String) session.getServletContext().getAttribute(PROJECT_DEFAULT_LANGUAGE)));
//			} else {
//				session.setAttribute(REQ_ATTRIB_LANGUAGE, new Locale(langUser));
//
//			}
//			
//		//if user changed language in /settings change the language to the new requested language
//		} else {
//			final String lang = langUser;
//			final Locale locale = (Locale) session.getAttribute(REQ_ATTRIB_LANGUAGE);
//			if( present(lang) && !locale.getLanguage().equals(new Locale(lang).getLanguage())) {
//				session.setAttribute(REQ_ATTRIB_LANGUAGE, new Locale(lang));
//			}
//		}*/
//		if (!present(session.getAttribute(REQ_ATTRIB_LANGUAGE))) {
//			log.info("session attribute " + REQ_ATTRIB_LANGUAGE + " not present, setting it to");
//			if (present(langUser)) {
//				final Locale locale = new Locale(langUser);
//				session.setAttribute(REQ_ATTRIB_LANGUAGE, locale);
//				log.info(locale + " from user");
//			} else {
//				final Locale locale = new Locale((String) session.getServletContext().getAttribute(PROJECT_DEFAULT_LANGUAGE));
//				session.setAttribute(REQ_ATTRIB_LANGUAGE, locale);
//				log.info(locale + " from defaults");
//			}
//		}
//		// pass control on to the next filter
//		chain.doFilter(request, response);
//	}
//
//	/**
//	 * Creates a user in the database for X.509.
//	 * 
//	 * The password is set to {@link #X509_GENERIC_PASSWORD}, the user name to
//	 * the given user name.
//	 * 
//	 * @param ibatisDBSessionFactory
//	 * @param uname
//	 * @return
//	 * @throws Exception
//	 */
//	private User createX509User(final IbatisDBSessionFactory ibatisDBSessionFactory, final String uname) throws Exception {
//		User loginUser;
//		/*
//		 * user not found in DB: create new user
//		 * 
//		 * TODO: use data from certificate
//		 */
//		loginUser = new User(uname);
//		loginUser.setPassword(X509_GENERIC_PASSWORD);
//		/*
//		 * get admin DB access
//		 */
//		final AdminLogicFactoryBean adminLogicFactory = new AdminLogicFactoryBean();
//		adminLogicFactory.setAdminUserName("admin");
//		adminLogicFactory.setDbSessionFactory(ibatisDBSessionFactory);
//		final LogicInterface adminLogic = adminLogicFactory.getObject();
//		/*
//		 * finally: create user
//		 */
//		adminLogic.createUser(loginUser);
//		return loginUser;
//	}
//
//	/**
//	 * Returns the value of a cookie with the given name.
//	 * 
//	 * @param request
//	 * @param cookieName
//	 * @return the value of a cookie with the given name
//	 */
//	public static String getCookie(HttpServletRequest request, String cookieName) {
//		Cookie cookieList[] = request.getCookies();
//		if (cookieList != null) {
//			for (Cookie theCookie : cookieList) {
//				if (theCookie.getName().equals(cookieName)) {
//					return theCookie.getValue();
//				}
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * Extracts the auth header, decodes it and returns an array containing at
//	 * position 0 the user name and at position 1 the user password.
//	 * 
//	 * @author rja
//	 * @return <code>new String[]{username, password}</code> or
//	 *         <code>null</code>, if no auth header found.
//	 * 
//	 * @throws IOException
//	 */
//	private String[] decodeAuthHeader(final HttpServletRequest request) {
//		final String authHeader = request.getHeader("authorization");
//		if (authHeader != null) {
//			// Decode it, using any base 64 decoder
//			final String userpassDecoded = new String(Base64.decodeBase64(authHeader.split("\\s+")[1].getBytes()));
//			// split user name and password
//			return userpassDecoded.split(":");
//		}
//		return null;
//	}
//
//	
//
//	/*
//	 * ***************************************************
//	 * 
//	 * the following methods are X.509 specific
//	 * 
//	 * written by Torsten Leidig
//	 * 
//	 * **************************************************
//	 */
//	private static String getUserName(HttpServletRequest request) throws CertificateException {
//		return getUserIdFromCertificate(getCert(request));
//	}
//
//	
//	private static X509Certificate getCert(HttpServletRequest request) throws CertificateException {
//		X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
//		if (certs != null) {
//			return certs[0]; // on index 0 shall be the client cert
//		}
//
//		// get cert from IIS reverse proxy send in request header
//		return decodeIisCertificate(request.getHeader("SSL_CLIENT_CERT"));
//	}
//
//	private static X509Certificate decodeIisCertificate(String certificate) throws CertificateException {
//		if (certificate == null) {
//			return null; // no cert from IisProxy
//		}
//		final byte[] decodedBytes = Base64.decodeBase64(certificate.getBytes());
//		final ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
//		final CertificateFactory cf = CertificateFactory.getInstance("X.509");
//		return (X509Certificate) cf.generateCertificate(bais);
//	}
//
//	private static String getUserIdFromCertificate(final X509Certificate cert) {
//
//		if (cert == null) {
//			return null;
//		}
//		/*
//		 * create new user
//		 */
//		final String subjectDN = cert.getSubjectX500Principal().getName();
//		return extractUidFromDN(subjectDN);
//	}
//
//	private static String extractUidFromDN(final String subjectDN) {
//		String uid = null;
//		final StringTokenizer tokenizer = new StringTokenizer(subjectDN, ",");
//		if (tokenizer.hasMoreTokens()) {
//			uid = tokenizer.nextToken();
//		}
//
//		int idx = uid.indexOf('=');
//		if (idx < 0) {
//			return null;
//		}
//		return uid.substring(idx + 1).trim().toUpperCase().trim();
//	}

}