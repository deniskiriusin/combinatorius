/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * Utility class contains useful static methods to simplify work with files.
 */
class CIOUtils {

	private CIOUtils() {
		// empty constructor
	}

	/**
	 * Iterates through the collection of files and returns date and time of the file last modified, as
	 * <code>long</code> value.
	 *
	 * @param files - collection of files
	 * @return last modified date and time as <code>long</code>, or <code>0</code> if the collection of files is <code>null</code> or
	 *         empty
	 */
	static final long lastModifiedFile(final Collection<File> files) {
		long lastModified = 0;
		for (final File file : files) {
			lastModified = Math.max(lastModified, file.lastModified());
		}
		return lastModified;
	}

	/**
	 * Receives an array of bytes and returns new array of bytes compressed using <code>"gzip"</code> compression
	 * method.
	 *
	 * @param bytes - an array of bytes to compress
	 * @return compressed bytes array, never <code>null</code>
	 * @throws IOException
	 */
	static final byte[] gzipContent(final byte[] bytes) throws IOException {
		ByteArrayOutputStream baos = null;
		GZIPOutputStream gzos = null;

		byte[] gzippedBytes = null;

		try {
			baos = new ByteArrayOutputStream();
			gzos = new GZIPOutputStream(baos);

			gzos.write(bytes);
			gzos.finish();
			gzos.flush();
			gzos.close();

			gzippedBytes = baos.toByteArray();
			baos.close();
		} finally {
			if (gzos != null) {
				gzos.close();
			}
			if (baos != null) {
				baos.close();
			}
		}
		return gzippedBytes;
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 * 
	 * @param data - data to digest
	 * @return MD5 digest as a hex string, never <code>null</code>
	 */
	static final String getMD5(String data) {
		StringBuffer hexString = new StringBuffer();

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data.getBytes());
			byte[] hash = md.digest();

			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 not supported", e);
		}

		return hexString.toString();
	}

	/**
	 * Minifies CSS by utilising YUI {@link CssCompressor}.
	 *
	 * @param bytes - an array of bytes to minify
	 * @param linebreakpos - split long line after a specific column
	 * @return minified content as array of bytes
	 * @throws IOException
	 */
	static final byte[] minifyCSS(final byte[] bytes, int linebreakpos) throws IOException {
		Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
		StringWriter writer = new StringWriter();

		try {
			new CssCompressor(reader).compress(writer, linebreakpos);
		} catch (IndexOutOfBoundsException e) {
			// handle YUI bug when file is empty or contains comments only
			return new byte[] {};
		}
		finally {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
		return writer.toString().getBytes("UTF-8");
	}

	/**
	 * Minifies JavaScript by utilising YUI {@link JavaScriptCompressor}.
	 *
	 * @param bytes - an array of bytes to minify
	 * @param linebreak - split long line after a specific column
	 * @param munge - obfuscate, not only minify
	 * @param verbose - display informational messages and warnings
	 * @param preserveAllSemiColons - preserve all semicolons
	 * @param disableOptimisations - disable micro optimisations
	 * @return minified content as array of bytes
	 * @throws IOException
	 */
	static final byte[] minifyJS(final byte[] bytes, int linebreak, boolean munge, boolean verbose,
			boolean preserveAllSemiColons, boolean disableOptimisations) throws IOException {
		Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
		StringWriter writer = new StringWriter();

		try {
			new JavaScriptCompressor(reader, null).compress(writer, linebreak, munge, verbose,
					preserveAllSemiColons, disableOptimisations);
		} catch (IndexOutOfBoundsException e) {
			// handle YUI bug when file is empty or contains comments only
			return new byte[] {};
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
		return writer.toString().getBytes("UTF-8");
	}

	/**
	 * Copy bytes from an {@link InputStream} to an {@link OutputStream}. This method buffers the input internally, so
	 * there is no need to use a {@link BufferedInputStream}. The method optionally can add a line break.
	 *
	 * @param input - {@link InputStream} to read
	 * @param output - {@link OutputStream}
	 * @param addLineBreak - add line break if <code>true</code>
	 * @throws IOException
	 */
	static final void readInputStream(final InputStream input, final OutputStream output, final boolean addLineBreak)
			throws IOException {
		final int length = IOUtils.copy(input, output);

		// Add line break between files. We don't have to worry about binary
		// files with this as we'll never be merging multiple binary files together in a single
		// request - just text files such as css or javascript files.
		// This fixes an issue where comments could become unbalanced across
		// multiple files if they are merged together.
		if (addLineBreak && (length > 0)) {
			output.write('\n');
		}
	}

	/**
	 * Receives a collection of files and returns byte array representation of the collection by converting
	 * each file into byte array.
	 * 
	 * @see {@link CIOUtils#readInputStream(InputStream, OutputStream, boolean)}
	 *
	 * @param files - collection of files
	 * @return byte array of files collection, never <code>null</code>
	 * @throws IOException
	 */
	static final byte[] getFilesByteArray(final Collection<File> files) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (final File file : files) {
			final InputStream inputStream = new FileInputStream(file);
			try {
				readInputStream(inputStream, baos, (files.size() > 1));
			} catch (IOException e) {
				throw new IOException("Error reading file [" + file.getAbsolutePath() + "]", e);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
				if (baos != null) {
					baos.close();
				}
			}
		}
		return baos.toByteArray();
	}

	/**
	 * Searches for files within a given directory (and optionally its sub directories) with matching extension[s].
	 *
	 * @param dir - the directory to search in
	 * @param extentions - a collection of extensions like: <blockquote>{"js","css"}</blockquote>
	 *            <p>
	 *            If <code>extentions</code> parameter is <code>null</code>, all files are returned.
	 * @param recursive - if true all sub directories are searched as well
	 * @return an unmodifiable collection of {@link File} objects, never <code>null</code>
	 */
	static final Collection<File> getUnmodifiableCollectionOfFiles(File dir, final Collection<String> extentions,
			final boolean recursive) {
		return Collections
				.unmodifiableCollection(FileUtils.listFiles(dir, extentions.toArray(new String[0]), recursive));
	}

	/**
	 * Loads {@link Properties} using current thread's classloader. Here <i>filename</i> is supposed to be placed in
	 * one of the roots which are covered by the default classpath of a webapp, e.g. <code>Webapp/WEB-INF/lib</code>,
	 * <code>Webapp/WEB-INF/classes</code>, <code>Appserver/lib</code> or <code>JRE/lib</code>. If the properties file
	 * is webapp-specific, best is to place it in <code>WEB-INF/classes</code>. If you're developing a project in an
	 * IDE, you can also drop it in <code>src</code> folder (the project's source folder).
	 * <p>
	 * You can alternatively also put it somewhere outside the default classpath and add its path to the classpath of
	 * the appserver. In for example Tomcat you can configure it as <code>shared.loader</code> property of
	 * <code>Tomcat/conf/catalina.properties</code>.
	 *
	 * @param filename - properties filename
	 * @return {@link Properties} object, never <code>null</code>
	 * @throws IOException
	 */
	static final Properties loadPropertiesFromClasspath(final String filename) throws IOException {
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename));
		return properties;
	}

	/**
	 * Construct a file from it's name.
	 *
	 * @param name - the name of the file
	 * @return {@link File} object, never <code>null</code>
	 */
	static final File getLocalFile(final String name) {
		return FileUtils.getFile(name);
	}

}
