/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility class contains useful static methods to simplify work with HTTP headers.
 */
class HttpUtils {

	private HttpUtils() {
		// empty constructor
	}

	/**
	 * Checks if the request has been modified.
	 *
	 * @param request - {@link HttpServletRequest} object to check
	 * @param eTag - eTag string
	 * @return <code>true</code> if the request is conditional (i.e. <code>"If-None-Match"</code> header equals
	 *         <code>"ETag"</code> header) or <code>false</code> otherwise.
	 */
	static final boolean isRequestModified(HttpServletRequest request, final String eTag) {
		if (request.getHeader("If-None-Match") != null && eTag.equalsIgnoreCase(request.getHeader("If-None-Match"))) {
			return false;
		}
		return true;
	}

	/**
	 * Checks <code>Accept-Encoding</code> request header and returns <code>true</code> if client accepts
	 * compressed(gzip) content or <code>false</code> otherwise.
	 *
	 * @param request - {@link HttpServletRequest}
	 * @return <code>true</code> - if compressed content accepted by client<br>
	 *         <code>false</code> - if compressed content is not accepted by client
	 */
	static final boolean isCompressionAccepted(HttpServletRequest request) {
		boolean acceptCompress = false;

		if (request.getHeader("Accept-Encoding") != null && request.getHeader("Accept-Encoding").indexOf("gzip") != -1) {
			acceptCompress = true;
		} else if (request.getHeader("---------------") != null) {
			// Norton Internet Security
			// see http://tinymce.moxiecode.com/punbb/viewtopic.php?pid=6250#p6250
			acceptCompress = true;
		}
		// test for IE < 7.0
		if (isIE70orLess(request)) {
			acceptCompress = false;
		}
		return acceptCompress;
	}

	/**
	 * Sets <code>Content-Encoding: gzip</code> header that indicates the content codings have been applied to response.
	 * The method also sets <code>Vary: Accept-Encoding</code> header that forces cache engine to add
	 * <code>Accept-Encoding</code> to it's caching decision strategy.
	 * 
	 * @param response - {@link HttpServletResponse}
	 */
	static final void setCompressionHeaders(HttpServletResponse response) {
		response.setHeader("Content-Encoding", "gzip");
		response.setHeader("Vary", "Accept-Encoding"); // Handle proxies
	}

	/**
	 * Returns correct servlet path. Some servlet containers return various strange, unhelpful values from
	 * {@link HttpServletRequest#getServletPath()}, so this function tries a few methods to get the same information.
	 *
	 * @param request - {@link HttpServletRequest}
	 * @return the correct servlet path
	 */
	static final String getServletPath(HttpServletRequest request) {
		String thisPath = request.getServletPath();

		if (thisPath == null) {
			final String requestURI = request.getRequestURI();
			if (request.getPathInfo() != null) {
				// strip the pathInfo from the requestURI
				thisPath = requestURI.substring(0, requestURI.indexOf(request.getPathInfo()));
			} else {
				thisPath = requestURI;
			}
		} else if (thisPath.equals("") && request.getPathInfo() != null) {
			thisPath = request.getPathInfo();
		}
		return thisPath;
	}

	/**
	 * Checks if the current request is originating from an IE browser whose version is less than version 7.0.
	 * Returns <code>true</code> if less than IE 7.0 or <code>false</code> otherwise.
	 *
	 * @param request - {@link HttpServletRequest}
	 * @return <code>true</code> if less than IE 7.0, <code>false</code> otherwise
	 * @throws NumberFormatException
	 */
	static final boolean isIE70orLess(HttpServletRequest request) throws NumberFormatException {
		// browser detection
		String userAgent = request.getHeader("User-Agent");

		if (userAgent == null || "".equalsIgnoreCase(userAgent)) {
			return false;
		}

		userAgent = userAgent.toLowerCase();

		// gecko based browser
		if (userAgent.indexOf("msie") == -1) {
			return false;
		}
		// ie version 7.0 on windows is the lowest minimum we'll take
		else if (userAgent.indexOf("msie") != -1 && userAgent.indexOf("windows") != -1) {
			boolean sp2 = userAgent.indexOf("sv1;") != -1;

			// http://msdn.microsoft.com/workshop/author/dhtml/overview/aboutuseragent.asp
			int verStart = userAgent.indexOf("msie");
			int verEnd = userAgent.indexOf(";", verStart);

			// try to parse
			String rv = userAgent.substring(verStart + 5, verEnd);
			// strip out letters - always seem to be the last character (i.e. 1.4b, 1.5a)
			if (Character.isLetter(rv.charAt(rv.length() - 1))) {
				rv = rv.substring(0, rv.length() - 1);
			}

			double ver = Double.parseDouble(rv);
			return ver < 7 || (ver == 6 && !sp2);
		}
		return false;
	}
}
