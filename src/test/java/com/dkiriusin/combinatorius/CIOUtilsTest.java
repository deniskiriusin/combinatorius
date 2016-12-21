/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.dkiriusin.combinatorius.CIOUtils;
import com.dkiriusin.combinatorius.CombinatoriusServlet;
import com.dkiriusin.combinatorius.Property;

@RunWith(MockitoJUnitRunner.class)
public class CIOUtilsTest {

	@Rule
	public TemporaryFolder tp = new TemporaryFolder();

	@Mock
	private ServletContext servletContext;

	private final Set<File> files = new LinkedHashSet<File>();
	private final Set<File> mocked_files = new LinkedHashSet<File>();

	@Before
	public void setUp()
			throws ServletException, URISyntaxException, IOException, InstantiationException, IllegalAccessException {
		mocked_files.addAll(TestUtils.getMockedFiles());

		tp.newFolder("css");
		File cssFolder = tp.newFolder("css/extra_css");
		File f1 = cssFolder.toPath().resolve("extra1.css").toFile();
		File f2 = cssFolder.toPath().resolve("extra2.css").toFile();
		TestUtils.writeTo(f1.getPath(), "body {background-color: white}");
		TestUtils.writeTo(f2.getPath(), "body {background-color: black}");
		files.addAll(new LinkedHashSet<File>(Arrays.asList(f1, f2)));
	}

	@Test
	public void testLastModifiedFile() {
		Assert.assertEquals(1335114000000L, CIOUtils.lastModifiedFile(mocked_files));
	}

	@Test
	public void testLastModifiedReturnsZeroIfFileCollectionIsEmpty() {
		final Set<File> files = new HashSet<File>();
		Assert.assertEquals(0L, CIOUtils.lastModifiedFile(files));
	}

	@Test
	public void testGzipContent() throws IOException {
		byte[] bytes = CIOUtils.gzipContent("12345678901234567890".getBytes());
		Assert.assertTrue(bytes.length == 32);
	}

	@Test(expected = NullPointerException.class)
	public void testGzipContentNeverReturnsNull() throws IOException {
		CIOUtils.gzipContent(null);
	}

	@Test
	public void testReadInputStream() throws IOException {
		InputStream input = new ByteArrayInputStream("test".getBytes(Charset.forName("UTF-8")));
		OutputStream output = new ByteArrayOutputStream();
		CIOUtils.readInputStream(input, output, true);
		Assert.assertTrue("The output should be 'test\n'", output.toString().equals("test\n"));
	}

	@Test
	public void testGetFilesByteArray() throws IOException {
		Assert.assertTrue(CIOUtils.getFilesByteArray(files).length > 20);
	}

	@Test
	public void testLoadPropertiesFromClasspath() throws IOException {
		Properties properties = new Properties();
		properties = CIOUtils.loadPropertiesFromClasspath(CombinatoriusServlet.propertiesFileName);
		final String cssDirPropertyName = Property.CSS_DIR.getName();
		Assert.assertTrue("Properties file should contain [" + cssDirPropertyName + "] key",
				properties.get(cssDirPropertyName) != null);
	}

	@Test
	public void testListFiles() throws IOException {
		final Set<String> set = Collections.singleton("css");
		final Collection<File> files = CIOUtils.getUnmodifiableCollectionOfFiles(tp.getRoot(), set, true);
		Assert.assertTrue("There should be two .css files in css directory", files.size() == 2);
	}

	@Test
	public void testMinifyCSS() throws UnsupportedEncodingException, IOException {
		final String css = "div { color: red; float: left; position: relative; }";
		final byte[] minified_css = CIOUtils.minifyCSS(css.getBytes("UTF-8"), -1);
		Assert.assertTrue("Initial CSS size should be 52", css.getBytes("UTF-8").length == 52);
		Assert.assertTrue("Minified CSS size should be 43", minified_css.length == 43);
	}

	@Test
	public void testMinifyJS() throws UnsupportedEncodingException, IOException {
		final String js = "/* comment to strip */ function toggle() { document.getElementById('elementID') } ";
		final byte[] minified_js = CIOUtils.minifyCSS(js.getBytes("UTF-8"), -1);
		Assert.assertTrue("Initial CSS size should be 82", js.getBytes("UTF-8").length == 82);
		Assert.assertTrue("Minified CSS size should be 55", minified_js.length == 55);
	}

	@Test
	public void testGetMD5() throws NoSuchAlgorithmException {
		final String s = "Combinatorius";
		final String hash1 = DigestUtils.md5Hex(s.getBytes());
		final String hash2 = CIOUtils.getMD5(s);
		Assert.assertTrue("Incorrect hash", hash1.equals(hash2));
	}

}
