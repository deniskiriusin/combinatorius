/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 * 
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ComboServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4603538340120848220L;

	static final String propertiesFileName = "combinatorius.properties";
	// thread-local RequestDetails object
	private static ThreadLocal<RequestDetails> requestDetails = new ThreadLocal<RequestDetails>();
	// created once during initialisation, read-only (no synchronisation
	// required)
	static Properties properties = new Properties();

	@Override
	public void init() throws ServletException {
		try {
			properties = CIOUtils.loadPropertiesFromClasspath(propertiesFileName);
		} catch (IOException e) {
			throw new ServletException("Initialisation failed: " + e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// send 501 code. Status code (501) indicating the HTTP server does not
		// support the
		// functionality needed to fulfil the request.
		response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		InputStream in = null;
		ServletOutputStream out = null;

		try {
			// construct RequestDetails object from HTTP request
			requestDetails.set(Helper.getInstance().getRequestDetails(request));
			// get a collection of JavaScript/CSS files
			final Collection<File> files = getFiles(request, requestDetails.get());
			// check if any of files have been changed since the last request
			final long lastModified = CIOUtils.lastModifiedFile(files);
			// generate ETag
			final String eTag = Helper.getInstance().generateEtagValue(files, lastModified, isCompressionEnabled());
			// the actual content
			final byte[] bytes = Helper.getInstance().getContent(requestDetails.get(), files, eTag, response);
			// check if request or files have been modified to set proper cache headers,
			// write the binary content out if request has been modified or send conditional
			// response headers otherwise
			if (HttpUtils.isRequestModified(request, eTag)) {
				setResponseHeaders(request, response, eTag, lastModified, bytes.length);
				// update 'v' URL parameter, a.k.a fingerprint
				getServletContext().setAttribute("com.dksoftware.combo.v_" + requestDetails.get().getExtension(),
						lastModified);
				in = new ByteArrayInputStream(bytes);
				out = response.getOutputStream();
				IOUtils.copy(in, out);
				out.flush();
			} else {
				setConditionalResponseHeaders(request, response);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());
			}
		}
	}

	/**
	 * Sets the following response headers: <code>Etag</code>,
	 * <code>Expires</code>, <code>Cache-Control</code>,
	 * <code>Last-Modified</code>, <code>Content-Length</code>. <br>
	 * Sets the character encoding (MIME charset) of the response being sent to
	 * the client to UTF-8. The method optionally adds
	 * <code>Content-Encoding:gzip</code> and <code>Vary:Accept-Encoding</code>
	 * compression headers when compression is enabled. <br>
	 * The method also adds "s-maxage" to <code>Cache-Control</code> header to
	 * fix Apache mod_cache bug described in URL below.
	 * <p>
	 * {@link http://www.gossamer-threads.com/lists/apache/dev/344665}.
	 * 
	 * @param request - {@link HttpServletRequest}
	 * @param response - {@link HttpServletResponse}
	 * @param eTag - eTag string
	 * @param lastModified - last modified as long
	 * @param contentLength - content length as int
	 */
	static void setResponseHeaders(HttpServletRequest request, HttpServletResponse response, final String eTag,
			final long lastModified, final int contentLength) {
		String cacheProxy = null;
		if (request.getScheme().equalsIgnoreCase("https") || requestDetails.get().getVersion() > 0L) {
			// add s-maxage to fix Apache mod_cache bug
			// @see http://www.gossamer-threads.com/lists/apache/dev/344665
			final String s_maxage = properties.getProperty(CProperties.S_MAXAGE.getName());
			cacheProxy = "public";
			if (s_maxage != null) {
				cacheProxy += ", s-maxage=" + s_maxage;
			}
		} else {
			cacheProxy = "private";
		}
		response.setContentType(requestDetails.get().getMimeType().getName());
		response.setHeader("Etag", eTag);
		response.setCharacterEncoding("UTF-8");
		// set Expires header
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);
		response.setDateHeader("Expires", cal.getTimeInMillis());
		// set Cache-Control header (private - instructs proxies in the path not
		// to cache the resource,
		// browser will still cache)
		final String max_age = properties.getProperty(CProperties.MAX_AGE.getName());
		response.setHeader("Cache-Control", cacheProxy + (max_age != null ? (", max-age=" + max_age) : ""));
		// Last-Modified: Tue, 15 Nov 2015 12:45:26 GMT
		response.setDateHeader("Last-Modified", lastModified);
		if (isCompressionEnabled()) {
			HttpUtils.setCompressionHeaders(response);
		}
		response.setContentLength(contentLength);
		response.setHeader("Version", String.valueOf(lastModified));
	}

	/**
	 * Sets conditional response headers. Status code (304) indicating that a
	 * conditional GET operation found that the resource was available and not
	 * modified. The method also sets <code>Content-Length: 0</code> and
	 * <code>Last-Modified</code> headers for clients that don't understand ETag
	 * headers.
	 * 
	 * @param request - {@link HttpServletRequest}
	 * @param response - {@link HttpServletResponse}
	 */
	static void setConditionalResponseHeaders(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		response.setHeader("Content-Length", "0");
		// set 'Last-Modified' for clients that don't understand ETag headers
		response.setHeader("Last-Modified", request.getHeader("If-Modified-Since"));
	}

	/**
	 * Returns unmodifiable collection of files from default directory specified
	 * in <code>combinatorius.properties</code> file as well as explicitly
	 * specified files via 'resources' and 'theme' URL parameters.
	 *
	 * @param request - {@link HttpServletRequest}
	 * @param requestDetails - {@link RequestDetails}
	 * @return unmodifiable collection of files, never <code>null</code>
	 */
	Collection<File> getFiles(HttpServletRequest request, final RequestDetails requestDetails) {
		Set<File> fileSet = new LinkedHashSet<File>();
		if (requestDetails == null) {
			return fileSet;
		}
		final File dir = requestDetails.getMimeType() == MimeType.css ? getCssDir() : getJsDir();
		// get files from directories specified in combinatorius.properties file
		try {
			fileSet.addAll(CIOUtils.getUnmodifiableCollectionOfFiles(dir,
					Collections.singleton(requestDetails.getExtension()), true));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error getting files from '" + dir.getAbsolutePath() + "' directory.");
		}
		// get files specified via 'theme' URL parameter
		if (requestDetails.getThemeName() != null || CookieUtils.getCookie(request, "combinatorius.theme") != null) {
			String themeName = getThemeName(request, requestDetails);
			try {
				fileSet.addAll(CIOUtils.getUnmodifiableCollectionOfFiles(getThemesDir(themeName),
						Collections.singleton(requestDetails.getExtension()), true));
			} catch (Exception e) {
				throw new IllegalArgumentException("Error getting '" + themeName + "' theme. Please "
						+ "make sure the theme name is correctly specified via 'theme' URL parameter or as 'combinatorius.theme' cookie value.");
			}
		}
		// get files explicitly specified via 'resources' URL parameter
		if (requestDetails.getResources() != null) {
			final String params[] = requestDetails.getResources().split(",");
			for (final String param : params) {
				final String realPath = getServletContext().getRealPath(param);
				fileSet.add(FileUtils.getFile(realPath));
			}
		}
		return Collections.unmodifiableCollection(fileSet);
	}

	/**
	 * Returns theme name by checking 'combinatorius.theme' cookies value first. 
	 * Returns theme name specified in URL if there are no cookies passed with request.
	 * 
	 * @param request - {@link HttpServletRequest}
	 * @param requestDetails {@link RequestDetails}
	 * @return the name of the theme
	 */
	String getThemeName(HttpServletRequest request, final RequestDetails requestDetails) {
		String themeName = null;
		Cookie cookie = CookieUtils.getCookie(request, "combinatorius.theme");
		if (cookie != null) {
			themeName = cookie.getValue();
		} else {
			themeName = requestDetails.getThemeName();
		}
		return themeName;
	}

	// Getters

	/**
	 * Returns default CSS directory as per <code>prop.css.dir<code> property.
	 */
	static File getCssDir() {
		String cssDir = properties.getProperty(CProperties.CSS_DIR.getName());
		if (cssDir != null) {
			cssDir = cssDir.trim();
		} else {
			throw new RuntimeException("CSS directory not specified");
		}
		return CIOUtils.getLocalFile(cssDir);
	}

	/**
	 * Returns cached CSS directory as per <code>prop.css.cache.dir
	 * <code> property.
	 */
	static File getCachedCssDir() {
		final String cachedCssDir = properties.getProperty(CProperties.CSS_CACHE_DIR.getName()).trim();
		return CIOUtils.getLocalFile(cachedCssDir);
	}

	/**
	 * Returns default JavaScript directory as per <code>prop.js.dir
	 * <code> property.
	 */
	static File getJsDir() {
		String javaScriptDir = properties.getProperty(CProperties.JS_DIR.getName());
		if (javaScriptDir != null) {
			javaScriptDir = javaScriptDir.trim();
		} else {
			throw new RuntimeException("JavaScript directory not specified");
		}
		return CIOUtils.getLocalFile(javaScriptDir);
	}

	/**
	 * Returns default cached JavaScript directory as per
	 * <code>prop.js.cache.dir<code> property.
	 */
	static File getCachedJsDir() {
		final String javaScriptDirCacheDir = properties.getProperty(CProperties.JS_CACHE_DIR.getName()).trim();
		return CIOUtils.getLocalFile(javaScriptDirCacheDir);
	}

	/**
	 * Returns default Themes directory as per <code>prop.themes.dir
	 * <code> property.
	 */
	static File getThemesDir(final String themeName) {
		String themesDir = properties.getProperty(CProperties.THEMES_DIR.getName());
		if (themeName != null) {
			themesDir = themesDir.trim();
			themesDir += (File.separator + themeName);
		}
		return CIOUtils.getLocalFile(themesDir);
	}

	/**
	 * Checks <code>prop.isCompressionEnabled<code> property to identify if
	 * compression is enabled.
	 */
	static boolean isCompressionEnabled() {
		final String isCompressionEnabled = properties.getProperty(CProperties.IS_COMPRESSION_ENABLED.getName()).trim();
		return isCompressionEnabled.equals("true") ? true : false;
	}

	// Servlet Info

	@Override
	public String getServletInfo() {
		return "Combinatorius by Denis Kiriusin (deniskir@gmail.com). "
				+ "An effective tool for delivering CSS and JavaScript files";
	}
}