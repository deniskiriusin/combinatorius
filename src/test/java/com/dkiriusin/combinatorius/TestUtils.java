/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.mockito.Mockito;

import com.dkiriusin.combinatorius.ComboServlet;

public class TestUtils {
	
	static final String LOCALHOST = "localhost";
	static final String EXPECTED_ETAG = "8effce7dc7eabc4dc38928f832ad2d28";
	static final String URL_RESOURCES = "extra_css/extra1.css,extra_css/extra2.css";
	static final String URL_NO_THEME = "/combo/&type=css&resources=" + URL_RESOURCES + "&v=16546544";
	static final String URL = "/combo/&type=css&resources=" + URL_RESOURCES + "&theme=blue&v=16546544";

	static final Set<File> getMockedFiles() {
		File mocked_file1 = Mockito.mock(File.class);
		// Sun 13 Nov 2011 12:14:12 GMT
		Mockito.when(mocked_file1.lastModified()).thenReturn(1321186452000L);
		Mockito.when(mocked_file1.getName()).thenReturn("mocked_file_1");
		Mockito.when(mocked_file1.getAbsolutePath()).thenReturn("mocked_file_1_AbsolutePath");

		File mocked_file2 = Mockito.mock(File.class);
		// Sun 22 Apr 2012 18:00:00 BST
		Mockito.when(mocked_file2.lastModified()).thenReturn(1335114000000L);
		Mockito.when(mocked_file2.getName()).thenReturn("mocked_file_2");
		Mockito.when(mocked_file2.getAbsolutePath()).thenReturn("mocked_file_2_AbsolutePath");

		// fill in a collection of mock files
		Set<File> mocked_files = new LinkedHashSet<File>();
		mocked_files.add(mocked_file1);
		mocked_files.add(mocked_file2);

		return mocked_files;
	}

	static final Properties loadTestProperties() throws URISyntaxException, FileNotFoundException, IOException {
		final URL testPropertiesURL = new URL(
				TestUtils.class.getResource(File.separator + ComboServlet.propertiesFileName).toString());
		final File file = new File(testPropertiesURL.toURI());
		final Properties properties = new Properties();
		properties.load(new FileInputStream(file));
		return properties;
	}

	static final void writeTo(String path, String content) throws IOException {
		Path target = Paths.get(path);
		if (Files.exists(target)) {
			throw new IOException("file already exists");
		}
		Files.copy(new ByteArrayInputStream(content.getBytes("UTF8")), target);
	}
}
