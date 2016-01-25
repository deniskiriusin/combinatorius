/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Helper class providing convenience methods.
 */
class ComboHelper {

	private static volatile ComboHelper instance = null;

	private ComboHelper() {
		// empty constructor
	}

	public static ComboHelper getInstance() {
		if (instance == null) {
			synchronized (ComboHelper.class) {
				if (instance == null) {
					instance = new ComboHelper();
				}
			}
		}
		return instance;
	}

	/**
	 * Constructs {@link Map} object from query string parameters.
	 * 
	 * @param request - {@link HttpServletRequest}
	 * @return unmodifiable {@link Map} of key-value query string parameters, never <code>null</code>
	 */
	final Map<String, String> parseQueryString(HttpServletRequest request) {
		Map<String, String> qsParams = new HashMap<String, String>();
		try {
			qsParams = Collections.unmodifiableMap(getQueryStringParameters(request.getRequestURL().toString()));
			if (qsParams.isEmpty()) {
				throw new IllegalArgumentException("Query string not specified");
			} else if (qsParams.get("type") == null
					|| (!qsParams.get("type").equals("css") && !qsParams.get("type").equals("js"))) {
				throw new IllegalArgumentException("Query string 'type' parameter is not specified "
						+ "or is incorrect, must be 'css' or 'js'");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Error parsing query string:" + e.getMessage(), e);
		}
		return qsParams;
	}

	/**
	 * Most proxies, most notably Squid up through version 3.0, do not cache resources with a "?" in their URL even if a
	 * <code>Cache-control: public</code> header is present in the response. To enable proxy caching for these
	 * resources, remove query strings from references to static resources, and instead encode the parameters into the
	 * file names themselves.
	 *
	 * @see <a href="http://code.google.com/speed/page-speed/docs/caching.html">
	 *      http://code.google.com/speed/page-speed/docs/caching.html</a>
	 *
	 * @param requestURL - the request URL
	 * @return unmodifiable {@link Map} filled with query string parameters, never <code>null</code>
	 */
	final Map<String, String> getQueryStringParameters(final String requestURL) {
		final Map<String, String> paramMap = new HashMap<String, String>();
		if (requestURL == null) {
			return paramMap;
		}
		int i = 0;
		try {
			for (final String param : requestURL.split("&")) {
				if (i > 0) {
					final String key = param.split("=")[0];
					final String value = param.split("=")[1];
					paramMap.put(key, value);
				}
				i++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Error getting query string parameters, index out of bound:" + e.getMessage());
		}
		return Collections.unmodifiableMap(paramMap);
	}

	/**
	 * Generates Etag string based on absolute paths of "files" as well as "lastModified" and "isCompressionEnabled" parameters.
	 * The method returns a valid Etag value wrapped with double quotes like: "8e0224303483a48d84b5db7fa4cf4b3e".
	 * 
	 * @param files - a collection of files the method uses to generate ETag string
	 * @param lastModified - lastModified date/time as <code>long</code>
	 * @param isCompressionEnabled - is compression enabled
	 * @return ETag string, never <code>null</code>
	 */
	final String generateEtagValue(final Collection<File> files, final long lastModified,
			final boolean isCompressionEnabled) {
		final StringBuilder filepaths = new StringBuilder(files.toString().length() + 20);
		for (final File file : files) {
			filepaths.append(file.getAbsolutePath()).append(",");
		}
		return new StringBuilder(34).append("\"")
				.append(DigestUtils.md5Hex(filepaths.toString() + lastModified + isCompressionEnabled)).append("\"")
				.toString();
	}

	/**
	 * Parses the query string, constructs and returns {@link RequestDetails} object. The method 
	 * also checks if there is a {@link Cookie} object with 'combinatorius.theme' name available and 
	 * assigns it value to {@link RequestDetails} object's 'theme' field.
	 * 
	 * @param request - {@link HttpServletRequest}
	 * @return {@link RequestDetails} object, never <code>null</code>
	 * @throws IOException
	 */
	final RequestDetails getRequestDetails(HttpServletRequest request) throws IOException {
		final Map<String, String> qsParams = parseQueryString(request);

		final String strType = qsParams.get("type");
		final String resources = qsParams.get("resources");
		String theme = qsParams.get("theme");
		if (theme == null) {
			Cookie cookie = CookieUtils.getCookie(request, "combinatorius.theme");
			if (cookie != null && cookie.getValue() != null) {
				theme = cookie.getValue().trim();
			}
		}
		final MimeType type = strType.equals("css") ? MimeType.css : MimeType.js;
		final long v = qsParams.get("v") == null ? 0L : Long.parseLong(qsParams.get("v"));

		return new RequestDetails.Builder(type).resources(resources).themeName(theme).version(v).build();
	}

	/**
	 * Returns the name of combined JavaScript or CSS file. The combined file is the one generated by Combinatorius
	 * servlet and deployed to appropriate cache directory. The name looks like
	 * <code>8e0224303483a48d84b5db7fa4cf4b3e.css.cmb.gzip</code> where <code>.gzip</code> is optionally added if 
	 * file has been compressed.
	 * 
	 * @param requestDetails - {@link RequestDetails}
	 * @param eTag - eTag string
	 * @return the name of combined JavaScript or CSS file, never <code>null</code>
	 * @throws IOException
	 */
	final String getCombinedFileName(final RequestDetails requestDetails, final String eTag) throws IOException {
		final StringBuilder sb = new StringBuilder(50).append(eTag.substring(1, eTag.length() - 1)).append(".")
				.append(requestDetails.getExtension()).append(".cmb");
		if (ComboServlet.isCompressionEnabled()) {
			sb.append(".gzip");
		}
		return sb.toString();
	}

	/**
	 * The method compresses array of bytes if <code>Content-Encoding</code> response header is not set
	 * and <code>prop.isCompressionEnabled</code> property is set to true.
	 * 
	 * @param bytes - array of bytes
	 * @param response - {@link HttpServletResponse}
	 * @return array of bytes, never <code>null</code>
	 * @throws IOException
	 */
	final byte[] compressConditionally(final byte[] bytes, final HttpServletResponse response) throws IOException {
		if (ComboServlet.isCompressionEnabled() && !response.containsHeader("Content-Encoding")) {
			return CIOUtils.gzipContent(bytes);
		}
		return bytes;
	}

	/**
	 * Returns byte array of combined locally cached file, if exists. Creates new cached file and returns the 
	 * byte array of collection of files specified via "files" parameter otherwise.
	 * 
	 * @param requestDetails - {@link RequestDetails}
	 * @param files - collection of files
	 * @param eTag - eTag string
	 * @param response - {@link HttpServletResponse}
	 * @return array of bytes, never <code>null</code>
	 * @throws IOException
	 */
	final byte[] getContent(final RequestDetails requestDetails, final Collection<File> files, final String eTag,
			final HttpServletResponse response) throws IOException {
		byte[] bytes = new byte[] {};
		try {
			// get cache directory
			final File cacheDir = requestDetails.getMimeType() == MimeType.css ? ComboServlet.getCachedCssDir() : ComboServlet.getCachedJsDir();
			// combined file
			final String combinedFileName = getCombinedFileName(requestDetails, eTag);
			final File combinedFile = FileUtils.getFile(cacheDir, combinedFileName);
			// return combined/cached file if exist
			if (combinedFile != null && combinedFile.exists()) {
				return CIOUtils.getFilesByteArray(Collections.unmodifiableCollection(Collections.singleton(combinedFile)));
			} else {
				OutputStream out = null;
				try {
					bytes = compressConditionally(CIOUtils.getFilesByteArray(files), response);
					// create new cached file otherwise
					if (combinedFile.createNewFile()) {
						out = new FileOutputStream(combinedFile);
						IOUtils.write(bytes, out);
						out.close();
					}
				} catch (Exception e) {
					throw new IOException("Error creating new file [" + combinedFile.getAbsolutePath() + "]: " + e);
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
			return bytes;
		} catch (Exception e) {
			throw new IOException("Error trying to get content: " + e);
		}
	}

}
