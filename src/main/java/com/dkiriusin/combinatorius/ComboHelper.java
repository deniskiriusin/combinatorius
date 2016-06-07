/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

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
	 * Generates Etag string based on absolute paths of "files" as well as "lastModified", "isCompressionEnabled"
	 * and YUI settings.
	 * The method returns a valid Etag value wrapped with double quotes like: "8e0224303483a48d84b5db7fa4cf4b3e".
	 * 
	 * @param files - a collection of files the method uses to generate ETag string
	 * @param lastModified - lastModified date/time as <code>long</code>
	 * @param requestDetails - {@link RequestDetails} object
	 * @return ETag string, never <code>null</code>
	 */
	final String generateEtagValue(final Collection<File> files, final long lastModified,
			final RequestDetails requestDetails) {
		final StringBuilder filepaths = new StringBuilder(files.toString().length() + 20);
		for (final File file : files) {
			filepaths.append(file.getAbsolutePath()).append(",");
		}
		final String yui_settings = requestDetails.getMimeType() ==
				MimeType.css ? getYUICSSCompressorSettings() : getYUIJsvsScriptCompressorSettings();
		return new StringBuilder(34).append("\"")
				.append(DigestUtils.md5Hex(filepaths.toString() + lastModified + yui_settings
						+ ComboServlet.isCompressionEnabled())).append("\"").toString();
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
			Cookie cookie = CookieUtils.getCookie(request, ComboServlet.combinatoriusTheme);
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
	 * Returns the name of minified JavaScript or CSS file. The minified files are kept in cache directory.
	 * The name format is <code>9a7264e193ef6bad8c1d180034be907f_filename.min.css</code>.
	 *
	 * @param f - file to rename
	 * @return the name of minified file
	 * @throws IOException
	 */
	final String getMinifiedFileName(final File f) throws IOException {
		final String fileName = f.getName();
		final StringBuilder sb = new StringBuilder(80)
				.append(DigestUtils.md5Hex(IOUtils.toByteArray(new FileInputStream(f))))
				.append("_")
				.append(fileName.substring(0, fileName.lastIndexOf(".")))
				.append(".min")
				.append(fileName.substring(fileName.lastIndexOf(".")));
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
	 * Minifies file by utilising {@link CssCompressor} or {@link JavaScriptCompressor} depending on
	 * Mime-Type.
	 *
	 * @param requestDetails - {@link RequestDetails} object
	 * @param file - file to minify
	 * @return minified version of file as byte array, never <code>null</code>
	 * @throws IOException
	 */
	final byte[] minifyFile(final RequestDetails requestDetails, final File file) throws IOException {
		if (!file.getName().endsWith(".css") && !file.getName().endsWith(".js")) {
			throw new IllegalArgumentException("File type is incorrect. Must be either .css or .js");
		}
		if (!ComboServlet.isYUICompressorEnabled()) {
			throw new IllegalStateException("YUI Compressor must be enabled in combinatorius.properties "
					+ "to perform file minification");
		}
		byte[] result = new byte[] {};
		switch (requestDetails.getMimeType()) {
			case css:
				result = CIOUtils.minifyCSS(IOUtils.toByteArray(new FileInputStream(file)),
						ComboServlet.getYUICSSCompressorLinebreakpos());
				break;
			case js:
				result = CIOUtils.minifyJS(IOUtils.toByteArray(new FileInputStream(file)),
						ComboServlet.getYUIJavaScriptCompressorLinebreak(),
						ComboServlet.isYUIJavaScriptCompressorMunge(),
						ComboServlet.isYUIJavaScriptCompressorVerbose(),
						ComboServlet.isYUIJavaScriptCompressorPreserveAllSemiColons(),
						ComboServlet.isYUIJavaScriptCompressorDisableOptimisations());
				break;
			default:
				break;
		}
		return result;
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
					final Pattern OMITED_FILES = Pattern.compile(ComboServlet.getYUIOmitFilesFromMinificationRegEx());
					for (final File f : files) {
						final Matcher m = OMITED_FILES.matcher(f.getAbsolutePath());
						byte[] file_bytes = null;
						// check if the file is not already minified, do not minify such files
						if (!m.matches() && ComboServlet.isYUICompressorEnabled()) {
							// minification is expensive process so we keep minified files in local cache
							final String cached_file_name = getMinifiedFileName(f);
							//final String cached_file_name = getCombinedFileName(requestDetails, DigestUtils.md5Hex(f.getAbsolutePath()));
							final File cached_file = FileUtils.getFile(cacheDir, cached_file_name);
							if (cached_file != null && cached_file.exists()) {
								file_bytes = CIOUtils.getFilesByteArray(Collections.unmodifiableCollection(Collections.singleton(cached_file)));
							} else {
								file_bytes = minifyFile(requestDetails, f);
								cached_file.createNewFile();
								FileUtils.writeByteArrayToFile(cached_file, file_bytes);
							}
						} else {
							file_bytes = CIOUtils.getFilesByteArray(Collections.singleton(f));
						}
						bytes = addArrays(bytes, file_bytes);
					}
					// Gzip content
					bytes = compressConditionally(bytes, response);
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

	/**
	 * Returns YUI CssCompresor's settings as String.
	 */
	final private String getYUICSSCompressorSettings() {
		return ComboServlet.isYUICompressorEnabled() + "" + ComboServlet.getYUICSSCompressorLinebreakpos();
	}

	/**
	 * Returns YUI JavaScriptCompresor's settings as String.
	 */
	final private String getYUIJsvsScriptCompressorSettings() {
		return ComboServlet.isYUICompressorEnabled() +
				"" + ComboServlet.getYUIJavaScriptCompressorLinebreak() +
				"" + ComboServlet.isYUIJavaScriptCompressorDisableOptimisations() +
				"" + ComboServlet.isYUIJavaScriptCompressorMunge() +
				"" + ComboServlet.isYUIJavaScriptCompressorPreserveAllSemiColons();
	}

	/**
	 * Clones an array. This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 *
	 * @param array - the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return (byte[]) array.clone();
	}

	/**
     * Adds all the elements of the given arrays into a new array.
     *
     * @param array1 - the first array whose elements are added to the new array
     * @param array2 - the second array whose elements are added to the new array
     * @return The new byte[] array
     */
    static byte[] addArrays(byte[] array1, byte[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }
}
