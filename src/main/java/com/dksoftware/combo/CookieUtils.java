/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Utility class contains useful static methods to simplify work with {@link Cookie} objects.
 */
class CookieUtils {

	private CookieUtils() {
		// empty constructor
	}

	/**
	 * Returns the specified cookie object, or <code>null</code> if the cookie does not exist.
	 * <p>
	 * <b>Note</b>: because of the way that cookies are implemented it's possible for multiple cookies with the same
	 * name to exist (but with different domain values). This method will return the first cookie that has a name match.
	 *
	 * @param request - {@link HttpServletRequest}
	 * @param name - the name of the cookie
	 * @return the {@link Cookie} object if it exists or <code>null</code> otherwise
	 */
	static final Cookie getCookie(HttpServletRequest request, final String name) {
		if (null == request || null == name || (name.length() == 0)) {
			return null;
		}

		final Cookie cookies[] = request.getCookies();
		// Return null if there are no cookies or the name is invalid.
		if (cookies == null) {
			return null;
		}
		// Otherwise, we do a linear scan for the cookie.
		Cookie cookie = null;
		for (int i = 0; i < cookies.length; i++) {
			// If the current cookie name matches the one we're looking for, we've found a
			// matching cookie.
			if (cookies[i] != null && cookies[i].getName() != null && cookies[i].getName().equals(name)) {
				cookie = cookies[i];
				// The best matching cookie will be the one that has the correct domain name.
				// If we've found the cookie with the correct domain name, return it.
				// Otherwise, we'll keep looking for a better match.
				if (request.getServerName() != null && request.getServerName().equals(cookie.getDomain())) {
					break;
				}
			}
		}
		return cookie;
	}

}
